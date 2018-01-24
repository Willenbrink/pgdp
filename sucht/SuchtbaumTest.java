package sucht;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;
import org.junit.Test;

public class SuchtbaumTest
{
  private Random random = new Random();
  private long startTime = System.currentTimeMillis();
  private int count;

  private class Runner implements Runnable
  {
    Suchtbaum baumi;

    //States are:
    //0: reading
    //1: adding
    //2: removing
    int state;
    int delay;

    private Runner(Suchtbaum suchti, int state, int ms)
    {
      baumi = suchti;
      this.state = state;
      delay = ms;
    }

    @Override
    public void run()
    {
      while (true)
      {
        try
        {
          int rand;
          count++;
          if (state <= 5)
          {
            String result = baumi.toString();
            //Kommentiert wegen extremen Ausgabeumfang
            log("Treepart: " + result.split("\n")[(int) (Math.random() * result
                .split("\n").length)]);
          }
          else if (state <= 80)
          {
            rand = (int) (Math.random() * 1000);
            if (baumi.contains(rand))
              log(rand + " contained");
          }
          else if (state <= 98)
          {
            rand = (int) (Math.random() * 1000);
            baumi.insert(rand);
            log("-> " + rand);
          }
          else if (state <= 100)
          {
            rand = (int) (Math.random() * 1000);
            if (baumi.contains(rand))
              log("<- " + rand);
            baumi.remove(rand);
          }
          Thread.sleep(delay);
          state = (int) (Math.random() * 100);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
          //break;
        }
      }
    }
  }

  private void log(String input)
  {
    System.out.print(".");
    //System.out.print("Time " + (System.currentTimeMillis() - startTime) + ": ");
    //System.out.println(input);
  }

  @Test
  public void testThreaded() throws InterruptedException
  {
    int n = 100;
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for (int i = 0; i < n; i++)
    {
      int delay = (int) (Math.random() * 1000);
      delay = 1000;
      Runner runner = new Runner(suchti, i, delay);
      new Thread(runner).start();
    }
    while (true)
    {
      System.out.println(count + " active");
      count = 0;
      Thread.sleep(1000);
    }
  }

  @Test
  public void testContains() throws InterruptedException
  {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 10000;
    for (int i = 0; i < n; i++)
    {
      testSet.add(random.nextInt(20 * n));
    }
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for (Integer i : testSet)
    {
      suchti.insert(i);
    }
    for (int i = 0; i < n; i++)
    {
      assertEquals(testSet.contains(i), suchti.contains(i));
    }
  }

  @Test
  public void testContainsSmall() throws InterruptedException
  {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 500;
    for (int i = 0; i < n; i++)
    {
      testSet.add(random.nextInt(20 * n));
    }
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for (Integer i : testSet)
    {
      suchti.insert(i);
    }
    for (int i = 0; i < n; i++)
    {
      assertEquals(testSet.contains(i), suchti.contains(i));
    }
  }

  @Test
  public void testContainsRemove() throws InterruptedException
  {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 10000;
    for (int i = 0; i < n; i++)
    {
      testSet.add(random.nextInt(20 * n));
    }
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for (Integer i : testSet)
    {
      suchti.insert(i);
    }
    for (int i = 0; i < n; i++)
    {
      int next = random.nextInt(20 * n);
      if (testSet.contains(next))
      {
        suchti.remove(next);
        testSet.remove(next);
      }
    }
    for (int i = 0; i < n; i++)
    {
      assertEquals(testSet.contains(i), suchti.contains(i));
    }
  }

  @Test
  public void testContainsRemoveSmall() throws InterruptedException
  {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 100;
    for (int i = 0; i < n; i++)
    {
      testSet.add(random.nextInt(20 * n));
    }
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for (Integer i : testSet)
    {
      suchti.insert(i);
    }
    for (int i = 0; i < n; i++)
    {
      int next = random.nextInt(20 * n);
      if (testSet.contains(next))
      {
        suchti.remove(next);
        testSet.remove(next);
      }
    }
    for (int i = 0; i < n; i++)
    {
      assertEquals(testSet.contains(i), suchti.contains(i));
    }
  }

}
