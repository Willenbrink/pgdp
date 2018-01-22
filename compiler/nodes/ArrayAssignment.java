package compiler.nodes;

import compiler.Visitor;

public class ArrayAssignment extends Statement
{
  private String name;
  private Expression field;
  private Expression value;

  public String getName()
  {
    return name;
  }

  public Expression getField()
  {
    return field;
  }

  public Expression getValue()
  {
    return value;
  }

  public ArrayAssignment(String name, Expression field, Expression value)
  {

    this.name = name;
    this.field = field;
    this.value = value;
  }

  @Override
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
