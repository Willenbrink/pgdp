package paralleluine;

public class Penguin implements Runnable
{
  //ID
  private int id;
  private static int amount;

  //Constants
  private int minimum = 200;
  private int increase = 300;
  public static final int adultAge = 26;

  private boolean female;
  private int x, y;
  private int age;
  private Colony colony;
  private boolean brutus;
  private boolean wurdeGezwungen;
  private boolean stop;

  public Penguin(boolean female, int x, int y, int age, Colony col)
  {
    id = amount;
    amount++;

    this.female = female;
    this.x = x;
    this.y = y;
    this.age = age;
    colony = col;
  }

  public void run()
  {
    while (!stop)
    {
      //TODO eventuell kann das Lock verloren gehen zwischen den Loops
      //synchronized (colony.squareLocks[x][y])
      {
        if(wurdeGezwungen)
        {
          brutussen();
          wurdeGezwungen = false;
        }
        adBrutensis();
        move();
        adSchlupfensis();
        aging();
        warten();
      }
    }
  }

  public int getFg()
  {
    if (age < adultAge)
    {
      if (brutus)
        throw new RuntimeException("Der Kleine ist nicht schwanger, nur dick");
      if (female)
        return GUI.KLEINUININ;
      return GUI.KLEINUIN;
    }
    if (brutus)
      return GUI.SCHWANGUIN;
    if (female)
      return GUI.FRAUIN;
    return GUI.MANNUIN;
  }

  private void warten()
  {
    int duration = minimum + (int) (Math.random() * increase);
    //log("Sleep: " + duration);
    try
    {
      Thread.sleep(duration);
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }

  //TODO was heißt "muss entscheidung akzeptieren", auch wenn er schon in der Bewegung ist?
  private synchronized void move()
  {
    if(brutus)
      return;

    int xNew, yNew;
    switch ((int) (Math.random() * 4))
    {
      case 0:
        xNew = x + 1;
        yNew = y;
        break;
      case 1:
        xNew = x - 1;
        yNew = y;
        break;
      case 2:
        xNew = x;
        yNew = y + 1;
        break;
      case 3:
        xNew = x;
        yNew = y - 1;
        break;
      default:
          throw new RuntimeException("Invalid direction");
    }
    if (canMove(xNew, yNew))
    {
      if(xNew < 0 || yNew < 0
          || xNew > colony.getWidth2()-1
          || yNew > colony.getHeight2()-1)
      {
        remove();
        return;
      }
      colony.move(this, x, y, xNew, yNew);
    }
  }

  public boolean canMove(int xNew, int yNew)
  {
    try
    {
      //Auch wenn ein anderer Pinguin da steht
      if(colony.checkPeng(xNew, yNew))
      {
        log("Penguin bei " + xNew + "|" + yNew);
        return false;
      }
      if (!colony.getSquareLocks()[xNew][yNew].check())
        return false;
      return true;
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      return true;
    }
  }

  private void adBrutensis()
  {
    if(female && colony.isIce(x,y) && Math.random() > 0.95 && !brutus && age >= adultAge)
    {
      //Falls am linken Rand des Spielfelds Eis ist
      if(x == 0)
        return;
      Penguin mannuin = colony.getPlaced()[x-1][y];
      if(mannuin != null && !mannuin.female && mannuin.age >= adultAge && !mannuin.brutus)
      {
        if(Math.random() > 0.5)
        {
          log("Frauin brütet");
          brutus = true;
          colony.move(this,x,y,x,y);
          try
          {
            Thread.sleep(9000);
          }
          catch (InterruptedException e)
          {}
          return;
        }
        else
        {
          mannuin.setWurdeGezwungen();
          return;
        }
      }
      log("Leider nix zum brüten da");
    }
  }

  public synchronized void brutussen()
  {
    log("Mannuin brütet");
    brutus = true;
    colony.move(this,x,y,x,y);
    try
    {
      Thread.sleep(9000);
    }
    catch (InterruptedException e)
    {}
  }

  private void adSchlupfensis()
  {
    if(brutus)
    {
      int prevx = x, prevy = y;
      brutus=false;
      move();
      boolean kleinuinuinuinuinuinininin;
      if(Math.random() > 0.5)
        kleinuinuinuinuinuinininin = true;
      else
        kleinuinuinuinuinuinininin = false;
      colony.getPlaced()[prevx][prevy] = new Penguin(kleinuinuinuinuinuinininin, prevx, prevy, 0, colony);
      new Thread(colony.getPlaced()[prevx][prevy]).start();
    }
  }

  private void aging()
  {
    age++;
  }

  public void remove()
  {
    log(" ist auf zu neuen Kolonien!");
    colony.remove(x,y);
    stop = true;
  }

  private void log(String input, int xNew, int yNew)
  {
    System.out.print("Time "
        + (System.currentTimeMillis() - Colony.startTime)
        + ": ID "
        + id + ": "
        + x + "|" + y
        + " -> " + xNew + "|" + yNew + ": ");
    System.out.println(input);
  }

  private void log(String input)
  {
    System.out.print("Time "
        + (System.currentTimeMillis() - Colony.startTime)
        + ": ID "
        + id + ": "
        + x + "|" + y + ": ");
    System.out.println(input);
  }

  @Override
  public String toString()
  {
    String result = "";
    result += "ID "
      + id + ": "
      + x + "|" + y;
    return result;
  }

  public void setPos(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public synchronized void setWurdeGezwungen()
  {
    wurdeGezwungen = true;
  }
}
