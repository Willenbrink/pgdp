package sucht;

public class Suchtbaum<T extends Comparable<T>>
{
  private class SuchtbaumElement
  {
    //SetElement nicht möglich, da der Baum dann nicht mehr sortiert wäre
    private T element;
    private SuchtbaumElement left = null;
    private SuchtbaumElement right = null;

    public SuchtbaumElement(T element)
    {
      this.element = element;
    }

    public T getElement()
    {
      return element;
    }

    public SuchtbaumElement getLeft()
    {
      return left;
    }

    public void setLeft(SuchtbaumElement left)
    {
      this.left = left;
    }

    public SuchtbaumElement getRight()
    {
      return right;
    }

    public void setRight(SuchtbaumElement right)
    {
      this.right = right;
    }

    @Override
    public String toString()
    {
      String result = element.toString() + ";\n";
      if (left != null)
      {
        result += element.toString()
            + " -> "
            + left.getElement().toString()
            + " [label=left];\n";
        result += left.toString();
      }

      if (right != null)
      {
        result += element.toString()
            + " -> "
            + right.getElement().toString()
            + " [label=right];\n";
        result += right.toString();
      }
      return result;
    }
  }

  private SuchtbaumElement root;
  private RWLock lock;
  //Whether there should be a delay during any synchronized Operation
  //The delay helps by increasing the duration of an access
  private final boolean delay;

  public Suchtbaum()
  {
    root = null;
    lock = new RWLock();
    delay = false;
  }

  public Suchtbaum(boolean delayed)
  {
    root = null;
    lock = new RWLock();
    delay = delayed;
  }

  /*
  Wrappermethoden um die Synchronisierung von der eigentlich Implementation
  zu trennen, wegen der Vorgabe wurden die Wrappermethoden nicht umbenannt,
  die gewrappten Methoden aber schon

  Synchronisationsblock
   */
  public boolean contains(T element) throws InterruptedException
  {
    lock.startRead();
    if (delay)
      Thread.sleep(50);
    boolean result = containsWrapped(element);
    lock.endRead();
    return result;
  }

  public void insert(T element) throws InterruptedException
  {
    lock.startWrite();
    try
    {
      if (delay)
        Thread.sleep(50);
      insertWrapped(element);
    } catch (RuntimeException e)
    {
      e.printStackTrace();
    } finally
    {
      lock.endWrite();
    }
  }

  public void remove(T element) throws InterruptedException
  {
    lock.startWrite();
    try
    {
      if (delay)
        Thread.sleep(50);
      removeWrapped(element);
    } catch (RuntimeException e)
    {
      e.printStackTrace();
    } finally
    {
      lock.endWrite();
    }
  }

  private void insertWrapped(T element)
  {
    if (root == null)
    {
      root = new SuchtbaumElement(element);
      return;
    }

    //Sehr seltsame Dinge passieren wenn man hier im Debugmode durchstepped, dabei hängt sich die VM auf
    // und terminiert nicht mehr, das Problem scheint irgendwo bei der Exception zu liegen
    // es am Stück laufen zu lassen produziert aber keinen Fehler
    // Seltsamerweise sind das nur Operationen während der Thread noch das Lock hat und nicht weggibt
    //Leider habe ich dafür keine Lösung gefunden
    //Beschreibung:
    // 1. Breakpoint bei throwException = ...; setzen
    // 1.1 Der Breakpoint kann tatsächlich irgendwo innerhalb der nächsten 3 Zeilen
    //     gesetzt werden und es hängt sich trotzdem auf
    // 2. Debugmodus starten
    // 3. F7 um einen Schritt zu machen
    // 4. Terminiert nicht, beenden der VM funktioniert nicht auf Anhieb
    //    der Prozess muss gekillt werden
    boolean throwException = containsWrapped(element);
    if (throwException)
    {
      throw new RuntimeException("Element bereits enthalten");
    }

    //Es wird von root aus der Baum nach unten gewandert bis
    // wir auf einen Knoten treffen der keinen Nachfolger hat, an diesen wird
    // dann das Element angehängt
    // falls root == null wurde es bereits vorher abgefangen
    //Seltsamerweise, wenn man einen Breakpoint mit Bedingung root==null bei while(true) setzt
    // wird der Code ca. 50x langsamer (9 Sekunden vs 260 ms)
    SuchtbaumElement walk = root;
    while (true)
    {
      int comp = element.compareTo(walk.getElement());
      if (comp < 0)
      {
        if (walk.getLeft() == null)
        {
          walk.setLeft(new SuchtbaumElement(element));
          return;
        }
        else
          walk = walk.getLeft();
      }
      else //comp > 0, comp == 0 kann nicht sein
      {
        if (walk.getRight() == null)
        {
          walk.setRight(new SuchtbaumElement(element));
          return;
        }
        else
          walk = walk.getRight();
      }
    }
  }

  private boolean containsWrapped(T element)
  {
    if (root == null)
      return false;
    SuchtbaumElement walk = root;
    while (true)
    {
      //Selbes Konzept wie vorher, Baum entlangwandern bis man das richtige Element
      // findet oder am Ende ankommt
      int comp = element.compareTo(walk.getElement());
      if (comp == 0)
        return true;
      if (comp < 0)
      {
        if (walk.getLeft() == null)
          return false;
        walk = walk.getLeft();
      }
      else
      {
        if (walk.getRight() == null)
          return false;
        walk = walk.getRight();
      }
    }
  }

  private void removeWrapped(T element)
  {
    if (!containsWrapped(element))
      throw new RuntimeException("Element nicht enthalten");
    //Root kann nicht null sein, da ansonsten die Exception bereits geworfen
    //worden wäre
    SuchtbaumElement parent = null;
    boolean walkedLeft = true;
    SuchtbaumElement walk = root;
    while (true)
    {
      int comp = element.compareTo(walk.getElement());
      if (comp == 0)
      {
        boolean left = walk.getLeft() != null, right = walk.getRight() != null;

        //Keine Nachfolger
        if (!left && !right)
        {
          setParent(parent, walkedLeft, null);
        }

        //Zwei Nachfolger
        else if (left && right)
        {
          SuchtbaumElement largest = getLargest(walk.getLeft());
          removeWrapped(largest.getElement());
          largest.setLeft(walk.getLeft());
          largest.setRight(walk.getRight());
          setParent(parent, walkedLeft, largest);
        }

        //Ein Nachfolger
        //XOR der Nachfolger, einer von beiden nicht null
        else
        {
          if (walk.getLeft() != null)
          {
            setParent(parent, walkedLeft, walk.getLeft());
            walk.setLeft(null);
          }

          else if (walk.getRight() != null)
          {
            setParent(parent, walkedLeft, walk.getRight());
            walk.setRight(null);
          }
        }
        break;
      }
      else if (comp < 0)
      {
        parent = walk;
        walkedLeft = true;
        walk = walk.getLeft();
        continue;
      }
      else //comp > 0
      {
        parent = walk;
        walkedLeft = false;
        walk = walk.getRight();
        continue;
      }
    }
  }

  @Override
  public String toString()
  {
    String result = "digraph G {\n";

    try
    {
      lock.startRead();
      if (root != null)
        result += root.toString();
      lock.endRead();
    }
    catch (InterruptedException e)
    {
      result += "Interrupted";
    }
    result += "}";
    return result;
  }

  private SuchtbaumElement getLargest(SuchtbaumElement start)
  {
    SuchtbaumElement walk = start;
    while (walk.getRight() != null)
    {
      walk = walk.getRight();
    }
    return walk;
  }

  private void setParent(SuchtbaumElement parent, boolean left, SuchtbaumElement element)
  {
    if (parent == null)
      root = element;
    else if (left)
      parent.setLeft(element);
    else
      parent.setRight(element);
  }

  /*
  Der Test wurde in das selbe Program geschrieben, da es auf Piazza nicht eindeutig ist
  ob das ganze als Unittest oder als Main geschrieben werden soll,
  Alternativ kann man natürlich alles in einen Unittest schreiben der TestThreaded ausführt,
  dafür muss zusätzlich alles unterhalb dieses Kommentars in den Unittest geschrieben werden
   */



  //Variable für das Testen
  private int count;

  private class Runner implements Runnable
  {
    Suchtbaum baumi;
    int delay;
    long start;

    private Runner(Suchtbaum suchti, int ms, long startTime)
    {
      baumi = suchti;
      delay = ms;
      start = startTime;
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
            log("read", start);
          }
          else if (state <= 60)
          {
            rand = (int) (Math.random() * 1000);
            baumi.contains(rand);
            log("read", start);
          }
          else if (state <= 80)
          {
            rand = (int) (Math.random() * 1000);
            baumi.insert(rand);
            log("write ->", start);
          }
          else if (state <= 100)
          {
            rand = (int) (Math.random() * 1000);
            baumi.remove(rand);
            log("write <-", start);
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

  private void log(String input, long start)
  {
    System.out.print("Time " + (System.currentTimeMillis() - start) + ": ");
    System.out.println(input);
  }

  public void testThreaded() throws InterruptedException
  {
    long startTime = System.currentTimeMillis();
    //Amount of threads
    int n = 100;
    int sleepDuration = 1000;
    Suchtbaum<Integer> suchti = new Suchtbaum<>();

    for (int i = 0; i < n; i++)
    {
      int delay = (int) (Math.random() * sleepDuration);
      Runner runner = new Runner(suchti, delay, startTime);
      new Thread(runner).start();
    }
    while (System.currentTimeMillis()-startTime < 10000)
    {
      System.out.println(count + " active in the last " + sleepDuration + "ms");
      count = 0;
      Thread.sleep(sleepDuration);
    }
    System.out.println("Test beendet");
    System.exit(0);
  }

  //TODO das ist noch nicht wirklich schön, warum kann ich testThreaded nicht einfach static
  // machen?
  public static void main(String[] args) throws InterruptedException
  {
    System.out.println("Hier werden jetzt viele Exceptions geworfen, da Objekte entfernt werden"
        + " die gar nicht in dem Baum enthalten sind");
    Suchtbaum hauptprogramm = new Suchtbaum();
    hauptprogramm.testThreaded();
  }
}
