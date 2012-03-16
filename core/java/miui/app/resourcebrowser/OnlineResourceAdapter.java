package miui.app.resourcebrowser;

import android.content.Context;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.widget.Toast;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import miui.os.AsyncTaskObserver;
import miui.widget.AsyncAdapter;
import miui.widget.AsyncAdapter.AsyncLoadMoreDataTask;
import miui.widget.AsyncAdapter.AsyncLoadMoreParams;

public class OnlineResourceAdapter extends ResourceAdapter
{
  protected String mCategoryCode;
  private HashMap<String, Integer> mDownloadedCount = new HashMap();
  private String mKeyword;
  private int mLoadMoreFlag;
  private LinkedHashMap<String, Void> mThumbnailDownloadQueue = new LinkedHashMap(0, 0.75F, true);
  private ThumbnailDownloadTask mThumbnailDownloadTask;

  public OnlineResourceAdapter(Context paramContext, Bundle paramBundle)
  {
    super(paramContext, paramBundle);
  }

  private void downloadThumbnail(String paramString)
  {
    if (paramString != null)
    {
      Integer localInteger = (Integer)this.mDownloadedCount.get(paramString);
      if ((localInteger == null) || (localInteger.intValue() < 5))
      {
        this.mThumbnailDownloadQueue.put(paramString, null);
        if ((this.mThumbnailDownloadTask == null) || (this.mThumbnailDownloadTask.getStatus() == AsyncTask.Status.FINISHED))
        {
          this.mThumbnailDownloadTask = new ThumbnailDownloadTask();
          this.mThumbnailDownloadTask.setTargetDirectory(ResourceConstants.THUMBNAIL_PATH + this.mResourceSetCode);
          ThumbnailDownloadTask localThumbnailDownloadTask = this.mThumbnailDownloadTask;
          String[] arrayOfString = new String[1];
          arrayOfString[0] = paramString;
          localThumbnailDownloadTask.execute(arrayOfString);
        }
      }
    }
  }

  private void resetResources()
  {
    this.mResourceSet.clear();
    notifyDataSetInvalidated();
    loadMoreData(false, this.mLoadMoreFlag, true);
  }

  protected AsyncAdapter<Resource>.AsyncLoadMoreDataTask getLoadMoreDataTask()
  {
    AsyncLoadMoreResourceTask localAsyncLoadMoreResourceTask = new AsyncLoadMoreResourceTask();
    localAsyncLoadMoreResourceTask.addObserver((AsyncTaskObserver)this.mContext);
    return localAsyncLoadMoreResourceTask;
  }

  protected boolean isValidKey(Object paramObject, Resource paramResource, int paramInt)
  {
    boolean bool = true;
    String str1 = (String)paramObject;
    String str2 = paramResource.getOnlineThumbnail(paramInt);
    File localFile = new File(str1);
    if (!localFile.exists())
      downloadThumbnail(str2);
    while (true)
    {
      return bool;
      if (localFile.lastModified() < paramResource.getModifiedTime())
      {
        localFile.delete();
        downloadThumbnail(str2);
        continue;
      }
      bool = super.isValidKey(paramObject, paramResource, paramInt);
    }
  }

  protected List<Resource> loadCacheData(AsyncAdapter.AsyncLoadMoreParams paramAsyncLoadMoreParams)
  {
    List localList;
    if ((paramAsyncLoadMoreParams.upwards) || (paramAsyncLoadMoreParams.cursor != 0))
      localList = null;
    while (true)
    {
      return localList;
      localList = null;
      String str1 = OnlineHelper.getListURL(paramAsyncLoadMoreParams.cursor, 0, this.mCategoryCode, this.mKeyword, this.mMetaData);
      String str2 = ResourceHelper.getFormattedDirectoryPath(this.mMetaData.getString("com.miui.android.resourcebrowser.CACHE_LIST_FOLDER"));
      String str3 = OnlineHelper.getFilePathByURL(str2 + this.mResourceSetCode, str1);
      if (!new File(str3).exists())
        continue;
      localList = OnlineHelper.readResources(str3, this.mMetaData);
    }
  }

  protected void postLoadMoreData(List<Resource> paramList)
  {
    if (paramList == null)
      Toast.makeText(this.mContext, 51118112, 0).show();
  }

  public void setCategoryCode(String paramString)
  {
    this.mCategoryCode = paramString;
    resetResources();
  }

  public void setFlag(int paramInt)
  {
    this.mLoadMoreFlag = paramInt;
    resetResources();
  }

  public void setKeyword(String paramString)
  {
    this.mKeyword = paramString;
    resetResources();
  }

  public class ThumbnailDownloadTask extends DownloadFileTask
  {
    private String mDownloadPath;

    public ThumbnailDownloadTask()
    {
    }

    protected List<DownloadFileTask.DownloadFileEntry> doInBackground(String[] paramArrayOfString)
    {
      this.mDownloadPath = paramArrayOfString[0];
      return super.doInBackground(paramArrayOfString);
    }

    protected void onPostExecute(List<DownloadFileTask.DownloadFileEntry> paramList)
    {
      Integer localInteger = (Integer)OnlineResourceAdapter.this.mDownloadedCount.get(this.mDownloadPath);
      if (localInteger == null)
        localInteger = Integer.valueOf(0);
      OnlineResourceAdapter.this.mDownloadedCount.put(this.mDownloadPath, Integer.valueOf(1 + localInteger.intValue()));
      OnlineResourceAdapter.this.mThumbnailDownloadQueue.remove(this.mDownloadPath);
      OnlineResourceAdapter.this.notifyDataSetChanged();
      if (!OnlineResourceAdapter.this.mThumbnailDownloadQueue.isEmpty())
      {
        Iterator localIterator = OnlineResourceAdapter.this.mThumbnailDownloadQueue.keySet().iterator();
        while (localIterator.hasNext())
          this.mDownloadPath = ((String)localIterator.next());
        OnlineResourceAdapter.access$402(OnlineResourceAdapter.this, new ThumbnailDownloadTask(OnlineResourceAdapter.this));
        OnlineResourceAdapter.this.mThumbnailDownloadTask.setTargetDirectory(ResourceConstants.THUMBNAIL_PATH + OnlineResourceAdapter.this.mResourceSetCode);
        ThumbnailDownloadTask localThumbnailDownloadTask = OnlineResourceAdapter.this.mThumbnailDownloadTask;
        String[] arrayOfString = new String[1];
        arrayOfString[0] = this.mDownloadPath;
        localThumbnailDownloadTask.execute(arrayOfString);
      }
    }
  }

  public class AsyncLoadMoreResourceTask extends AsyncAdapter.AsyncLoadMoreDataTask
  {
    public AsyncLoadMoreResourceTask()
    {
      super();
    }

    protected List<Resource> loadMoreData(AsyncAdapter.AsyncLoadMoreParams paramAsyncLoadMoreParams)
    {
      List localList1;
      if (paramAsyncLoadMoreParams.upwards)
        localList1 = null;
      while (true)
      {
        return localList1;
        localList1 = null;
        DownloadFileTask localDownloadFileTask = new DownloadFileTask();
        String str1 = ResourceHelper.getFormattedDirectoryPath(OnlineResourceAdapter.this.mMetaData.getString("com.miui.android.resourcebrowser.CACHE_LIST_FOLDER"));
        localDownloadFileTask.setTargetDirectory(str1 + OnlineResourceAdapter.this.mResourceSetCode);
        String str2 = OnlineHelper.getListURL(paramAsyncLoadMoreParams.cursor, 0, OnlineResourceAdapter.this.mCategoryCode, OnlineResourceAdapter.this.mKeyword, OnlineResourceAdapter.this.mMetaData);
        String[] arrayOfString = new String[1];
        arrayOfString[0] = str2;
        List localList2 = localDownloadFileTask.doInForeground(arrayOfString);
        if (paramAsyncLoadMoreParams.flag != OnlineResourceAdapter.this.mLoadMoreFlag)
          continue;
        String str3 = null;
        if (localList2.size() > 0)
          str3 = ((DownloadFileTask.DownloadFileEntry)localList2.get(0)).getPath();
        if (str3 == null)
          continue;
        localList1 = OnlineHelper.readResources(str3, OnlineResourceAdapter.this.mMetaData);
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.OnlineResourceAdapter
 * JD-Core Version:    0.6.0
 */