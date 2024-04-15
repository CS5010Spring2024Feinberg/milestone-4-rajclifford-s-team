package clinicmanagement;

import java.util.List;
import javax.swing.JFrame;

/**
 * An interface that defines the operations for managing a clinic.
 */
public interface ClinicInterface {

  /**
   * Adds a staff member to the clinic.
   *
   * @param staffMember The staff member to add.
   */
  void addStaff(Staff staffMember);

  /**
   * Registers a new patient in the clinic.
   *
   * @param patient The patient to register.
   * @return The registered patient.
   */
  Patient registerNewPatient(Patient patient);


  // Getters and Setters for clinic properties
  /**
   * Gets the name of the clinic.
   *
   * @return The name of the clinic.
   */
  String getName();

  /**
   * Sets the name of the clinic.
   *
   * @param name The name to set for the clinic.
   */
  void setName(String name);

  /**
   * Adds a room to the clinic.
   *
   * @param room The room to add.
   */
  void addRoom(Room room);

  /**
   * Adds a patient to the clinic.
   *
   * @param patient The patient to add.
   */
  void addPatient(Patient patient);

  /**
   * Assign a patient to clinical staff.
   * @param clinicalStaffMember Clinical Staff to assign.
   * @param patientToStaff Patient to be assigned.
   */
  void assignClinicalStaffToPatient(Patient patientToStaff, ClinicalStaff clinicalStaffMember);

  /**
   * Assigns a patient to a specific room.
   *
   * @param patient   The patient to assign.
   * @param roomName  The name of the room to assign the patient to.
   */
  void assignPatientToRoom(Patient patient, String roomName);

  /**
   * Retrieves the list of patients in a particular room.
   *
   * @param room The room for which to retrieve patients.
   * @return The list of patients in the specified room.
   */

  List<Patient> getPatientsInRoom(Room room);

  /**
   * Finds a patient by their first name and last name.
   *
   * @param firstName The first name of the patient.
   * @param lastName  The last name of the patient.
   * @return The found patient or null if not found.
   */
  Patient findPatientByName(String firstName, String lastName);

  /**
   * Finds a clinical staff member by their first name and last name.
   *
   * @param firstName The first name of the clinical staff member.
   * @param lastName  The last name of the clinical staff member.
   * @return The found clinical staff member or null if not found.
   */
  ClinicalStaff findClinicalStaffByName(String firstName, String lastName);

  /**
   * Checks if a patient is in an exam or procedure room.
   *
   * @param patient The patient to check.
   * @return True if the patient is in an exam or procedure room, false otherwise.
   */
  boolean isPatientInExamOrProcedureRoom(Patient patient);

  /**
   * Checks if a patient is a duplicate entry.
   *
   * @param newPatient The new patient to check.
   * @return True if the patient is a duplicate, false otherwise.
   */
  boolean isDuplicatePatient(Patient newPatient);

  /**
   * Gets the assigned room for a patient.
   *
   * @param patient The patient whose assigned room is to be retrieved.
   * @return The assigned room for the patient.
   */
  Room getAssignedRoomForPatient(Patient patient);

  /**
   * Gets the current room of a patient.
   *
   * @param patient The patient whose current room is to be retrieved.
   * @return The current room of the patient.
   */
  Room getPatientCurrentRoom(Patient patient);


  /**
   * Lists all Clinical Staff in the clinic instance.
   * @return All Clinical Staff
   *
   */
  List<ClinicalStaff> getClinicalStaffList();

  /**
   * Shows clinical Staff list in gui table.
   * @param frame J panel
   */
  void showClinicalStaffList(JFrame frame);

  /**
   * Checks if a room is occupied.
   *
   * @param roomName The name of the room to check.
   * @return True if the room is occupied, false otherwise.
   */
  boolean isRoomOccupied(String roomName);

  /**
   * Gets a room by its room number.
   *
   * @param roomNumber The room number to search for.
   * @return The room with the specified room number, or null if not found.
   */
  Room getRoomByNumber(int roomNumber);

  /**
   * Shows the list of rooms in the clinic.
   * @return list of rooms.
   *
   */
  List<Room> getRooms();

  /**
   * Registers a new clinical staff member.
   *
   * @param staffMember The clinical staff member to register.
   */
  void registerNewClinicalStaff(ClinicalStaff staffMember);

  /**
   * Register new Clinical Staff in the clinic.
   * @param guiController controller input
   */
  void registerNewClinicalStaff(GuiController guiController);

  /**
   * Sends a patient home, releasing them from the clinic.
   *
   * @param patient        The patient to send home.
   * @param clinicalStaff  The clinical staff member overseeing the patient.
   */
  void sendPatientHome(Patient patient, ClinicalStaff clinicalStaff);


  /**
   * Finds a patient in the existing clinic directory.
   * @param newPatient as input to match with exisitng patient.
   * @return the existing patient
   */
  Patient findExistingPatient(Patient newPatient);

  /**
   * List all patient in the clinic.
   * @return all patient list
   */
  List<Patient> getAllPatients() throws IllegalArgumentException;

  /**
   * Register new patient in the clinic.
   * @param guiController controller input
   */
  void registerNewPatientGui(GuiController guiController);

  /**
   * Assign a patient to a specific room in the clinc.
   * @param guiController controller input
   */
  void assignPatientToRoomGui(GuiController guiController);

  /**
   * Add new visit record for patient in the clinic.
   * @param guiController controller input
   */
  void addVisitRecordGui(GuiController guiController);

  /**
   * Register clinincal staff to patient in the clinic.
   *
   */
  void assignStaffToPatientGui();

  /**
   * Send patient to home from clinic.
   * @param guiController controller input
   */
  void sendPatientHomeGui(GuiController guiController);

  /**
   * Deactivate a specific Clinical Staff in the clinic.
   *
   */
  void deactivateStaffGui();

  /**
   * Shows comprehensive info on patient in the clinic.
   *
   */
  void showPatientDetailsGui();

  /**
   * Unassign a clinical staff from a patient in the clinic.
   *
   */
  void unassignStaffFromPatientGui();

  /**
   * List how many patients have been assigned to the clinical staff.
   *
   */
  void listClinicalStaffAndPatientCountsGui();

  /**
   * List patients that have not visited clinic for an year (not deactive).
   *
   */
  void listInactivePatientsForYearGui();

  /**
   * List staff with ongoing patient visits.
   * @param guiController controller input
   * @param clinicalStaffList clinical staff list
   */
  void listClinicalStaffWithIncompleteVisitsGui(List<ClinicalStaff> clinicalStaffList,
                                                GuiController guiController);

  /**
   * Shows patients who have visited the clinic multiple times in last year.
   * @param guiController controller input
   */
  void listPatientsWithMultipleVisitsInLastYear(GuiController guiController);
}
