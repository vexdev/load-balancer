package balancer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import provider.DefaultProvider
import provider.Provider
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

internal abstract class LoadBalancerTest {

    @Test
    internal fun returnsAValidProvider() {
        val tested = createLoadBalancer()
        val provider1 = DefaultProvider()
        val provider2 = DefaultProvider()
        val providerKeys = listOf(provider1.key, provider2.key)

        tested.registerProvider(provider1)
        tested.registerProvider(provider2)

        assertTrue(providerKeys.contains(tested.get()))
    }

    @Test
    internal fun handlesParallelRequests() {
        val tested = createLoadBalancer()
        val blockingProvider = BlockingProvider()
        val countDownLatch = CountDownLatch(2)
        tested.registerProvider(blockingProvider)

        thread {
            tested.get()
            countDownLatch.countDown()
        }
        thread {
            tested.get()
            countDownLatch.countDown()
        }
        blockingProvider.blocking = false
        assertEquals(true, countDownLatch.await(3, TimeUnit.MILLISECONDS))
    }

    abstract fun createLoadBalancer(): LoadBalancer

    /**
     * Works as a latch, blocking all threads calling this method until a variable is released.
     */
    class BlockingProvider : Provider {
        override val key = UUID.randomUUID().toString()
        var blocking = true
        override fun get(): String {
            while (blocking) {
                Thread.sleep(1)
            }
            return key
        }
    }

}