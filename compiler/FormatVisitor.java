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

  }

  @Override
  public void visit(Function item)
  {

  }

  @Override
  public void visit(Parameters item)
  {

  }

  @Override
  public void visit(Declaration item)
  {

  }

  @Override
  public void visit(Assignment item)
  {

  }

  @Override
  public void visit(Composite item)
  {

  }

  @Override
  public void visit(IfThen item)
  {

  }

  @Override
  public void visit(IfThenElse item)
  {

  }

  @Override
  public void visit(While item)
  {

  }

  @Override
  public void visit(Read item)
  {

  }

  @Override
  public void visit(Write item)
  {

  }

  @Override
  public void visit(Return item)
  {

  }

  @Override
  public void visit(Variable item)
  {

  }

  @Override
  public void visit(Number item)
  {

  }

  @Override
  public void visit(Binary item)
  {

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
