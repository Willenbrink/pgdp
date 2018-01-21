package compiler;

public class Read extends Statement
{
  private String name;

  public String getName()
  {
    return name;
  }

  public Read(String name)
  {

    this.name = name;
  }

  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
