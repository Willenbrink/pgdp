package optimizer;

import compiler.Interpreter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TailCallOptimization
{
  /*
  Die Vorgehensweise ist:
  Finde Endrekursion
  Überprüfe ob die aufgerufene Funktion die selbe ist
  Ersetze Call
    Hierbei werden erst alle Parameter in die Variablen gespeichert mit SRS
    Danach werden die Instruktionen:
      LDI -1  JUMP x angehängt
   */

  /*
  Das Optimieren in der Codegenerierung hätte den Vorteil gehabt, dass man nicht auf komplizierte
  Art und Weise überprüfen muss ob die Optimierung zulässig ist. Es wäre deutlich leichter gewesen
  auch eine indirekte Rekursion zu entdecken und zu überprüfen.
  Außerdem ist die Kombination mit der 32-bit Erweiterung nur schwer möglich, da dafür bei jedem LDI -1
  welches notwendig ist für den JUMP-Befehl, 3 zusätzliche Befehle eingefügt werden müssen.
  Um diese Optimierung umzusetzen könnte man bspw. in visit (Function item) die jeweils nächsten
  zwei Statements betrachten und wenn sie Call und Return sind dementsprechend
  einen optimierten Code generieren.

  Die dritte Möglichkeit wäre bereits beim Parsen den rekursiven Aufruf zu entdecken und ein eigenes
  RekursiverAufruf-Objekt zu erstellen. Alternativ kann das natürlich auch zwischen Parsen und Codegen
  passieren
   */


  // Zeile -> Optimierung möglich
  static Map<Integer, Boolean> modifyCheck = new HashMap<>();

  public static void optimize(int[] program)
  {
    int previous = 0;
    int tail;
    checkArguments(program);
    do
    {
      tail = findTailRecursion(program, previous);
      previous = tail+1;
      if (tail == -1)
        return;
      replaceCall(program, tail);
    } while (true);
  }

  private static void replaceCall(int[] program, int tail)
  {
    int jump = getImmediate(program[tail]);
    int amount = getImmediate(program[tail+1]);
    for(Entry<Integer, Boolean> entry : modifyCheck.entrySet())
    {
      if(entry.getKey() == tail+1 && !entry.getValue())
        return;
    }
    int line = tail;
    for (int i = 0; i < amount; i++)
    {
      program[line-amount] = (Interpreter.STS << 16) ^ ((-i) & 0xFFFF);
      line++;
    }
    program[tail] = (Interpreter.LDI << 16) ^ 0xFFFF;
    program[tail+1] = (Interpreter.JUMP << 16) ^ jump;
    program[tail+2] = (Interpreter.NOP << 16);
  }

  private static void checkArguments(int[] program)
  {
    for (int i = 0; i < program.length; i++)
    {
      Loop: if(getOpcode(program[i]) == Interpreter.CALL)
      {
        for (int j = i; j >= 0 ; j--)
        {
          if(getOpcode(program[j]) == Interpreter.ALLOC)
          {
            if(getImmediate(program[i-1]) == j)
            {
              modifyCheck.put(i, true);
              break Loop;
            }
            modifyCheck.put(i, false);
            break Loop;
          }
        }
      }
    }
  }

  private static int findTailRecursion(int[] program, int start)
  {
    for (int i = start; i < program.length-2; i++)
    {
      if(getOpcode(program[i]) == Interpreter.LDI
          && getOpcode(program[i+1]) == Interpreter.CALL
          && getOpcode(program[i+2]) == Interpreter.RETURN)
      {
        //TailRecursion found
        return i;
      }
    }
    return -1;
  }

  private static int getOpcode(int instruction)
  {
    return instruction >>> 16;
  }

  private static int getImmediate(int instruction)
  {
    return (short) (instruction & 0xFFFF);
  }
}
//UTF-8 Encoded ä
