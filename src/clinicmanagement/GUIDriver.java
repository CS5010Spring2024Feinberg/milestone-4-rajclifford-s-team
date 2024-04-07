package clinicmanagement;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GUIDriver {

  private static void initializeGui(Clinic clinic) {
    SwingUtilities.invokeLater(() -> {
      GuiController guiController = new GuiController(clinic);
      guiController.initializeGui();
    });
  }

  public static void main(String[] args) {
    String resourcePath = "/clinicfile.txt";  // Path to the resource file

    // Load the resource file
    InputStream inputStream = GUIDriver.class.getResourceAsStream(resourcePath);
    if (inputStream == null) {
      System.err.println("Resource file not found: " + resourcePath);
      return;
    }

    Clinic clinic = null;
    try (Reader fileReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      ClinicFileParser fileParser = new ClinicFileParser(fileReader);
      clinic = fileParser.parseFile(); // Parses clinic information from the file
    } catch (IOException e) {
      System.err.println("Failed to parse clinic file: " + e.getMessage());
      return;
    }

    if (clinic == null) {
      System.err.println("Clinic initialization failed.");
      return;
    }

    // Initialize the GUI
    initializeGui(clinic);
  }
}
