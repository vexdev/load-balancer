package balancer

import provider.Provider

/**
 * Holds a list of [Provider]s and provides method to register or delete them.
 */
interface ProviderRegistry {

    fun registerProvider(provider: Provider): Boolean

    fun getProvider(providerId: String): Provider?

    fun dropProvider(providerId: String)

    fun getProviders(): LinkedHashMap<String, Provider>

}