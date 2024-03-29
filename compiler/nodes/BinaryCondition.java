package compiler.nodes;

import compiler.FormatVisitor;
import compiler.Visitor;

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

  @Override
  public int firstLevelPriority()
  {
    switch (operator)
    {
      case And: return FormatVisitor.and;
      case Or: return FormatVisitor.or;
      default: throw new RuntimeException("Invalid Bbinop");
    }
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
