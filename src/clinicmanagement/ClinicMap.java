package clinicmanagement;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Generates and saves a map of the clinic layout based on the provided clinic's rooms.
 * The generated map includes room outlines, room labels, and patient names assigned to each room.
 * The map is saved as a PNG image file named "clinic_map.png" in the "res" directory.
 */
public class ClinicMap {


  /**
   * Generates and saves a map of the clinic layout based on the provided clinic's rooms.
   * The generated map includes room outlines, room labels, and patient names assigned to each room.
   * The map is saved as a PNG image file named "clinic_map.png" in the "res" directory.
   *
   * @param clinic the clinic for which to generate the map
   */
  public static void createClinicMap(Clinic clinic) throws IllegalArgumentException {
    List<Room> rooms = clinic.getRooms(); // Retrieve rooms from the clinic
    if (rooms == null || rooms.isEmpty()) {
      System.out.println("There are no rooms in the clinic to display.");
      return;
    }
    // Image dimensions
    final int width = 1200;
    final int height = 800;
    final int margin = 50; // Add a margin to the image
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.getGraphics();

    // Set background color
    graphics.setColor(Color.black);
    graphics.fillRect(0, 0, width, height);

    // Set font for room labels
    Font font = new Font("SansSerif", Font.BOLD, 12);
    graphics.setFont(font);

    // Debug print: number of rooms to draw
    System.out.println("Number of rooms to draw: " + rooms.size());

    // Calculate actual bounds of the clinic layout
    int minClinicX = rooms.stream().min(Comparator.comparingInt(r -> r
        .getCoordinates().get("lowerLeftX"))).orElseThrow().getCoordinates().get("lowerLeftX");
    int maxClinicX = rooms.stream().max(Comparator.comparingInt(r -> r
        .getCoordinates().get("upperRightX"))).orElseThrow().getCoordinates().get("upperRightX");
    int minClinicY = rooms.stream().min(Comparator.comparingInt(r -> r
        .getCoordinates().get("lowerLeftY"))).orElseThrow().getCoordinates().get("lowerLeftY");
    int maxClinicY = rooms.stream().max(Comparator.comparingInt(r -> r
        .getCoordinates().get("upperRightY"))).orElseThrow().getCoordinates().get("upperRightY");

    // Calculate scaling factors
    double scaleX = (width - (2 * margin)) / (double) (maxClinicX - minClinicX);
    double scaleY = (height - (2 * margin)) / (double) (maxClinicY - minClinicY);
    double scaleFactor = Math.min(scaleX, scaleY);

    // Calculate padding to center the clinic layout
    int horizontalPadding = (width - (int) ((maxClinicX - minClinicX) * scaleFactor)) / 2;
    int verticalPadding = (height - (int) ((maxClinicY - minClinicY) * scaleFactor)) / 2;

    // Drawing rooms and labels
    // Drawing rooms and labels
    for (Room room : rooms) {
      Map<String, Integer> coordinates = room.getCoordinates();

      // Calculate scaled coordinates for the room
      int scaledLowerLeftX = (int) ((coordinates.get("lowerLeftX")
          - minClinicX) * scaleFactor) + horizontalPadding;
      int scaledLowerLeftY = (int) ((maxClinicY - coordinates.get("lowerLeftY"))
          * scaleFactor) + verticalPadding;
      int scaledUpperRightX = (int) ((coordinates.get("upperRightX") - minClinicX)
          * scaleFactor) + horizontalPadding;
      int scaledUpperRightY = (int) ((maxClinicY - coordinates.get("upperRightY"))
          * scaleFactor) + verticalPadding;

      // Calculate room width and height
      int roomWidth = scaledUpperRightX - scaledLowerLeftX;
      int roomHeight = scaledLowerLeftY - scaledUpperRightY;

      // Draw the room
      graphics.setColor(Color.green);
      graphics.drawRect(scaledLowerLeftX, height - scaledLowerLeftY, roomWidth, roomHeight);

      // Draw a border around the canvas
      graphics.setColor(Color.white);
      graphics.drawRect(0, 0, width - 1, height - 1);

      // Construct room label with name, type, and number
      String roomLabel = room.getName()
          + " (" + room.getType().getType() + ", #" + room.getRoomNumber() + ")";

      // Calculate the width of the room label text
      int roomLabelWidth = graphics.getFontMetrics().stringWidth(roomLabel);

      // Adjust the position of the room label to prevent it from spilling out horizontally
      int labelX = scaledLowerLeftX + 5; // Initial position
      if (labelX + roomLabelWidth > scaledUpperRightX) {
        labelX = scaledUpperRightX - roomLabelWidth; // Move label to the left if it spills out
      }

      // Draw room label
      graphics.drawString(roomLabel, labelX, height - scaledLowerLeftY + 15);

      // Get patient names or indicate "empty"
      List<Patient> assignedPatients = room.getAssignedPatients();
      int patientY = height - scaledLowerLeftY + 30 + 20; // Initial patient Y position

      // Calculate the width of the "Patient:" label text
      int patientLabelWidth = graphics.getFontMetrics().stringWidth("Patient:");

      // Draw "Patient:" label on a new line
      graphics.drawString("Patient:", labelX, patientY);
      patientY += 15; // Increase Y position for patient names

      // Draw each patient on a new line, or "Empty" if no patients
      if (assignedPatients.isEmpty()) {
        graphics.drawString("Empty", labelX + patientLabelWidth + 5, patientY);
      } else {
        int maxPatientNameWidth = 0; // Maximum width of patient names
        for (Patient patient : assignedPatients) {
          int patientNameWidth = graphics.getFontMetrics().stringWidth(patient.getFullName());
          if (patientNameWidth > maxPatientNameWidth) {
            maxPatientNameWidth = patientNameWidth; // Update maximum width if needed
          }
        }
        int patientX = labelX + patientLabelWidth + 5;
        // Initial X position for patient names
        if (patientX + maxPatientNameWidth > scaledUpperRightX) {
          patientX = scaledUpperRightX - maxPatientNameWidth - 5;
          // Adjust X position to fit within room bounds
        }
        for (Patient patient : assignedPatients) {
          graphics.drawString(patient.getFullName(), patientX, patientY);
          patientY += 15; // Increase Y position for the next line
        }
      }
    }

    graphics.dispose(); // Dispose graphics to free resources

    // Save the image to file
    File file = new File("res/clinic_map.png");
    try {
      ImageIO.write(image, "PNG", file);
      System.out.println("Clinic map generated successfully: " + file.getAbsolutePath());
    } catch (IOException e) {
      System.err.println("An error occurred while saving the clinic map: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
