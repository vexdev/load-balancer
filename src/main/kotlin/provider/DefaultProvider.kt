package provider

import java.util.*

/**
 * Default provider implementation making use of a random number generator to provide a (mostly) unique identifier.
 */
class DefaultProvider : Provider {
    override val key: String = UUID.randomUUID().toString().take(7)

    override fun get(): String = key
}