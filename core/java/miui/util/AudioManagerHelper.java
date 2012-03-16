package miui.util;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings.System;
import miui.provider.ExtraSettings.System;

public class AudioManagerHelper
{
  public static void amendVibratePrefs(Context paramContext)
  {
    amendVibratePrefsByRingerMode(paramContext);
    amendVibratePrefsByVibrate(paramContext, 1);
    amendVibratePrefsByVibrate(paramContext, 0);
  }

  public static void amendVibratePrefsByRingerMode(Context paramContext)
  {
    int i = ((AudioManager)paramContext.getSystemService("audio")).getRingerMode();
    if (i == 0)
      updatePrefIfChanged(paramContext.getContentResolver(), "vibrate_in_silent", 0);
    while (true)
    {
      return;
      if (i == 1)
      {
        updatePrefIfChanged(paramContext.getContentResolver(), "vibrate_in_silent", 1);
        continue;
      }
    }
  }

  public static void amendVibratePrefsByVibrate(Context paramContext, int paramInt)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    String str;
    if (paramInt == 0)
    {
      str = "vibrate_ringer";
      switch (localAudioManager.getVibrateSetting(paramInt))
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      return;
      str = "vibrate_notification";
      break;
      if ((Settings.System.getInt(localContentResolver, "vibrate_in_silent", 1) != 1) && (Settings.System.getInt(localContentResolver, "vibrate_in_normal", 1) != 1))
        continue;
      updatePrefIfChanged(localContentResolver, str, 0);
      continue;
      updatePrefIfChanged(localContentResolver, "vibrate_in_normal", 1);
      updatePrefIfChanged(localContentResolver, str, 1);
      continue;
      updatePrefIfChanged(localContentResolver, "vibrate_in_silent", 1);
      updatePrefIfChanged(localContentResolver, "vibrate_in_normal", 0);
      updatePrefIfChanged(localContentResolver, str, 1);
    }
  }

  public static boolean isSilentEnabled(Context paramContext)
  {
    if (((AudioManager)paramContext.getSystemService("audio")).getRingerMode() != 2);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isVibrateEnabled(Context paramContext)
  {
    int i = 1;
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    int j = 0;
    switch (localAudioManager.getRingerMode())
    {
    default:
      if ((j == 0) || ((!ExtraSettings.System.isVibratePrefOn(paramContext, "vibrate_ringer")) && (!ExtraSettings.System.isVibratePrefOn(paramContext, "vibrate_notification")) && (!ExtraSettings.System.isVibratePrefOn(paramContext, "vibrate_mms"))))
        break;
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      return i;
      j = 0;
      break;
      j = 1;
      break;
      if (Settings.System.getInt(paramContext.getContentResolver(), "vibrate_in_normal", i) == i);
      for (j = i; ; j = 0)
        break;
      i = 0;
    }
  }

  private static void setVibrateInternel(Context paramContext, String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    boolean bool = ExtraSettings.System.isVibratePrefOn(paramContext, paramString);
    int i = 0;
    if (paramBoolean1)
      if (bool)
        i = 1;
    do
      while (true)
      {
        localAudioManager.setVibrateSetting(paramInt, i);
        return;
        i = 0;
      }
    while (!paramBoolean2);
    if (bool);
    for (i = 2; ; i = 0)
      break;
  }

  public static boolean shouldVibrateForPref(Context paramContext, String paramString)
  {
    int i = 1;
    boolean bool = false;
    switch (((AudioManager)paramContext.getSystemService("audio")).getRingerMode())
    {
    default:
    case 0:
    case 1:
      while (true)
      {
        return bool;
        bool = false;
        continue;
        bool = ExtraSettings.System.isVibratePrefOn(paramContext, paramString);
      }
    case 2:
    }
    if (Settings.System.getInt(paramContext.getContentResolver(), "vibrate_in_normal", i) == i)
      label74: if (i == 0)
        break label92;
    label92: for (bool = ExtraSettings.System.isVibratePrefOn(paramContext, paramString); ; bool = false)
    {
      break;
      i = 0;
      break label74;
    }
  }

  public static void toggleSilent(Context paramContext)
  {
    if (!isSilentEnabled(paramContext));
    for (boolean bool = true; ; bool = false)
    {
      updateVibrateState(paramContext, bool);
      return;
    }
  }

  public static void toggleVibrate(Context paramContext)
  {
    int i = 1;
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    if (!isVibrateEnabled(paramContext));
    ContentResolver localContentResolver;
    boolean bool;
    for (int j = i; ; j = 0)
    {
      localContentResolver = paramContext.getContentResolver();
      bool = false;
      switch (localAudioManager.getRingerMode())
      {
      default:
        updateVibrateState(paramContext, bool);
        return;
      case 0:
      case 1:
      case 2:
      }
    }
    if (j != 0);
    while (true)
    {
      Settings.System.putInt(localContentResolver, "vibrate_in_silent", i);
      bool = true;
      break;
      i = 0;
    }
    if (j != 0);
    while (true)
    {
      Settings.System.putInt(localContentResolver, "vibrate_in_normal", i);
      break;
      i = 0;
    }
  }

  private static void updatePrefIfChanged(ContentResolver paramContentResolver, String paramString, int paramInt)
  {
    if (Settings.System.getInt(paramContentResolver, paramString, -paramInt) != paramInt)
      Settings.System.putInt(paramContentResolver, paramString, paramInt);
  }

  public static void updateVibrateState(Context paramContext, boolean paramBoolean)
  {
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    ContentResolver localContentResolver = paramContext.getContentResolver();
    boolean bool1;
    boolean bool2;
    if (Settings.System.getInt(localContentResolver, "vibrate_in_silent", 1) == 1)
    {
      bool1 = true;
      if (Settings.System.getInt(localContentResolver, "vibrate_in_normal", 1) != 1)
        break label93;
      bool2 = true;
      label43: i = 2;
      if (paramBoolean)
        if (!bool1)
          break label99;
    }
    label93: label99: for (int i = 1; ; i = 0)
    {
      localAudioManager.setRingerMode(i);
      setVibrateInternel(paramContext, "vibrate_ringer", 0, bool2, bool1);
      setVibrateInternel(paramContext, "vibrate_notification", 1, bool2, bool1);
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label43;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.AudioManagerHelper
 * JD-Core Version:    0.6.0
 */