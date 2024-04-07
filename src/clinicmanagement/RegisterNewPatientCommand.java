package clinicmanagement;

import java.io.IOException;

/**
 * Executes the command to register a new patient. It registers the patient in the clinic
 * and optionally adds a visiting record for the new patient.
 **/
public class RegisterNewPatientCommand implements Command {
  private Clinic clinic;
  private Patient newPatient;

  /**
   * Constructs a {@code RegisterNewPatientCommand} with the specified clinic and patient.
   *
   * @param clinic the clinic to which this command will apply
   * @param newPatient the patient to be registered
   */
  public RegisterNewPatientCommand(Clinic clinic, Patient newPatient) {
    this.clinic = clinic;
    this.newPatient = newPatient;
  }

  /**
   * Executes the command to register a new patient.
   */
  @Override
  public void execute() throws IOException {
    if (newPatient == null) {
      System.out.println("Patient information is missing.");
      return;
    }

    // Register the new patient with the clinic
    clinic.registerNewPatient(newPatient);
    System.out.println("Patient registered successfully.");

    // Here, you could add additional logic to handle visiting records
    // For example, you might want to prompt the user (through the GUI) to add a visit record
    // and then execute the corresponding logic based on the user's response.
  }
}
