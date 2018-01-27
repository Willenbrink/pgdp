package bandwurm;

public class Uebungsleitung extends Tutor
{
  private String besteAntworten = "bcaabddc";

  public Uebungsleitung()
  {
    super(8, false);
  }

  @Override
  protected void correct(Klausur klausur)
  {
    //correct ist hier leider nicht korrekt sondern eher willkürlich
    // Das spiegelt aber unter Umständen die realen Begebenheiten gut wieder
    double chance = Math.random();
    if (chance < 0.9)
      return;
    for (int i = 0; i < klausur.getPunkte().length; i++)
    {
      int punkte = klausur.getPunkte()[i];
      chance = Math.random();
      boolean lower, rise;
      lower = punkte > 0;
      rise = punkte < Korrekturschema.punkte(i + 1,
          besteAntworten.charAt(i) + "");
      if (!lower && !rise)
      {
        //Sollte normalerweise nicht eintreten,
        // da es keine Aufgabe mit max 0 Punkten gibt
        return;
      }
      if (lower && !rise)
      {
        System.out.println("Dem haben wirs gezeigt, mit Mr. Kranz ist nicht zu spaßen!");
        klausur.setZweitkorrektur(i, punkte - 1);
      }
      if (rise && !lower)
      {
        System.out.println("Na gut, wenn er eh schon keine Punkte hat");
        klausur.setZweitkorrektur(i, punkte + 1);
      }
      if (rise && lower)
      {
        System.out.println("Kopf oder Zahl? Mal schauen was du für eine Note kriegst");
        if (chance > 0.95)
          klausur.setZweitkorrektur(i, punkte - 1);
        else
          klausur.setZweitkorrektur(i, punkte + 1);
      }
    }
  }
}
