package miui.app.resourcebrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class ZipResourceInfo
  implements IntentConstants
{
  protected static final String AUTHOR_TAG = "author";
  protected static final String DESIGNER_TAG = "designer";
  protected static final int MAXIMUM_PREVIEW_COUNT = 10;
  protected static final String PLATFORM_VERSION_TAG = "platformVersion";
  protected static final String TITLE_TAG = "title";
  protected static final String UI_VERSION_TAG = "uiVersion";
  protected static final String VERSION_TAG = "version";
  public String mAuthor;
  public Context mContext;
  public String mDesigner;
  protected String mEncodedPath;
  public long mLastModified;
  protected HashMap<String, String> mNvp = new HashMap();
  public String mPath;
  public int mPlatformVersion;
  protected String[] mPrefix;
  public ArrayList<String> mPreviews = new ArrayList();
  public long mSize;
  public ArrayList<String> mThumbnails = new ArrayList();
  public String mTitle;
  public String mVersion;
  public ZipResourceCache mZipResourceCache = getCacheInstance();

  protected ZipResourceInfo(Context paramContext, String paramString, ZipResourceCache paramZipResourceCache)
    throws IOException, ParserConfigurationException, SAXException
  {
    this.mContext = paramContext;
    this.mPath = paramString;
    this.mEncodedPath = paramString.replace('/', '_');
    try
    {
      this.mPrefix = ((Activity)this.mContext).getIntent().getBundleExtra("META_DATA").getStringArray("com.miui.android.resourcebrowser.PREVIEW_PREFIX");
      label91: File localFile = new File(paramString);
      this.mLastModified = localFile.lastModified();
      if (this.mLastModified > System.currentTimeMillis())
      {
        localFile.setLastModified(System.currentTimeMillis());
        this.mLastModified = localFile.lastModified();
      }
      this.mSize = localFile.length();
      if ((paramZipResourceCache != null) && (paramZipResourceCache.valid()))
      {
        this.mTitle = paramZipResourceCache.title;
        this.mAuthor = paramZipResourceCache.author;
        this.mDesigner = paramZipResourceCache.designer;
        this.mVersion = paramZipResourceCache.version;
        this.mPlatformVersion = paramZipResourceCache.platformVersion;
        this.mNvp = paramZipResourceCache.nvp;
      }
      while (true)
      {
        return;
        parseBasicInformation(paramString);
        this.mZipResourceCache.filePath = this.mPath;
        this.mZipResourceCache.lastModifyTime = this.mLastModified;
        this.mZipResourceCache.fileSize = this.mSize;
        this.mZipResourceCache.title = this.mTitle;
        this.mZipResourceCache.author = this.mAuthor;
        this.mZipResourceCache.designer = this.mDesigner;
        this.mZipResourceCache.version = this.mVersion;
        this.mZipResourceCache.platformVersion = this.mPlatformVersion;
        this.mZipResourceCache.nvp = this.mNvp;
      }
    }
    catch (Exception localException)
    {
      break label91;
    }
  }

  // ERROR //
  public static ZipResourceInfo createZipResourceInfo(Context paramContext, String paramString, ZipResourceCache paramZipResourceCache, Object[] paramArrayOfObject)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: new 2	miui/app/resourcebrowser/ZipResourceInfo
    //   6: dup
    //   7: aload_0
    //   8: aload_1
    //   9: aload_2
    //   10: invokespecial 188	miui/app/resourcebrowser/ZipResourceInfo:<init>	(Landroid/content/Context;Ljava/lang/String;Lmiui/app/resourcebrowser/ZipResourceCache;)V
    //   13: astore 5
    //   15: aload_2
    //   16: ifnull +10 -> 26
    //   19: aload_2
    //   20: invokevirtual 149	miui/app/resourcebrowser/ZipResourceCache:valid	()Z
    //   23: ifne +14 -> 37
    //   26: aload 5
    //   28: invokevirtual 191	miui/app/resourcebrowser/ZipResourceInfo:loadResourcePreviews	()V
    //   31: aload 5
    //   33: getfield 82	miui/app/resourcebrowser/ZipResourceInfo:mZipResourceCache	Lmiui/app/resourcebrowser/ZipResourceCache;
    //   36: astore_2
    //   37: aload 5
    //   39: aload_2
    //   40: invokevirtual 195	miui/app/resourcebrowser/ZipResourceInfo:loadPreviewsFromCache	(Lmiui/app/resourcebrowser/ZipResourceCache;)V
    //   43: aload_2
    //   44: ifnull +9 -> 53
    //   47: aload 5
    //   49: aload_2
    //   50: putfield 82	miui/app/resourcebrowser/ZipResourceInfo:mZipResourceCache	Lmiui/app/resourcebrowser/ZipResourceCache;
    //   53: aload 5
    //   55: astore 4
    //   57: aload 4
    //   59: areturn
    //   60: astore 6
    //   62: aload 6
    //   64: invokevirtual 198	java/lang/Exception:printStackTrace	()V
    //   67: goto -10 -> 57
    //   70: astore 6
    //   72: aload 5
    //   74: astore 4
    //   76: goto -14 -> 62
    //
    // Exception table:
    //   from	to	target	type
    //   3	15	60	java/lang/Exception
    //   19	53	70	java/lang/Exception
  }

  public void clearCache()
  {
    this.mZipResourceCache = null;
  }

  protected String extract(ZipFile paramZipFile, ZipEntry paramZipEntry, String paramString)
  {
    String str = getPreviewPathPrefix() + File.separator + paramString;
    File localFile = new File(str);
    if ((localFile.exists()) && (localFile.lastModified() < this.mLastModified))
      localFile.delete();
    if (!localFile.exists());
    try
    {
      ResourceHelper.writeTo(paramZipFile.getInputStream(paramZipEntry), str);
      return str;
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  public ZipResourceCache getCache()
  {
    return this.mZipResourceCache;
  }

  protected ZipResourceCache getCacheInstance()
  {
    return new ZipResourceCache.DefaultZipCacheImpl();
  }

  public Bundle getInformation()
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("filesize", ResourceHelper.getFormattedSize(this.mContext, this.mSize));
    localBundle.putString("m_title", this.mTitle);
    localBundle.putString("nickname", this.mAuthor);
    localBundle.putString("version", this.mVersion);
    localBundle.putInt("ui_version", this.mPlatformVersion);
    localBundle.putLong("m_lastupdate", this.mLastModified);
    localBundle.putString("local_path", this.mPath);
    localBundle.putStringArrayList("local_preview", this.mPreviews);
    localBundle.putStringArrayList("local_thumbnail", this.mThumbnails);
    localBundle.putSerializable("RESOURCE_NVP", this.mNvp);
    return localBundle;
  }

  protected String getPreviewPathPrefix()
  {
    return ResourceHelper.getFormattedDirectoryPath(ResourceConstants.PREVIEW_PATH + this.mEncodedPath);
  }

  protected boolean loadPreview(ZipFile paramZipFile, String paramString, ArrayList<String> paramArrayList)
  {
    ZipEntry localZipEntry = paramZipFile.getEntry(paramString);
    if (localZipEntry != null)
      paramArrayList.add(extract(paramZipFile, localZipEntry, paramString));
    for (int i = 1; ; i = 0)
      return i;
  }

  protected ArrayList<String> loadPreviews(ZipFile paramZipFile, String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < 10; i++)
    {
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = paramString;
      arrayOfObject1[1] = Integer.valueOf(i);
      if (loadPreview(paramZipFile, String.format("%s%d.jpg", arrayOfObject1), localArrayList))
        continue;
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = paramString;
      arrayOfObject2[1] = Integer.valueOf(i);
      loadPreview(paramZipFile, String.format("%s%d.png", arrayOfObject2), localArrayList);
    }
    return localArrayList;
  }

  protected void loadPreviewsFromCache(ZipResourceCache paramZipResourceCache)
  {
    if (paramZipResourceCache == null);
    while (true)
    {
      return;
      this.mPreviews = paramZipResourceCache.previews;
      this.mThumbnails = paramZipResourceCache.thumbnails;
    }
  }

  protected void loadResourcePreviews()
  {
    try
    {
      ZipFile localZipFile = new ZipFile(this.mPath);
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      for (int i = 0; i < this.mPrefix.length; i++)
      {
        String str = "preview/";
        if (!TextUtils.isEmpty(this.mPrefix[i]))
          str = str + this.mPrefix[i];
        ArrayList localArrayList3 = loadPreviews(localZipFile, str + "big_");
        ArrayList localArrayList4 = loadPreviews(localZipFile, str + "small_");
        if (localArrayList3.size() == 0)
          localArrayList3.addAll(loadPreviews(localZipFile, str));
        if (localArrayList4.size() == 0)
          localArrayList4 = localArrayList3;
        localArrayList1.addAll(localArrayList4);
        localArrayList2.addAll(localArrayList3);
      }
      this.mZipResourceCache.thumbnails = localArrayList1;
      this.mZipResourceCache.previews = localArrayList2;
      return;
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  protected void parseBasicInformation(String paramString)
    throws ZipException, IOException, ParserConfigurationException, SAXException
  {
    String str1 = null;
    this.mNvp = ZipFileHelper.getZipResourceDescribeInfo(paramString, "description.xml");
    String str2;
    if (this.mNvp != null)
    {
      this.mTitle = ((String)this.mNvp.get("title"));
      if (this.mTitle != null)
        break label271;
      str2 = null;
    }
    while (true)
    {
      this.mTitle = str2;
      this.mAuthor = ((String)this.mNvp.get("author"));
      String str3;
      label76: String str4;
      if (this.mAuthor == null)
      {
        str3 = null;
        this.mAuthor = str3;
        this.mDesigner = ((String)this.mNvp.get("designer"));
        if (this.mDesigner != null)
          break label294;
        str4 = null;
        label108: this.mDesigner = str4;
        this.mVersion = ((String)this.mNvp.get("version"));
        if (this.mVersion != null)
          break label306;
        label137: this.mVersion = str1;
      }
      try
      {
        if (this.mNvp.containsKey("platformVersion"));
        for (this.mPlatformVersion = Integer.parseInt((String)this.mNvp.get("platformVersion")); ; this.mPlatformVersion = Integer.parseInt((String)this.mNvp.get("uiVersion")))
        {
          if (TextUtils.isEmpty(this.mTitle))
            this.mTitle = this.mPath.substring(1 + this.mPath.lastIndexOf("/"), this.mPath.lastIndexOf("."));
          if (TextUtils.isEmpty(this.mAuthor))
            this.mAuthor = this.mContext.getResources().getString(51118091);
          if (TextUtils.isEmpty(this.mVersion))
            this.mVersion = DateFormat.format("yyyy.MM.d", this.mLastModified).toString();
          return;
          label271: str2 = this.mTitle.trim();
          break;
          str3 = this.mAuthor.trim();
          break label76;
          label294: str4 = this.mDesigner.trim();
          break label108;
          label306: str1 = this.mVersion.trim();
          break label137;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        while (true)
          this.mPlatformVersion = 0;
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ZipResourceInfo
 * JD-Core Version:    0.6.0
 */