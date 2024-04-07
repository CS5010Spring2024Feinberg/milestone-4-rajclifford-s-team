import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * The TestUtils class provides utility methods for testing purposes.
 */
public class TestUtils {

  /**
   * Captures the console output produced by a given task.
   *
   * @param task The task whose console output needs to be captured.
   * @return The captured console output as a string.
   */
  public static String captureConsoleOutput(Runnable task) {
    // Create a ByteArrayOutputStream to capture console output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;

    try {
      // Redirect System.out to the outputStream
      System.setOut(new PrintStream(outputStream));

      // Execute the task
      task.run();

      // Return the captured output as a string
      return outputStream.toString();
    } finally {
      // Reset System.out to the original PrintStream
      System.setOut(originalOut);
    }
  }
}
