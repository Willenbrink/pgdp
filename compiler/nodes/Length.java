package compiler.nodes;

import compiler.Visitor;

public class Length extends Expression
{
  private Expression expr;

  public Length(Expression expr)
  {
    this.expr = expr;
  }

  public Expression getExpr()
  {
    return expr;
  }

  public void setExpr(Expression expr)
  {
    this.expr = expr;
  }

  @Override
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    return 0;
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
