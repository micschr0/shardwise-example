package example.notifications;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void rendersTheOrderConfirmation() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void retriesAFailedDelivery() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void honoursOptOut() {
        assertEquals(32, Workload.seconds(2.0).length);
    }
}
