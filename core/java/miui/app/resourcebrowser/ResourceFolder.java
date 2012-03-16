package miui.app.resourcebrowser;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.widget.AsyncAdapter;
import miui.widget.AsyncAdapter.AsyncLoadDataTask;
import miui.widget.AsyncAdapter.AsyncLoadDataVisitor;

public abstract class ResourceFolder
  implements AsyncAdapter.AsyncLoadDataVisitor<Resource>, IntentConstants
{
  protected String mCacheFileName;
  protected Context mContext;
  protected Map<String, Object> mFileFlags = new HashMap();
  private boolean mFirstLoadData = true;
  protected String mFolderPath;
  protected Bundle mMetaData;

  public ResourceFolder(Context paramContext, Bundle paramBundle, String paramString)
  {
    this.mContext = paramContext;
    this.mMetaData = paramBundle;
    this.mFolderPath = paramString;
    if (!TextUtils.isEmpty(paramString))
    {
      String str = ResourceHelper.getFormattedDirectoryPath(this.mMetaData.getString("com.miui.android.resourcebrowser.CACHE_LIST_FOLDER"));
      File localFile = new File(str + paramString.replace('/', '_'));
      if (localFile.exists())
        localFile.delete();
      this.mCacheFileName = (str + "cache" + paramString.replace('/', '_'));
    }
  }

  protected void addItem(String paramString)
  {
  }

  protected Resource buildResource(String paramString)
  {
    Bundle localBundle = new Bundle();
    File localFile = new File(paramString);
    localBundle.putString("m_title", localFile.getName());
    localBundle.putString("filesize", ResourceHelper.getFormattedSize(this.mContext, localFile.length()));
    localBundle.putLong("m_lastupdate", localFile.lastModified());
    localBundle.putString("local_path", paramString);
    Resource localResource = new Resource();
    localResource.setInformation(localBundle);
    return localResource;
  }

  public boolean dataChanged()
  {
    return true;
  }

  public void loadData(AsyncAdapter<Resource>.AsyncLoadDataTask paramAsyncAdapter)
  {
    if (TextUtils.isEmpty(this.mFolderPath));
    while (true)
    {
      return;
      if (this.mFirstLoadData);
      int i;
      try
      {
        readCache();
        this.mFirstLoadData = false;
        i = 0;
        localFolderInfo = ResourceHelper.getFolderInfoCache(this.mFolderPath);
        if (localFolderInfo != null)
        {
          ArrayList localArrayList1 = new ArrayList(localFolderInfo.files.keySet());
          sortResource(localArrayList1, localFolderInfo);
          for (int j = 0; j < localArrayList1.size(); j++)
          {
            String str2 = (String)localArrayList1.get(j);
            if (!this.mFileFlags.containsKey(str2))
            {
              addItem(str2);
              i = 1;
            }
            Resource localResource = buildResource(str2);
            if (localResource == null)
              continue;
            Resource[] arrayOfResource = new Resource[1];
            arrayOfResource[0] = localResource;
            paramAsyncAdapter.onLoadData(arrayOfResource);
          }
        }
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        while (true)
          reset();
      }
      catch (Exception localException2)
      {
        ResourceHelper.FolderInfo localFolderInfo;
        while (true)
        {
          localException2.printStackTrace();
          reset();
        }
        ArrayList localArrayList2 = new ArrayList(this.mFileFlags.keySet());
        for (int k = 0; k < localArrayList2.size(); k++)
        {
          String str1 = (String)localArrayList2.get(k);
          if (localFolderInfo.files.containsKey(str1))
            continue;
          removeItem(str1);
          i = 1;
        }
      }
      if (i == 0)
        continue;
      try
      {
        saveCache();
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
      }
    }
  }

  // ERROR //
  protected void readCache()
    throws Exception
  {
    // Byte code:
    //   0: new 62	java/io/File
    //   3: dup
    //   4: aload_0
    //   5: getfield 93	miui/app/resourcebrowser/ResourceFolder:mCacheFileName	Ljava/lang/String;
    //   8: invokespecial 82	java/io/File:<init>	(Ljava/lang/String;)V
    //   11: astore_1
    //   12: aconst_null
    //   13: astore_2
    //   14: new 206	java/io/ObjectInputStream
    //   17: dup
    //   18: new 208	java/io/BufferedInputStream
    //   21: dup
    //   22: new 210	java/io/FileInputStream
    //   25: dup
    //   26: aload_1
    //   27: invokespecial 213	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   30: sipush 16384
    //   33: invokespecial 216	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   36: invokespecial 219	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
    //   39: astore_3
    //   40: aload_0
    //   41: aload_3
    //   42: invokevirtual 223	miui/app/resourcebrowser/ResourceFolder:readDataFromStream	(Ljava/io/ObjectInputStream;)V
    //   45: aload_3
    //   46: ifnull +7 -> 53
    //   49: aload_3
    //   50: invokevirtual 226	java/io/ObjectInputStream:close	()V
    //   53: return
    //   54: astore 4
    //   56: aload_2
    //   57: ifnull +7 -> 64
    //   60: aload_2
    //   61: invokevirtual 226	java/io/ObjectInputStream:close	()V
    //   64: aload 4
    //   66: athrow
    //   67: astore 4
    //   69: aload_3
    //   70: astore_2
    //   71: goto -15 -> 56
    //
    // Exception table:
    //   from	to	target	type
    //   14	40	54	finally
    //   40	45	67	finally
  }

  protected void readDataFromStream(ObjectInputStream paramObjectInputStream)
    throws Exception
  {
    this.mFileFlags = ((HashMap)paramObjectInputStream.readObject());
  }

  protected void removeItem(String paramString)
  {
    this.mFileFlags.remove(paramString);
  }

  protected void reset()
  {
    this.mFileFlags.clear();
  }

  // ERROR //
  protected void saveCache()
    throws Exception
  {
    // Byte code:
    //   0: new 62	java/io/File
    //   3: dup
    //   4: aload_0
    //   5: getfield 93	miui/app/resourcebrowser/ResourceFolder:mCacheFileName	Ljava/lang/String;
    //   8: invokespecial 82	java/io/File:<init>	(Ljava/lang/String;)V
    //   11: astore_1
    //   12: aload_1
    //   13: invokevirtual 89	java/io/File:delete	()Z
    //   16: pop
    //   17: aconst_null
    //   18: astore_3
    //   19: new 239	java/io/ObjectOutputStream
    //   22: dup
    //   23: new 241	java/io/BufferedOutputStream
    //   26: dup
    //   27: new 243	java/io/FileOutputStream
    //   30: dup
    //   31: aload_1
    //   32: invokespecial 244	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   35: invokespecial 247	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   38: invokespecial 248	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   41: astore 4
    //   43: aload_0
    //   44: aload 4
    //   46: invokevirtual 252	miui/app/resourcebrowser/ResourceFolder:writeDataToStream	(Ljava/io/ObjectOutputStream;)V
    //   49: aload 4
    //   51: ifnull +8 -> 59
    //   54: aload 4
    //   56: invokevirtual 253	java/io/ObjectOutputStream:close	()V
    //   59: return
    //   60: astore 5
    //   62: aload_3
    //   63: ifnull +7 -> 70
    //   66: aload_3
    //   67: invokevirtual 253	java/io/ObjectOutputStream:close	()V
    //   70: aload 5
    //   72: athrow
    //   73: astore 5
    //   75: aload 4
    //   77: astore_3
    //   78: goto -16 -> 62
    //
    // Exception table:
    //   from	to	target	type
    //   19	43	60	finally
    //   43	49	73	finally
  }

  protected void sortResource(List<String> paramList, ResourceHelper.FolderInfo paramFolderInfo)
  {
    if ((ResourceHelper.isSystemResource(this.mFolderPath)) || (ResourceHelper.isDataResource(this.mFolderPath)))
      Collections.sort(paramList);
    while (true)
    {
      return;
      Collections.sort(paramList, new Comparator(paramFolderInfo)
      {
        public int compare(String paramString1, String paramString2)
        {
          try
          {
            Long localLong = Long.valueOf(((ResourceHelper.FileInfo)this.val$folderInfo.files.get(paramString1)).modifiedTime);
            int j = Long.valueOf(((ResourceHelper.FileInfo)this.val$folderInfo.files.get(paramString2)).modifiedTime).compareTo(localLong);
            i = j;
            return i;
          }
          catch (Exception localException)
          {
            while (true)
              int i = 0;
          }
        }
      });
    }
  }

  protected void writeDataToStream(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(this.mFileFlags);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceFolder
 * JD-Core Version:    0.6.0
 */