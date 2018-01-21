package nogivan;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Diese Klasse erlaubt es, aus einer Datei im OSM-Format ein MapGraph-Objekt zu erzeugen. Sie nutzt
 * dazu einen XML-Parser.
 */
public class MapParser {

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
      System.out.println("Nodes: " + map.getNodes().size());
      System.out.println("Edges: " + map.getEdges().size());
      System.out.println("Ways: " + userhandler.amountWays);
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
