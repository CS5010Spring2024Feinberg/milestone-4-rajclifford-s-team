import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import clinicmanagement.Clinic;
import clinicmanagement.ClinicInterface;
import clinicmanagement.ClinicalStaff;
import clinicmanagement.Patient;
import clinicmanagement.Room;
import clinicmanagement.Visitrecord;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;


/**
 * JUIT4 Tests for all Clinic class methods.
 */
public class ClinicTest {
  private ClinicInterface clinic;

  @Before
  public void setUp() {
    clinic = new Clinic(); // Initialize your Clinic implementation
  }

  /**
   * Tests that registering a new patient correctly assigns them to a waiting room.
   */
  @Test
  public void testRegisterPatientAssignsWaitingRoom() {
    Patient patient = new Patient(0, "Future", "Follicle", "6/6/1986");
    clinic.registerNewPatient(patient);
    Room assignedRoom = clinic.getAssignedRoomForPatient(patient);
    assertNotNull("Patient should be assigned to a room", assignedRoom);
    assertEquals(Room.RoomType.WAITING, assignedRoom.getType());
  }

  /**
   * Tests the assignment of a patient to a specific examination room.
   */
  @Test
  public void testAssignPatientToExamRoom() {
    Patient patient = new Patient(1,
        "Future", "Doe", "6/6/1986");
    Patient registeredPatient = clinic.findPatientByName("Future", "Doe");
    clinic.assignPatientToRoom(registeredPatient, "Triage");
    Room assignedRoom = clinic.getAssignedRoomForPatient(registeredPatient);
    assertNotNull(patient);
    assertEquals("Triage", assignedRoom.getName());
    assertEquals(Room.RoomType.EXAM, assignedRoom.getType());
  }

  /**
   * Confirms that a patient can be reassigned to a different room without affecting their
   * clinical staff assignments, if any.
   */
  @Test
  public void testAssignPatientToAnotherRoomWithoutStaffAssignment() {
    // Assuming the patient "Shoulder Doe" has been registered separately
    Patient patientData = clinic.findPatientByName("Shoulder", "Doe");
    clinic.assignPatientToRoom(patientData, "Triage");
    clinic.assignPatientToRoom(patientData, "ExamRoom");
    Room assignedRoom = clinic.getAssignedRoomForPatient(patientData);
    assertNotNull(assignedRoom);
    assertEquals("ExamRoom", assignedRoom.getName());
    // Assuming the method getAssignedClinicalStaff does not exist and needs correction
    // This part of the code will likely need adjustment based on your implementation
  }

  /**
   * Tests the scenario where two patients are incorrectly assigned to the same room, which
   * should not be allowed by the system.
   */
  @Test(expected = Exception.class) // Adjust the exception type based on your implementation
  public void testAssignTwoPatientsToSameExamRoom() {
    Patient registeredPatient8 = clinic.findPatientByName("Rajorshi", "Sarkar");
    Patient registeredPatient9 = clinic.findPatientByName("John", "Doe");
    clinic.assignPatientToRoom(registeredPatient8, "Surgical");
    clinic.assignPatientToRoom(registeredPatient9, "Surgical");
    // The code should throw an exception to pass this test
  }



  /**
   * Tests the properties and behavior of a visit record.
   */
  @Test
  public void testVisitRecord() {
    // Create LocalDateTime object for registration date and time
    LocalDateTime registrationDateTime = LocalDateTime.of(2024,
        2, 25, 10, 30);

    // Generate a random body temperature within the range of 20 to 40 degrees Celsius
    Random random = new Random();
    double randomTemperature = 20 + (random.nextDouble() * (40 - 20));

    // Create a VisitRecord object
    Visitrecord visitRecord = new Visitrecord(registrationDateTime, "Fever",
        randomTemperature);

    // Test the properties and behavior of the visitRecord object
    // (Assertions would be added here to verify the correctness of the visitRecord object)
  }

  /**
   * Tests the functionality of sending a patient home with physician approval.
   */
  @Test
  public void testSendPatientHomeWithPhysicianApproval() {
    // Assume a patient is registered and assigned to a room

    // Get a registered patient from the clinic
    Patient patient = clinic.findPatientByName("John", "Doe");

    // Simulate the approval process by a physician
    ClinicalStaff physician = clinic.findClinicalStaffByName("Dr. Smith", "Physician");

    // Get the room assigned to the patient before sending them home
    Room assignedRoomBefore = clinic.getAssignedRoomForPatient(patient);

    // Send the patient home with physician approval
    clinic.sendPatientHome(patient, physician);

    // Get the room assigned to the patient after sending them home
    Room assignedRoomAfter = clinic.getAssignedRoomForPatient(patient);

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
   * Tests the scenario where a patient is assigned to a room, then moved to another room, and
   * subsequently another patient is assigned to the vacated initial room.
   */
  @Test
  public void testAssignPtoRoom1AndThenAssignToVacatedRoom() {
    // Register two patients
    Patient patient1 = new Patient(0, "Future",
        "Follicle", "6/6/1986");
    Patient patient2 = new Patient(1, "John",
        "Doe", "1/1/1980");
    clinic.registerNewPatient(patient1);
    clinic.registerNewPatient(patient2);

    // Assign patient1 to an initial room
    clinic.assignPatientToRoom(patient1, "ExamRoom");

    // Get the initial room assigned to patient1
    Room initialRoom = clinic.getAssignedRoomForPatient(patient1);

    // Assign patient1 to another room
    clinic.assignPatientToRoom(patient1, "Triage");

    // Get the room assigned to patient1 after moving
    Room roomAfterMove = clinic.getAssignedRoomForPatient(patient1);

    // Assign patient2 to the initial room vacated by patient1
    clinic.assignPatientToRoom(patient2, initialRoom.getName());

    // Get the room assigned to patient2
    Room roomAssignedToPatient2 = clinic.getAssignedRoomForPatient(patient2);

    // Assert that patient1 is now in the new room and patient2 is in the initial room
    assertEquals("Triage", roomAfterMove.getName());
    assertEquals(initialRoom.getName(), roomAssignedToPatient2.getName());
  }

  /**
   * Tests the assignment of two patients to the same examination room in the clinic.
   */
  @Test
  public void testAssignTwoPatientsToSameExamRoom2() {
    // Assuming patients are registered in the clinic
    Patient registeredPatient1 = new Patient(1, "Rajorshi", "Sarkar", "1980-01-01");
    Patient registeredPatient2 = new Patient(2, "John", "Doe", "1985-05-05");

    // Registering patients in the clinic
    clinic.registerNewPatient(registeredPatient1);
    clinic.registerNewPatient(registeredPatient2);
    // Assign both patients to the same examination room
    clinic.assignPatientToRoom(registeredPatient1, "ExamRoom");
    assertThrows(Exception.class, () -> clinic.assignPatientToRoom(registeredPatient2, "ExamRoom"));
  }

  /**
   * Tests the {@code addVisitRecordForPatient} method of the
   * {@code Patient} class by simulating user input and verifying the output.
   */
  @Test
  public void testAddVisitRecordForPatient() throws IOException {
    // Register a patient
    Patient patient = new Patient(0, "Future", "Follicle", "6/6/1986");
    clinic.registerNewPatient(patient);

    // Prepare input string to mimic user inputs
    String input = "2024-02-27\n12:30:00\nFever\n38.5\n";

    // Set up a ByteArrayInputStream with the input string
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(byteArrayInputStream);

    // Redirect System.out to capture output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    System.setOut(printStream);
    PrintStream originalOut = System.out;
    // Call the method with the mocked Scanner
    patient.addVisitRecordForPatient(scanner);

    // Reset System.out
    System.out.flush();
    System.setOut(originalOut);

    // Get the captured output
    String output = outputStream.toString();

    // Verify that the output contains all the information
    assertTrue(output.contains("Patient Information:"));
    assertTrue(output.contains("Serial Number: 0"));
    assertTrue(output.contains("Name: Future Follicle"));
    assertTrue(output.contains("Date of Birth: 6/6/1986"));
    assertTrue(output.contains("Room Number: 2"));
    assertTrue(output.contains("Room Name: Exam"));
    assertTrue(output.contains("Room Type: Exam"));
    assertTrue(output.contains("Visit Records:"));
    assertTrue(output.contains("Registration Date and Time: 2024-02-27 12:30:00"));
    assertTrue(output.contains("Chief Complaint: Fever"));
    assertTrue(output.contains("Body Temperature: 38.5"));
    assertTrue(output.contains("Visit record added successfully."));
  }

}

