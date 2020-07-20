import com.rsk.security.Argument
import com.rsk.security.Argument.argument
import java.security.Provider
import java.security.Security

class ProviderDetails(val providerName: String, val name: String)

class Providers {

    private val filter: String by argument()

    fun run() {
        listAllProviders()
    }

    private fun listAllProviders() {
        if (filter.isEmpty()) {
            getProviders().forEach {
                display(it)
            }
        } else {
            getFilteredProviders().forEach {
                println(println("${it.providerName} : ${it.name}"))
            }
        }

    }

    private fun display(provider: Provider) {
        println(provider.name)
        println("-----------------------------------------------------------------------------------------------------")

        provider.entries.forEach { entry ->
            println("\t ${entry.key}, ${entry.value}") /*Print out the type of Algorithm provided to us*/
        }

        println("-----------------------------------------------------------------------------------------------------")
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