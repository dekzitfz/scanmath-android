package id.adiandrea.scanmath.feature.encryptedfile

import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import id.adiandrea.scanmath.App
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

    fun saveData(data: String) {
        encryptedFile.openFileOutput().apply {
            write(data.toByteArray(StandardCharsets.UTF_8))
            flush()
            close()
        }
    }

    fun getData(): String {
        var result = ""
        try {
            val bufferReader = encryptedFile.openFileInput().bufferedReader()
            result = bufferReader.readText()
            bufferReader.close()
        } catch (exception: IOException) {
            Timber.w(exception)
        }
        return result
    }
}