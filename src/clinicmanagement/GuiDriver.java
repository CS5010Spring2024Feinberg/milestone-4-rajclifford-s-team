package clinicmanagement;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * This class serves as the entry point for the Clinic Management System GUI.
 * It prompts the user to select a clinic data file and initializes the GUI
 * with the loaded clinic data.
 */
public class GuiDriver {

  /**
   * Main method to start the Clinic Management System GUI.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args) {
    if (args.length > 0) {
      // Notify the user that command-line arguments are not supported
      System.out.println("Command-line arguments are not supported for this program.");
      System.out.println("The GUI will be launched without processing the command-line arguments.");
      System.out.println();
    }
    SwingUtilities.invokeLater(GuiDriver::runGui);
  }

  /**
   * Runs the GUI application.
   */
  private static void runGui() {
    showWelcomeMessage();
    File selectedFile = selectClinicFile();
    if (selectedFile != null) {
      Clinic clinic = initializeClinic(selectedFile.getAbsolutePath());
      if (clinic != null) {
        startGui(clinic);
      } else {
        showError("Failed to load clinic data.");
        System.exit(1);
      }
    } else {
      System.out.println("No file selected, application will exit.");
      System.exit(0);
    }
  }

  /**
   * Displays the welcome message.
   */
  private static void showWelcomeMessage() {
    JOptionPane.showMessageDialog(
        null,
        "Welcome to the Clinic Management Centre \n (By: Rajorshi and Cliford)."
            + "\nPlease select the clinic file to start.",
        "Welcome",
        JOptionPane.INFORMATION_MESSAGE
    );
  }

  /**
   * Prompts the user to select a clinic data file.
   *
   * @return The selected clinic data file, or null if no file is selected.
   */
  protected static File selectClinicFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    fileChooser.setDialogTitle("Select Clinic Data File");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = fileChooser.showOpenDialog(null);
    return result == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
  }

  /**
   * Initializes the clinic with data from the selected file.
   *
   * @param filePath The path of the selected clinic data file.
   * @return The initialized clinic object, or null if initialization fails.
   */
  protected static Clinic initializeClinic(String filePath) {
    Clinic clinic = null;
    try (FileReader fileReader = new FileReader(filePath)) {
      ClinicFileParser fileParser = new ClinicFileParser(fileReader);
      clinic = fileParser.parseFile(); // Parses clinic information from the file
    } catch (IOException e) {
      showError("Failed to parse clinic file: " + e.getMessage());
    }
    return clinic;
  }

  /**
   * Starts the GUI with the given clinic object.
   *
   * @param clinic The clinic object to be managed by the GUI controller.
   */
  private static void startGui(Clinic clinic) {
    new GuiController(clinic);
  }

  /**
   * Displays an error message dialog.
   *
   * @param message The error message to display.
   */
  private static void showError(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
