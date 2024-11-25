package pppb1.pertemuan13.pppb1_room_kpu.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import pppb1.pertemuan13.pppb1_room_kpu.PemilihAdapter
import pppb1.pertemuan13.pppb1_room_kpu.PrefManager
import pppb1.pertemuan13.pppb1_room_kpu.R
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihDao
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihRoomDatabase
import pppb1.pertemuan13.pppb1_room_kpu.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    // Variabel untuk menghubungkan layout, mengelola preferensi login, dan akses database.
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private lateinit var mDataPemilihDao: DataPemilihDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Menghubungkan dengan layout XML.
        setContentView(binding.root)

        // Inisialisasi objek untuk mengelola preferensi login.
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus() // Memastikan user telah login.

        // Membuat thread executor untuk operasi database di latar belakang.
        executorService = Executors.newSingleThreadExecutor()

        // Menghubungkan database Room.
        val db = DataPemilihRoomDatabase.getDatabase(this)
        mDataPemilihDao = db!!.dataPemilihDao()!!

        // Mengatur aksi pada tombol.
        with(binding) {
            btnTambahdata.setOnClickListener {
                startActivity(Intent(this@MainActivity, InputActivity::class.java))
            }
            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false) // Mengubah status login.
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish() // Menutup aktivitas.
            }
        }
    }

    // Memeriksa status login user dan mengarahkan ke halaman login jika belum login.
    fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    // Menampilkan semua data dari database menggunakan RecyclerView.
    private fun getAllNotes() {
        with(binding) {
            rvPemilih.layoutManager = LinearLayoutManager(this@MainActivity) // Pengaturan layout vertikal.
            mDataPemilihDao.allDataPemilih.observe(this@MainActivity) { pemilih ->
                val adapter = PemilihAdapter(
                    pemilih,
                    onDeleteClick = { position ->
                        val note = (rvPemilih.adapter as PemilihAdapter).listPemilih[position]
                        executorService.execute { mDataPemilihDao.delete(note) } // Hapus data.
                    },
                    onEditClick = { position ->
                        val pemilih = (rvPemilih.adapter as PemilihAdapter).listPemilih[position]
                        val intent = Intent(this@MainActivity, EditActivity::class.java)
                        intent.putExtra("pemilih_id", pemilih.id) // Kirim ID untuk edit.
                        startActivity(intent)
                    },
                    onViewClick = { position ->
                        val pemilih = (rvPemilih.adapter as PemilihAdapter).listPemilih[position]
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra("pemilih_id", pemilih.id) // Kirim ID untuk melihat detail.
                        startActivity(intent)
                    }
                )
                rvPemilih.adapter = adapter // Pasang adapter ke RecyclerView.
            }
        }
    }

    // Memanggil data kembali saat aktivitas dilanjutkan.
    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}
