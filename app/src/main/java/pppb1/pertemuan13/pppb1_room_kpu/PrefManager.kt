package pppb1.pertemuan13.pppb1_room_kpu

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {
    // Menggunakan SharedPreferences untuk menyimpan data seperti status login, username, dan password
    private val sharedPreferences: SharedPreferences

    companion object {
        // Nama file SharedPreferences dan kunci untuk data yang disimpan
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        // Singleton instance untuk memastikan hanya ada satu instance PrefManager
        @Volatile
        private var instance: PrefManager? = null

        // Mengembalikan instance PrefManager, membuatnya jika belum ada
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        // Inisialisasi SharedPreferences dengan nama file yang ditentukan
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    // Menyimpan status login
    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    // Mengambil status login
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Menyimpan username
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username).apply()
    }

    // Menyimpan password
    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password).apply()
    }

    // Mengambil username
    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "") ?: ""
    }

    // Mengambil password
    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }

    // Membersihkan semua data yang tersimpan
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
