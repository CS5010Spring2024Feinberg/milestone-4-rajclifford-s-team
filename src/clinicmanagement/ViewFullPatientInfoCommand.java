package clinicmanagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Executes the command to display detailed information for a specific patient.
 * It prompts the user to enter the patient's first name, last name, and date of birth,
 * then displays the details of the patient if found.
 **/
public class ViewFullPatientInfoCommand implements Command {
  private Clinic clinic;

  /**
   * Constructs a {@code ViewFullPatientInfoCommand} with the specified clinic.
   *
   * @param clinic the clinic to which this command will apply
   */
  public ViewFullPatientInfoCommand(Clinic clinic) {
    this.clinic = clinic;
  }

  /**
   * Executes the command to display detailed information for a specific patient.
   * It prompts the user to enter the patient's first name, last name, and date of birth,
   * then displays the details of the patient if found.
   *
   * @param sc the scanner used to read user input
   */
  @Override
  public void execute(Scanner sc) throws IllegalArgumentException {
    String patientFirstName;
    String patientLastName;
    String dobStr;
    while (true) {
      try {
        System.out.println("Please enter the patient's details for searching or 'q' to quit:");

        // First Name
        System.out.print("First Name: ");
        patientFirstName = sc.nextLine().trim();
        if ("q".equalsIgnoreCase(patientFirstName)) {
          System.out.println("Operation cancelled.");
          return;
        }
        if (patientFirstName.isEmpty()) {
          throw new IllegalArgumentException("First name cannot be empty.");
        }

        // Last Name
        System.out.print("Last Name: ");
        patientLastName = sc.nextLine().trim();
        if (patientLastName.isEmpty()) {
          throw new IllegalArgumentException("Last name cannot be empty.");
        }

        // Date of Birth
        System.out.print("Date of Birth (yyyy-MM-dd): ");
        dobStr = sc.nextLine().trim();
        LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Validate date format

        break;  // Break the loop if all inputs are valid

      } catch (IllegalArgumentException | DateTimeParseException e) {
        System.out.println(e.getMessage() + " Please try again.");
      }
    }

    // Find and display the patient's information
    Patient patient = clinic.findPatientByNameAndDob(patientFirstName, patientLastName, dobStr);
    if (patient != null && !patient.deactivated) {
      patient.displayFullInformation();
    } else {
      System.out.println("Patient not found or date of"
          +
          " birth does not match or patient has been deactivated.");
    }
  }
}
