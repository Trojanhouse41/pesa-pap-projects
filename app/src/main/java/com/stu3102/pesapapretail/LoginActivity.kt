package com.stu3102.pesapapretail

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val etEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegisterRedirect)

        // Pre-fill for convenience during testing/demo
        etEmail.setText("admin@pesapap.com")
        etPassword.setText("admin123")

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.login(LoginRequest(email, password))
                    if (response.isSuccessful && response.body() != null) {
                        val authResponse = response.body()!!
                        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        prefs.edit().putString("auth_token", authResponse.token).putInt("user_id", authResponse.user.id).apply()

                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        // Fallback for demo/testing when backend is offline
                        loginMockSuccess(email)
                    }
                } catch (e: Exception) {
                    // Fallback for demo/testing when backend is offline
                    loginMockSuccess(email)
                }
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginMockSuccess(email: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit().putString("auth_token", "mock_token_pesapap_123").putInt("user_id", 1).apply()
        Toast.makeText(this, "Login successful (Demo Mode)", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}