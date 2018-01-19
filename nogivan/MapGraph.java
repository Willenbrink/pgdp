package nogivan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Diese Klasse repräsentiert den Graphen der Straßen und Wege aus
 * OpenStreetMap.
 */
public class MapGraph
{
  private Map<Long, OSMNode> nodes;
  private Map<Long, Set<MapEdge>> edges;

  public MapGraph()
  {
    nodes = new HashMap<>();
    edges = new HashMap<>();
  }

  /**
   * Ermittelt, ob es eine Kante im Graphen zwischen zwei Knoten gibt.
   *
   * @param from der Startknoten
   * @param to der Zielknoten
   * @return 'true' falls es die Kante gibt, 'false' sonst
   */
  boolean hasEdge(OSMNode from, OSMNode to)
  {
    //TODO verify
    for (MapEdge edge : edges.get(from))
    {
      if (edge.getTo() == to.getId())
        return true;
    }
    return false;
  }

  /**
   * Diese Methode findet zu einem gegebenen Kartenpunkt den
   * nähesten OpenStreetMap-Knoten. Gibt es mehrere Knoten mit
   * dem gleichen kleinsten Abstand zu, so wird derjenige Knoten
   * von ihnen zurückgegeben, der die kleinste Id hat.
   *
   * @param p der Kartenpunkt
   * @return der OpenStreetMap-Knoten
   */
  public OSMNode closest(MapPoint p)
  {
    //TODO verify
    int closest = Integer.MAX_VALUE;
    OSMNode closeNode = null;
    for (Entry<Long, OSMNode> node : nodes.entrySet())
    {
      int dist = p.distance(node.getValue().getLocation());
      if (dist < closest)
      {
        closeNode = node.getValue();
        closest = dist;

      }
    }

    return closeNode;
  }

  /**
   * Diese Methode sucht zu zwei Kartenpunkten den kürzesten Weg durch
   * das Straßen/Weg-Netz von OpenStreetMap.
   *
   * @param from der Kartenpunkt, bei dem gestartet wird
   * @param to der Kartenpunkt, der das Ziel repräsentiert
   * @return eine mögliche Route zum Ziel und ihre Länge; die Länge
   * des Weges bezieht sich nur auf die Länge im Graphen, der Abstand
   * von 'from' zum Startknoten bzw. 'to' zum Endknoten wird
   * vernachlässigt.
   */
  public RoutingResult route(MapPoint from, MapPoint to)
  {
    /*
    BinomialHeap vermutet = new BinomialHeap();
    vermutet.add(closest(from));
    List<Eisscholle> tour = new ArrayList<>();

    //2
    while (vermutet.size() > 0)
    {
      //2a
      Eisscholle closest = vermutet.poll();
      addBekannt(closest);

      //2b
      setNachbar(closest, seewege);
      calculateDistance(closest, seewege);

      //2c
      while (nachbarschollen.size() > 0)
      {
        Eisscholle eisscholle = nachbarschollen.poll();
        if(eisscholle.getState() != Eisscholle.BEKANNT)
          vermutet.add(eisscholle);
      }
    }

    Eisscholle walk = eisschollen[endIndex];
    while (walk != eisschollen[startIndex])
    {
      tour.add(walk);
      walk = walk.getVorgaenger();
    }
    tour.add(walk);
    List<Eisscholle> finalTour = new ArrayList<>();
    for (int i = tour.size() - 1; i >= 0; i--)
    {
      finalTour.add(tour.get(i));
    }
    return finalTour;
    */
    return null;
  }

  public Map<Long, OSMNode> getNodes()
  {
    return nodes;
  }

  public void addNode(OSMNode node)
  {
    this.nodes.put(node.getId(), node);
  }

  public void setNodes(Map<Long, OSMNode> nodes)
  {
    this.nodes = nodes;
  }

  public Map<Long, Set<MapEdge>> getEdges()
  {
    return edges;
  }

  public void addEdge(long key, MapEdge edge)
  {
    Set<MapEdge> edgeSet = this.edges.get(key);
    if (edgeSet == null)
      edgeSet = new HashSet<>();
    edgeSet.add(edge);
    this.edges.put(key, edgeSet);
  }
}
