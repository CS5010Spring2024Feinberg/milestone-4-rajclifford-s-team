package clinicmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a visit record in a clinic management system.
 */
public class Visitrecord {
  private LocalDateTime registrationDateTime;
  private String chiefComplaint;
  private double bodyTemperature;

  /**
   * Constructs a visit record with the specified
   * registration date and time, chief complaint, and body temperature.
   *
   * @param registrationDateTime The date and time when the visit was registered.
   * @param chiefComplaint       The chief complaint reported by the patient.
   * @param bodyTemperature      The body temperature of the patient during the visit.
   */
  public Visitrecord(LocalDateTime registrationDateTime,
                     String chiefComplaint, double bodyTemperature) {
    // Assign the values directly without validation in the constructor
    this.registrationDateTime = registrationDateTime;
    this.chiefComplaint = chiefComplaint;
    this.bodyTemperature = bodyTemperature;
  }


  /**
   * Validates if the specified registration date and time is not null.
   *
   * @param registrationDateTime The date and time to validate.
   * @return True if the date and time is valid, otherwise false.
   */
  public static boolean isValidDate(LocalDateTime registrationDateTime) {

    return registrationDateTime != null;
  }

  /**
   * Validates if the specified chief complaint is not null or empty.
   *
   * @param chiefComplaint The chief complaint to validate.
   * @return True if the chief complaint is valid, otherwise false.
   */
  public static boolean isValidComplaint(String chiefComplaint) {
    return chiefComplaint != null && !chiefComplaint.trim().isEmpty();
  }

  /**
   * Validates if the specified body temperature is within a reasonable range.
   *
   * @param bodyTemperature The body temperature to validate.
   * @return True if the body temperature is valid, otherwise false.
   */
  public static boolean isValidTemperature(double bodyTemperature) {
    return bodyTemperature >= 25.0 && bodyTemperature <= 45.0;
  }


  /**
   * Gets the registration date and time of the visit record.
   *
   * @return The registration date and time.
   */
  public LocalDateTime getRegistrationDateTime() {
    return registrationDateTime;
  }

  /**
   * Gets the chief complaint recorded during the visit.
   *
   * @return The chief complaint.
   */
  public String getChiefComplaint() {
    return chiefComplaint;
  }

  /**
   * Gets the body temperature recorded during the visit.
   *
   * @return The body temperature.
   */
  public double getBodyTemperature() {
    return bodyTemperature;
  }

  /**
   * Returns a string representation of the visit record.
   *
   * @return A string containing the registration date and time,
   *        chief complaint, and body temperature.
   */
  @Override
  public String toString() {
    return "Visit Record: "
        +
        "Registration DateTime: " + registrationDateTime
        +
        ", Chief Complaint: '" + chiefComplaint + '\''
        +
        ", Body Temperature: " + String.format("%.1f", bodyTemperature) + "°C";
  }

  /**
   * Checks if the last visit occurred within the last year.
   *
   * @return True if the last visit occurred within the last year, otherwise false.
   */
  public boolean islastvisitwithinayear() {
    LocalDateTime lastVisitDateTime = getRegistrationDateTime();
    LocalDate oneYearAgo = LocalDate.now().minusYears(1);
    return lastVisitDateTime.toLocalDate().isAfter(oneYearAgo)
        || lastVisitDateTime.toLocalDate().isEqual(oneYearAgo);
  }
}
