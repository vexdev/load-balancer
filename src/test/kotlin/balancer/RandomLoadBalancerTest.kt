package balancer

internal class RandomLoadBalancerTest : LoadBalancerTest() {
    override fun createLoadBalancer(): LoadBalancer = RandomLoadBalancer()
}