package compiler;

public class Unary extends Expression
{
  private Unop operator;
  private Expression operand;

  public Unary(Unop operator, Expression operand)
  {
    this.operator = operator;
    this.operand = operand;
  }

  public Unop getOperator()
  {
    return operator;
  }

  public Expression getOperand()
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
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
