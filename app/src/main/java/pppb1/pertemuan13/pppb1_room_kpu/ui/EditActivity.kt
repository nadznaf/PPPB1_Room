package pppb1.pertemuan13.pppb1_room_kpu.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pppb1.pertemuan13.pppb1_room_kpu.R
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilih
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihDao
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihRoomDatabase
import pppb1.pertemuan13.pppb1_room_kpu.databinding.ActivityEditBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var dataPemilihDao: DataPemilihDao
    private lateinit var executorService: ExecutorService
    private var dataPemilihId: Int=0

    // Fungsi onCreate adalah titik awal untuk activity ini. Ini mengatur binding,
    // menyiapkan database, dan mengambil ID pemilih dari intent untuk digunakan di activity ini
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor() // Inisialisasi Executor untuk menjalankan operasi database di thread terpisah
        val db = DataPemilihRoomDatabase.getDatabase(this) // Mendapatkan instance database
        dataPemilihDao = db!!.dataPemilihDao()!! // Mengakses DataPemilihDao untuk operasi CRUD
        dataPemilihId = intent.getIntExtra("pemilih_id", 0) // Mendapatkan ID pemilih dari Intent yang dilewatkan ke Activity ini

        loadPemilihData() // Memuat data pemilih berdasarkan ID yang diberikan

        // Menambahkan listener pada tombol Simpan untuk menyimpan data yang telah diubah
        with(binding) {
            btnSimpan.setOnClickListener(View.OnClickListener {
                updatedata() // Memanggil fungsi untuk memperbarui data pemilih
            })
        }
    }

    // Fungsi untuk memperbarui data pemilih di database
    private fun updatedata() {
        val selectedGender = when (binding.rgGender.checkedRadioButtonId) {
            binding.rbPria.id -> "Pria" // Jika radio button "Pria" yang dipilih
            binding.rbWanita.id -> "Wanita" // Jika radio button "Wanita" yang dipilih
            else -> ""
        }
        val nama_pemilih = binding.namaEdit.text.toString()
        val nik = binding.nikEdit.text.toString()
        val gender = selectedGender
        val alamat = binding.alamatEdit.text.toString()

        // Validasi input sebelum menyimpan data
        if (validateInput(nama_pemilih, nik, alamat)) {
            lifecycleScope.launch(Dispatchers.IO) {
                // Menyimpan data pemilih yang sudah diperbarui ke database
                dataPemilihDao.update(
                    DataPemilih(
                        id = dataPemilihId,
                        nama_pemilih = nama_pemilih,
                        nik = nik,
                        gender = gender,
                        alamat = alamat
                    )
                )
                // Menampilkan pesan sukses setelah data berhasil diperbarui
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Menutup activity setelah data disimpan
                }
            }
        } else {
            // Menampilkan pesan error jika input belum lengkap
            Toast.makeText(this@EditActivity, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk memvalidasi apakah semua input sudah diisi
    private fun validateInput(namaPemilih: String, nik: String, alamat: String): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank() // Mengembalikan true jika semua field tidak kosong
    }

    // Fungsi untuk memuat data pemilih berdasarkan ID yang diberikan
    private fun loadPemilihData() {
        lifecycleScope.launch (Dispatchers.IO) {
            // Mengambil data pemilih dari database berdasarkan ID
            dataPemilihDao.getVoterById(dataPemilihId).collect { pemilih ->
                if (pemilih != null) {
                    // Jika data ditemukan, menampilkan data pemilih di UI
                    withContext(Dispatchers.Main) {
                        binding.namaEdit.setText(pemilih.nama_pemilih)
                        binding.nikEdit.setText(pemilih.nik)
                        // Mengatur radio button sesuai dengan gender yang dipilih
                        if (pemilih.gender == "Laki-laki") {
                            binding.rbPria.isChecked = true
                        } else {
                            binding.rbWanita.isChecked = true
                        }
                        binding.alamatEdit.setText(pemilih.alamat)
                    }
                }
            }
        }
    }
}
