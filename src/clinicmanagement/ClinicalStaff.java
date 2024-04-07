package clinicmanagement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A class representing clinical staff members.
 */
public class ClinicalStaff extends Staff implements ClinicalStaffInterface,
    Comparable<ClinicalStaff> {
  private String prefix;
  private String npi; // National Provider Identifier for clinical staff
  private List<Patient> assignedPatients;
  private Set<Integer> uniquePatientSerials = new HashSet<>();

  /**
   * Constructs a ClinicalStaff object with specified attributes.
   *
   * @param jobTitle       The job title of the clinical staff.
   * @param firstName      The first name of the clinical staff.
   * @param lastName       The last name of the clinical staff.
   * @param educationLevel The education level of the clinical staff.
   * @param npi            The National Provider Identifier (NPI) of the clinical staff.
   */
  public ClinicalStaff(String jobTitle, String firstName, String lastName,
                       EducationLevel educationLevel, String npi) {
    super(jobTitle, firstName, lastName, educationLevel);
    if (jobTitle == null || firstName == null || lastName == null || npi == null
        ||
        jobTitle.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || npi.isEmpty()) {
      throw new IllegalArgumentException("Parameters cannot be null or empty");
    }
    this.npi = npi;
    this.assignedPatients = new ArrayList<>();

    // Determine and set the prefix based on the job title
    if ("Nurse".equalsIgnoreCase(jobTitle)) {
      this.prefix = "Nr.";
    } else if ("Physician".equalsIgnoreCase(jobTitle)) {
      this.prefix = "Dr.";
    } else {
      this.prefix = ""; // Default prefix or consider other titles
    }
  }

  /**
   * Gets the Assigned Patient for staff of the clinical staff.
   *
   * @return assigned Patients.
   */
  @Override
  public List<Patient> getAssignedPatients() {
    return assignedPatients;
  }

  /**
   * Gets the National Provider Identifier (NPI) of the clinical staff.
   *
   * @return The NPI of the clinical staff.
   */
  @Override
  public String getNpi() {
    return npi;
  }

  /**
   * Returns a string representation of the ClinicalStaff object.
   *
   * @return A string representation of the ClinicalStaff.
   */
  @Override
  public String toString() {
    return "ClinicalStaff{"
        + "jobTitle='" + jobTitle + '\''
        + ", prefix='" + prefix + '\''
        + ", fullName='" + getFullName() + '\''
        + ", educationLevel='" + educationLevel + '\''
        + ", npi='" + npi + '\''
        + ", assignedPatientsCount=" + assignedPatients.size()
        + '}';
  }

  /**
   * Gets the prefix of the clinical staff.
   *
   * @return The prefix of the clinical staff.
   */
  public String getPrefix() throws IllegalArgumentException {
    if (prefix == null || prefix.isEmpty()) {
      throw new IllegalArgumentException("Prefix cannot be null or empty");
    }
    return prefix;
  }

  /**
   * Assigns a patient for counting unique patient serial numbers.
   *
   * @param patient The patient to assign for counting.
   */
  public void assignPatientforCount(Patient patient) {
    uniquePatientSerials.add(patient.getSerialNumber());
  }

  /**
   * Retrieves the count of unique patient serial numbers.
   *
   * @return The count of unique patient serial numbers.
   */
  public int getUniquePatientCount() {
    return uniquePatientSerials.size();
  }

  /**
  * Computes the hash code of the ClinicalStaff object.
  *
  * @return The hash code value of the object.
  */
  @Override
  public int hashCode() {

    return Objects.hash(super.hashCode(), prefix, npi, assignedPatients);
  }

  /**
   * Checks if this ClinicalStaff object is equal to another object.
   *
   * @param o The object to compare with.
   * @return {@code true} if the objects are equal, {@code false} otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ClinicalStaff)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ClinicalStaff that = (ClinicalStaff) o;
    return Objects.equals(prefix, that.prefix)
        &&
        Objects.equals(npi, that.npi)
        &&
        Objects.equals(assignedPatients, that.assignedPatients);
  }

  /**
   * Compares this ClinicalStaff object with another ClinicalStaff object for ordering.
   *
   * @param o The ClinicalStaff object to be compared.
   * @return A negative integer, zero, or a positive integer as this object is less than,
   *         equal to, or greater than the specified object.
   */
  @Override
  public int compareTo(ClinicalStaff o) {
    // Comparison based on the full name of the clinical staff
    return this.getFullName().compareTo(o.getFullName());
  }

  /**
   * Finds a clinical staff member by their serial number.
   *
   * @param clinic The clinic object to search for the clinical staff member.
   * @param serialNumber The serial number of the clinical staff member to find.
   * @return The ClinicalStaff object with the specified serial number, or null if not found.
   */
  public static ClinicalStaff findClinicalStaffBySerialNumber(Clinic clinic, int serialNumber) {
    Staff staffMember = clinic.findStaffBySerialNumber(serialNumber);
    if (staffMember instanceof ClinicalStaff) {
      return (ClinicalStaff) staffMember;
    }
    return null;
  }
}
