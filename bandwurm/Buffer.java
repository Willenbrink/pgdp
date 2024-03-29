package bandwurm;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer
{
  private Queue<Klausur> klausuren;
  private final Semaphore sema;


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
        return null;
      }
    }
  }

  public int getAmount()
  {
    return sema.getAmount();
  }

  public int getTraffic()
  {
    return sema.getTraffic();
  }

  public void addKlausur(Klausur klausur)
  {
    synchronized (sema)
    {
      try
      {
        sema.add();
        this.klausuren.add(klausur);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
        addKlausur(klausur);
      }
    }
  }
}
//UTF-8 ä
