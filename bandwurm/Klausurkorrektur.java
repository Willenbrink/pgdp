package bandwurm;

public class Klausurkorrektur
{
  public static long start = System.currentTimeMillis();
  private static Buffer[] buffers;
  private final static int amountStudents = 1700;

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
    // Die letzten 5 Tutoren zählen die Punkte
    // der Rest korrigiert die Aufgaben
    for (int i = 0; i < 60; i++)
    {
      if(i > 55)
        new Tutor(9, true).start();
      else
        new Tutor(i%8, false).start();
    }
    new Uebungsleitung().start();
    new Uebungsleitung().start();

    while(buffers[buffers.length-1].getTraffic() != amountStudents)
      Thread.sleep(1000);
    System.out.println("Korrektur der Info 1 Klausur beendet :)");
  }

  public static Buffer getBuffer(int pos)
  {
    return buffers[pos];
  }

  public static int getAmountStudents()
  {
    return amountStudents;
  }

  public static void main(String[] args) throws Exception
  {
    new Klausurkorrektur();
  }
}
//UTF-8 ä
