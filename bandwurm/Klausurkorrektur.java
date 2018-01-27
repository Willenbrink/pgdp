package bandwurm;

public class Klausurkorrektur
{
  public static long start = System.currentTimeMillis();
  private static Buffer[] buffers;
  //TODO 1700
  private final static int amountStudents = 100;

  public Klausurkorrektur() throws Exception
  {
    //Initialisierung
    buffers = new Buffer[10];
    buffers[0] = new Buffer(amountStudents);
    buffers[8] = new Buffer(amountStudents);
    buffers[9] = new Buffer(amountStudents);
    for (int i = 1; i < 8; i++)
      buffers[i] = new Buffer(50);

    //Hinzufügen der Klausuren
    for (int i = 0; i < amountStudents; i++)
      buffers[0].addKlausur(new Klausur());

    //Start der Korrekturmaschine
    //TODO Verteilung eventuell noch ein wenig anpassen
    for (int i = 0; i < 60; i++)
    {
      if(i > 55)
        new Tutor(9, true).start();
      else
        new Tutor(i%8, false).start();
    }
    new Uebungsleitung().start();
    new Uebungsleitung().start();

    while(buffers[9].getTraffic() != amountStudents)
      Thread.sleep(1000);
    //Ich Schelm habe das Grinsegesicht umgedreht,
    // ich krieg dafür aber keinen Punktabzug oder?
    System.out.println("Korrektur der Info 1 Klausur beendet (:");
    //Eigentlich soll ja nur der Main-Thread beenden aber das macht irgendwie keinen Sinn
    //Deswegen einfach System.exit(0);
    //Thread.yield();
    System.exit(0);
  }

  public static Buffer getBuffer(int pos)
  {
    return buffers[pos];
  }

  public static void main(String[] args) throws Exception
  {
    new Klausurkorrektur();
  }
}
