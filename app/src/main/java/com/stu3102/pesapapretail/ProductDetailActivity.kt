package com.stu3102.pesapapretail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.button.MaterialButton

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Product Details"

        val ivImage = findViewById<ImageView>(R.id.ivDetailImage)
        val tvName = findViewById<TextView>(R.id.tvDetailName)
        val tvCategory = findViewById<TextView>(R.id.tvDetailCategory)
        val tvPrice = findViewById<TextView>(R.id.tvDetailPrice)
        val tvStock = findViewById<TextView>(R.id.tvDetailStock)
        val tvDescription = findViewById<TextView>(R.id.tvDetailDescription)
        val btnAddToCart = findViewById<MaterialButton>(R.id.btnAddToCart)

        val id = intent.getIntExtra("product_id", 0)
        val name = intent.getStringExtra("product_name") ?: ""
        val category = intent.getStringExtra("product_category") ?: ""
        val price = intent.getIntExtra("product_price", 0)
        val available = intent.getIntExtra("product_available", 0)
        val description = intent.getStringExtra("product_description") ?: ""
        val imageUrl = intent.getStringExtra("product_image")

        tvName.text = name
        tvCategory.text = category
        tvPrice.text = "KES $price"
        tvStock.text = "Available: $available"
        tvDescription.text = description

        val finalImage = if (!imageUrl.isNullOrEmpty()) {
            imageUrl
        } else {
            getMatchingImageUrl(name, category)
        }

        ivImage.load(finalImage) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        btnAddToCart.setOnClickListener {
            val product = Product(id, name, description, price, available, category, finalImage)
            CartManager.addToCart(product)
            Toast.makeText(this, "$name added to cart", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun getMatchingImageUrl(name: String, category: String): String {
        val query = (name + " " + category).lowercase()
        return when {
            query.contains("phone") || query.contains("iphone") || query.contains("samsung") -> "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500"
            query.contains("laptop") || query.contains("macbook") || query.contains("computer") -> "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500"
            query.contains("shoe") || query.contains("sneaker") || query.contains("nike") -> "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500"
            query.contains("headphone") || query.contains("audio") || query.contains("airpods") -> "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"
            query.contains("watch") || query.contains("smartwatch") -> "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500"
            query.contains("grocery") || query.contains("food") || query.contains("milk") -> "https://images.unsplash.com/photo-1542838132-92c53300491e?w=500"
            else -> "https://images.unsplash.com/photo-1526738549149-8e07eca6c147?w=500"
        }
    }
}