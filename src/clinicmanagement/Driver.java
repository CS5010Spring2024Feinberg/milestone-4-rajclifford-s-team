package clinicmanagement;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * The main class responsible for driving the Clinic Management System.
 * It loads a clinic file, initializes the system, and provides a menu for user interaction.
 */
public class Driver {

  /**
   * Main method to start the Clinic Management System.
   *
   * @param args command-line arguments. Expects the file path
   *             of the clinic file in "/res/test.txt" format.
   * @throws IOException if an I/O error occurs while reading the clinic file
   */
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Welcome! Please provide the file path of the clinic file in "
          + "/res/test.txt format,"
          + " to upload it in our Clinic Management System");
      return;
    }

    String filePath = args[0];

    // Load a resource file from the specified file path
    InputStream inputStream = Driver.class.getResourceAsStream(filePath);
    if (inputStream == null) {
      System.out.println("File not found: " + filePath);
      return;
    }

    // Wrap the InputStream with an InputStreamReader to create a Readable object
    Reader fileReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

    // Initialize the clinic object
    Clinic clinic = null;
    try {
      ClinicFileParser fileParser = new ClinicFileParser(fileReader);
      clinic = fileParser.parseFile(); // Parses clinic information from the file
    } catch (IOException e) {
      System.out.println("Failed to parse clinic file: " + e.getMessage());
      return;
    }

    // Ensure clinic was successfully initialized
    if (clinic == null) {
      System.out.println("Clinic initialization failed.");
      return;
    }

    // Initialize Scanner for user input
    Scanner sc = new Scanner(System.in);

    // Create Controller with input from Scanner and output to System.out
    Controller controller = new Controller(clinic, System.out);

    // Launch the menu-driven interface
    launchMenu(sc, controller);

    // Close Scanner after use
    sc.close();
  }


  private static void launchMenu(Scanner sc, Controller controller)
      throws IllegalArgumentException {
    boolean running = true;
    while (running) {
      System.out.println("\nClinic Management System Menu:");
      System.out.println("1. Register a new patient");
      System.out.println("2. Add a Visiting Record to an Existing patient");
      System.out.println("3. View Information of Patient");
      System.out.println("4. Get Information on Room");
      System.out.println("5. Register a Clinical Staff");
      System.out.println("6. Send Patient Home");
      System.out.println("7. Assign Room to a Patient");
      System.out.println("8. Assign Clinical Staff to a Patient");
      System.out.println("9. Display all Room seating chart");
      System.out.println("10. Generate Clinic Map");  // New option for generating the clinic map
      System.out.println("11. Deactivate Staff");
      System.out.println("12. List clinical staff members who have a patient in the clinic.");
      System.out.println("13. Unassign a selected clinical staff member from a patient.");
      System.out.println("14. List all the clinical staff members and number(s) "
          +
          "of patient ever assigned");
      System.out.println("15. List of patients who have had two or more visits in"
          +
          " the past 365 days");
      System.out.println("16. List patients who have been in our clinic before but "
          +
          "have not been in the clinic "
          +
          "for more than 365 days from today");
      System.out.print("Enter your choice: ");

      if (!sc.hasNextInt()) {
        throw new IllegalArgumentException("Invalid input. Please enter a number.");
      }

      int choice = sc.nextInt();
      sc.nextLine(); // Consume the newline character left from the previous input

      controller.handleUserChoice(choice, sc);
    }
  }
}
