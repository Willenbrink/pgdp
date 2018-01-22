package compiler.nodes;

import compiler.Visitor;

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
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
