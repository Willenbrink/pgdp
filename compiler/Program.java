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
}
//UTF-8 Encoded ä
