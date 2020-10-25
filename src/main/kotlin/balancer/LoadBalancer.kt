package balancer

interface LoadBalancer : ProviderRegistry {

    /**
     * Invokes the get of one of the registered providers.
     */
    fun get(): String

}