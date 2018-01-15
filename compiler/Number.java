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
}
//UTF-8 Encoded ä
