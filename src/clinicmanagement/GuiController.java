package clinicmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class GuiController {

  protected Clinic clinic;
  protected JFrame frame;
  protected JLabel clinicMapLabel;
  private Map<Integer, Runnable> commands;

  public GuiController(Clinic clinic) {
    this.clinic = clinic;
    this.commands = new HashMap<>();
    initializeCommands();
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

  private void initializeCommands() {
    commands.put(1, () -> clinic.registerNewPatientGUI(this));
    commands.put(2, () -> clinic.assignPatientToRoomGUI(this));
    commands.put(3, () -> clinic.addVisitRecordGUI(this));
    commands.put(4, () -> clinic.registerNewClinicalStaff(this));
    commands.put(5, () -> clinic.assignStaffToPatientGUI());
    commands.put(6, () -> clinic.sendPatientHomeGUI(this));
    commands.put(7, () -> clinic.deactivateStaffGUI());
    commands.put(8, () -> clinic.showPatientDetailsGUI());
    commands.put(9, () -> clinic.unassignStaffFromPatientGUI());
    commands.put(10, () -> clinic.listClinicalStaffAndPatientCountsGUI());
    commands.put(11, () -> clinic.listInactivePatientsForYearGUI());
    commands.put(12, () -> clinic.listClinicalStaffWithIncompleteVisitsGUI(clinic.getClinicalStaffList(), this));
    commands.put(13, () -> clinic.listPatientsWithMultipleVisitsInLastYear(this));
    commands.put(14, () -> System.exit(0));
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("Clinic Menu");
    fileMenu.setFont(new Font("Arial", Font.BOLD, 14));

    addMenuItem(fileMenu, "Register New Patient", 1);
    addMenuItem(fileMenu, "Assign Patient to Room", 2);
    addMenuItem(fileMenu, "Add Visit Record", 3);
    addMenuItem(fileMenu, "Add Clinical Staff", 4);
    addMenuItem(fileMenu, "Assign Clinical Staff to Patient", 5);
    addMenuItem(fileMenu, "Send Patient Home", 6);
    addMenuItem(fileMenu, "Deactivate Staff", 7);
    addMenuItem(fileMenu, "Show Patient Details", 8);
    addMenuItem(fileMenu, "Unassign Clinical Staff from Patient", 9);
    addMenuItem(fileMenu, "List Clinical Staff and Patient Counts", 10);
    addMenuItem(fileMenu, "List Inactive Patients for Over a Year", 11);
    addMenuItem(fileMenu, "List Clinical Staff with Incomplete Visit", 12);
    addMenuItem(fileMenu, "List Patients with Multiple Visits in Last Year", 13);
    addMenuItem(fileMenu, "Exit", 14);

    menuBar.add(fileMenu);
    frame.setJMenuBar(menuBar);
  }

  private void addMenuItem(JMenu menu, String title, int commandKey) {
    JMenuItem menuItem = new JMenuItem(title);
    menuItem.addActionListener(e -> executeCommand(commands.get(commandKey)));
    menu.add(menuItem);
  }

  private void executeCommand(Runnable command) {
    try {
      command.run();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frame, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
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
    SwingUtilities.invokeLater(this::updateMapImage);
  }
}
