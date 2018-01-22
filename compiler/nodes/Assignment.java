package compiler.nodes;

import compiler.Visitor;

public class Assignment extends Statement
{
  private String name;
  private Expression expression;

  public Assignment(String name, Expression expression)
  {
    this.name = name;
    this.expression = expression;
  }

  public String getName()
  {
    return name;
  }

  public Expression getExpression()
  {
    return expression;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
