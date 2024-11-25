package pppb1.pertemuan13.pppb1_room_kpu.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity: Representasi tabel dalam database Room.
@Entity(tableName = "datapemilih_table") // Nama tabel di database
data class DataPemilih (
    // Primary Key: Kolom unik untuk setiap data, dengan nilai auto-generate.
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0, // Default ID dimulai dari 0, akan di-generate secara otomatis.

    // Kolom untuk menyimpan nama pemilih.
    @ColumnInfo(name = "nama_pemilih")
    val nama_pemilih: String,

    // Kolom untuk menyimpan NIK pemilih.
    @ColumnInfo(name = "nik")
    val nik: String,

    // Kolom untuk menyimpan gender pemilih.
    @ColumnInfo(name = "gender")
    val gender: String,

    // Kolom untuk menyimpan alamat pemilih.
    @ColumnInfo(name = "alamat")
    val alamat: String
)
