package compiler;

public abstract class Statement
{
  public abstract void accept(Visitor visitor);

  @Override
  public String toString()
  {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
//UTF-8 Encoded ä
