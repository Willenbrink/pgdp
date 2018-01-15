package compiler;

public class Assignment extends Statement
{
  private String name;
  private Expression expression;

  public Assignment(String name, Expression expression)
  {
    this.name = name;
    this.expression = expression;
  }

  public String getName()
  {
    return name;
  }

  public Expression getExpression()
  {
    return expression;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
