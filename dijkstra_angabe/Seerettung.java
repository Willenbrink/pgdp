package dijkstra_angabe;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Seerettung
{
  private static PriorityQueue<Eisscholle> nachbarschollen;
  private static PriorityQueue<Eisscholle> bekannt;
  private static PriorityQueue<Eisscholle> vermutet;

  public static List<Eisscholle> findeWeg
      (Eisscholle[] eisschollen,
          List<Seeweg> seewege,
          int startIndex,
          int endIndex)
  {
    //1
    init(eisschollen, startIndex);
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
  }

  private static void init(Eisscholle[] eisschollen, int startIndex)
  {
    nachbarschollen = new PriorityQueue<>(new EisschollenComparator());
    bekannt = new PriorityQueue<>(new EisschollenComparator());
    vermutet = new PriorityQueue<>(new EisschollenComparator());
    eisschollen[startIndex].setDistance(0);
    addVermutet(eisschollen[startIndex]);
  }

  private static void setNachbar(Eisscholle e, List<Seeweg> seewege)
  {
    if (nachbarschollen.size() > 0)
      System.err.println("Nachbarschollen noch nicht leer");
    for (Seeweg seeweg : seewege)
    {
      if (seeweg.getFrom().equals(e))
        nachbarschollen.add(seeweg.getTo());
    }
  }

  private static void calculateDistance(Eisscholle e, List<Seeweg> seewege)
  {
    for (Eisscholle nachbar : nachbarschollen)
    {
      for (Seeweg s : seewege)
      {
        if (s.getFrom().equals(e) && s.getTo().equals(nachbar))
          if (nachbar.getDistance() > e.getDistance() + s.getDistance())
          {
            nachbar.setVorgaenger(e);
            nachbar.setDistance(e.getDistance() + s.getDistance());
          }
      }
    }
  }

  private static void addBekannt(Eisscholle eisscholle)
  {
    eisscholle.setState(eisscholle.BEKANNT);
    bekannt.add(eisscholle);
  }

  private static void addVermutet(Eisscholle eisscholle)
  {
    eisscholle.setState(eisscholle.VERMUTET);
    vermutet.add(eisscholle);
  }
}
//UTF-8 Encoded ä
