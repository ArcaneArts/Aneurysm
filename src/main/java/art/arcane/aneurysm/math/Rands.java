package art.arcane.aneurysm.math;

import java.util.Random;

public final class Rands {
    public static final Random RANDOM = new Random();

    /**
     * Generates a random {@link Integer}.
     *
     * @param minimum the minimum value of the generated value.
     * @param maximum the maximum value of the generated value.
     * @return a randomly generated {@link Integer} in the defined range.
     * @see #RANDOM
     */
    public static int generateRandomInteger(int minimum, int maximum) {
        return minimum + (int) (RANDOM.nextDouble() * ((maximum - minimum) + 1));
    }

    /**
     * Checks if a specific {@link Integer} is in the given range.
     * If not the respective bound of the range is returned.
     *
     * @param value the value which should be checked.
     * @param max   the maximum value.
     * @param min   the minimum value
     * @return the calculated value.
     */
    public static int getMaxOrMin(int value, int max, int min) {
        return value < max ? (Math.max(value, min)) : max;
    }
}
