package compiler;

public class Function
{
  private Type type;
  private String name;
  private Parameters params;
  private Declaration[] declarations;
  private Statement[] statements;

  public Type getType()
  {
    return type;
  }

  public String getName()
  {
    return name;
  }

  public Parameters getParameters()
  {
    return params;
  }

  public Declaration[] getDeclarations()
  {
    return declarations;
  }

  public Statement[] getStatements()
  {
    return statements;
  }

  public Function(Type type, String name, Parameters params, Declaration[] declarations,
      Statement[] statements)
  {
    this.type = type;
    this.name = name;
    this.params = params;
    this.declarations = declarations;
    this.statements = statements;
  }

  public Function(String name, String[] params, Declaration[] declarations,
      Statement[] statements)
  {
    type = Type.Integer;
    this.name = name;
    this.params = new Parameters(new Type[params.length], params);
    this.declarations = declarations;
    this.statements = statements;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
