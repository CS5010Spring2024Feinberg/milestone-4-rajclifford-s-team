package clinicmanagement;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * The UserInput class handles user input related to patient and clinical staff information.
 */
public class UserInput {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
  private String dobInput = "";
  private String patientFirstName = null;
  private String patientLastName = null;

  /**
   * Prompt the user to enter patient information.
   * If the user enters 'q', the process is cancelled.
   * If the input is invalid, the user is prompted to try again.
   * @param sc input
   * @return A Patient object with the entered information, or null if the process is cancelled.
   */
  public Patient getPatientInformation(Scanner sc) throws IllegalArgumentException {
    sc = new Scanner(System.in);
    while (true) {
      try {
        System.out.print("Enter patient first name (or 'q' to quit): ");
        String input = sc.nextLine().trim();
        if ("q".equalsIgnoreCase(input)) {
          System.out.println("Input process cancelled.");
          return null; // Return null to indicate cancellation
        }
        patientFirstName = input;

        System.out.print("Enter patient last name (or 'q' to quit): ");
        input = sc.nextLine().trim();
        if ("q".equalsIgnoreCase(input)) {
          System.out.println("Input process cancelled.");
          return null; // Return null to indicate cancellation
        }
        patientLastName = input;

        // Validate first name
        if (patientFirstName.isEmpty()) {
          throw new IllegalArgumentException("First name cannot be empty.");
        }
        // Validate last name
        if (patientLastName.isEmpty()) {
          throw new IllegalArgumentException("Last name cannot be empty.");
        }

        break; // Break the loop if all inputs are provided and valid
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage() + " Please try again.");
        return null;
      }
    }

    while (true) {
      dobInput = getValidDateOfBirth(sc);
      if (dobInput == null) {
        return null; // Return null to indicate cancellation
      }
      break; // Break the loop if dobInput is valid or user cancelled
    }

    return new Patient(1, patientFirstName, patientLastName, dobInput);
  }

  /**
   * Helper method to validate and retrieve a valid date of birth from the user.
   *
   * @param sc The Scanner object for user input.
   * @return A valid date of birth in the format "M/d/yyyy."
   */
  private String getValidDateOfBirth(Scanner sc) throws IllegalArgumentException {
    while (true) {
      System.out.print("Enter date of birth (M/D/YYYY)(or 'q' to quit): ");
      dobInput = sc.nextLine();
      if ("q".equalsIgnoreCase(dobInput)) {
        System.out.println("Input process cancelled.");
        return null; // Return null to indicate cancellation
      }
      try {
        // Check if the entered date is in the correct format
        FORMATTER.parse(dobInput);
        break; // Valid date, exit the loop
      } catch (DateTimeParseException e) {
        System.out.println("Invalid date format. Please try again.");
      }
    }
    return dobInput;
  }
}

