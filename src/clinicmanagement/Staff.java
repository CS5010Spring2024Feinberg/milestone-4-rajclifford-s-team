package clinicmanagement;

import java.util.List;

/**
 * This abstract class represents a staff member in a clinic.
 */
public abstract class Staff implements StaffInterface {
  private static int nextSerialNumber = 1; // Static counter to ensure unique serial numbers
  protected String jobTitle;
  protected EducationLevel educationLevel;
  protected String firstName;
  protected String lastName;
  private int serialNumber; // Unique serial number for each staff member
  private boolean deactivated; // Flag to indicate whether the staff member is deactivated

  /**

  /**
   * Enumeration for the education level of a staff member.
   */
  public enum EducationLevel {
    DOCTORAL, MASTERS, ALLIED
  }

  /**
   * Constructs a Staff object with the specified attributes.
   *
   * @param jobTitle       The job title of the staff member.
   * @param firstName      The first name of the staff member.
   * @param lastName       The last name of the staff member.
   * @param educationLevel The education level of the staff member.
   */
  public Staff(String jobTitle, String firstName, String lastName, EducationLevel educationLevel)
      throws IllegalArgumentException {
    this.jobTitle = jobTitle;
    this.firstName = firstName;
    this.lastName = lastName;
    this.educationLevel = educationLevel;
    // Assign the next unique serial number and increment the counter
    this.serialNumber = nextSerialNumber++;
    this.deactivated = false;
    if (jobTitle == null || firstName == null || lastName == null
        ||
        educationLevel == null) {
      throw new IllegalArgumentException("Staff is Null List");
    }
  }

  /**
   * Returns the serial number of the object.
   *
   * @return The serial number.
   */
  public int getSerialNumber() {
    return serialNumber;
  }

  /**
   * Get the job title of the staff member.
   *
   * @return The job title.
   */
  @Override
  public String getJobTitle() {
    return jobTitle;
  }

  /**
   * Get the first name of the staff member.
   *
   * @return The first name.
   */
  @Override
  public String getFirstName() {
    return firstName;
  }

  /**
   * Get the last name of the staff member.
   *
   * @return The last name.
   */
  @Override
  public String getLastName() {
    return lastName;
  }

  /**
   * Get the education level of the staff member.
   *
   * @return The education level.
   */
  @Override
  public EducationLevel getEducationLevel() {

    return educationLevel;
  }

  /**
   * Get the full name of the staff member.
   *
   * @return The full name.
   */
  @Override
  public String getFullName() {

    return firstName + " " + lastName;
  }

  /**
   * Sets the deactivated status of the staff member.
   *
   * @param deactivated true if the staff member is deactivated, false otherwise
   */
  public void setDeactivated(boolean deactivated) {
    this.deactivated = deactivated;
  }

  /**
   * Checks if the staff member is deactivated.
   *
   * @return true if the staff member is deactivated, false otherwise
   */
  public boolean isDeactivated() {
    return deactivated;
  }


}


