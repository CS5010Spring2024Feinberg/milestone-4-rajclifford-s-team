package clinicmanagement;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


/**
 * The {@code GuiController} class manages the graphical user interface (GUI)
 * for interacting with the clinic management system.
 * It provides functionalities for creating the main frame, displaying
 * the clinic map, adding menu items, executing commands, and handling
 * user interactions.
 */
public class GuiController {

  protected Clinic clinic;
  protected JFrame frame;
  protected JLabel clinicMapLabel;
  private Map<Integer, Runnable> commands;

  /**
   * Creates a new {@code GuiController} with the specified clinic.
   * Initializes the GUI components and displays them.
   *
   * @param clinic The clinic object to be managed by this GUI controller.
   * @throws IllegalArgumentException If the clinic object is null.
   */
  public GuiController(Clinic clinic) {
    try {
      if (clinic == null) {
        throw new IllegalArgumentException("Clinic object cannot be null.");
      }
      this.clinic = clinic;
      this.commands = new HashMap<>();
      initializeCommands();
      initializeGui();
    } catch (IllegalArgumentException e) {
      // Handle the exception gracefully, e.g., display an error message
      JOptionPane.showMessageDialog(null, "Error: "
          + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }


  /**
   * Initializes the graphical user interface (GUI) components.
   * This method creates the main frame, displays welcome message,
   * clinic map, and adds menu bar.
   */
  private void initializeGui() {
    createFrame();
    displayWelcomeMessage();
    displayClinicMap();
    createMenuBar();
    frame.setVisible(true); // Set frame visible after adding all components
  }

  /**
   * Creates the main frame for the clinic management GUI.
   * The frame includes the title, size, layout, and default close operation.
   */
  private void createFrame() {
    frame = new JFrame("Clinic Management System");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLayout(new BorderLayout()); // Set BorderLayout for the frame
  }

  private void displayWelcomeMessage() {
    String clinicName = clinic.getName(); // Assuming a getName() method exists
    JLabel welcomeLabel = new JLabel("Welcome to " + clinicName
        + " Management System!", JLabel.CENTER);
    welcomeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
    frame.add(welcomeLabel, BorderLayout.NORTH);
  }

  private void initializeCommands() {
    commands.put(1, () -> clinic.registerNewPatientGui(this));
    commands.put(2, () -> clinic.assignPatientToRoomGui(this));
    commands.put(3, () -> clinic.addVisitRecordGui(this));
    commands.put(4, () -> clinic.registerNewClinicalStaff(this));
    commands.put(5, () -> clinic.assignStaffToPatientGui());
    commands.put(6, () -> clinic.sendPatientHomeGui(this));
    commands.put(7, () -> clinic.deactivateStaffGui());
    commands.put(8, () -> clinic.showPatientDetailsGui());
    commands.put(9, () -> clinic.unassignStaffFromPatientGui());
    commands.put(10, () -> clinic.listClinicalStaffAndPatientCountsGui());
    commands.put(11, () -> clinic.listInactivePatientsForYearGui());
    commands.put(12, () -> clinic.listClinicalStaffWithIncompleteVisitsGui(clinic
        .getClinicalStaffList(), this));
    commands.put(13, () -> clinic.listPatientsWithMultipleVisitsInLastYear(this));
    commands.put(14, () -> System.exit(0));
  }

  private void createMenuBar() {
    final JMenuBar menuBar = new JMenuBar();
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
    } catch (IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(frame, "Error occurred: "
          + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void displayClinicMap() {
    clinicMapLabel = new JLabel();
    updateMapImage();
    JScrollPane scrollPane = new JScrollPane(clinicMapLabel);
    frame.add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Updates the displayed clinic map image in the GUI.
   * This method generates a new clinic map image using the ClinicMap class
   * and sets it as the icon for the clinic map label. If the clinic map
   * cannot be generated, an error message dialog is displayed.
   */
  public void updateMapImage() {
    BufferedImage clinicMap = ClinicMap.createClinicMap(clinic);
    if (clinicMap == null) {
      JOptionPane.showMessageDialog(frame, "Failed to generate clinic map.",
          "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    ImageIcon newIcon = new ImageIcon(clinicMap);
    clinicMapLabel.setIcon(newIcon);
    clinicMapLabel.revalidate();
    clinicMapLabel.repaint();
  }

  /**
   * Asynchronously updates the clinic map in the GUI.
   * This method invokes the updateMapImage() method using SwingUtilities.invokeLater(),
   * ensuring that the update operation is performed on the Event Dispatch Thread (EDT).
   * This is necessary for safely updating GUI components from a background thread.
   */
  public void updateClinicMap() {
    SwingUtilities.invokeLater(this::updateMapImage);
  }
}
