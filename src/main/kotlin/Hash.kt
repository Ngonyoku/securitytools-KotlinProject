import com.rsk.security.Argument.argument
import java.io.*
import java.lang.IllegalArgumentException
import java.security.MessageDigest

class Hash(outputStrategy: OutputStrategy) : SecurityBase(outputStrategy) {

    private val algorithm: String by argument()
    private val filename: String by argument()
    private val digestFilename: String by argument()
    private val provider: String by argument()

    class Help {
        fun help() {
            println("usage: java Hash -op 'hash' [-f filename] [-d destfilename] [-p provider] [-a algorithm] [-o] [-encode]")
            println("\tf filename\t: read input data from filename")
            println("\td destfilename\t: write output hash to destfilename")
            println("\tp provider\t: use specific provider")
            println("\ta algorithm\t: algorithm to use for digest")
            println("\to\t\t: overwrite destfilename file")
            println("\te\t\t: Base64 encode output")
        }
    }

    init {
        if (algorithm.isEmpty()) throw IllegalArgumentException()
    }

    override fun run() {
        val md = createDigestInstance(algorithm, provider)

        createInputStream(filename).use { input ->
            createOutputStream(digestFilename).use { output ->
                val hashedBytes = digestData(md, input)
                writeBytes(output, hashedBytes)
            }
        }
    }

    private fun digestData(md: MessageDigest, input: InputStream): ByteArray {
        val bytesToHash = input.readBytes()
        md.update(bytesToHash)
        return md.digest()
    }

    private fun createDigestInstance(algorithm: String, provider: String?): MessageDigest {
        return if (provider.isNullOrEmpty()) {
            MessageDigest.getInstance(algorithm)
        } else {
            MessageDigest.getInstance(algorithm, provider)
        }
    }
}