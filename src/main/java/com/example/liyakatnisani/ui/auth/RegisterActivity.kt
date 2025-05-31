package com.example.liyakatnisani.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.liyakatnisani.MainActivity
import com.example.liyakatnisani.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.tietEmailRegister.text.toString().trim()
            val password = binding.tietPasswordRegister.text.toString().trim()
            val confirmPassword = binding.tietConfirmPasswordRegister.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Tüm alanlar doldurulmalıdır.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Şifreler eşleşmiyor.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Şifre en az 6 karakter olmalıdır.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Yükleniyor göstergesi eklenebilir

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Yükleniyor göstergesi gizlenebilir
                    if (task.isSuccessful) {
                        // Kayıt başarılı
                        Log.d("RegisterActivity", "createUserWithEmail:success")
                        // val user = firebaseAuth.currentUser // Kullanıcı bilgisi gerekirse
                        Toast.makeText(this, "Kayıt başarılı.", Toast.LENGTH_SHORT).show()
                        // MainActivity'e yönlendir
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // Kayıt başarısız
                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Kayıt başarısız: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.tvGoToLogin.setOnClickListener {
            // Intent ile LoginActivity'e geçişte finish() çağrılmamalı,
            // kullanıcı geri gelmek isteyebilir. Ancak mevcut yapıda LoginActivity'den Register'a
            // geçişte de finish() yok, bu yüzden bir döngü olabilir.
            // Şimdilik bu şekilde bırakıyorum, idealde navigasyon component ile yönetilmeli
            // veya finish() çağrıları daha dikkatli yönetilmeli.
            // Önceki subtask'te tvGoToLogin içinde finish() vardı, bunu kaldırıyorum.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // finish() // Bu satırı yorumladım veya sildim.
        }
    }
}
