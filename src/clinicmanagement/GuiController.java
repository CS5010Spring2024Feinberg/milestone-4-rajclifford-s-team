package clinicmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GuiController {

  protected Clinic clinic;
  protected JFrame frame;
  protected JLabel clinicMapLabel;

  public GuiController(Clinic clinic) {
    this.clinic = clinic;
    initializeGui();
  }

  private void initializeGui() {
    createFrame();
    displayWelcomeMessage();
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
    String clinicName = clinic.getName(); // Assuming a getName() method exists
    JLabel welcomeLabel = new JLabel("Welcome to " + clinicName + " Management System!", JLabel.CENTER);
    welcomeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
    frame.add(welcomeLabel, BorderLayout.NORTH);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground(Color.WHITE);
    Font menuFont = new Font("Arial", Font.BOLD, 14);

    JMenu fileMenu = new JMenu("Clinic Menu");
    fileMenu.setFont(menuFont);

    // Register New Patient menu item
    JMenuItem registerPatientMenuItem = new JMenuItem("Register New Patient");
    registerPatientMenuItem.setFont(menuFont);
    registerPatientMenuItem.addActionListener(e -> clinic.registerNewPatientGUI(this));
    fileMenu.add(registerPatientMenuItem);

    // Assign Patient to Room menu item
    JMenuItem assignPatientMenuItem = new JMenuItem("Assign Patient to Room");
    assignPatientMenuItem.setFont(menuFont);
    assignPatientMenuItem.addActionListener(e -> clinic.assignPatientToRoomGUI(this));
    fileMenu.add(assignPatientMenuItem);

    // Add Visitor Record menu item
    JMenuItem addVisitorRecordMenuItem = new JMenuItem("Add Visit Record");
    addVisitorRecordMenuItem.setFont(menuFont);
    addVisitorRecordMenuItem.addActionListener(e -> clinic.addVisitRecordGUI(this));
    fileMenu.add(addVisitorRecordMenuItem);

    JMenuItem addClinicalStaffMenuItem = new JMenuItem("Add Clinical Staff");
    addVisitorRecordMenuItem.setFont(menuFont);
    addClinicalStaffMenuItem.addActionListener(e -> clinic.registerNewClinicalStaff(this));
    fileMenu.add(addClinicalStaffMenuItem);

    // Add Assign Clinical Staff to Patient menu item
    JMenuItem assignStaffToPatientMenuItem = new JMenuItem("Assign Clinical Staff to Patient");
    assignStaffToPatientMenuItem.setFont(menuFont);
    assignStaffToPatientMenuItem.addActionListener(e -> clinic.assignStaffToPatientGUI()); // Add action listener here
    fileMenu.add(assignStaffToPatientMenuItem);

    JMenuItem sendPatientHomeMenuItem = new JMenuItem("Send Patient Home");
    sendPatientHomeMenuItem.setFont(menuFont);
    sendPatientHomeMenuItem.addActionListener(e -> clinic.sendPatientHomeGUI(this));
    fileMenu.add(sendPatientHomeMenuItem);

    JMenuItem deactivateStaffMenuItem = new JMenuItem("Deactivate Staff");
    deactivateStaffMenuItem.setFont(menuFont);
    deactivateStaffMenuItem.addActionListener(e -> clinic.deactivateStaffGUI());
    fileMenu.add(deactivateStaffMenuItem);

    JMenuItem showPatientDetailsMenuItem = new JMenuItem("Show Patient Details");
    showPatientDetailsMenuItem.setFont(new Font("Arial", Font.BOLD, 14));
    showPatientDetailsMenuItem.addActionListener(e -> showPatientDetailsGUI());
    fileMenu.add(showPatientDetailsMenuItem);


    JMenuItem exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.setFont(menuFont);
    exitMenuItem.addActionListener(e -> System.exit(0));
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);
    frame.setJMenuBar(menuBar);
  }

  private void displayClinicMap() {
    clinicMapLabel = new JLabel();
    updateMapImage();
    JScrollPane scrollPane = new JScrollPane(clinicMapLabel);
    frame.add(scrollPane, BorderLayout.CENTER);
  }

  public void updateMapImage() {
    BufferedImage clinicMap = ClinicMap.createClinicMap(clinic);
    if (clinicMap == null) {
      JOptionPane.showMessageDialog(frame, "Failed to generate clinic map.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    ImageIcon newIcon = new ImageIcon(clinicMap);
    clinicMapLabel.setIcon(newIcon);
    clinicMapLabel.revalidate();
    clinicMapLabel.repaint();
  }

  public void updateClinicMap() {
    SwingUtilities.invokeLater(() -> {
      updateMapImage();
      frame.revalidate();
      frame.repaint();
    });


  }
  public void showPatientDetailsGUI() {
    // Initialize the patient selection combo box
    JComboBox<Patient> patientComboBox = new JComboBox<>();
    List<Patient> allPatients = clinic.getAllPatients(); // Retrieve all patients
    if (allPatients.isEmpty()) {
      JOptionPane.showMessageDialog(null, "There are no patients available.", "No Patients", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    // Add only active patients to the combo box
    allPatients.stream()
        .filter(patient -> !patient.isDeactivated())
        .forEach(patientComboBox::addItem);

    // Show a message if there are no active patients
    if (patientComboBox.getItemCount() == 0) {
      JOptionPane.showMessageDialog(null, "There are no active patients available.", "No Active Patients", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    // Show the combo box in a confirm dialog
    int patientChoice = JOptionPane.showConfirmDialog(null, patientComboBox, "Select an Active Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (patientChoice != JOptionPane.OK_OPTION) return; // User cancelled or closed the dialog

    // Retrieve the selected patient and display their full information
    Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
    String patientDetails = selectedPatient.getFullInformation();
    JOptionPane.showMessageDialog(null, patientDetails, "Patient Details", JOptionPane.INFORMATION_MESSAGE);
  }




}
