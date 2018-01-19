package compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FormatTest {

  @Test
  public void testProgramsSameLine()
  {
    String[] codesWithBraceOnSameLine = {
        //ggt
        "int ggt(int a, int b) {\n" +
            "  int temp;\n" +
            "  if(b > a) {\n" +
            "    temp = a;\n" +
            "    a = b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  while(a != 0) {\n" +
            "    temp = a;\n" +
            "    a = a % b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  return b;\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  int a, b, r;\n" +
            "  a = 3528;\n" +
            "  b = 3780;\n" +
            "  r = ggt(a, b);\n" +
            "  return r;\n" +
            "}",
        //fak
        "int fak(int n) {\n" +
            "  if(n == 0)\n" +
            "    return 1;\n" +
            "  return n * fak(n - 1);\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  return fak(6);\n" +
            "}\n",
        //prim-test
        "int prim(int n) {\n" +
            "  int divisors, i;\n" +
            "  divisors = 0;\n" +
            "  \n" +
            "  i = 2;\n" +
            "  while(i < n) {\n" +
            "    if(n % i == 0)\n" +
            "      divisors = divisors + 1;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  \n" +
            "  if(divisors == 0 && n >= 2) {\n" +
            "    return 1;\n" +
            "  }\n" +
            "  else\n" +
            "  {\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  int prims;\n" +
            "  prims = 0;\n" +
            "  prims = prims + prim(997);\n" +
            "  prims = prims + prim(120);\n" +
            "  prims = prims + prim(887);\n" +
            "  prims = prims + prim(21);\n" +
            "  prims = prims + prim(379);\n" +
            "  prims = prims + prim(380);\n" +
            "  prims = prims + prim(757);\n" +
            "  prims = prims + prim(449);\n" +
            "  prims = prims + prim(5251);\n" +
            "  return prims;\n" +
            "}"
        };

    for(String code : codesWithBraceOnSameLine)
    {
      MiniJavaParser parser = new MiniJavaParser(code);
      Program program = parser.parse();
      FormatVisitor fv = new FormatVisitor();
      program.accept(fv);
      //System.out.println(fv.getResult());
      assertEquals(code, fv.getResult());
    }
  }

  @Test
  public void testProgramsNextLine()
  {
    String[] codesWithBraceOnNextLine = {
        //ggt
        "int ggt(int a, int b)\n{\n" +
            "  int temp;\n" +
            "  if(b > a)\n" +
            "  {\n" +
            "    temp = a;\n" +
            "    a = b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  while(a != 0)\n" +
            "  {\n" +
            "    temp = a;\n" +
            "    a = a % b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  return b;\n" +
            "}\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "  int a, b, r;\n" +
            "  a = 3528;\n" +
            "  b = 3780;\n" +
            "  r = ggt(a, b);\n" +
            "  return r;\n" +
            "}",
        //fak
        "int fak(int n)\n" +
            "{\n" +
            "  if(n == 0)\n" +
            "    return 1;\n" +
            "  return n * fak(n - 1);\n" +
            "}\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "  return fak(6);\n" +
            "}",
        //prim-test
        "int prim(int n)\n" +
            "{\n" +
            "  int divisors, i;\n" +
            "  divisors = 0;\n" +
            "  i = 2;\n" +
            "  while(i < n)\n" +
            "  {\n" +
            "    if(n % i == 0)\n" +
            "      divisors = divisors + 1;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  if(divisors == 0 && n >= 2)\n" +
            "  {\n" +
            "    return 1;\n" +
            "  }\n" +
            "  else\n" +
            "  {\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "  int prims;\n" +
            "  prims = 0;\n" +
            "  prims = prims + prim(997);\n" +
            "  prims = prims + prim(120);\n" +
            "  prims = prims + prim(887);\n" +
            "  prims = prims + prim(21);\n" +
            "  prims = prims + prim(379);\n" +
            "  prims = prims + prim(380);\n" +
            "  prims = prims + prim(757);\n" +
            "  prims = prims + prim(449);\n" +
            "  prims = prims + prim(5251);\n" +
            "  return prims;\n" +
            "}"
    };

    for(String code : codesWithBraceOnNextLine)
    {
      MiniJavaParser parser = new MiniJavaParser(code);
      Program program = parser.parse();
      FormatVisitor fv = new FormatVisitor();
      program.accept(fv);
      //System.out.println(fv.getResult());
      assertEquals(code, fv.getResult());
    }
  }

  @Test
  public void testExpression1() {
    Expression exp = new Binary(
        new Binary(new Number(99), Binop.Plus, new Number(11)),
        Binop.Minus,
        new Binary(new Variable("a"), Binop.Plus, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 + 11 - (a + 1)", visitor.getResult());
  }

  @Test
  public void testExpression2() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Plus, new Number(11)), Binop.Plus,
        new Binary(new Variable("a"), Binop.Plus, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 + 11 + a + 1", visitor.getResult());
  }

  @Test
  public void testExpression3() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Plus, new Number(11)), Binop.Plus,
        new Binary(new Variable("a"), Binop.MultiplicationOperator, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 + 11 + a * 1", visitor.getResult());
  }

  @Test
  public void testExpression4() {
    Expression exp = new Unary(Unop.Minus,
        new Binary(new Number(99), Binop.Plus, new Number(11)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("-(99 + 11)", visitor.getResult());
  }

  @Test
  public void testExpression5() {
    Expression exp = new Unary(Unop.Minus,
        new Binary(new Number(99), Binop.MultiplicationOperator, new Number(11)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("-99 * 11", visitor.getResult());
  }

  //TODO
  /*
  @Test
  public void testExpression6() {
    Expression exp = new ArrayAccess(new ArrayInitializer(new Number(99)), new Number(3));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("(new int[99])[3]", visitor.getResult());
  }
  */

  @Test
  public void testExpression7() {
    Expression exp = new Binary(
        new Binary(
            new Number(99), Binop.DivisionOperator, new Number(11)),
        Binop.DivisionOperator,
        new Binary(
            new Variable("a"), Binop.DivisionOperator, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 / 11 / (a / 1)", visitor.getResult());
  }

  @Test
  public void testExpression8() {
    Expression exp = new Binary(
        new Binary(new Number(99), Binop.DivisionOperator,
            new Call("hugo", new Expression[] {new Number(1), new Variable("b")})),
        Binop.DivisionOperator,
        new Binary(new Variable("a"), Binop.DivisionOperator, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 / hugo(1, b) / (a / 1)", visitor.getResult());
  }

  @Test
  public void testExpression9() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Plus, new Number(11)),
        Binop.MultiplicationOperator, new Binary(new Variable("a"), Binop.Plus, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("(99 + 11) * (a + 1)", visitor.getResult());
  }

  @Test
  public void testExpression10() {
    Expression exp =
        new Binary(new Binary(new Number(99), Binop.MultiplicationOperator, new Number(11)),
            Binop.MultiplicationOperator, new Binary(new Variable("a"), Binop.Plus, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("99 * 11 * (a + 1)", visitor.getResult());
  }

  @Test
  public void testExpression11() {
    Expression exp = new Binary(new Binary(new Number(99), Binop.Minus, new Number(11)),
        Binop.MultiplicationOperator,
        new Binary(new Variable("a"), Binop.MultiplicationOperator, new Number(1)));
    FormatVisitor visitor = new FormatVisitor();
    exp.accept(visitor);
    assertEquals("(99 - 11) * a * 1", visitor.getResult());
  }

  @Test
  public void testCondition1() {
    Condition cond = new BinaryCondition(new True(), Bbinop.And,
        new BinaryCondition(new False(), Bbinop.And, new True()));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("true && false && true", visitor.getResult());
  }

  @Test
  public void testCondition2() {
    Condition cond = new BinaryCondition(new True(), Bbinop.And,
        new BinaryCondition(new False(), Bbinop.Or, new True()));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("true && (false || true)", visitor.getResult());
  }

  @Test
  public void testCondition3() {
    Condition cond =
        new BinaryCondition(new Comparison(new Number(2), Comp.Greater, new Variable("a")),
            Bbinop.Or, new BinaryCondition(new False(), Bbinop.And, new True()));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("2 > a || false && true", visitor.getResult());
  }

  @Test
  public void testCondition4() {
    Condition cond =
        new UnaryCondition(Bunop.Not, new Comparison(new Number(1), Comp.Equals, new Number(2)));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("!(1 == 2)", visitor.getResult());
  }

  @Test
  public void testCondition5() {
    Condition cond = new UnaryCondition(Bunop.Not,
        new BinaryCondition(new Comparison(new Number(2), Comp.Greater, new Variable("a")),
            Bbinop.Or, new BinaryCondition(new False(), Bbinop.And, new True())));
    FormatVisitor visitor = new FormatVisitor();
    cond.accept(visitor);
    assertEquals("!(2 > a || false && true)", visitor.getResult());
  }
}
