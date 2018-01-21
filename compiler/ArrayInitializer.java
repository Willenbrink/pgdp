package compiler;

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
    return FormatVisitor.call;
  }
}
