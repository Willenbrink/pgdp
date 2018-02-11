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
  private boolean amBrüten;
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
      if(wurdeGezwungen)
      {
        mannuinBrüten();
        wurdeGezwungen = false;
      }
      brütuininTry();
      move();
      amSchlüpfen();
      warten();
    }
  }

  public int getFg()
  {
    if (age < adultAge)
    {
      if (amBrüten)
        throw new RuntimeException("Der Kleine ist nicht schwanger, nur dick");
      if (female)
        return GUI.KLEINUININ;
      return GUI.KLEINUIN;
    }
    if (amBrüten)
      return GUI.SCHWANGUIN;
    if (female)
      return GUI.FRAUIN;
    return GUI.MANNUIN;
  }

  private void warten()
  {
    int duration = minimum + (int) (Math.random() * increase);
    if(Colony.logSleep)
      log("Sleep: " + duration);
    try
    {
      Thread.sleep(duration);
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }

  private synchronized void move()
  {
    int xNew, yNew;
    //Auswahl der Richtung
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
        remove();
      else
      {
        if(Colony.logMove)
          log("Move", xNew, yNew);
        colony.move(this, x, y, xNew, yNew);
        colony.getSquareLocks()[x][y].reset();
        colony.getSquareLocks()[xNew][yNew].check();
        //Nur wenn er moved, soll er auch altern
        aging();
      }
    }
  }

  private boolean canMove(int xNew, int yNew)
  {
    try
    {
      //Auch wenn ein anderer Pinguin da steht
      //if(colony.checkPeng(xNew, yNew))
        //return false;
      return colony.getSquareLocks()[xNew][yNew].check();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      //Wenn es eine OutOfBounds Exception gibt, versucht der Pinguin die Kolonie
      // zu verlassen, das geht immer
      return true;
    }
  }

  private void brütuininTry()
  {
    if(female && colony.isIce(x,y) && Math.random() > 0.95 && !amBrüten && age >= adultAge)
    {
      //Falls am linken Rand des Spielfelds Eis ist
      if(x == 0)
        return;
      Penguin mannuin = colony.getPlaced()[x-1][y];
      if(mannuin != null && !mannuin.female && mannuin.age >= adultAge && !mannuin.amBrüten)
      {
        if(Math.random() > 0.5)
        {
          if(Colony.logBrüten)
            log("Frauin brütet");
          amBrüten = true;
          colony.move(this,x,y,x,y);
          colony.getSquareLocks()[x][y].check();
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
      if(Colony.logBrüten)
        log("Leider niemand zum brüten da");
    }
  }

  private synchronized void mannuinBrüten()
  {
    if(Colony.logBrüten)
      log("Mannuin brütet");
    amBrüten = true;
    colony.move(this,x,y,x,y);
    colony.getSquareLocks()[x][y].check();
    try
    {
      Thread.sleep(9000);
    }
    catch (InterruptedException e)
    {}
  }

  private void amSchlüpfen()
  {
    if(amBrüten)
    {
      int prevx = x, prevy = y;
      amBrüten =false;
      move();
      boolean kleinuinuinuinuinuinininin;
      if(Math.random() > 0.5)
        kleinuinuinuinuinuinininin = true;
      else
        kleinuinuinuinuinuinininin = false;
      colony.getPlaced()[prevx][prevy] = new Penguin(kleinuinuinuinuinuinininin, prevx, prevy, 0, colony);
      new Thread(colony.getPlaced()[prevx][prevy]).start();
      if(Colony.logSchlüpfen)
        System.out.println("Frolocket, uns ist ein Pinguin geboren worden!");
    }
  }

  private void aging()
  {
    age++;
  }

  private void remove()
  {
    if(Colony.logRemove)
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

  private synchronized void setWurdeGezwungen()
  {
    //Der arme :(
    wurdeGezwungen = true;
  }
}
