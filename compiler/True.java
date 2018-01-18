package compiler;

public class True extends Condition
{
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
//UTF-8 Encoded ä
