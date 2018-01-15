package compiler;

public class BinaryCondition extends Condition
{
  private Condition lhs, rhs;
  private Bbinop operator;

  public BinaryCondition(Condition lhs, Bbinop operator, Condition rhs)
  {
    this.lhs = lhs;
    this.rhs = rhs;
    this.operator = operator;
  }

  public Condition getLhs()
  {
    return lhs;
  }

  public Bbinop getOperator()
  {
    return operator;
  }

  public Condition getRhs()
  {
    return rhs;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
