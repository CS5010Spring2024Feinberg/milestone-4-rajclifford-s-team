import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import clinicmanagement.Clinic;
import clinicmanagement.ClinicFileParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.junit.Before;
import org.junit.Test;

/**
 * This class contains JUnit test cases for the {@link ClinicFileParser} class.
 */
public class ClinicFileParserTest {
  private static final String VALID_FILE_PATH = "/res/clinicfile.txt";
  private static final String EMPTY_CLINIC_NAME_FILE_PATH = "/res/test_empty_clinic_name.txt";
  private static final String INVALID_ROOM_NAME_FILE_PATH = "/res/test_invalid_room_name.txt";
  private static final String INVALID_STAFF_FORMAT_FILE_PATH = "/res/test_invalid_staff_format.txt";
  private static final String INVALID_PATIENT_FORMAT_FILE_PATH = "/res/test_invalid_format.txt";
  private ClinicFileParser clinicFileParser;

  /**
   * Sets up the clinic file parser before each test.
   */
  @Before
  public void setUp() {
    InputStream inputStream = getClass().getResourceAsStream(VALID_FILE_PATH);
    Reader reader = new InputStreamReader(inputStream);
    clinicFileParser = new ClinicFileParser(reader);
  }

  /**
   * Tests parsing a valid clinic file.
   *
   * @throws IOException if there is an issue reading the file.
   */
  @Test
  public void testParsingClinic() throws IOException {
    Clinic clinic = clinicFileParser.parseFile();
    assertNotNull(clinic);
    assertEquals("Sample Clinic", clinic.getName());
    // Add more assertions for clinic properties if needed
  }

  /**
   * Tests parsing when the clinic name is empty.
   *
   * @throws IOException if there is an issue reading the file.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyClinicName() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(EMPTY_CLINIC_NAME_FILE_PATH);
    Reader reader = new InputStreamReader(inputStream);
    clinicFileParser = new ClinicFileParser(reader);
    clinicFileParser.parseFile();
  }

  /**
   * Tests parsing when a room name is invalid.
   *
   * @throws IOException if there is an issue reading the file.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRoomName() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(INVALID_ROOM_NAME_FILE_PATH);
    Reader reader = new InputStreamReader(inputStream);
    clinicFileParser = new ClinicFileParser(reader);
    clinicFileParser.parseFile();
  }

  /**
   * Tests parsing when the staff format is invalid.
   *
   * @throws IOException if there is an issue reading the file.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStaffFormat() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(INVALID_STAFF_FORMAT_FILE_PATH);
    Reader reader = new InputStreamReader(inputStream);
    clinicFileParser = new ClinicFileParser(reader);
    clinicFileParser.parseFile();
  }

  /**
   * Tests parsing when the patient format is invalid.
   *
   * @throws IOException if there is an issue reading the file.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPatientFormat() throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(INVALID_PATIENT_FORMAT_FILE_PATH);
    Reader reader = new InputStreamReader(inputStream);
    clinicFileParser = new ClinicFileParser(reader);
    clinicFileParser.parseFile();
  }


}

