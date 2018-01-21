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
