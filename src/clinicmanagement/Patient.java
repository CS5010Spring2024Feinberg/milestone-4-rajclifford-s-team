package clinicmanagement;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a patient in a clinic.
 */
public class Patient implements PatientInterface {
  private static int nextSerial = 1; // Start serial numbers at 1
  // Static counter for serial numbers // Serial number for each patient object
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
  private static final Map<String, Integer> nameDobToSerial = new HashMap<>();
  boolean deactivated;
  private final int serialNumber; // Serial number for each patient object
  private int roomNumber;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String roomName;
  private List<DeactivationRecord> deactivationHistory = new ArrayList<>();

  private Room.RoomType roomType;
  private List<ClinicalStaff> assignedClinicalStaff = new ArrayList<>();
  // Added field for assigned clinical staff

  private List<Visitrecord> visitRecords;


  /**
   * Creates a patient with the specified attributes.
   *
   * @param roomNumber  The room number where the patient is located.
   * @param firstName   The first name of the patient.
   * @param lastName    The last name of the patient.
   * @param dateOfBirth The date of birth of the patient in "M/d/yyyy" format.
   *
   * @throws IllegalArgumentException if the date of birth format is invalid.
   */
  public Patient(int roomNumber, String firstName, String lastName, String dateOfBirth)
      throws IllegalArgumentException {
    this.roomNumber = roomNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.deactivated = false;
    this.visitRecords = new ArrayList<>();
    this.deactivationHistory = new ArrayList<>();
    try {
      this.dateOfBirth = LocalDate.parse(dateOfBirth, FORMATTER);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format for patient's date of birth.");
    }
    // Generate a unique key for each patient based on name and DOB
    String key = firstName + lastName + dateOfBirth;
    if (!nameDobToSerial.containsKey(key)) {
      this.serialNumber = nextSerial++;
      nameDobToSerial.put(key, this.serialNumber);
    } else {
      this.serialNumber = nameDobToSerial.get(key);
      // Reuse the serial number for existing name+DOB combinations
    }
  }


  /**
   * Sets the deactivated status of the patient and records the date of the action.
   *
   * @param deactivated true if the patient is deactivated, false otherwise
   */
  @Override
  public void setDeactivated(boolean deactivated) {
    if (this.deactivated != deactivated) {
      this.deactivated = deactivated;

      if (deactivated) {
        // Record the deactivation event
        deactivationHistory.add(new DeactivationRecord(LocalDate.now()));
      } else {
        // Record the reactivation event, if
        // there's a previous deactivation record without a reactivation date
        if (!deactivationHistory.isEmpty()) {
          DeactivationRecord lastRecord = deactivationHistory
              .get(deactivationHistory.size() - 1);
          if (lastRecord.getReactivationDate() == null) {
            lastRecord.setReactivationDate(LocalDate.now());
          }
        }
      }
    }
  }

  /**
   * Sets the patient's status to reactivated and records
   * the reactivation date if the patient was previously deactivated.
   */
  @Override
  public void setReactivated() {
    // Only reactivate if the patient is currently deactivated
    if (this.deactivated) {
      this.deactivated = false;

      // Find the most recent deactivation record without a reactivation date
      if (!deactivationHistory.isEmpty()) {
        DeactivationRecord lastRecord = deactivationHistory.get(deactivationHistory.size() - 1);
        if (lastRecord.getReactivationDate() == null) {
          // Set the reactivation date to now
          lastRecord.setReactivationDate(LocalDate.now());
        }
      }
    }
  }

  @Override
  public int getRoomNumber() {
    return roomNumber;
  }

  @Override
  public String getFirstName() throws IllegalArgumentException {
    if (firstName == null) {
      throw new IllegalArgumentException("first name cannot be Empty");
    }
    return firstName;
  }



  /**
   * Checks if the staff member is deactivated.
   *
   * @return true if the staff member is deactivated, false otherwise
   */
  public boolean isDeactivated() {
    return deactivated;
  }

  /**
   * Provide last name of Patient.
   *
   * @return Last name string
   */
  @Override
  public String getLastName() throws IllegalArgumentException {
    if (lastName == null) {
      throw new IllegalArgumentException("last name cannot be Empty");
    }
    return lastName;
  }

  /**
   * Provide DOB of Patient.
   *
   * @return Date of birth as a LocalDate object.
   * @throws IllegalArgumentException if the date of birth is not set (null).
   */
  @Override
  public LocalDate getDateOfBirth() throws IllegalArgumentException {
    if (dateOfBirth == null) {
      throw new IllegalArgumentException("first name cannot be Empty");
    }
    return dateOfBirth;
  }

  /**
   * Retrieves the name of the room.
   *
   * @return The name of the room as a String.
   * @throws IllegalArgumentException if the room name is null or empty.
   */
  @Override
  public String getRoomName() throws IllegalArgumentException {
    if (roomName == null || roomName.isEmpty()) {
      throw new IllegalArgumentException("Room name cannot be null or empty");
    }
    return roomName;
  }

  /**
   * Retrieves the type of the room.
   *
   * @return The type of the room as a RoomType.
   * @throws IllegalArgumentException if the room type is not set (null).
   */
  @Override
  public Room.RoomType getRoomType() throws IllegalArgumentException {
    if (roomType == null) {
      throw new IllegalArgumentException("Room type cannot be null");
    }
    return roomType;
  }

  /**
   * Retrieves the list of clinical staff assigned to the room.
   *
   * @return The list of assigned clinical staff as a List.
   * @throws IllegalArgumentException if the list of assigned clinical staff is null.
   */
  @Override
  public List<ClinicalStaff> getAssignedClinicalStaff() throws IllegalArgumentException {
    if (assignedClinicalStaff == null) {
      throw new IllegalArgumentException("Assigned clinical staff list cannot be null");
    }
    return new ArrayList<>(assignedClinicalStaff); // Return a copy to preserve encapsulation
  }

  /**
   * Populates room information from the given room object.
   *
   * @param room The room object from which to populate information.
   * @throws IllegalArgumentException if the input room object is null.
   */
  @Override
  public void populateRoomInfo(Room room) throws IllegalArgumentException {
    if (room == null) {
      throw new IllegalArgumentException("Room object cannot be null");
    }
    roomName = room.getName();
    roomType = room.getType();
  }

  /**
   * Retrieves the full name of the person.
   *
   * @return The full name, combining the first name and the last name.
   * @throws IllegalArgumentException if either the first name or the last name is null.
   */
  @Override
  public String getFullName() throws IllegalArgumentException {
    if (firstName == null || lastName == null) {
      throw new IllegalArgumentException("Both first name and last name are required");
    }
    return firstName + " " + lastName;
  }

  /**
   * Set the room type for the patient.
   *
   * @param roomType The room type to set.
   */
  @Override
  public void setRoomType(Room.RoomType roomType) throws IllegalArgumentException {
    if (roomType == null) {
      throw new IllegalArgumentException("name cannot be Empty");
    }
    this.roomType = roomType;
  }

  /**
   * Retrieves the serial number of the patient.
   *
   * @return The serial number of the patient.
   * @throws IllegalStateException if the serial number is invalid
   *         (e.g., less than or equal to zero).
   */
  @Override
  public int getSerialNumber() throws IllegalArgumentException  {
    if (serialNumber <= 0) {
      throw new IllegalStateException("Serial number must be greater than zero.");
    }
    return serialNumber;
  }

  /**
   * Set the room name for the patient.
   *
   * @param roomName The room name to set.
   */
  @Override
  public void setRoomName(String roomName) throws IllegalArgumentException {
    if (roomName == null) {
      throw new IllegalArgumentException("name cannot be Empty");
    }
    this.roomName = roomName;
  }

  /**
   * Set the room number for the patient.
   *
   * @param roomNumber The room number to set.
   */
  @Override
  public void setRoomNumber(int roomNumber) throws IllegalArgumentException {
    if (roomNumber == 0 || roomNumber < 0) {
      throw new IllegalArgumentException("Room number cannot be Empty or negative");
    }
    this.roomNumber = roomNumber;
  }

  /**
   * Adds a new visit record to the patient's history.
   *
   * @param registrationDateTime the date and time of the visit registration
   * @param chiefComplaint       the chief complaint reported by the patient
   * @param bodyTemperature      the body temperature of the patient at the time of visit
   */
  @Override
  public void addVisitRecord(LocalDateTime registrationDateTime,
                             String chiefComplaint, double bodyTemperature)
      throws IllegalArgumentException {
    // Validate registrationDateTime
    if (registrationDateTime == null) {
      throw new IllegalArgumentException("Registration date and time cannot be null.");
    }

    // Validate chiefComplaint
    if (chiefComplaint == null || chiefComplaint.isEmpty()) {
      throw new IllegalArgumentException("Chief complaint cannot be null or empty.");
    }

    // Validate bodyTemperature
    if (bodyTemperature <= 0) {
      throw new IllegalArgumentException("Body temperature must be a positive value.");
    }

    // All parameters are valid, proceed to add the visit record
    Visitrecord visitRecord = new Visitrecord(registrationDateTime,
        chiefComplaint, bodyTemperature);
    visitRecords.add(visitRecord);

    // Sort the list every time a new record is added
    Collections.sort(visitRecords, Comparator.comparing(Visitrecord::getRegistrationDateTime));
  }

  @Override
  public List<Visitrecord> getVisitRecords() {
    return visitRecords;
  }

  /**
   * Adds a new visit record to the patient's history.
   */
  @Override
  public void displayFullInformation() throws IllegalArgumentException {
    // Display basic patient information
    System.out.println("Patient Information:");
    System.out.println("Serial Number: " + getSerialNumber()); // Display the serial number
    System.out.println("Name: " + getFullName());
    System.out.println("Date of Birth: " + dateOfBirth
        .format(DateTimeFormatter.ofPattern("M/d/yyyy")));
    System.out.println("Room Number: " + roomNumber);
    System.out.println("Room Name: " + roomName);
    System.out.println("Room Type: "
        + (roomType != null ? roomType.getType() : "N/A")); // Check for null room type

    // Display visit records
    if (visitRecords.isEmpty()) {
      System.out.println("No visit records available.");
    } else {
      System.out.println("Visit Records:");
      for (Visitrecord visitRecord : visitRecords) {
        // Ensure each record is displayed with all necessary details
        System.out.println("\tRegistration Date and Time: "
            + visitRecord.getRegistrationDateTime()
            .format(DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss")));
        System.out.println("\tChief Complaint: " + visitRecord.getChiefComplaint());
        System.out.println("\tBody Temperature: "
            + String.format("%.1f°C", visitRecord.getBodyTemperature()));
        System.out.println("\t--------------------------"); // Separator for readability
      }
    }
  }

  /**
   * Computes the hash code for the patient based on their serial number.
   *
   * @return The hash code value for this patient.
   */
  @Override
  public int hashCode() {
    return Objects.hash(serialNumber);
  }

  /**
   * Checks if this patient is equal to another object.
   *
   * @param obj The object to compare this patient with.
   * @return True if the given object is equal to this patient, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Patient patient = (Patient) obj;
    return serialNumber == patient.serialNumber;
  }

  /**
   * Retrieves the last deactivation date of the patient. If the patient has been
   * reactivated or never deactivated, this method returns null.
   *
   * @return the last deactivation date or null if not applicable.
   */
  public LocalDate getLastDeactivationDate() {
    if (deactivationHistory.isEmpty() || deactivationHistory
        .get(deactivationHistory.size() - 1).getReactivationDate() != null) {
      return null; // Either no deactivation record exists, or the patient was reactivated.
    }
    return deactivationHistory.get(deactivationHistory.size() - 1).getDeactivationDate();
  }

  /**
   * Deactivates the patient at a specified past date.
   * This method is primarily intended for testing purposes
   * to simulate the patient's state at a given time in the past.
   * It directly sets the patient's deactivation
   * status to true and adds a corresponding record to the deactivation history.
   *@param deactivationDate The date in the past when the patient
   *                        is to be considered as deactivated.
   *                      Should be before the current date.
   */
  @Override
  public void deactivateAtPastDate(LocalDate deactivationDate) {
    this.deactivated = true;
    this.deactivationHistory.add(new DeactivationRecord(deactivationDate));
  }


  /**
   * Add Visitiation Record.
   * @param sc the Scanner object for user input
   * @throws IOException if an I/O error occurs
   */
  public void addVisitRecordForPatient(Scanner sc) throws IllegalArgumentException {
    try {
      System.out.println("Enter registration date (yyyy-MM-dd): ");
      LocalDate registrationDate = LocalDate.parse(sc.nextLine().trim(),
          DateTimeFormatter.ofPattern("yyyy-MM-dd"));

      System.out.println("Enter registration time (HH:mm:ss): ");
      LocalTime registrationTime = LocalTime.parse(sc.nextLine().trim(),
          DateTimeFormatter.ofPattern("HH:mm:ss"));

      LocalDateTime registrationDateTime = LocalDateTime.of(registrationDate,
          registrationTime);

      System.out.println("Enter chief complaint: ");
      String chiefComplaint = sc.nextLine().trim();

      System.out.println("Enter body temperature in Celsius (e.g., 37.5): ");
      double bodyTemperature = Double.parseDouble(sc.nextLine().trim());

      // Add visit record to the patient
      addVisitRecord(registrationDateTime, chiefComplaint, bodyTemperature);

      System.out.println("Visit record added successfully.");
    } catch (DateTimeParseException e) {
      System.out.println("Invalid date or time format. Please enter the date in"
          +
          " yyyy-MM-dd and time in HH:mm:ss format.");
    } catch (NumberFormatException e) {
      System.out.println("Invalid body temperature format. Please enter a "
          +
          "valid numeric value.");
    }

  }

  /**
   * Represents a deactivation and reactivation record for a patient.
   * This private static class is used internally to track the deactivation
   * and reactivation dates of the patient.
   */
  private static class DeactivationRecord {
    private LocalDate deactivationDate;
    private LocalDate reactivationDate;

    /**
     * Constructs a DeactivationRecord with the specified deactivation date.
     *
     * @param deactivationDate The date when the deactivation occurred.
     */
    public DeactivationRecord(LocalDate deactivationDate) {
      this.deactivationDate =
          deactivationDate;
    }

    /**
     * Sets the reactivation date for this record.
     *
     * @param reactivationDate The date when the reactivation occurred.
     */
    public void setReactivationDate(LocalDate reactivationDate) {
      this.reactivationDate =
          reactivationDate;
    }

    /**
     * Retrieves the deactivation date.
     *
     * @return The date when the deactivation occurred.
     */
    public LocalDate getDeactivationDate() {
      return deactivationDate;
    }

    /**
     * Retrieves the reactivation date.
     *
     * @return The date when the reactivation occurred,
     *         or null if the patient has not been reactivated.
     */
    public LocalDate getReactivationDate() {
      return reactivationDate;
    }
  }


}




