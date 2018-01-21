package compiler;

public class Program
{
  private Function[] functions;

  public Function[] getFunctions()
  {
    return functions;
  }

  public Program(Function[] functions)
  {

    this.functions = functions;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public String toString()
  {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
