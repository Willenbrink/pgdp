package compiler;

public class FormatVisitor implements Visitor
{
  /*
  0. Number
  1. True/False (bool)
  2. Negation, Multiplikation, Division
  3. And
  4. Modulo
  5. Or
  6. Minus

  8. Plus


  0: Number, True/False
1: Negation
2:  &&
3:||
2. Comparison turns to boolean and does not have any priority
   */

  //Priorities
  public static int number = 0;
  public static int negation = 2;
  public static int multiplication = 2;
  public static int modulo = 4;
  public static int division = 2;
  public static int subtraction = 6; //TODO substraction subtraction
  public static int addition = 6;
  public static int bool = 1;
  public static int and = 3;
  public static int or = 5;
  public static int comparison = 3;
  public static int call = -1;

  String result;
  int depth; //TODO indentation
  //TODO priority in stmt not allowed

  public FormatVisitor()
  {
    result = "";
  }

  public String getResult()
  {
    //TODO
    System.out.println(result);
    return result;
    //return result.substring(0, result.length()-1);
  }

  @Override
  public void visit(Program item)
  {
    for(Function func : item.getFunctions())
      check(func);
  }

  @Override
  public void visit(Function item)
  {
    add(item.getType(), item.getName());
    item.getParameters().accept(this);
    for (int i = 0; i < item.getDeclarations().length; i++)
    {
      check(item.getDeclarations()[i]);
    }
    for (int i = 0; i < item.getStatements().length; i++)
    {
      check(item.getStatements()[i]);
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
      check(stmt);
    /* Results in incorrect indentation
    if()
      {
        bla();
      }
    else
     */
  }

  @Override
  public void visit(IfThen item)
  {
    add("if(");
    item.getCond().accept(this);
    add(")\n");
    item.getThenBranch().accept(this);
  }

  @Override
  public void visit(IfThenElse item)
  {
    //TODO newlines missing at most places
    add("if(");
    item.getCond().accept(this);
    add(")\n");
    item.getThenBranch().accept(this);
    add("else");
    item.getElseBranch().accept(this);
  }

  @Override
  public void visit(While item)
  {
    add("while(");
    item.getCond().accept(this);
    add(")\n");
    item.getBody().accept(this);
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
    add("return ");
    item.getExpression().accept(this);
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
    check(item.getLhs(), item.firstLevelPriority(), true);
    addS(item.getOperator());
    check(item.getRhs(), item.firstLevelPriority(), false);
  }

  @Override
  public void visit(Unary item)
  {
    add(item.getOperator());
    check(item.getOperand(), item.firstLevelPriority(), false);
  }

  @Override
  public void visit(Call item)
  {
    add(item.getFunctionName());
    add("(");
    for (int i = 0; i < item.getArguments().length; i++)
    {
      item.getArguments()[i].accept(this);
      if(i != item.getArguments().length-1)
        add(", ");
    }
    add(")");
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
    check(item.getLhs(), item.firstLevelPriority(), true);
    addS(item.getOperator());
    check(item.getRhs(), item.firstLevelPriority(), false);
  }

  @Override
  public void visit(Comparison item)
  {
    check(item.getLhs(), item.firstLevelPriority(), true);
    addS(item.getOperator());
    check(item.getRhs(), item.firstLevelPriority(), false);
  }

  @Override
  public void visit(UnaryCondition item)
  {
    add(item.getOperator());
    check(item.getOperand(), item.firstLevelPriority(), false);
  }

  @Override
  public void visit(Empty item)
  {

  }

  private void check(Expression side, int priority, boolean isLeft)
  {
    boolean isBraced;
    if(isLeft)
      isBraced = side.firstLevelPriority() > priority;
    else
      isBraced = side.firstLevelPriority() >= priority;
    if(isBraced)
    {
      //Winter is coming
      add("(");
      side.accept(this);
      add(")");
      return;
    }
    side.accept(this);
  }

  private void check(Condition side, int priority, boolean isLeft)
  {
    boolean isBraced;
    if(isLeft)
      isBraced = side.firstLevelPriority() > priority;
    else
      isBraced = side.firstLevelPriority() >= priority;
    if(isBraced)
    {
      //Winter is coming
      add("(");
      side.accept(this);
      add(")");
      return;
    }
    side.accept(this);
  }

  private void check(Statement side)
  {
    depth++;
    side.accept(this);
    depth--;
  }

  private void check(Declaration side)
  {
    depth++;
    side.accept(this);
    depth--;
  }

  private void check(Function side)
  {
    depth++;
    side.accept(this);
    depth--;
  }

  private void add(Object... appendage)
  {
    for (Object obj : appendage)
    {
      result += obj.toString() + "";
    }
  }

  private void addS(Object appendage)
  {
    result += " " + appendage.toString() + " ";
  }
}
