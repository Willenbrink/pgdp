package compiler;

public class Return extends Statement
{
  private Expression expression;

  public Expression getExpression()
  {
    return expression;
  }

  public Return(Expression expression)
  {

    this.expression = expression;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
