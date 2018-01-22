package compiler;

import compiler.nodes.Program;

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
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
