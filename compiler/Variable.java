package compiler;

public class Variable extends Expression
{
  private String name;

  public Variable(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    return FormatVisitor.number; //TODO name not number
  }
}
//UTF-8 Encoded ä
