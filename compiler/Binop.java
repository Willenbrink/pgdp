package compiler;

public enum Binop
{
  Minus, Plus, MultiplicationOperator, DivisionOperator, Modulo;

  @Override
  public String toString()
  {
    switch (this)
    {
      case Minus:
        return "-";
      case Plus:
        return "+";
      case MultiplicationOperator:
        return "*";
      case DivisionOperator:
        return "/";
      case Modulo:
        return "%";
      default:
        throw new RuntimeException("Invalid Binop");
    }
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
