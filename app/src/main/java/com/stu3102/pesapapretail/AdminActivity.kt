package com.stu3102.pesapapretail

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Admin Inventory"

        val etName = findViewById<EditText>(R.id.etAdminName)
        val etDesc = findViewById<EditText>(R.id.etAdminDesc)
        val etPrice = findViewById<EditText>(R.id.etAdminPrice)
        val etStock = findViewById<EditText>(R.id.etAdminStock)
        val etCategory = findViewById<EditText>(R.id.etAdminCategory)
        val etImage = findViewById<EditText>(R.id.etAdminImage)
        val btnAdd = findViewById<MaterialButton>(R.id.btnAddProduct)

        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val stockStr = etStock.text.toString().trim()
            val category = etCategory.text.toString().trim()
            val imageUrl = etImage.text.toString().trim().takeIf { it.isNotEmpty() }

            if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.toIntOrNull() ?: 0
            val stock = stockStr.toIntOrNull() ?: 0

            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val token = prefs.getString("auth_token", "") ?: ""

            lifecycleScope.launch {
                try {
                    val newProduct = Product(
                        id = 0,
                        name = name,
                        description = desc,
                        price_kes = price,
                        available = stock,
                        category = category,
                        image_url = imageUrl
                    )
                    val response = RetrofitClient.apiService.addProduct("Bearer $token", newProduct)
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminActivity, "Product added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AdminActivity, "Failed to add product", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AdminActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}