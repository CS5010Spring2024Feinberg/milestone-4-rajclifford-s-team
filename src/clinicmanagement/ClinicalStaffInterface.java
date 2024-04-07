package clinicmanagement;

import java.util.List;

/**
 * The ClinicalStaffInterface extends StaffInterface and adds methods specific to clinical staff.
 */
interface ClinicalStaffInterface extends StaffInterface {

  /**
   * The ClinicalStaffInterface extends StaffInterface and adds methods specific to clinical staff.
   */
  List<Patient> getAssignedPatients();

  /**
   * Get the National Provider Identifier (NPI) of the clinical staff member.
   *
   * @return The NPI.
   */
  String getNpi();

}
