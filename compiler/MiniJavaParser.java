package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniJavaParser
{
  // Methoden sind public wegen Test, vgl. @3453
  private String rawProgram;
  private String[] program;
  private int from;
  //Funktionsname -> Anzahl Argumente
  private Map<String, Integer> calls = new HashMap<>();

  public MiniJavaParser(String code)
  {
    from = 0;
    rawProgram = code;
  }

  public Program parse()
  {
    lex(rawProgram);
    return parseProgram();
  }

  public String[] lex(String program)
  {
    List<String> output = new ArrayList<>();
    int i = 0;
    String substring = program;
    while (substring.length() > 0)
    {
      substring = program.substring(i, program.length());
      Pattern p = Pattern.compile(""
          + "([\\s]*"
          + "(\\(|\\)|\\{|}|,|;" //Kontrollstrukturen
          + "|(\\d)+|([a-zA-Z][a-zA-Z0-9]*)" //Namen und Zahlen
          + "|\\+|-|\\*|/|%" //Operatoren
          + "|==?|!=?|<=?|>=?|&&|\\|\\|)" //Boolean Operatoren
          + "[\\s]*)");
      Matcher m = p.matcher(substring);
      if (!m.lookingAt())
      {
        System.err.println("From " + i + " on, no token could be found");
        break;
      }
      String group = m.group(2);
      output.add(group);
      i += m.end(1);
      if (i >= program.length())
      {
        break;
      }
    }
    for (int j = 0; j < 10; j++)
    {
      output.add("");
    }
    this.program = output.toArray(new String[output.size()]);
    return output.toArray(new String[output.size()]);
  }

  public Program parseProgram()
  {
    List<Function> moreFunkyFuncs = new ArrayList<>();
    Function funkyFunc = parseFunction();
    while (funkyFunc != null)
    {
      moreFunkyFuncs.add(funkyFunc);
      funkyFunc = parseFunction();
    }
    for (Function func : moreFunkyFuncs)
    {
      for (Entry<String, Integer> entry : calls.entrySet())
      {
        if (func.getName().equals(entry.getKey()))
        {
          if (func.getParameters().getNames().length != entry.getValue())
            throw new RuntimeException(
                "Invalid amount of calls\n" + func.getName() + " is called with "
                    + entry.getValue() + " parameters instead of " + func.getParameters()
                    .getNames().length);
        }
      }
    }
    return new Program(moreFunkyFuncs.toArray(new Function[0]));
  }

  public Function parseFunction()
  {
    Type type = parseType();
    String name = parseName();
    boolean isValid = Pattern.matches("\\(", program[from++]);
    Parameters params = parseParameters();
    isValid = isValid && Pattern.matches("\\)", program[from++])
        && Pattern.matches("\\{", program[from++]);
    int start = from;
    List<Declaration> decls = new ArrayList<>();
    Declaration decl = parseDecl();
    while (decl != null)
    {
      start = from;
      decls.add(decl);
      decl = parseDecl();
    }
    if(decls.size() == 0)
      decls.add(new Declaration(new String[0]));
    from = start;

    List<Statement> stmts = new ArrayList<>();
    Statement stmt = parseStatement();
    while (stmt != null)
    {
      start = from;
      stmts.add(stmt);
      stmt = parseStatement();
    }
    from = start;

    isValid = isValid && Pattern.matches("}", program[from++]);
    if (isValid)
      return new Function(type, name, params, decls.toArray(new Declaration[0]),
          stmts.toArray(new Statement[0]));
    return null;
  }

  private Parameters parseParameters()
  {
    int start = from;
    List<Type> types = new ArrayList<>();
    List<String> names = new ArrayList<>();
    Type type = parseType();
    String name = parseName();
    while (type != null && name != null)
    {
      start = from;
      types.add(type);
      names.add(name);
      if (!Pattern.matches(",", program[from++]))
        break;
      type = parseType();
      name = parseName();
    }
    from = start;
    return new Parameters(types.toArray(new Type[0]), names.toArray(new String[0]));
  }

  public Declaration parseDecl()
  {
    Type type = parseType();
    if (type == null)
      return null;

    String token;
    List<String> names = new ArrayList<>();
    String name;
    do
    {
      name = parseName();
      if (name == null)
        return null;
      names.add(name);
      if(program.length <= from)
        return null;
      token = program[from++];
    } while (Pattern.matches(",", token));
    if (Pattern.matches(";", token))
      return new Declaration(names.toArray(new String[0]));
    return null;
  }

  public Statement parseStatement()
  {
    int start = from;

    //;
    {
      from = start;
      if (Pattern.matches(";", program[from++]))
        return new Composite(new Statement[0]);
    }

    //{ stmt* }
    {
      from = start;
      if (Pattern.matches("\\{", program[from++]))
      {
        int beforeStatements = from;
        List<Statement> statements = new ArrayList<>();
        Statement statement = parseStatement();
        while (statement != null)
        {
          beforeStatements = from;
          statements.add(statement);
          statement = parseStatement();
        }
        from = beforeStatements;
        if (Pattern.matches("}", program[from++]))
          return new Composite(statements.toArray(new Statement[0]));
      }
    }

    // Name = expr;
    {
      from = start;
      String name = parseName();
      boolean isValid = Pattern.matches("=", program[from++]);
      Expression expr = parseExpression();
      isValid = isValid && Pattern.matches(";", program[from++]);
      if (isValid && name != null && expr != null)
        return new Assignment(name, expr);
    }

    //Name = read();
    {
      from = start;
      String name = parseName();
      boolean isValid = Pattern.matches("=", program[from++]);
      isValid = isValid && Pattern.matches("read", program[from++])
          && Pattern.matches("\\(", program[from++])
          && Pattern.matches("\\)", program[from++])
          && Pattern.matches(";", program[from++]);
      if (isValid && name != null)
        return new Read(name);
    }

    // write(expr)
    {
      from = start;
      boolean isValid = Pattern.matches("write", program[from++])
          && Pattern.matches("\\(", program[from++]);
      Expression expr = parseExpression();
      isValid = isValid && Pattern.matches("\\)", program[from++]);
      if (isValid && expr != null)
        return new Write(expr);
    }

    //If
    {
      from = start;
      boolean isValid = Pattern.matches("if", program[from++]);
      isValid = isValid && Pattern.matches("\\(", program[from++]);
      Condition cond = parseCondition();
      isValid = isValid && Pattern.matches("\\)", program[from++]);
      if (isValid && cond != null)
      {
        Statement stmt = parseStatement();
        int beforeElse = from;
        boolean isElse = Pattern.matches("else", program[from++]);
        Statement stmt2 = parseStatement();
        if (stmt != null)
        {
          if (isElse && stmt2 != null)
            return new IfThenElse(cond, stmt, stmt2);
          from = beforeElse;
          return new IfThen(cond, stmt);
        }
      }
    }

    //While
    {
      from = start;
      boolean isValid = Pattern.matches("while", program[from++])
          && Pattern.matches("\\(", program[from++]);
      Condition cond = parseCondition();
      isValid = isValid && Pattern.matches("\\)", program[from++]);
      if (isValid && cond != null)
      {
        Statement stmt = parseStatement();
        if (stmt != null)
          return new While(cond, stmt);
      }
    }

    //Return
    {
      from = start;
      boolean isValid = Pattern.matches("return", program[from++]);
      Expression expr = parseExpression();
      isValid = isValid && Pattern.matches(";", program[from++]);
      if (isValid && expr != null)
        return new Return(expr);
    }

    //Invalid Statement
    from = start;
    return null;
  }

  public Number parseNumber()
  {
    String token = program[from++];
    if (Pattern.matches("\\d+", token))
      return new Number(Integer.parseInt(token));
    return null;
  }

  public String parseName()
  {
    String token = program[from++];
    String[] invalidNames = new String[]{"if", "while", "else", "write", "read", "true", "false",
        "int"};
    if (Pattern.matches("[a-zA-Z]([a-zA-Z]|\\d)*", token))
    {
      for (String name : invalidNames)
      {
        if (name.equals(token))
          return null;
      }
      return token;
    }
    return null;
  }

  public Type parseType()
  {
    String token = program[from++];
    if (Pattern.matches("int", token))
    {
      return Type.Integer;
    }
    return null;
  }

  public Unop parseUnop()
  {
    String token = program[from++];
    if (Pattern.matches("-", token))
    {
      return Unop.Minus;
    }
    return null;
  }

  public Binop parseBinop()
  {
    String token = program[from++];
    if (Pattern.matches("[-]", token))
      return Binop.Minus;
    if (Pattern.matches("[+]", token))
      return Binop.Plus;
    if (Pattern.matches("[*]", token))
      return Binop.MultiplicationOperator;
    if (Pattern.matches("[/]", token))
      return Binop.DivisionOperator;
    if (Pattern.matches("[%]", token))
      return Binop.Modulo;
    return null;
  }

  public Comp parseComp()
  {
    String currentToken = program[from++];
    if (Pattern.matches("(==)", currentToken))
      return Comp.Equals;
    if (Pattern.matches("(!=)", currentToken))
      return Comp.NotEquals;
    if (Pattern.matches("(<=)", currentToken))
      return Comp.LessEqual;
    if (Pattern.matches("(<)", currentToken))
      return Comp.Less;
    if (Pattern.matches("(>=)", currentToken))
      return Comp.GreaterEqual;
    if (Pattern.matches("(>)", currentToken))
      return Comp.Greater;
    return null;
  }

  public Expression parseExpression()
  {
    int start = from;

    //expr binop expr
    Expression expr = parseBasicExpression();
    Binop binop = parseBinop();
    if (expr != null && binop != null)
    {
      Expression expr2 = parseExpression();
      if (expr2 != null)
        return new Binary(expr, binop, expr2);
    }
    from = start;

    return parseBasicExpression();
  }

  private Expression parseBasicExpression()
  {
    int start = from;

    //name (expr*)
    {
      from = start;
      String name = parseName();
      if (Pattern.matches("\\(", program[from++]))
      {
        int beforeExpressions = from;
        List<Expression> expressions = new ArrayList<>();
        Expression expr = parseExpression();
        while (expr != null)
        {
          beforeExpressions = from;
          expressions.add(expr);
          if (!Pattern.matches(",", program[from++]))
            break;
          expr = parseExpression();
        }
        from = beforeExpressions;
        if (name != null && Pattern.matches("\\)", program[from++]))
        {
          calls.put(name, expressions.size());
          return new Call(name, expressions.toArray(new Expression[0]));
        }
      }
    }

    //(expr)
    {
      from = start;
      if (Pattern.matches("\\(", program[from++]))
      {
        Expression expr = parseExpression();
        if (Pattern.matches("\\)", program[from++]))
          return expr;
      }
    }

    //number
    {
      from = start;
      Number number = parseNumber();
      if (number != null)
        return number;
    }

    //name
    {
      from = start;
      String name = parseName();
      if (name != null)
        return new Variable(name);
    }

    //unop expr
    {
      from = start;
      Unop unop = parseUnop();
      if (unop != null)
      {
        Expression expr = parseExpression();
        if (expr != null)
          return expr;
      }
    }

    //Invalid Expression
    from = start;
    return null;
  }

  public Bbinop parseBbinop()
  {
    String currentToken = program[from++];
    if (Pattern.matches("(&&)", currentToken))
      return Bbinop.And;
    if (Pattern.matches("(\\|\\|)", currentToken))
      return Bbinop.Or;
    return null;
  }

  public Bunop parseBunop()
  {
    String currentToken = program[from++];
    if (Pattern.matches("!", currentToken))
      return Bunop.Not;
    return null;
  }

  public Condition parseCondition()
  {
    int start = from;

    //cond bbinop cond
    {
      from = start;
      Condition cond = parseBasicCondition();
      Bbinop op = parseBbinop();
      if (cond != null && op != null)
      {
        Condition cond2 = parseCondition();
        if (cond2 != null)
          return new BinaryCondition(cond, op, cond2);
      }
    }

    from = start;
    return parseBasicCondition();
  }

  private Condition parseBasicCondition()
  {
    int start = from;

    //expr comp expr
    {
      from = start;
      Expression expr = parseExpression();
      Comp comp = parseComp();
      Expression expr2 = parseExpression();
      if (expr != null && comp != null && expr2 != null)
        return new Comparison(expr, comp, expr2);
    }

    //bunop (cond)
    {
      from = start;
      Bunop bunop = parseBunop();
      boolean brace = Pattern.matches("\\(", program[from++]);
      if (brace)
      {
        Condition cond = parseCondition();
        brace = Pattern.matches("\\)", program[from++]);
        if (bunop != null && brace && cond != null)
          return new UnaryCondition(bunop, cond);
      }
    }

    //(cond)
    {
      from = start;
      boolean brace = Pattern.matches("\\(", program[from++]);
      if (brace)
      {
        Condition cond = parseCondition();
        brace = Pattern.matches("\\)", program[from++]);
        if (brace && cond != null)
          return cond;
      }
    }

    //true
    {
      from = start;
      if (Pattern.matches("true", program[from++]))
        return new True();
    }

    //false
    {
      from = start;
      if (Pattern.matches("false", program[from++]))
        return new False();
    }

    //Invalid Condition
    from = start;
    return null;
  }
}//UTF-8 Encoded ä
