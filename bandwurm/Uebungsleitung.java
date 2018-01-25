package bandwurm;

public class Uebungsleitung extends Tutor
{
  String besteAntworten = "bcaabddc";

  public Uebungsleitung()
  {
    super(8, false);
  }

  @Override
  protected void correct(Klausur klausur)
  {
    //correct ist hier leider nicht korrekt sondern eher willkürlich
    //Das spieegelt unter Umständen aber die realen Begebenheiten gut wieder
    //TODO soll jede Punkteverteilung gleich wahrscheinlich sein?
    double chance = Math.random();
    if(chance < 0.9)
      return;
    for (int i = 0; i < klausur.getPunkte().length; i++)
    {
      int punkte = klausur.getPunkte()[i];
      chance = Math.random();
      if(punkte > 0 && chance < 0.5)
      {
        klausur.setPunkte(i, punkte - 1);
        log("Erfolg beim Schnitt senken");
        return;
      }
      else if(punkte < Korrekturschema.punkte(i+1, besteAntworten.charAt(i)+""))
      {
        klausur.setPunkte(i, punkte + 1);
        return;
      }


    }
  }
}
