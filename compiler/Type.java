package compiler;

public enum Type
{
  Integer, Array;

  @Override
  public String toString()
  {
    switch(this)
    {
      case Integer: return "int";
      case Array: return "int[]";
      default: throw new RuntimeException("Invalid Comparison");
    }
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
