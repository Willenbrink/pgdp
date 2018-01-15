package compiler;

public class Binary extends Expression
{
  private Expression lhs, rhs;
  private Binop operator;

  public Binary(Expression lhs, Binop operator, Expression rhs)
  {
    this.lhs = lhs;
    this.rhs = rhs;
    this.operator = operator;
  }

  public Expression getLhs()
  {
    return lhs;
  }

  public Binop getOperator()
  {
    return operator;
  }

  public Expression getRhs()
  {
    return rhs;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
