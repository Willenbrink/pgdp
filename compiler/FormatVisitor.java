package compiler;

public class FormatVisitor implements Visitor
{
  String result;

  public FormatVisitor()
  {
    result = "";
  }

  public String getResult()
  {
    return result;
  }

  @Override
  public void visit(Program item)
  {
    for(Function func : item.getFunctions())
      func.accept(this);
  }

  @Override
  public void visit(Function item)
  {
    add(item.getType(), item.getName());
    item.getParameters().accept(this);
    for (int i = 0; i < item.getDeclarations().length; i++)
    {
      item.getDeclarations()[i].accept(this);
    }
    for (int i = 0; i < item.getStatements().length; i++)
    {
      item.getStatements()[i].accept(this);
    }
  }

  @Override
  public void visit(Parameters item)
  {
    for (int i = 0; i < item.getNames().length; i++)
    {
      add(item.getTypes()[i], item.getNames()[i], ",");
    }
  }

  @Override
  public void visit(Declaration item)
  {
    //TODO type
    add("int ");
    for(String name : item.getNames())
      add(name, ",");
  }

  @Override
  public void visit(Assignment item)
  {
    add(item.getName());
    item.getExpression().accept(this);
  }

  @Override
  public void visit(Composite item)
  {
    for(Statement stmt : item.getStatements())
      stmt.accept(this);
  }

  @Override
  public void visit(IfThen item)
  {
    //TODO
  }

  @Override
  public void visit(IfThenElse item)
  {
    //TODO
  }

  @Override
  public void visit(While item)
  {
    //TODO
  }

  @Override
  public void visit(Read item)
  {
    add(item.getName(), "= read()");
  }

  @Override
  public void visit(Write item)
  {
    add("= write (");
    item.getExpression().accept(this);
    add(");");
  }

  @Override
  public void visit(Return item)
  {
    //TODO
  }

  @Override
  public void visit(Variable item)
  {
    //TODO type missing?
    add(item.getName());
  }

  @Override
  public void visit(Number item)
  {
    add(item.getValue());
  }

  @Override
  public void visit(Binary item)
  {
    item.getLhs().accept(this);
    add(item.getOperator());
    item.getRhs().accept(this);
  }

  @Override
  public void visit(Unary item)
  {
    add(item.getOperator());
    item.getOperand().accept(this);
  }

  @Override
  public void visit(Call item)
  {
    //TODO
  }

  @Override
  public void visit(True item)
  {
    add("true");
  }

  @Override
  public void visit(False item)
  {
    add("false");
  }

  @Override
  public void visit(BinaryCondition item)
  {
    item.getLhs().accept(this);
    add(item.getOperator());
    item.getRhs().accept(this);
  }

  @Override
  public void visit(Comparison item)
  {
    item.getLhs().accept(this);
    add(item.getOperator());
    item.getRhs().accept(this);
  }

  @Override
  public void visit(UnaryCondition item)
  {
    add(item.getOperator());
    item.getOperand().accept(this);
  }

  @Override
  public void visit(Empty item)
  {

  }

  private void add(Object... appendage)
  {
    //TODO .toString() unnecessary?
    for (Object obj : appendage)
    {
      result += obj + " ";
    }
  }
}
