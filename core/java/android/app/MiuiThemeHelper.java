package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import miui.content.res.ExtraConfiguration;
import miui.content.res.IconCustomizer;
import miui.content.res.ThemeResources;

public class MiuiThemeHelper
{
  public static final String MIUI_RES_PATH = "/system/framework/framework-miui-res.apk";
  private static final String TAG = "IconHelper";

  public static void addExtraAssetPaths(AssetManager paramAssetManager)
  {
    paramAssetManager.addAssetPath("/system/framework/framework-miui-res.apk");
  }

  public static void copyExtraConfigurations(Configuration paramConfiguration1, Configuration paramConfiguration2)
  {
    paramConfiguration2.extraConfig.themeChanged = paramConfiguration1.extraConfig.themeChanged;
  }

  public static Drawable getDrawable(PackageManager paramPackageManager, String paramString, int paramInt, ApplicationInfo paramApplicationInfo, PackageItemInfo paramPackageItemInfo, boolean paramBoolean)
  {
    if ((!paramBoolean) || (paramPackageItemInfo == null));
    for (Drawable localDrawable = paramPackageManager.getDrawable(paramString, paramInt, paramApplicationInfo); ; localDrawable = getDrawable(paramPackageManager, paramString, paramInt, paramApplicationInfo, paramPackageItemInfo.name))
      return localDrawable;
  }

  public static Drawable getDrawable(PackageManager paramPackageManager, String paramString1, int paramInt, ApplicationInfo paramApplicationInfo, String paramString2)
  {
    String str = IconCustomizer.getFileName(paramString1, paramString2);
    ApplicationPackageManager.ResourceName localResourceName = new ApplicationPackageManager.ResourceName(str, paramInt);
    Drawable localDrawable = ApplicationPackageManager.getCachedIcon(localResourceName);
    Object localObject2;
    if (localDrawable != null)
    {
      localObject2 = localDrawable;
      return localObject2;
    }
    Object localObject1;
    if ((IconCustomizer.isExclude(paramString1)) && (ThemeResources.getSystem().hasIcon("icon_mask.png")))
      localObject1 = paramPackageManager.getDrawable(paramString1, paramInt, paramApplicationInfo);
    while (true)
    {
      if (localObject1 != null)
        ApplicationPackageManager.putCachedIcon(localResourceName, (Drawable)localObject1);
      localObject2 = localObject1;
      break;
      localObject1 = IconCustomizer.getCustomizedIconDrawable(str);
      if (localObject1 != null)
        continue;
      localObject1 = paramPackageManager.getDrawable(paramString1, paramInt, paramApplicationInfo);
      if (localObject1 == null)
        continue;
      Log.d("IconHelper", "Generate customized icon for " + str);
      localObject1 = IconCustomizer.generateIconDrawable((Drawable)localObject1);
      IconCustomizer.saveCustomizedIconBitmap(str, ((BitmapDrawable)localObject1).getBitmap());
    }
  }

  public static void handleExtraConfigurationChanges(int paramInt)
  {
    if ((0x80000000 & paramInt) != 0)
    {
      Canvas.freeCaches();
      IconCustomizer.clearCache();
    }
  }

  public static void handleExtraConfigurationChanges(int paramInt, Configuration paramConfiguration, Context paramContext, Handler paramHandler)
  {
    if ((0x80000000 & paramInt) != 0)
    {
      long l = paramConfiguration.extraConfig.themeChangedFlags;
      ExtraConfiguration.addNeedRestartActivity(l);
      handleExtraConfigurationChanges(paramInt);
      if (ExtraConfiguration.needRestartStatusBar(l))
        paramContext.sendBroadcast(new Intent("com.miui.app.ExtraStatusBarManager.REQUEST_RESTART"));
    }
  }

  public static boolean isCompatibilityMode(int paramInt)
  {
    if ((0x8000000 & paramInt) != 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isCustomizedIcon(IntentFilter paramIntentFilter)
  {
    int j;
    if (paramIntentFilter != null)
    {
      j = -1 + paramIntentFilter.countCategories();
      if (j >= 0)
        if (!"android.intent.category.LAUNCHER".equals(paramIntentFilter.getCategory(j)));
    }
    for (int i = 1; ; i = 0)
    {
      return i;
      j--;
      break;
    }
  }

  public static boolean needRestartActivity(String paramString, int paramInt, Configuration paramConfiguration)
  {
    if ((paramInt == -2147483648) && (!ExtraConfiguration.removeNeedRestartActivity(paramString)) && (!ExtraConfiguration.needRestartActivity(paramString, paramConfiguration.extraConfig.themeChangedFlags)));
    for (int i = 1; ; i = 0)
      return i;
  }

  public static Integer parseDimension(String paramString)
  {
    int i = -4;
    int j = -3;
    int k = -2;
    int m = -1;
    int n = 0;
    int i1;
    int i2;
    if (n < paramString.length())
    {
      int i5 = paramString.charAt(n);
      if ((i == -4) && (i5 >= 48) && (i5 <= 57))
        i = n;
      if ((j == -3) && (i5 == 46))
        j = n;
      if ((j != -3) && (i5 >= 48) && (i5 <= 57))
        k = n;
      if ((m == -1) && (i5 >= 97) && (i5 <= 122))
        m = n;
    }
    else
    {
      i1 = 0;
      i2 = 0;
      if ((m == -1) || (j >= k) || (k >= m))
        break label443;
    }
    while (true)
    {
      try
      {
        float f1 = Float.parseFloat(paramString.substring(0, m));
        f2 = f1;
        if ((j == -3) || (k == -2))
          continue;
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        try
        {
          int i4 = Integer.parseInt(paramString.substring(k, m));
          i1 = i4;
          if (i1 >= 256)
            continue;
          f2 *= 256.0F;
          str = paramString.substring(m);
          if (!str.equals("px"))
            continue;
          i3 = 0;
          localInteger = Integer.valueOf(i3 | Integer.valueOf(Integer.valueOf(0xFFFFFF00 & Integer.valueOf((int)f2).intValue()).intValue() | i2 << 4).intValue());
          return localInteger;
          n++;
          break;
          localNumberFormatException1 = localNumberFormatException1;
          localInteger = null;
          continue;
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          float f2;
          String str;
          localInteger = null;
          continue;
          if (i1 >= 32768)
            continue;
          f2 *= 32768.0F;
          i2 = 1;
          continue;
          if (i1 >= 1073741824)
            continue;
          f2 *= 1.073742E+09F;
          i2 = 2;
          continue;
          if (i1 >= 2097152)
            continue;
          f2 *= 2097152.0F;
          i2 = 3;
          continue;
          if ((!str.equals("dp")) && (!str.equals("dip")))
            continue;
          int i3 = 1;
          continue;
          if (!str.equals("sp"))
            continue;
          i3 = 2;
          continue;
          if (!str.equals("pt"))
            continue;
          i3 = 3;
          continue;
          if (!str.equals("in"))
            continue;
          i3 = 4;
          continue;
          if (!str.equals("mm"))
            continue;
          i3 = 5;
          continue;
          localInteger = null;
          continue;
        }
      }
      label443: Integer localInteger = null;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.app.MiuiThemeHelper
 * JD-Core Version:    0.6.0
 */