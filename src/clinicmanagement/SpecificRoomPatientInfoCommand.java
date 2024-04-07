package clinicmanagement;

import java.io.IOException;
import java.util.Scanner;

/**
 * Executes the command to display patient information for a specific room.
 * It prompts the user to select a room and displays the details of the
 *            patients assigned to that room.
 **/
public class SpecificRoomPatientInfoCommand implements Command {
  private Clinic clinic;

  /**
   * Constructs a {@code SpecificRoomPatientInfoCommand} with the specified clinic.
   *
   * @param clinic the clinic to which this command will apply
   */
  public SpecificRoomPatientInfoCommand(Clinic clinic) {
    this.clinic = clinic;
  }

  /**
   * Executes the command to display patient information for a specific room.
   * It prompts the user to select a room and displays the details of the
   *            patients assigned to that room.
   *
   * @param sc the scanner used to read user input
   * @throws IOException if an input or output exception occurs during the execution process.
   */
  @Override
  public void execute(Scanner sc) throws IllegalArgumentException {
    // Print the list of rooms
    System.out.println("List of Rooms:");
    clinic.printRoomList();

    while (true) {
      System.out.print("\nEnter the room number to display details (or enter 'q' to quit): ");
      String input = sc.nextLine().trim();

      if ("q".equalsIgnoreCase(input)) {
        break; // Exit the loop
      }

      try {
        int roomNumber = Integer.parseInt(input);
        Room selectedRoom = clinic.getRoomByNumber(roomNumber);

        if (selectedRoom != null) {
          selectedRoom.displayRoomDetails(); // Call the method to display room details
        } else {
          System.out.println("Room not found.");
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid input. "
            +
            "Please enter a valid room number or 'q' to quit.");
      }
    }
  }
}
