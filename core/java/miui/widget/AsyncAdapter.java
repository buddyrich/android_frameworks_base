package miui.widget;

import android.os.AsyncTask.Status;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import miui.os.DaemonAsyncTask;
import miui.os.ObservableAsyncTask;

public abstract class AsyncAdapter<T> extends BaseAdapter
{
  public static final int BOTH = 3;
  public static final int DOWNWARDS = 2;
  public static final int NONE = 0;
  public static final int UPWARDS = 1;
  private static DataCache<Object, Object> mPartialDataCache;
  private ArrayAdapter<T> mAdapter;
  private boolean mAutoLoadDownwardsMore;
  private boolean mAutoLoadUpwardsMore;
  private int mDataPerLine = 1;
  private List<T> mDataSet;
  private boolean mDecorating;
  private int mLoadMoreFlag;
  private boolean mLoadMoreUsingCache;
  private Map<Object, Integer> mLoadPartialDataCount;
  private AsyncAdapter<T>.AsyncLoadPartialDataTask mLoadPartialDataTask;
  private boolean mLoadUsingCache = true;
  private int mPreloadOffset;
  protected boolean mReachBottom;
  protected boolean mReachTop;
  private byte[] mTaskLocker = new byte[0];
  private Set<ObservableAsyncTask<?, ?, ?>> mTaskSet;

  public AsyncAdapter()
  {
    this.mDataSet = new ArrayList();
    init();
  }

  public AsyncAdapter(ArrayAdapter<T> paramArrayAdapter)
  {
    this.mAdapter = paramArrayAdapter;
    this.mDecorating = true;
    init();
  }

  private void postExecuteTask(ObservableAsyncTask<?, ?, ?> paramObservableAsyncTask)
  {
    synchronized (this.mTaskLocker)
    {
      this.mTaskSet.remove(paramObservableAsyncTask);
      return;
    }
  }

  private boolean preExecuteTask(ObservableAsyncTask<?, ?, ?> paramObservableAsyncTask)
  {
    int i;
    synchronized (this.mTaskLocker)
    {
      if (this.mTaskSet.contains(paramObservableAsyncTask))
      {
        i = 0;
      }
      else
      {
        this.mTaskSet.add(paramObservableAsyncTask);
        i = 1;
      }
    }
    return i;
  }

  protected abstract View bindContentView(View paramView, List<T> paramList, int paramInt);

  protected abstract void bindPartialContentView(View paramView, T paramT, int paramInt, List<Object> paramList);

  protected abstract List<Object> getCacheKeys(T paramT);

  public int getCount()
  {
    int i = getDataCount();
    if (i == 0);
    for (int j = 0; ; j = 1 + (i - 1) / this.mDataPerLine)
      return j;
  }

  public int getDataCount()
  {
    if (this.mDecorating);
    for (int i = this.mAdapter.getCount(); ; i = this.mDataSet.size())
      return i;
  }

  public T getDataItem(int paramInt)
  {
    if (this.mDecorating);
    for (Object localObject = this.mAdapter.getItem(paramInt); ; localObject = this.mDataSet.get(paramInt))
      return localObject;
  }

  public long getDataItemId(int paramInt)
  {
    return paramInt;
  }

  public int getDataPerLine()
  {
    return this.mDataPerLine;
  }

  public Object getItem(int paramInt)
  {
    return getDataItem(paramInt * this.mDataPerLine);
  }

  public long getItemId(int paramInt)
  {
    return getDataItemId(paramInt * this.mDataPerLine);
  }

  protected AsyncAdapter<T>.AsyncLoadDataTask getLoadDataTask()
  {
    return null;
  }

  protected AsyncAdapter<T>.AsyncLoadMoreDataTask getLoadMoreDataTask()
  {
    return null;
  }

  protected AsyncAdapter<T>.AsyncLoadPartialDataTask getLoadPartialDataTask()
  {
    return null;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject;
    if (getDataCount() == 0)
    {
      localObject = null;
      return localObject;
    }
    int i = paramInt * this.mDataPerLine;
    int j = Math.min(this.mDataPerLine, getDataCount() - i);
    if ((this.mDecorating) && (this.mDataPerLine == 1));
    ArrayList localArrayList;
    for (View localView = this.mAdapter.getView(i, paramView, paramViewGroup); ; localView = bindContentView(paramView, localArrayList, i))
    {
      for (int m = j - 1; m >= 0; m--)
        loadPartialData(localView, getDataItem(i + m), i + m);
      localArrayList = new ArrayList();
      for (int k = 0; k < j; k++)
        localArrayList.add(getDataItem(i + k));
    }
    if ((!this.mReachTop) && (paramInt == this.mPreloadOffset) && (this.mAutoLoadUpwardsMore))
      loadMoreData(true, this.mLoadMoreFlag, this.mLoadMoreUsingCache);
    while (true)
    {
      localObject = localView;
      break;
      if ((this.mReachBottom) || (paramInt != -1 + getCount() - this.mPreloadOffset) || (!this.mAutoLoadDownwardsMore))
        continue;
      loadMoreData(false, this.mLoadMoreFlag, this.mLoadMoreUsingCache);
    }
  }

  protected void init()
  {
    this.mTaskSet = new HashSet();
    mPartialDataCache = new DataCache(0, 0.75F, true);
    mPartialDataCache.setMaximumCapacity(100);
    this.mLoadPartialDataCount = new HashMap();
  }

  protected boolean isValidKey(Object paramObject, T paramT, int paramInt)
  {
    return mPartialDataCache.containsKey(paramObject);
  }

  protected List<T> loadCacheData(AsyncLoadMoreParams paramAsyncLoadMoreParams)
  {
    return null;
  }

  public void loadData()
  {
    AsyncLoadDataTask localAsyncLoadDataTask = getLoadDataTask();
    if (localAsyncLoadDataTask == null);
    while (true)
    {
      return;
      localAsyncLoadDataTask.setId("loadData");
      if (!preExecuteTask(localAsyncLoadDataTask))
        continue;
      try
      {
        localAsyncLoadDataTask.execute(new Void[0]);
      }
      catch (IllegalStateException localIllegalStateException)
      {
      }
    }
  }

  public void loadMoreData(boolean paramBoolean1, int paramInt, boolean paramBoolean2)
  {
    AsyncLoadMoreDataTask localAsyncLoadMoreDataTask = getLoadMoreDataTask();
    if (localAsyncLoadMoreDataTask == null);
    while (true)
    {
      return;
      AsyncLoadMoreParams localAsyncLoadMoreParams = new AsyncLoadMoreParams();
      localAsyncLoadMoreParams.upwards = paramBoolean1;
      localAsyncLoadMoreParams.flag = paramInt;
      this.mLoadMoreFlag = paramInt;
      List localList;
      int i;
      if ((paramBoolean1) || (getDataCount() == 0))
      {
        localAsyncLoadMoreParams.cursor = 0;
        if (!paramBoolean2)
          break label156;
        localList = loadCacheData(localAsyncLoadMoreParams);
        if (localList == null)
          break label150;
        i = 0;
        label75: if (i >= localList.size())
          break label150;
        if (!this.mDecorating)
          break label128;
        this.mAdapter.add(localList.get(i));
      }
      while (true)
      {
        i++;
        break label75;
        localAsyncLoadMoreParams.cursor = getDataCount();
        break;
        label128: this.mDataSet.add(localList.get(i));
      }
      label150: localAsyncLoadMoreDataTask.setClearData(true);
      label156: localAsyncLoadMoreDataTask.setLoadParams(localAsyncLoadMoreParams);
      localAsyncLoadMoreDataTask.setId("loadMoreData");
      if (!preExecuteTask(localAsyncLoadMoreDataTask))
        continue;
      try
      {
        localAsyncLoadMoreDataTask.execute(new Void[0]);
      }
      catch (IllegalStateException localIllegalStateException)
      {
      }
    }
  }

  protected void loadPartialData(View paramView, T paramT, int paramInt)
  {
    if ((this.mLoadPartialDataTask == null) || (this.mLoadPartialDataTask.getStatus() == AsyncTask.Status.FINISHED))
    {
      this.mLoadPartialDataTask = getLoadPartialDataTask();
      if (this.mLoadPartialDataTask != null);
    }
    while (true)
    {
      return;
      if (this.mLoadPartialDataTask.getStatus() == AsyncTask.Status.PENDING);
      try
      {
        this.mLoadPartialDataTask.execute(new Void[0]);
        label61: ArrayList localArrayList = new ArrayList();
        List localList = getCacheKeys(paramT);
        int i = 0;
        if (i < localList.size())
        {
          Object localObject = localList.get(i);
          if (isValidKey(localObject, paramT, i))
            localArrayList.add(mPartialDataCache.get(localObject));
          while (true)
          {
            i++;
            break;
            Integer localInteger = (Integer)this.mLoadPartialDataCount.get(localObject);
            if (localInteger == null)
              localInteger = Integer.valueOf(0);
            if (localInteger.intValue() >= 5)
              continue;
            this.mLoadPartialDataTask.addJob(localObject);
            this.mLoadPartialDataCount.put(localObject, Integer.valueOf(1 + localInteger.intValue()));
          }
        }
        bindPartialContentView(paramView, paramT, paramInt, localArrayList);
      }
      catch (IllegalStateException localIllegalStateException)
      {
        break label61;
      }
    }
  }

  public void onStop()
  {
    this.mLoadPartialDataCount.clear();
    if (this.mLoadPartialDataTask != null)
      this.mLoadPartialDataTask.stop();
  }

  protected void postLoadData(List<T> paramList)
  {
  }

  protected void postLoadMoreData(List<T> paramList)
  {
  }

  protected void postLoadPartialData(List<Object> paramList)
  {
  }

  public void setAutoLoadMoreStyle(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if ((paramInt & 0x1) != 0)
    {
      bool2 = bool1;
      this.mAutoLoadUpwardsMore = bool2;
      if ((paramInt & 0x2) == 0)
        break label32;
    }
    while (true)
    {
      this.mAutoLoadDownwardsMore = bool1;
      return;
      bool2 = false;
      break;
      label32: bool1 = false;
    }
  }

  public void setDataPerLine(int paramInt)
  {
    this.mDataPerLine = paramInt;
  }

  public void setDataSet(List<T> paramList)
  {
    this.mDataSet = paramList;
    notifyDataSetInvalidated();
  }

  public void setLoadMoreUsingCache(boolean paramBoolean)
  {
    this.mLoadMoreUsingCache = paramBoolean;
  }

  public void setLoadUsingCache(boolean paramBoolean)
  {
    this.mLoadUsingCache = paramBoolean;
  }

  public void setPreloadOffset(int paramInt)
  {
    this.mPreloadOffset = paramInt;
  }

  public abstract class AsyncLoadPartialDataTask extends DaemonAsyncTask<Object, Object>
  {
    public AsyncLoadPartialDataTask()
    {
    }

    protected void onProgressUpdate(Pair<Object, Object>[] paramArrayOfPair)
    {
      if ((paramArrayOfPair == null) || (paramArrayOfPair.length == 0));
      while (true)
      {
        return;
        Object localObject1 = paramArrayOfPair[0].first;
        Object localObject2 = paramArrayOfPair[0].second;
        if (localObject2 != null)
        {
          AsyncAdapter.mPartialDataCache.put(localObject1, localObject2);
          AsyncAdapter.this.notifyDataSetChanged();
        }
        super.onProgressUpdate(paramArrayOfPair);
      }
    }
  }

  public abstract class AsyncLoadMoreDataTask extends ObservableAsyncTask<Void, T, List<T>>
  {
    private boolean mClearData;
    private AsyncAdapter.AsyncLoadMoreParams mLoadParams;

    public AsyncLoadMoreDataTask()
    {
    }

    protected List<T> doInBackground(Void[] paramArrayOfVoid)
    {
      boolean bool = false;
      List localList;
      if (this.mLoadParams == null)
        localList = null;
      while (true)
      {
        return localList;
        localList = loadMoreData(this.mLoadParams);
        if (this.mLoadParams.upwards)
        {
          AsyncAdapter localAsyncAdapter2 = AsyncAdapter.this;
          if ((localList == null) || (localList.size() == 0))
            bool = true;
          localAsyncAdapter2.mReachTop = bool;
          continue;
        }
        AsyncAdapter localAsyncAdapter1 = AsyncAdapter.this;
        if ((localList == null) || (localList.size() == 0))
          bool = true;
        localAsyncAdapter1.mReachBottom = bool;
      }
    }

    protected abstract List<T> loadMoreData(AsyncAdapter.AsyncLoadMoreParams paramAsyncLoadMoreParams);

    protected void onPostExecute(List<T> paramList)
    {
      int i;
      if ((this.mClearData) && (paramList != null))
      {
        if (AsyncAdapter.this.mDecorating)
          AsyncAdapter.this.mAdapter.clear();
      }
      else
      {
        if (paramList == null)
          break label118;
        i = 0;
        label37: if (i >= paramList.size())
          break label118;
        if (!AsyncAdapter.this.mDecorating)
          break label95;
        AsyncAdapter.this.mAdapter.add(paramList.get(i));
      }
      while (true)
      {
        i++;
        break label37;
        AsyncAdapter.this.mDataSet.clear();
        break;
        label95: AsyncAdapter.this.mDataSet.add(paramList.get(i));
      }
      label118: AsyncAdapter.this.notifyDataSetChanged();
      super.onPostExecute(paramList);
      AsyncAdapter.this.postExecuteTask(this);
      AsyncAdapter.this.postLoadMoreData(paramList);
    }

    public void setClearData(boolean paramBoolean)
    {
      this.mClearData = paramBoolean;
    }

    public void setLoadParams(AsyncAdapter.AsyncLoadMoreParams paramAsyncLoadMoreParams)
    {
      this.mLoadParams = paramAsyncLoadMoreParams;
    }
  }

  public static class AsyncLoadMoreParams
  {
    public int cursor;
    public int flag;
    public boolean upwards;
  }

  public abstract class AsyncLoadDataTask extends ObservableAsyncTask<Void, T, List<T>>
  {
    private boolean mFirstTimeLoad = true;
    private List<T> mResultDataSet = new ArrayList();
    private List<T> mTempDataSet = new ArrayList();
    private List<AsyncAdapter.AsyncLoadDataVisitor<T>> mVisitors = new ArrayList();

    public AsyncLoadDataTask()
    {
    }

    private boolean realNeedExecuteTask()
    {
      int i = 0;
      if (i < this.mVisitors.size())
        if (!((AsyncAdapter.AsyncLoadDataVisitor)this.mVisitors.get(i)).dataChanged());
      for (int j = 1; ; j = 0)
      {
        return j;
        i++;
        break;
      }
    }

    public void addVisitor(AsyncAdapter.AsyncLoadDataVisitor<T> paramAsyncLoadDataVisitor)
    {
      this.mVisitors.add(paramAsyncLoadDataVisitor);
    }

    protected List<T> doInBackground(Void[] paramArrayOfVoid)
    {
      if (this.mVisitors.size() > 0)
        for (int j = 0; j < this.mVisitors.size(); j++)
          ((AsyncAdapter.AsyncLoadDataVisitor)this.mVisitors.get(j)).loadData(this);
      int i = 0;
      while (true)
      {
        Object[] arrayOfObject = loadData(i);
        if (arrayOfObject == null)
          break;
        Collections.addAll(this.mResultDataSet, arrayOfObject);
        publishProgress(arrayOfObject);
        i += arrayOfObject.length;
      }
      return this.mResultDataSet;
    }

    protected abstract T[] loadData(int paramInt);

    public void onLoadData(T[] paramArrayOfT)
    {
      Collections.addAll(this.mResultDataSet, paramArrayOfT);
      publishProgress(paramArrayOfT);
    }

    protected void onPostExecute(List<T> paramList)
    {
      if ((AsyncAdapter.this.mLoadUsingCache) && (!this.mFirstTimeLoad))
      {
        if (AsyncAdapter.this.mDecorating)
        {
          AsyncAdapter.this.mAdapter.clear();
          for (int i = 0; i < this.mTempDataSet.size(); i++)
            AsyncAdapter.this.mAdapter.add(this.mTempDataSet.get(i));
        }
        AsyncAdapter.this.mDataSet.clear();
        AsyncAdapter.this.mDataSet.addAll(this.mTempDataSet);
        AsyncAdapter.this.notifyDataSetChanged();
      }
      super.onPostExecute(paramList);
      AsyncAdapter.this.postExecuteTask(this);
      AsyncAdapter.this.postLoadData(paramList);
    }

    protected void onPreExecute()
    {
      boolean bool = true;
      if (!realNeedExecuteTask())
      {
        cancel(false);
        AsyncAdapter.this.postExecuteTask(this);
        return;
      }
      if (AsyncAdapter.this.mDecorating)
        if (AsyncAdapter.this.mAdapter.getCount() == 0)
        {
          label47: this.mFirstTimeLoad = bool;
          if ((AsyncAdapter.this.mLoadUsingCache) && (!this.mFirstTimeLoad))
            break label136;
          if (!AsyncAdapter.this.mDecorating)
            break label121;
          AsyncAdapter.this.mAdapter.clear();
        }
      while (true)
      {
        super.onPreExecute();
        break;
        bool = false;
        break label47;
        if (AsyncAdapter.this.mDataSet.size() == 0)
          break label47;
        bool = false;
        break label47;
        label121: AsyncAdapter.this.mDataSet.clear();
        continue;
        label136: this.mTempDataSet.clear();
      }
    }

    protected void onProgressUpdate(T[] paramArrayOfT)
    {
      int i = 0;
      if (i < paramArrayOfT.length)
      {
        if ((!AsyncAdapter.this.mLoadUsingCache) || (this.mFirstTimeLoad))
          if (AsyncAdapter.this.mDecorating)
            AsyncAdapter.this.mAdapter.add(paramArrayOfT[i]);
        while (true)
        {
          i++;
          break;
          AsyncAdapter.this.mDataSet.add(paramArrayOfT[i]);
          continue;
          this.mTempDataSet.add(paramArrayOfT[i]);
        }
      }
      if ((!AsyncAdapter.this.mLoadUsingCache) || (this.mFirstTimeLoad))
        AsyncAdapter.this.notifyDataSetChanged();
      super.onProgressUpdate(paramArrayOfT);
    }
  }

  public static abstract interface AsyncLoadDataVisitor<T>
  {
    public abstract boolean dataChanged();

    public abstract void loadData(AsyncAdapter<T>.AsyncLoadDataTask paramAsyncAdapter);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.AsyncAdapter
 * JD-Core Version:    0.6.0
 */