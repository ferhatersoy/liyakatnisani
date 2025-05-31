package com.example.liyakatnisani.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.liyakatnisani.MainActivity
import com.example.liyakatnisani.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.tietEmailLogin.text.toString().trim()
            val password = binding.tietPasswordLogin.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "E-posta ve şifre boş bırakılamaz.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Yükleniyor göstergesi eklenebilir (ProgressBar)

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Yükleniyor göstergesi gizlenebilir
                    if (task.isSuccessful) {
                        // Giriş başarılı
                        Log.d("LoginActivity", "signInWithEmail:success")
                        // val user = firebaseAuth.currentUser // Kullanıcı bilgisi gerekirse
                        // MainActivity'e yönlendir
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // Giriş başarısız
                        Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Giriş başarısız: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
