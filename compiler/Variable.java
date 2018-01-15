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
}
//UTF-8 Encoded ä
