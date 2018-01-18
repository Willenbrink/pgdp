package compiler;

public class Call extends Expression
{
  private String functionName;
  private Expression[] arguments;

  public Call(String functionName, Expression[] arguments)
  {
    this.functionName = functionName;
    this.arguments = arguments;
  }

  public String getFunctionName()
  {
    return functionName;
  }

  public Expression[] getArguments()
  {
    return arguments;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public int firstLevelPriority()
  {
    //TODO
    return -1;
  }


}
//UTF-8 Encoded ä
