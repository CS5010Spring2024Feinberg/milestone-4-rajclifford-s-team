package clinicmanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class is responsible for parsing clinic information.
 */
public class ClinicFileParser implements ClinicFileParserInterface {
  private Reader reader;

  /**
   * Constructs a new ClinicFileParser with the specified Reader.
   *
   * @param reader the Reader to be used for parsing clinic data
   * @throws IllegalArgumentException if the reader is null
   */
  public ClinicFileParser(Reader reader) throws IllegalArgumentException {
    if (reader == null) {
      throw new IllegalArgumentException("Reader cannot be null.");
    }
    this.reader = reader;
  }

  /**
   * Parse the clinic information from a file and create a Clinic object.
   *
   * @return A Clinic object containing the parsed information.
   * @throws IOException If there is an issue reading the file.
   */
  @Override
  public Clinic parseFile() throws IOException {
    Clinic clinic = new Clinic();
    try (BufferedReader reader = new BufferedReader(this.reader)) {
      parseClinicName(reader, clinic);
      parseRooms(reader, clinic);
      parseStaff(reader, clinic);
      parsePatients(reader, clinic);
    }
    return clinic;
  }

  /**
   * Parses the name of the clinic from a BufferedReader and sets it to the clinic object.
   *
   * @param reader The BufferedReader containing the clinic name.
   * @param clinic The clinic object to which the name will be set.
   * @throws IOException if an I/O error occurs while reading from the reader.
   * @throws IllegalArgumentException if the clinic name is empty.
   */
  private void parseClinicName(BufferedReader reader, Clinic clinic) throws IOException {
    String clinicName = reader.readLine().trim();
    if (clinicName.isEmpty()) {
      throw new IllegalArgumentException("Clinic name cannot be empty.");
    }
    System.out.println("WELCOME! Your" + "Clinic Name is : " + clinicName);
    clinic.setName(clinicName);
  }

  /**
   * Parses room information from a BufferedReader and adds rooms to the clinic.
   *
   * @param reader The BufferedReader containing the room information.
   * @param clinic The clinic object to which the rooms will be added.
   * @throws IOException if an I/O error occurs while reading from the reader.
   */
  private void parseRooms(BufferedReader reader, Clinic clinic) throws IOException {
    int numberOfRooms = Integer.parseInt(reader.readLine().trim());

    for (int i = 0; i < numberOfRooms; i++) {
      String line = reader.readLine().trim();
      validateRoomName(line);

      Room room = new Room(line, i + 1);
      clinic.addRoom(room);
    }
  }

  /**
   * Validates the format of a room name.
   *
   * @param roomName The name of the room to validate.
   * @throws IllegalArgumentException if the room name is empty or has an invalid format.
   */
  private void validateRoomName(String roomName) throws IllegalArgumentException {
    if (roomName.isEmpty()) {
      throw new IllegalArgumentException("Room name cannot be empty.");
    }
    char firstChar = roomName.charAt(0);
    if (!Character.isDigit(firstChar)) {
      throw new IllegalArgumentException("Invalid room name format: " + roomName);
    }
  }

  /**
   * Parses staff information from a BufferedReader and adds staff members to the clinic.
   *
   * @param reader The BufferedReader containing the staff information.
   * @param clinic The clinic object to which the staff members will be added.
   * @throws IOException if an I/O error occurs while reading from the reader.
   */
  private void parseStaff(BufferedReader reader, Clinic clinic) throws IOException {
    int numberOfStaff = Integer.parseInt(reader.readLine().trim());

    for (int i = 0; i < numberOfStaff; i++) {
      String line = reader.readLine().trim();
      if (!line.isEmpty()) {
        String[] parts = line.split("\\s+");
        validateStaffFormat(parts, line);
        addStaff(clinic, parts);
      }
    }
  }

  /**
   * Validates the format of staff information.
   *
   * @param parts An array containing details of the staff member.
   * @param line The original line containing staff information.
   * @throws IllegalArgumentException if the format of staff information is invalid.
   */
  private void validateStaffFormat(String[] parts, String line) throws IllegalArgumentException {
    if (parts.length < 5) {
      throw new IllegalArgumentException("Invalid staff information format: " + line);
    }
  }

  /**
   * Adds a staff member to the clinic.
   *
   * @param clinic The clinic object to which the staff member will be added.
   * @param parts An array containing details of the staff member, including
   *             job title, first name, last name, education level, and unique identifier.
   *              The elements in the array must be in the following order:
   *              job title, first name, last name, education level, and unique identifier.
   */
  private void addStaff(Clinic clinic, String[] parts) throws IllegalArgumentException {
    Staff staff;
    String jobTitle = parts[0];
    String firstName = parts[1];
    String lastName = parts[2];
    Staff.EducationLevel educationLevel = Staff.EducationLevel.valueOf(parts[3].toUpperCase());
    String uniqueIdentifier = parts[4];
    if (uniqueIdentifier.matches("\\d{10}")) {
      staff = new ClinicalStaff(jobTitle, firstName, lastName, educationLevel, uniqueIdentifier);
    } else {
      staff = new NonClinicalStaff(jobTitle, firstName, lastName, educationLevel, uniqueIdentifier);
    }
    clinic.addStaff(staff);
  }

  /**
   * Parses patient information from a BufferedReader and adds patients to the clinic.
   *
   * @param reader The BufferedReader containing the patient information.
   * @param clinic The clinic object to which the patients will be added.
   * @throws IOException if an I/O error occurs while reading from the reader.
   */
  private void parsePatients(BufferedReader reader, Clinic clinic) throws IOException {
    int numberOfPatients = Integer.parseInt(reader.readLine().trim());

    for (int i = 0; i < numberOfPatients; i++) {
      String line = reader.readLine().trim();
      if (!line.isEmpty()) {
        String[] patientDetails = line.split("\\s+");
        validatePatientFormat(patientDetails, line);
        addPatient(clinic, patientDetails);
      }
    }
  }

  /**
   * Validates the format of patient information.
   *
   * @param patientDetails An array containing details of the patient.
   * @param line The original line containing patient information.
   * @throws IllegalArgumentException if the format of patient information is invalid.
   */
  private void validatePatientFormat(String[] patientDetails, String line)
      throws IllegalArgumentException {
    if (patientDetails.length < 4) {
      throw new IllegalArgumentException("Invalid patient information format: " + line);
    }
  }

  /**
   * Adds a new patient to the clinic.
   *
   * @param clinic The clinic object to which the patient will be added.
   * @param patientDetails An array containing details of the patient,
   *                      including room number, first name, last name, and date of birth.
   *                       The elements in the array must be in the following order:
   *                       room number, first name, last name, date of birth.
   * @throws IllegalArgumentException if the patient room number is invalid.
   */
  private void addPatient(Clinic clinic, String[] patientDetails) throws IllegalArgumentException {
    int patientRoomNumber = Integer.parseInt(patientDetails[0]);
    String firstName = patientDetails[1];
    String lastName = patientDetails[2];
    String dateOfBirth = patientDetails[3];
    Room patientRoom = clinic.getRoomByNumber(patientRoomNumber);
    if (patientRoom != null) {
      Patient patient = new Patient(patientRoomNumber, firstName, lastName, dateOfBirth);
      patient.populateRoomInfo(patientRoom);
      patientRoom.getAssignedPatients().add(patient);
      clinic.addPatient(patient);
    } else {
      throw new IllegalArgumentException("Invalid patient room number: " + patientRoomNumber);
    }
  }
}
