package compiler.nodes;

import compiler.FormatVisitor;
import compiler.Visitor;

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

  @Override
  public int firstLevelPriority()
  {
    return FormatVisitor.comparison;
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
