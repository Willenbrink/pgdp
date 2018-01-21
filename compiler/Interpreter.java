package compiler;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter extends MiniJava
{
  public static final int NOP = 0;
  public static final int ADD = 1;
  public static final int SUB = 2;
  public static final int MUL = 3;
  public static final int MOD = 4;
  public static final int LDI = 5;
  public static final int LDS = 6;
  public static final int STS = 7;
  public static final int IN = 8;
  public static final int OUT = 9;
  public static final int CALL = 10;
  public static final int RETURN = 11;
  public static final int HALT = 12;
  public static final int ALLOC = 13;
  public static final int DIV = 14;
  public static final int AND = 15;
  public static final int OR = 16;
  public static final int NOT = 17;
  public static final int JUMP = 18;
  public static final int EQ = 19;
  public static final int LT = 20;
  public static final int LE = 21;
  public static final int SHL = 26;
  public static final int LDH = 27;
  public static final int STH = 28;
  public static final int ALLOCH = 29;
  //TODO add to parser

  private static int[] stack;
  private static int pointer = -1;
  private static int frame = -1;

  private static int[] program;
  private static int free = 0;

  private static int[] heap;

  private static String[] labels;

  private static Dictionary<Integer, String> codes = new Hashtable<>();
  static
  {
    codes.put(NOP, "NOP");
    codes.put(ADD, "ADD");
    codes.put(SUB, "SUB");
    codes.put(MUL, "MUL");
    codes.put(MOD, "MOD");
    codes.put(LDI, "LDI");
    codes.put(LDS, "LDS");
    codes.put(STS, "STS");
    codes.put(IN, "IN");
    codes.put(OUT, "OUT");
    codes.put(CALL, "CALL");
    codes.put(RETURN, "RETURN");
    codes.put(HALT, "HALT");
    codes.put(ALLOC, "ALLOC");
    codes.put(DIV, "DIV");
    codes.put(AND, "AND");
    codes.put(OR, "OR");
    codes.put(NOT, "NOT");
    codes.put(JUMP, "JUMP");
    codes.put(EQ, "EQ");
    codes.put(LT, "LT");
    codes.put(LE, "LE");
    codes.put(SHL, "SHL");
    codes.put(LDH, "LDH");
    codes.put(STH, "STH");
    codes.put(ALLOCH, "ALLOCH");
  }

  private static Dictionary<String, Boolean> immediatesMap = new Hashtable<>();
  static
  {
    immediatesMap.put("NOP", false);
    immediatesMap.put("ADD", false);
    immediatesMap.put("SUB", false);
    immediatesMap.put("MUL", false);
    immediatesMap.put("MOD", false);
    immediatesMap.put("LDI", true);
    immediatesMap.put("LDS", true);
    immediatesMap.put("STS", true);
    immediatesMap.put("IN", false);
    immediatesMap.put("OUT", false);
    immediatesMap.put("CALL", true);
    immediatesMap.put("RETURN", true);
    immediatesMap.put("HALT", false);
    immediatesMap.put("ALLOC", true);
    immediatesMap.put("DIV", false);
    immediatesMap.put("AND", false);
    immediatesMap.put("OR", false);
    immediatesMap.put("NOT", false);
    immediatesMap.put("JUMP", true);
    immediatesMap.put("EQ", false);
    immediatesMap.put("LT", false);
    immediatesMap.put("LE", false);
    immediatesMap.put("SHL", true);
    immediatesMap.put("LDH", false);
    immediatesMap.put("STH", false);
    immediatesMap.put("ALLOCH", false);
  }

  static void error(String message)
  {
    throw new RuntimeException(message);
  }

  public static String programToString(int[] program)
  {
    String output = "";
    for (int i = 0; i < program.length; i++)
    {
      String code = codes.get(program[i] >> 16);
      int immediate = getImm(program[i]);
      if(code.equalsIgnoreCase("ALLOC"))
        System.out.println();
      if(immediatesMap.get(code))
        output += i+": " + code + " " + immediate + "\n";
      else
      {
        if(immediate != 0)
          System.err.println("Line " + i + ": " + code + " shouldn't have immediate: " + immediate);
        output += i + ": " + code + "\n";
      }
    }
    return output;
  }

  public static String readProgramConsole()
  {
    @SuppressWarnings("resource")
    Scanner sin = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (true)
    {
      String nextLine = sin.nextLine();
      if (nextLine.equals(""))
      {
        nextLine = sin.nextLine();
        if (nextLine.equals(""))
        {
          break;
        }
      }
      if (nextLine.startsWith("//"))
      {
        continue;
      }
      builder.append(nextLine);
      builder.append('\n');
    }
    return builder.toString();
  }

  public static void main(String[] args)
  {
    String input = readProgramConsole();
    int[] prog = parse(input);
    MiniJava.writeLineConsole(execute(prog));
  }

  public static int[] parse(String textProgram)
  {
    free = 0;
    parseLabels(textProgram);
    //Das Programm braucht nicht notwendigerweise Zeilen
    //alle Instruktionen können in eine Zeile geschrieben werden
    //jedoch sind mehrere Zeilen kein Problem
    //Dadurch ist eigentlich keine Abweichung vom Format
    String[] input = textProgram.split(" |\n");
    program = new int[input.length];
    for (int i = 0; i < input.length; i++)
    {
      int next = 0;
      switch (input[i])
      {
        case "NOP":
          next = NOP;
          next = next << 16;
          break;
        case "ADD":
          next = ADD;
          next = next << 16;
          break;
        case "SUB":
          next = SUB;
          next = next << 16;
          break;
        case "MUL":
          next = MUL;
          next = next << 16;
          break;
        case "DIV":
          next = DIV;
          next = next << 16;
          break;
        case "OR":
          next = OR;
          next = next << 16;
          break;
        case "NOT":
          next = NOT;
          next = next << 16;
          break;
        case "AND":
          next = AND;
          next = next << 16;
          break;
        case "EQ":
          next = EQ << 16;
          break;
        case "LT":
          next = LT << 16;
          break;
        case "LE":
          next = LE << 16;
          break;
        case "MOD":
          next = MOD;
          next = next << 16;
          break;
        case "SHL":
          next = SHL;
          next = next << 16;
          break;
        case "LDI":
          next = LDI;
          next = next << 16;
          i++;
          if (checkLabel(input[i]) != -1)
          {
            next += checkLabel(input[i]);
          }
          else
          {
            next = next | parseImm(input[i]);
          }
          break;
        case "LDS":
          next = LDS;
          next = next << 16;
          i++;
          if (checkLabel(input[i]) != -1)
          {
            next += checkLabel(input[i]);
          }
          else
          {
            next = next | parseImm(input[i]);
          }
          break;
        case "STS":
          next = STS;
          next = next << 16;
          i++;
          if (checkLabel(input[i]) != -1)
          {
            next += checkLabel(input[i]);
          }
          else
          {
            next = next | parseImm(input[i]);
          }
          break;
        case "JUMP":
          next = JUMP;
          next = next << 16;
          i++;
          if (checkLabel(input[i]) != -1)
          {
            next += checkLabel(input[i]);
          }
          else
          {
            next = next | parseImm(input[i]);
          }
          break;
        case "IN":
          next = IN;
          next = next << 16;
          break;
        case "OUT":
          next = OUT;
          next = next << 16;
          break;
        case "CALL":
          next = CALL;
          next = next << 16;
          i++;
          if (checkLabel(input[i]) != -1)
          {
            next += checkLabel(input[i]);
          }
          else
          {
            next = next | parseImm(input[i]);
          }
          break;
        case "RETURN":
          next = RETURN;
          next = next << 16;
          i++;
          if (checkLabel(input[i]) != -1)
          {
            next += checkLabel(input[i]);
          }
          else
          {
            next = next | parseImm(input[i]);
          }
          break;
        case "HALT":
          next = HALT;
          next = next << 16;
          break;
        case "ALLOC":
          next = ALLOC;
          next = next << 16;
          i++;
          if (checkLabel(input[i]) != -1)
          {
            next += checkLabel(input[i]);
          }
          else
          {
            next = next | parseImm(input[i]);
          }
          break;
      }
      set(next);
    }
    return program;
  }

  private static void parseLabels(String textProgram)
  {
    String[] input = textProgram.split("[ \n]");
    program = new int[input.length];
    labels = new String[input.length];
    for (int i = 0; i < input.length; i++)
    {

      //TODO labelparser testen
      try
      {
        if(immediatesMap.get(input[i]))
          i++;
      }
      catch (NullPointerException e)
      {
        if (input[i].charAt(input[i].length() - 1) == ':')
        {
          labels[free] = input[i].subSequence(0, input[i].length() - 1).toString();
        }
        else
        {
          error("Invalid instruction/label definition: " + input[i]);
        }
      }
      set(0);
    }
    free = 0;
  }

  static int checkLabel(String input)
  {
    for (int i = 0; i < labels.length; i++)
    {
      if (input.equals(labels[i]))
      {
        return i;
      }
    }
    if (Pattern.matches("[a-zA-Z]+", input)) {
      error("Undefined label");
    }
    return -1;
  }

  static int parseImm(String input)
  {
    short imm;
    try
    {
      imm = Short.parseShort(input);
    } catch (Exception e)
    {
      error("Error when parsing immediate: " + input);
      imm = 0;
    }
    int i = imm & 0xFFFF;
    return i;
  }

  public static int execute(int[] prog)
  {
    System.out.println(programToString(prog));

    program = prog;
    stack = new int[32];
    heap = new int[128];
    heap[heap.length-1] = 127;
    pointer = -1;
    frame = -1;
    free = 0;
    //ip ist der instructionpointer und damit äquivalent zu pc
    int ip = 0;
    Loop:
    while (true)
    {
      if(ip >= prog.length)
        error("No HALT found at the end of the program");
      int jp = -1;
      int imm = getImm(prog[ip]);
      int opcode = prog[ip] >> 16;
      switch (opcode)
      {
        case NOP:
          break;
        case ADD:
          add();
          break;
        case SUB:
          sub();
          break;
        case MUL:
          mul();
          break;
        case DIV:
          div();
          break;
        case AND:
          and();
          break;
        case OR:
          or();
          break;
        case NOT:
          not();
          break;
        case MOD:
          mod();
          break;
        case LDI:
          //Funktioniert nicht mit der Optimierung wegen Platzmangel
          int unsigned = getImmUnsigned(prog[ip]);
          ldi(unsigned);
          break;
        case LDS:
          lds(imm);
          break;
        case STS:
          sts(imm);
          break;
        case EQ:
          eq();
          break;
        case LT:
          lt();
          break;
        case LE:
          le();
          break;
        case JUMP:
          jp = jump(imm);
          if (jp > 0)
          {
            ip = jp;
          }
          break;
        case CALL:
          jp = call(imm, ip);
          if (jp > 0)
          {
            ip = jp;
          }
          break;
        case RETURN:
          jp = ret(imm);
          if (jp > 0)
          {
            ip = jp;
          }
          break;
        case IN:
          in();
          break;
        case OUT:
          out();
          break;
        case HALT:
          halt();
          break Loop;
        case ALLOC:
          alloc(imm);
          break;
        case SHL:
          shl(imm);
          break;
        case LDH:
          ldh();
          break;
        case STH:
          sth();
          break;
        case ALLOCH:
          alloch();
          break;
        default:
          error(opcode + " is no recognized instuction");
      }
      if (jp >= 0)
      {
        ip = jp;
      }
      else
      {
        ip++;
      }
    }
    if (pointer < 0)
    {
      error("Stack empty upon exit");
    }
    return pop();
  }

  private static void div()
  {
    int first = pop();
    int second = pop();
    push(first/second);
  }

  private static void and()
  {
    int first = pop();
    int second = pop();
    push(first&second);
  }

  private static void or()
  {
    int first = pop();
    int second = pop();
    push(first|second);
  }

  private static void not()
  {
    int first = pop();
    push(~first);
  }

  private static void alloc(int imm)
  {
    if (frame != pointer)
    {
      error("ALLOC used in function");
    }
    pointer += imm;
  }

  private static void halt()
  {
    //Nothing to do here right?
  }

  private static void out()
  {
    MiniJava.writeLineConsole(pop());
  }

  private static void in()
  {
    push(MiniJava.readInt());
  }

  private static int ret(int imm)
  {
    int ret = pop();
    pointer -= imm;
    int ip = pop();
    frame = pop();
    push(ret);
    return ip;
  }

  private static int call(int imm, int ip)
  {
    int address = pop();
    //Check if address is within bounds
    if(address < 0 || address >= program.length)
       error("Jumpaddress not in the program");
    int[] params = new int[imm];
    for (int i = imm - 1; i >= 0; i--)
    {
      params[i] = pop();
    }
    //Nicht eindeutige Angabe
    //Es wird beschrieben, dass erst der Instruktionpointer und dann der Framepointer abgelegt werden
    //Jedoch zeigt das Schaubild es anders
    push(frame);
    push(ip + 1);
    for (int i = 0; i < imm; i++)
    {
      push(params[i]);
    }
    frame = pointer;
    return address;
  }

  private static void lt()
  {
    if (pop() < pop())
      push(-1);
    else
      push(0);
  }

  private static void le()
  {
    if (pop() <= pop())
      push(-1);
    else
      push(0);
  }

  private static void eq()
  {
    if (pop() == pop())
      push(-1);
    else
      push(0);
  }

  private static int jump(int imm)
  {
    if(pop() != -1)
      return -1;
    return imm;
  }

  private static void sts(int imm)
  {
    stack[frame + imm] = pop();
  }

  private static void lds(int imm)
  {
    push(stack[frame + imm]);
  }

  private static void ldi(int imm)
  {
    push(imm);
  }

  private static void mod()
  {
    int g = pop();
    int h = pop();
    push(g % h);
  }

  private static void mul()
  {
    int e = pop();
    int f = pop();
    push(e * f);
  }

  private static void sub()
  {
    int c = pop();
    int d = pop();
    push(c - d);
  }

  private static void add()
  {
    int a = pop();
    int b = pop();
    push(a + b);
  }

  static int getImm(int prog)
  {
    return (short) (prog & 0xFFFF);
  }

  static int getImmUnsigned(int prog)
  {
    return (prog & 0xFFFF);
  }

  static void set(int value)
  {
    program[free] = value;
    free++;
  }

  public static int pop()
  {
    pointer--;
    if (pointer < -1)
    {
      error("Stack ist leer");
    }
    return stack[pointer + 1];
  }

  public static void push(int value)
  {
    pointer++;
    if (pointer > 127)
    {
      error("Stack ist voll");
    }
    stack[pointer] = value;
  }

  private static void shl(int imm)
  {
    int stackValue = pop();
    int value = stackValue << imm;
    push(value);
  }

  private static void ldh()
  {
    int ref = pop();
    int from = getFrom(ref);
    int to = getTo(ref);
    int offset = pop();
    if(offset+from > heap.length-1 || from+offset < 0)
      throw new RuntimeException("Accessing invalid heapfields");
    int value = heap[from+offset];
    push(value);
  }

  private static void sth()
  {
    int ref = pop();
    int from = getFrom(ref);
    int to = getTo(ref);
    int offset = pop();
    //Eigentlich muss auch -1 ausgechlossen werden
    // das musste wegen dem Längeproblem wieder rückgängig gemacht werden
    //Anscheinend soll der Interpreter überhaupt nur auf OutOfBounds prüfen, daher hier keine Prüfung
    //if(offset > to || offset < -1)
    //  throw new RuntimeException("Accessing field outside of array");
    if(offset+from > heap.length-1 || from+offset < 0)
      throw new RuntimeException("Accessing invalid heapfields");
    heap[from+offset] = pop();
  }

  private static void alloch()
  {
    int headerEnd = heap[heap.length-1];
    int prevObjectEnd;
    if(headerEnd != heap.length-1)
      prevObjectEnd = heap[headerEnd] >>> 16;
    else
      prevObjectEnd = 0;
    int from = prevObjectEnd;
    int to = from+pop();
    if(to < from)
      throw new RuntimeException("Negative Arraysize");
    if(to >= headerEnd)
      throw new RuntimeException("Heap is full/Overflow");
    heap[headerEnd-1] = (to << 16) ^ (from & 0xFFFF);
    push(heap[headerEnd-1]);
    heap[heap.length-1]--;
  }

  private static int getFrom(int pointer)
  {
    int from = pointer & 0xFFFF;
    return from;
  }

  private static int getTo(int pointer)
  {
    int to = pointer >> 16;
    return to;
  }

  //Debugfunktion um Stack ausgeben zu können
  public static String stackToString(int pointer)
  {
    String output = "";
    for (int i = 0; i < pointer; i++)
    {
      output += stack[i] + "|";
    }
    return output;
  }

  public static String heapToString()
  {
    String output = "";
    for (int i = heap.length-1; i >= heap[heap.length-1]; i--)
    {
      //output += Integer.toBinaryString(heap[i]) + "|";
      output += heap[i] + "|";
    }
    return output;

  }
}
//UTF-8 Encoded ä
//UTF-8 ä Ich mach es tatsächlich per Commandline und in jedem Dokument... mit echo TEXT | tee -a `ls | grep .java` MIT EINZELNEN ANFÜHRUNGSZEICHEN, das macht ansonsten alles kaputt...
