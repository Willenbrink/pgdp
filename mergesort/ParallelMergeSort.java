package mergesort;

public class ParallelMergeSort implements Runnable
{
  public static int numberOfThreads = 8;
  private static int currentThreads = 0;
  private int[] arr;
  private int low, high;

  public ParallelMergeSort(int[] array, int low, int high)
  {
    this.arr = array;
    this.low = low;
    this.high = high;
  }

  public static void mergeSort(int[] arr)
  {
    ParallelMergeSort sorter = new ParallelMergeSort(arr, 0, arr.length - 1);
    sorter.run();
  }

  @Override
  public void run()
  {
    //if (arr.length > 1)
    {
      int mid = (low + high) / 2;
      Thread thread = createThread(arr, low, mid);
      if (thread != null)
      {
        int prevLow = low;
        low = mid+1;
        run();
        try
        {
          thread.join();
          currentThreads--;
        } catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        NormalMergeSort.mergePublic(arr, prevLow, mid, high);
      }
      else
      {
        NormalMergeSort.mergeSort(arr, low, high);
      }
    }
  }

  private static synchronized Thread createThread(int[] array, int low, int high)
  {
    if (currentThreads >= numberOfThreads)
      return null;
    currentThreads++;
    Thread thread = new Thread(new ParallelMergeSort(array, low, high));
    thread.start();
    return thread;
  }
}
