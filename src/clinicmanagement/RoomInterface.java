package clinicmanagement;

import java.util.List;

/**
 * This interface defines the operations for managing a clinic's rooms.
 */
public interface RoomInterface {

  /**
   * Get the room number.
   *
   * @return The room number.
   */
  int getRoomNumber();

  /**
   * Get the room type.
   *
   * @return The room type.
   */
  Room.RoomType getType();

  /**
   * Get the room name.
   *
   * @return The room name.
   */
  String getName();

  /**
   * Get the list of assigned patients.
   *
   * @return The list of assigned patients.
   */
  List<Patient> getAssignedPatients();

  /**
   * Set the room name.
   *
   * @param name The room name to set.
   */
  void setName(String name);

  /**
   * Check if the room is a waiting room.
   *
   * @return True if the room is a waiting room, false otherwise.
   */
  boolean isWaitingRoom();

  /**
   * Display room details.
   */
  void displayRoomDetails();

}
