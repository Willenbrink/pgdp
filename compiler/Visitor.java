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
import compiler.nodes.Declaration;
import compiler.nodes.Empty;
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
import compiler.nodes.True;
import compiler.nodes.Unary;
import compiler.nodes.UnaryCondition;
import compiler.nodes.Variable;
import compiler.nodes.While;
import compiler.nodes.Write;

public interface Visitor
{
  void visit(Program item);

  void visit(Function item);

  void visit(Parameters item);

  void visit(Declaration item);

  void visit(Assignment item);

  void visit(Composite item);

  void visit(IfThen item);

  void visit(IfThenElse item);

  void visit(While item);

  void visit(Read item);

  void visit(Write item);

  void visit(Return item);

  void visit(Variable item);

  void visit(Number item);

  void visit(Binary item);

  void visit(Unary item);

  void visit(Call item);

  void visit(True item);

  void visit(False item);

  void visit(BinaryCondition item);

  void visit(Comparison item);

  void visit(UnaryCondition item);

  void visit(Empty item);

  void visit(ArrayInitializer item);

  void visit(ArrayAccess item);

  void visit(ArrayAssignment item);

  void visit(Length item);
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
