package com.stu3102.pesapapretail

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Shopping Cart"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        tvTotal = findViewById<TextView>(R.id.tvCartTotal)
        val btnCheckout = findViewById<MaterialButton>(R.id.btnCheckout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        updateCartView()

        btnCheckout.setOnClickListener {
            if (CartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, CheckoutActivity::class.java))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun updateCartView() {
        val items = CartManager.getCartItems()
        adapter = ProductAdapter(items) { product ->
            CartManager.removeFromCart(product)
            Toast.makeText(this, "${product.name} removed from cart", Toast.LENGTH_SHORT).show()
            updateCartView()
        }
        findViewById<RecyclerView>(R.id.recyclerViewCart).adapter = adapter
        tvTotal.text = "Total: KES ${CartManager.getTotalPrice()}"
    }
}