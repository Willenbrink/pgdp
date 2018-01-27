package paralleluine;

public class Colony extends GUI
{
  public static long startTime;
  private int width, height;

  private final int[][] landscape;
  private final Penguin[][] placed;

  public final Object[][] squareLocks;
  public final Object drawLock = new Object();


  public Colony(int width, int height, boolean standard)
  {
    this.width = width;
    this.height = height;
    startTime = System.currentTimeMillis();
    placed = new Penguin[width][height];
    landscape = new int[placed.length][placed[0].length];
    squareLocks = new Monitor[placed.length][placed[0].length]; // all still null
    generateAntarctic(landscape, placed, standard);

    for (int i = 0; i < width; i++)
    {
      for (int j = 0; j < height; j++)
      {
        squareLocks[i][j] = new Monitor();
      }
    }

    for (int i = 0; i < width; i++)
    {
      for (int j = 0; j < height; j++)
      {
        if (placed[i][j] != null)
        {
          new Thread(placed[i][j]).start();
          ((Monitor) (squareLocks[i][j])).check();
        }
      }
    }

    synchronized (drawLock)
    {
      System.out.print(logMonitors());
      draw(landscape);
      nop();
    }
  }

  private void nop()
  {
  }

  public void move(Penguin peng, int x, int y, int xNew, int yNew)
  {
    placed[xNew][yNew] = placed[x][y];
    placed[x][y] = null;
    reset(x, y);
    peng.setPos(xNew, yNew);
    setForeground(landscape, xNew, yNew, peng.getFg());

    //Draw muss jedes Mal aufgerufen werden wenn diese Methode aufgerufen wird,
    // da die Position sich jedes Mal zwangsläufig ändern muss
    synchronized (drawLock)
    {
      System.out.print(logMonitors());
      draw(landscape);
      nop();
    }
  }

  private void reset(int x, int y)
  {
    ((Monitor) (squareLocks[x][y])).reset();
    setForeground(landscape, x, y, NIXUIN);
  }

  public void remove(int x, int y)
  {
    placed[x][y] = null;
    reset(x, y);
  }

  public boolean checkPeng(int x, int y)
  {
    return placed[x][y] != null;
  }

  private synchronized String logMonitors()
  {
    String result = "";
    for (int j = 0; j < squareLocks[0].length; j++)
    {
      for (int i = 0; i < squareLocks.length; i++)
      {
        if (((Monitor) (squareLocks[i][j])).peek())
          result += 1;
        else
          result += ".";
      }
      result += "\n";
    }
    result += "\n";
    result  = "";
    return result;
  }

  public boolean isIce(int x, int y)
  {
    return !ocean(landscape, x, y);
  }

  public Monitor[][] getSquareLocks()
  {
    return (Monitor[][]) squareLocks;
  }

  public int getWidth2()
  {
    return width;
  }

  public int getHeight2()
  {
    return height;
  }

  public Penguin[][] getPlaced()
  {
    return placed;
  }
}
