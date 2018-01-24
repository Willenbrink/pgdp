package sucht;

public class RWLock
{
  //Äquivalent zur Vorlesung, countReader < 0 ist Write
  //count = 0 ist frei und count > 0 ist Read mit count = Anzahl Leser
  private int countReader;

  public synchronized void startRead() throws InterruptedException
  {
    while(countReader < 0)
      wait();
    countReader++;
  }

  public synchronized void endRead()
  {
    countReader--;
    if(countReader == 0)
      notifyAll();
  }

  public synchronized void startWrite() throws InterruptedException
  {
    while(countReader != 0)
      wait();
    countReader = -1;
  }

  public synchronized void endWrite()
  {
    countReader = 0;
    notifyAll();
  }
}
