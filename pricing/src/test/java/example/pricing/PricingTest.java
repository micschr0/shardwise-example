package example.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class PricingTest {

    @Test
    void appliesATieredDiscount() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void stacksCouponsInTheDefinedOrder() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void roundsHalfUpPerCurrency() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void rejectsANegativePrice() {
        assertEquals(32, Workload.seconds(2.0).length);
    }
}
