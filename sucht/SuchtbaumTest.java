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
    int delay;

    private Runner(Suchtbaum suchti, int ms)
    {
      baumi = suchti;
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
          int state = (int) (Math.random() * 100);
          if (state <= 20)
          {
            String result = baumi.toString();
            log("read");
          }
          else if (state <= 60)
          {
            rand = (int) (Math.random() * 1000);
            baumi.contains(rand);
            log("read");
          }
          else if (state <= 80)
          {
            rand = (int) (Math.random() * 1000);
            baumi.insert(rand);
            log("write ->");
          }
          else if (state <= 100)
          {
            rand = (int) (Math.random() * 1000);
            baumi.remove(rand);
            log("write <-");
          }
          Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  private void log(String input)
  {
    System.out.print("Time " + (System.currentTimeMillis() - startTime) + ": ");
    System.out.println(input);
  }

  //@Test
  public void testThreaded() throws InterruptedException
  {
    //Amount of threads
    int n = 100;
    int sleepDuration = 10000;
    Suchtbaum<Integer> suchti = new Suchtbaum<>();

    for (int i = 0; i < n; i++)
    {
      int delay = (int) (Math.random() * sleepDuration);
      Runner runner = new Runner(suchti, delay);
      new Thread(runner).start();
    }
    while (true)
    {
      System.out.println(count + " active in the last " + sleepDuration + "ms");
      count = 0;
      Thread.sleep(sleepDuration);
    }
  }

  @Test
  public void testDoubleValue() throws InterruptedException
  {
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    suchti.insert(10);
    suchti.insert(10);
    suchti.insert(10);
    suchti.remove(10);
    assertEquals(false, suchti.contains(10));
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
