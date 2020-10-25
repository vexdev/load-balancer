package provider

interface Provider {

    /**
     * Uniquely identifies this provider instance.
     */
    fun get(): String

}