package clinicmanagement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class is responsible for getting clinical staff information.
 */
public class ClinicalStaffInput {
  private static final Set<String> VALID_JOB_TITLES = new HashSet<>(Arrays.asList("physician",
      "nurse", "technician"));
  private static final Set<String> VALID_EDUCATIONAL_LEVELS
      = new HashSet<>(Arrays.asList("doctoral", "masters", "allied"));
  private Scanner scanner;
  private String staffFirstName;
  private String staffLastName;
  private String jobTitle;
  private String educationalLevel;
  private String npi;

  /**
   * Get clinical staff information from the user.
   *
   * @param sc The Scanner object for input.
   * @return A ClinicalStaff object containing the entered information.
   */
  public ClinicalStaff getClinicalStaffInformation(Scanner sc) throws IllegalArgumentException {
    while (true) {
      try {
        System.out.print("Enter staff first name (or 'q' to quit): ");
        String input = sc.nextLine().trim();
        if ("q".equalsIgnoreCase(input)) {
          System.out.println("Input process cancelled.");
          return null; // Return null to indicate cancellation
        }
        staffFirstName = input;
        System.out.print("Enter staff last name (or 'q' to quit): ");
        input = sc.nextLine().trim();
        if ("q".equalsIgnoreCase(input)) {
          System.out.println("Input process cancelled.");
          return null; // Return null to indicate cancellation
        }
        staffLastName = input;
        jobTitle = promptForJobTitle(sc);
        educationalLevel = promptForEducationalLevel(sc);
        npi = promptForNpi(sc);
        // Break the loop if all inputs are provided
        break;
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage() + " Please try again.");
      }
    }

    return new ClinicalStaff(jobTitle, staffFirstName, staffLastName,
        Staff.EducationLevel.valueOf(educationalLevel.toUpperCase()), npi);
  }


  /**
   * Prompt the user for a valid job title.
   *
   * @param sc The Scanner object for input.
   * @return A valid job title entered by the user.
   */
  private String promptForJobTitle(Scanner sc) throws IllegalArgumentException {
    String jobTitle;
    while (true) {
      System.out.print("Enter job title (Physician, Nurse, Technician): ");
      jobTitle = sc.nextLine().toLowerCase();
      if (VALID_JOB_TITLES.contains(jobTitle)) {
        return jobTitle.substring(0, 1).toUpperCase()
            + jobTitle.substring(1); // Capitalize first letter
      } else {
        throw new IllegalArgumentException("Invalid job title."
            +
            " Valid options are: Physician, Nurse, Technician.");
      }
    }
  }

  /**
   * Prompt the user for a valid educational level.
   *
   * @param sc The Scanner object for input.
   * @return A valid educational level entered by the user.
   */
  private String promptForEducationalLevel(Scanner sc) throws IllegalArgumentException {
    String educationalLevel;
    while (true) {
      System.out.print("Enter educational level (Doctoral, Masters, Allied): ");
      educationalLevel = sc.nextLine().toLowerCase();
      if (VALID_EDUCATIONAL_LEVELS.contains(educationalLevel)) {
        return educationalLevel.toUpperCase(); // Convert to uppercase for enum matching
      } else {
        throw new IllegalArgumentException("Invalid educational level. Valid options are:"
            +
            " Doctoral, Masters, Allied.");
      }
    }
  }

  /**
   * Prompt the user for a valid NPI (National Provider Identifier).
   *
   * @param sc The Scanner object for input.
   * @return A valid NPI entered by the user.
   */
  private String promptForNpi(Scanner sc) throws IllegalArgumentException {
    while (true) {
      System.out.print("Enter staff NPI (10 digits): ");
      String npi = sc.nextLine();
      if (npi.matches("\\d{10}")) {
        return npi;
      } else {
        throw new IllegalArgumentException("Invalid NPI. The NPI must contain 10 digits.");
      }
    }
  }
}
