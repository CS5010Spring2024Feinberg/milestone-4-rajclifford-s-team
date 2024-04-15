package clinicmanagement;

import java.awt.Font;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;




/**
 * A simulated view for a clinic management system, allowing
 * interaction with the system's functionality
 * through simulated button clicks.
 */
public class MockView {

  protected MockModel clinic;
  protected JFrame frame;
  protected JLabel clinicMapLabel;
  private Map<Integer, Runnable> commands;
  private JMenu menu;

  private String mockFirstName;
  private String mockLastName;
  private String mockdob;

  private String mockcFirstName;
  private String mockclastname; // Corrected variable name
  private String mockcJob;
  private String mockcEducation;
  private String mockCnpi;

  private LocalDateTime time;
  private String complaint;
  private double temp;

  private int staff;
  private int patient;

  private int patientRecord;

  private int patientHome;

  private int staffDeactivate;

  private int unassignStaff;
  private int unassignPatient;


  /**
   * Constructs a MockView with the specified MockModel.
   *
   * @param clinic The MockModel instance representing the clinic.
   */
  public MockView(MockModel clinic) {
    try {
      if (clinic == null) {
        throw new IllegalArgumentException("Clinic object cannot be null.");
      }
      this.clinic = clinic;
      this.commands = new HashMap<>();
      initializeCommands();
      createMenuBar();
    } catch (IllegalArgumentException e) {
      // Handle the exception gracefully, e.g., display an error message
      JOptionPane.showMessageDialog(null, "Error: "
          + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }


  private void initializeCommands() {
    commands.put(1, () -> clinic.registerNewPatientGui(this, this.mockFirstName,
        this.mockLastName, this.mockdob));
    commands.put(2, () -> clinic.assignPatientToRoomGui(this));
    commands.put(3, () -> clinic.addVisitRecordGui(this, this.patientRecord,
        this.time, this.complaint, this.temp));
    commands.put(4, () -> clinic.registerNewClinicalStaff(this, this.mockcFirstName,
        this.mockclastname, this.mockcJob, this.mockcEducation, this.mockCnpi));
    commands.put(5, () -> clinic.assignStaffToPatientGui(this.staff, this.patient));
    commands.put(6, () -> clinic.sendPatientHomeGui(this, this.patientHome));
    commands.put(7, () -> clinic.deactivateStaffGui(this.staffDeactivate));
    commands.put(8, () -> clinic.showPatientDetailsGui());
    commands.put(9, () -> clinic.unassignStaffFromPatientGui(this.unassignStaff,
        this.unassignPatient));
    commands.put(10, () -> clinic.listClinicalStaffAndPatientCountsGui());
    commands.put(11, () -> clinic.listInactivePatientsForYearGui());
    commands.put(12, () -> clinic.listClinicalStaffWithIncompleteVisitsGui(clinic
            .getClinicalStaffList(),
        this));
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

    this.menu = fileMenu;
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

  /**
   * Simulates a button click to register a new patient with the provided details.
   *
   * @param mockFirstName The mock first name of the patient.
   * @param mockLastName  The mock last name of the patient.
   * @param mockdob       The mock date of birth of the patient.
   */
  public void simulateRegisterNewPatientPress(String mockFirstName,
                                              String mockLastName, String mockdob) {
    this.mockFirstName = mockFirstName;
    this.mockLastName = mockLastName;
    this.mockdob = mockdob;
    JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(0);
    menuItem.doClick();
  }

  /**
   * Simulates a button click to assign a patient to an exam room.
   */
  public void simulateAssignPatientToExamPress() {
    JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(1);
    menuItem.doClick();
  }

  /**
   * Simulates a button click to add a visit record for a patient.
   *
   * @param patientRecord The index of the patient to add the visit record for.
   * @param time          The time of the visit record.
   * @param complaint     The complaint recorded during the visit.
   * @param temp          The temperature recorded during the visit.
   */
  public void simulateAddVisitRecordPress(int patientRecord,
                                          LocalDateTime time, String complaint, double temp) {
    this.patientRecord = patientRecord;
    this.time = time;
    this.complaint = complaint;
    this.temp = temp;
    JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(2);
    menuItem.doClick();
  }

  /**
   * Simulates a button click to register a new patient with the provided details.
   *
   * @param mockcFirstName The mock first name of the patient.
   * @param mockcLastName  The mock last name of the patient.
   * @param mockcJob       The mock date of birth of the patient.
   * @param mockceducation edu level
   * @param mockCnpi npi number
   */
  public void simulateRegisterClinicianPress(String mockcFirstName,
                                             String mockcLastName,
                                             String mockcJob,
                                             String mockceducation, String mockCnpi) {
    this.mockcFirstName = mockcFirstName;
    this.mockclastname = mockcLastName;
    this.mockcJob = mockcJob;
    this.mockcEducation = mockceducation;
    this.mockCnpi = mockCnpi;
    JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(3);
    menuItem.doClick();
  }

  public void simulateAssignStaffToPatientPress(int staff, int patient) {
    JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(4);
    menuItem.doClick();
  }

  /**
   * Simulates pressing the button to send a patient home.
   *
   * @param patient The ID of the patient to send home.
   */
  public void simulateSendPatientHomePress(int patient) {
    try {
      if (patient < 0) {
        throw new IllegalArgumentException("Patient ID cannot be negative.");
      }
      this.patientHome = patient;
      JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(5);
      menuItem.doClick();
    } catch (IllegalArgumentException e) {
      // Handle the exception gracefully, e.g., display an error message
      JOptionPane.showMessageDialog(null, "Error: "
          + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Simulates pressing the button to deactivate a staff member.
   *
   * @param staff The ID of the staff member to deactivate.
   */
  public void simulateDeactivateStaffPress(int staff) {
    try {
      if (staff < 0) {
        throw new IllegalArgumentException("Staff ID cannot be negative.");
      }
      this.staffDeactivate = staff;
      JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(6);
      menuItem.doClick();
    } catch (IllegalArgumentException e) {
      // Handle the exception gracefully, e.g., display an error message
      JOptionPane.showMessageDialog(null, "Error: "
          + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Simulates pressing the button to unassign a staff member from a patient.
   *
   * @param staff   The ID of the staff member to unassign.
   * @param patient The ID of the patient from which to unassign the staff member.
   */
  public void simulateUnassignStaffPress(int staff, int patient) {
    try {
      if (staff < 0 || patient < 0) {
        throw new IllegalArgumentException("Staff ID and Patient ID cannot be negative.");
      }
      this.unassignStaff = staff;
      this.unassignPatient = patient;
      JMenuItem menuItem = (JMenuItem) this.menu.getMenuComponent(8);
      menuItem.doClick();
    } catch (IllegalArgumentException e) {
      // Handle the exception gracefully, e.g., display an error message
      JOptionPane.showMessageDialog(null, "Error: "
          + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
