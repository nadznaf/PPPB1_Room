package pppb1.pertemuan13.pppb1_room_kpu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

// RoomDatabase: Kelas abstrak untuk mengelola database.
@Database(entities = [DataPemilih::class], version = 1, exportSchema = false)
abstract class DataPemilihRoomDatabase : RoomDatabase() {

    // Fungsi abstrak untuk mendapatkan instance DAO.
    abstract fun dataPemilihDao(): DataPemilihDao?

    companion object {
        // Volatile: Variabel ini langsung terlihat oleh thread lain ketika berubah.
        @Volatile
        private var INSTANCE: DataPemilihRoomDatabase? = null

        // Singleton Pattern: Mengembalikan instance database yang sama jika sudah ada.
        fun getDatabase(context: Context): DataPemilihRoomDatabase? {
            // Jika INSTANCE masih null, buat database baru.
            if (INSTANCE == null) {
                synchronized(DataPemilihRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext, // Konteks aplikasi.
                        DataPemilihRoomDatabase::class.java, // Kelas database.
                        "pemilih_database" // Nama file database.
                    ).build()
                }
            }
            // Kembalikan instance database.
            return INSTANCE
        }
    }
}
