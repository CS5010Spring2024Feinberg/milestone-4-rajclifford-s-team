import java.util.Random;

/**
 * A utility class for generating random numbers, with support for predictable testing.
 */
public class RandomNumberGenerator {

  private final Random random;
  private final int[] predefinedValues;
  private int index;

  /**
   * Constructs a RandomNumberGenerator using Java's Random class.
   * This constructor generates random numbers when getRandomInt is called.
   */
  public RandomNumberGenerator() {
    this.random = new Random();
    this.predefinedValues = null;
    this.index = 0;
  }

  /**
   * Constructs a RandomNumberGenerator using predefined values.
   * This constructor allows for predictable testing by returning predefined values in order.
   * @param predefinedValues an array of predefined integer values
   */
  public RandomNumberGenerator(int... predefinedValues) {
    this.random = null;
    this.predefinedValues = predefinedValues;
    this.index = 0;
  }

  /**
   * Generates a random integer between min (inclusive) and max (inclusive).
   * If predefinedValues are provided, returns values from the predefined list.
   * @param min the minimum value of the range
   * @param max the maximum value of the range
   * @return a random integer within the specified range
   */
  public int getRandomInt(int min, int max) {
    if (predefinedValues != null && index < predefinedValues.length) {
      return predefinedValues[index++];
    } else {
      return random.nextInt(max - min + 1) + min;
    }
  }
}
