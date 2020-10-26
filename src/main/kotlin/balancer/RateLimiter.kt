package balancer

import provider.Provider

/**
 * Limits the rate of requests that are going trough a [LoadBalancer].
 */
interface RateLimiter {

    fun callWithLimit(provider: Provider): String

}