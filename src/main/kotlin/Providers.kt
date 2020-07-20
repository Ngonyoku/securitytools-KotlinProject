import java.security.Provider
import java.security.Security

class Providers {
    fun run() {
        listAllProviders()
    }

    private fun listAllProviders() {
        getProviders().forEach {
            display(it)
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
}