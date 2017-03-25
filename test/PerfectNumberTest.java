import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class PerfectNumberTest {

    @Test
    /**
     * When 28
     * Then the number is perfect
     */
    public void when28ThenPerfect() {
        // Act
        boolean result = PerfectNumber.reduceSqrtIsPerfect(28);

        // Assert
        assertThat(result, equalTo(true));
    }

    @Test
    /**
     * When 16
     * Then the number is not perfect
     */
    public void when16ThenNotPerfect() {
        // Act
        boolean result = PerfectNumber.reduceSqrtIsPerfect(16);

        // Assert
        assertThat(result, equalTo(false));
    }

    @Test
    /**
     * When 496
     * Then the number is perfect
     */
    public void when496ThenPerfect() {
        // Act
        boolean result = PerfectNumber.reduceSqrtIsPerfect(496);

        // Assert
        assertThat(result, equalTo(true));
    }

    @Test
    /**
     * When 8128
     * Then the number is perfect
     */
    public void when8128ThenPerfect() {
        // Act
        boolean result = PerfectNumber.reduceSqrtIsPerfect(8128);

        // Assert
        assertThat(result, equalTo(true));
    }

    @Test
    /**
     * When 33550336
     * Then the number is perfect
     */
    public void when33550336ThenPerfect() {
        // Act
        boolean result = PerfectNumber.reduceSqrtIsPerfect(33550336);

        // Assert
        assertThat(result, equalTo(true));
    }

    @Test
    /**
     * When a number between 29 and 495
     * Then the number is not perfect
     */
    public void whenBetween29And495ThenNotPerfect() {
        for (int x = 29; x <= 495; x++) {
            // Act
            boolean result = PerfectNumber.reduceSqrtIsPerfect(x);

            // Assert
            assertThat(result, equalTo(false));
        }
    }

    @Test
    /**
     * When 8589869056
     * Then the number is perfect
     */
    public void when8589869056ThenPerfect() {
        // Act
        long start = System.currentTimeMillis();
        boolean result = PerfectNumber.reduceSqrtIsPerfect(8589869056l);

        System.out.println((System.currentTimeMillis() - start) + " ms");

        // Assert
        assertThat(result, equalTo(true));

    }

}
