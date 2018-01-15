package compiler;

public class Write extends Statement
{
  private Expression expression;

  public Expression getExpression()
  {
    return expression;
  }

  public Write(Expression expression)
  {

    this.expression = expression;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
