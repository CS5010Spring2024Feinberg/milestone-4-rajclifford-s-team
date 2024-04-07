import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import clinicmanagement.Clinic;
import clinicmanagement.ClinicInterface;
import clinicmanagement.ClinicalStaff;
import clinicmanagement.Patient;
import clinicmanagement.Room;
import clinicmanagement.Visitrecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Visitrecord}.
 */
public class VisitRecordTest {

  @Test
  public void testVisitRecordConstructor() {
    // Arrange
    LocalDateTime registrationDateTime = LocalDateTime.of(2024,
        2, 25, 10, 30);
    String chiefComplaint = "Fever";
    double bodyTemperature = 37.5;

    // Act
    Visitrecord visitRecord = new Visitrecord(registrationDateTime,
        chiefComplaint, bodyTemperature);

    // Assert
    assertEquals(registrationDateTime, visitRecord.getRegistrationDateTime());
    assertEquals(chiefComplaint, visitRecord.getChiefComplaint());
    assertEquals(bodyTemperature, visitRecord.getBodyTemperature(),
        0.001); // Specify delta for double comparison
  }

  /**
   * Tests the {@code toString()} method of Visitrecord.
   * It verifies that the string representation contains expected information.
   */
  @Test
  public void testToString() {
    // Arrange
    LocalDateTime registrationDateTime = LocalDateTime.of(2024,
        2, 25, 10, 30);
    String chiefComplaint = "Fever";
    double bodyTemperature = 37.5;
    Visitrecord visitRecord = new Visitrecord(registrationDateTime,
        chiefComplaint, bodyTemperature);

    // Act
    String toStringResult = visitRecord.toString();

    // Assert
    assertTrue(toStringResult.contains("Registration DateTime: 2024-02-25T10:30"));
    assertTrue(toStringResult.contains("Chief Complaint: 'Fever'"));
    assertTrue(toStringResult.contains("Body Temperature: 37.5°C"));
  }

  /**
   * Tests the constructor of Visitrecord with random temperature.
   * It verifies that the generated temperature is within the specified range.
   */
  @Test
  public void testVisitRecordConstructor2() {
    // Arrange
    LocalDateTime registrationDateTime = LocalDateTime.of(2024,
        2, 25, 10, 30);
    String chiefComplaint = "Fever";
    double minTemperature = 20.0;
    double maxTemperature = 40.0;
    RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
    double bodyTemperature = randomNumberGenerator
        .getRandomInt((int) (minTemperature * 10), (int) (maxTemperature * 10)) / 10.0;

    // Act
    Visitrecord visitRecord = new Visitrecord(registrationDateTime,
        chiefComplaint, bodyTemperature);

    // Assert
    assertEquals(registrationDateTime, visitRecord.getRegistrationDateTime());
    assertEquals(chiefComplaint, visitRecord.getChiefComplaint());
    assertTrue(bodyTemperature >= minTemperature && bodyTemperature <= maxTemperature);
  }

  /**
   * Tests the {@code toString()} method of Visitrecord with random temperature.
   * It verifies that the string representation contains expected information.
   */
  @Test
  public void testToString1() {
    // Arrange
    LocalDateTime registrationDateTime = LocalDateTime.of(2024,
        2, 25, 10, 30);
    String chiefComplaint = "Fever";
    double minTemperature = 20.0;
    double maxTemperature = 40.0;
    RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
    double bodyTemperature = randomNumberGenerator.getRandomInt((int) (minTemperature * 10),
        (int) (maxTemperature * 10)) / 10.0;
    Visitrecord visitRecord = new Visitrecord(registrationDateTime,
        chiefComplaint, bodyTemperature);

    // Act
    String toStringResult = visitRecord.toString();

    // Assert
    assertTrue(toStringResult.contains("Registration DateTime: 2024-02-25T10:30"));
    assertTrue(toStringResult.contains("Chief Complaint: 'Fever'"));
    assertTrue(toStringResult.contains(String.format("Body Temperature: %.1f°C",
        bodyTemperature)));
  }

  /**
   * Tests the {@code hashCode()} method of Visitrecord.
   * It verifies that the hash codes of two equal objects are the same.
   */
  @Test
  public void testHashCode() {
    // Arrange
    LocalDateTime registrationDateTime = LocalDateTime.of(2024,
        2, 25, 10, 30);
    String chiefComplaint = "Fever";
    double bodyTemperature = 37.5;

    Visitrecord visitRecord1 = new Visitrecord(registrationDateTime,
        chiefComplaint, bodyTemperature);
    Visitrecord visitRecord2 = new Visitrecord(registrationDateTime,
        chiefComplaint, bodyTemperature);

    // Act
    int hashCode1 = visitRecord1.hashCode();
    int hashCode2 = visitRecord2.hashCode();

    // Assert
    assertEquals(hashCode1, hashCode2);
  }



}