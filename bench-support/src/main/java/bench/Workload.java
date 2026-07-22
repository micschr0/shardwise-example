package bench;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Deterministic CPU work, so this example's test durations are honest rather than
 * {@code Thread.sleep}. Each module burns a fixed number of SHA-256 rounds; how many
 * milliseconds that costs is up to the machine, which is exactly the point — the
 * weights file is measured per environment, never hand-written.
 */
public final class Workload {

    /**
     * Rounds that cost roughly one second on a GitHub-hosted {@code ubuntu-latest} runner.
     *
     * <p>This is the calibration knob. On slower or faster hardware the whole suite scales
     * with it; re-measure with {@code ./gradlew :catalog:test --info} and adjust if the
     * example takes an unreasonable amount of time on your machine.
     */
    public static final int UNIT = 5_800_000;

    private Workload() {
    }

    /** Burns approximately {@code n} seconds of CPU. Returns the digest so the JIT cannot elide it. */
    public static byte[] seconds(double n) {
        return burn((int) (n * UNIT));
    }

    private static byte[] burn(int rounds) {
        MessageDigest digest = sha256();
        byte[] state = new byte[32];
        for (int i = 0; i < rounds; i++) {
            state = digest.digest(state);
        }
        return state;
    }

    private static MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required of every Java platform", e);
        }
    }
}
