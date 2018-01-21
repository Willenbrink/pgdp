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
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
