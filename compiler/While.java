package compiler;

public class While extends Statement
{
  private Condition cond;
  private Statement body;

  public Condition getCond()
  {
    return cond;
  }

  public Statement getBody()
  {
    return body;
  }

  public While(Condition cond, Statement body)
  {

    this.cond = cond;
    this.body = body;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
