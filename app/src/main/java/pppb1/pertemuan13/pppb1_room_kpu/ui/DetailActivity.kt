package pppb1.pertemuan13.pppb1_room_kpu.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pppb1.pertemuan13.pppb1_room_kpu.R
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihDao
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihRoomDatabase
import pppb1.pertemuan13.pppb1_room_kpu.databinding.ActivityDetailBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mDataPemilihDao: DataPemilihDao
    private lateinit var executorService: ExecutorService
    private var dataPemilihId: Int=0

    // Fungsi onCreate berfungsi untuk mempersiapkan activity ini,
    // mengatur binding, mengakses database, dan mengambil ID pemilih dari intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor() // Inisialisasi Executor untuk operasi database
        val db = DataPemilihRoomDatabase.getDatabase(this) // Mendapatkan instance database
        mDataPemilihDao = db!!.dataPemilihDao()!! // Mengakses DataPemilihDao untuk operasi CRUD
        dataPemilihId = intent.getIntExtra("pemilih_id", 0) // Mendapatkan ID pemilih dari Intent

        loadPemilihData() // Memuat data pemilih berdasarkan ID yang diberikan

        // Menambahkan listener pada tombol Kembali untuk kembali ke MainActivity
        with(binding){
            btnKembali.setOnClickListener(){
                startActivity(Intent(this@DetailActivity,MainActivity::class.java)) // Navigasi kembali ke MainActivity
            }
        }
    }

    // Fungsi untuk memuat data pemilih berdasarkan ID yang diberikan
    private fun loadPemilihData() {
        lifecycleScope.launch (Dispatchers.IO) {
            // Mengambil data pemilih dari database berdasarkan ID
            mDataPemilihDao.getVoterById(dataPemilihId).collect { pemilih ->
                if (pemilih != null) {
                    // Jika data ditemukan, menampilkan data pemilih di UI
                    withContext(Dispatchers.Main) {
                        binding.namaDetail.setText(pemilih.nama_pemilih)
                        binding.nikDetail.setText(pemilih.nik)
                        binding.genderDetail.setText(pemilih.gender)
                        binding.alamatDetail.setText(pemilih.alamat)
                    }
                }
            }
        }
    }
}
