package balancer

import java.util.concurrent.atomic.AtomicInteger

/**
 * Implements a load balancer by using a round robin approach over the [ProviderRegistry].
 */
class RoundRobinLoadBalancer : LoadBalancer, ProviderRegistry by SizeLimitedProviderRegistry() {
    var position: AtomicInteger = AtomicInteger(0)

    @Synchronized
    override fun get(): String {
        val providers = getProviders()
        return providers[position.getAndUpdate { it + 1 % providers.size }]!!.get()
    }

}