package balancer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import provider.DefaultProvider
import provider.Provider
import java.lang.RuntimeException
import java.util.*

internal class ExcludingHearthbeatCheckerTest {

    @Test
    internal fun workingProvidersAreNotExcluded() {
        val providerRegistry = SizeLimitedProviderRegistry()
        val tested = ExcludingHearthbeatChecker(providerRegistry)
        val provider1 = DefaultProvider()
        val provider2 = DefaultProvider()

        providerRegistry.registerProvider(provider1)
        providerRegistry.registerProvider(provider2)
        tested.check()

        assertEquals(setOf<String>(), providerRegistry.getExclusions())
    }

    @Test
    internal fun failingProvidersAreExcluded() {
        val providerRegistry = SizeLimitedProviderRegistry()
        val tested = ExcludingHearthbeatChecker(providerRegistry)
        val provider1 = FallableProvider()
        val provider2 = FallableProvider()

        providerRegistry.registerProvider(provider1)
        providerRegistry.registerProvider(provider2)
        provider1.fails = true
        tested.check()

        assertEquals(setOf(provider1.key), providerRegistry.getExclusions())
    }

    @Test
    internal fun providersKeepFailingStillExcluded() {
        val providerRegistry = SizeLimitedProviderRegistry()
        val tested = ExcludingHearthbeatChecker(providerRegistry)
        val provider1 = FallableProvider()
        val provider2 = FallableProvider()

        providerRegistry.registerProvider(provider1)
        providerRegistry.registerProvider(provider2)
        provider1.fails = true
        tested.check()
        tested.check()
        tested.check()

        assertEquals(setOf(provider1.key), providerRegistry.getExclusions())
    }

    @Test
    internal fun providersAreNotIncludedAfterOneSuccess() {
        val providerRegistry = SizeLimitedProviderRegistry()
        val tested = ExcludingHearthbeatChecker(providerRegistry)
        val provider1 = FallableProvider()
        val provider2 = FallableProvider()

        providerRegistry.registerProvider(provider1)
        providerRegistry.registerProvider(provider2)
        provider1.fails = true
        tested.check()
        tested.check()
        provider1.fails = false
        tested.check()

        assertEquals(setOf(provider1.key), providerRegistry.getExclusions())
    }

    @Test
    internal fun providersIncludedAfterTwoSuccesses() {
        val providerRegistry = SizeLimitedProviderRegistry()
        val tested = ExcludingHearthbeatChecker(providerRegistry)
        val provider1 = FallableProvider()
        val provider2 = FallableProvider()

        providerRegistry.registerProvider(provider1)
        providerRegistry.registerProvider(provider2)
        provider1.fails = true
        tested.check()
        tested.check()
        provider1.fails = false
        tested.check()
        tested.check()

        assertEquals(setOf<String>(), providerRegistry.getExclusions())
    }

    class FallableProvider : Provider {
        var fails: Boolean = false
        override val key: String = UUID.randomUUID().toString()
        override fun get(): String = if (fails) {
            throw RuntimeException("Something bad happened")
        } else {
            key
        }
    }

}