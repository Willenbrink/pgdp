package dijkstra_angabe;
public class Eisscholle
{

  public final static int UNBEKANNT = 0;
  public final static int VERMUTET = 1;
  public final static int BEKANNT = 2;


  private int distance;
  private Eisscholle vorgaenger;

  private final String name;
  private int state;

  public Eisscholle(String name)
  {
    this.name = name;
    distance = Integer.MAX_VALUE;
    vorgaenger = null;
    state = UNBEKANNT;
  }

  public Eisscholle getVorgaenger()
  {
    return vorgaenger;
  }

  public void setVorgaenger(Eisscholle vorgaenger)
  {
    this.vorgaenger = vorgaenger;
  }

  public int getState()
  {
    return state;
  }

  public void setState(int state)
  {
    this.state = state;
  }

  public int getDistance()
  {
    return distance;
  }

  public void setDistance(int distance)
  {
    if (distance < 0)
      this.distance = Integer.MAX_VALUE;
    this.distance = distance;
  }

  public String getName()
  {
    return name;
  }

  @Override
  public boolean equals(Object o)
  {
    //o.toString ist nicht nützlich da toString nicht nur den Namen sondern auch die Distanz zurückgibt
    //Nur wenn das Objekt eine Eisscholle ist, besteht überhaupt die Möglichkeit, dass sie gleich sind
    if(o instanceof Eisscholle)
      return ((Eisscholle) o).getName().equals(this.name);
    return false;
  }

  @Override
  public String toString()
  {
    //Ich hoffe die Methode ist jetzt "sinnvoll" genug, eigentlich reicht ja name + distance
    return name + " | " + distance + " | " + getState();
  }
}
//UTF-8 Encoded ä
