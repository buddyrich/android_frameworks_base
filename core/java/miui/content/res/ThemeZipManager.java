package miui.content.res;

import android.text.TextUtils;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ThemeZipManager
{
  private ThemeZipFile mFrameworkZipFile;
  private ThemeZipFile mIconsZipFile;
  private ThemeZipFile mLockscreenZipFile;
  private ThemeZipFile mMiuiZipFile;
  private Map<String, ThemeZipFile> mPackageZipFiles;
  private String mThemePath;

  public ThemeZipManager(String paramString)
  {
    if (paramString.endsWith("/"));
    while (true)
    {
      this.mThemePath = paramString;
      this.mFrameworkZipFile = new ThemeZipFile(this.mThemePath + "framework-res");
      this.mMiuiZipFile = new ThemeZipFile(this.mThemePath + "framework-miui-res");
      this.mIconsZipFile = new ThemeZipFile(this.mThemePath + "icons");
      this.mLockscreenZipFile = new ThemeZipFile(this.mThemePath + "lockscreen");
      this.mPackageZipFiles = new HashMap();
      return;
      paramString = paramString + "/";
    }
  }

  private ThemeZipFile getPackageThemeZipFile(String paramString)
  {
    if (!this.mPackageZipFiles.containsKey(paramString));
    synchronized (this.mPackageZipFiles)
    {
      if (!this.mPackageZipFiles.containsKey(paramString))
        this.mPackageZipFiles.put(paramString, new ThemeZipFile(this.mThemePath + paramString));
      return (ThemeZipFile)this.mPackageZipFiles.get(paramString);
    }
  }

  public boolean containsAdvanceLockscreen()
  {
    return this.mLockscreenZipFile.containsEntry("advance/manifest.xml");
  }

  public boolean containsIcon(String paramString)
  {
    return this.mIconsZipFile.containsEntry(paramString);
  }

  public boolean containsIcons()
  {
    return this.mIconsZipFile.exists();
  }

  public InputStream getStreamFromAdvanceLockscreen(String paramString, int[] paramArrayOfInt)
  {
    return this.mLockscreenZipFile.getInputStream("advance/" + paramString, paramArrayOfInt);
  }

  public InputStream getStreamFromFramework(String paramString1, String paramString2, int[] paramArrayOfInt)
  {
    InputStream localInputStream1 = null;
    String str = paramString1.substring(1 + paramString1.lastIndexOf('/'));
    if ((str.equals("sym_def_app_icon.png")) || (str.equals("icon_search.png")) || (str.equals("icon_browser.png")))
    {
      localInputStream1 = this.mIconsZipFile.getInputStream(str, paramArrayOfInt);
      if ((localInputStream1 == null) && (!TextUtils.isEmpty(paramString2)))
        localInputStream1 = getStreamFromPackage("framework-res/" + paramString1, paramString2, paramArrayOfInt);
      if (localInputStream1 == null)
        localInputStream1 = this.mFrameworkZipFile.getInputStream(paramString1, paramArrayOfInt);
    }
    for (InputStream localInputStream2 = localInputStream1; ; localInputStream2 = null)
    {
      return localInputStream2;
      if (!str.equals("default_wallpaper.jpg"))
        break;
    }
  }

  public InputStream getStreamFromIcon(String paramString, int[] paramArrayOfInt)
  {
    return this.mIconsZipFile.getInputStream(paramString, paramArrayOfInt);
  }

  public InputStream getStreamFromMiui(String paramString1, String paramString2, int[] paramArrayOfInt)
  {
    InputStream localInputStream = null;
    String str = paramString1.substring(1 + paramString1.lastIndexOf('/'));
    if (str.startsWith("lock_screen_"))
    {
      localInputStream = this.mLockscreenZipFile.getInputStream(paramString1, paramArrayOfInt);
      if (localInputStream == null)
        localInputStream = this.mLockscreenZipFile.getInputStream(str, paramArrayOfInt);
    }
    if ((localInputStream == null) && (!TextUtils.isEmpty(paramString2)))
      localInputStream = getStreamFromPackage("framework-miui-res/" + paramString1, paramString2, paramArrayOfInt);
    if (localInputStream == null)
      localInputStream = this.mMiuiZipFile.getInputStream(paramString1, paramArrayOfInt);
    return localInputStream;
  }

  public InputStream getStreamFromPackage(String paramString1, String paramString2, int[] paramArrayOfInt)
  {
    InputStream localInputStream = null;
    if ("com.miui.home".equals(paramString2))
    {
      String str = paramString1.substring(1 + paramString1.lastIndexOf('/'));
      if ((str.equals("icon_folder.png")) || (str.startsWith("folder_icon_cover_")))
        localInputStream = this.mIconsZipFile.getInputStream(str, paramArrayOfInt);
    }
    if (localInputStream == null)
      localInputStream = getPackageThemeZipFile(paramString2).getInputStream(paramString1, paramArrayOfInt);
    return localInputStream;
  }

  public boolean needReloadFrameworkThemeValueFile()
  {
    return this.mFrameworkZipFile.confirmReset();
  }

  public boolean needReloadLockscreenThemeValueFile()
  {
    return this.mLockscreenZipFile.confirmReset();
  }

  public boolean needReloadMiuiThemeValueFile()
  {
    return this.mMiuiZipFile.confirmReset();
  }

  public boolean needReloadPackageThemeValueFile(String paramString)
  {
    ThemeZipFile localThemeZipFile = getPackageThemeZipFile(paramString);
    if (localThemeZipFile != null);
    for (boolean bool = localThemeZipFile.confirmReset(); ; bool = false)
      return bool;
  }

  public void reset(String paramString)
  {
    ThemeZipFile localThemeZipFile = getPackageThemeZipFile(paramString);
    if (localThemeZipFile != null)
      localThemeZipFile.reset();
  }

  public void resetAll()
  {
    resetSystem();
    Iterator localIterator = this.mPackageZipFiles.values().iterator();
    while (localIterator.hasNext())
      ((ThemeZipFile)localIterator.next()).reset();
  }

  public void resetIcons()
  {
    this.mIconsZipFile.reset();
  }

  public void resetSystem()
  {
    this.mFrameworkZipFile.reset();
    this.mMiuiZipFile.reset();
    this.mIconsZipFile.reset();
    this.mLockscreenZipFile.reset();
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.content.res.ThemeZipManager
 * JD-Core Version:    0.6.0
 */