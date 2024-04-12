package clinicmanagement;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GUIDriver {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      // Welcome dialog
      JOptionPane.showMessageDialog(
          null,
          "Welcome to the Clinic Management Centre \n (By: Rajorshi and Cliford).\nPlease select the clinic file to start.",
          "Welcome",
          JOptionPane.INFORMATION_MESSAGE
      );

      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
      fileChooser.setDialogTitle("Select Clinic Data File");
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

      int result = fileChooser.showOpenDialog(null); // Null parent component centers it on the screen

      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        Clinic clinic = initializeClinic(selectedFile.getAbsolutePath());

        if (clinic != null) {
          // Clinic data loaded successfully, proceed to initialize the GUI
          new GuiController(clinic);
        } else {
          // Clinic data failed to load, show an error message and exit
          JOptionPane.showMessageDialog(null, "Failed to load clinic data.", "Error", JOptionPane.ERROR_MESSAGE);
          System.exit(1);
        }
      } else {
        // Exit if no file is chosen
        System.out.println("No file selected, application will exit.");
        System.exit(0);
      }
    });
  }

  private static Clinic initializeClinic(String filePath) {
    Clinic clinic = null;
    try (FileReader fileReader = new FileReader(filePath)) {
      ClinicFileParser fileParser = new ClinicFileParser(fileReader);
      clinic = fileParser.parseFile(); // Parses clinic information from the file
    } catch (IOException e) {
      System.out.println("Failed to parse clinic file: " + e.getMessage());
    }
    return clinic; // Return the clinic object (could be null if an exception occurred)
  }
}
