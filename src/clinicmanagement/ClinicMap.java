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

    graphics.setColor(Color.black);
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

      graphics.setColor(Color.green);
      graphics.drawRect(x1, y2, x2 - x1, y1 - y2);

      String roomLabel = room.getName() + " (#" + room.getRoomNumber() + ")";
      graphics.drawString(roomLabel, x1 + 5, y2 + 15);

      // Display patient names or indicate the room is empty
      List<Patient> patients = room.getAssignedPatients();
      if (patients.isEmpty()) {
        graphics.drawString("Empty", x1 + 5, y2 + 30);
      } else {
        for (int i = 0; i < patients.size(); i++) {
          graphics.drawString(patients.get(i).getFullName(), x1 + 5, y2 + 30 + (i + 1) * 15);
        }
      }
    }

    graphics.dispose();
    return image;
  }
}
