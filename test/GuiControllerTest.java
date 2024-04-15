import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import clinicmanagement.ClinicalStaff;
import clinicmanagement.MockModel;
import clinicmanagement.MockView;
import clinicmanagement.Patient;
import clinicmanagement.Room;
import clinicmanagement.Visitrecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;


/**
 * JUIT4 Tests for all GuiController class methods.
 */
public class GuiControllerTest {
  private MockModel clinic;
  private MockView view;

  @Before
  public void setUp() {
    clinic = new MockModel(); // Initialize your Clinic implementation
    view = new MockView(clinic);
  }

  /**
   * Tests that registering a new patient correctly assigns them to a waiting room.
   */
  @Test
  public void testRegisterPatientAssignsWaitingRoomGui() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 waiting Front Waiting Room", 1));
    view.simulateRegisterNewPatientPress("Future", "Follicle", "6/6/1986");
    Room assignedRoom = clinic.getAssignedRoomForPatient("Future", "Follicle", "6/6/1986");
    assertNotNull("Patient should be assigned to a room", assignedRoom);
    assertEquals(Room.RoomType.WAITING, assignedRoom.getType());
  }

  /**
   * Tests the assignment of a patient to a specific examination room.
   */
  @Test
  public void testAssignPatientToExamRoomGui() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 waiting Front Waiting Room", 1));
    clinic.addRoom(new Room("28  0 35  5 exam Triage", 2));
    view.simulateRegisterNewPatientPress("Future", "Doe", "6/6/1986");
    Patient registeredPatient = clinic.findPatientByName("Future", "Doe");
    view.simulateAssignPatientToExamPress();
    Room assignedRoom = clinic.getAssignedRoomForPatient("Future", "Doe", "6/6/1986");
    assertNotNull(registeredPatient);
    assertEquals("Triage", assignedRoom.getName());
    assertEquals(Room.RoomType.EXAM, assignedRoom.getType());
  }

  /**
   * Confirms that a patient can be reassigned to a different room without affecting their
   * clinical staff assignments, if any.
   */
  @Test
  public void testAssignPatientToAnotherRoomWithoutStaffAssignmentGui() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 waiting Front Waiting Room", 1));
    clinic.addRoom(new Room("28  0 35  5 exam Triage", 2));
    view.simulateRegisterClinicianPress("Amy", "Anguish", "physician", "doctoral", "1234567890");
    // Assuming the patient "Shoulder Doe" has been registered separately
    view.simulateRegisterNewPatientPress("Shoulder", "Doe", "6/6/1986");
    // Assign clinician Amy Anguish to patient Shoulder Doe
    view.simulateAssignStaffToPatientPress(0, 0);
    view.simulateAssignPatientToExamPress();
    Patient patient = clinic.findPatientByName("Shoulder", "Doe");
    List<ClinicalStaff> assignedStaff = patient.getAssignedClinicalStaff();

    assertNotNull(assignedStaff);
    assertEquals(1, assignedStaff.size());
    assertEquals("Amy Anguish", String.format("%s %s",
        assignedStaff.get(0).getFirstName(), assignedStaff.get(0).getLastName()));
    // Assuming the method getAssignedClinicalStaff does not exist and needs correction
    // This part of the code will likely need adjustment based on your implementation
  }

  /**
   * Tests the scenario where two patients are incorrectly assigned to the same room, which
   * should not be allowed by the system.
   */
  @Test(expected = Exception.class)
  public void testAssignTwoPatientsToSameExamRoomGui() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 exam Surgical", 2));
    view.simulateRegisterNewPatientPress("Future", "Doe", "6/6/1986");
    view.simulateRegisterNewPatientPress("Shoulder", "Doe", "6/6/1986");
    Patient patient1 = clinic.findPatientByName("Shoulder", "Doe");
    Patient patient2 = clinic.findPatientByName("Shoulder", "Doe");
    clinic.assignPatientToRoom(patient1, "Surgical");
    clinic.assignPatientToRoom(patient2, "Surgical");
    // The code should throw an exception to pass this test
  }



  /**
   * Tests the properties and behavior of a visit record.
   */
  @Test
  public void testAddVisitRecordGui() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 waiting Front Waiting Room", 1));
    view.simulateRegisterNewPatientPress("Future", "Doe", "6/6/1986");
    // Create LocalDateTime object for registration date and time
    LocalDateTime registrationDateTime = LocalDateTime.of(2024,
        2, 25, 10, 30);

    // Generate a random body temperature within the range of 20 to 40 degrees Celsius
    Random random = new Random();
    double randomTemperature = 20 + (random.nextDouble() * (40 - 20));

    view.simulateAddVisitRecordPress(0, registrationDateTime, "Fever", randomTemperature);

    // Test the properties and behavior of the visitRecord object
    Patient patient = clinic.findPatientByName("Future", "Doe");
    List<Visitrecord> visitRecords = patient.getVisitRecords();

    assertNotNull(visitRecords);
    assertEquals(1, visitRecords.size());
    assertEquals(registrationDateTime, visitRecords.get(0).getRegistrationDateTime());
    assertEquals("Fever", visitRecords.get(0).getChiefComplaint());
    assertEquals(randomTemperature, visitRecords.get(0).getBodyTemperature(), 1E-15);

  }

  /**
   * Tests the functionality of sending a patient home with physician approval.
   */
  @Test
  public void testSendPatientHomeWithPhysicianApproval() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 waiting Front Waiting Room", 1));
    clinic.addRoom(new Room("28  0 35  5 exam Triage", 2));
    view.simulateRegisterClinicianPress("Mark", "Ming", "physician", "doctoral", "1234567890");
    // Get a registered patient from the clinic
    view.simulateRegisterNewPatientPress("John", "Smith", "6/6/1986");
    view.simulateAssignPatientToExamPress();

    // Get the room assigned to the patient before sending them home
    Room assignedRoomBefore = clinic.getAssignedRoomForPatient("John", "Smith", "6/6/1986");

    view.simulateSendPatientHomePress(0);

    // Get the room assigned to the patient after sending them home
    Room assignedRoomAfter = clinic.getAssignedRoomForPatient("John", "Smith", "6/6/1986");

    // Verify that the patient is sent home by checking if they are no longer assigned to any room
    assertNull("Patient should not be assigned to any "
        +
        "room after being sent home", assignedRoomAfter);
    // Additionally, you might want to assert that the assigned
    // room before sending home was not null
    assertNotNull("Patient should have been assigned to "
        +
        "a room before being sent home", assignedRoomBefore);
  }

  /**
   * Confirms that a staff member can be deactivated through GUI.
   */
  @Test
  public void testDeactivateStaffGui() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 waiting Front Waiting Room", 1));
    clinic.addRoom(new Room("28  0 35  5 exam Triage", 2));
    view.simulateRegisterClinicianPress("Amy", "Anguish", "physician", "doctoral", "1234567890");
    // Assuming the patient "Shoulder Doe" has been registered separately
    view.simulateRegisterNewPatientPress("Shoulder", "Doe", "6/6/1986");
    // Assign clinician Amy Anguish to patient Shoulder Doe
    view.simulateAssignStaffToPatientPress(0, 0);
    view.simulateDeactivateStaffPress(0);
    Patient patient = clinic.findPatientByName("Shoulder", "Doe");
    List<ClinicalStaff> assignedStaff = patient.getAssignedClinicalStaff();

    assertEquals(0, assignedStaff.size());
  }

  /**
   * Confirms that a staff member can be unassigned from a patient
   * through the GUI.
   */
  @Test
  public void testUnassignStaffGui() {
    clinic.clear();
    clinic.addRoom(new Room("28  0 35  5 waiting Front Waiting Room", 1));
    clinic.addRoom(new Room("28  0 35  5 exam Triage", 2));
    view.simulateRegisterClinicianPress("Amy", "Anguish", "physician", "doctoral", "1234567890");
    // Assuming the patient "Shoulder Doe" has been registered separately
    view.simulateRegisterNewPatientPress("Shoulder", "Doe", "6/6/1986");
    // Assign clinician Amy Anguish to patient Shoulder Doe
    view.simulateAssignStaffToPatientPress(0, 0);

    Patient patient = clinic.findPatientByName("Shoulder", "Doe");
    List<ClinicalStaff> assignedStaff = patient.getAssignedClinicalStaff();

    assertNotNull(assignedStaff);
    assertEquals(1, assignedStaff.size());
    assertEquals("Amy Anguish", String.format("%s %s",
        assignedStaff.get(0).getFirstName(), assignedStaff.get(0).getLastName()));

    view.simulateUnassignStaffPress(0, 0);

    Patient patient2 = clinic.findPatientByName("Shoulder", "Doe");
    List<ClinicalStaff> assignedStaff2 = patient.getAssignedClinicalStaff();

    assertEquals(0, assignedStaff2.size());

  }

}

