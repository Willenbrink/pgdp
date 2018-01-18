package compiler;

public enum Binop
{
  Minus, Plus, MultiplicationOperator, DivisionOperator, Modulo;

  @Override
  public String toString()
  {
    switch (this)
    {
      case Minus:
        return "-";
      case Plus:
        return "+";
      case MultiplicationOperator:
        return "*";
      case DivisionOperator:
        return "/";
      case Modulo:
        return "%";
      default:
        throw new RuntimeException("Invalid Binop");
    }
  }
}
//UTF-8 Encoded ä
