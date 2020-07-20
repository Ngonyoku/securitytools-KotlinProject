import com.rsk.security.Argument
import com.rsk.security.Argument.argument
import java.security.Provider
import java.security.Security

class ProviderDetails(val providerName: String, val name: String)

class Providers(outputStrategy: OutputStrategy) : SecurityBase(outputStrategy) {

    private val filter: String by argument()

    class Help {
        fun help() {
            println("Providers: java SecurityToolsKt [-op 'providers'] [-d destfilename] [-p provider] [-a algorithm] [-o] [-encode]")
        }
    }

    override fun run() {
        listAllProviders()
    }

    private fun listAllProviders() {
        if (filter.isEmpty()) {
            getProviders().forEach {
                display(it)
            }
        } else {
            getFilteredProviders().forEach {
                println(display("${it.providerName} : ${it.name}"))
            }
        }

    }

    private fun display(provider: Provider) {
        outputStrategy.write(provider.name)
        outputStrategy.writeHeader()

        provider.entries.forEach { entry ->
            /*Print out the type of Algorithm provided to us*/
            outputStrategy.write("\t ${entry.key}, ${entry.value}")
        }

        outputStrategy.writeFooter()
    }

    private fun display(message: String) {
        outputStrategy.write(message)
    }

    private fun getProviders(): List<Provider> {
        val providers = Security.getProviders()
        val list: List<Provider> = providers.asList()
        return list
    }

    private fun getFilteredProviders(): List<ProviderDetails> {
        return Security.getProviders()
                .flatMap { provider ->
                    provider.entries
                            .filter { it -> it.key.toString().contains(filter, true) }
                            .map { ProviderDetails(provider.name, it.key.toString()) }
                }
    }
}