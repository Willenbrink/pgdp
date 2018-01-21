package compiler;

public class ArrayAssignment extends Statement
{
  private String name;
  private Expression field;
  private Expression value;

  public String getName()
  {
    return name;
  }

  public Expression getField()
  {
    return field;
  }

  public Expression getValue()
  {
    return value;
  }

  public ArrayAssignment(String name, Expression field, Expression value)
  {

    this.name = name;
    this.field = field;
    this.value = value;
  }

  @Override
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
