import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Random;
import org.junit.Test;

public class SuchtbaumTest {
  private Random random = new Random();
  
  @Test
  public void testContains() throws InterruptedException {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 10000;
    for (int i = 0; i < n; i++)
      testSet.add(random.nextInt(20*n));
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for(Integer i : testSet)
      suchti.insert(i);
    for (int i = 0; i < 20*n; i++)
      assertEquals(testSet.contains(i), suchti.contains(i));
  }
  
  @Test
  public void testContainsRemove() throws InterruptedException {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 10000;
    for (int i = 0; i < n; i++)
      testSet.add(random.nextInt(20*n));
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for(Integer i : testSet)
      suchti.insert(i);
    for (int i = 0; i < n; i++) {
      int next = random.nextInt(20*n);
      if(testSet.contains(next)) {
        suchti.remove(next);
        testSet.remove(next);
      }
    }
    for (int i = 0; i < 20*n; i++)
      assertEquals(testSet.contains(i), suchti.contains(i));
  }

}
