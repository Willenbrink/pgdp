package compiler;

public class Read extends Statement
{
  private String name;

  public String getName()
  {
    return name;
  }

  public Read(String name)
  {

    this.name = name;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
