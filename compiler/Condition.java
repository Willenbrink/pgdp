package compiler;

public abstract class Condition
{
  public abstract void accept(Visitor visitor);

  public abstract int firstLevelPriority();

  @Override
  public String toString()
  {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
//UTF-8 Encoded ä
