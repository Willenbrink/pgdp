package compiler;

public enum Type
{
  Integer;

  @Override
  public String toString()
  {
    switch(this)
    {
      case Integer: return "int";
      default: throw new RuntimeException("Invalid Comparison");
    }
  }
}
//UTF-8 Encoded ä
