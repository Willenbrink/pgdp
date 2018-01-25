package bandwurm;

public class Klausurkorrektur
{
  public static long start = System.currentTimeMillis();
  private static Buffer[] buffers;

  public Klausurkorrektur()
  {
    buffers = new Buffer[10];
    buffers[0] = new Buffer(1700);
    buffers[8] = new Buffer(1700);
    buffers[9] = new Buffer(1700);
    for (int i = 1; i < 8; i++)
    {
      buffers[i] = new Buffer(50);
    }

    for (int i = 0; i < 1700; i++)
    {
      buffers[0].addKlausur(new Klausur());
    }

    for (int i = 0; i < 60; i++)
    {
      if(i > 56)
        new Tutor(9, true).start();
      else
        new Tutor(i%8, false).start();
    }
    new Uebungsleitung().start();
    new Uebungsleitung().start();
  }

  public static Buffer getBuffer(int pos)
  {
    return buffers[pos];
  }

  public static void main(String[] args) throws Exception
  {
    new Klausurkorrektur();
    while(buffers[9].getAmount() != 0
        && buffers[9].getTraffic() == 1700)
      Thread.sleep(1000);
    //Ich Schelm habe das Grinsegesicht umgedreht, krieg ich dafür Punktabzug?
    System.out.println("Korrektur der Info 1 Klausur beendet (:");
  }
}
