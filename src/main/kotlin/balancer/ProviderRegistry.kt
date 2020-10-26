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

    /**
     * Providers can be excluded by their Provider ID. If excluded they are still retained in the registry but they will
     * not be returned from the [getProviders] method.
     */
    fun setExclusions(exclusions: Set<String>)

    fun getExclusions(): Set<String>

}