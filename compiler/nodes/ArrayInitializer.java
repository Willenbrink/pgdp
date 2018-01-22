package compiler.nodes;

import compiler.FormatVisitor;
import compiler.Visitor;

public class ArrayInitializer extends Expression
{
  private Expression expr;

  public ArrayInitializer(Expression expr)
  {
    this.expr = expr;
  }

  public Expression getExpr()
  {
    return expr;
  }

  @Override
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    return FormatVisitor.top;
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
