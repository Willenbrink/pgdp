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
  private Map<Long, HeapElement> elementMap;
  private Map<Long, Set<MapEdge>> edges;
  private BinomialHeap<HeapElement> vermutet = new BinomialHeap<>();
  private Map<Long, BinomialHeapHandle> handles = new HashMap<>();

  private final boolean dijkstra = true;

  public MapGraph()
  {
    nodes = new HashMap<>();
    elementMap = new HashMap<>();
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
    int closest = Integer.MAX_VALUE;
    long id = 0;
    OSMNode closeNode = null;
    for (Entry<Long, OSMNode> node : nodes.entrySet())
    {
      int dist = p.distance(node.getValue().getLocation());
      if (dist < closest)
      {
        closeNode = node.getValue();
        closest = dist;
        id = node.getKey();
      }
      //Sollte eigentlich nicht notwendig sein aber man kann Mengen ja nicht vertrauen
      if (dist <= closest && node.getKey() < id)
      {
        closeNode = node.getValue();
        closest = dist;
        id = node.getKey();
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
    //Initialisation
    Set<HeapElement> bekannt = new HashSet<>();

    for(Entry<Long, OSMNode> node : nodes.entrySet())
      elementMap.put(node.getKey(), new HeapElement(node.getValue()));

    HeapElement start = elementMap.get(closest(from).getId());
    HeapElement target = elementMap.get(closest(to).getId());

    //Hier wird entweder mit dem Dijkstra- oder A*-Algorithmus gerechnet
    //A* liefert dabei deutlich schneller Ergebnisse, garantiert aber nicht mehr eine perfekte
    //Lösung deswegen ist es abhängig von der Anwendung was man benutzt
    //
    if(dijkstra)
      start.setDistance(0);
    else
      start.setDistance(start.getLocation().distance(target.getLocation()));
    vermute(start);

    //2
    while (vermutet.getSize() > 0)
    {
      //2a
      HeapElement next = vermutet.poll();
      bekannt.add(next);

      //2b
      long id = next.getId();
      edges.computeIfAbsent(id, d -> new HashSet<>());
      for (MapEdge edge : edges.get(id))
      {
        HeapElement neighbor = elementMap.get(edge.getTo());
        if (bekannt.contains(neighbor))
          continue;

        int newDistance;
        if(dijkstra)
          newDistance = next.getLocation().distance(neighbor.getLocation())+next.getDistance();
        else
        {
          newDistance =
              next.getLocation().distance(neighbor.getLocation())
                  + next.getDistance()
                  - next.getLocation().distance(target.getLocation())
                  + neighbor.getLocation().distance(target.getLocation());
        }
        int oldDistance = neighbor.getDistance();
        if(oldDistance > newDistance)
        {
          neighbor.setDistance(newDistance);
          neighbor.setVorgänger(next);
          //Kann man die Aufrufe noch weiter reduzieren? Sie müssen ja ersetzt werden
          //wenn die Distanz kleiner ist und sie schon enthalten sind
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
        if(bekannt.contains(elementMap.get(edge.getTo())))
          continue;
        HeapElement heapElement = elementMap.get(edge.getTo());
        if(!heapElement.vermutet)
          vermute(heapElement);
      }
    }

    List<HeapElement> result = new ArrayList<>();

    HeapElement walk = target;
    while(walk.compareTo(start) != 0)
    {
      result.add(walk);
      walk = walk.getVorgänger();
      if(walk == null)
        return null;
    }
    result.add(walk);

    OSMNode[] tour = new OSMNode[result.size()];

    for (int i = tour.length - 1; i >= 0; i--)
    {
      tour[tour.length-i-1] = result.get(i).getPart();
    }
    System.out.println(target.getDistance());
    return new RoutingResult(tour, target.getDistance());
  }

  private void vermute(HeapElement heapElement)
  {
    handles.put(heapElement.getId(), (BinomialHeapHandle) vermutet.insert(heapElement));
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
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
