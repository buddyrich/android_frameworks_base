package miui.content.res;

import android.app.MiuiThemeHelper;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ThemeResources
{
  public static final String ADVANCE_LOCKSCREEN_NAME = "advance";
  private static final String ATTR_NAME = "name";
  private static final String ATTR_PACKAGE = "package";
  public static final String FRAMEWORK_NAME = "framework-res";
  public static final String FRAMEWORK_PACKAGE = "android";
  public static final String ICONS_NAME = "icons";
  public static final String LANGUAGE_THEME_PATH = "/data/system/language";
  public static final String LOCKSCREEN_NAME = "lockscreen";
  public static final String LOCKSCREEN_WALLPAPER_NAME = "lock_wallpaper";
  public static final String MIUI_NAME = "framework-miui-res";
  public static final String MIUI_PACKAGE = "miui";
  public static final String SYSTEM_LANGUAGE_THEME_PATH = "/system/language";
  public static final String SYSTEM_THEME_PATH = "/system/media/theme/default";
  private static final String TAG_BOOLEAN = "bool";
  private static final String TAG_COLOR = "color";
  private static final String TAG_DIMEN = "dimen";
  private static final String TAG_INTEGER = "integer";
  private static final String TAG_ROOT = "MIUI_Theme_Values";
  private static final String TAG_STRING = "string";
  public static final String THEME_PATH = "/data/system/theme";
  public static final MetaData[] THEME_PATHS;
  public static final String THEME_VALUE_FILE = "theme_values.xml";
  private static final String TRUE = "true";
  public static final String WALLPAPER_NAME = "wallpaper";
  private static Drawable sLockWallpaperCache;
  private static long sLockWallpaperModifiedTime;
  private static final ThemeResources sMiuiResources;
  private static final Map<String, WeakReference<ThemeResources>> sPackageResources;
  private static final ThemeResources sSystem;
  private static final Map<String, ThemeZipManager> sThemeZipManagers;
  private SparseArray<CharSequence> mCharSequences = new SparseArray();
  private SparseArray<Integer> mDimensions = new SparseArray();
  private SparseArray<Integer> mIntegers = new SparseArray();
  private MetaData mMetaData;
  private String mPackageName;
  private Resources mResources;
  private ThemeZipManager mThemeZipManager;
  private ThemeResources mWrapped;

  static
  {
    MetaData[] arrayOfMetaData = new MetaData[4];
    arrayOfMetaData[0] = new MetaData("/system/language", false, true, false, false);
    arrayOfMetaData[1] = new MetaData("/data/system/language", false, true, false, false);
    arrayOfMetaData[2] = new MetaData("/system/media/theme/default", true, false, true, true);
    arrayOfMetaData[3] = new MetaData("/data/system/theme", true, false, true, true);
    THEME_PATHS = arrayOfMetaData;
    sPackageResources = new HashMap();
    sThemeZipManagers = new HashMap();
    sSystem = getTopLevelThemeResources(Resources.getSystem(), "android");
    sMiuiResources = getTopLevelThemeResources(Resources.getSystem(), "miui");
  }

  private ThemeResources(Resources paramResources, String paramString, MetaData paramMetaData)
  {
    init(paramResources, paramString, paramMetaData);
  }

  private ThemeResources(ThemeResources paramThemeResources, MetaData paramMetaData)
  {
    this.mWrapped = paramThemeResources;
    init(this.mWrapped.mResources, this.mWrapped.mPackageName, paramMetaData);
  }

  public static void clearLockWallpaperCache()
  {
    sLockWallpaperModifiedTime = 0L;
    sLockWallpaperCache = null;
  }

  private boolean confirmLoad()
  {
    boolean bool = false;
    if ("android".equals(this.mPackageName))
      if ((this.mThemeZipManager.needReloadFrameworkThemeValueFile()) || (this.mThemeZipManager.needReloadLockscreenThemeValueFile()))
        bool = true;
    while (true)
    {
      return bool;
      if ("miui".equals(this.mPackageName))
      {
        if ((!this.mThemeZipManager.needReloadMiuiThemeValueFile()) && (!this.mThemeZipManager.needReloadLockscreenThemeValueFile()))
          continue;
        bool = true;
        continue;
      }
      bool = this.mThemeZipManager.needReloadPackageThemeValueFile(this.mPackageName);
    }
  }

  public static Drawable getLockWallpaperCache(Context paramContext)
  {
    File localFile = getSystem().getThemeFile("lock_wallpaper");
    Drawable localDrawable;
    if (sLockWallpaperModifiedTime == localFile.lastModified())
      localDrawable = sLockWallpaperCache;
    while (true)
    {
      return localDrawable;
      sLockWallpaperModifiedTime = localFile.lastModified();
      sLockWallpaperCache = null;
      try
      {
        Bitmap localBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
        sLockWallpaperCache = new BitmapDrawable(paramContext.getResources(), localBitmap);
        label62: localDrawable = sLockWallpaperCache;
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        break label62;
      }
      catch (Exception localException)
      {
        break label62;
      }
    }
  }

  public static ThemeResources getMiuiResources()
  {
    return sMiuiResources;
  }

  public static ThemeResources getSystem()
  {
    return sSystem;
  }

  public static ThemeResources getThemeResources(Resources paramResources, String paramString)
  {
    ThemeResources localThemeResources = null;
    if (sPackageResources.containsKey(paramString))
      localThemeResources = (ThemeResources)((WeakReference)sPackageResources.get(paramString)).get();
    if (localThemeResources == null)
      synchronized (sPackageResources)
      {
        if (sPackageResources.containsKey(paramString))
          localThemeResources = (ThemeResources)((WeakReference)sPackageResources.get(paramString)).get();
        if (localThemeResources == null)
        {
          localThemeResources = getTopLevelThemeResources(paramResources, paramString);
          sPackageResources.put(paramString, new WeakReference(localThemeResources));
        }
      }
    return localThemeResources;
  }

  private ThemeZipManager getThemeZipManager()
  {
    if (!sThemeZipManagers.containsKey(this.mMetaData.themePath));
    synchronized (sThemeZipManagers)
    {
      if (!sThemeZipManagers.containsKey(this.mMetaData.themePath))
        sThemeZipManagers.put(this.mMetaData.themePath, new ThemeZipManager(this.mMetaData.themePath));
      return (ThemeZipManager)sThemeZipManagers.get(this.mMetaData.themePath);
    }
  }

  private static ThemeResources getTopLevelThemeResources(Resources paramResources, String paramString)
  {
    Object localObject = new ThemeResources(paramResources, paramString, THEME_PATHS[0]);
    int i = 1;
    while (i < THEME_PATHS.length)
    {
      ThemeResources localThemeResources = new ThemeResources((ThemeResources)localObject, THEME_PATHS[i]);
      i++;
      localObject = localThemeResources;
    }
    return (ThemeResources)localObject;
  }

  private void init(Resources paramResources, String paramString, MetaData paramMetaData)
  {
    this.mResources = paramResources;
    this.mPackageName = paramString;
    this.mMetaData = paramMetaData;
    this.mThemeZipManager = getThemeZipManager();
    reload();
  }

  private void load()
  {
    if ((this.mResources == null) || (!confirmLoad()));
    while (true)
    {
      return;
      this.mIntegers = new SparseArray();
      this.mCharSequences = new SparseArray();
      this.mDimensions = new SparseArray();
      if ("android".equals(this.mPackageName))
      {
        loadThemeValues(this.mThemeZipManager.getStreamFromFramework("theme_values.xml", null, null), "android");
        loadThemeValues(this.mThemeZipManager.getStreamFromAdvanceLockscreen("theme_values.xml", null), "android");
        continue;
      }
      if ("miui".equals(this.mPackageName))
      {
        loadThemeValues(this.mThemeZipManager.getStreamFromMiui("theme_values.xml", null, null), "miui");
        continue;
      }
      loadThemeValues(this.mThemeZipManager.getStreamFromPackage("theme_values.xml", this.mPackageName, null), this.mPackageName);
    }
  }

  // ERROR //
  private void loadThemeValues(InputStream paramInputStream, String paramString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +46 -> 47
    //   4: invokestatic 298	org/xmlpull/v1/XmlPullParserFactory:newInstance	()Lorg/xmlpull/v1/XmlPullParserFactory;
    //   7: invokevirtual 302	org/xmlpull/v1/XmlPullParserFactory:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   10: astore 7
    //   12: new 304	java/io/BufferedInputStream
    //   15: dup
    //   16: aload_1
    //   17: sipush 8192
    //   20: invokespecial 307	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   23: astore 8
    //   25: aload 7
    //   27: aload 8
    //   29: aconst_null
    //   30: invokeinterface 312 3 0
    //   35: aload_0
    //   36: aload 7
    //   38: aload_2
    //   39: invokespecial 316	miui/content/res/ThemeResources:mergeThemeValues	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   42: aload 8
    //   44: invokevirtual 321	java/io/InputStream:close	()V
    //   47: return
    //   48: astore 10
    //   50: aload 10
    //   52: invokevirtual 324	java/io/IOException:printStackTrace	()V
    //   55: goto -8 -> 47
    //   58: astore 5
    //   60: aload_1
    //   61: invokevirtual 321	java/io/InputStream:close	()V
    //   64: goto -17 -> 47
    //   67: astore 6
    //   69: aload 6
    //   71: invokevirtual 324	java/io/IOException:printStackTrace	()V
    //   74: goto -27 -> 47
    //   77: astore_3
    //   78: aload_1
    //   79: invokevirtual 321	java/io/InputStream:close	()V
    //   82: aload_3
    //   83: athrow
    //   84: astore 4
    //   86: aload 4
    //   88: invokevirtual 324	java/io/IOException:printStackTrace	()V
    //   91: goto -9 -> 82
    //   94: astore_3
    //   95: aload 8
    //   97: astore_1
    //   98: goto -20 -> 78
    //   101: astore 9
    //   103: aload 8
    //   105: astore_1
    //   106: goto -46 -> 60
    //
    // Exception table:
    //   from	to	target	type
    //   42	47	48	java/io/IOException
    //   4	25	58	org/xmlpull/v1/XmlPullParserException
    //   60	64	67	java/io/IOException
    //   4	25	77	finally
    //   78	82	84	java/io/IOException
    //   25	42	94	finally
    //   25	42	101	org/xmlpull/v1/XmlPullParserException
  }

  private void mergeThemeValues(XmlPullParser paramXmlPullParser, String paramString)
  {
    try
    {
      int i;
      do
        i = paramXmlPullParser.next();
      while ((i != 2) && (i != 1));
      if (i != 2)
        throw new XmlPullParserException("No start tag found");
      if (!paramXmlPullParser.getName().equals("MIUI_Theme_Values"))
        throw new XmlPullParserException("Unexpected start tag: found " + paramXmlPullParser.getName() + ", expected " + "MIUI_Theme_Values");
      while (true)
      {
        int j = paramXmlPullParser.next();
        if ((j != 2) && (j != 1))
          continue;
        String str1 = paramXmlPullParser.getName().trim();
        if (str1 == null)
          break;
        String str2 = null;
        String str3 = null;
        k = -1 + paramXmlPullParser.getAttributeCount();
        if (k >= 0)
        {
          String str5 = paramXmlPullParser.getAttributeName(k).trim();
          if (str5.equals("name"))
          {
            str2 = paramXmlPullParser.getAttributeValue(k);
            break label437;
          }
          if (!str5.equals("package"))
            break label437;
          str3 = paramXmlPullParser.getAttributeValue(k);
          break label437;
        }
        String str4 = paramXmlPullParser.nextText();
        if ((str2 == null) || (TextUtils.isEmpty(str4)))
          continue;
        if (str3 == null);
        for (int m = this.mResources.getIdentifier(str2, str1, paramString); ; m = this.mResources.getIdentifier(str2, str1, str3))
        {
          if (m <= 0)
            break label320;
          if (!str1.equals("bool"))
            break label322;
          SparseArray localSparseArray = this.mIntegers;
          if (!"true".equals(str4.trim()))
            break label448;
          n = 1;
          localSparseArray.put(m, Integer.valueOf(n));
          break;
        }
        label320: continue;
        label322: if ((str1.equals("color")) || (str1.equals("integer")))
        {
          this.mIntegers.put(m, Integer.valueOf(XmlUtils.convertValueToUnsignedInt(str4.trim(), 0)));
          continue;
        }
        if (str1.equals("string"))
        {
          this.mCharSequences.put(m, str4);
          continue;
        }
        if (!str1.equals("dimen"))
          continue;
        Integer localInteger = MiuiThemeHelper.parseDimension(str4.toString());
        if (localInteger == null)
          continue;
        this.mDimensions.put(m, localInteger);
      }
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      return;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      while (true)
      {
        int k;
        continue;
        k--;
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        continue;
        int n = 0;
      }
    }
    catch (Exception localException)
    {
      label431: label437: label448: break label431;
    }
  }

  private void reset()
  {
    this.mThemeZipManager.reset(this.mPackageName);
    if ("android".equals(this.mPackageName))
      this.mThemeZipManager.resetSystem();
  }

  public InputStream getAwesomeLockscreenFileStream(String paramString, int[] paramArrayOfInt)
  {
    InputStream localInputStream = this.mThemeZipManager.getStreamFromAdvanceLockscreen(paramString, paramArrayOfInt);
    if ((localInputStream == null) && (!this.mThemeZipManager.containsAdvanceLockscreen()) && (this.mWrapped != null))
      localInputStream = this.mWrapped.getAwesomeLockscreenFileStream(paramString, paramArrayOfInt);
    return localInputStream;
  }

  public Bitmap getIcon(Resources paramResources, String paramString)
  {
    Bitmap localBitmap = BitmapFactory.decodeStream(this.mThemeZipManager.getStreamFromIcon(paramString, null));
    if (localBitmap != null)
      localBitmap.setDensity(0);
    while (true)
    {
      return localBitmap;
      if ((this.mThemeZipManager.containsIcons()) || (this.mWrapped == null))
        continue;
      localBitmap = this.mWrapped.getIcon(paramResources, paramString);
    }
  }

  public String getPackageName()
  {
    return this.mPackageName;
  }

  public Resources getResources()
  {
    return this.mResources;
  }

  public CharSequence getThemeCharSequence(int paramInt)
  {
    CharSequence localCharSequence = null;
    if (this.mMetaData.supportCharSequence)
      localCharSequence = (CharSequence)this.mCharSequences.get(paramInt);
    if ((localCharSequence == null) && (this.mWrapped != null))
      localCharSequence = this.mWrapped.getThemeCharSequence(paramInt);
    return localCharSequence;
  }

  public Integer getThemeDimension(int paramInt)
  {
    Integer localInteger = null;
    if (this.mMetaData.supportDimension)
      localInteger = (Integer)this.mDimensions.get(paramInt);
    if ((localInteger == null) && (this.mWrapped != null))
      localInteger = this.mWrapped.getThemeDimension(paramInt);
    return localInteger;
  }

  public File getThemeFile(String paramString)
  {
    File localFile = null;
    if (this.mMetaData.supportFile)
      localFile = new File(this.mMetaData.themePath + "/" + paramString);
    if (((localFile == null) || (!localFile.exists())) && (this.mWrapped != null))
      localFile = this.mWrapped.getThemeFile(paramString);
    return localFile;
  }

  public InputStream getThemeFileStream(int paramInt, String paramString1, String paramString2)
  {
    InputStream localInputStream = null;
    if (this.mMetaData.supportFile)
    {
      if (1 != paramInt)
        break label57;
      localInputStream = this.mThemeZipManager.getStreamFromFramework(paramString1, paramString2, null);
    }
    while (true)
    {
      if ((localInputStream == null) && (this.mWrapped != null))
        localInputStream = this.mWrapped.getThemeFileStream(paramInt, paramString1, paramString2);
      return localInputStream;
      label57: if (2 == paramInt)
      {
        localInputStream = this.mThemeZipManager.getStreamFromMiui(paramString1, paramString2, null);
        continue;
      }
      localInputStream = this.mThemeZipManager.getStreamFromPackage(paramString1, this.mPackageName, null);
    }
  }

  public Integer getThemeInt(int paramInt)
  {
    Integer localInteger = null;
    if (this.mMetaData.supportInt)
      localInteger = (Integer)this.mIntegers.get(paramInt);
    if ((localInteger == null) && (this.mWrapped != null))
      localInteger = this.mWrapped.getThemeInt(paramInt);
    return localInteger;
  }

  public String getThemePath()
  {
    return this.mMetaData.themePath;
  }

  public ThemeResources getWrappedResources()
  {
    return this.mWrapped;
  }

  public boolean hasAwesomeLockscreen()
  {
    boolean bool = this.mThemeZipManager.containsAdvanceLockscreen();
    if ((!bool) && (!this.mThemeZipManager.containsAdvanceLockscreen()) && (this.mWrapped != null))
      bool = this.mWrapped.hasAwesomeLockscreen();
    return bool;
  }

  public boolean hasIcon(String paramString)
  {
    boolean bool = this.mThemeZipManager.containsIcon(paramString);
    if ((!bool) && (!this.mThemeZipManager.containsIcons()) && (this.mWrapped != null))
      bool = this.mWrapped.hasIcon(paramString);
    return bool;
  }

  public boolean hasTypedTheme()
  {
    boolean bool = false;
    if ((0 == 0) && (this.mMetaData.supportInt))
    {
      if (this.mIntegers.size() > 0)
        bool = true;
    }
    else if ((!bool) && (this.mMetaData.supportDimension))
      if (this.mDimensions.size() <= 0)
        break label80;
    label80: for (bool = true; ; bool = false)
    {
      if ((!bool) && (this.mWrapped != null))
        bool = this.mWrapped.hasTypedTheme();
      return bool;
      bool = false;
      break;
    }
  }

  public void mergeThemeValues(int paramInt)
  {
    if (this.mResources != null)
      mergeThemeValues(this.mResources.getXml(paramInt), this.mPackageName);
  }

  public void reload()
  {
    reset();
    load();
  }

  public void resetIcons()
  {
    this.mThemeZipManager.resetIcons();
    if (this.mWrapped != null)
      this.mWrapped.resetIcons();
  }

  protected static class MetaData
  {
    public boolean supportCharSequence;
    public boolean supportDimension;
    public boolean supportFile;
    public boolean supportInt;
    public String themePath;

    public MetaData(String paramString)
    {
      this(paramString, true, true, true, true);
    }

    public MetaData(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
    {
      this.themePath = paramString;
      this.supportInt = paramBoolean1;
      this.supportCharSequence = paramBoolean2;
      this.supportDimension = paramBoolean3;
      this.supportFile = paramBoolean4;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.content.res.ThemeResources
 * JD-Core Version:    0.6.0
 */