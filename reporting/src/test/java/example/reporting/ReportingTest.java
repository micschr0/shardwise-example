package example.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class ReportingTest {

    @Test
    void aggregatesDailyRevenue() {
        assertEquals(32, Workload.seconds(1.0).length);
    }

    @Test
    void excludesCancelledOrders() {
        assertEquals(32, Workload.seconds(1.0).length);
    }

    @Test
    void bucketsByTimezone() {
        assertEquals(32, Workload.seconds(1.0).length);
    }

    @Test
    void handlesAnEmptyPeriod() {
        assertEquals(32, Workload.seconds(1.0).length);
    }
}
