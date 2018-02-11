package bandwurm;

public class Tutor extends Thread
{
  //Buffers[pos] -> Buffers[pos+1]
  private int pos;
  private boolean zählen;

  public Tutor(int pos, boolean zählen)
  {
    if(pos < 0 || pos > 9)
      throw new RuntimeException("Invalide Tutorposition");
    this.zählen = zählen;
    this.pos = pos;
  }

  @Override
  public void run()
  {
    Buffer bufferFrom = Klausurkorrektur.getBuffer(pos);
    while (bufferFrom.getTraffic() < Klausurkorrektur.getAmountStudents()
        || bufferFrom.getAmount() > 0)
    {

      Klausur klausur = bufferFrom.getKlausur();
      if (!zählen)
      {
        Buffer bufferTo = Klausurkorrektur.getBuffer(pos + 1);
        correct(klausur);
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
      Thread.sleep(100);
    } catch (Exception e)
    {
    }
    klausur.setPunkte(pos,
        Korrekturschema.punkte(pos + 1, klausur.getAntwort(pos)));

    int punkte = 0;
    for (int punkt : klausur.getPunkte())
    {
      punkte += punkt;
    }
    log("Aufgabe " + (pos+1) + " wurde korrigiert, hat Punkte: " + punkte);
  }

  private void zählen(Klausur klausur)
  {
    int punkte = 0;
    for (int punkt : klausur.getPunkte())
    {
      punkte += punkt;
    }
    klausur.setGesamtpunktzahl(punkte);
    klausur.setNote(Korrekturschema.note(punkte));
    log(klausur.toString());
  }

  protected void log(String s)
  {
    System.out.print("At " +
        (System.currentTimeMillis() - Klausurkorrektur.start) + "ms: ");
    System.out.println(s);
  }
}
