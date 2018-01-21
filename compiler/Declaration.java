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

  @Override
  public String toString()
  {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
//UTF-8 Encoded ä
