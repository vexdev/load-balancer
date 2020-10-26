package balancer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import provider.DefaultProvider

internal class RoundRobinLoadBalancerTest : LoadBalancerTest() {
    override fun createLoadBalancer(): LoadBalancer = RoundRobinLoadBalancer()

    @Test
    internal fun returnsAllPossibleProviders() {
        val tested = RoundRobinLoadBalancer()
        val provider1 = DefaultProvider()
        val provider2 = DefaultProvider()
        val provider3 = DefaultProvider()

        tested.registerProvider(provider1)
        tested.registerProvider(provider2)
        tested.registerProvider(provider3)

        assertEquals(provider1.get(), tested.get())
        assertEquals(provider2.get(), tested.get())
        assertEquals(provider3.get(), tested.get())
        assertEquals(provider1.get(), tested.get())
    }

    @Test
    internal fun loopsOverIfProviderIsRemoved() {
        val tested = RoundRobinLoadBalancer()
        val provider1 = DefaultProvider()
        val provider2 = DefaultProvider()
        val provider3 = DefaultProvider()

        tested.registerProvider(provider1)
        tested.registerProvider(provider2)
        tested.registerProvider(provider3)
        tested.get()
        tested.get()
        tested.dropProvider(provider3.get())

        assertEquals(provider1.get(), tested.get())
    }
}