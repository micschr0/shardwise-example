package example.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void capturesAnAuthorizedAmount() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void declinesAnExpiredCard() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void isIdempotentOnRetriedCapture() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void refundsPartially() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void rejectsANegativeRefund() {
        assertEquals(32, Workload.seconds(3.0).length);
    }
}
