package clinicmanagement;

import javax.swing.*;

public class GUIDriver {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Clinic clinic = new Clinic(); // Initialize your clinic instance here
      new GuiController(clinic);
    });
  }
}
