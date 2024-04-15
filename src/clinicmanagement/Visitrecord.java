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

  // Validation methods
  public static boolean isValidDate(LocalDateTime registrationDateTime) {
    return registrationDateTime != null;
  }

  public static boolean isValidComplaint(String chiefComplaint) {
    return chiefComplaint != null && !chiefComplaint.trim().isEmpty();
  }

  public static boolean isValidTemperature(double bodyTemperature) {
    return bodyTemperature >= 25.0 && bodyTemperature <= 45.0;
  }

  // Getters
  public LocalDateTime getRegistrationDateTime() {
    return registrationDateTime;
  }

  public String getChiefComplaint() {
    return chiefComplaint;
  }

  public double getBodyTemperature() {
    return bodyTemperature;
  }

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
