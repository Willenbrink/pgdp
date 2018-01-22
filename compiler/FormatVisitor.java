package compiler;

import compiler.nodes.ArrayAccess;
import compiler.nodes.ArrayAssignment;
import compiler.nodes.ArrayInitializer;
import compiler.nodes.Assignment;
import compiler.nodes.Binary;
import compiler.nodes.BinaryCondition;
import compiler.nodes.Call;
import compiler.nodes.Comparison;
import compiler.nodes.Composite;
import compiler.nodes.Condition;
import compiler.nodes.Declaration;
import compiler.nodes.Empty;
import compiler.nodes.Expression;
import compiler.nodes.False;
import compiler.nodes.Function;
import compiler.nodes.IfThen;
import compiler.nodes.IfThenElse;
import compiler.nodes.Length;
import compiler.nodes.Number;
import compiler.nodes.Parameters;
import compiler.nodes.Program;
import compiler.nodes.Read;
import compiler.nodes.Return;
import compiler.nodes.Statement;
import compiler.nodes.True;
import compiler.nodes.Unary;
import compiler.nodes.UnaryCondition;
import compiler.nodes.Variable;
import compiler.nodes.While;
import compiler.nodes.Write;

public class FormatVisitor implements Visitor
{
  //Priorities
  public static int call = 0; //or any other 0-value statement e.g. array

  //Binary
  public static int bool = 0;
  public static int negation = 2;
  public static int comparison = 3;
  public static int and = 4;
  public static int or = 5;

  //Arithmetic
  public static int number = 0; //or name
  public static int modulo = 1;
  public static int division = 2;
  public static int multiplication = 3;
  public static int subtraction = 4;
  public static int addition = 5;

  private String result;
  private int depth;
  static final boolean braceOnNextLine = true;

  public FormatVisitor()
  {
    result = "";
  }

  public String getResult()
  {
    //Entfernt alle \n am Ende des Programms
    for (int i = result.length()-1; i > 0; i--)
    {
      if(result.charAt(i) == '\n')
        result = result.substring(0, i);
      else
        break;
    }
    System.out.println(result);
    return result;
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
    add(item.getType(), " ", item.getName());
    item.getParameters().accept(this);
    add("{\n");
    for (int i = 0; i < item.getDeclarations().length; i++)
    {
      check(item.getDeclarations()[i]);
    }
    for (int i = 0; i < item.getStatements().length; i++)
    {
      check(item.getStatements()[i]);
    }
    add("}\n\n");
  }

  @Override
  public void visit(Parameters item)
  {
    add("(");
    for (int i = 0; i < item.getNames().length; i++)
    {
      add(item.getTypes()[i], " ", item.getNames()[i]);
      if(i < item.getNames().length-1)
        sep();
    }
    add(")");
    checkBrace();
  }

  @Override
  public void visit(Declaration item)
  {
    //TODO anpassen für Arrays
    //Wurde für diese Abgabe noch nicht gemacht, wird eventuell später noch gemacht
    //wenn noch mehr Typen hinzugefügt werden
    if(item.getNames().length == 0)
      return;
    add("int ");
    for (int i = 0; i < item.getNames().length; i++)
    {
      add(item.getNames()[i]);
      if(i < item.getNames().length-1)
        sep();
      else
        end();
    }
  }

  @Override
  public void visit(Assignment item)
  {
    add(item.getName());
    addSpaced("=");
    item.getExpression().accept(this);
    add(";\n");
  }

  @Override
  public void visit(Composite item)
  {
    add("{\n");
    for(Statement stmt : item.getStatements())
      check(stmt);
    add("}\n");
  }

  @Override
  public void visit(IfThen item)
  {
    add("if (");
    item.getCond().accept(this);
    add(")");
    checkBrace();
    boolean isComposite = item.getThenBranch() instanceof Composite;
    if(!isComposite)
      depth++;
    item.getThenBranch().accept(this);
    if(!isComposite)
      depth--;
  }

  @Override
  public void visit(IfThenElse item)
  {
    add("if (");
    item.getCond().accept(this);
    add(")");
    checkBrace();
    boolean isComposite = item.getThenBranch() instanceof Composite;
    if(!isComposite)
      depth++;
    item.getThenBranch().accept(this);
    if(!isComposite)
      depth--;
    add("else");
    checkBrace();
    isComposite = item.getElseBranch() instanceof Composite;
    if(!isComposite)
      depth++;
    item.getElseBranch().accept(this);
    if(!isComposite)
      depth--;
  }

  @Override
  public void visit(While item)
  {
    add("while (");
    item.getCond().accept(this);
    add(")");
    checkBrace();
    boolean isComposite = item.getBody() instanceof Composite;
    if(!isComposite)
      depth++;
    item.getBody().accept(this);
    if(!isComposite)
      depth--;
  }

  @Override
  public void visit(Read item)
  {
    add(item.getName());
    addSpaced("=");
    add("read()");
    end();
  }

  @Override
  public void visit(Write item)
  {
    add("= write (");
    item.getExpression().accept(this);
    add(")");
    end();
  }

  @Override
  public void visit(Return item)
  {
    add("return ");
    item.getExpression().accept(this);
    end();
  }

  @Override
  public void visit(Variable item)
  {
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
    addSpaced(item.getOperator());
    check(item.getRhs(), item.firstLevelPriority(), false);
  }

  @Override
  public void visit(Unary item)
  {
    add(item.getOperator());
    check(item.getOperand(), item.firstLevelPriority(), true);
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
  public void visit(Length item)
  {
    add("length (");
    item.getExpr().accept(this);
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
    check(item.getLhs(), item.firstLevelPriority());
    addSpaced(item.getOperator());
    check(item.getRhs(), item.firstLevelPriority());
  }

  @Override
  public void visit(Comparison item)
  {
    check(item.getLhs(), item.firstLevelPriority(), true);
    addSpaced(item.getOperator());
    check(item.getRhs(), item.firstLevelPriority(), false);
  }

  @Override
  public void visit(UnaryCondition item)
  {
    add(item.getOperator());
    check(item.getOperand(), item.firstLevelPriority());
  }

  @Override
  public void visit(Empty item)
  {

  }

  @Override
  public void visit(ArrayInitializer item)
  {
    add("new int[");
    check(item.getExpr(), item.firstLevelPriority(), true);
    add("]");
  }

  @Override
  public void visit(ArrayAccess item)
  {
    check(item.getExpr(), item.firstLevelPriority(), false);
    add("[");
    check(item.getField(), item.firstLevelPriority(), true);
    add("]");
  }

  @Override
  public void visit(ArrayAssignment item)
  {
    add(item.getName(), " [");
    item.getField().accept(this);
    add("] = ");
    item.getValue().accept(this);
    add(";");
  }

  private void check(Expression side, int priority, boolean isLeft)
  {
    boolean isBraced;
    if(isLeft)
      isBraced = (side.firstLevelPriority() >> 1) > (priority >> 1);
    else
      isBraced = side.firstLevelPriority() > priority
          || side.firstLevelPriority() == priority
          && priority % 2 == 0;

    if(isBraced) //Winter is coming
      brace(side);
    else
      side.accept(this);
  }

  private void check(Condition side, int priority)
  {
    boolean isBraced = side.firstLevelPriority() > priority;

    if(isBraced) //Winter is coming
      brace(side);
    else
      side.accept(this);
  }

  private void brace(Expression item)
  {
    add("(");
    item.accept(this);
    add(")");
  }

  private void brace(Condition item)
  {
    add("(");
    item.accept(this);
    add(")");
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
    side.accept(this);
  }

  private void add(Object... objs)
  {
    if(depth > 0 && result.charAt(result.length()-1) == '\n')
      for (int i = 0; i < depth; i++)
      {
        result += "  ";
      }
    for(Object obj : objs)
      result += obj.toString();
  }

  private void addSpaced(Object obj)
  {
    if(depth > 0 && result.charAt(result.length()-1) == '\n')
      for (int i = 0; i < depth; i++)
      {
        result += "  ";
      }
    result += " " + obj.toString() + " ";
  }

  private void sep()
  {
    result += ", ";
  }

  private void end()
  {
    result += ";\n";
  }

  private void checkBrace()
  {
    if(braceOnNextLine)
      add("\n");
    else
      add(" ");
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
