package id.adiandrea.scanmath.feature.encryptedfile

import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import id.adiandrea.scanmath.App
import id.adiandrea.scanmath.model.History
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets

class EncryptedFileSystem(masterKey: MasterKey) {

    companion object {
        private const val FILE_NAME = "scanmath_file.txt"
    }

    private val encryptedFile = EncryptedFile.Builder(
        App.instance,
        File(App.instance.filesDir, FILE_NAME),
        masterKey,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    fun saveData(data: MutableList<History>) {
        val file = File(App.instance.filesDir, FILE_NAME)
        if (file.exists()) {
            file.delete()
        }

        encryptedFile.openFileOutput().apply {
            write(Gson().toJson(data).toByteArray(StandardCharsets.UTF_8))
            flush()
            close()
        }
    }

    fun getData(): MutableList<History> {
        var result = mutableListOf<History>()
        try {
            val bufferReader = encryptedFile.openFileInput().bufferedReader()
            val data = bufferReader.readText()
            if(data.isNotEmpty()){
                result = Gson().fromJson(data, Array<History>::class.java).toMutableList()
            }
            bufferReader.close()
        } catch (exception: IOException) {
            Timber.w(exception)
        }
        return result
    }
}