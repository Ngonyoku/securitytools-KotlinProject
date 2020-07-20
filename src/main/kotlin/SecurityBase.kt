import com.rsk.security.Argument
import java.io.*
import java.util.*

abstract class SecurityBase(protected val outputStrategy: OutputStrategy) {
    abstract fun run()

    private val overwrite: Boolean by Argument.argument()
    private val encode: Boolean by Argument.argument()

    @Throws(IOException::class)
    fun createOutputStream(fileName: String): OutputStream {
        return if (fileName.isEmpty()) {
            System.out
        } else {
            val file = File(fileName)

            val fileOutputStrem = if (file.exists()) {
                if (overwrite) {
                    FileOutputStream(file)
                } else {
                    throw IOException("Destination File Already Exists")
                }
            } else {
                FileOutputStream(file)
            }

            fileOutputStrem
        }
    }

    @Throws(FileNotFoundException::class)
    fun createInputStream(fileName: String): InputStream {
        return if (fileName.isEmpty()) {
            System.`in`
        } else {
            val f = File(fileName)
            if (f.exists()) {
                FileInputStream(f)
            } else {
                throw FileNotFoundException()
            }
        }
    }

    @Throws(IOException::class)
    fun writeBytes(os: OutputStream, bytes: ByteArray) {
        if (encode) {
            val temp: String
            val encoder = Base64.getEncoder()
            temp = encoder.encodeToString(bytes)
            os.write(temp.toByteArray())
        } else {
            os.write(bytes)
        }
        os.flush()
    }
}