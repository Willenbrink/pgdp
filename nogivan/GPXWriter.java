package nogivan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class GPXWriter {
  private PrintWriter writer;

  public GPXWriter(String fileName) throws FileNotFoundException {
    writer = new PrintWriter(new File(fileName));
  }

  private void writeLine(String line) {
    writer.println(line);
  }

  public void close() {
    writer.close();
  }

  public void writeGPX(RoutingResult rr) {
    writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
    writeLine("<gpx version=\"1.1\" creator=\"Nogivan\">");
    OSMNode[] path = rr.getPath();
    for (int i = 0; i < path.length; i++)
    {
      OSMNode node = path[i];
      double lat = node.getLocation().getLat();
      double lon = node.getLocation().getLon();
      writeLine("  <wpt lat=\"" + lat + "\" lon=\"" + lon + "\"></wpt>");
    }
    writeLine("</gpx>");
    close();
  }
}
