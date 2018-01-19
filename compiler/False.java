package compiler;

public class False extends Condition
{
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    return FormatVisitor.bool;
  }
}
//UTF-8 Encoded ä
