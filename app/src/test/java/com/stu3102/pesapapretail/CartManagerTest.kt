package com.stu3102.pesapapretail

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class CartManagerTest {

    @After
    fun tearDown() {
        CartManager.clearCart()
    }

    @Test
    fun testAddToCartAndTotal() {
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Desc",
            price_kes = 500,
            available = 10,
            category = "Electronics",
            image_url = null
        )

        CartManager.addToCart(product)
        assertEquals(1, CartManager.getCartItems().size)
        assertEquals(500, CartManager.getTotalPrice())

        CartManager.addToCart(product)
        assertEquals(2, CartManager.getCartItems().size)
        assertEquals(1000, CartManager.getTotalPrice())
    }
}