package clinicmanagement;

import static clinicmanagement.GuiDriver.initializeClinic;
import static clinicmanagement.GuiDriver.selectClinicFile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
  private Map<Integer, Command> commands;

  /**
   * Creates a new {@code GuiController} with the specified clinic.
   * Initializes the GUI components and displays them.
   *
   * @param clinic The clinic object to be managed by this GUI controller.
   * @throws IllegalArgumentException If the clinic object is null.
   */
  public GuiController(Clinic clinic) {
    if (clinic == null) {
      throw new IllegalArgumentException("Clinic object cannot be null.");
    }
    this.clinic = clinic;
    this.commands = new HashMap<>();
    initializeCommands();
    initializeGui();
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

  /**
   * Displays Welcome bar in the GUI.
   */
  private void displayWelcomeMessage() {
    String clinicName = clinic.getName();
    JLabel welcomeLabel = new JLabel("Welcome to " + clinicName
        + " Management System!", JLabel.CENTER);
    welcomeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
    frame.add(welcomeLabel, BorderLayout.NORTH);
  }

  /**
   * Initializes the commands associated with menu items.
   */
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
    commands.put(14, this::loadNewClinicFile);
    commands.put(15, () -> System.exit(0));
  }

  /**
   * Creates the menu bar in the GUI.
   */
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
    addMenuItem(fileMenu, "Clear the Current Clinic and Load new Clinic File", 14);
    addMenuItem(fileMenu, "Exit", 15);
    menuBar.add(fileMenu);
    frame.setJMenuBar(menuBar);
  }

  /**
   * Add the command items as items in menu bar in the GUI.
   */
  private void addMenuItem(JMenu menu, String title, int commandKey) {
    JMenuItem menuItem = new JMenuItem(title);
    menuItem.addActionListener(e -> executeCommand(commands.get(commandKey)));
    menu.add(menuItem);
  }


  /**
   * Executes the specified command.
   *
   * @param command The command to execute.
   */
  private void executeCommand(Command command) {
    try {
      command.execute();
    } catch (IllegalArgumentException | IOException ex) {
      JOptionPane.showMessageDialog(frame, "Error occurred: "
          + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Displays the clinic map in the GUI.
   */
  private void displayClinicMap() {
    clinicMapLabel = new JLabel();
    updateMapImage();
    JScrollPane scrollPane = new JScrollPane(clinicMapLabel);
    frame.add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Loads a new clinic file into the GUI.
   */
  private void loadNewClinicFile() {
    // Clear the current clinic model
    clinic.clearModel();

    // Prompt the user to select a new clinic data file
    JOptionPane.showMessageDialog(frame, "Please select a new clinic data file.");
    File selectedFile = selectClinicFile();

    if (selectedFile != null) {
      Clinic newClinic = initializeClinic(selectedFile.getAbsolutePath());

      if (newClinic != null) {
        // Clinic initialized successfully with data from the new file
        clinic = newClinic; // Update clinic reference

        // Update clinic name
        String newName = newClinic.getName();
        clinic.setName(newName);

        // Update the map
        updateMapImage();
        updateClinicMap();
        // Update welcome message with new clinic name
        updateWelcomeMessage();
        // Show success message
        JOptionPane.showMessageDialog(frame, "New clinic data loaded successfully.");
      } else {
        // Error loading clinic data from the new file
        JOptionPane.showMessageDialog(frame, "Failed to load clinic data from the new file.",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      // No file selected, display message and do nothing
      JOptionPane.showMessageDialog(frame, "No new clinic data file selected.");
    }
  }

  /**
   * Updates the displayed welcome message with the current clinic name.
   */
  private void updateWelcomeMessage() {
    String clinicName = clinic.getName();
    Component[] components = frame.getContentPane().getComponents();
    for (Component component : components) {
      if (component instanceof JLabel) {
        JLabel label = (JLabel) component;
        if (label.getText().startsWith("Welcome to ")) {
          label.setText("Welcome to " + clinicName + " Management System!");
          return;
        }
      }
    }
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
