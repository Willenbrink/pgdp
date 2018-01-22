package compiler.nodes;

import compiler.Visitor;

public class Write extends Statement
{
  private Expression expression;

  public Expression getExpression()
  {
    return expression;
  }

  public Write(Expression expression)
  {

    this.expression = expression;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
