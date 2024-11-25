package pppb1.pertemuan13.pppb1_room_kpu.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.pertemuan13.pppb1_room_kpu.PrefManager
import pppb1.pertemuan13.pppb1_room_kpu.R
import pppb1.pertemuan13.pppb1_room_kpu.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    // Variabel untuk menghubungkan layout dan mengelola preferensi.
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater) // Menghubungkan dengan layout XML.
        setContentView(binding.root)

        // Inisialisasi PrefManager untuk mengelola data login.
        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnLogin.setOnClickListener {
                val username = usernameLogin.text.toString()
                val password = passwordLogin.text.toString()

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true) // Mengubah status login.
                        checkLoginStatus() // Periksa status login.
                    } else {
                        Toast.makeText(this@LoginActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Aksi untuk berpindah ke halaman registrasi.
            txtRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    // Validasi username dan password yang dimasukkan user.
    private fun isValidUsernamePassword(): Boolean {
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.usernameLogin.text.toString()
        val inputPassword = binding.passwordLogin.text.toString()
        return username == inputUsername && password == inputPassword
    }

    // Memeriksa status login user.
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish() // Menutup aktivitas login.
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }
}
