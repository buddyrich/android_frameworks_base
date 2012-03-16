package miui.app.resourcebrowser;

import android.content.Context;
import android.os.Bundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import miui.widget.AsyncAdapter;

public class ZipResourceFolder extends ResourceFolder
{
  private Map<String, ZipResourceCache> mZipResourceInfoCache = new HashMap();

  public ZipResourceFolder(Context paramContext, Bundle paramBundle, String paramString)
  {
    super(paramContext, paramBundle, paramString);
  }

  protected void addItem(String paramString)
  {
    super.addItem(paramString);
  }

  protected Resource buildResource(String paramString)
  {
    ZipResourceCache localZipResourceCache = (ZipResourceCache)this.mZipResourceInfoCache.get(paramString);
    ZipResourceInfo localZipResourceInfo = createZipResource(this.mContext, paramString, localZipResourceCache);
    Resource localResource;
    if (localZipResourceInfo == null)
      localResource = null;
    while (true)
    {
      return localResource;
      this.mZipResourceInfoCache.put(paramString, localZipResourceInfo.getCache());
      localZipResourceInfo.clearCache();
      localResource = new Resource();
      localResource.setInformation(localZipResourceInfo.getInformation());
      if (ResourceHelper.isCompatiblePlatformVersion(localResource.getPlatformVersion(), this.mMetaData.getInt("com.miui.android.resourcebrowser.PLATFORM_VERSION_START"), this.mMetaData.getInt("com.miui.android.resourcebrowser.PLATFORM_VERSION_END")))
        continue;
      localResource = null;
    }
  }

  protected ZipResourceInfo createZipResource(Context paramContext, String paramString, ZipResourceCache paramZipResourceCache)
  {
    if (!ResourceHelper.isZipResource(paramString));
    for (ZipResourceInfo localZipResourceInfo = null; ; localZipResourceInfo = ZipResourceInfo.createZipResourceInfo(paramContext, paramString, paramZipResourceCache, new Object[0]))
      return localZipResourceInfo;
  }

  public boolean dataChanged()
  {
    int i = 1;
    ResourceHelper.FolderInfo localFolderInfo = ResourceHelper.getFolderInfoCache(this.mFolderPath);
    if (localFolderInfo != null)
    {
      Iterator localIterator1 = this.mFileFlags.keySet().iterator();
      String str2;
      do
      {
        if (!localIterator1.hasNext())
          break;
        str2 = (String)localIterator1.next();
      }
      while (localFolderInfo.files.containsKey(str2));
    }
    while (true)
    {
      return i;
      Iterator localIterator2 = localFolderInfo.files.keySet().iterator();
      while (true)
        if (localIterator2.hasNext())
        {
          String str1 = (String)localIterator2.next();
          if ((this.mFileFlags.containsKey(str1)) || (!ResourceHelper.isZipResource(str1)))
            continue;
          break;
        }
      i = 0;
    }
  }

  public void loadData(AsyncAdapter<Resource>.AsyncLoadDataTask paramAsyncAdapter)
  {
    super.loadData(paramAsyncAdapter);
  }

  protected void readDataFromStream(ObjectInputStream paramObjectInputStream)
    throws Exception
  {
    super.readDataFromStream(paramObjectInputStream);
    this.mZipResourceInfoCache = ((HashMap)paramObjectInputStream.readObject());
  }

  protected void removeItem(String paramString)
  {
    super.removeItem(paramString);
    this.mZipResourceInfoCache.remove(paramString);
  }

  protected void reset()
  {
    super.reset();
    this.mZipResourceInfoCache.clear();
  }

  protected void writeDataToStream(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    super.writeDataToStream(paramObjectOutputStream);
    paramObjectOutputStream.writeObject(this.mZipResourceInfoCache);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ZipResourceFolder
 * JD-Core Version:    0.6.0
 */