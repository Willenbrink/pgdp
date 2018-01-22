package compiler.nodes;

import compiler.Visitor;

public class Composite extends Statement
{
  private Statement[] statements;

  public Composite(Statement[] statements)
  {
    this.statements = statements;
  }

  public Statement[] getStatements()
  {
    return statements;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
