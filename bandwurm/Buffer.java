package bandwurm;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer
{
  private Queue<Klausur> klausuren;
  private final Semaphore sema;
  private int traffic;


  public Buffer (int amount)
  {
    klausuren = new LinkedList<>();
    sema = new Semaphore(amount);
  }

  public Klausur getKlausur()
  {
    synchronized (sema)
    {
      try
      {
        sema.get();
        return klausuren.poll();
      } catch (InterruptedException e)
      {
        e.printStackTrace();
        return getKlausur();
      }
    }
  }

  public int getAmount()
  {
    return sema.getAmount();
  }

  public int getTraffic()
  {
    return traffic;
  }

  public void addKlausur(Klausur klausur)
  {
    synchronized (sema)
    {
      try
      {
        sema.add();
        this.klausuren.add(klausur);
        traffic++;
      } catch (InterruptedException e)
      {
        e.printStackTrace();
        addKlausur(klausur);
      }
    }
  }
}
