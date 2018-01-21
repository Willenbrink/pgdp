package compiler;

public enum Comp {
  Equals, NotEquals, LessEqual, Less, GreaterEqual, Greater;

  @Override
  public String toString()
  {
    switch(this)
    {
      case Equals: return "==";
      case Greater: return ">";
      case Less: return "<";
      case LessEqual:return "<=";
      case NotEquals: return "!=";
      case GreaterEqual: return ">=";
      default: throw new RuntimeException("Invalid Comparison");
    }
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
