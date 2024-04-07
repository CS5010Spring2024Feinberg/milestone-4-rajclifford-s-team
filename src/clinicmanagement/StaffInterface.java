package clinicmanagement;

/**
 * The StaffInterface defines common methods for all staff members.
 */
public interface StaffInterface {
  /**
   * Get the job title of the staff member.
   *
   * @return The job title.
   */
  String getJobTitle();

  /**
   * Get the first name of the staff member.
   *
   * @return The first name.
   */
  String getFirstName();

  /**
   * Get the last name of the staff member.
   *
   * @return The last name.
   */
  String getLastName();

  /**
   * Get the education level of the staff member.
   *
   * @return The education level.
   */
  Staff.EducationLevel getEducationLevel();

  /**
   * Get the full name of the staff member.
   *
   * @return The full name.
   */
  String getFullName();
}

