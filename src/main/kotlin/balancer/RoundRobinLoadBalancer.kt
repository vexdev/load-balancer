package balancer

/**
 * Implements a load balancer by using a round robin approach over the [ProviderRegistry].
 */
class RoundRobinLoadBalancer : LoadBalancer,
    ProviderRegistry by SizeLimitedProviderRegistry(),
    HeartbeatChecker {
    val hearthbeatChecker = ExcludingHearthbeatChecker(this)
    val rateLimiter = ConstantRateLimiter(this)
    var position: Int = -1

    override fun get(): String {
        val providers = getProviders()
        if (providers.isEmpty()) throw NoSuchElementException("No providers are registered")
        position = (position + 1) % providers.size
        return providers.values.elementAt(position).let { rateLimiter.callWithLimit(it) }
    }

    override fun check() = hearthbeatChecker.check()

}