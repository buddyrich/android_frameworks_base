package miui.os;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

public abstract class ObservableAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
  private String mId = Long.toString(super.hashCode());
  private List<AsyncTaskObserver<Params, Progress, Result>> mObservers = new ArrayList();

  public void addObserver(AsyncTaskObserver<Params, Progress, Result> paramAsyncTaskObserver)
  {
    if (paramAsyncTaskObserver != null)
      this.mObservers.add(paramAsyncTaskObserver);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof ObservableAsyncTask));
    for (boolean bool = this.mId.equals(((ObservableAsyncTask)paramObject).getId()); ; bool = false)
      return bool;
  }

  public String getId()
  {
    return this.mId;
  }

  public int hashCode()
  {
    return this.mId.hashCode();
  }

  // ERROR //
  protected void onCancelled()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 54	android/os/AsyncTask:onCancelled	()V
    //   4: aload_0
    //   5: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   8: astore_1
    //   9: aload_1
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   15: invokeinterface 58 1 0
    //   20: astore_3
    //   21: aload_3
    //   22: invokeinterface 64 1 0
    //   27: ifeq +25 -> 52
    //   30: aload_3
    //   31: invokeinterface 68 1 0
    //   36: checkcast 70	miui/os/AsyncTaskObserver
    //   39: invokeinterface 71 1 0
    //   44: goto -23 -> 21
    //   47: astore_2
    //   48: aload_1
    //   49: monitorexit
    //   50: aload_2
    //   51: athrow
    //   52: aload_1
    //   53: monitorexit
    //   54: return
    //
    // Exception table:
    //   from	to	target	type
    //   11	50	47	finally
    //   52	54	47	finally
  }

  // ERROR //
  protected void onPostExecute(Result paramResult)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 75	android/os/AsyncTask:onPostExecute	(Ljava/lang/Object;)V
    //   5: aload_0
    //   6: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: aload_0
    //   13: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   16: invokeinterface 58 1 0
    //   21: astore 4
    //   23: aload 4
    //   25: invokeinterface 64 1 0
    //   30: ifeq +27 -> 57
    //   33: aload 4
    //   35: invokeinterface 68 1 0
    //   40: checkcast 70	miui/os/AsyncTaskObserver
    //   43: aload_1
    //   44: invokeinterface 76 2 0
    //   49: goto -26 -> 23
    //   52: astore_3
    //   53: aload_2
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    //   57: aload_2
    //   58: monitorexit
    //   59: return
    //
    // Exception table:
    //   from	to	target	type
    //   12	55	52	finally
    //   57	59	52	finally
  }

  // ERROR //
  protected void onPreExecute()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 79	android/os/AsyncTask:onPreExecute	()V
    //   4: aload_0
    //   5: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   8: astore_1
    //   9: aload_1
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   15: invokeinterface 58 1 0
    //   20: astore_3
    //   21: aload_3
    //   22: invokeinterface 64 1 0
    //   27: ifeq +25 -> 52
    //   30: aload_3
    //   31: invokeinterface 68 1 0
    //   36: checkcast 70	miui/os/AsyncTaskObserver
    //   39: invokeinterface 80 1 0
    //   44: goto -23 -> 21
    //   47: astore_2
    //   48: aload_1
    //   49: monitorexit
    //   50: aload_2
    //   51: athrow
    //   52: aload_1
    //   53: monitorexit
    //   54: return
    //
    // Exception table:
    //   from	to	target	type
    //   11	50	47	finally
    //   52	54	47	finally
  }

  // ERROR //
  protected void onProgressUpdate(Progress[] paramArrayOfProgress)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 84	android/os/AsyncTask:onProgressUpdate	([Ljava/lang/Object;)V
    //   5: aload_0
    //   6: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: aload_0
    //   13: getfield 19	miui/os/ObservableAsyncTask:mObservers	Ljava/util/List;
    //   16: invokeinterface 58 1 0
    //   21: astore 4
    //   23: aload 4
    //   25: invokeinterface 64 1 0
    //   30: ifeq +27 -> 57
    //   33: aload 4
    //   35: invokeinterface 68 1 0
    //   40: checkcast 70	miui/os/AsyncTaskObserver
    //   43: aload_1
    //   44: invokeinterface 85 2 0
    //   49: goto -26 -> 23
    //   52: astore_3
    //   53: aload_2
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    //   57: aload_2
    //   58: monitorexit
    //   59: return
    //
    // Exception table:
    //   from	to	target	type
    //   12	55	52	finally
    //   57	59	52	finally
  }

  public void removeObserver(AsyncTaskObserver<Params, Progress, Result> paramAsyncTaskObserver)
  {
    if (paramAsyncTaskObserver != null)
      this.mObservers.remove(paramAsyncTaskObserver);
  }

  public void setId(String paramString)
  {
    this.mId = paramString;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.os.ObservableAsyncTask
 * JD-Core Version:    0.6.0
 */