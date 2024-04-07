package clinicmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a room in a clinic.
 */
public class Room implements RoomInterface {
  private int roomNumber; //
  private Map<String, Integer> coordinates = new HashMap<>();
  private RoomType type;
  private String name;
  private List<Patient> assignedPatients; // List of assigned patients

  /**
   * Enumeration for room types.
   */
  public enum RoomType {
    EXAM("exam"), PROCEDURE("procedure"), WAITING("waiting");

    private final String type;

    RoomType(String type) {
      this.type = type;
    }

    /**
     * Get the room type as a string.
     *
     * @return The room type.
     */
    public String getType() {
      return this.type;
    }

    /**
     * Convert a string to a RoomType.
     *
     * @param text The string to convert.
     * @return The corresponding RoomType.
     * @throws IllegalArgumentException if the string does not match any RoomType.
     */
    public static RoomType fromString(String text) throws IllegalArgumentException {
      for (RoomType r : RoomType.values()) {
        if (r.type.equalsIgnoreCase(text)) {
          return r;
        }
      }
      throw new IllegalArgumentException("Unknown room type: " + text);
    }
  }

  /**
   * Constructor to handle the specific input format.
   *
   * @param inputLine  The input line containing room information.
   * @param roomNumber The room number.
   */

  public Room(String inputLine, int roomNumber) throws IllegalArgumentException  {
    this.roomNumber = roomNumber; // Set the room number
    String[] parts = inputLine.split("\\s+"); // Split the input line
    if (parts.length < 6) {
      throw new IllegalArgumentException("Invalid room input format");
    }
    coordinates.put("lowerLeftX", Integer.parseInt(parts[0]));
    coordinates.put("lowerLeftY", Integer.parseInt(parts[1]));
    coordinates.put("upperRightX", Integer.parseInt(parts[2]));
    coordinates.put("upperRightY", Integer.parseInt(parts[3]));
    this.type = RoomType.fromString(parts[4]);
    this.name = parts[5]; // Assumes the rest of the line is the room name
    this.assignedPatients = new ArrayList<>(); // Initialize assigned patients as an empty list
  }

  /**
   * Get the room number.
   *
   * @return The room number.
   */
  @Override
  public int getRoomNumber() {
    return roomNumber;
  }

  /**
   * Get the room's coordinates.
   *
   * @return The coordinates.
   */
  public Map<String, Integer> getCoordinates() throws IllegalArgumentException {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates Cant be empty.");
    }
    return coordinates;
  }

  /**
   * Get the room type.
   *
   * @return The room type.
   */
  @Override
  public RoomType getType() throws IllegalArgumentException {
    if (type == null) {
      throw new IllegalArgumentException("Type cannot be null or empty.");
    }
    return type;
  }

  /**
   * Get the room name.
   *
   * @return The room name.
   * @throws IllegalArgumentException to handle exceptions.
   * */
  @Override
  public String getName() throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    return name;
  }

  /**
   * Get the list of assigned patients.
   *
   * @return The list of assigned patients.
   */
  @Override
  public List<Patient> getAssignedPatients() {
    return assignedPatients;
  }

  /**
   * Set the room name.
   *
   * @param name The room name to set.
   */
  @Override
  public void setName(String name) throws IllegalArgumentException {
    // Validate name
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null or empty.");
    }
    // All parameters are valid, proceed to set the name
    this.name = name;
  }

  /**
   * Check if the room is a waiting room.
   *
   * @return True if the room is a waiting room, false otherwise.
   */
  @Override
  public boolean isWaitingRoom() {
    return this.type == RoomType.WAITING;
  }

  /**
   * Provides a string representation of a Room object, including its name and type.
   *
   * @return A string detailing the room's name and type.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Room Name: ").append(name).append("\n");
    sb.append("Room Type: ").append(type).append("\n");
    return sb.toString();
  }

  /**
   * Method to display room details and list patients in the room.
    */
  @Override
  public void displayRoomDetails() throws IllegalArgumentException {
    System.out.println("\nRoom Details:");
    System.out.println("Room Number: " + getRoomNumber());
    System.out.println("Room Type: " + getType().getType()); // Assuming getType() returns an enum
    System.out.println("Room Name: " + getName());
    System.out.println("Coordinates: " + getCoordinates());

    // Assuming there's a method to get assigned patients directly from the Room object
    List<Patient> assignedPatients = getAssignedPatients();

    System.out.println("\nPatients in the Room:");
    if (assignedPatients.isEmpty()) {
      System.out.println("No patients in this room.");
    } else {
      for (Patient patient : assignedPatients) {
        System.out.println("Patient: " + patient.getFullName());
        // Display the latest visit record's chief complaint if available
        if (!patient.getVisitRecords().isEmpty()) {
          Visitrecord latestVisitRecord = patient.getVisitRecords()
              .get(patient.getVisitRecords().size() - 1);
          System.out.println("\tChief Latest Complaint: " + latestVisitRecord.getChiefComplaint());
        } else {
          System.out.println("\tNo visit records available.");
        }
      }
    }
  }

  /**
   * Checks if this Room object is equal to another object.
   *
   * @param obj The object to compare to.
   * @return True if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Room room = (Room) obj;
    return roomNumber == room.roomNumber
        &&
        Objects.equals(coordinates, room.coordinates)
        &&
        type == room.type
        &&
        Objects.equals(name, room.name)
        &&
        Objects.equals(assignedPatients, room.assignedPatients);
  }

  /**
   * Generates a hash code value for this Room object.
   *
   * @return The hash code value.
   */
  @Override
  public int hashCode() {
    return Objects.hash(roomNumber, coordinates, type, name, assignedPatients);
  }

}

