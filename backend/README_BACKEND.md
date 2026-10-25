# PesaPap Retail Backend Server

This is the Node.js / Express backend server implementation for the PesaPap Retail e-commerce application, featuring Safaricom Daraja API M-Pesa STK Push integration and asynchronous callback handling.

## Setup Instructions

1. **Prerequisites:**
   - Install [Node.js](https://nodejs.org/) (v18 or higher recommended).

2. **Install Dependencies:**
   Navigate to the `backend` folder and run:
   ```bash
   npm install
   ```

3. **Configure Daraja Credentials (Optional for Sandbox Testing):**
   - Sign up on the [Safaricom Daraja Developer Portal](https://developer.safaricom.co.ke/).
   - Create an app to obtain your **Consumer Key** and **Consumer Secret**.
   - Update `server.js` with your credentials or set environment variables:
     ```bash
     export DARAJA_CONSUMER_KEY="your_consumer_key"
     export DARAJA_CONSUMER_SECRET="your_consumer_secret"
     ```

4. **Run the Server:**
   ```bash
   npm start
   ```
   The server will start on port `5000` (`http://localhost:5000`).

5. **Exposing Callback for Real STK Push Prompts (Using Ngrok):**
   - Install [Ngrok](https://ngrok.com/).
   - Run: `ngrok http 5000`
   - Copy the public HTTPS URL (e.g., `https://xxxx.ngrok-free.app`) and update `callbackUrl` in `server.js` to `https://xxxx.ngrok-free.app/api/callback`.
   - When you checkout in the Android app, an actual STK Push prompt will be sent to your Safaricom test phone number!
