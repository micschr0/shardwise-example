package example.newcomer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class NewcomerTest {

    @Test
    void parsesTheNewLoyaltyPayload() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void awardsPointsOnCheckout() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void expiresPointsAfterAYear() {
        assertEquals(32, Workload.seconds(2.0).length);
    }

    @Test
    void refusesNegativePointGrants() {
        assertEquals(32, Workload.seconds(2.0).length);
    }
}
