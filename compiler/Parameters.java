package compiler;

public class Parameters extends TreeNode
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
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
