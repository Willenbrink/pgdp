package compiler.nodes;

import compiler.FormatVisitor;
import compiler.Visitor;

public class ArrayAccess extends Expression
{
  private Expression expr;
  private Expression field;

  public ArrayAccess(Expression expr, Expression field)
  {
    this.expr = expr;
    this.field = field;
  }

  public Expression getExpr()
  {
    return expr;
  }

  public Expression getField()
  {
    return field;
  }

  @Override
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    return FormatVisitor.call;
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
