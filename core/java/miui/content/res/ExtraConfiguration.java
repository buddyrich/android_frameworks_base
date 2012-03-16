package miui.content.res;

import android.os.Parcel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExtraConfiguration
  implements Comparable<ExtraConfiguration>
{
  public static final String CONTACTS_PKG_NAME = "com.android.contacts";
  public static final String LAUNCHER_PKG_NAME = "com.miui.home";
  public static final String MMS_PKG_NAME = "com.android.mms";
  public static final String SETTINGS_PKG_NAME = "com.android.settings";
  public static final String SYSTEMUI_PKG_NAME = "com.android.systemui";
  public static final long SYSTEM_INTRESTE_CHANGE_FLAG = 268466329L;
  public static final long THEME_FLAG_ALARM = 1024L;
  public static final long THEME_FLAG_AUDIO_EFFECT = 32768L;
  public static final long THEME_FLAG_BOOT_ANIMATION = 32L;
  public static final long THEME_FLAG_BOOT_AUDIO = 64L;
  public static final long THEME_FLAG_CLOCK = 65536L;
  public static final long THEME_FLAG_CONTACT = 2048L;
  public static final long THEME_FLAG_FONT = 16L;
  public static final long THEME_FLAG_FRAMEWORK = 1L;
  public static final long THEME_FLAG_ICON = 8L;
  public static final long THEME_FLAG_LAST = 131072L;
  public static final long THEME_FLAG_LAUNCHER = 16384L;
  public static final long THEME_FLAG_LOCKSCREEN = 4L;
  public static final long THEME_FLAG_LOCKSTYLE = 4096L;
  public static final long THEME_FLAG_MMS = 128L;
  public static final long THEME_FLAG_NOTIFICATION = 512L;
  public static final long THEME_FLAG_PHOTO_FRAME = 131072L;
  public static final long THEME_FLAG_RINGTONE = 256L;
  public static final long THEME_FLAG_STATUSBAR = 8192L;
  public static final long THEME_FLAG_THIRD_APP = 268435456L;
  public static final long THEME_FLAG_WALLPAPER = 2L;
  private static final Set<String> needRestartActivitySet = Collections.synchronizedSet(new HashSet());
  public int themeChanged;
  public long themeChangedFlags;

  public static void addNeedRestartActivity(long paramLong)
  {
    if (needRestartLauncher(paramLong))
      needRestartActivitySet.add("com.miui.home");
    if (needRestartSettings(paramLong))
      needRestartActivitySet.add("com.android.settings");
    if (needRestartMms(paramLong))
      needRestartActivitySet.add("com.android.mms");
    if (needRestartContacts(paramLong))
      needRestartActivitySet.add("com.android.contacts");
  }

  public static boolean needNewResources(int paramInt)
  {
    if ((0x80000000 & paramInt) != 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean needRestart3rdApp(long paramLong)
  {
    if ((0x10000011 & paramLong) != 0L);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean needRestartActivity(String paramString, long paramLong)
  {
    boolean bool;
    if (paramString != null)
      if (paramString.startsWith("com.miui.home"))
        bool = needRestartLauncher(paramLong);
    while (true)
    {
      return bool;
      if (paramString.startsWith("com.android.settings"))
      {
        bool = needRestartSettings(paramLong);
        continue;
      }
      if (paramString.startsWith("com.android.mms"))
      {
        bool = needRestartMms(paramLong);
        continue;
      }
      if (paramString.startsWith("com.android.contacts"))
      {
        bool = needRestartContacts(paramLong);
        continue;
      }
      bool = needRestart3rdApp(paramLong);
    }
  }

  public static boolean needRestartContacts(long paramLong)
  {
    if ((0x811 & paramLong) != 0L);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean needRestartLauncher(long paramLong)
  {
    if ((0x4019 & paramLong) != 0L);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean needRestartMms(long paramLong)
  {
    if ((0x91 & paramLong) != 0L);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean needRestartSettings(long paramLong)
  {
    if ((0x19 & paramLong) != 0L);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean needRestartStatusBar(long paramLong)
  {
    if ((0x2019 & paramLong) != 0L);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean removeNeedRestartActivity(String paramString)
  {
    return needRestartActivitySet.remove(paramString);
  }

  public int compareTo(ExtraConfiguration paramExtraConfiguration)
  {
    int i = this.themeChanged - paramExtraConfiguration.themeChanged;
    if (i != 0);
    return i;
  }

  public int diff(ExtraConfiguration paramExtraConfiguration)
  {
    int i = 0;
    if (this.themeChanged < paramExtraConfiguration.themeChanged)
      i = 0x0 | 0x80000000;
    return i;
  }

  public int hashCode()
  {
    return this.themeChanged + (int)this.themeChangedFlags;
  }

  public void readFromParcel(Parcel paramParcel)
  {
    this.themeChanged = paramParcel.readInt();
    this.themeChangedFlags = paramParcel.readLong();
  }

  public void setTo(ExtraConfiguration paramExtraConfiguration)
  {
    this.themeChanged = paramExtraConfiguration.themeChanged;
    this.themeChangedFlags = paramExtraConfiguration.themeChangedFlags;
  }

  public void setToDefaults()
  {
    this.themeChanged = 0;
    this.themeChangedFlags = 0L;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" themeChanged=");
    localStringBuilder.append(this.themeChanged);
    localStringBuilder.append(" themeChangedFlags=");
    localStringBuilder.append(this.themeChangedFlags);
    return localStringBuilder.toString();
  }

  public int updateFrom(ExtraConfiguration paramExtraConfiguration)
  {
    int i = 0;
    if (this.themeChanged < paramExtraConfiguration.themeChanged)
    {
      i = 0x0 | 0x80000000;
      this.themeChanged = paramExtraConfiguration.themeChanged;
      this.themeChangedFlags = paramExtraConfiguration.themeChangedFlags;
    }
    return i;
  }

  public void updateTheme(long paramLong)
  {
    this.themeChanged = (1 + this.themeChanged);
    this.themeChangedFlags = paramLong;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.themeChanged);
    paramParcel.writeLong(this.themeChangedFlags);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.content.res.ExtraConfiguration
 * JD-Core Version:    0.6.0
 */