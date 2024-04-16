package clinicmanagement;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
public class Clinic implements ClinicInterface {
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
        Room room = getRoomByNumber(1); // Assuming logic to get the correct room number
        if (room != null) {
          existingPatient.setRoomName(room.getName());
          existingPatient.setRoomType(room.getType());
          room.getAssignedPatients().add(existingPatient); // Add the patient to the room's list
          JOptionPane.showMessageDialog(null,
              "Duplicate patient found. Reactivated patient: " + existingPatient.getFullName());
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
      Room room = getRoomByNumber(1); // Assuming logic to get the correct room number
      if (room != null) {
        newPatient.setRoomName(room.getName());
        newPatient.setRoomType(room.getType());
        room.getAssignedPatients().add(newPatient); // Add the patient to the room's list
        patients.add(newPatient); // Also add the patient to the clinic's overall list
        roomAssignments.put(room, newPatient); // Update room assignments map
        JOptionPane.showMessageDialog(null,
            "Patient " + newPatient.getFullName()
                + " registered successfully in room "
                + room.getRoomNumber() + ": " + room.getName()
                + " (Type: " + room.getType() + ")");
        return newPatient;
      } else {
        JOptionPane.showMessageDialog(null,
            "Room with number " + 1 + " not found.");
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
      throw new IllegalStateException("Patient "
          + patient.getFullName() + " cannot be deactivated again,"
          + " as they were already deactivated"
          + (lastDeactivationDate != null ? " on "
          + lastDeactivationDate : "") + ".");
    }

    patient.setDeactivated(true);

    // Double-check if deactivation was successful
    if (!patient.isDeactivated()) {
      throw new IllegalStateException("Error: Failed to deactivate patient.");
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
  }


  @Override
  public void assignClinicalStaffToPatient(Patient patientToStaff,
                                           ClinicalStaff clinicalStaffMember) {
    if (clinicalStaffMember != null && !clinicalStaffMember.isDeactivated()) {
      if (!clinicalStaffMember.getAssignedPatients().contains(patientToStaff)) {
        patientToStaff.assignClinicalStaff(clinicalStaffMember);
        // Use the new method
        clinicalStaffMember.getAssignedPatients().add(patientToStaff);
        // Assuming getAssignedPatients is mutable
        clinicalStaffMember.assignPatientforCount(patientToStaff);
        // Handle patient count

        // GUI feedback
        JOptionPane.showMessageDialog(null,
            clinicalStaffMember.getPrefix()
                + clinicalStaffMember.getFullName() + " has been added to "
                + patientToStaff.getFullName() + "'s care team.",
            "Assignment Successful", JOptionPane.INFORMATION_MESSAGE);
      } else {
        // GUI feedback for duplicate assignment
        JOptionPane.showMessageDialog(null,
            "Error: " + patientToStaff.getFullName()
                + " is already assigned to " +  clinicalStaffMember.getPrefix()
                + clinicalStaffMember.getFullName() + ".", "Assignment Error",
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      // GUI feedback for invalid staff member or already assigned member
      JOptionPane.showMessageDialog(null, "Error:"
              +
          " Cannot assign deactivated staff.", "Assignment Error",
          JOptionPane.ERROR_MESSAGE);
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

    if (roomToAssign == null) {
      JOptionPane.showMessageDialog(null,
          "Error: Room with name '" + roomName + "' not found.");
      return;
    }

    if (isRoomOccupied(roomName) && !roomToAssign.isWaitingRoom()) {
      JOptionPane.showMessageDialog(null,
          "Error: The selected room is already occupied by another patient.");
      return;
    }

    if (isPatientInExamOrProcedureRoom(patient) && roomToAssign.isWaitingRoom()) {
      JOptionPane.showMessageDialog(null,
          "Error: Patient is already in an Exam or "
              +
              "Procedure Room and cannot be assigned to a Waiting Room.");
      return;
    }

    // Remove the patient from their current room's assigned patient list
    Room currentRoom = getPatientCurrentRoom(patient);
    if (currentRoom != null && currentRoom.getName().equalsIgnoreCase(roomName)) {
      JOptionPane.showMessageDialog(null,
          "Error: Patient is already assigned to the specified room.");
      return;
    }
    if (currentRoom != null) {
      currentRoom.getAssignedPatients().remove(patient);
    }

    // Assign the patient to the new room
    roomToAssign.getAssignedPatients().add(patient);

    // Update the patient's room details
    patient.setRoomNumber(roomToAssign.getRoomNumber());
    patient.setRoomName(roomName);
    patient.setRoomType(roomToAssign.getType());


    JOptionPane.showMessageDialog(null, "Patient "
        + patient.getFullName() + " assigned to room: " + roomName);
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
   * @return The patient with the specified first name and last name,
   *        or null if not found.
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
          return room;
          // Return the room if the patient is found in its assigned patients list
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
          staffListBuilder.append("Serial Number: ")
              .append(clinicalStaff.getSerialNumber()).append("<br>");
          staffListBuilder.append("Name: ").append(prefix).append(" ")
              .append(clinicalStaff.getFullName()).append("<br>");
          staffListBuilder.append("Job Title: ").append(clinicalStaff
              .getJobTitle()).append("<br>");
          staffListBuilder.append("Education Level: ").append(clinicalStaff
              .getEducationLevel()).append("<br>");
          staffListBuilder.append("Unique Identifier (NPI): ").append(clinicalStaff
              .getNpi()).append("<br>");
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
        if (room.getType() == Room.RoomType.PROCEDURE || room.getType() == Room.RoomType
            .EXAM) {
          // The room is considered occupied if there are any patients assigned to it
          return !room.getAssignedPatients().isEmpty();
        }
        // For any room types other than PROCEDURE or EXAM,
        // consider them not occupied for this check
        return false;
      }
    }
    // If no room with the given name is found, or it's
    // not a PROCEDURE or EXAM room, return false
    return false;
  }

  /**
   * Checks if a patient is assigned to an "exam" or "procedure" room.
   *
   * @param patient The patient to check for room assignment.
   * @return true if the patient is in an exam or procedure room, false otherwise.
   */
  @Override
  public boolean isPatientInExamOrProcedureRoom(Patient patient)
      throws IllegalArgumentException {
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
   * Retrieves a list of all active patients.
   *
   * @return List of active Patient objects.
   * @throws IllegalArgumentException if the patients list is null.
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
   * Displays a GUI dialog to register a new patient and adds them to the clinic.
   *
   * @param guiController The GUI controller object.
   */
  @Override
  public void registerNewPatientGui(GuiController guiController) {
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField dobField = new JTextField();
    Object[] message = {
        "First Name:", firstNameField,
        "Last Name:", lastNameField,
        "Date of Birth (M/d/yyyy):", dobField,
    };

    int option = JOptionPane.showConfirmDialog(null,
        message, "Register New Patient", JOptionPane.OK_CANCEL_OPTION);
    if (option == JOptionPane.OK_OPTION) {
      try {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dobStr = dobField.getText().trim();
        LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("M/d/yyyy"));

        // Placeholder for room number - ensure your Patient
        // constructor handles this appropriately
        Patient newPatient = new Patient(0, firstName, lastName, dobStr);
        registerNewPatient(newPatient);
        guiController.updateClinicMap();

      } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(guiController.frame,
            "Invalid date format. Please use M/d/yyyy.",
            "Error", JOptionPane.ERROR_MESSAGE);
      } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(guiController.frame,
            "Error registering patient: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Displays a GUI dialog to select a patient and
   * a room from a clinic map to assign the patient to.
   *
   * @param guiController The GUI controller object.
   * @throws IllegalArgumentException if there are no registered patients.
   */
  @Override
  public void assignPatientToRoomGui(GuiController guiController) {
    try {
      List<Patient> patients = getAllPatients();
      if (patients.isEmpty()) {
        JOptionPane.showMessageDialog(null,
            "No patients are currently registered.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      JComboBox<Patient> patientComboBox = new JComboBox<>(patients.toArray(new Patient[0]));
      Object[] message = {"Select Patient:", patientComboBox};
      int option = JOptionPane.showConfirmDialog(null,
          message, "Assign Patient to Room", JOptionPane.OK_CANCEL_OPTION);

      if (option == JOptionPane.OK_OPTION) {
        Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
        if (selectedPatient != null) {
          JOptionPane.showMessageDialog(guiController.frame,
              "Please click on the clinic map to select a room.",
              "Select Room", JOptionPane.INFORMATION_MESSAGE);

          guiController.clinicMapLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              guiController.clinicMapLabel.removeMouseListener(this);
              int x = e.getX();
              int y = e.getY();
              Room clickedRoom = ClinicMap.getRoomFromCoordinates(Clinic.this, x, y);

              if (clickedRoom != null) {
                try {
                  assignPatientToRoom(selectedPatient, clickedRoom.getName());
                  guiController.updateClinicMap(); // Call to update the clinic map
                } catch (IllegalArgumentException ex) {
                  JOptionPane.showMessageDialog(guiController.frame, ex.getMessage(),
                      "Error", JOptionPane.ERROR_MESSAGE);
                }
              } else {
                JOptionPane.showMessageDialog(guiController.frame,
                    "No room found at the clicked location.", "Error",
                    JOptionPane.ERROR_MESSAGE);
              }
            }
          });
        } else {
          JOptionPane.showMessageDialog(guiController.frame,
              "Please select a patient.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Displays a GUI dialog to select a patient and add a visit record for them.
   *
   * @param guiController The GUI controller object.
   */
  @Override
  public void addVisitRecordGui(GuiController guiController) {
    List<Patient> patients = getAllPatients();
    if (patients.isEmpty()) {
      JOptionPane.showMessageDialog(guiController.frame,
           "There are no registered patients to add a visit record for.",
          "No Patients", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    JComboBox<Patient> patientComboBox = new JComboBox<>();
    for (Patient patient : patients) {
      if (!patient.isDeactivated()) { // Assuming isDeactivated is a method in Patient
        patientComboBox.addItem(patient);
      }
    }

    int result = JOptionPane.showConfirmDialog(guiController.frame,
         patientComboBox, "Select a patient to add a visit record for:",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
      if (selectedPatient != null) {
        // Displaying the selected patient's full information
        selectedPatient.displayPatientFullInformation(guiController);

        // Proceed to add a visit record for the selected patient
        selectedPatient.addVisitRecordForPatient(guiController);
      }
    }
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
   * Displays a GUI dialog to register a new clinical
   * staff member and adds them to the system.
   *
   * @param guiController The GUI controller object.
   */
  @Override
  public void registerNewClinicalStaff(GuiController guiController) {
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JComboBox<String> jobTitleField = new JComboBox<>(new String[] {"Physician",
        "Nurse", "Technician"});
    JComboBox<String> educationalLevelField = new JComboBox<>(new String[] {"Doctoral",
        "Masters", "Allied"});
    JTextField npiField = new JTextField();

    Object[] message = {
        "First Name:", firstNameField,
        "Last Name:", lastNameField,
        "Job Title:", jobTitleField,
        "Educational Level:", educationalLevelField,
        "NPI (10 digits):", npiField,
    };

    int option = JOptionPane.showConfirmDialog(guiController.frame,
        message, "Enter Clinical Staff Information", JOptionPane.OK_CANCEL_OPTION);
    if (option != JOptionPane.OK_OPTION) {
      JOptionPane.showMessageDialog(guiController.frame,
          "Registration process cancelled.", "Cancelled",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    String firstName = firstNameField.getText().trim();
    String lastName = lastNameField.getText().trim();
    String jobTitle = (String) jobTitleField.getSelectedItem();
    String educationalLevel = (String) educationalLevelField.getSelectedItem();
    String npi = npiField.getText().trim();

    // Validate NPI
    if (!npi.matches("\\d{10}")) {
      JOptionPane.showMessageDialog(guiController.frame,
          "Invalid NPI. The NPI must contain 10 digits.",
          "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Assuming ClinicalStaff constructor matches this signature
    ClinicalStaff newStaff = new ClinicalStaff(jobTitle, firstName,
        lastName, Staff.EducationLevel.valueOf(educationalLevel.toUpperCase()), npi);
    registerNewClinicalStaff(newStaff);
    JOptionPane.showMessageDialog(guiController.frame,
        "Clinical staff registered successfully.",
        "Success", JOptionPane.INFORMATION_MESSAGE);
    showClinicalStaffList(guiController.frame);
  }

  /**
   * Displays a GUI dialog to assign a clinical staff member to a patient.
   * Shows a list of available patients and clinical staff members for selection.
   */
  @Override
  public void assignStaffToPatientGui() {
    // Select a patient
    JComboBox<Patient> patientComboBox = new JComboBox<>();
    List<Patient> allPatients = getAllPatients();
    if (allPatients.isEmpty()) {
      JOptionPane.showMessageDialog(null,
          "There are no patients available for assignment.",
          "No Patients", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    allPatients.forEach(patientComboBox::addItem);

    int patientChoice = JOptionPane.showConfirmDialog(null,
        patientComboBox, "Select a Patient", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (patientChoice != JOptionPane.OK_OPTION) {
      return;
    }

    Patient selectedPatient = patientComboBox.getItemAt(patientComboBox
        .getSelectedIndex());

    // Show patient information including assigned clinical staff
    JOptionPane.showMessageDialog(null,
        selectedPatient.getFullInformation(), "Patient Information",
        JOptionPane.INFORMATION_MESSAGE);

    // Select clinical staff, filtering out the deactivated and already assigned staff
    JComboBox<String> staffComboBox = new JComboBox<>();
    List<ClinicalStaff> availableStaff = getClinicalStaffList().stream()
        .filter(staff -> !staff.isDeactivated() && !selectedPatient
            .getAssignedClinicalStaff().contains(staff))
        .collect(Collectors.toList());

    if (availableStaff.isEmpty()) {
      JOptionPane.showMessageDialog(null,
          "There are no available clinical staff for assignment.",
          "No Available Clinical Staff", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    Map<String, ClinicalStaff> staffDisplayMap = new HashMap<>();
    for (ClinicalStaff staff : availableStaff) {
      String displayString = staff.getPrefix() + " " + staff.getFullName();
      staffComboBox.addItem(displayString);
      staffDisplayMap.put(displayString, staff);
    }

    int staffChoice = JOptionPane.showConfirmDialog(null,
        staffComboBox, "Select Clinical Staff to Assign",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (staffChoice != JOptionPane.OK_OPTION) {
      return;
    }

    String selectedDisplayString = (String) staffComboBox.getSelectedItem();
    ClinicalStaff selectedStaff = staffDisplayMap.get(selectedDisplayString);

    // Confirmation dialog with customized message
    int confirmation = JOptionPane.showConfirmDialog(
        null,
        "Are you sure you want to assign "
            + selectedDisplayString + " to " + selectedPatient.getFullName() + "?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    if (confirmation != JOptionPane.YES_OPTION) {
      return;
    }

    // Assign the selected staff to the selected patient
    assignClinicalStaffToPatient(selectedPatient, selectedStaff);

    // Show updated patient information
    JOptionPane.showMessageDialog(null,
        selectedPatient.getFullInformation(),
        "Updated Patient Information", JOptionPane.INFORMATION_MESSAGE);
  }


  /**
   * Displays a GUI dialog to select a patient and send them home.
   * Also, prompts the user to select a physician
   * to approve the discharge.
   *
   * @param guiController The GUI controller object.
   */
  @Override
  public void sendPatientHomeGui(GuiController guiController) {
    // Select a patient
    JComboBox<Patient> patientComboBox = new JComboBox<>();
    List<Patient> allPatients = getAllPatients();
    if (allPatients.isEmpty()) {
      JOptionPane.showMessageDialog(null,
          "There are no patients available.", "No Patients",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    allPatients.forEach(patientComboBox::addItem);

    int patientChoice = JOptionPane.showConfirmDialog(null, patientComboBox,
        "Select a Patient to Send Home", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (patientChoice != JOptionPane.OK_OPTION) {
      return;
    }

    Patient selectedPatient = patientComboBox.getItemAt(patientComboBox
        .getSelectedIndex());

    // Select approving clinical staff (only Physicians)
    JComboBox<String> staffComboBox = new JComboBox<>();
    List<ClinicalStaff> allStaff = getClinicalStaffList();
    Map<String, ClinicalStaff> staffMap = new HashMap<>();

    for (ClinicalStaff staff : allStaff) {
      if ("Physician".equalsIgnoreCase(staff.getJobTitle()) && !staff.isDeactivated()) {
        String staffDisplay = staff.getPrefix() + " " + staff.getFullName();
        staffComboBox.addItem(staffDisplay);
        staffMap.put(staffDisplay, staff);
      }
    }

    if (staffComboBox.getItemCount() == 0) {
      JOptionPane.showMessageDialog(null,
          "There are no physicians available.", "No Physicians",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    int staffChoice = JOptionPane.showConfirmDialog(null,
        staffComboBox, "Select Approving Clinical Staff",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (staffChoice != JOptionPane.OK_OPTION) {
      return;
    }

    String selectedStaffDisplay = (String) staffComboBox.getSelectedItem();
    ClinicalStaff selectedStaff = staffMap.get(selectedStaffDisplay);

    // Execute the send home action
    try {
      sendPatientHome(selectedPatient, selectedStaff);
      guiController.updateMapImage();
      JOptionPane.showMessageDialog(null, "Patient "
          + selectedPatient.getFullName() + " has been sent home by "
          + selectedStaffDisplay + ".", "Patient Sent Home",
          JOptionPane.INFORMATION_MESSAGE);
    } catch (IllegalArgumentException | IllegalStateException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Displays a GUI dialog to select a clinical staff member to deactivate.
   * Deactivates the selected staff member and unassigns them from all patients.
   */
  @Override
  public void deactivateStaffGui() {
    // Select a clinical staff member to deactivate
    JComboBox<String> staffComboBox = new JComboBox<>();
    Map<String, ClinicalStaff> staffMap = new HashMap<>();
    List<ClinicalStaff> allStaff = getClinicalStaffList();

    for (ClinicalStaff staffMember : allStaff) {
      if (!staffMember.isDeactivated()) {
        String staffDisplay = staffMember.getPrefix() + " "
            + staffMember.getFullName() + " - " + staffMember.getJobTitle();
        staffComboBox.addItem(staffDisplay);
        staffMap.put(staffDisplay, staffMember);
      }
    }

    if (staffComboBox.getItemCount() == 0) {
      JOptionPane.showMessageDialog(null,
          "There are no active clinical staff members available.",
          "No Active Clinical Staff", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    int staffChoice = JOptionPane.showConfirmDialog(null,
        staffComboBox, "Select a Clinical Staff Member to Deactivate",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (staffChoice != JOptionPane.OK_OPTION) {
      JOptionPane.showMessageDialog(null,
          "Operation cancelled.", "Cancelled",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    String selectedStaffDisplay = (String) staffComboBox.getSelectedItem();
    ClinicalStaff selectedStaff = staffMap.get(selectedStaffDisplay);

    // Iterate through all patients and unassign the staff member
    // using the unassignClinicalStaff method
    for (Patient patient : getAllPatients()) {
      if (patient.getAssignedClinicalStaff().contains(selectedStaff)) {
        patient.unassignClinicalStaff(selectedStaff);
      }
    }

    // Deactivate the staff member
    selectedStaff.setDeactivated(true);

    JOptionPane.showMessageDialog(null,
        "Clinical staff member " + selectedStaffDisplay
            + " deactivated successfully.", "Deactivated",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Displays a GUI dialog to select a patient and view their details.
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
   * Displays a GUI dialog to select a patient and unassign
   * a clinical staff member from them.
   */
  @Override
  public void unassignStaffFromPatientGui() {
    // Select a patient
    JComboBox<Patient> patientComboBox = new JComboBox<>();
    List<Patient> allPatients = getAllPatients();  // Retrieve all patients
    if (allPatients.isEmpty()) {
      JOptionPane.showMessageDialog(null,
          "There are no patients available.", "No Patients",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    allPatients.forEach(patientComboBox::addItem);

    int patientChoice = JOptionPane.showConfirmDialog(null,
        patientComboBox, "Select a Patient", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (patientChoice != JOptionPane.OK_OPTION) {
      return;
    }

    Patient selectedPatient = patientComboBox.getItemAt(patientComboBox
        .getSelectedIndex());

    // Show patient information including assigned clinical staff
    JOptionPane.showMessageDialog(null,
        selectedPatient.getFullInformation(), "Patient Information",
        JOptionPane.INFORMATION_MESSAGE);

    // Select clinical staff to unassign
    JComboBox<String> staffComboBox = new JComboBox<>();
    Map<String, ClinicalStaff> staffMap = new HashMap<>();
    for (ClinicalStaff staff : selectedPatient.getAssignedClinicalStaff()) {
      String staffDisplay = staff.getPrefix() + staff.getFullName();
      staffComboBox.addItem(staffDisplay);
      staffMap.put(staffDisplay, staff);
    }

    if (staffComboBox.getItemCount() == 0) {
      JOptionPane.showMessageDialog(null,
          "This patient has no assigned clinical staff to unassign.",
          "No Assignments", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    int staffChoice = JOptionPane.showConfirmDialog(null,
        staffComboBox, "Select Clinical Staff to Unassign",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (staffChoice != JOptionPane.OK_OPTION) {
      return;
    }

    String selectedStaffDisplay = (String) staffComboBox.getSelectedItem();
    ClinicalStaff selectedStaff = staffMap.get(selectedStaffDisplay);

    // Confirmation dialog
    int confirmation = JOptionPane.showConfirmDialog(
        null,
        "Are you sure you want to unassign " + selectedStaffDisplay
            + " from " + selectedPatient.getFullName() + "?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    if (confirmation != JOptionPane.YES_OPTION) {
      return;
    }

    // Unassign the selected staff from the selected patient
    selectedPatient.unassignClinicalStaff(selectedStaff);
    selectedStaff.getAssignedPatients().remove(selectedPatient);

    // Show updated patient information
    JOptionPane.showMessageDialog(null,
        selectedPatient.getFullInformation(),
        "Updated Patient Information", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Displays a GUI dialog showing a table with clinical staff
   * members and their patient assignment counts.
   */
  @Override
  public void listClinicalStaffAndPatientCountsGui() {
    // Create column names for the JTable
    String[] columnNames = {"Name", "Serial Number",
        "Unique Patients Assigned", "Status"};

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
   * Displays a GUI dialog showing a list of patients with their
   * last visit more than a year ago.
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
              .append(", Last Visit Date: ")
              .append(lastVisit.getRegistrationDateTime().toLocalDate())
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
    JOptionPane.showMessageDialog(null,
        scrollPane, "Patients with Last Visit Over a Year Ago",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Displays a GUI dialog showing clinical staff members with
   * active patients and incomplete visits for each.
   *
   * @param clinicalStaffList The list of clinical staff members to display.
   * @param guiController     The GUI controller object.
   */
  @Override
  public void listClinicalStaffWithIncompleteVisitsGui(List<ClinicalStaff>
                                                             clinicalStaffList,
                                                       GuiController guiController) {
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
   * Displays a GUI dialog showing patients with two or more visits in the past 365 days.
   *
   * @param guiController The GUI controller object.
   */
  @Override
  public void listPatientsWithMultipleVisitsInLastYear(GuiController guiController) {
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
        "Patients with Multiple Visits in Last Year",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Clears all data from the clinic model, including patient records, clinical staff records,
   * room assignments, and visit records.
   */
  public void clearModel() {
    clearPatientRecords();
    clearStaffRecords();
    clearRoom();
    clearVisitRecords();
  }

  /**
   * Clears the patient records in the clinic model.
   */
  private void clearPatientRecords() {
    patients.clear(); // Assuming patients is a list of Patient objects
  }

  /**
   * Clears the staff records in the clinic model.
   */
  private void clearStaffRecords() {
    staff.clear();
  }

  /**
   * Clears the room assignments and deletes the rooms in the clinic model.
   */
  private void clearRoom() {
    for (Room room : rooms) {
      room.getAssignedPatients().clear(); // Clearing the list of assigned patients for each room
    }
    rooms.clear(); // Clearing the list of rooms
  }

  /**
   * Clears the visit records for all patients in the clinic model.
   */
  private void clearVisitRecords() {
    for (Patient patient : patients) {
      patient.clearVisitRecords();
    }
  }

}










