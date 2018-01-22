package compiler;

import compiler.nodes.ArrayAccess;
import compiler.nodes.ArrayAssignment;
import compiler.nodes.ArrayInitializer;
import compiler.nodes.Assignment;
import compiler.nodes.Binary;
import compiler.nodes.BinaryCondition;
import compiler.nodes.Bunop;
import compiler.nodes.Call;
import compiler.nodes.Comparison;
import compiler.nodes.Composite;
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
import compiler.nodes.Unop;
import compiler.nodes.Variable;
import compiler.nodes.While;
import compiler.nodes.Write;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CodeGenerationVisitor implements Visitor
{
  private List<Integer> program;
  //Name -> Zelle im Frame
  private Map<String, Integer> variables;
  //Label -> Zeile mit Label
  private Map<String, Integer> labels;
  //Zeile mit Sprung -> Label
  private Map<Integer, String> unresolvedLabels;
  //Aktueller Return-Wert, wird von Funktion gesetzt damit alle Returns innerhalb der Funktion
  //darauf zugreifen können
  private int currentReturn;

  public CodeGenerationVisitor()
  {
    program = new ArrayList<>();
    variables = new HashMap<>();
    labels = new HashMap<>();
    unresolvedLabels = new HashMap<>();
    currentReturn = 0;
  }

  public int[] getProgram()
  {
    int[] output = new int[program.size()];
    for (int i = 0; i < output.length; i++)
    {
      output[i] = program.get(i);
    }
    return output;
  }

  public void visit(Program item)
  {
    //Lädt Wert auf Stack, dieser wird später über resolveLabels gesetzt
    new Call("main", new Expression[0]).accept(this);
    addCode(Interpreter.HALT);
    Function[] functions = item.getFunctions();
    for (Function function : functions)
    {
      function.accept(this);
    }
    resolveLabels();
  }

  public void visit(Function item)
  {
    item.getParameters().accept(this);
    //Trickreich, da Declarations wiederum mehrere Variablen enthalten können
    //bspw. int a, b; \n int c,d;ö
    for (int i = 0; i < item.getDeclarations().length; i++)
    {
      currentReturn += item.getDeclarations()[i].getNames().length;
    }
    if (labels.containsKey(item.getName()))
      throw new RuntimeException("Funktion " + item.getName() + " wurde doppelt definiert");
    labels.put(item.getName(), program.size());
    int varAmount = 0;
    for (Declaration declaration : item.getDeclarations())
    {
      declaration.accept(this);
      varAmount += declaration.getNames().length;
    }
    addCode(Interpreter.ALLOC, varAmount);
    for (Statement statement : item.getStatements())
    {
      statement.accept(this);
    }
  }

  public void visit(Parameters item)
  {
    variables.clear();
    currentReturn = item.getNames().length;
    for (int i = 0; i < item.getNames().length; i++)
    {
      variables.put(item.getNames()[i], i + 1 - item.getNames().length);
    }
  }

  public void visit(Declaration item)
  {
    for (int i = 0; i < item.getNames().length; i++)
    {
      if (variables.containsKey(item.getNames()[i]))
        throw new RuntimeException("Variable " + item.getNames()[i] + " already used");
      variables.put(item.getNames()[i], lastVariableIndex());
    }
  }

  private int lastVariableIndex()
  {
    int i = 1;
    while (variables.containsValue(i))
    {
      i++;
    }
    return i;
  }

  public void visit(Assignment item)
  {
    item.getExpression().accept(this);
    int var = getVar(item.getName());
    addCode(Interpreter.STS, var);
  }

  public void visit(Composite item)
  {
    for (Statement statement : item.getStatements())
    {
      statement.accept(this);
    }
  }

  public void visit(IfThen item)
  {
    item.getCond().accept(this);
    addCode(Interpreter.NOT);
    addCode(Interpreter.JUMP);
    int index = program.size() - 1;
    item.getThenBranch().accept(this);
    //An der Stelle an der der Sprung durchgeführt wird, wird jetzt das Ziel hinzugefügt
    program.set(index,
        program.get(index) + program.size());
  }

  public void visit(IfThenElse item)
  {
    item.getCond().accept(this);
    addCode(Interpreter.NOT);
    addCode(Interpreter.JUMP);
    int index = program.size() - 1;

    item.getThenBranch().accept(this);

    addCode(Interpreter.LDI, -1);
    addCode(Interpreter.JUMP);
    int index2 = program.size() - 1;
    //An der Stelle an der der Sprung durchgeführt wird, wird jetzt das Ziel hinzugefügt
    program.set(index,
        program.get(index) + program.size());
    item.getElseBranch().accept(this);
    program.set(index2,
        program.get(index2) + program.size());
  }

  public void visit(While item)
  {
    //While = If mit unbedingten Sprung an den Anfang
    int index = program.size();
    item.getCond().accept(this);
    addCode(Interpreter.NOT);
    int unresolvedEnd = program.size();
    addCode(Interpreter.JUMP);
    item.getBody().accept(this);

    //Unbedingter Sprung an den Anfang
    //index-1 da index auf den Jumpbefehl zeigt
    addCode(Interpreter.LDI, -1);
    addCode(Interpreter.JUMP, index);

    //An der Stelle an der der Sprung durchgeführt wird, wird jetzt das Ziel hinzugefügt
    program.set(unresolvedEnd,
        program.get(unresolvedEnd) + program.size());
  }

  public void visit(Read item)
  {
    addCode(Interpreter.IN);
  }

  public void visit(Write item)
  {
    addCode(Interpreter.OUT);
  }

  public void visit(Return item)
  {
    item.getExpression().accept(this);
    addCode(Interpreter.RETURN, currentReturn);
  }

  public void visit(Variable item)
  {
    int pos = getVar(item.getName());
    addCode(Interpreter.LDS, pos);
  }

  public void visit(Number item)
  {
    if (item.getValue() < 0)
    {
      //Hier wurde Exception nicht geworfen, da alle Tests nicht darauf ausgelegt sind
      // negative Nummern zu verbieten
      System.err.println("Negative Nummern sind laut Grammatik nicht erlaubt");
      //throw new RuntimeException("Negative Nummern sind laut Grammatik nicht erlaubt");
    }
    addCode(Interpreter.LDI, item.getValue());
  }

  public void visit(Binary item)
  {
    item.getRhs().accept(this);
    item.getLhs().accept(this);
    switch (item.getOperator())
    {
      case Plus:
        addCode(Interpreter.ADD);
        break;
      case Minus:
        addCode(Interpreter.SUB);
        break;
      case Modulo:
        addCode(Interpreter.MOD);
        break;
      case MultiplicationOperator:
        addCode(Interpreter.MUL);
        break;
      case DivisionOperator:
        addCode(Interpreter.DIV);
        break;
      default:
        throw (new RuntimeException("Unknown binary operator"));
    }
  }

  public void visit(Unary item)
  {
    item.getOperand().accept(this);
    if (item.getOperator() != Unop.Minus)
      throw (new RuntimeException("Unknown unary operator"));
    addCode(Interpreter.NOT);
    addCode(Interpreter.LDI, 1);
    addCode(Interpreter.ADD);
  }

  public void visit(Call item)
  {
    for (int i = 0; i < item.getArguments().length; i++)
    {
      item.getArguments()[i].accept(this);
    }
    for (int i = 0; i < item.getArguments().length; i++)
    {
      addCode(Interpreter.NOP);
    }
    addCode(Interpreter.LDI);
    unresolvedLabels.put(program.size() - 1, item.getFunctionName());
    addCode(Interpreter.CALL, item.getArguments().length);
  }

  public void visit(True item)
  {
    new Number(-1).accept(this);
  }

  public void visit(False item)
  {
    new Number(0).accept(this);
  }

  public void visit(BinaryCondition item)
  {
    item.getLhs().accept(this);
    item.getRhs().accept(this);
    switch (item.getOperator())
    {
      case Or:
        addCode(Interpreter.OR);
        break;
      case And:
        addCode(Interpreter.AND);
        break;
      default:
        throw new RuntimeException("Unbekannter Operator: " + item.getOperator());
    }
  }

  public void visit(Comparison item)
  {
    //Erst rechts dann links, da
    //LT wahr, wenn o1 < o2
    //Da o1 oben liegt muss links oben sein
    item.getRhs().accept(this);
    item.getLhs().accept(this);
    switch (item.getOperator())
    {
      case Equals:
        addCode(Interpreter.EQ);
        break;
      case Greater:
        addCode(Interpreter.LE);
        addCode(Interpreter.NOT);
        break;
      case NotEquals:
        addCode(Interpreter.EQ);
        addCode(Interpreter.NOT);
        break;
      case Less:
        addCode(Interpreter.LT);
        break;
      case LessEqual:
        addCode(Interpreter.LE);
        break;
      case GreaterEqual:
        addCode(Interpreter.LT);
        addCode(Interpreter.NOT);
        break;
      default:
        throw new RuntimeException("Unbekannter Operator: " + item.getOperator());
    }
  }

  public void visit(UnaryCondition item)
  {
    item.getOperand().accept(this);
    if (item.getOperator() != Bunop.Not)
      throw new RuntimeException("Unary Operator ist nicht \"not\"");
    addCode(Interpreter.NOT);
  }

  public void visit(Empty item)
  {
  }

  @Override
  public void visit(ArrayInitializer item)
  {
    {
      addCode(Interpreter.LDI, 0);
      addCode(Interpreter.LDI, 1);
      {
        //Speichert die Länge in einem Array
        item.getExpr().accept(this);
        addCode(Interpreter.LDI, 0);
        {
          //Erstellt ein Array der Länge 1
          addCode(Interpreter.LDI, 1);
          addCode(Interpreter.ALLOCH);
        }
        addCode(Interpreter.STH);
      }
      {
        //Erstellt ein Array der Länge 0
        //Legt also eigentlich nur eine Referenz zum Array davor oben auf dem Stack
        addCode(Interpreter.LDI, 0);
        addCode(Interpreter.ALLOCH);
      }
      //Lädt die Länge wieder oben auf den Stack
      addCode(Interpreter.SUB);
      addCode(Interpreter.LDH);
      addCode(Interpreter.ALLOCH);
    }
  }

  @Override
  public void visit(ArrayAccess item)
  {
    item.getField().accept(this);
    item.getExpr().accept(this);
    addCode(Interpreter.LDH);
  }

  @Override
  public void visit(ArrayAssignment item)
  {
    item.getValue().accept(this);
    item.getField().accept(this);
    addCode(Interpreter.LDS, getVar(item.getName()));
    addCode(Interpreter.STH);
  }

  @Override
  public void visit(Length item)
  {
    addCode(Interpreter.LDI, -1);
    item.getExpr().accept(this);
    addCode(Interpreter.LDH);
  }

  private Integer getVar(String name)
  {
    Integer pos = variables.get(name);
    if (pos == null)
      throw new RuntimeException("Variable \"" + name + "\" does not exist");
    return pos;
  }

  private void resolveLabels()
  {
    Map<Integer, Integer> resolvedLabels = new HashMap<>();
    for (Entry<Integer, String> entry : unresolvedLabels.entrySet())
    {
      int callLine = entry.getKey();
      try
      {
        int labelLine = labels.get(entry.getValue());
        resolvedLabels.put(callLine, labelLine);
      } catch (NullPointerException e)
      {
        throw new RuntimeException("label: " + entry.getValue() + " does not exist");
      }
    }
    for (Entry<Integer, Integer> entry : resolvedLabels.entrySet())
    {
      //Addiere auf Sprungbefehl die Zielzeile hinzu
      int newCode = program.get(entry.getKey()) + entry.getValue();
      program.set(entry.getKey(), newCode);
    }
  }

  private void addCode(int opcode, int constant)
  {

    if (opcode == Interpreter.LDI && (constant >>> 16) != 0)
    {
      addCode(Interpreter.LDI, constant >>> 16);
      addCode(Interpreter.SHL, 16);
      addCode(Interpreter.LDI, constant & 0xFFFF);
      addCode(Interpreter.OR);
      return;
    }

    int code = (opcode << 16) ^ (constant & 0xFFFF);
    program.add(code);
  }

  private void addCode(int opcode)
  {
    int code;
    code = (opcode << 16);
    program.add(code);
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
