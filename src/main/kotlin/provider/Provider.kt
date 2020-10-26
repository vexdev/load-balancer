package provider

interface Provider {

    /**
     * Uniquely identifies this provider instance.
     */
    val key: String

    /**
     * Performs an action and returns the identifier of this provider.
     */
    fun get(): String

}