package compiler;

public class IfThenElse extends Statement
{
  private Condition cond;
  private Statement thenBranch;
  private Statement elseBranch;

  public Condition getCond()
  {
    return cond;
  }

  public Statement getThenBranch()
  {
    return thenBranch;
  }

  public Statement getElseBranch()
  {
    return elseBranch;
  }

  public IfThenElse(Condition cond, Statement thenBranch, Statement elseBranch)
  {

    this.cond = cond;
    this.thenBranch = thenBranch;
    this.elseBranch = elseBranch;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
