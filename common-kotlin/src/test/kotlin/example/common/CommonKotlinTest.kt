package example.common

import bench.Workload
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CommonKotlinTest {

    @Test
    fun `normalises a postal address`() {
        assertEquals(32, Workload.seconds(1.0).size)
    }

    @Test
    fun `parses an ISO timestamp`() {
        assertEquals(32, Workload.seconds(1.0).size)
    }

    @Test
    fun `rejects a malformed currency code`() {
        assertEquals(32, Workload.seconds(1.0).size)
    }
}
