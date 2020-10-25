package provider

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultProviderTest {

    @Test
    internal fun identifiersShouldBeUnique() {
        // This tests to a reasonable degree the uniqueness, however this is not intended to be a proof.
        val set = hashSetOf<String>()
        for (i in 1..100) set.add(DefaultProvider().get())
        assertEquals(100, set.size)
    }
}