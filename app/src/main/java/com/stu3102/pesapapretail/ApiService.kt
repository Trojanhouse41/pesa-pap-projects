package com.stu3102.pesapapretail

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/products")
    suspend fun getProducts(): Response<List<Product>>

    @POST("api/orders/checkout")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body request: CheckoutRequest
    ): Response<CheckoutResponse>

    @GET("api/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String
    ): Response<List<Order>>

    @POST("api/products")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Response<Product>
}