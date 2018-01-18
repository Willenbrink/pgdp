package compiler;

public enum Bbinop {
  And, Or;

  @Override
  public String toString()
  {
    switch(this)
    {
      case And: return "&&";
      case Or: return "||";
      default: throw new RuntimeException("Invalid Comparison");
    }
  }
}
//UTF-8 Encoded ä
