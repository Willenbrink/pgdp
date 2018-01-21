package compiler;

public class Parameters
{
  private String[] names;
  private Type[] types;

  public Type[] getTypes()
  {
    return types;
  }

  public String[] getNames()
  {
    return names;
  }

  public Parameters(Type[] types, String[] names)
  {
    this.types = types;
    this.names = names;
  }

  public Parameters(String[] names)
  {
    types = new Type[names.length];
    for (int i = 0; i < names.length; i++)
    {
      types[i] = Type.Integer;
    }
    this.names = names;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public String toString()
  {
    FormatVisitor visitor = new FormatVisitor();
    accept(visitor);
    return visitor.getResult();
  }
}
//UTF-8 Encoded ä
