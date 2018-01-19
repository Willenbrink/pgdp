package nogivan;

import java.util.ArrayList;
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
  private Map<Long, Node> improvedNodes;
  private Map<Long, Set<MapEdge>> edges;

  public MapGraph()
  {
    nodes = new HashMap<>();
    improvedNodes = new HashMap<>();
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
    for (MapEdge edge : edges.get(from.getId()))
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

  //TODO move
  private BinomialHeap<Node> vermutet = new BinomialHeap<>();
  private Map<Long, BinomialHeapHandle> handles = new HashMap<>();

  public RoutingResult route(MapPoint from, MapPoint to)
  {
    //Initialisation
    List<Node> bekannt = new ArrayList<>();

    for(Entry<Long, OSMNode> node : nodes.entrySet())
    {
      improvedNodes.put(node.getKey(), new Node(node.getValue()));
    }

    Node start = improvedNodes.get(closest(from).getId());
    Node target = improvedNodes.get(closest(to).getId());

    start.setDistance(0);
    vermute(start);

    //2
    do
    {
      //2a
      Node next = vermutet.poll();
      bekannt.add(next);

      //2b
      long id = next.getId();
      edges.computeIfAbsent(id, d -> new HashSet<>());
      for (MapEdge edge : edges.get(id))
      {
        Node neighbor = improvedNodes.get(edge.getTo());
        if (bekannt.contains(next.getId()))
          continue;

        int newDistance = next.getLocation().distance(neighbor.getLocation())+next.getDistance();
        int oldDistance = neighbor.getDistance();
        if(oldDistance > newDistance)
        {
          neighbor.setDistance(newDistance);
          neighbor.setVorgänger(next);
          if(neighbor.vermutet)
            vermutet.replaceWithSmallerElement(handles.get(neighbor.getId()), neighbor);
          else
          {
            neighbor.vermutet = true;
            vermute(neighbor);
          }
        }
      }

      //2c
      for(MapEdge edge : edges.get(next.getId()))
      {
        if(bekannt.contains(edge.getTo()))
          continue;
        Node node = improvedNodes.get(edge.getTo());
        if(!node.vermutet)
          vermute(node);
      }
    }while (vermutet.getSize() > 0);

    List<Node> result = new ArrayList<>();

    Node walk = target;
    Node prev = walk;
    while(walk.compareTo(start) != 0)
    {
      result.add(walk);
      prev = walk;
      walk = walk.getVorgänger();
      prev = prev;
    }
    result.add(walk);

    OSMNode[] tour = new OSMNode[result.size()];

    for (int i = tour.length - 1; i >= 0; i--)
    {
      tour[tour.length-i-1] = result.get(i).getPart();
    }
    return new RoutingResult(tour, target.getDistance());
  }

  private void vermute(Node node)
  {
    handles.put(node.getId(), (BinomialHeapHandle) vermutet.insert(node));
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
