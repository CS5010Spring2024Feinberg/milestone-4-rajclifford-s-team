package clinicmanagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Controller class manages user interaction and command
 *        execution in the clinic management system.
 * It maps user choices to specific commands and executes them accordingly.
 */
public class Controller {
  private Clinic clinic;
  private Appendable output;
  private Map<Integer, Command> knownCommands;

  /**
   * Constructs a Controller object with the specified clinic and output stream.
   *
   * @param clinic the clinic object representing the clinic management system
   * @param output the output stream to display messages and results
   */
  public Controller(Clinic clinic, Appendable output) {
    this.clinic = clinic;
    this.output = output;
    this.knownCommands = new HashMap<>();
    initializeCommands();
  }


  /**
   * Initializes the map of known commands with their corresponding user choices.
   */
  private void initializeCommands() throws IllegalArgumentException {
    knownCommands.put(1, new RegisterNewPatientCommand(clinic));
    knownCommands.put(2, new VisitingRecordCommand(clinic, new Scanner(System.in)));
    knownCommands.put(3, new ViewFullPatientInfoCommand(clinic));
    knownCommands.put(4, new SpecificRoomPatientInfoCommand(clinic));
    knownCommands.put(5, new RegisterNewClinicalStaffCommand(clinic));
    knownCommands.put(6, new SendPatientHomeCommand(clinic));
    knownCommands.put(7, new AssignPatientToRoomCommand(clinic));
    knownCommands.put(8, new AssignPatientToStaffCommand(clinic, new Scanner(System.in)));
    knownCommands.put(9, sc -> clinic.displaySeatingChart());
    knownCommands.put(10, sc -> ClinicMap.createClinicMap(clinic));
    knownCommands.put(11, new DeactivateStaffCommand(clinic));
    knownCommands.put(12, sc -> clinic.listClinicalStaffWithIncompleteVisits());
    knownCommands.put(13, sc -> {
      clinic.printClinicalStaffList();
      clinic.unassignClinicalStaffFromPatient(sc);
    });
    knownCommands.put(14, sc -> clinic.listClinicalStaffAndPatientCounts());
    knownCommands.put(15, sc -> clinic.listPatientsWithMultipleVisitsInLastYear());
    knownCommands.put(16, sc -> clinic.listInactivePatientsForYear());
    knownCommands.put(17, sc -> clinic.displayPatientStaff(sc));

  }

  /**
   * Handles the user's choice by executing the corresponding command.
   *
   * @param choice the user's choice representing the selected command
   * @param sc     the scanner object used for user input
   */
  public void handleUserChoice(int choice, Scanner sc) throws IllegalArgumentException {
    if (choice < 1 || choice > knownCommands.size()) {
      throw new IllegalArgumentException("Invalid choice: " + choice);
    }
    Command command = knownCommands.get(choice);
    if (command != null) {
      try {
        command.execute(sc);
      } catch (IOException e) {
        System.out.println("An error occurred while executing the command: " + e.getMessage());
      }
    } else {
      System.out.println("Invalid choice. Please try again.");
    }
  }


}
















