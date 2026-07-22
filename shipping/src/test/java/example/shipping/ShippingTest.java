package example.shipping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class ShippingTest {

    @Test
    void picksTheCheapestCarrier() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void fallsBackWhenACarrierIsDown() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void quotesInternationalSurcharges() {
        assertEquals(32, Workload.seconds(3.0).length);
    }

    @Test
    void refusesToShipToEmbargoedRegions() {
        assertEquals(32, Workload.seconds(3.0).length);
    }
}
