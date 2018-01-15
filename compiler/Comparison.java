package compiler;

public class Comparison extends Condition
{
  private Expression lhs, rhs;
  private Comp operator;

  public Comparison(Expression lhs, Comp operator, Expression rhs)
  {
    this.lhs = lhs;
    this.rhs = rhs;
    this.operator = operator;
  }

  public Expression getLhs()
  {
    return lhs;
  }

  public Comp getOperator()
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
