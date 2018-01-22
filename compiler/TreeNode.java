package compiler;

public abstract class TreeNode
{
  @Override
  public final String toString()
  {
    FormatVisitor visitor = new FormatVisitor();
    this.accept(visitor);
    return visitor.getResult();
  }

  public abstract void accept(Visitor visitor);
}
