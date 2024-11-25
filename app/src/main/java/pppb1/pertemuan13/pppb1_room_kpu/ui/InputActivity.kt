package pppb1.pertemuan13.pppb1_room_kpu.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.pertemuan13.pppb1_room_kpu.R
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilih
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihDao
import pppb1.pertemuan13.pppb1_room_kpu.database.DataPemilihRoomDatabase
import pppb1.pertemuan13.pppb1_room_kpu.databinding.ActivityInputBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private lateinit var mDataPemilihDao: DataPemilihDao //dao untuk akses database
    private lateinit var executorService: ExecutorService //eksekutor untuk background tasks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor() //initialize the executor

        // Initialize database and DAO
        val db = DataPemilihRoomDatabase.getDatabase(this)
        mDataPemilihDao = db!!.dataPemilihDao()!!

        // Set onClickListener for the "Tambah" button to insert new data
        with(binding) {
            btnTambah.setOnClickListener(View.OnClickListener {
                val nama_pemilih = binding.namaInput.text.toString()
                val nik = binding.nikInput.text.toString()
                val alamat = binding.alamatInput.text.toString()
                val selectedGender = when (binding.rgGender.checkedRadioButtonId) {
                    binding.rbPria.id -> "Pria"
                    binding.rbWanita.id -> "Wanita"
                    else -> ""
                }

                //validasi input
                if (validateInput(nama_pemilih, nik, alamat)) {
                    insert(
                        DataPemilih(
                            nama_pemilih = nama_pemilih,
                            nik = nik,
                            gender = selectedGender,
                            alamat = alamat
                        )
                    )
                    startActivity(Intent(this@InputActivity,MainActivity::class.java)) //navigasi ke main activity

                }else{
                    Toast.makeText(this@InputActivity, "Isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
                setEmptyField() //clear input fields
            })
        }
    }

    // validasi input pastikan tidak kosong
    private fun validateInput(namaPemilih: String, nik: String, alamat: String): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank()
    }

    // insert data objek ke database dalam background thread
    private fun insert(dataPemilih: DataPemilih) {
        executorService.execute { mDataPemilihDao.insert(dataPemilih) }
    }

    // clear the input fields after submission
    private fun setEmptyField() {
        with(binding){
            namaInput.setText("")
            nikInput.setText("")
            alamatInput.setText("")
        }
    }

}