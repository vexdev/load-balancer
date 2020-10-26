package balancer

interface HeartbeatChecker {

    /**
     * Checks the liveness status of the connected providers.
     */
    fun check()

}