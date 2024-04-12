package clinicmanagement;

import java.io.IOException;

/**
 * Represents a command within the clinic management system for a GUI context.
 * This interface defines a common protocol for executing commands.
 */
public interface Command {

  /**
   * Executes the command. The specific behavior of this method is defined
   * in the classes that implement this interface.
   *
   * @throws IOException if an input or output exception occurs during command execution.
   */
  void execute() throws IOException;
}
