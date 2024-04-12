package clinicmanagement;

import java.util.Objects;

/**
 * This class represents a non-clinical staff member in a clinic.
 */
class NonClinicalStaff extends Staff implements NonClinicalStaffInterface {
  private String cprLevel; // CPR level for non-clinical staff

  /**
   * Constructs a NonClinicalStaff object with the specified attributes.
   *
   * @param jobTitle       The job title of the non-clinical staff member.
   * @param firstName      The first name of the non-clinical staff member.
   * @param lastName       The last name of the non-clinical staff member.
   * @param educationLevel The education level of the non-clinical staff member.
   * @param cprLevel       The CPR level of the non-clinical staff member.
   */
  public NonClinicalStaff(String jobTitle, String firstName,
                          String lastName, EducationLevel educationLevel, String cprLevel) {
    super(jobTitle, firstName, lastName, educationLevel);
    this.cprLevel = cprLevel;
  }

  /**
   * Checks if this NonClinicalStaff object is equal to another object.
   *
   * @param obj The object to compare to.
   * @return True if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    NonClinicalStaff that = (NonClinicalStaff) obj;
    return Objects.equals(getJobTitle(), that.getJobTitle())
        &&
        Objects.equals(getFirstName(), that.getFirstName())
        &&
        Objects.equals(getLastName(), that.getLastName())
        &&
        getEducationLevel() == that.getEducationLevel()
        &&
        Objects.equals(cprLevel, that.cprLevel);
  }

  /**
   * Generates a hash code value for this NonClinicalStaff object.
   *
   * @return The hash code value.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getJobTitle(),
        getFirstName(), getLastName(), getEducationLevel(), cprLevel);
  }

}
