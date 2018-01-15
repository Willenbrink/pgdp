package compiler;

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
