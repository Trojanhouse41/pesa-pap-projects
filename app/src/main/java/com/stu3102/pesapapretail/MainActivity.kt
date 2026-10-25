package com.stu3102.pesapapretail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: ProductAdapter
    private var allProducts = listOf<Product>()
    private var currentThemeMode = 0 // 0: Light, 1: Dark, 2: High Contrast B&W

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check auth token
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        if (token == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize immediately with sample products so they show up instantly
        allProducts = getSampleProducts()

        adapter = ProductAdapter(allProducts) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("product_id", product.id)
                putExtra("product_name", product.name)
                putExtra("product_category", product.category)
                putExtra("product_price", product.price_kes)
                putExtra("product_available", product.available)
                putExtra("product_description", product.description)
                putExtra("product_image", product.image_url)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val btnAiPrompt = findViewById<Button>(R.id.btnAiPrompt)
        val btnThemeToggle = findViewById<Button>(R.id.btnThemeToggle)
        val btnOrders = findViewById<Button>(R.id.btnOrders)
        val btnAdmin = findViewById<Button>(R.id.btnAdmin)
        val btnCart = findViewById<Button>(R.id.btnCart)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val searchView = findViewById<SearchView>(R.id.searchView)

        swipeRefreshLayout.setOnRefreshListener {
            fetchProducts()
        }

        btnAiPrompt.setOnClickListener {
            showAiPromptDialog()
        }

        btnThemeToggle.setOnClickListener {
            toggleTheme()
        }

        btnOrders.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        btnAdmin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        btnLogout.setOnClickListener {
            prefs.edit().remove("auth_token").apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterProducts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText)
                return true
            }
        })

        fetchProducts()
    }

    private fun fetchProducts() {
        swipeRefreshLayout.isRefreshing = true
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getProducts()
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    allProducts = response.body()!!
                    adapter.updateList(allProducts)
                }
            } catch (_: Exception) {
                // Keep sample products if offline
            } finally {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun getSampleProducts(): List<Product> {
        return listOf(
            Product(1, "Smartphone X Pro", "High-performance smartphone with 128GB storage and 48MP camera.", 45000, 15, "Electronics", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500"),
            Product(2, "UltraBook 15 Laptop", "Sleek 15-inch laptop with 16GB RAM and Core i7 processor.", 85000, 8, "Electronics", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500"),
            Product(3, "20,000mAh Fast Charging Power Bank", "Portable high-capacity power bank with dual USB-C and quick charge.", 3500, 40, "Electronics", "https://images.unsplash.com/photo-1609592424104-16a73c153b6f?w=500"),
            Product(4, "Wireless Noise-Canceling Headphones", "Immersive sound with 40-hour battery life.", 12500, 25, "Electronics", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"),
            Product(5, "Smart Fitness Watch", "Tracks heart rate, sleep, and workouts with GPS.", 8900, 30, "Electronics", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500"),
            Product(6, "Nike Air Max Sneakers", "Comfortable running shoes designed for daily wear.", 14000, 12, "Clothing", "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500"),
            Product(7, "Smart 4K Ultra HD TV 55\"", "Stunning 4K resolution with HDR and smart streaming apps.", 55000, 6, "Electronics", "https://images.unsplash.com/photo-1593784991095-a205069470b6?w=500"),
            Product(8, "Organic Fresh Milk (1L)", "Farm fresh pasteurized cow milk.", 180, 50, "Groceries", "https://images.unsplash.com/photo-1542838132-92c53300491e?w=500")
        )
    }

    private fun filterProducts(query: String?) {
        val filtered = if (query.isNullOrBlank()) {
            allProducts
        } else {
            allProducts.filter {
                it.name.contains(query, true) || it.category.contains(query, true)
            }
        }
        adapter.updateList(filtered)
    }

    private fun toggleTheme() {
        currentThemeMode = (currentThemeMode + 1) % 3
        when (currentThemeMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Toast.makeText(this, "Theme: Light Mode", Toast.LENGTH_SHORT).show()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Toast.makeText(this, "Theme: Dark Mode", Toast.LENGTH_SHORT).show()
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Toast.makeText(this, "Theme: High-Contrast Black & White", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAiPromptDialog() {
        val input = EditText(this)
        input.hint = "e.g., 'Find cheap electronics under 5000 KES'"

        AlertDialog.Builder(this)
            .setTitle("✨ AI Product Assistant")
            .setMessage("Ask AI to find or recommend products for you using natural language:")
            .setView(input)
            .setPositiveButton("Ask AI") { _, _ ->
                val prompt = input.text.toString().lowercase()
                processAiPrompt(prompt)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun processAiPrompt(prompt: String) {
        val filtered = allProducts.filter { product ->
            when {
                prompt.contains("cheap") || prompt.contains("budget") -> product.price_kes < 5000
                prompt.contains("electronic") -> product.category.equals("electronics", true)
                prompt.contains("clothing") || prompt.contains("fashion") -> product.category.equals("clothing", true)
                prompt.contains("grocery") || prompt.contains("food") -> product.category.equals("groceries", true)
                prompt.contains("under") -> {
                    val numbers = prompt.replace(Regex("[^0-9]"), " ").trim().split(Regex("\\s+"))
                    if (numbers.isNotEmpty() && numbers[0].toIntOrNull() != null) {
                        product.price_kes <= numbers[0].toInt()
                    } else true
                }
                else -> product.name.contains(prompt, true) || product.category.contains(prompt, true)
            }
        }
        adapter.updateList(filtered)
        Toast.makeText(this, "AI found ${filtered.size} matching products", Toast.LENGTH_LONG).show()
    }
}