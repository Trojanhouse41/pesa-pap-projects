package com.stu3102.pesapapretail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class OrderHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Order History"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = prefs.getString("auth_token", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getOrders("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val orders = response.body()!!
                    recyclerView.adapter = OrderAdapter(orders)
                } else {
                    Toast.makeText(this@OrderHistoryActivity, "Failed to load orders", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OrderHistoryActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}