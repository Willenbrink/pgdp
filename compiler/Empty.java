package compiler;

public class Empty extends Statement
{
  @Override
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
