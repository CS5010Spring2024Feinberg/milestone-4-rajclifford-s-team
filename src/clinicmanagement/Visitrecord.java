package clinicmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Visitrecord {
  private LocalDateTime registrationDateTime;
  private String chiefComplaint;
  private double bodyTemperature;

  public Visitrecord(LocalDateTime registrationDateTime, String chiefComplaint, double bodyTemperature) {
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
    return "Visit Record: " +
        "Registration DateTime: " + registrationDateTime +
        ", Chief Complaint: '" + chiefComplaint + '\'' +
        ", Body Temperature: " + String.format("%.1f", bodyTemperature) + "°C";
  }

  // Private method to check if the last visit occurred within the last year
  public boolean isLastVisitWithinAYear() {
    LocalDateTime lastVisitDateTime = getRegistrationDateTime();
    LocalDate oneYearAgo = LocalDate.now().minusYears(1);
    return lastVisitDateTime.toLocalDate().isAfter(oneYearAgo) || lastVisitDateTime.toLocalDate().isEqual(oneYearAgo);
  }
}
