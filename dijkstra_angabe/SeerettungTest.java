package dijkstra_angabe;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SeerettungTest {
  Eisscholle[] eisschollen;
  List<Seeweg> seewege;

  @Before
  public void setUp() throws Exception {
    eisschollen = new Eisscholle[6];
    seewege = new LinkedList<>();

    for (char c = 'A'; c <= 'F'; c++) {
      eisschollen[c - 'A'] = new Eisscholle("" + c);
    }
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void findeWegBeispiel() throws Exception {
    seewege.add(new Seeweg(10, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(12, eisschollen[1], eisschollen[3]));
    seewege.add(new Seeweg(15, eisschollen[0], eisschollen[2]));
    seewege.add(new Seeweg(10, eisschollen[2], eisschollen[4]));
    seewege.add(new Seeweg(1, eisschollen[3], eisschollen[5]));
    seewege.add(new Seeweg(5, eisschollen[5], eisschollen[4]));
    seewege.add(new Seeweg(2, eisschollen[3], eisschollen[4]));
    seewege.add(new Seeweg(15, eisschollen[1], eisschollen[5]));

    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 4);
    assertEquals(4, schollen.size());
    assertEquals(eisschollen[0], schollen.get(0));
    assertEquals(eisschollen[1], schollen.get(1));
    assertEquals(eisschollen[3], schollen.get(2));
    assertEquals(eisschollen[4], schollen.get(3));
  }

  @Test
  public void findeWegBeispiel0() throws Exception {
    seewege.add(new Seeweg(10, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(12, eisschollen[1], eisschollen[3]));
    seewege.add(new Seeweg(3, eisschollen[0], eisschollen[2]));
    seewege.add(new Seeweg(3, eisschollen[2], eisschollen[4]));
    seewege.add(new Seeweg(1, eisschollen[3], eisschollen[5]));
    seewege.add(new Seeweg(5, eisschollen[5], eisschollen[4]));
    seewege.add(new Seeweg(2, eisschollen[3], eisschollen[4]));
    seewege.add(new Seeweg(15, eisschollen[1], eisschollen[5]));

    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 4);
    System.out.println("Ergebnis:\n" + schollen); //kann auskommentiert werden
    assertEquals(3, schollen.size());
    assertEquals(eisschollen[0], schollen.get(0));
    assertEquals(eisschollen[2], schollen.get(1));
    assertEquals(eisschollen[4], schollen.get(2));
  }

  @Test
  public void findeWegBeispiel1() throws Exception {
    seewege.add(new Seeweg(1, eisschollen[0], eisschollen[1]));
    seewege.add(new Seeweg(100, eisschollen[0], eisschollen[2]));
    seewege.add(new Seeweg(1, eisschollen[1], eisschollen[2]));
    seewege.add(new Seeweg(50, eisschollen[1], eisschollen[3]));
    seewege.add(new Seeweg(1, eisschollen[2], eisschollen[3]));

    List<Eisscholle> schollen = Seerettung.findeWeg(eisschollen, seewege, 0, 3);
    System.out.println("Ergebnis:\n"+schollen);
    assertEquals(4, schollen.size());
    assertEquals(eisschollen[0], schollen.get(0));
    assertEquals(eisschollen[1], schollen.get(1));
    assertEquals(eisschollen[2], schollen.get(2));
    assertEquals(eisschollen[3], schollen.get(3));
  }

  @Test
  public void findeWegLandkarteWikipedia() throws Exception {
    Eisscholle muenchen = new Eisscholle("München");
    Eisscholle augsburg = new Eisscholle("Augsburg");
    Eisscholle karlsruhe = new Eisscholle("Karlsruhe");
    Eisscholle mannheim = new Eisscholle("Mannheim");
    Eisscholle frankfurt = new Eisscholle("Frankfurt");
    Eisscholle wuerzburg = new Eisscholle("Würzburg");
    Eisscholle erfurt = new Eisscholle("Erfurt");
    Eisscholle nuernberg = new Eisscholle("Nürnberg");
    Eisscholle stuttgart = new Eisscholle("Stuttgart");
    Eisscholle kassel = new Eisscholle("Kassel");

    List<Seeweg> seewege = new LinkedList<>();
    addNonDirectionalEdge(84, muenchen, augsburg, seewege);
    addNonDirectionalEdge(250, karlsruhe, augsburg, seewege);
    addNonDirectionalEdge(80, karlsruhe, mannheim, seewege);
    addNonDirectionalEdge(85, mannheim, frankfurt, seewege);
    addNonDirectionalEdge(217, frankfurt, wuerzburg, seewege);
    addNonDirectionalEdge(186, wuerzburg, erfurt, seewege);
    addNonDirectionalEdge(103, wuerzburg, nuernberg, seewege);
    addNonDirectionalEdge(183, nuernberg, stuttgart, seewege);
    addNonDirectionalEdge(167, nuernberg, muenchen, seewege);
    addNonDirectionalEdge(502, muenchen, kassel, seewege);
    addNonDirectionalEdge(173, kassel, frankfurt, seewege);

    Eisscholle[] cities = {muenchen, augsburg, karlsruhe, mannheim, frankfurt, wuerzburg, erfurt, nuernberg, stuttgart, kassel};

    List<Eisscholle> path;

    // muenchen to frankfurt
    /*
    path = Seerettung.findeWeg(cities.clone(), new LinkedList<Seeweg>(seewege), 4, 0);
    assertEquals(4, path.size());
    assertEquals(frankfurt, path.get(0));
    assertEquals(wuerzburg, path.get(1));
    assertEquals(nuernberg, path.get(2));
    assertEquals(muenchen, path.get(3));
    */

    // ausgburg to erfurt
    path = Seerettung.findeWeg(cities.clone(), new LinkedList<Seeweg>(seewege), 1, 6); // shortest path: augsburg - muenchen - nuernberg - wuerzburg - erfurt
    assertEquals(5, path.size());
    assertEquals(augsburg, path.get(0));
    assertEquals(muenchen, path.get(1));
    assertEquals(nuernberg, path.get(2));
    assertEquals(wuerzburg, path.get(3));
    assertEquals(erfurt, path.get(4));
  }

  private void addNonDirectionalEdge(int dist, Eisscholle a, Eisscholle b, List<Seeweg> seewege) {
    seewege.add(new Seeweg(dist, a, b));
    seewege.add(new Seeweg(dist, b, a));
  }
}//UTF-8 Encoded ä
