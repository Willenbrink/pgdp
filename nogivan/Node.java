package nogivan;

public class Node implements Comparable<Node>
{
  OSMNode part;
  boolean vermutet;

  public Node(OSMNode osmNode)
  {
    part = osmNode;
  }

  private int distance = Integer.MAX_VALUE;

  public int getDistance()
  {
    return distance;
  }

  public void setDistance(int distance)
  {
    this.distance = distance;
  }

  private Node vorgänger;

  public Node getVorgänger()
  {
    return vorgänger;
  }

  public void setVorgänger(Node vorgänger)
  {
    this.vorgänger = vorgänger;
  }

  public String tour()
  {
    if(vorgänger == null)
      return getId() + "";
    return getId() + " | " + vorgänger.tour();
  }

  public long getId()
  {
    return part.getId();
  }

  public MapPoint getLocation()
  {
    return part.getLocation();
  }

  public OSMNode getPart()
  {
    return part;
  }

  @Override
  public String toString()
  {
    if(vorgänger == null)
      return "Node {id = " + getId() + ", " + part.location + "}";
    return "Node: " + getId() + " prev: " + vorgänger.getId() + " dist: " + distance;
  }

  @Override
  public int compareTo(Node o)
  {
    return distance - o.distance;
  }
}
