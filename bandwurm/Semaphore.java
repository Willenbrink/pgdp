package bandwurm;

public class Semaphore
{
  private int free, cap;

  public Semaphore(int cap)
  {
    this.cap = cap;
    this.free = cap;
  }

  public synchronized void get() throws InterruptedException
  {
    while(free == cap)
      wait();
    free++;
    notifyAll();
  }

  public synchronized void add() throws InterruptedException
  {
    while(free == 0)
      wait();
    free--;
    notifyAll();
  }

  public int getAmount()
  {
    return cap-free;
  }
}
