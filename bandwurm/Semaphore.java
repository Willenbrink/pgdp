package bandwurm;

public class Semaphore
{
  private int free, cap;
  private int traffic;

  public Semaphore(int cap)
  {
    this.cap = cap;
    this.free = cap;
  }

  public synchronized void get() throws InterruptedException
  {
    while(free == cap)
    {
      if(traffic == Klausurkorrektur.getAmountStudents() && free-cap == 0)
        return;
      wait();
    }
    free++;
    notifyAll();
  }

  public synchronized void add() throws InterruptedException
  {
    while(free == 0)
      wait();
    free--;
    traffic++;
    notifyAll();
  }

  public int getTraffic()
  {
    return traffic;
  }

  public int getAmount()
  {
    return cap-free;
  }
}
