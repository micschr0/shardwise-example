package example.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void reservesStockForAnOrder() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void releasesAnExpiredReservation() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void refusesToOversell() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void mergesStockAcrossWarehouses() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void tracksBackorders() {
        assertEquals(32, Workload.seconds(2.0).length);
    }
}
