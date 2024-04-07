package clinicmanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GuiController {

  private Clinic clinic;
  private JFrame frame;
  private JLabel clinicMapLabel;

  public GuiController(Clinic clinic) {
    this.clinic = clinic;
    initializeGui();
  }

  void initializeGui() {
    createFrame();
    displayWelcomeMessage();
    loadClinicFile(); // Prompt user to load clinic file
    displayClinicMap();
    createMenuBar();
    frame.setVisible(true); // Set frame visible after adding all components
  }

  private void createFrame() {
    frame = new JFrame("Clinic Management System");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLayout(new BorderLayout()); // Set BorderLayout for the frame
  }

  private void displayWelcomeMessage() {
    JOptionPane.showMessageDialog(frame, "Welcome to the Clinic Management System!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
  }

  private void loadClinicFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    int result = fileChooser.showOpenDialog(frame);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      loadClinicData(selectedFile.getAbsolutePath());
    }
  }

  private void loadClinicData(String filePath) {
    try (FileReader fileReader = new FileReader(filePath)) {
      ClinicFileParser fileParser = new ClinicFileParser(fileReader);
      this.clinic = fileParser.parseFile();
      JOptionPane.showMessageDialog(frame, "Clinic file loaded successfully.");
      displayClinicName(); // Display clinic name after loading clinic data
    } catch (IOException e) {
      JOptionPane.showMessageDialog(frame, "Failed to load clinic file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void displayClinicName() {
    JLabel clinicNameLabel = new JLabel("Clinic Name: " + clinic.getName());
    clinicNameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the label
    frame.add(clinicNameLabel, BorderLayout.NORTH); // Add clinic name label to the frame's header
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem registerPatientMenuItem = new JMenuItem("Register New Patient");
    registerPatientMenuItem.addActionListener(e -> registerNewPatient());
    fileMenu.add(registerPatientMenuItem);
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.addActionListener(e -> System.exit(0));
    fileMenu.add(exitMenuItem);
    menuBar.add(fileMenu);

    frame.add(menuBar, BorderLayout.SOUTH); // Add menuBar to the frame
  }

  private void displayClinicMap() {
    clinicMapLabel = new JLabel();
    BufferedImage clinicMap = ClinicMap.createClinicMap(clinic);
    if (clinicMap == null) {
      JOptionPane.showMessageDialog(frame, "Failed to generate clinic map.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    clinicMapLabel.setIcon(new ImageIcon(clinicMap));
    frame.add(new JScrollPane(clinicMapLabel), BorderLayout.CENTER); // Add clinicMapLabel to the frame
  }

  private void registerNewPatient() {
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField dobField = new JTextField();
    Object[] message = {
        "First Name:", firstNameField,
        "Last Name:", lastNameField,
        "Date of Birth (M/d/yyyy):", dobField,
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Register New Patient", JOptionPane.OK_CANCEL_OPTION);
    if (option == JOptionPane.OK_OPTION) {
      try {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dobStr = dobField.getText().trim();
        LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("M/d/yyyy"));

        // Placeholder for room number - ensure your Patient constructor handles this appropriately
        Patient newPatient = new Patient(0, firstName, lastName, dobStr);
        RegisterNewPatientCommand command = new RegisterNewPatientCommand(clinic, newPatient);
        command.execute();

        JOptionPane.showMessageDialog(frame, "Patient registered successfully.");
      } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(frame, "Invalid date format. Please use M/d/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, "Error registering patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public static void main(String[] args) {
    Clinic clinic = new Clinic(); // Initialize your clinic instance here
    new GuiController(clinic);
  }
}
