package clinicmanagement;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * The {@code Clinic} class represents a medical clinic and manages its rooms,
 * patients, staff, and assignments.
 */
public class Clinic implements ClinicInterface {
  private String name;
  private final List<Room> rooms;
  private final List<Patient> patients;
  private final List<Staff> staff;
  private final Map<Room, Patient> roomAssignments;
  private final Map<Room, List<Patient>> waitingRoomAssignments;
  private final Map<Patient, List<Staff>> patientAssignments;
  private int roomNumber;
  private LocalDate dob;

  /**
   * Constructs a new clinic with empty lists and maps for rooms, patients, staff, and assignments.
   **/
  public Clinic() throws IllegalArgumentException {
    rooms = new ArrayList<>();
    patients = new ArrayList<>();
    staff = new ArrayList<>();
    roomAssignments = new HashMap<>();
    waitingRoomAssignments = new HashMap<>();
    patientAssignments = new HashMap<>();
    if (rooms == null || patients == null || staff == null || roomAssignments == null
        ||
        waitingRoomAssignments == null || patientAssignments == null) {
      throw new IllegalArgumentException("Lists and maps cannot be null.");
    }
  }

  /**
   * Retrieves the name of the clinic.
   *
   * @return The name of the clinic.
   * @throws IllegalArgumentException if the name is null.
   */
  @Override
  // Getters and Setters for clinic properties
  public String getName() throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    return name;
  }

  /**
   * Sets the name of the clinic.
   *
   * @param name The name to set for the clinic.
   * @throws IllegalArgumentException if the name is null.
   */
  @Override
  public void setName(String name) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.name = name;
  }

  /**
   * Adds a room to the clinic.
   *
   * @param room The room to be added to the clinic.
   * @throws IllegalArgumentException if the room is null.
   */
  @Override
  public void addRoom(Room room) throws IllegalArgumentException {
    if (room == null) {
      throw new IllegalArgumentException("Room cannot be null.");
    }
    rooms.add(room);
  }

  @Override
  public void addPatient(Patient patient) throws IllegalArgumentException {
    if (patient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }
    patients.add(patient);
  }

  /**
   * Retrieves the list of all patients in the clinic.
   *
   * @return The list of all patients.
   * @throws IllegalArgumentException if any parameter is invalid.
   */
  public List<Patient> getAllPatients() throws IllegalArgumentException {
    if (patients == null) {
      throw new IllegalArgumentException("Patients list is null");
    }
    return patients;
  }

  /**
   * Adds a patient to the clinic.
   *
   * @param staffMember The Staff to be added to the clinic.
   * @throws IllegalArgumentException if the patient is null.
   */
  @Override
  public void addStaff(Staff staffMember) throws IllegalArgumentException {
    if (staff == null) {
      throw new IllegalArgumentException("Staff cannot be null.");
    }
    staff.add(staffMember);
  }

  /**
   * Registers a new patient in the clinic. If a patient with the same details already exists,
   * and they are deactivated, the patient will be reactivated. Otherwise, a duplicate patient
   * message will be displayed.
   *
   * @param newPatient The patient to register in the clinic.
   * @return The registered or reactivated patient object.
   * @throws IllegalArgumentException if the patient object is null.
   */
  @Override
  public Patient registerNewPatient(Patient newPatient) throws IllegalArgumentException {
    if (newPatient == null) {
      throw new IllegalArgumentException("Patient object cannot be null");
    }

    if (isDuplicatePatient(newPatient)) {
      Patient existingPatient = findExistingPatient(newPatient);
      // Check if the existing patient is deactivated
      if (existingPatient != null && existingPatient.isDeactivated()) {
        // Reactivate the existing patient
        existingPatient.setReactivated();
        // Update the existing patient's room information
        roomNumber = 1; // Assuming room number assignment logic here
        Room room = getRoomByNumber(roomNumber);
        if (room != null) {
          existingPatient.setRoomName(room.getName());
          existingPatient.setRoomType(room.getType());
          System.out.println("Duplicate patient found. Reactivated patient: "
              + existingPatient.getFullName());
          return existingPatient;
        } else {
          System.out.println("Primary Waiting Room with number " + roomNumber + " not found.");
        }
      } else {
        // Inform user about the duplicate patient
        System.out.println("Duplicate patient found. This patient is already registered.");
      }
    } else {
      // Proceed with registering the new patient
      roomNumber = 1; // Assuming room number assignment logic here
      Room room = getRoomByNumber(roomNumber);
      if (room != null) {
        newPatient.setRoomName(room.getName());
        newPatient.setRoomType(room.getType());
        roomAssignments.put(room, newPatient);
        patients.add(newPatient);
        System.out.println("Patient " + newPatient.getFullName() + " registered successfully in "
            + "room " + room.getRoomNumber() + ": " + room.getName() + " (Type: " + room.getType()
            + ")");
        return newPatient;
      } else {
        System.out.println("Room with number " + roomNumber + " not found.");
      }
    }
    return null; // Return null if patient registration fails
  }



  /**
   * Prints the list of rooms in the clinic, including room number, type, and name.
   * Each room's details are displayed with a separator.
   */
  @Override
  public void printRoomList() throws IllegalArgumentException {
    if (rooms == null) {
      throw new IllegalArgumentException("Room list cannot be null.");
    }

    System.out.println("\nList of Rooms:");
    for (Room room : rooms) {
      System.out.println("Room Number: " + room.getRoomNumber());
      System.out.println("Room Type: " + room.getType().getType());
      System.out.println("Room Name: " + room.getName());
      System.out.println("--------------------------");
    }
  }

  /**
   * Returns a Room object based on the room number.
   *
   * @param roomNumber The room number to search for.
   * @return The Room object with the specified room number, or null if not found.
   */
  @Override
  public Room getRoomByNumber(int roomNumber) throws IllegalArgumentException {
    if (rooms == null) {
      throw new IllegalArgumentException("Room cannot be null.");
    }
    for (Room room : rooms) {
      if (room.getRoomNumber() == roomNumber) {
        return room;
      }
    }
    return null; // Room not found
  }

  /**
   * Room getter method for testing.
   * return a list of rooms.
   * @return a list of rooms.
   */
  public List<Room> getRooms() {
    return rooms;
  }


  /**
   * Registers a new clinical staff member.
   *
   * @param staffMember The clinical staff member to register.
   */
  @Override
  public void registerNewClinicalStaff(ClinicalStaff staffMember) throws IllegalArgumentException {
    if (staff == null) {
      throw new IllegalArgumentException("staff cannot be null.");
    }
    staff.add(staffMember);
  }

  /**
   * Sends a patient home, approved by a clinical staff member.
   *
   * @param patient        The patient to send home.
   * @param approvingStaff The clinical staff member approving the patient's discharge.
   */
  @Override
  public void sendPatientHome(Patient patient, ClinicalStaff approvingStaff)
      throws IllegalArgumentException {
    if (patient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    if (approvingStaff == null) {
      throw new IllegalArgumentException("Approving staff cannot be null.");
    }
    // Check if the patient is already deactivated before attempting to deactivate
    if (patient.isDeactivated()) {
      LocalDate lastDeactivationDate = patient.getLastDeactivationDate();
      System.out.println("Patient " + patient.getFullName() + " cannot be deactivated again,"
          +
          " as they were already deactivated"
          +
          (lastDeactivationDate != null ? " on " + lastDeactivationDate : "") + ".");
      return;
    }

    patient.setDeactivated(true);

    // Double-check if deactivation was successful
    if (!patient.isDeactivated()) {
      throw new IllegalArgumentException("Error: Failed to deactivate patient.");

    }

    // Remove the patient from any room assignments
    if (rooms != null) {
      rooms.forEach(room -> room.getAssignedPatients().remove(patient));
    }

    // Remove the patient from any clinical staff's list of assigned patients
    if (staff != null) {
      staff.forEach(staffMember -> {
        if (staffMember instanceof ClinicalStaff) {
          ((ClinicalStaff) staffMember).getAssignedPatients().remove(patient);
        }
      });
    }

    System.out.println("Patient " + patient.getFullName()
        + " has been deactivated and removed from all areas.");
  }

  /**
   * Assigns a patient to a room, superseding any previous assignment.
   *
   * @param patient  The patient to assign to a room.
   * @param roomName The name of the room to which the patient is assigned.
   */
  @Override
  public void assignPatientToRoom(Patient patient, String roomName)
      throws IllegalArgumentException {
    if (patient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }
    if (roomName == null || roomName.isEmpty()) {
      throw new IllegalArgumentException("Room name cannot be null or empty.");
    }

    // Find the Room object based on roomName
    Room roomToAssign = this.rooms.stream()
        .filter(room -> room.getName().equalsIgnoreCase(roomName))
        .findFirst()
        .orElse(null);

    if (roomToAssign == null) {
      System.out.println("Error: Room with name '" + roomName + "' not found.");
      return;
    }

    if (isRoomOccupied(roomName) && !roomToAssign.isWaitingRoom()) {
      System.out.println("Error: The selected room is already occupied by another patient.");
      return;
    }

    if (isPatientInExamOrProcedureRoom(patient) && roomToAssign.isWaitingRoom()) {
      System.out.println("Error: Patient is already in an Exam or "
          + "Procedure Room and cannot be assigned to a Waiting Room.");
      return;
    }

    // Remove the patient from their current room's assigned patient list
    Room currentRoom = getPatientCurrentRoom(patient);
    if (currentRoom != null) {
      currentRoom.getAssignedPatients().remove(patient);
    }

    // Assign the patient to the new room
    roomToAssign.getAssignedPatients().add(patient);

    // Update the patient's room details
    patient.setRoomNumber(roomToAssign.getRoomNumber());
    patient.setRoomName(roomName);
    patient.setRoomType(roomToAssign.getType());

    System.out.println("Patient " + patient.getFullName() + " assigned to room: " + roomName);
  }


  /**
   * Returns a list of patients in a specified room.
   *
   * @param room The room to search for patients in.
   * @return A list of patients in the specified room.
   */
  @Override
  public List<Patient> getPatientsInRoom(Room room) throws IllegalArgumentException {
    if (room == null) {
      throw new IllegalArgumentException("Room cannot be null.");
    }

    List<Patient> patientsInRoom = new ArrayList<>();

    for (Patient patient : patients) {
      if (!patient.deactivated) {
        if (patient.getRoomNumber() == room.getRoomNumber()) {
          patientsInRoom.add(patient);
        }
      }
    }

    return patientsInRoom;
  }


  /**
   * Finds a patient by their first name and last name.
   *
   * @param firstName The first name of the patient to search for.
   * @param lastName  The last name of the patient to search for.
   * @return The patient with the specified first name and last name, or null if not found.
   */
  @Override
  public Patient findPatientByName(String firstName, String lastName)
      throws IllegalArgumentException {
    if (firstName == null || lastName == null) {
      throw new IllegalArgumentException("First name and last name cannot be null.");
    }
    for (Patient patient : patients) {
      if (patient.getFirstName().equalsIgnoreCase(firstName)
          && patient.getLastName().equalsIgnoreCase(lastName)) {
        return patient;
      }
    }
    return null; // Patient not found
  }

  /**
   * Searches for and returns a Patient object that matches the given
   * first name, last name, and date of birth.
   *
   * This method iterates through the list of patients and compares each patient's
   * first name, last name,
   * and date of birth with the provided parameters. The comparison of first name
   * and last name is case-insensitive,
   * while the date of birth must match exactly.
   *
   * @param firstName the first name of the patient to find, case-insensitive
   * @param lastName  the last name of the patient to find, case-insensitive
   * @param dobStr    the date of birth of the patient to find, formatted as
   *                  "yyyy-MM-dd"
   * @return          the Patient object that matches the given names
   *                  and date of birth, or {@code null} if no such patient is found
   * @throws DateTimeParseException if the {@code dobStr} does not match the
   *                  "yyyy-MM-dd" pattern
   */
  public Patient findPatientByNameAndDob(String firstName, String lastName, String dobStr)
      throws IllegalArgumentException {
    // Validate parameters
    if (firstName == null || lastName == null || dobStr == null) {
      throw new IllegalArgumentException("First name, last name, and date of "
          +
          "birth cannot be null.");
    }
    try {
      dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date of birth format. "
          +
          "Please provide date in yyyy-MM-dd format.", e);
    }
    for (Patient patient : patients) {
      if (patient.getFirstName().equalsIgnoreCase(firstName)
          && patient.getLastName().equalsIgnoreCase(lastName)
          && patient.getDateOfBirth().equals(dob)) {
        return patient;
      }
    }
    return null; // No matching patient found
  }


  /**
   * Finds a clinical staff member by their first name and last name.
   *
   * @param firstName The first name of the clinical staff member to search for.
   * @param lastName  The last name of the clinical staff member to search for.
   * @return The clinical staff member with the specified first name and last name.
   */
  @Override
  public ClinicalStaff findClinicalStaffByName(String firstName, String lastName)
      throws IllegalArgumentException {
    // Validate parameters
    if (firstName == null || lastName == null) {
      throw new IllegalArgumentException("First name and last name cannot be null.");
    }
    for (Staff staffMember : staff) {
      if (staffMember instanceof ClinicalStaff) {
        ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;
        // Compare names after ensuring they are not null
        if (firstName.equalsIgnoreCase(clinicalStaff.getFirstName())
            && lastName.equalsIgnoreCase(clinicalStaff.getLastName())) {
          return clinicalStaff;
        }
      }
    }
    return null; // Clinical staff member not found
  }

  /**
   * Gets the current room of a patient.
   *
   * @param patient The patient whose current room is to be determined.
   * @return The room to which the patient is currently assigned, or null.
   */
  @Override
  public Room getPatientCurrentRoom(Patient patient) throws IllegalArgumentException {
    // Validate parameter
    if (patient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    for (Room room : rooms) {
      // Check if the patient is in the list of assigned patients for each room
      for (Patient assignedPatient : room.getAssignedPatients()) {
        if (assignedPatient.equals(patient)) {
          return room; // Return the room if the patient is found in its assigned patients list
        }
      }
    }
    return null; // Return null if the patient is not assigned to any room
  }

  /**
   * Displays a list of available rooms.
   */
  @Override
  public void displayAvailableRooms() throws IllegalArgumentException {
    // Validate parameter
    if (rooms == null) {
      throw new IllegalArgumentException("Rooms list cannot be null.");
    }

    System.out.println("Available Rooms:");
    boolean hasAvailableRooms = false;
    for (Room room : rooms) {
      // Assuming isRoomOccupied checks if the room is currently not occupied
      if (!isRoomOccupied(room.getName())) {
        System.out.println("Room Number: " + room.getRoomNumber()
            + " | Room Type: " + room.getType().getType()
            + " | Room Name: " + room.getName());
        hasAvailableRooms = true;
      }
    }
    if (!hasAvailableRooms) {
      System.out.println("No available rooms at the moment.");
    }
  }

  /**
   * Prints a list of clinical staff members.
   */
  @Override
  public void printClinicalStaffList()  throws IllegalArgumentException {
    // Validate parameter
    if (staff == null) {
      throw new IllegalArgumentException("Staff list cannot be null.");
    }

    System.out.println("Clinical Staff List:");
    for (Staff staffMember : staff) {
      if (staffMember instanceof ClinicalStaff) {
        ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;
        // Assuming getPrefix() method exists in ClinicalStaff
        // class to get the prefix based on the job title
        if (!clinicalStaff.isDeactivated()) {
          String prefix = clinicalStaff.getPrefix();
          System.out.println("Serial Number: " + clinicalStaff.getSerialNumber());
          // Assuming getSerialNumber() exists in Staff
          System.out.println("Name: " + prefix + " " + clinicalStaff.getFullName());
          // Including prefix in the name
          System.out.println("Job Title: " + clinicalStaff.getJobTitle());
          System.out.println("Education Level: " + clinicalStaff.getEducationLevel());
          System.out.println("Unique Identifier (NPI): " + clinicalStaff.getNpi());
          System.out.println("--------------------------");
        }
      }
    }
  }

  /**
   * Prints a list of staff members.
   */
  @Override
  public void printStaffList() throws IllegalArgumentException {
    if (staff == null) {
      throw new IllegalArgumentException("Staff list cannot be null.");
    }
    System.out.println("Staff List:");
    for (Staff staffMember : staff) {
      String prefix = "";
      if (staffMember instanceof ClinicalStaff) {
        ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;
        if (clinicalStaff.isDeactivated()) {
          continue; // Skip deactivated clinical staff
        }
        prefix = clinicalStaff.getPrefix();
      } else {
        NonClinicalStaff nonClinicalStaff = (NonClinicalStaff) staffMember;
        if (nonClinicalStaff.isDeactivated()) {
          continue; // Skip deactivated non-clinical staff
        }
      }
      System.out.println("Serial Number: " + staffMember.getSerialNumber());
      System.out.println("Name: " + prefix + " " + staffMember.getFullName());
      if (staffMember instanceof ClinicalStaff) {
        ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;
        System.out.println("Job Title: " + clinicalStaff.getJobTitle());
        System.out.println("Education Level: " + clinicalStaff.getEducationLevel());
        System.out.println("Unique Identifier (NPI): " + clinicalStaff.getNpi());
      } else {
        NonClinicalStaff nonClinicalStaff = (NonClinicalStaff) staffMember;
        System.out.println("Job Title: " + nonClinicalStaff.getJobTitle());
      }
      System.out.println("--------------------------");
    }
  }

  /**
   * Checks if a room is occupied, specifically for "procedure" and "exam" rooms.
   *
   * @param roomName The name of the room to check for occupancy.
   * @return true if the room is occupied, false otherwise.
   */
  @Override
  public boolean isRoomOccupied(String roomName) throws IllegalArgumentException {
    // Validate parameter
    if (rooms == null) {
      throw new IllegalArgumentException("Rooms list cannot be null.");
    }
    for (Room room : rooms) {
      if (room.getName().equalsIgnoreCase(roomName)) {
        // Directly check if the room is of type PROCEDURE or EXAM
        if (room.getType() == Room.RoomType.PROCEDURE || room.getType() == Room.RoomType.EXAM) {
          // The room is considered occupied if there are any patients assigned to it
          return !room.getAssignedPatients().isEmpty();
        }
        // For any room types other than PROCEDURE or EXAM,
        // consider them not occupied for this check
        return false;
      }
    }
    // If no room with the given name is found, or it's not a PROCEDURE or EXAM room, return false
    return false;
  }

  /**
   * Checks if a patient is assigned to an "exam" or "procedure" room.
   *
   * @param patient The patient to check for room assignment.
   * @return true if the patient is in an exam or procedure room, false otherwise.
   */
  @Override
  public boolean isPatientInExamOrProcedureRoom(Patient patient) throws IllegalArgumentException {
    // Validate parameters
    if (roomAssignments == null) {
      throw new IllegalArgumentException("Room assignments map cannot be null.");
    }
    if (patient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    for (Map.Entry<Room, Patient> entry : roomAssignments.entrySet()) {
      Room room = entry.getKey();
      Patient assignedPatient = entry.getValue();

      // Check if the patient matches and if the room is of type EXAM or PROCEDURE
      if (patient.equals(assignedPatient)
          && (room.getType() == Room.RoomType.EXAM || room.getType() == Room.RoomType.PROCEDURE)) {
        return true; // Patient is in an exam or procedure room
      }
    }
    return false; // Patient is not in an exam or procedure room
  }

  /**
   * Displays a seating chart with room assignments, including assigned patients for each room.
   */
  @Override
  public void displaySeatingChart() throws IllegalArgumentException {
    // Validate parameter
    if (rooms == null) {
      throw new IllegalArgumentException("Rooms list cannot be null.");
    }

    for (Room room : rooms) {
      System.out.println(room); // Utilize toString() method of Room class

      // Get the assigned patients for the room
      List<Patient> assignedPatients = getPatientsInRoom(room);

      if (!assignedPatients.isEmpty()) {
        System.out.println("Assigned Patient(s):");
        for (Patient patient : assignedPatients) {
          System.out.println("- " + patient.getFullName());
        }
      } else {
        System.out.println("No patients assigned to this room.");
      }

      System.out.println("--------------------------");
    }
  }


  /**
   * Helper method to check for duplicate patients based on first name, last name, and DOB.
   *
   * @param newPatient The patient to check for duplicates.
   * @return true if a duplicate patient is found, false otherwise.
   */
  @Override
  public boolean isDuplicatePatient(Patient newPatient) throws IllegalArgumentException {
    // Validate parameter
    if (newPatient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    for (Patient existingPatient : patients) {
      if (existingPatient.getFirstName().equalsIgnoreCase(newPatient.getFirstName())
          && existingPatient.getLastName().equalsIgnoreCase(newPatient.getLastName())
          && existingPatient.getDateOfBirth().equals(newPatient.getDateOfBirth())) {
        return true; // Duplicate patient found
      }
    }
    return false; // No duplicate patient found
  }

  /**
   * Gets the assigned room for a patient.
   *
   * @param patient The patient whose assigned room is to be retrieved.
   * @return The assigned room for the patient, or null if not assigned to any room.
   */
  @Override
  public Room getAssignedRoomForPatient(Patient patient) throws IllegalArgumentException {
    // Validate parameter
    if (patient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    for (Map.Entry<Room, Patient> entry : roomAssignments.entrySet()) {
      if (entry.getValue().equals(patient)) {
        return entry.getKey();
      }
    }
    return null; // Return null if the patient is not assigned to any room
  }


  /**
   * Lists all registered patients along with their details such as
   * serial number, name, date of birth, room number, room name, and room type.
   * If no patients are registered, a message indicating the absence of
   * registered patients is printed.
   *
   * @throws NullPointerException if any of the patients' fields
   *              (serial number, name, date of birth, room number, room name, room type) are null
   */
  @Override
  public void listAllPatients() throws IllegalArgumentException {
    // Validate patients list
    if (patients == null) {
      throw new IllegalArgumentException("Patients list cannot be null.");
    }

    if (patients.isEmpty()) {
      System.out.println("No patients are currently registered.");
      return;
    }

    System.out.println("List of Registered Patients:");
    System.out.println("Serial Number | Name | Date of Birth"
        +
        " | Room Number | Room Name | Room Type");
    for (Patient patient : patients) {
      if (patient.deactivated) {
        continue;
      }
      // Assuming that getRoomNumber() returns an int.
      // If it's Integer, it can be null, hence no need for null check.
      String roomNumber = patient.getRoomNumber() > 0
          ? String.valueOf(patient.getRoomNumber()) : "Not assigned";

      // Assuming that getRoomName() and getRoomType() methods exist and return a String.
      // If room name or type could be null, you might want to handle that with similar checks.
      String roomName = patient.getRoomName() != null ? patient.getRoomName() : "Not assigned";
      String roomType = patient.getRoomType() != null
          ? patient.getRoomType().getType() : "Not assigned";

      System.out.println(patient.getSerialNumber()
          + " | " + patient.getFullName() + " | "
          + patient.getDateOfBirth().format(DateTimeFormatter.ofPattern("M/d/yyyy"))
          + " | " + roomNumber + " | " + roomName + " | " + roomType);
    }
  }

  /**
   *
   * This method iterates through the list of patients and compares each
   * patient's first name, last name,
   * and date of birth with the provided parameters. The comparison of first
   * name and last name is case-insensitive,
   * while the date of birth must match exactly.
   * @param serialNumber patient serial number
   * @return patient object
   */
  @Override
  public Patient findPatientBySerialNumber(int serialNumber)throws IllegalArgumentException {
    // Validate patients list
    if (patients == null) {
      throw new IllegalArgumentException("Patients list cannot be null.");
    }

    for (Patient patient : patients) {
      if (patient.getSerialNumber() == serialNumber) {
        return patient;
      }
    }
    return null; // No matching patient found
  }

  /**
   * Finds and returns a staff member by their serial number.
   *
   * @param serialNumber the serial number of the staff member to find
   * @return the staff member with the given serial number,
   *          or null if no staff member is found with the specified serial number
   */
  public Staff findStaffBySerialNumber(int serialNumber)throws IllegalArgumentException {
    // Validate staff list
    if (staff == null) {
      throw new IllegalArgumentException("Staff list cannot be null.");
    }

    for (Staff staffMember : staff) {
      if (staffMember.getSerialNumber() == serialNumber) {
        return staffMember;
      }
    }
    return null; // Return null if no staff member with the given serial number is found
  }

  /**
   * Computes a hash code for this clinic.
   *
   * @return a hash code value for this clinic.
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, rooms, patients, staff,
        roomAssignments, waitingRoomAssignments, patientAssignments);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare.
   * @return {@code true} if this clinic is the same as the obj argument; {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Clinic clinic = (Clinic) obj;
    return Objects.equals(name, clinic.name)
        &&
        Objects.equals(rooms, clinic.rooms)
        &&
        Objects.equals(patients, clinic.patients)
        &&
        Objects.equals(staff, clinic.staff)
        &&
        Objects.equals(roomAssignments, clinic.roomAssignments)
        &&
        Objects.equals(waitingRoomAssignments, clinic.waitingRoomAssignments)
        &&
        Objects.equals(patientAssignments, clinic.patientAssignments);
  }


  /**
   * Helper to select patient by Serial Number.
   *
   * @param sc the Scanner object for user input
   * @throws IllegalArgumentException if an I/O error occurs
   */
  @Override
  public Patient selectPatientBySerialNumber(Scanner sc)throws IllegalArgumentException {
    listAllPatients(); // List all patients
    System.out.println("Enter the serial number of the"
        +
        " patient, or enter 'q' to quit:");

    while (true) {
      String input = sc.nextLine().trim();
      if ("q".equalsIgnoreCase(input)) {
        System.out.println("Operation cancelled.");
        return null; // Indicates the user opted to cancel the operation
      }

      try {
        int serialNumber = Integer.parseInt(input);
        Patient patientToAssign = findPatientBySerialNumber(serialNumber);
        if (patientToAssign != null && !patientToAssign.deactivated) {
          return patientToAssign; // Successfully found the patient
        } else {
          System.out.println("No patient found with the given serial number. "
              +
              "Please try again or enter 'q' to quit:");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format. Please enter "
            +
            "a valid serial number or 'q' to quit:");
      }
    }
  }

  /**
   * Deactivates a staff member based on the provided serial number.
   * If the operation is successful, the staff member is deactivated,
   * and any assigned patients are disassociated from the clinical staff member.
   *
   * @param sc the scanner used to read user input
   * @return true if the staff member is successfully deactivated,
   *        false if the operation is cancelled
   */
  public boolean deactivateStaff(Scanner sc) throws IllegalArgumentException {
    System.out.println("Enter the serial number of the "
        +
        "staff member to deactivate, or enter 'q' to quit:");

    while (true) {
      String input = sc.nextLine().trim();
      if ("q".equalsIgnoreCase(input)) {
        System.out.println("Operation cancelled.");
        return false; // Indicates the user opted to cancel the operation
      }

      try {
        // Attempt to parse the input as an integer
        int serialNumber = Integer.parseInt(input);

        // Check if the parsed number is less than or equal to zero
        if (serialNumber <= 0) {
          System.out.println("Error: Invalid serial number. "
              +
              "Serial number must be a positive integer.");
          continue; // Illegal argument provided, continue to prompt for input
        }

        boolean found = false;
        for (Staff staffMember : staff) {
          if (staffMember.getSerialNumber() == serialNumber && !staffMember.isDeactivated()) {
            found = true;

            // Check if the staff member is a clinical staff
            if (staffMember instanceof ClinicalStaff) {
              ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;

              // Remove the assigned patients from the clinical staff member
              List<Patient> assignedPatients = clinicalStaff.getAssignedPatients();
              for (Patient patient : assignedPatients) {
                patient.getAssignedClinicalStaff().remove(clinicalStaff);
              }
              assignedPatients.clear(); // Clear the list of assigned patients
            }
            // Deactivate the staff member
            staffMember.setDeactivated(true);
            System.out.println("Staff member with serial number " + serialNumber
                + " deactivated successfully.");
            return true;
          }
        }
        if (!found) {
          throw new IllegalArgumentException("Error: Staff member with serial number "
              + serialNumber
              + " not found or already deactivated.");
        }
      } catch (NumberFormatException e) {
        // Handle the case where the input cannot be parsed as an integer
        System.out.println("Invalid input. Please enter a valid serial number or "
            +
            "'q' to quit:");
      }
    }
  }

  /**
   * Assigns a clinical staff member to a patient.
   *
   * @param patientToStaff The patient to whom the clinical staff member will be assigned.
   * @param serialNumber The serial number of the clinical staff member to assign.
   * @param controller The controller object used for certain operations.
   */
  @Override
  public void assignClinicalStaffToPatient(Patient patientToStaff,
                                           int serialNumber,
                                           AssignPatientToStaffCommand controller)

      throws IllegalArgumentException {
    // Check if the clinical staff member is already assigned to the patient
    ClinicalStaff clinicalStaffMember
        = ClinicalStaff.findClinicalStaffBySerialNumber(this, serialNumber);
    if (clinicalStaffMember != null && !clinicalStaffMember.getAssignedPatients()
        .contains(patientToStaff) && !clinicalStaffMember.isDeactivated()) {
      // Check if the patient is already assigned to the clinical staff member
      if (!patientToStaff.getAssignedClinicalStaff().contains(clinicalStaffMember)) {
        patientToStaff.getAssignedClinicalStaff().add(clinicalStaffMember);
        clinicalStaffMember.getAssignedPatients().add(patientToStaff);
        clinicalStaffMember.assignPatientforCount(patientToStaff);
        // This line updates the unique patient count.
        System.out.println(clinicalStaffMember.getFullName()
            + " has been added to the patient's care team.");
      } else {
        System.out.println("Error: The patient is already"
            +
            " assigned to this clinical staff member.");
      }
    } else {
      System.out.println("Error: The selected staff member is"
          +
          " not a clinical staff or is already assigned to the patient.");
    }
  }

  /**
   * Lists clinical staff members who have at least one assigned patient with an incomplete visit.
   * For each clinical staff member, lists the currently assigned patients.
   */
  public void listClinicalStaffWithIncompleteVisits() throws IllegalArgumentException {
    System.out.println("Clinical Staff with Active Patients:");
    for (Staff staffMember : staff) { // Directly using the staff member variable
      if (staffMember instanceof ClinicalStaff && !staffMember.isDeactivated()) {
        ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;
        List<Patient> assignedPatients = clinicalStaff.getAssignedPatients();
        boolean hasIncompleteVisit = false;
        for (Patient patient : assignedPatients) {
          if (!patient.isDeactivated()) { // Corrected logic for incomplete visit
            hasIncompleteVisit = true;
            break;
          }
        }
        if (hasIncompleteVisit) {
          System.out.println("Name: " + clinicalStaff.getFullName());
          System.out.println("Job Title: " + clinicalStaff.getJobTitle());
          System.out.println("Currently Assigned Patients with Ongoing Visits:");
          for (Patient patient : assignedPatients) {
            if (!patient.isDeactivated()) {
              System.out.println("- " + patient.getFullName()); // Assuming getFullName exists
            }
          }
          System.out.println("--------------------------");
        }
      }
    }
  }

  /**
   * Allows the user to unassign a clinical staff member from a patient.
   * This method prompts the user to enter the serial number of the clinical staff member
   * they wish to unassign from a patient, and then prompts for the serial number of the patient
   * to unassign. The method then removes the association between the specified patient
   * and clinical staff member.
   *
   * @param sc Scanner object used for user input.
   * @return {@code true} if the unassignment operation was successful, {@code false} otherwise.
   *         Returns {@code false} if the user cancels the operation or if there was an error.
   */
  @Override
  public boolean unassignClinicalStaffFromPatient(Scanner sc)throws IllegalArgumentException {
    System.out.println("Enter the serial number of the clinical "
        +
        "staff member for unassignment, or enter 'q' to quit:");

    while (true) {
      String input = sc.nextLine().trim();
      if ("q".equalsIgnoreCase(input)) {
        System.out.println("Operation cancelled.");
        return false; // Indicates the user opted to cancel the operation
      }

      try {
        int serialNumber = Integer.parseInt(input);
        if (serialNumber <= 0) {
          System.out.println("Error: Invalid serial number. Serial number "
              +
              "must be a positive integer.");
          continue;
        }

        ClinicalStaff clinicalStaff =
            ClinicalStaff.findClinicalStaffBySerialNumber(this, serialNumber);
        if (clinicalStaff != null && !clinicalStaff.isDeactivated()) {
          List<Patient> assignedPatients = clinicalStaff.getAssignedPatients();
          if (assignedPatients.isEmpty()) {
            System.out.println("This clinical staff member has no assigned patients.");
            return false;
          }

          System.out.println("Assigned Patients:");
          for (Patient patient : assignedPatients) {
            System.out.println("Serial Number: " + patient.getSerialNumber()
                + ", Name: " + patient.getFullName());
          }

          System.out.println("Enter the serial number of the patient to unassign, "
              +
              "or 'q' to quit:");
          input = sc.nextLine().trim();
          if ("q".equalsIgnoreCase(input)) {
            System.out.println("Operation cancelled.");
            return false;
          }

          int patientSerialNumber = Integer.parseInt(input);
          Patient patientToUnassign = null;
          for (Patient patient : assignedPatients) {
            if (patient.getSerialNumber() == patientSerialNumber) {
              patientToUnassign = patient;
              break;
            }
          }

          if (patientToUnassign != null) {
            clinicalStaff.getAssignedPatients().remove(patientToUnassign);
            patientToUnassign.getAssignedClinicalStaff().remove(clinicalStaff);
            System.out.println("Clinical staff member unassigned from patient successfully.");
            return true;
          } else {
            System.out.println("Error: Patient with serial number "
                + patientSerialNumber + " not found among the assigned patients.");
          }
        } else {
          System.out.println("Error: Clinical staff member with serial number "
              + serialNumber + " not found or is deactivated.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid serial number or"
            +
            " 'q' to quit:");
      }
    }
  }

  /**
   * Lists all clinical staff members along with the count of patients assigned
   * to each staff member.
   * For each clinical staff member, prints their full name, serial number,
   * number of unique patients assigned, and their status (active or inactive).
   */
  @Override
  public void listClinicalStaffAndPatientCounts() throws IllegalArgumentException {
    System.out.println("Clinical Staff and Their patient Assignment Counts:");
    for (Staff staffMember : staff) {
      if (staffMember instanceof ClinicalStaff) {
        ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;
        int uniquePatientCount = clinicalStaff.getUniquePatientCount();
        String status = clinicalStaff.isDeactivated() ? "Inactive" : "Active";
        System.out.println("Name: " + clinicalStaff.getFullName()
            +
            ", Serial Number: " + clinicalStaff.getSerialNumber()
            +
            ", Unique Patients Assigned: " + uniquePatientCount
            +
            ", Status: " + status);
      }
    }
  }

  /**
   * Lists patients who have not visited for more than a year.
   * For each inactive patient based on last visit and deactivated date,
   * prints their serial number, full name,
   * and the date of their last visit.
   * If no inactive patients are found, prints a message indicating
   * that there are no inactive patients for more than a year.
   */
  @Override
  public void listInactivePatientsForYear() throws IllegalArgumentException {
    System.out.println("Listing patients who have been inactive for more than"
        +
        " a year and are currently deactivated:");
    boolean foundInactivePatient = false;

    for (Patient patient : patients) {
      // Only proceed if the patient is deactivated
      if (patient.isDeactivated()) {
        // Get the last deactivation date
        LocalDate lastDeactivationDate = patient.getLastDeactivationDate();

        // Calculate the number of days since the last deactivation
        long daysSinceLastDeactivation = lastDeactivationDate != null ? ChronoUnit.DAYS
            .between(lastDeactivationDate, LocalDate.now()) : Long.MAX_VALUE;

        // Check if the deactivation was over 365 days ago
        if (daysSinceLastDeactivation > 365) {
          foundInactivePatient = true;
          System.out.println("Patient Serial Number: " + patient.getSerialNumber()
              + ", Name: " + patient.getFullName()
              + ", Last Deactivation: " + lastDeactivationDate);
        }
      }
    }

    if (!foundInactivePatient) {
      System.out.println("No patients meet the criteria of being inactive for more than a year.");
    }
  }


  /**
   * Lists patients who have made two or more visits in the past 365 days.
   * For each eligible patient, prints their full name, serial number,
   * and the number of visits they made in the last year.
   */
  @Override
  public void listPatientsWithMultipleVisitsInLastYear() throws IllegalArgumentException {
    System.out.println("Patients with two or more visits in the past 365 days:");
    LocalDate oneYearAgo = LocalDate.now().minusDays(365);

    for (Patient patient : patients) {
      int recentVisitCount = 0;

      for (Visitrecord visitRecord : patient.getVisitRecords()) {
        if (visitRecord.getRegistrationDateTime().toLocalDate().isAfter(oneYearAgo)) {
          recentVisitCount++;
        }
      }

      if (recentVisitCount >= 2) {
        System.out.println("Patient: "
            + patient.getFullName()
            +
            " - Serial Number: "
            + patient.getSerialNumber()
            +
            " - Number of Visits in Last Year: "
            + recentVisitCount);
      }
    }
  }

  /**
   * Displays a list of registered patients and allows the user to
   * select a patient by their serial number.
   * Once a patient is selected, the method displays all clinical
   * staff assigned to that patient.
   * The user can interactively select a patient and view the associated
   * clinical staff details,
   * including their names, job titles, and NPI numbers.
   *
   * Usage:
   * - The method lists all patients with their serial numbers and names.
   * - The user is prompted to input a patient's serial number.
   * - If a valid serial number is entered, the method displays all clinical
   * staff assigned to the selected patient.
   * - If no clinical staff are assigned to the patient, a message indicating
   * this is displayed.
   * - The user can exit the selection process by entering 'q'.
   *
   * @param sc The Scanner instance used to read input from the user. It should be opened
   *           and ready to use before calling this method.
   */
  @Override
  public void displayPatientStaff(Scanner sc) throws IllegalArgumentException {
    if (patients.isEmpty()) {
      System.out.println("There are no patients registered in the clinic.");
      return;
    }

    System.out.println("List of Registered Patients:");
    for (Patient patient : patients) {
      System.out.println("Serial Number: " + patient.getSerialNumber()
          + ", Name: " + patient.getFullName());
    }

    System.out.println("Enter the serial number of the patient to view"
        +
        " assigned clinical staff, or enter 'q' to quit:");

    while (true) {
      String input = sc.nextLine().trim();
      if ("q".equalsIgnoreCase(input)) {
        System.out.println("Operation cancelled.");
        return;
      }

      try {
        int serialNumber = Integer.parseInt(input);
        Patient selectedPatient = findPatientBySerialNumber(serialNumber);
        if (selectedPatient == null) {
          System.out.println("Patient with serial number " + serialNumber
              + " not found. Please try again or enter 'q' to quit:");
        } else {
          List<ClinicalStaff> assignedStaff = selectedPatient.getAssignedClinicalStaff();
          if (assignedStaff.isEmpty()) {
            System.out.println("No clinical staff are assigned to "
                + selectedPatient.getFullName() + ".");
          } else {
            System.out.println("Clinical Staff assigned to "
                + selectedPatient.getFullName() + ":");
            for (ClinicalStaff staff : assignedStaff) {
              System.out.println("Name: " + staff.getFullName()
                  + ", Job Title: " + staff.getJobTitle() + ", NPI: " + staff.getNpi());
            }
          }
          return;
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid "
            +
            "serial number or 'q' to quit:");
      }
    }
  }

  /**
   * Finds an existing patient in the clinic based on the provided patient details.
   *
   * @param newPatient The patient object to search for.
   * @return The existing patient object if found, or null if not found.
   * @throws IllegalArgumentException if the newPatient parameter is null.
   */
  @Override
  public Patient findExistingPatient(Patient newPatient) throws IllegalArgumentException {
    // Validate parameter
    if (newPatient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    // Iterate through the list of patients to find a match
    for (Patient existingPatient : patients) {
      // Check if the patient details match
      if (existingPatient.getFirstName().equalsIgnoreCase(newPatient.getFirstName())
          && existingPatient.getLastName().equalsIgnoreCase(newPatient.getLastName())
          && existingPatient.getDateOfBirth().equals(newPatient.getDateOfBirth())) {
        return existingPatient; // Existing patient found
      }
    }
    return null; // No existing patient found
  }

}




