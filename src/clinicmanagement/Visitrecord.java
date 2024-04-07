package clinicmanagement;

import java.time.LocalDateTime;

/**
 * Represents a visit record of a patient,
 * including registration date and time, chief complaint, and body temperature.
 */
public class Visitrecord  {
  private LocalDateTime registrationDateTime;
  private String chiefComplaint;
  private double bodyTemperature;

  /**
   * Constructs a Visitrecord with the specified
   * registration date and time, chief complaint, and body temperature.
   *
   * @param registrationDateTime The date and time of registration.
   * @param chiefComplaint The chief complaint of the visit.
   * @param bodyTemperature The body temperature in degrees Celsius.
   * @throws IllegalArgumentException if any argument is not within expected ranges or conditions.
   */
  public Visitrecord(LocalDateTime registrationDateTime, String chiefComplaint,
                     double bodyTemperature) {
    if (registrationDateTime == null) {
      throw new IllegalArgumentException("Registration date and time cannot be null.");
    }

    if (chiefComplaint == null || chiefComplaint.trim().isEmpty()) {
      throw new IllegalArgumentException("Chief complaint cannot be null or empty.");
    }

    // Assuming a normal human body temperature range from 25.0 to 45.0 degrees Celsius.
    // Adjust this range based on the context and medical standards.
    if (bodyTemperature < 25.0 || bodyTemperature > 45.0) {
      throw new IllegalArgumentException("Body temperature is out of the normal range: "
          + bodyTemperature);
    }

    this.registrationDateTime = registrationDateTime;
    this.chiefComplaint = chiefComplaint;
    this.bodyTemperature = bodyTemperature;
  }


  /**
   * Retrieves the registration date and time.
   *
   * @return date and time of registration.
   */
  public LocalDateTime getRegistrationDateTime() {
    return registrationDateTime;
  }

  /**
   * Retrieves the chief complaint of the patient.
   *
   * @return A string representing the patient's chief complaint.
   */
  public String getChiefComplaint() {
    return chiefComplaint;
  }

  /**
   * Returns the body temperature.
   *
   * @return The body temperature in degrees Celsius.
   * @throws IllegalArgumentException if the body temperature is outside the
   *         range of 25.0 to 45.0 degrees Celsius.
   */
  public double getBodyTemperature() throws IllegalArgumentException {
    if (bodyTemperature < 25.0 || bodyTemperature > 45.0) {
      throw new IllegalArgumentException("Body temperature is out of normal range: "
          + bodyTemperature);
    }
    return bodyTemperature;
  }

  /**
   * Returns a string representation of the visit record.
   *
   * @return A string containing the visit record details.
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
}
