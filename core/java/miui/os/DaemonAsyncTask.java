package miui.os;

import android.os.AsyncTask;
import android.util.Pair;
import java.util.Stack;

public class DaemonAsyncTask<Job, JobResult> extends ObservableAsyncTask<Void, Pair<Job, JobResult>, Void>
{
  private JobPool<Job> mJobPool;
  private byte[] mLocker = new byte[0];
  private boolean mNeedStop;

  static
  {
    AsyncTask.setDefaultExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  public void addJob(Job paramJob)
  {
    this.mJobPool.addJob(paramJob);
    synchronized (this.mLocker)
    {
      this.mLocker.notifyAll();
      return;
    }
  }

  protected Void doInBackground(Void[] paramArrayOfVoid)
  {
    while (!this.mNeedStop)
    {
      Object localObject1 = null;
      if (!this.mJobPool.isEmpty())
        localObject1 = this.mJobPool.getNextJob();
      if (localObject1 == null)
        try
        {
          synchronized (this.mLocker)
          {
            this.mLocker.wait();
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
            localInterruptedException.printStackTrace();
        }
      Pair localPair = new Pair(localObject1, doJob(localObject1));
      Pair[] arrayOfPair = new Pair[1];
      arrayOfPair[0] = localPair;
      publishProgress(arrayOfPair);
    }
    return null;
  }

  protected JobResult doJob(Job paramJob)
  {
    return null;
  }

  public void setJobPool(JobPool<Job> paramJobPool)
  {
    this.mJobPool = paramJobPool;
  }

  public void setLocker(byte[] paramArrayOfByte)
  {
    this.mLocker = paramArrayOfByte;
  }

  public void stop()
  {
    this.mNeedStop = true;
    synchronized (this.mLocker)
    {
      this.mLocker.notifyAll();
      return;
    }
  }

  public static class StackJobPool<T>
    implements DaemonAsyncTask.JobPool<T>
  {
    private Stack<T> mJobs = new Stack();

    /** @deprecated */
    public void addJob(T paramT)
    {
      monitorenter;
      try
      {
        if (!this.mJobs.contains(paramT))
          this.mJobs.push(paramT);
        monitorexit;
        return;
      }
      finally
      {
        localObject = finally;
        monitorexit;
      }
      throw localObject;
    }

    /** @deprecated */
    // ERROR //
    public T getNextJob()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 22	miui/os/DaemonAsyncTask$StackJobPool:mJobs	Ljava/util/Stack;
      //   6: invokevirtual 39	java/util/Stack:pop	()Ljava/lang/Object;
      //   9: astore 4
      //   11: aload 4
      //   13: astore_3
      //   14: aload_0
      //   15: monitorexit
      //   16: aload_3
      //   17: areturn
      //   18: astore_2
      //   19: aconst_null
      //   20: astore_3
      //   21: goto -7 -> 14
      //   24: astore_1
      //   25: aload_0
      //   26: monitorexit
      //   27: aload_1
      //   28: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   2	11	18	java/util/EmptyStackException
      //   2	11	24	finally
    }

    /** @deprecated */
    public boolean isEmpty()
    {
      monitorenter;
      try
      {
        boolean bool = this.mJobs.empty();
        monitorexit;
        return bool;
      }
      finally
      {
        localObject = finally;
        monitorexit;
      }
      throw localObject;
    }
  }

  public static abstract interface JobPool<T>
  {
    public abstract void addJob(T paramT);

    public abstract T getNextJob();

    public abstract boolean isEmpty();
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.os.DaemonAsyncTask
 * JD-Core Version:    0.6.0
 */