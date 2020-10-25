package provider

import java.util.*

/**
 * Default provider implementation making use of a random number generator to provide a (mostly) unique identifier.
 */
class DefaultProvider : Provider {

    override fun get(): String =
        UUID.randomUUID().toString().take(7)

}