package compiler;

public enum Type
{
  Integer, Array;

  @Override
  public String toString()
  {
    switch(this)
    {
      case Integer: return "int";
      case Array: return "int[]";
      default: throw new RuntimeException("Invalid Comparison");
    }
  }
}
//UTF-8 Encoded ä
