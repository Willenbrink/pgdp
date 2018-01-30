package paralleluine;

public class Colony extends GUI
{
  public static final boolean logSleep =      true;
  public static final boolean logMove =       true;
  public static final boolean logBrüten =     true;
  public static final boolean logSchlüpfen =  true;
  public static final boolean logAltern =     true;
  public static final boolean logRemove =     true;
  public static final boolean logMonitors =   true;


  public static long startTime;
  private int width, height;

  private final int[][] landscape;
  private final Penguin[][] placed;

  private final Object[][] squareLocks;
  private final Object drawLock = new Object();


  public Colony(int width, int height, boolean standard)
  {
    this.width = width;
    this.height = height;
    startTime = System.currentTimeMillis();
    placed = new Penguin[width][height];
    landscape = new int[placed.length][placed[0].length];
    squareLocks = new Monitor[placed.length][placed[0].length]; // all still null
    generateAntarctic(landscape, placed, standard);

    //Monitore müssen erst vollständig instanziiert sein bevor
    // die Pinguine starten, da ansonsten ein Pinguin auf ein noch nicht
    // initialisiertes Feld zugreifen könnte
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
    draw();
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
    draw();
  }

  private void reset(int x, int y)
  {
    ((Monitor) (squareLocks[x][y])).reset();
    setForeground(landscape, x, y, NIXUIN);
    draw();
  }

  private void draw()
  {
    synchronized (drawLock)
    {
      if(logMonitors)
        System.out.print(logMonitors());
      draw(landscape);
    }
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
