package com.stu3102.pesapapretail

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price_kes: Int,
    val available: Int,
    val category: String,
    val image_url: String?
)

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val full_name: String, val email: String, val phone: String, val password: String)
data class AuthResponse(val token: String, val user: UserInfo)
data class UserInfo(val id: Int, val full_name: String, val email: String, val role: String)

data class Order(
    val order_id: Int,
    val order_date: String,
    val total_amount: Int,
    val order_status: String,
    val payment_status: String,
    val mpesa_receipt_number: String?
)

data class CheckoutRequest(
    val user_id: Int,
    val phone_number: String,
    val total_amount: Int,
    val items: List<CartItemRequest>
)

data class CartItemRequest(
    val product_id: Int,
    val quantity: Int,
    val unit_price: Int
)

data class CheckoutResponse(
    val success: Boolean,
    val order_reference: String,
    val message: String
)
