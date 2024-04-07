package clinicmanagement;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class represents a command to [describe the specific action or operation] within the clinic.
 * It encapsulates the necessary steps to [briefly describe the process or workflow].
 */
public class VisitingRecordCommand implements Command {
  private Clinic clinic;
  private Scanner sc;

  /**
   * Constructs a {@code SpecificCommand} with the specified clinic and scanner.
   *
   * @param clinic the clinic to which this command will apply
   * @param sc the scanner used to read user input
   */
  public VisitingRecordCommand(Clinic clinic, Scanner sc) {
    this.clinic = clinic;
    this.sc = sc;
  }

  /**
   * Executes the command to update visit record for patient.
   * It prompts the user to [describe the user interaction process],
   * then [describe the outcome or the effect on the system].
   *
   * @throws IOException if an input or output exception occurs during the execution process.
   */
  @Override
  public void execute(Scanner sc) throws IllegalArgumentException {
    clinic.listAllPatients();

    System.out.println("Enter the serial number of the patient"
        +
        " to add a visiting record, or enter 'q' to quit:");
    Patient visitingPatient = null;
    while (visitingPatient == null) {
      String inputvr = sc.nextLine().trim();
      if ("q".equalsIgnoreCase(inputvr)) {
        System.out.println("Operation cancelled.");
        return;
      }

      try {
        int serialNumbervr = Integer.parseInt(inputvr);
        visitingPatient = clinic.findPatientBySerialNumber(serialNumbervr);
        if (visitingPatient == null || visitingPatient.deactivated) {
          System.out.println("No patient found with the given serial number."
              +
              " Please try again or enter 'q' to quit:");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format. Please enter a valid serial "
            +
            "number or 'q' to quit:");
      }
    }

    System.out.println("Current Record of Patient:");
    visitingPatient.displayFullInformation();

    System.out.println("Proceed to add a visit record for this patient? (yes/no): ");
    String responseforvp = sc.nextLine().trim();
    if ("yes".equalsIgnoreCase(responseforvp)) {
      visitingPatient.addVisitRecordForPatient(sc);
    } else {
      System.out.println("Operation cancelled.");
    }
  }
}

