package clinicmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * An interface that defines the operations for managing patient information.
 */
public interface PatientInterface {

  /**
   * set patient deactivated.
   *
   * @param deactivated as status of the patient.
   */
  void setDeactivated(boolean deactivated);

  /**
   * set patient as activated.
   */
  void setReactivated();

  /**
   * Get the room number of the patient.
   *
   * @return The room number.
   */
  int getRoomNumber();

  /**
   * Get the first name of the patient.
   *
   * @return The first name.
   */
  String getFirstName();

  /**
   * Get the last name of the patient.
   *
   * @return The last name.
   */
  String getLastName();

  /**
   * Get the date of birth of the patient.
   *
   * @return The date of birth.
   */
  LocalDate getDateOfBirth();

  /**
   * Get the name of the room assigned to the patient.
   *
   * @return The room name.
   */
  String getRoomName();

  /**
   * Get the type of the room assigned to the patient.
   *
   * @return The room type.
   */
  Room.RoomType getRoomType();

  /**
   * Get the list of clinical staff assigned to the patient.
   *
   * @return The list of clinical staff members.
   */
  List<ClinicalStaff> getAssignedClinicalStaff();

  /**
   * Set the room type assigned to the patient.
   *
   * @param roomType The room type to set.
   */
  void setRoomType(Room.RoomType roomType);

  /**
   * Retrieves the serial number of the patient.
   *
   * @return The serial number of the patient.
   */
  int getSerialNumber();

  /**
   * Set the room name assigned to the patient.
   *
   * @param roomName The room name to set.
   */
  void setRoomName(String roomName);

  /**
   * Set the room number assigned to the patient.
   *
   * @param roomNumber The room number to set.
   */
  void setRoomNumber(int roomNumber);

  /**
   * Get the full name of the patient.
   *
   * @return The full name.
   */
  String getFullName();

  /**
   * Populate room information for the patient.
   *
   * @param room The room to populate information from.
   */
  void populateRoomInfo(Room room);

  /**
   * Populate visit record patient.
   * @param bodyTemperature etc
   * @param registrationDateTime etc
   * @param chiefComplaint etc
   */
  void addVisitRecord(LocalDateTime registrationDateTime,
                      String chiefComplaint, double bodyTemperature);

  /**
   * Retrieves the serial number of the patient.
   *
   * @return The list of visit record.
   */
  List<Visitrecord> getVisitRecords();

  /**
   * Populate display record.
   */
  void displayFullInformation();

  /**
   * Forced deactivation in the past.
   * @param deactivationDate setiings
   */
  void deactivateAtPastDate(LocalDate deactivationDate);
}
