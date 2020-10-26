package balancer

/**
 * Implementation of [HeartbeatChecker] that excludes the providers who fail the liveness check from the given
 * [ProviderRegistry].
 */
class ExcludingHearthbeatChecker(private val providerRegistry: ProviderRegistry) : HeartbeatChecker {

    private val inclusionMap = mutableMapOf<String, Int>()

    @Synchronized
    override fun check() {
        val exclusions = providerRegistry.getExclusions().toMutableSet()
        // See if excluded providers are now working
        for (providerKey in exclusions) {
            try {
                providerRegistry.getProvider(providerKey)?.get() ?: exclusions.remove(providerKey)
                val successfulAttempts = inclusionMap.getOrDefault(providerKey, 0) + 1;
                inclusionMap[providerKey] = successfulAttempts
                if (successfulAttempts >= 2) {
                    inclusionMap.remove(providerKey)
                    exclusions.remove(providerKey)
                }
            } catch (_: Exception) {
                // Nothing to do, probably just log that the provider is still not working.
            }
        }
        // See if all included providers are working
        for (provider in providerRegistry.getProviders()) {
            try {
                provider.value.get()
            } catch (e: Exception) { // In reality you should be more specific, but this is a simulation.
                exclusions.add(provider.key)
                inclusionMap.remove(provider.key)
            }
        }
        // Propagate the list of exclusions
        providerRegistry.setExclusions(exclusions)
    }

}