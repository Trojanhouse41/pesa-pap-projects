package com.stu3102.pesapapretail

object CartManager {
    private val cartItems = mutableListOf<Product>()

    fun addToCart(product: Product) {
        cartItems.add(product)
    }

    fun removeFromCart(product: Product) {
        cartItems.remove(product)
    }

    fun getCartItems(): List<Product> {
        return cartItems.toList()
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getTotalPrice(): Int {
        return cartItems.sumOf { it.price_kes }
    }
}