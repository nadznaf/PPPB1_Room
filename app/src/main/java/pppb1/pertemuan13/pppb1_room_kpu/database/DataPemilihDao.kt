package pppb1.pertemuan13.pppb1_room_kpu.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// DAO: Interface untuk mengelola operasi database (CRUD).
@Dao
interface DataPemilihDao {

    // Fungsi untuk menyisipkan data ke dalam tabel.
    // Jika data dengan ID yang sama sudah ada, operasi ini akan diabaikan.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dataPemilih: DataPemilih)

    // Fungsi untuk memperbarui data yang sudah ada di database.
    @Update
    fun update(dataPemilih: DataPemilih)

    // Fungsi untuk menghapus data dari database.
    @Delete
    fun delete(dataPemilih: DataPemilih)

    // Query untuk mendapatkan semua data dalam tabel, diurutkan berdasarkan ID secara ascending.
    // LiveData digunakan untuk memantau perubahan data secara real-time.
    @get:Query("SELECT * from datapemilih_table ORDER BY id ASC")
    val allDataPemilih: LiveData<List<DataPemilih>>

    // Query untuk mengambil data spesifik berdasarkan ID.
    // Flow digunakan agar data dapat diakses secara asinkron dan reaktif.
    @Query("SELECT * FROM datapemilih_table WHERE id = :id")
    fun getVoterById(id: Int): Flow<DataPemilih>
}
