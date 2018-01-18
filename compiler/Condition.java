package compiler;

public abstract class Condition
{
  public abstract void accept(Visitor visitor);

  public abstract int firstLevelPriority();
}
//UTF-8 Encoded ä
