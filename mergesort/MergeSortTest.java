package mergesort;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;
import org.junit.Test;

public class MergeSortTest {

  @Test
  public void mergeSort() throws Exception {
    // Zufälliges großes Array zum Testen
    int n = 100;//1000000;
    int maxValue = 1000;//10000000;
    Random rand = new Random();

    int[] randomArray = new int[n];
    for (int i = 0; i < n; i++) {
      randomArray[i] = rand.nextInt(maxValue);
    }
    int[] sortedArray = Arrays.copyOf(randomArray, randomArray.length);
    Arrays.sort(sortedArray);

    int[] copy1 = Arrays.copyOf(randomArray, randomArray.length);

    long timeStart = System.nanoTime();
    NormalMergeSort.mergeSort(copy1);
    long timeEnd = System.nanoTime();
    long timeDiff = timeEnd - timeStart;
    assertTrue("NormalMergeSort - Das Array sollte sortiert sein!",
        Arrays.equals(sortedArray, copy1));
    System.out.println("Normal MergeSort took               : " + timeDiff + " nanoseconds.");

    ParallelMergeSort.numberOfThreads = 4;
    copy1 = Arrays.copyOf(randomArray, randomArray.length);
    timeStart = System.nanoTime();
    ParallelMergeSort.mergeSort(copy1);
    timeEnd = System.nanoTime();
    timeDiff = timeEnd - timeStart;
    printArrays(copy1);
    assertTrue("ParallelMergeSort - n = 4 - Das Array sollte sortiert sein!",
        Arrays.equals(sortedArray, copy1));
    System.out.println("Parallel MergeSort with n =   4 took: " + timeDiff + " nanoseconds.");

    // n = 8
    ParallelMergeSort.numberOfThreads = 8;
    copy1 = Arrays.copyOf(randomArray, randomArray.length);
    timeStart = System.nanoTime();
    ParallelMergeSort.mergeSort(copy1);
    timeEnd = System.nanoTime();
    timeDiff = timeEnd - timeStart;
    printArrays(copy1);
    assertTrue("ParallelMergeSort - n = 8 - Das Array sollte sortiert sein!",
        Arrays.equals(sortedArray, copy1));
    System.out.println("Parallel MergeSort with n =   8 took: " + timeDiff + " nanoseconds.");

    // n = 16
    ParallelMergeSort.numberOfThreads = 16;
    copy1 = Arrays.copyOf(randomArray, randomArray.length);
    timeStart = System.nanoTime();
    ParallelMergeSort.mergeSort(copy1);
    timeEnd = System.nanoTime();
    timeDiff = timeEnd - timeStart;
    printArrays(copy1);
    assertTrue("ParallelMergeSort - n = 16 - Das Array sollte sortiert sein!",
        Arrays.equals(sortedArray, copy1));
    System.out.println("Parallel MergeSort with n =  16 took: " + timeDiff + " nanoseconds.");

    // n = 32
    ParallelMergeSort.numberOfThreads = 32;
    copy1 = Arrays.copyOf(randomArray, randomArray.length);
    timeStart = System.nanoTime();
    ParallelMergeSort.mergeSort(copy1);
    timeEnd = System.nanoTime();
    timeDiff = timeEnd - timeStart;
    printArrays(copy1);
    assertTrue("ParallelMergeSort - n = 32 - Das Array sollte sortiert sein!",
        Arrays.equals(sortedArray, copy1));
    System.out.println("Parallel MergeSort with n =  32 took: " + timeDiff + " nanoseconds.");

    // n = 128
    ParallelMergeSort.numberOfThreads = 128;
    copy1 = Arrays.copyOf(randomArray, randomArray.length);
    timeStart = System.nanoTime();
    ParallelMergeSort.mergeSort(copy1);
    timeEnd = System.nanoTime();
    timeDiff = timeEnd - timeStart;
    printArrays(copy1);
    assertTrue("ParallelMergeSort - n = 128 - Das Array sollte sortiert sein!",
        Arrays.equals(sortedArray, copy1));
    System.out.println("Parallel MergeSort with n = 128 took: " + timeDiff + " nanoseconds.");
  }

  public boolean equals(int[] array)
  {

    int[] sortedArray = Arrays.copyOf(array, array.length);
    Arrays.sort(sortedArray);
    if(Arrays.equals(array, sortedArray))
      return true;
    return false;
  }

  private void printArrays(int[] array)
  {
    int[] origianl = Arrays.copyOf(array, array.length);
    int[] sortedArray = Arrays.copyOf(array, array.length);
    Arrays.sort(sortedArray);


    for (int i = 0; i < array.length; i++)
    {
      if((origianl[i]-sortedArray[i]) != 0)
        System.out.println((origianl[i]-sortedArray[i]) + " | " + origianl[i] + " --- " + sortedArray[i]);
    }
    System.out.println("Is sorted: " + equals(origianl));
  }
}
//UTF-8 ä
