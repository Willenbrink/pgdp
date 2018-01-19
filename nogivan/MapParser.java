package nogivan;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Diese Klasse erlaubt es, aus einer Datei im OSM-Format ein MapGraph-Objekt zu erzeugen. Sie nutzt
 * dazu einen XML-Parser.
 */
public class MapParser {

  public static void main(String[] args)
  {
    parseFile("campus_garching.osm");
  }

  public static MapGraph parseFile(String fileName) {
    File inputFile = new File(fileName);
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      UserHandler userhandler = new UserHandler();
      saxParser.parse(inputFile, userhandler);
      userhandler.cleanNodes();
      MapGraph map = userhandler.map;

      System.out.println();
      System.out.println(map.getNodes().size());
      System.out.println(map.getEdges().size());
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}