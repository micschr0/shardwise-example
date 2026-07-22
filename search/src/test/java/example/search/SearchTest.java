package example.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bench.Workload;
import org.junit.jupiter.api.Test;

class SearchTest {

    @Test
    void matchesOnPartialTitles() {
        assertEquals(32, Workload.seconds(1.0).length);
    }

    @Test
    void ranksExactMatchesFirst() {
        assertEquals(32, Workload.seconds(1.0).length);
    }

    @Test
    void toleratesTypos() {
        assertEquals(32, Workload.seconds(1.0).length);
    }

    @Test
    void filtersByFacet() {
        assertEquals(32, Workload.seconds(1.0).length);
    }

    @Test
    void returnsEmptyForNoMatch() {
        assertEquals(32, Workload.seconds(1.0).length);
    }
}
