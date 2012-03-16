package miui.app.resourcebrowser;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import miui.os.AsyncTaskObserver;
import miui.widget.AsyncAdapter;
import miui.widget.AsyncAdapter.AsyncLoadDataTask;
import miui.widget.AsyncAdapter.AsyncLoadDataVisitor;

public class LocalResourceAdapter extends ResourceAdapter
{
  protected List<AsyncAdapter.AsyncLoadDataVisitor<Resource>> mVisitors = null;

  public LocalResourceAdapter(Context paramContext, Bundle paramBundle)
  {
    super(paramContext, paramBundle);
  }

  private void checkVersionStatus(List<Resource> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramList.size(); i++)
    {
      Resource localResource3 = (Resource)paramList.get(i);
      Pair localPair = ResourceHelper.parseIdVersion(localResource3.getLocalPath());
      if (localPair == null)
        continue;
      Bundle localBundle = localResource3.getInformation();
      localBundle.putString("x_id", (String)localPair.first);
      localBundle.putString("version", Long.toString(((Integer)localPair.second).intValue()));
      localResource3.setInformation(localBundle);
      localArrayList.add(localResource3);
      localStringBuffer.append((String)localPair.first);
      localStringBuffer.append(",");
    }
    if (localArrayList.size() < 1);
    while (true)
    {
      return;
      String str1 = this.mMetaData.getString("com.miui.android.resourcebrowser.VERSION_URL");
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localStringBuffer.substring(0, -1 + localStringBuffer.length());
      String str2 = OnlineHelper.getEncryptedURL(String.format(str1, arrayOfObject));
      String str3 = ResourceConstants.VERSION_PATH + this.mResourceSetCode;
      String str4 = OnlineHelper.getFilePathByURL(str3, str2);
      File localFile = new File(str4);
      if (localFile.exists())
      {
        Map localMap = OnlineHelper.readSpecifiedResources(str4, this.mMetaData);
        for (int j = 0; j < localArrayList.size(); j++)
        {
          Resource localResource1 = (Resource)localArrayList.get(j);
          Resource localResource2 = (Resource)localMap.get(localResource1.getId());
          if (localResource2 == null)
            continue;
          if (localResource1.getVersion() < localResource2.getVersion())
            localResource1.setStatus(1);
          ResourceHelper.copyResourceInformation(localResource2, localResource1);
        }
        notifyDataSetChanged();
        if (System.currentTimeMillis() - localFile.lastModified() < 600000L)
          continue;
      }
      DownloadVersionTask localDownloadVersionTask = new DownloadVersionTask();
      localDownloadVersionTask.setTargetDirectory(str3);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = str2;
      localDownloadVersionTask.execute(arrayOfString);
    }
  }

  protected boolean checkResourceModifyTime()
  {
    return false;
  }

  protected int getFlagId(Resource paramResource)
  {
    int i = super.getFlagId(paramResource);
    if (i == 50462778)
      i = 0;
    return i;
  }

  protected AsyncAdapter<Resource>.AsyncLoadDataTask getLoadDataTask()
  {
    AsyncLoadResourceTask localAsyncLoadResourceTask = new AsyncLoadResourceTask();
    localAsyncLoadResourceTask.addObserver((AsyncTaskObserver)this.mContext);
    List localList = getVisitors();
    for (int i = 0; i < localList.size(); i++)
      localAsyncLoadResourceTask.addVisitor((AsyncAdapter.AsyncLoadDataVisitor)localList.get(i));
    return localAsyncLoadResourceTask;
  }

  protected AsyncAdapter.AsyncLoadDataVisitor<Resource> getVisitor(String paramString)
  {
    Object localObject;
    if (this.mResourceSetCategory == 1)
      localObject = new ImageResourceFolder(this.mContext, this.mMetaData, paramString);
    while (true)
    {
      return localObject;
      if (this.mResourceSetCategory == 2)
      {
        localObject = new AudioResourceFolder(this.mContext, this.mMetaData, paramString);
        continue;
      }
      localObject = new ZipResourceFolder(this.mContext, this.mMetaData, paramString);
    }
  }

  protected List<AsyncAdapter.AsyncLoadDataVisitor<Resource>> getVisitors()
  {
    Object localObject;
    if (this.mVisitors != null)
      localObject = this.mVisitors;
    while (true)
    {
      return localObject;
      localObject = new ArrayList();
      String[] arrayOfString = this.mMetaData.getStringArray("com.miui.android.resourcebrowser.SOURCE_FOLDERS");
      if (this.mResourceSetCategory == 2)
      {
        AudioResourceFolder localAudioResourceFolder = new AudioResourceFolder(this.mContext, this.mMetaData, null);
        localAudioResourceFolder.enableMuteOption(this.mMetaData.getBoolean("android.intent.extra.ringtone.SHOW_SILENT", true));
        localAudioResourceFolder.enableDefaultOption(this.mMetaData.getBoolean("android.intent.extra.ringtone.SHOW_DEFAULT", false));
        ((List)localObject).add(localAudioResourceFolder);
      }
      for (int i = 0; i < arrayOfString.length; i++)
        ((List)localObject).add(getVisitor(arrayOfString[i]));
      this.mVisitors = ((List)localObject);
    }
  }

  protected void postLoadData(List<Resource> paramList)
  {
    if (this.mMetaData.getBoolean("com.miui.android.resourcebrowser.VERSION_SUPPORTED"))
      checkVersionStatus(paramList);
  }

  public class DownloadVersionTask extends DownloadFileTask
  {
    public DownloadVersionTask()
    {
    }

    protected void onPostExecute(List<DownloadFileTask.DownloadFileEntry> paramList)
    {
      if ((paramList.size() > 0) && (((DownloadFileTask.DownloadFileEntry)paramList.get(0)).getPath() != null))
        LocalResourceAdapter.this.checkVersionStatus(LocalResourceAdapter.this.mResourceSet);
    }
  }

  public class AsyncLoadResourceTask extends AsyncAdapter.AsyncLoadDataTask
  {
    public AsyncLoadResourceTask()
    {
      super();
    }

    protected Resource[] loadData(int paramInt)
    {
      return null;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.LocalResourceAdapter
 * JD-Core Version:    0.6.0
 */