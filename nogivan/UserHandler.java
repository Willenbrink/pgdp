package nogivan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class UserHandler extends DefaultHandler
{
  MapGraph map;
  int amountWays = 0;

  private OSMWay way;
  private long id;
  private List<Long> nodes;
  private boolean oneWay;
  private boolean isValid;

  public UserHandler()
  {
    map = new MapGraph();
  }

  @Override
  public void startElement(
      String uri, String localName, String qName, Attributes attributes)
      throws SAXException
  {
    //Node-Logic
    if(qName.equalsIgnoreCase("node"))
    {
      long id = Long.parseLong(attributes.getValue("id"));
      double lat = Double.parseDouble(attributes.getValue("lat"));
      double lon = Double.parseDouble(attributes.getValue("lon"));
      OSMNode node = new OSMNode(id, lat, lon);
      map.addNode(node);
      return;
    }

    //Start of way
    if(qName.equalsIgnoreCase("way"))
    {
      id = Long.parseLong(attributes.getValue("id"));
      nodes = new ArrayList<>();
      oneWay = false;
      isValid = false;
      return;
    }

    //Nodes in way
    if(qName.equalsIgnoreCase("nd"))
    {
      nodes.add(Long.parseLong(attributes.getValue("ref")));
      return;
    }

    //Tags of way
    if(qName.equalsIgnoreCase("tag"))
    {
      if(attributes.getValue(0).equalsIgnoreCase("oneway"))
      {
        oneWay = attributes.getValue(1).equalsIgnoreCase("yes");
      }
      if(attributes.getValue(0).equalsIgnoreCase("highway"))
      {
        isValid = true;
        String[] invalidTags = new String[]{
            "construction",
            "proposed",
            //"footway",
        };
        for(String tag : invalidTags)
          if(attributes.getValue(1).equalsIgnoreCase(tag))
            isValid = false;
      }
      return;
    }
  }

  @Override
  public void endElement(String uri,
      String localName, String qName) throws SAXException
  {
    if (qName.equalsIgnoreCase("way"))
    {
      if(!isValid)
        return;
      amountWays++;

      int amountEdges = 0;
      way = new OSMWay(id, nodes.toArray(new Long[0]), oneWay, "name");
      for (int i = 0; i < way.getNodes().length-1; i++)
      {
        MapEdge edge = new MapEdge(way.getNodes()[i+1], way);
        map.addEdge(way.getNodes()[i], edge);
        amountEdges++;
      }
      if(amountEdges != way.getNodes().length-1)
        System.err.println("Invalid amount of edges");

      if(!oneWay)
      {
        for (int i = 1; i < way.getNodes().length; i++)
        {
          MapEdge edge = new MapEdge(way.getNodes()[i-1], way);
          map.addEdge(way.getNodes()[i], edge);
          amountEdges++;
        }
      }
    }
  }

  public void cleanNodes()
  {
    Map<Long, Boolean> isValid = new HashMap<>();
    for(Entry<Long, Set<MapEdge>> edges : map.getEdges().entrySet())
    {
      for(MapEdge edge : edges.getValue())
      {
        isValid.put(edge.getTo(), true);
      }
    }

    Set<Long> allNodes = new HashSet<>();
    allNodes.addAll(map.getNodes().keySet());
    for(Long node : allNodes)
    {
      if(!isValid.containsKey(node))
      {
        map.getNodes().remove(node);
      }
    }
  }
}//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
