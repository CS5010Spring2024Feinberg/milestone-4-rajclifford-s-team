package clinicmanagement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ClinicMap {

  public static BufferedImage createClinicMap(Clinic clinic) {
    List<Room> rooms = clinic.getRooms();
    if (rooms == null || rooms.isEmpty()) {
      System.out.println("There are no rooms in the clinic to display.");
      return null;
    }

    final int width = 1200;
    final int height = 800;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.getGraphics();

    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, width, height);

    Font font = new Font("SansSerif", Font.BOLD, 12);
    graphics.setFont(font);

    int minClinicX = rooms.stream().min(Comparator.comparingInt(r -> r.getCoordinates().get("lowerLeftX"))).orElseThrow().getCoordinates().get("lowerLeftX");
    int maxClinicX = rooms.stream().max(Comparator.comparingInt(r -> r.getCoordinates().get("upperRightX"))).orElseThrow().getCoordinates().get("upperRightX");
    int minClinicY = rooms.stream().min(Comparator.comparingInt(r -> r.getCoordinates().get("lowerLeftY"))).orElseThrow().getCoordinates().get("lowerLeftY");
    int maxClinicY = rooms.stream().max(Comparator.comparingInt(r -> r.getCoordinates().get("upperRightY"))).orElseThrow().getCoordinates().get("upperRightY");

    double scaleX = (width - 100.0) / (maxClinicX - minClinicX);
    double scaleY = (height - 100.0) / (maxClinicY - minClinicY);
    double scaleFactor = Math.min(scaleX, scaleY);

    int paddingX = (width - (int) ((maxClinicX - minClinicX) * scaleFactor)) / 2;
    int paddingY = (height - (int) ((maxClinicY - minClinicY) * scaleFactor)) / 2;

    for (Room room : rooms) {
      Map<String, Integer> coordinates = room.getCoordinates();
      int x1 = (int) ((coordinates.get("lowerLeftX") - minClinicX) * scaleFactor) + paddingX;
      int y1 = height - ((int) ((coordinates.get("lowerLeftY") - minClinicY) * scaleFactor) + paddingY);
      int x2 = (int) ((coordinates.get("upperRightX") - minClinicX) * scaleFactor) + paddingX;
      int y2 = height - ((int) ((coordinates.get("upperRightY") - minClinicY) * scaleFactor) + paddingY);

      // Set the color for the room based on its type
      Color roomColor = getColorForRoomType(String.valueOf(room.getType()));
      graphics.setColor(roomColor);
      graphics.fillRect(x1, y2, x2 - x1, y1 - y2); // Fill the rectangle with selected color

      // Set the color for the room border
      graphics.setColor(Color.black);
      graphics.drawRect(x1, y2, x2 - x1, y1 - y2); // Draw the room border

      // Set font for room title
      Font roomFont = new Font("SansSerif", Font.BOLD, 12);
      graphics.setFont(roomFont);

      // Room title
      String roomLabel = room.getName() + " (#" + room.getRoomNumber() + ")";
      int labelWidth = graphics.getFontMetrics().stringWidth(roomLabel);
      int labelStartX = x1 + 5; // Start from the left with a margin
      if (labelStartX + labelWidth > x2) {
        labelStartX = x2 - labelWidth - 5; // Adjust to fit within the right boundary
      }
      int titleY = y2 + 15;
      graphics.drawString(roomLabel, labelStartX, titleY);

      // Space between the title and patient names
      int spaceBelowTitle = 5; // Adjust the space as needed

      // Set font for patient names to one size smaller
      Font patientFont = new Font("SansSerif", Font.PLAIN, 11);
      graphics.setFont(patientFont);

      // Patient names
      List<Patient> patients = room.getAssignedPatients();
      int patientNameY = titleY + graphics.getFontMetrics(roomFont).getHeight() + spaceBelowTitle;
      if (patients.isEmpty()) {
        graphics.drawString("Empty", labelStartX, patientNameY);
      } else {
        for (Patient patient : patients) {
          String patientName = patient.getFullName();
          int patientNameWidth = graphics.getFontMetrics().stringWidth(patientName);
          int patientNameStartX = x1 + 5; // Start from the left with a margin
          if (patientNameStartX + patientNameWidth > x2) {
            patientNameStartX = x2 - patientNameWidth - 5; // Adjust to fit within the right boundary
          }
          graphics.drawString(patientName, patientNameStartX, patientNameY);
          patientNameY += graphics.getFontMetrics(patientFont).getHeight(); // Increment Y position for next patient name
        }
      }

      // Reset the font back to room title font for the next room
      graphics.setFont(roomFont);
    }





    graphics.dispose();
    return image;
  }




  public static Room getRoomFromCoordinates(Clinic clinic, int x, int y) {
    List<Room> rooms = clinic.getRooms();
    if (rooms == null || rooms.isEmpty()) {
      return null; // No rooms available
    }

    // Dimensions used in createClinicMap
    final int width = 1200;
    final int height = 800;

    // Minimum and maximum coordinates used in createClinicMap
    int minClinicX = rooms.stream().min(Comparator.comparingInt(r -> r.getCoordinates().get("lowerLeftX"))).orElseThrow().getCoordinates().get("lowerLeftX");
    int maxClinicX = rooms.stream().max(Comparator.comparingInt(r -> r.getCoordinates().get("upperRightX"))).orElseThrow().getCoordinates().get("upperRightX");
    int minClinicY = rooms.stream().min(Comparator.comparingInt(r -> r.getCoordinates().get("lowerLeftY"))).orElseThrow().getCoordinates().get("lowerLeftY");
    int maxClinicY = rooms.stream().max(Comparator.comparingInt(r -> r.getCoordinates().get("upperRightY"))).orElseThrow().getCoordinates().get("upperRightY");

    // Scale factors used in createClinicMap
    double scaleX = (width - 100.0) / (maxClinicX - minClinicX);
    double scaleY = (height - 100.0) / (maxClinicY - minClinicY);
    double scaleFactor = Math.min(scaleX, scaleY);

    // Padding used in createClinicMap
    int paddingX = (width - (int) ((maxClinicX - minClinicX) * scaleFactor)) / 2;
    int paddingY = (height - (int) ((maxClinicY - minClinicY) * scaleFactor)) / 2;

    // Translate click coordinates to clinic map coordinates
    int clinicX = (int) ((x - paddingX) / scaleFactor + minClinicX);
    int clinicY = (int) (((height - y - paddingY) / scaleFactor) + minClinicY);

    // Iterate over rooms to find a match
    for (Room room : rooms) {
      Map<String, Integer> coordinates = room.getCoordinates();
      int lowerLeftX = coordinates.get("lowerLeftX");
      int upperRightX = coordinates.get("upperRightX");
      int lowerLeftY = coordinates.get("lowerLeftY");
      int upperRightY = coordinates.get("upperRightY");

      if (clinicX >= lowerLeftX && clinicX <= upperRightX && clinicY >= lowerLeftY && clinicY <= upperRightY) {
        return room;
      }
    }

    return null; // No room found at the clicked coordinates
  }

  private static Color getColorForRoomType(String type) {
    switch (type.toLowerCase()) {
      case "surgical":
        return new Color(255, 228, 196, 123); // Light bisque color with some transparency
      case "exam":
        return new Color(144, 238, 144, 123); // Light green color with some transparency
      case "procedure":
        return new Color(173, 216, 230, 123); // Light blue color with some transparency
      case "waiting":
        return new Color(255, 250, 205, 123); // Light yellow color with some transparency
      default:
        return new Color(240, 240, 240, 123); // Default light gray with some transparency
    }
  }



}
