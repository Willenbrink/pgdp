package compiler;

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
