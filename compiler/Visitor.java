package compiler;

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
}
//UTF-8 Encoded ä
