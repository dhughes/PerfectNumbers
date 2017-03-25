import java.util.stream.LongStream;

/**
 * This class contains a single static method that determines whether a provided
 * number is "perfect" or not. A perfect number is one whose divisors (excluding
 * the number it self) add up to the original number.
 *
 * For example:
 *
 * The divisors of 28 are 1*28, 2*14, 4*7. Forgetting 28, we get
 * 1 + 2 + 14 + 4 + 7 = 28. Thus 28 is perfect.
 *
 * However, 16's divisors are 1*16, 2*8, 4*4. Forgetting 16, we get
 * 1 + 2 + 8 + 4 + 4 = 19. Thus, 16 is not perfect.
 */
public class PerfectNumber {

    /**
     * Determines if a given number is perfect, given the rules defined above.
     *
     * Confirms 8589869056l in ~3 milliseconds
     *
     * This method is different from the following in that rather than looping
     * to num/2, it loops to the square root of num. When the number we are
     * testing is a valid divisor we add that to the sum as well as it's inverse.
     * EG: if the num is 28, and the test is 2, then we add both 2 and 14 to the
     * sum.
     *
     * This significantly reduces the amount of work necessary.
     *
     * I wish I could say that I determined this on my own, but given the
     * performance of the options below I decided that I need to find a way to
     * more efficiently identify divisors. Google led me to the strategy I
     * outlined above.
     *
     * @param num The number to test
     * @return A boolean indicating whether or not the number is perfect.
     */
    public static boolean reduceSqrtIsPerfect(long num) {
        return LongStream.rangeClosed(2, (long) Math.sqrt(num))
                .reduce(1, (sum, test) -> num % test == 0 ? sum + test + (num / test) : sum) == num;
    }


    /*
        The following methods are other attempts I made to solve the problem.
        They all work and tests (would) pass, but they are significantly less
        efficient than reduceSqrtIsPerfect() above.
     */

    // Confirms 8589869056l using a parallel filter then sum in ~23 secs
    public static boolean parallelFilteredIsPerfect(long num) {
        return LongStream.rangeClosed(1, num / 2)
                // make this a parallel stream so we can find the whole divisors more quickly (ideally)
                .parallel()
                // filter out any non-whole divisors
                .filter(test -> num % test == 0)
                .sum() == num;
    }

    // Confirms 8589869056l using filter then sum in ~50 secs
    public static boolean filteredIsPerfect(long num) {
        return LongStream.rangeClosed(1, num / 2)
                // filter out any non-whole divisors
                .filter(test -> num % test == 0)
                .sum() == num;
    }

    // Confirms 8589869056l using reduce in ~45 secs
    public static boolean reduceIsPerfect(long num) {
        return LongStream.rangeClosed(2, num / 2)
                .reduce(1, (sum, test) -> num % test == 0 ? sum + test : sum) == num;
    }

    // Confirms 8589869056l using a classic for loop in ~44 secs
    public static boolean forLoopIsPerfect(long num) {
        long sum = 0;

        for(long test = 1 ; test <= num / 2 ; test++){
            if(num % test == 0) sum += test;
        }

        return sum == num;
    }
}
