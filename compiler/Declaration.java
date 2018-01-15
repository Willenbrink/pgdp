package compiler;

public class Declaration
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
