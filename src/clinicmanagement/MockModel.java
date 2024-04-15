package clinicmanagement;

import java.awt.Dimension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The {@code Clinic} class represents a medical clinic and manages its rooms,
 * patients, staff, and assignments.
 */
public class MockModel extends Clinic {
  private String name;
  private final List<Room> rooms;
  private final List<Patient> patients;
  private final List<Staff> staff;
  private final Map<Room, Patient> roomAssignments;
  private final Map<Room, List<Patient>> waitingRoomAssignments;
  private final Map<Patient, List<Staff>> patientAssignments;

  /**
   * Constructs a new clinic with empty lists and maps for rooms, patients, staff, and assignments.
   **/
  public MockModel() throws IllegalArgumentException {
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
   * Clear data from the clinic.
   */
  public void clear() {
    this.rooms.clear();
    this.patients.clear();
    this.staff.clear();
    this.roomAssignments.clear();
    this.waitingRoomAssignments.clear();
    this.patientAssignments.clear();
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
        // Update the existing patient's room information and add them to the room
        Room room = getRoomByNumber(1);
        // Assuming logic to get the correct room number
        if (room != null) {
          existingPatient.setRoomName(room.getName());
          existingPatient.setRoomType(room.getType());
          room.getAssignedPatients().add(existingPatient);
          // Add the patient to the room's list
          JOptionPane.showMessageDialog(null,
              "Duplicate patient found. Reactivated patient: "
                  + existingPatient.getFullName());
          return existingPatient;
        } else {
          JOptionPane.showMessageDialog(null,
              "Primary Waiting Room with number " + 1 + " not found.");
        }
      } else {
        // Inform user about the duplicate patient
        JOptionPane.showMessageDialog(null,
            "Duplicate patient found. This patient is already registered.");
      }
    } else {
      // Proceed with registering the new patient
      Room room = getRoomByNumber(1);
      // Assuming logic to get the correct room number
      if (room != null) {
        newPatient.setRoomName(room.getName());
        newPatient.setRoomType(room.getType());
        room.getAssignedPatients().add(newPatient); // Add the patient to the room's list
        patients.add(newPatient); // Also add the patient to the clinic's overall list
        roomAssignments.put(room, newPatient); // Update room assignments map
        return newPatient;
      }
    }
    return null; // Return null if patient registration fails
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
  @Override
  public List<Room> getRooms() {
    return rooms;
  }




  /**
   * Sends a patient home, approved by a clinical staff member.
   *
   * @param patient        The patient to send home.
   * @param approvingStaff The clinical staff member approving the patient's discharge.
   */
  @Override
  public void sendPatientHome(Patient patient, ClinicalStaff approvingStaff) {
    if (patient == null) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    if (approvingStaff == null) {
      throw new IllegalArgumentException("Approving staff cannot be null.");
    }

    // Check if the patient is already deactivated before attempting to deactivate
    if (patient.isDeactivated()) {
      LocalDate lastDeactivationDate = patient.getLastDeactivationDate();
      throw new IllegalStateException("Patient " + patient.getFullName()
          + " cannot be deactivated again,"
          + " as they were already deactivated"
          + (lastDeactivationDate != null ? " on " + lastDeactivationDate : "") + ".");
    }

    patient.setDeactivated(true);

    // Double-check if deactivation was successful
    if (!patient.isDeactivated()) {
      throw new IllegalStateException("Error: Failed to deactivate patient.");
    }

    // Remove the patient from any room assignments
    if (rooms != null) {
      rooms.forEach(room -> room.getAssignedPatients().remove(patient));
      rooms.forEach(room -> roomAssignments.remove(room, patient));
    }

    // Remove the patient from any clinical staff's list of assigned patients
    if (staff != null) {
      staff.forEach(staffMember -> {
        if (staffMember instanceof ClinicalStaff) {
          ((ClinicalStaff) staffMember).getAssignedPatients().remove(patient);
        }
      });
    }
  }


  @Override
  public void assignClinicalStaffToPatient(Patient patientToStaff,
                                           ClinicalStaff clinicalStaffMember) {
    if (clinicalStaffMember != null && !clinicalStaffMember.isDeactivated()) {
      if (!clinicalStaffMember.getAssignedPatients().contains(patientToStaff)) {
        patientToStaff.assignClinicalStaff(clinicalStaffMember); // Use the new method
        clinicalStaffMember.getAssignedPatients().add(patientToStaff);
        // Assuming getAssignedPatients is mutable
        clinicalStaffMember.assignPatientforCount(patientToStaff); // Handle patient count


      }
    }
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
          return room; // Return the room if the patient
          // is found in its assigned patients list
        }
      }
    }
    return null; // Return null if the patient is not assigned to any room
  }

  @Override
  public List<ClinicalStaff> getClinicalStaffList() {
    List<ClinicalStaff> clinicalStaffList = new ArrayList<>();
    for (Staff staffMember : staff) {
      if (staffMember instanceof ClinicalStaff) {
        clinicalStaffList.add((ClinicalStaff) staffMember);
      }
    }
    return clinicalStaffList;
  }

  /**
   * Prints a list of clinical staff members.
   */
  @Override
  public void showClinicalStaffList(JFrame frame) {
    if (staff == null) {
      throw new IllegalArgumentException("Staff list cannot be null.");
    }

    StringBuilder staffListBuilder = new StringBuilder();
    staffListBuilder.append("<html><body>Clinical Staff List:<br>");

    for (Staff staffMember : staff) {
      if (staffMember instanceof ClinicalStaff) {
        ClinicalStaff clinicalStaff = (ClinicalStaff) staffMember;
        if (!clinicalStaff.isDeactivated()) {
          String prefix = clinicalStaff.getPrefix();
          // getPrefix method should exist in ClinicalStaff
          staffListBuilder.append("Serial Number: ").append(clinicalStaff
              .getSerialNumber()).append("<br>");
          staffListBuilder.append("Name: ").append(prefix).append(" ")
              .append(clinicalStaff.getFullName()).append("<br>");
          staffListBuilder.append("Job Title: ").append(clinicalStaff
              .getJobTitle()).append("<br>");
          staffListBuilder.append("Education Level: ").append(clinicalStaff
              .getEducationLevel()).append("<br>");
          staffListBuilder.append("Unique Identifier (NPI): ")
              .append(clinicalStaff.getNpi()).append("<br>");
          staffListBuilder.append("--------------------------<br>");
        }
      }
    }

    staffListBuilder.append("</body></html>");
    JOptionPane.showMessageDialog(frame, staffListBuilder.toString(),
        "Clinical Staff List", JOptionPane.INFORMATION_MESSAGE);
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
        if (room.getType() == Room.RoomType.PROCEDURE || room.getType()
            == Room.RoomType.EXAM) {
          System.out.println(room.getAssignedPatients().get(0).getFirstName());
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
   * @param first name of patient
   * @param last name o f patient
   * @param dob date of birth
   * @return The assigned room for the patient, or null if not assigned to any room.
   */
  public Room getAssignedRoomForPatient(String first, String last, String dob)
      throws IllegalArgumentException {
    // Validate parameter
    if (first == null || last == null || dob == null || first.isEmpty()
        || last.isEmpty() || dob.isEmpty()) {
      throw new IllegalArgumentException("Patient cannot be null.");
    }

    Patient patient = new Patient(0, first, last, dob);

    for (Map.Entry<Room, Patient> entry : roomAssignments.entrySet()) {
      if (entry.getValue().equals(patient)) {
        return entry.getKey();
      }
    }
    return null; // Return null if the patient is not assigned to any room
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
    MockModel clinic = (MockModel) obj;
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

  /**
   * Retrieves a list of all active patients in the clinic for Mock Test.
   *
   * @return A list containing all active patients.
   * @throws IllegalArgumentException If the patients list is null.
   */
  @Override
  public List<Patient> getAllPatients() throws IllegalArgumentException {
    // Validate patients list
    if (patients == null) {
      throw new IllegalArgumentException("Patients list cannot be null.");
    }

    List<Patient> activePatients = new ArrayList<>();
    for (Patient patient : patients) {
      if (!patient.deactivated) {
        activePatients.add(patient);
      }
    }
    return activePatients;
  }


  /**
   * Registers a new patient using the provided information from the GUI.
   * This method creates text fields for entering the patient's first name,
   * last name, and date of birth (in the format "M/d/yyyy"). It then extracts
   * the entered information from the text fields, parses the date of birth
   * string into a LocalDate object, and creates a new Patient instance with
   * the provided information. Finally, it invokes the registerNewPatient()
   * method of the associated Clinic object to register the new patient.
   *
   * @param guiController The MockView instance representing the GUI controller.
   * @param mockFirstName The mock first name to prefill in the first name field.
   * @param mockLastName The mock last name to prefill in the last name field.
   * @param mockdob The mock date of birth to prefill in the date of birth field.
   */
  public void registerNewPatientGui(MockView guiController,
                                    String mockFirstName,
                                    String mockLastName, String mockdob) {
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField dobField = new JTextField();

    firstNameField.setText(mockFirstName);
    lastNameField.setText(mockLastName);
    dobField.setText(mockdob);

    Object[] message = {
        "First Name:", firstNameField,
        "Last Name:", lastNameField,
        "Date of Birth (M/d/yyyy):", dobField,
    };

    String firstName = firstNameField.getText().trim();
    String lastName = lastNameField.getText().trim();
    String dobStr = dobField.getText().trim();
    LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("M/d/yyyy"));

    // Placeholder for room number - ensure your Patient constructor handles this appropriately
    Patient newPatient = new Patient(0, firstName, lastName, dobStr);
    registerNewPatient(newPatient);

  }

  /**
   * Assigns a patient to a room based on predefined patient and room selections.
   * This method selects the first patient and the second room from the available lists,
   * assigns the selected patient to the selected room, and updates the room assignments.
   *
   * @param guiController The MockView instance representing the GUI controller.
   */
  public void assignPatientToRoomGui(MockView guiController) {
    List<Patient> patients = getAllPatients();

    Patient selectedPatient = (Patient) patients.get(0);
    Room clickedRoom = this.rooms.get(1);

    selectedPatient.setRoomName(clickedRoom.getName());
    clickedRoom.getAssignedPatients().add(selectedPatient); // Add the patient to the room's list
    roomAssignments.remove(this.rooms.get(0), selectedPatient);
    roomAssignments.put(clickedRoom, selectedPatient); // Update room assig
    assignPatientToRoom(selectedPatient, clickedRoom.getName());

  }

  /**
   * Adds a visit record for a selected patient with the provided information.
   * This method retrieves the selected patient from the available list of patients
   * and adds a visit record containing the specified time, complaint, and temperature.
   *
   * @param guiController The MockView instance representing the GUI controller.
   * @param patient The index of the selected patient in the list of all patients.
   * @param time The time of the visit as a LocalDateTime object.
   * @param complaint The complaint or reason for the visit.
   * @param temp The patient's temperature recorded during the visit.
   */
  public void addVisitRecordGui(MockView guiController,
                                int patient, LocalDateTime time, String complaint, double temp) {
    List<Patient> patients = getAllPatients();
    Patient selectedPatient = patients.get(patient);

    // Proceed to add a visit record for the selected patient
    selectedPatient.addVisitRecord(time, complaint, temp);
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
   * Registers a new clinical staff member with the provided information.
   * This method creates input fields for entering the staff member's first name, last name,
   * job title, educational level, and NPI. It then validates the NPI, creates a new
   * ClinicalStaff instance with the provided information, and registers the staff member.
   *
   * @param guiController The MockView instance representing the GUI controller.
   * @param mockFirstName The mock first name to prefill in the first name field.
   * @param mockLastName The mock last name to prefill in the last name field.
   * @param mockJob The mock job title to preselect in the job title field.
   * @param mockEducation The mock educational level to preselect in the educational
   *                      level field.
   * @param mocknpi The mock NPI to prefill in the NPI field.
   */
  public void registerNewClinicalStaff(MockView guiController,
                                       String mockFirstName, String mockLastName,
                                       String mockJob, String mockEducation, String mocknpi) {
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JComboBox<String> jobTitleField = new JComboBox<>(new String[] {"Physician",
        "Nurse", "Technician"});
    final JComboBox<String> educationalLevelField = new JComboBox<>(new String[] {"Doctoral",
        "Masters", "Allied"});
    final JTextField npiField = new JTextField();
    firstNameField.setText(mockFirstName);
    lastNameField.setText(mockLastName);
    jobTitleField.setSelectedItem(mockJob);
    educationalLevelField.setSelectedItem(mockEducation);
    npiField.setText(mocknpi);

    String firstName = firstNameField.getText().trim();
    String lastName = lastNameField.getText().trim();
    String jobTitle = (String) jobTitleField.getSelectedItem();
    String educationalLevel = (String) educationalLevelField.getSelectedItem();
    String npi = npiField.getText().trim();

    // Validate NPI
    if (!npi.matches("\\d{10}")) {
      return;
    }

    // Assuming ClinicalStaff constructor matches this signature
    ClinicalStaff newStaff = new ClinicalStaff(jobTitle,
        firstName, lastName, Staff.EducationLevel.valueOf(educationalLevel.toUpperCase()), npi);
    registerNewClinicalStaff(newStaff);

  }

  /**
   * Assigns a clinical staff member to a selected patient based on predefined selections.
   * This method selects a patient and a clinical staff member from the available lists
   * and assigns the selected staff to the selected patient.
   *
   * @param staff The index of the selected staff member in the list of all clinical staff.
   * @param patient The index of the selected patient in the list of all patients.
   */
  public void assignStaffToPatientGui(int staff, int patient) {
    // Select a patient

    List<Patient> allPatients = getAllPatients();
    if (allPatients.isEmpty()) {
      return;
    }

    Patient selectedPatient = (Patient) patients.get(patient);

    List<ClinicalStaff> availableStaff = getClinicalStaffList();

    ClinicalStaff selectedStaff = availableStaff.get(staff);

    // Assign the selected staff to the selected patient
    assignClinicalStaffToPatient(selectedPatient, selectedStaff);

  }

  /**
   * Sends a selected patient home under the care of a predefined clinical staff member.
   * This method selects a patient and a clinical staff member from the available lists
   * and sends the selected patient home under the care of the selected staff member.
   *
   * @param guiController The MockView instance representing the GUI controller.
   * @param patient The index of the selected patient in the list of all patients.
   */
  public void sendPatientHomeGui(MockView guiController, int patient) {
    // Select a patient

    List<Patient> allPatients = getAllPatients();
    if (allPatients.isEmpty()) {
      return;
    }

    Patient selectedPatient = (Patient) patients.get(patient);

    // Select approving clinical staff (only Physicians)

    List<ClinicalStaff> allStaff = getClinicalStaffList();

    ClinicalStaff selectedStaff = allStaff.get(0);

    // Execute the send home action
    sendPatientHome(selectedPatient, selectedStaff);

  }

  /**
   * Deactivates a clinical staff member and unassigns them from all patients.
   *
   * @param staff The index of the clinical staff member to deactivate.
   */
  public void deactivateStaffGui(int staff) {
    // Select a clinical staff member to deactivate

    List<ClinicalStaff> allStaff = getClinicalStaffList();

    ClinicalStaff selectedStaff = allStaff.get(staff);

    // Iterate through all patients and unassign
    // the staff member using the unassignClinicalStaff method
    for (Patient patient : getAllPatients()) {
      if (patient.getAssignedClinicalStaff().contains(selectedStaff)) {
        patient.unassignClinicalStaff(selectedStaff);
      }
    }

    // Deactivate the staff member
    selectedStaff.setDeactivated(true);

  }

  /**
   * Displays detailed information about a selected active patient in a dialog.
   */
  @Override
  public void showPatientDetailsGui() {
    // Initialize the patient selection combo box
    JComboBox<Patient> patientComboBox = new JComboBox<>();
    List<Patient> allPatients = getAllPatients(); // Retrieve all patients
    if (allPatients.isEmpty()) {
      JOptionPane.showMessageDialog(null,
          "There are no patients available.",
          "No Patients", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    // Add only active patients to the combo box
    allPatients.stream()
        .filter(patient -> !patient.isDeactivated())
        .forEach(patientComboBox::addItem);

    // Show a message if there are no active patients
    if (patientComboBox.getItemCount() == 0) {
      JOptionPane.showMessageDialog(null,
          "There are no active patients available.",
          "No Active Patients", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    // Show the combo box in a confirm dialog
    int patientChoice = JOptionPane.showConfirmDialog(null,
        patientComboBox, "Select an Active Patient", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (patientChoice != JOptionPane.OK_OPTION) {
      return;
    }
    // User cancelled or closed the dialog

    // Retrieve the selected patient and display their full information
    Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
    String patientDetails = selectedPatient.getFullInformation();
    JOptionPane.showMessageDialog(null, patientDetails,
        "Patient Details", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Unassigns a staff member from a patient.
   *
   * @param staff   The index of the staff member to assign.
   * @param patient The index of the patient to unassign the staff member from.
   */
  public void unassignStaffFromPatientGui(int staff, int patient) {
    // Select a patient

    List<Patient> allPatients = getAllPatients();
    if (allPatients.isEmpty()) {
      return;
    }

    Patient selectedPatient = (Patient) patients.get(patient);

    List<ClinicalStaff> availableStaff = getClinicalStaffList();

    ClinicalStaff selectedStaff = availableStaff.get(staff);

    // Assign the selected staff to the selected patient
    assignClinicalStaffToPatient(selectedPatient, selectedStaff);

    // Unassign the selected staff from the selected patient
    selectedPatient.unassignClinicalStaff(selectedStaff);
    selectedStaff.getAssignedPatients().remove(selectedPatient);

  }

  /**
   * Lists all clinical staff members along with the number
   * of patients each staff member is assigned to.
   */
  @Override
  public void listClinicalStaffAndPatientCountsGui() {
    // Create column names for the JTable
    String[] columnNames = {"Name", "Serial Number", "Unique Patients Assigned", "Status"};

    // Retrieve the clinical staff list
    List<ClinicalStaff> clinicalStaffList = getClinicalStaffList();
    // Assuming this method returns all clinical staff members

    // Create a two-dimensional Object array to hold the table data
    Object[][] data = new Object[clinicalStaffList.size()][4];

    // Fill the data array with clinical staff information
    int i = 0;
    for (ClinicalStaff clinicalStaff : clinicalStaffList) {
      data[i][0] = clinicalStaff.getFullName();
      data[i][1] = clinicalStaff.getSerialNumber();
      data[i][2] = clinicalStaff.getUniquePatientCount();
      data[i][3] = clinicalStaff.isDeactivated() ? "Inactive" : "Active";
      i++;
    }

    // Create a JTable to display the clinical staff and their patient counts
    JTable table = new JTable(data, columnNames);
    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
    table.setFillsViewportHeight(true);

    // Create a scroll pane to make the table scrollable
    JScrollPane scrollPane = new JScrollPane(table);

    // Create and display a dialog containing the table
    JOptionPane.showMessageDialog(null, scrollPane,
        "Clinical Staff and Their Patient Assignment Counts",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Lists inactive patients who haven't had a visit record in over a year.
   */
  @Override
  public void listInactivePatientsForYearGui() {
    StringBuilder sb = new StringBuilder();
    sb.append("Listing patients with last visit record more than a year ago:\n");
    boolean foundInactivePatient = false;

    // Assuming getAllPatients retrieves all patients, including deactivated ones
    List<Patient> allPatients = getAllPatients();

    for (Patient patient : allPatients) {
      List<Visitrecord> visitRecords = patient.getVisitRecords();
      if (!visitRecords.isEmpty()) {
        Visitrecord lastVisit = visitRecords.get(visitRecords.size() - 1);
        long daysSinceLastVisit = ChronoUnit.DAYS.between(lastVisit
            .getRegistrationDateTime().toLocalDate(), LocalDate.now());

        if (daysSinceLastVisit > 365) {
          foundInactivePatient = true;
          sb.append("Patient Serial Number: ").append(patient.getSerialNumber())
              .append(", Name: ").append(patient.getFullName())
              .append(", Last Visit Date: ").append(lastVisit
                  .getRegistrationDateTime().toLocalDate())
              .append("\n");
        }
      }
    }

    if (!foundInactivePatient) {
      sb.append("No patients found with last visit record more than a year ago.");
    }

    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 300));
    JOptionPane.showMessageDialog(null, scrollPane,
        "Patients with Last Visit Over a Year Ago", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Lists clinical staff members who have active patients with incomplete visits.
   *
   * @param clinicalStaffList The list of clinical staff members.
   * @param guiController     The GUI controller used to display the information.
   */
  public void listClinicalStaffWithIncompleteVisitsGui(List<ClinicalStaff> clinicalStaffList,
                                                       MockView guiController) {
    StringBuilder sb = new StringBuilder();
    sb.append("Clinical Staff with Active Patients and Incomplete Visits:\n");
    for (ClinicalStaff clinicalStaff : clinicalStaffList) {
      if (!clinicalStaff.isDeactivated()) {
        List<Patient> assignedPatients = clinicalStaff.getAssignedPatients();
        boolean hasIncompleteVisit = false;
        for (Patient patient : assignedPatients) {
          // Check if the patient has at least one active visit
          List<Visitrecord> visitRecords = patient.getVisitRecords();
          if (!visitRecords.isEmpty()) {
            Visitrecord lastVisit = visitRecords.get(visitRecords.size() - 1);
            if (!patient.isDeactivated() && lastVisit.islastvisitwithinayear()) {
              hasIncompleteVisit = true;
              break;
            }
          }
        }
        if (hasIncompleteVisit) {
          sb.append("Name: ").append(clinicalStaff.getFullName()).append("\n");
          sb.append("Job Title: ").append(clinicalStaff.getJobTitle()).append("\n");
          sb.append("Currently Assigned Patients with Incomplete Visits:\n");
          for (Patient patient : assignedPatients) {
            List<Visitrecord> visitRecords = patient.getVisitRecords();
            if (!visitRecords.isEmpty()) {
              Visitrecord lastVisit = visitRecords.get(visitRecords.size() - 1);
              if (!patient.isDeactivated() && lastVisit.islastvisitwithinayear()) {
                sb.append("- ").append(patient.getFullName()).append("\n");
              }
            }
          }
          sb.append("--------------------------\n");
        }
      }
    }

    // Display the information in a JOptionPane dialog
    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    JOptionPane.showMessageDialog(guiController.frame, scrollPane,
        "Clinical Staff with Incomplete Visits", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Lists patients who have had two or more visits in the past year.
   *
   * @param guiController The GUI controller used to display the information.
   */
  public void listPatientsWithMultipleVisitsInLastYear(MockView guiController) {
    StringBuilder sb = new StringBuilder();
    sb.append("Patients with two or more visits in the past 365 days:\n");
    LocalDate oneYearAgo = LocalDate.now().minusDays(365);

    for (Patient patient : getAllPatients()) {
      int recentVisitCount = 0;

      for (Visitrecord visitRecord : patient.getVisitRecords()) {
        if (visitRecord.getRegistrationDateTime().toLocalDate().isAfter(oneYearAgo)) {
          recentVisitCount++;
        }
      }

      if (recentVisitCount >= 2) {
        sb.append("Patient: ")
            .append(patient.getFullName())
            .append(" - Serial Number: ")
            .append(patient.getSerialNumber())
            .append(" - Number of Visits in Last Year: ")
            .append(recentVisitCount)
            .append("\n");
      }
    }

    // Display the information in a JOptionPane dialog
    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    JOptionPane.showMessageDialog(guiController.frame, scrollPane,
        "Patients with Multiple Visits in Last Year", JOptionPane.INFORMATION_MESSAGE);
  }
}






