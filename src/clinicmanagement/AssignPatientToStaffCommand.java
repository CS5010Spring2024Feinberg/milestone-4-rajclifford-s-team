package clinicmanagement;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a command to assign clinical staff to a patient within a clinic.
 * It allows the user to associate one or more clinical staff
 * members with a patient based on their serial numbers.
 */
public class AssignPatientToStaffCommand implements Command {
  private Clinic clinic;
  private Scanner sc;

  /**
   * Constructs an {@code AssignPatientToStaffCommand} with the specified clinic and scanner.
   *
   * @param clinic the clinic to which this command will apply
   * @param sc the scanner used to read user input
   */
  public AssignPatientToStaffCommand(Clinic clinic, Scanner sc) {
    this.clinic = clinic;
    this.sc = sc;
  }

  /**
   * Executes the command to assign clinical staff to a patient.
   * It prompts the user to select a patient
   * and then allows the user to assign one or more clinical staff members
   * to the selected patient.
   * The assignment is based on the serial numbers of the clinical staff.
   *
   * @throws IOException if an input or output exception occurs
   */
  @Override
  public void execute(Scanner sc) throws IllegalArgumentException {
    Patient patientToStaff = clinic.selectPatientBySerialNumber(sc);
    if (patientToStaff == null) {
      return; // The user cancelled the operation or an error occurred
    }

    // Print already assigned clinical staff
    System.out.println("Currently assigned clinical staff to "
        +
        patientToStaff.getFullName() + ":");
    List<ClinicalStaff> assignedClinicalStaff = patientToStaff.getAssignedClinicalStaff();
    if (assignedClinicalStaff == null || assignedClinicalStaff.isEmpty()) {
      System.out.println("Currently, there are no clinical staff assigned to "
          +
          patientToStaff.getFullName() + ".");
    } else {
      System.out.println(">" + patientToStaff.getFullName() + ":");
      assignedClinicalStaff.forEach(staff -> System.out.println("- " + staff.getFullName()));
    }

    Set<Integer> alreadyAssignedSerialNumbers =
        patientToStaff.getAssignedClinicalStaff()
            .stream()
            .map(ClinicalStaff::getSerialNumber)
            .collect(Collectors.toSet());

    System.out.println("List of Existing Clinical Staff");
    clinic.printClinicalStaffList();

    while (true) {
      System.out.print("Enter the serial number of the clinical staff to assign,"
          +
          " or 'q' to finish: ");
      String inputst = sc.nextLine().trim();

      if ("q".equalsIgnoreCase(inputst)) {
        break; // Exit if the user enters 'q'
      }

      try {
        int serialNumberpts = Integer.parseInt(inputst);
        if (alreadyAssignedSerialNumbers.add(serialNumberpts)) {
          clinic.assignClinicalStaffToPatient(patientToStaff, serialNumberpts, this);
        } else {
          System.out.println("Error: This clinical staff member has already been assigned "
              +
              "to the patient.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid serial number format. Please enter a valid serial "
            +
            "number or 'q' to quit:");
      } catch (NullPointerException e) {
        throw new IllegalArgumentException("No staff found with the given serial number. "
            +
            "Please try again or enter 'q' to quit:");
      }
    }

    // Final list of assigned clinical staff
    System.out.println("Final list of clinical staff assigned to "
        + patientToStaff.getFullName() + ":");
    patientToStaff.getAssignedClinicalStaff().forEach(staff ->
        System.out.println("- " + staff.getFullName()));
  }
}
