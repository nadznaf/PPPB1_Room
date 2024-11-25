package pppb1.pertemuan13.pppb1_room_kpu.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pppb1.pertemuan13.pppb1_room_kpu.PrefManager
import pppb1.pertemuan13.pppb1_room_kpu.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengakses PrefManager untuk menyimpan data pengguna
        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnRegis.setOnClickListener {
                val username = usernameRegis.text.toString()
                val password = passwordRegis.text.toString()
                val confirmPassword = confirmPasswordRegis.text.toString()

                // Validasi input
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(this@RegisterActivity, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Password tidak sama", Toast.LENGTH_SHORT).show()
                } else {
                    // Menyimpan data ke SharedPreferences
                    prefManager.saveUsername(username)
                    prefManager.savePassword(password)
                    prefManager.setLoggedIn(true)
                    checkLoginStatus()
                }
            }

            txtLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    // Mengecek status login dan berpindah ke MainActivity jika berhasil
    private fun checkLoginStatus() {
        if (prefManager.isLoggedIn()) {
            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show()
        }
    }
}
