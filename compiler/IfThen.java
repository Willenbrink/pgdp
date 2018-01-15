package compiler;

public class IfThen extends Statement
{
  private Condition cond;
  private Statement thenBranch;

  public Condition getCond()
  {
    return cond;
  }

  public Statement getThenBranch()
  {
    return thenBranch;
  }

  public IfThen(Condition cond, Statement thenBranch)
  {

    this.cond = cond;
    this.thenBranch = thenBranch;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
