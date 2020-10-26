package balancer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import provider.DefaultProvider

internal class SizeLimitedProviderRegistryTest {

    @Test
    internal fun limitsInsertions() {
        val tested = SizeLimitedProviderRegistry(3)
        var registered = 0

        if (tested.registerProvider(DefaultProvider())) registered++
        if (tested.registerProvider(DefaultProvider())) registered++
        if (tested.registerProvider(DefaultProvider())) registered++

        assertEquals(3, registered)
        assertFalse(tested.registerProvider(DefaultProvider()))
    }

    @Test
    internal fun removesProvidersAndAllowsFurtherRegistration() {
        val tested = SizeLimitedProviderRegistry(3)

        val provider = DefaultProvider()
        tested.registerProvider(DefaultProvider())
        tested.registerProvider(DefaultProvider())
        tested.registerProvider(provider)
        tested.dropProvider(provider.key)

        assertTrue(tested.registerProvider(DefaultProvider()))
    }

    @Test
    internal fun returnsExistingProvider() {
        val tested = SizeLimitedProviderRegistry(3)

        val provider = DefaultProvider()
        tested.registerProvider(DefaultProvider())
        tested.registerProvider(provider)

        assertEquals(provider, tested.getProvider(provider.get()))
    }

    @Test
    internal fun returnsListOfProviders() {
        val tested = SizeLimitedProviderRegistry(3)

        val provider = DefaultProvider()
        tested.registerProvider(DefaultProvider())
        tested.registerProvider(provider)

        assertTrue(tested.getProviders().containsKey(provider.get()))
    }

    @Test
    internal fun returnsACopyListOfProviders() {
        val tested = SizeLimitedProviderRegistry(3)

        val provider = DefaultProvider()
        tested.registerProvider(DefaultProvider())
        tested.registerProvider(provider)
        tested.getProviders().clear() // Clears the list

        assertTrue(tested.getProviders().containsKey(provider.get()))
    }

    @Test
    internal fun exclusionListIsSetAndReturned() {
        val tested = SizeLimitedProviderRegistry()
        val exclusions = setOf("a", "b", "c")

        tested.setExclusions(exclusions)

        assertEquals(exclusions, tested.getExclusions())
    }

    @Test
    internal fun exclusionListExcludesProviders() {
        val tested = SizeLimitedProviderRegistry(3)
        val provider1 = DefaultProvider()
        val provider2 = DefaultProvider()
        val provider3 = DefaultProvider()

        tested.registerProvider(provider1)
        tested.registerProvider(provider2)
        tested.registerProvider(provider3)
        tested.setExclusions(setOf(provider3.key))

        assertEquals(setOf(provider1, provider2), tested.getProviders().values.toSet())
    }
}