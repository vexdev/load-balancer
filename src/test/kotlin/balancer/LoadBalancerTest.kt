package balancer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import provider.DefaultProvider

internal abstract class LoadBalancerTest {

    @Test
    internal fun returnsAValidProvider() {
        val tested = createLoadBalancer()
        val provider1 = DefaultProvider()
        val provider2 = DefaultProvider()
        val providerKeys = listOf(provider1.get(), provider2.get())

        tested.registerProvider(provider1)
        tested.registerProvider(provider2)

        assertTrue(providerKeys.contains(tested.get()))
    }

    abstract fun createLoadBalancer(): LoadBalancer

}