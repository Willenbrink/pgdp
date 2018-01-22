package compiler.nodes;

import compiler.FormatVisitor;
import compiler.Visitor;

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

  @Override
  public int firstLevelPriority()
  {
    switch (operator)
    {
      case MultiplicationOperator: return FormatVisitor.multiplication;
      case DivisionOperator: return FormatVisitor.division;
      case Modulo: return FormatVisitor.modulo;
      case Plus: return FormatVisitor.addition;
      case Minus: return FormatVisitor.subtraction;
      default: throw new RuntimeException("Invalid Binop");
    }
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
