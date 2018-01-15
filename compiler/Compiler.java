package compiler;

public class Compiler
{
  public static int[] compile(String code)
  {
    MiniJavaParser parser = new MiniJavaParser(code);
    Program tree = parser.parse();
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    tree.accept(cgv);
    int[] program = cgv.getProgram();
    //Interpreter.execute(program);
    return program;
  }
}
//UTF-8 Encoded ä
