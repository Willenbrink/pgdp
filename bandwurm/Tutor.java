package bandwurm;

public class Tutor extends Thread
{
  //Buffers[pos] -> Buffers[pos+1]
  private int pos;
  private boolean zählen;

  public Tutor(int pos, boolean zählen)
  {
    //TODO check pos
    this.zählen = zählen;
    this.pos = pos;
  }

  @Override
  public void run()
  {
    //TODO breakcondition braucht man gar nicht oder?
    while (true)
    {
      Buffer bufferFrom = Klausurkorrektur.getBuffer(pos);

      Klausur klausur = bufferFrom.getKlausur();
      if (!zählen)
      {
        correct(klausur);
        Buffer bufferTo = Klausurkorrektur.getBuffer(pos + 1);
        bufferTo.addKlausur(klausur);
      }
      else
        zählen(klausur);
    }
  }

  protected void correct(Klausur klausur)
  {
    try
    {
      Thread.sleep(10);
    } catch (Exception e)
    {
    }
    klausur.setPunkte(pos,
        Korrekturschema.punkte(pos + 1, klausur.getAntwort(pos)));
    log("Aufgabe aus " + pos + " wurde korrigiert");
  }

  private void zählen(Klausur klausur)
  {
    int punkte = 0;
    for (int punkt : klausur.getPunkte())
    {
      punkte += punkt;
    }
    klausur.setGesamtpunktzahl(punkte);
    log(klausur.toString());
  }

  protected void log(String s)
  {
    System.out.print("At " +
        (System.currentTimeMillis() - Klausurkorrektur.start) + "ms: ");
    System.out.println(s);
  }
}
