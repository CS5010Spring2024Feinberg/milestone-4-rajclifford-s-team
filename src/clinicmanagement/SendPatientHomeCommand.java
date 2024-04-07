package clinicmanagement;

import java.io.IOException;
import java.util.Scanner;

/**
 * Executes the command to send a patient home from the clinic.
 * It prompts the user to identify the patient and the approving clinical staff,
 * and processes the patient's discharge.
 **/
public class SendPatientHomeCommand implements Command {
  private Clinic clinic;

  /**
   * Constructs a {@code SendPatientHomeCommand} with the specified clinic.
   *
   * @param clinic the clinic to which this command will apply
   */
  public SendPatientHomeCommand(Clinic clinic) {
    this.clinic = clinic;
  }

  /**
   * Executes the command to send a patient home from the clinic.
   * It prompts the user to identify the patient and the approving clinical staff,
   * and processes the patient's discharge.
   *
   * @param sc the scanner used to read user input
   * @throws IOException if an input or output exception occurs
   */
  @Override
  public void execute(Scanner sc) throws IllegalArgumentException {
    Patient sendoffPatient = clinic.selectPatientBySerialNumber(sc);
    if (sendoffPatient == null) {
      return; // The user cancelled the operation or an error occurred
    }
    System.out.println("List of Existing Clinical Staff");
    clinic.printClinicalStaffList();

    System.out.println("Please enter the first and last name of the approving clinical staff.");
    System.out.print("First Name: ");
    String staffFirstName = sc.nextLine().trim();
    System.out.print("Last Name: ");
    String staffLastName = sc.nextLine().trim();

    ClinicalStaff approvingStaff = clinic.findClinicalStaffByName(staffFirstName, staffLastName);

    if (approvingStaff != null && "Physician".equalsIgnoreCase(approvingStaff.getJobTitle())) {
      clinic.sendPatientHome(sendoffPatient, approvingStaff);
      System.out.println("Patient has been sent home by Dr. " + approvingStaff.getLastName() + ".");
    } else if (approvingStaff != null) {
      System.out.println("Error: Only Physicians can approve sending patients home."
          +
          " The provided staff member is not a Physician.");
    } else {
      throw new IllegalArgumentException("Error: Approving staff not found.");
    }
  }
}
