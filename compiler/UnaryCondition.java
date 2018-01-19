package compiler;

public class UnaryCondition extends Condition
{
  private Bunop operator;
  private Condition operand;

  public UnaryCondition(Bunop operator, Condition operand)
  {
    this.operator = operator;
    this.operand = operand;
  }

  public Bunop getOperator()
  {
    return operator;
  }

  public Condition getOperand()
  {
    return operand;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    return FormatVisitor.negation;
  }
}
//UTF-8 Encoded ä
