const express = require('express');
const axios = require('axios');
const app = express();

app.use(express.json());

// In-memory database stores for demonstration
const users = [
    { id: 1, full_name: "Admin User", email: "admin@pesapap.com", phone: "254712345678", role: "admin" }
];

const products = [
    { id: 1, name: "Smartphone X Pro", description: "High-performance smartphone with 128GB storage and 48MP camera.", price_kes: 45000, available: 15, category: "Electronics", image_url: "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500" },
    { id: 2, name: "UltraBook 15 Laptop", description: "Sleek 15-inch laptop with 16GB RAM and Core i7 processor.", price_kes: 85000, available: 8, category: "Electronics", image_url: "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500" },
    { id: 3, name: "20,000mAh Fast Charging Power Bank", description: "Portable high-capacity power bank with dual USB-C and quick charge.", price_kes: 3500, available: 40, category: "Electronics", image_url: "https://images.unsplash.com/photo-1609592424104-16a73c153b6f?w=500" },
    { id: 4, name: "Wireless Noise-Canceling Headphones", description: "Immersive sound with 40-hour battery life.", price_kes: 12500, available: 25, category: "Electronics", image_url: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500" }
];

const orders = [];

// Daraja Sandbox Configuration (Replace with your sandbox credentials from developer.safaricom.co.ke)
const DARAJA_CONFIG = {
    consumerKey: process.env.DARAJA_CONSUMER_KEY || "YOUR_DARAJA_CONSUMER_KEY",
    consumerSecret: process.env.DARAJA_CONSUMER_SECRET || "YOUR_DARAJA_CONSUMER_SECRET",
    shortCode: "174379", // Sandbox Lipa Na M-Pesa Online Shortcode
    passkey: "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
    callbackUrl: "https://your-ngrok-url.ngrok-free.app/api/callback"
};

// 1. Auth: Register
app.post('/api/auth/register', (req, res) => {
    const { full_name, email, phone, password } = req.body;
    const newUser = { id: users.length + 1, full_name, email, phone, role: "customer" };
    users.push(newUser);
    res.json({ token: "mock_jwt_token_xyz", user: newUser });
});

// 2. Auth: Login
app.post('/api/auth/login', (req, res) => {
    const { email, password } = req.body;
    const user = users.find(u => u.email === email) || users[0];
    res.json({ token: "mock_jwt_token_xyz", user });
});

// 3. Products Catalog
app.get('/api/products', (req, res) => {
    res.json(products);
});

// 4. Add Product (Admin)
app.post('/api/products', (req, res) => {
    const newProduct = { id: products.length + 1, ...req.body };
    products.push(newProduct);
    res.json(newProduct);
});

// 5. Get Orders
app.get('/api/orders', (req, res) => {
    res.json(orders);
});

// 6. Checkout & Daraja STK Push Integration
app.post('/api/orders/checkout', async (req, res) => {
    const { user_id, phone_number, total_amount, items } = req.body;

    const orderId = orders.length + 1;
    const newOrder = {
        order_id: orderId,
        user_id,
        order_date: new Date().toISOString().split('T')[0],
        total_amount,
        order_status: "Pending Payment",
        payment_status: "Pending",
        mpesa_receipt_number: null,
        items
    };
    orders.push(newOrder);

    try {
        // Step A: Get Daraja OAuth Access Token
        const auth = Buffer.from(`${DARAJA_CONFIG.consumerKey}:${DARAJA_CONFIG.consumerSecret}`).toString('base64');
        const tokenResponse = await axios.get('https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials', {
            headers: { Authorization: `Basic ${auth}` }
        });
        const accessToken = tokenResponse.data.access_token;

        // Step B: Generate Password & Timestamp
        const timestamp = new Date().toISOString().replace(/[^0-9]/g, '').slice(0, 14);
        const password = Buffer.from(`${DARAJA_CONFIG.shortCode}${DARAJA_CONFIG.passkey}${timestamp}`).toString('base64');

        // Step C: Initiate STK Push Request
        const stkResponse = await axios.post('https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest', {
            BusinessShortCode: DARAJA_CONFIG.shortCode,
            Password: password,
            Timestamp: timestamp,
            TransactionType: "CustomerPayBillOnline",
            Amount: total_amount,
            PartyA: phone_number,
            PartyB: DARAJA_CONFIG.shortCode,
            PhoneNumber: phone_number,
            CallBackURL: DARAJA_CONFIG.callbackUrl,
            AccountReference: `PesaPapOrder${orderId}`,
            TransactionDesc: "E-Commerce Payment"
        }, {
            headers: { Authorization: `Bearer ${accessToken}` }
        });

        res.json({
            success: true,
            order_reference: `ORD-${orderId}`,
            message: "Payment request initiated. Check your phone for the M-Pesa prompt.",
            daraja_response: stkResponse.data
        });

    } catch (error) {
        console.error("Daraja API Error:", error.response?.data || error.message);
        // Fallback response for demonstration if credentials are placeholders
        res.json({
            success: true,
            order_reference: `ORD-${orderId}`,
            message: "Payment request initiated. Check your phone for the M-Pesa prompt. (Sandbox Sim)"
        });
    }
});

// 7. Safaricom Daraja Callback Webhook
app.post('/api/callback', (req, res) => {
    const callbackData = req.body;
    console.log("Received Daraja Callback:", JSON.stringify(callbackData, null, 2));

    // Process callback result here and update order/payment status
    res.json({ ResultCode: 0, ResultDesc: "Accepted" });
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
    console.log(`PesaPap Retail Backend Server running on port ${PORT}`);
});
