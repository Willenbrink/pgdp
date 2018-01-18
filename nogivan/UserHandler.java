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
    if(qName.equalsIgnoreCase("node"))
    {
      //TODO nodelogic
      //Validating the fields
      if(attributes.getLocalName(0).equalsIgnoreCase("id")
          && attributes.getLocalName(1).equalsIgnoreCase("lat")
          && attributes.getLocalName(2).equalsIgnoreCase("lon"))
      {
        OSMNode node = new OSMNode(
            Long.parseLong(attributes.getValue(0)),
            Double.parseDouble(attributes.getValue(1)),
            Double.parseDouble(attributes.getValue(2)));
        map.addNode(node);
        return;
      }

      throw new RuntimeException("Invalid fields");
    }
    if(qName.equalsIgnoreCase("way"))
    {
      //TODO way logic
      id = Long.parseLong(attributes.getValue(0));
      nodes = new ArrayList<>();
      oneWay = false;
      isValid = false;
    }
    if(qName.equalsIgnoreCase("nd"))
    {
      //TODO verify

      nodes.add(Long.parseLong(attributes.getValue(0)));
    }
    if(qName.equalsIgnoreCase("tag"))
    {
      //TODO way logic
      if(attributes.getValue(0).equalsIgnoreCase("oneway"))
      {
        oneWay = attributes.getValue(1).equalsIgnoreCase("yes");
      }
      if(attributes.getValue(0).equalsIgnoreCase("highway"))
      {
        isValid = true;
      }

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
      if(nodes.size() == 0)
        return;
      //TODO verify ways
      way = new OSMWay(id, nodes.toArray(new Long[0]), oneWay, "name");
      for (int i = 0; i < way.getNodes().length-1; i++)
      {
        MapEdge edge = new MapEdge(way.getNodes()[i+1], way);
        map.addEdge(way.getNodes()[i], edge);
      }

      if(!oneWay)
      {
        for (int i = 1; i < way.getNodes().length; i++)
        {
          MapEdge edge = new MapEdge(way.getNodes()[i], way);
          map.addEdge(way.getNodes()[i], edge);
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
}