package clinicmanagement;

import java.util.Scanner;

/**
 * Executes the command to deactivate a staff member.
 * It prompts the user to select a staff member from the list,
 * then deactivates the selected staff member.
 * */
public class DeactivateStaffCommand implements Command {
  private Clinic clinic;

  /**
   * Constructor for DeactivateStaffCommand.
   * @param clinic The clinic object.
   */
  public DeactivateStaffCommand(Clinic clinic) {
    this.clinic = clinic;
  }

  /**
   * Executes the command to deactivate a staff member.
   * It prompts the user to select a staff member from the list,
   * then deactivates the selected staff member.
   * @param sc The scanner object for input.
   */
  @Override
  public void execute(Scanner sc) {
    clinic.printStaffList();
    clinic.deactivateStaff(sc);
  }
}
