package balancer

import provider.Provider
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Limits the maximum number of requests going trough a [LoadBalancer] based on the [requestsPerProvider] parameter.
 */
class ConstantRateLimiter(
    private val providerRegistry: ProviderRegistry,
    private val requestsPerProvider: Int = 3
) : RateLimiter {
    private val ongoingRequests = AtomicInteger(0)

    override fun callWithLimit(provider: Provider): String {
        synchronized(this) {
            if (ongoingRequests.get() >= requestsPerProvider * providerRegistry.getProviders().size)
                throw MaxRequestsExceededException()
            else {
                ongoingRequests.incrementAndGet()
            }
        }
        try {
            return provider.get()
        } finally {
            ongoingRequests.decrementAndGet()
        }
    }

    class MaxRequestsExceededException : RuntimeException("The maximum number of requests was exceeded")

}