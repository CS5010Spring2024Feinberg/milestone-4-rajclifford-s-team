package clinicmanagement;

import java.io.IOException;
import java.util.Scanner;

/**
 * Executes the command to register a new clinical staff member.
 * It prompts the user to input the new staff member's details
 * and registers them in the clinic system.
 * */
public class RegisterNewClinicalStaffCommand implements Command {
  private Clinic clinic;

  /**
   * Constructor for RegisterNewClinicalStaffCommand.
   * @param clinic The clinic object.
   */
  public RegisterNewClinicalStaffCommand(Clinic clinic) {
    this.clinic = clinic;
  }

  /**
   * Executes the command to register a new clinical staff member.
   * It prompts the user to input the new staff member's details
   * and registers them in the clinic system.
   * @param sc The scanner object for input.
   * @throws IllegalArgumentException If an input or output exception occurs.
   */
  @Override
  public void execute(Scanner sc) throws IllegalArgumentException {
    System.out.println("List of Existing Clinical Staff");
    clinic.printClinicalStaffList();
    // Register a new clinical staff member
    ClinicalStaffInput clinicalStaffInput = new ClinicalStaffInput();
    ClinicalStaff newClinicalStaff = clinicalStaffInput.getClinicalStaffInformation(sc);
    if (newClinicalStaff == null) {
      // User cancelled the input process
      System.out.println("Registration process cancelled.");
      return;
    }
    clinic.registerNewClinicalStaff(newClinicalStaff);
    // Print the updated list of clinical staff members
    System.out.println("New clinical staff member registered successfully.");
    System.out.println("Updated list of clinical staff members:");
    clinic.printClinicalStaffList();
  }
}
