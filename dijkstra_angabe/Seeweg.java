package dijkstra_angabe;
//ä
public class Seeweg {
  private int distance;
  private Eisscholle from;
  private Eisscholle to;

  public int getDistance()
  {
    return distance;
  }

  public void setDistance(int distance)
  {
    this.distance = distance;
  }

  public void setFrom(Eisscholle from)
  {
    this.from = from;
  }

  public Eisscholle getTo()
  {
    return to;
  }

  public void setTo(Eisscholle to)
  {
    this.to = to;
  }

  public Seeweg(int distance, Eisscholle from, Eisscholle to)
  {
    this.distance = distance;
    this.from = from;
    this.to = to;

  }

  @Override
  public String toString()
  {
    return from + "->" + to + " | Path: " + distance;
  }


  public Eisscholle getFrom() {
    return from;
  }
}
//UTF-8 Encoded ä
