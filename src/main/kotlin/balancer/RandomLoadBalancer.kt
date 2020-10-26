package balancer

/**
 * Implements a load balancer by randomly calling a provider from the [ProviderRegistry].
 */
class RandomLoadBalancer : LoadBalancer,
    ProviderRegistry by SizeLimitedProviderRegistry(),
    HeartbeatChecker {
    val hearthbeatChecker = ExcludingHearthbeatChecker(this)
    val rateLimiter = ConstantRateLimiter(this)

    override fun get(): String = getProviders().entries.random().value.let { rateLimiter.callWithLimit(it) }
    override fun check() = hearthbeatChecker.check()

}