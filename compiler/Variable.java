package compiler;

public class Variable extends Expression
{
  private String name;

  public Variable(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    return FormatVisitor.number;
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
