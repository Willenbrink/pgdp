package nogivan;

public class HeapElement implements Comparable<HeapElement>
{
  OSMNode part;
  boolean vermutet;

  public HeapElement(OSMNode osmNode)
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

  private HeapElement vorgänger;

  public HeapElement getVorgänger()
  {
    return vorgänger;
  }

  public void setVorgänger(HeapElement vorgänger)
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
  public int compareTo(HeapElement o)
  {
    return distance - o.distance;
  }
}
