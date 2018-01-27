package paralleluine;

public class Monitor
{
  private boolean isLocked;

  public synchronized boolean peek()
  {
    return isLocked;
  }

  public synchronized boolean check()
  {
    //Returns false if it is already locked
    if(isLocked)
      return false;
    isLocked = true;
    return true;
  }

  public synchronized void reset()
  {
    isLocked = false;
  }
}
