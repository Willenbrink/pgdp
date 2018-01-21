package nogivan;

public class Pair<_1, _2> {
  public final _1 _1;
  public final _2 _2;

  public Pair (_1 _1, _2 _2) {
    this._1 = _1;
    this._2 = _2;
  }

  public String toString () {
    return "(" + _1.toString() + "," + _2.toString() + ")";
  }

  @SuppressWarnings("rawtypes")
  @Override public boolean equals (Object o) {
    return o instanceof Pair && ((Pair) o)._1.equals(_1) && ((Pair) o)._2.equals(_2);
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
