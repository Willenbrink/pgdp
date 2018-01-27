package sucht;

//TODO remove dis

import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Random;
import org.junit.Test;

public class SuchtbaumTestMultiThread extends Thread {

  private class SuchtbaumTestContains extends Thread {
    private Random random = new Random();
    private Suchtbaum<Integer> tree;
    private int rangeStart = 0;
    private int rangeEnd = 0;
    private String result = "OK";

    public SuchtbaumTestContains(Suchtbaum tree, int rangeStart, int rangeEnd) {
      this.tree = tree;
      this.rangeStart = rangeStart;
      this.rangeEnd = rangeEnd;
    }

    @Override
    public void run() {
      try {
        System.out.println("Thread " + rangeStart + "-" + rangeEnd + ": I am alive! :D");

        // !!! Code used from SuchtbaumTest !!!
        HashSet<Integer> testSet = new HashSet<>();
        int n = 10000;
        for (int i = 0; i < n; i++)
          // testSet.add(random.nextInt(20 * n)); // orig
          testSet.add((random.nextInt(20 * n) % (rangeEnd - rangeStart)) + rangeStart); // changed
        // to
        // stay
        // within
        // given
        // range
        for (Integer i : testSet)
          tree.insert(i);
        for (int i = rangeStart; i < rangeEnd; i++) // changed to check range
          if (!(testSet.contains(i) == tree.contains(i))) {
            result = "Error: Element mismatch ;(";
          }
      } catch (InterruptedException e) {
        result = "I Got Interupted! >:c";
      }
    }

    public String getResult() {
      return result;
    }

    public int getRangeStart() {
      return rangeStart;
    }

    public int getRangeEnd() {
      return rangeEnd;
    }
  }


  private class SuchtbaumTestContainsRemove extends Thread {
    private Random random = new Random();
    private Suchtbaum<Integer> tree;
    private String result = "OK";

    public SuchtbaumTestContainsRemove(Suchtbaum tree) {
      this.tree = tree;
    }

    @Override
    public void run() {
      try {
        System.out.println("Thread " + this.getName() + ": I am alive! :D");

        // !!! Code used from SuchtbaumTest !!!
        HashSet<Integer> testSet = new HashSet<>();
        int n = 10;
        for (int i = 0; i < n; i++)
          testSet.add(random.nextInt(20 * n));
        for (Integer i : testSet)
          tree.insert(i);
        for (int i = 0; i < n; i++) {
          int next = random.nextInt(20 * n);
          if (testSet.contains(next)) {
            tree.remove(next);
            testSet.remove(next);
          }
        }
        for (Integer i : testSet) {
          if (!tree.contains(i)) { // all my elements should be there!
            result = "Error: My element was deleted by someone else ;(";
            return;
          }
          tree.remove(i); // if removed now, the tree should be empty afterwards
        }
      } catch (InterruptedException e) {
        result = "I Got Interupted! >:c";
      }
    }

    public String getResult() {
      return result;
    }
  }

  @Test
  public void testContainsMultiThreaded() throws InterruptedException {
    // Create a bunch of threads and give each a interval to work in.
    // Then let them add and read everything and check if they run correctly.

    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    SuchtbaumTestContains[] threads = new SuchtbaumTestContains[50]; // <<<<<<<<<< THREAD COUNT IS
    // HERE
    int rangeSize = 10000;
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new SuchtbaumTestContains(suchti, i * rangeSize, i * rangeSize + rangeSize - 1);
      sleep(100); // delay threads
      threads[i].start();
    }

    for (SuchtbaumTestContains thread : threads) {
      thread.join();
    }

    // Done testing!
    int okCount = 0;
    String res;
    for (SuchtbaumTestContains thread : threads) {
      res = thread.getResult();
      if (res.equals("OK")) {
        okCount++;
      } else {
        System.out
            .println("Thread " + thread.getRangeStart() + "-" + thread.getRangeEnd() + ": " + res);
      }
    }
    System.out.println("A whole " + okCount + " Threads did fine! :D");

    assertEquals("Successful Threads", threads.length, okCount);
  }

  @Test
  public void testContainsRemoveMultiThreaded() throws InterruptedException {
    // Create a bunch of threads.
    // They generate a few random numbers, add them and then remove a few of them randomly.
    // Then check if they ran correctly.
    // !! duplicate inserts are possible !!

    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    SuchtbaumTestContainsRemove[] threads = new SuchtbaumTestContainsRemove[50]; // <<<<<<<<<<
    // THREAD COUNT
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new SuchtbaumTestContainsRemove(suchti);
      threads[i].setName("#" + i);
      sleep(100); // delay thread starts
      threads[i].start();
    }

    for (SuchtbaumTestContainsRemove thread : threads) {
      thread.join();
    }

    // Done testing!
    int okCount = 0;
    String res;
    for (SuchtbaumTestContainsRemove thread : threads) {
      res = thread.getResult();
      if (res.equals("OK")) {
        okCount++;
      } else {
        System.out.println("Thread " + thread.getName() + ": " + res);
      }
    }
    System.out.println("A whole " + okCount + " Threads did fine! :D");

    assertEquals("Successful Threads", threads.length, okCount); // all Threads did good
    assertEquals("digraph G {\n}", suchti.toString()); // Tree is empty now
  }
}