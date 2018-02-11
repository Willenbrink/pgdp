package paralleluine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BreedingTest {
  Colony col;
  int simRuns = 0;
  int testRuns = 0;

  //TODO entfernen

  @Test
  void test() throws InterruptedException {
    boolean keepRunning = true;
    boolean foundMannuin = false;
    boolean foundFrauin = false;

    do {
      if (col != null) {
        col.stopSimulation();
        col = null;
        Thread.sleep(500); // wait for garbage collector?
      }

      simRuns++;
      System.out.println("SimRun " + simRuns);
      testRuns = 0;
      col = new Colony(24, 20, false);
      do {
        testRuns++;
        Thread.sleep(5000);
        keepRunning = false;
        foundMannuin = false;
        foundFrauin = false;

        loop: for (int i = 0; i < col.getPlaced().length; i++) {
          for (int j = 0; j < col.getPlaced()[i].length; j++) {
            if (col.getPlaced()[i][j] != null) {
              if (col.getPlaced()[i][j].getFg() == GUI.MANNUIN
                  || col.getPlaced()[i][j].getFg() == GUI.KLEINUIN) {
                foundMannuin = true;
              } else if (col.getPlaced()[i][j].getFg() == GUI.FRAUIN
                  || col.getPlaced()[i][j].getFg() == GUI.KLEINUININ) {
                foundFrauin = true;
              }
              keepRunning = foundMannuin && foundFrauin;
              if (keepRunning) {
                break loop;
              }
            }
          }
        }
      } while (keepRunning && testRuns < 6); // max 30 sek. per testRun
      // simulation ended

    } while (!col.kids);
    // got lucky

    System.out.println("Got lucky after " + simRuns + " Simulation-Runs!");
    assertTrue(true);
  }

}