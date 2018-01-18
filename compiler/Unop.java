package compiler;

public enum Unop {
  Minus;

  @Override
  public String toString()
  {
    switch(this)
    {
      case Minus: return "-";
      default: throw new RuntimeException("Invalid Comparison");
    }
  }
}
//UTF-8 Encoded ä
