package sucht;

public class Suchtbaum<T extends Comparable<T>>
{
  private class SuchtbaumElement
  {
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

    public void setElement(T element)
    {
      this.element = element;
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
      //TODO
      //In der Angabe haben nur die label=left ein Semikolon
      //Ich bin mir nicht sicher ob das Absicht ist, jedoch wurde
      //es hier einfach übernommen

      String result = element.toString() + ";\n";
      if(left != null)
      {
        result += element.toString()
            + " -> "
            + left.getElement().toString()
            + " [label=left];\n";
        result += left.toString();
      }

      if(right != null)
      {
        result += element.toString()
            + " -> "
            + right.getElement().toString()
            + " [label=right]\n";
        result += right.toString();
      }
      return result;
    }
  }

  private SuchtbaumElement root;
  private RWLock lock;

  public Suchtbaum()
  {
    lock = new RWLock();
  }

  //Etwas unpraktisches Design, da bei jedem return davor das Lock
  // gelöst werden müsste, deswegen eigene Wrappermethode, die das übernehmen
  public void insert(T element) throws InterruptedException
  {
    try
    {
      lock.startWrite();
      Thread.sleep(50);
      insertWrapped(element);
      lock.endWrite();
    }
    catch (RuntimeException e)
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

    if (containsWrapped(element))
      throw new RuntimeException("Element bereits enthalten");

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
        walk = walk.getLeft();
        continue;
      }
      if (comp > 0)
      {
        if (walk.getRight() == null)
        {
          walk.setRight(new SuchtbaumElement(element));
          return;
        }
        walk = walk.getRight();
        continue;
      }
    }
  }

  public boolean contains(T element) throws InterruptedException
  {
    try{
      lock.startRead();
      Thread.sleep(500);
      boolean result = containsWrapped(element);
      lock.endRead();
      return result;
    }
    catch(Exception e)
    {
      throw new RuntimeException("Blubb");
    }
  }

  private boolean containsWrapped(T element)
  {
    if (root == null)
      return false;
    SuchtbaumElement walk = root;
    while (true)
    {
      int comp = element.compareTo(walk.getElement());
      if (comp == 0)
        return true;
      if (comp < 0)
      {
        if (walk.getLeft() == null)
          return false;
        walk = walk.getLeft();
        continue;
      }
      if (walk.getRight() == null)
        return false;
      walk = walk.getRight();
    }
  }

  public void remove(T element) throws InterruptedException
  {
    try
    {
      lock.startWrite();
      Thread.sleep(50);
      removeWrapped(element);
      lock.endWrite();
    }
    catch (RuntimeException e)
    {
      lock.endWrite();
    }
  }

  private void removeWrapped(T element)
  {
    if (!containsWrapped(element))
      throw new RuntimeException("Element nicht enthalten");
    //Root kann nicht null sein, da ansonsten die Exception bereits geworfen
    //worden wäre
    SuchtbaumElement parent = root;
    boolean left = true;
    SuchtbaumElement walk = root;
    while (true)
    {
      int comp = element.compareTo(walk.getElement());
      if (comp == 0)
      {
        if (walk.getLeft() == null && walk.getRight() == null)
        {
          if (walk == root)
          {
            root = null;
            return;
          }
          setParent(parent, left, null);
          return;
        }

        if (walk.getLeft() != null && walk.getRight() != null)
        {
          SuchtbaumElement largest = getLargest(walk.getLeft());
          removeWrapped(largest.getElement());
          largest.setLeft(walk.getLeft());
          largest.setRight(walk.getRight());
          setParent(parent, left, largest);
          return;
        }

        //XOR der Nachfolger, einer von beiden nicht null
        {
          if (walk.getLeft() != null)
          {
            setParent(parent, left, walk.getLeft());
            walk.setLeft(null);
            return;
          }
          if (walk.getRight() != null)
          {
            setParent(parent, left, walk.getRight());
            walk.setRight(null);
            return;
          }
        }
      }

      //Hier wird bewusst auf die Überprüfung der Existenz verzichtet
      // da das Element enthalten sein muss
      if (comp < 0)
      {
        parent = walk;
        left = true;
        walk = walk.getLeft();
        continue;
      }
      if (comp > 0)
      {
        parent = walk;
        left = false;
        walk = walk.getRight();
        continue;
      }
    }
  }

  @Override
  public String toString()
  {
    try
    {
      lock.startRead();
      String result = "digraph G {\n";
      if (root != null)
        result += root.toString();
      result += "}";
      lock.endRead();
      return result;
    }
    catch (InterruptedException e)
    {
      //TODO Was mach ich jetzt damit?
      return "";
    }
  }

  private SuchtbaumElement getLargest(SuchtbaumElement start)
  {
    SuchtbaumElement walk = start;
    while(walk.getRight() != null)
      walk = walk.getRight();
    return walk;
  }

  private void setParent(SuchtbaumElement parent, boolean left, SuchtbaumElement element)
  {
    if (left)
      parent.setLeft(element);
    else
      parent.setRight(element);
  }
}
