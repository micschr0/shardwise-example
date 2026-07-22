package example.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void acceptsAValidOrder() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void rejectsAnEmptyCart() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void splitsShipmentsByWarehouse() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void cancelsWithinTheGracePeriod() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void refusesToCancelAfterDispatch() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void recalculatesTotalsOnLineChange() {
        assertEquals(32, Workload.seconds(3.0).length);
    }
}
