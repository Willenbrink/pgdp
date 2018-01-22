package compiler.nodes;

import compiler.FormatVisitor;
import compiler.Visitor;

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
    return FormatVisitor.top;
  }


}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
