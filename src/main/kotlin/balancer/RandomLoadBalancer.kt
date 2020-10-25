package balancer

/**
 * Implements a load balancer by randomly calling a provider from the [ProviderRegistry].
 */
class RandomLoadBalancer : LoadBalancer, ProviderRegistry by SizeLimitedProviderRegistry() {

    override fun get(): String = getProviders().entries.random().value.get()

}