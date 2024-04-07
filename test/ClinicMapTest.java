import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import clinicmanagement.Clinic;
import clinicmanagement.ClinicMap;
import clinicmanagement.ClinicalStaff;
import clinicmanagement.Patient;
import clinicmanagement.Room;
import clinicmanagement.Staff;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import jdk.internal.access.JavaNetUriAccess;
import org.junit.Before;
import org.junit.Test;

/**
 * The ClinicMapTest class contains test cases to verify the functionality
 * of creating a clinic map.
 */
public class ClinicMapTest {

  private static final JavaNetUriAccess JAI = null;
  private Clinic clinic;

  @Before
  public void setUp() {
    clinic = new Clinic();
  }

  @Test
  public void testCreateClinicMap() {
    // Create rooms
    Room room1 = new Room("28 12 35 19 waiting Front", 1);
    Room room2 = new Room("20 10 30 15 exam Triage", 2);

    // Add rooms to the clinic
    clinic.addRoom(room1);
    clinic.addRoom(room2);

    // Create patients
    Patient patient1 = new Patient(1, "John",
        "Doe", "1980-01-01");
    Patient patient2 = new Patient(2, "Jane",
        "Smith", "1985-05-15");

    // Assign patients to rooms
    clinic.registerNewPatient(patient1);
    clinic.registerNewPatient(patient2);
    clinic.assignPatientToRoom(patient1, room1.getName());
    clinic.assignPatientToRoom(patient2, room2.getName());

    // Generate the clinic map
    ClinicMap.createClinicMap(clinic);

    // Check if the clinic map file has been created
    File file = new File("res/clinic_map.png");
    assertTrue("Clinic map file should be generated", file.exists());
  }

  /**
   * Generates and saves a map of the clinic layout based on the provided clinic's rooms.
   * The map includes room outlines, room labels, and patient names assigned to each room.
   * The generated map is saved as a PNG image file named "clinic_map.png" in the "res"
   *         directory.
   */
  @Test
  public void testEmptyRoomLabel()  {
    // Generate the map
    Clinic clinic = new Clinic();
    List<Object> rooms = new ArrayList<>().reversed();
    Room room1 = new Room("28 12 35 19 waiting Front", 1);
    Room room2 = new Room("20 10 30 15 exam Triage", 2);
    rooms.add(room1);
    rooms.add(room2);
    clinic.addRoom((Room) rooms);
    ClinicMap clinicMap = new ClinicMap();
    clinicMap.createClinicMap(clinic);

    // Load the generated image
    URI image = JAI.create("fileload", "res/clinic_map.png");

    // Load the generated image
    File file = new File("res/clinic_map.png");
    try {
      BufferedImage image2 = ImageIO.read(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * Generates and saves a map of the clinic layout based on the provided clinic's rooms.
   * The generated map includes room outlines, room labels, and patient names assigned
   * to each room.
   * The map is saved as a PNG image file named "clinic_map.png" in the "res" directory.
   */
  @Test
  public void testGenerateMapWithPatientAndStaff() throws IOException {
    // Create a new clinic
    Clinic clinic = new Clinic();

    // Create a room
    Room room = new Room("10 10 20 20", 1);

    // Add the room to the clinic
    List<Room> rooms = new ArrayList<>();
    rooms.add(room);
    clinic.addRoom((Room) rooms);

    // Create two staff members
    ClinicalStaff staff1 = new ClinicalStaff("Physician",
        "Smith", "John", Staff.EducationLevel.DOCTORAL, "1234567891");
    ClinicalStaff staff2 = new ClinicalStaff("Nurse", "Johnson",
        "Jane", Staff.EducationLevel.DOCTORAL, "1234567892");

    // Register the staff members in the clinic
    clinic.registerNewClinicalStaff(staff1);
    clinic.registerNewClinicalStaff(staff2);

    // Create a patient
    Patient patient = new Patient(1, "Doe", "John",
        "1980-01-01");

    // Assign the patient to the room
    clinic.assignPatientToRoom(patient, ((Room) rooms).getName());
    // Assign both staff members to the patient
    patient.getAssignedClinicalStaff().add(staff1);
    patient.getAssignedClinicalStaff().add(staff2);

    // Generate the clinic map
    ClinicMap.createClinicMap(clinic);

    // Load the generated image
    File file = new File("res/clinic_map.png");
    BufferedImage image = ImageIO.read(file);

    // Assert that the image is not null and contains the room label
    assertNotNull(image);

  }


}




