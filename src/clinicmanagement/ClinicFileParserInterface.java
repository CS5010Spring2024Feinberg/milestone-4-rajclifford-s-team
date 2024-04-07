package clinicmanagement;

import java.io.IOException;
import java.io.Reader;

/**
 * Interface for parsing clinic files.
 */
public interface ClinicFileParserInterface {

  /**
   * Parses a clinic file and returns a Clinic object representing the clinic's data.
   *
   * @return The Clinic object parsed from the file.
   * @throws IOException if an I/O error occurs while parsing the file.
   */
  Clinic parseFile() throws IOException;
}
