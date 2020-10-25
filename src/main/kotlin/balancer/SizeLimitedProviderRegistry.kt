package balancer

import provider.Provider

/**
 * [ProviderRegistry] implementation limited in size based on the limit passed in the constructor, defaults to 10.
 */
class SizeLimitedProviderRegistry(private val sizeLimit: Int = 10) : ProviderRegistry {

    private val registry = LinkedHashMap<String, Provider>(sizeLimit)

    @Synchronized
    override fun registerProvider(provider: Provider): Boolean {
        if (registry.size >= sizeLimit) return false
        return registry.putIfAbsent(provider.get(), provider) == null
    }

    @Synchronized
    override fun dropProvider(providerId: String) {
        registry.remove(providerId)
    }

    @Synchronized
    override fun getProvider(providerId: String): Provider? = registry[providerId]

    @Synchronized
    override fun getProviders() = LinkedHashMap(registry)

}