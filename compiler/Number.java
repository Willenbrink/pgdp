package compiler;

public class Number extends Expression
{
  private int value;

  public Number(int value)
  {
    this.value = value;
  }

  public int getValue()
  {
    return value;
  }

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
