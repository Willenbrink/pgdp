package compiler;

public class Declaration extends TreeNode
{
  private String[] names;

  public String[] getNames()
  {
    return names;
  }

  public Declaration(String[] names)
  {
    this.names = names;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
