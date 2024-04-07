package clinicmanagement;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class represents a command to assign a patient to a room within the clinic.
 * It allows the user to select a patient and assign them to a specific room.
 */
public class AssignPatientToRoomCommand implements Command {
  private Clinic clinic;

  /**
   * Constructs an {@code AssignPatientToRoomCommand} with the specified clinic.
   *
   * @param clinic the clinic to which this command will apply
   */
  public AssignPatientToRoomCommand(Clinic clinic) {
    this.clinic = clinic;
  }

  @Override
  public void execute(Scanner sc) throws IllegalArgumentException {
    Patient patientToAssign = clinic.selectPatientBySerialNumber(sc);
    if (patientToAssign == null || patientToAssign.deactivated) {
      return; // The user cancelled the operation or an error occurred
    }
    // Display current room assignment, if any
    Room currentRoom = clinic.getPatientCurrentRoom(patientToAssign);
    System.out.println("Current Room: " + (currentRoom != null ? currentRoom.getName() : "None"));
    // Display available rooms
    clinic.displayAvailableRooms();
    System.out.print("Enter the number of the room to assign the patient: ");
    int roomNumber;
    try {
      roomNumber = Integer.parseInt(sc.nextLine().trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid room number format.");
    }
    Room targetRoom = clinic.getRoomByNumber(roomNumber);

    if (targetRoom == null) {
      throw new IllegalArgumentException("Error: Room does not exist.");
    }

    if (currentRoom != null && currentRoom.getRoomNumber() == targetRoom.getRoomNumber()) {
      System.out.println("Error: Patient is already assigned to this room.");
      return;
    }

    if (clinic.isRoomOccupied(targetRoom.getName())) {
      System.out.println("Error: The selected room is already occupied by another patient.");
      return;
    }

    clinic.assignPatientToRoom(patientToAssign, targetRoom.getName());
    System.out.println("Patient assigned to room: " + targetRoom.getName());
  }
}
