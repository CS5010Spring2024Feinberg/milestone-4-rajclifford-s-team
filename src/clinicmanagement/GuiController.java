package clinicmanagement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GuiController {

  private Clinic clinic;
  private JFrame frame;

  public GuiController(Clinic clinic) {
    this.clinic = clinic;
    initializeGui();
  }

  void initializeGui() {
    frame = new JFrame("Clinic Management System");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    JButton registerButton = new JButton("Register New Patient");
    registerButton.addActionListener(e -> registerNewPatient());
    frame.setLayout(new FlowLayout());
    frame.add(registerButton);

    frame.setVisible(true);
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

        Patient newPatient = new Patient(0, firstName, lastName, dobStr);  // Adjust as per your constructor
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
}
