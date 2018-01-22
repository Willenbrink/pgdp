package compiler.nodes;

public enum Bbinop {
  And, Or;

  @Override
  public String toString()
  {
    switch(this)
    {
      case And: return "&&";
      case Or: return "||";
      default: throw new RuntimeException("Invalid Comparison");
    }
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
