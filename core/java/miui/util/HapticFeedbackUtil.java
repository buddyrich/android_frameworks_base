package miui.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;

public class HapticFeedbackUtil
{
  private static final String[] KEYBOARD_TAP_PATTERN_PROPERTY;
  private static final String KEY_VIBRATE_EX_ENABLED = "ro.haptic.vibrate_ex.enabled";
  private static final String[] LONG_PRESS_PATTERN_PROPERTY;
  private static final String TAG = "HapticFeedbackUtil";
  private static final String[] VIRTUAL_DOWN_PATTERN_PROPERTY;
  private static final String[] VIRTUAL_UP_PATTERN_PROPERTY;
  private final Context mContext;
  private long[] mKeyboardTapVibePattern;
  private long[] mLongPressVibePattern;
  private final boolean mVibrateEx;
  private Vibrator mVibrator;
  private long[] mVirtualKeyUpVibePattern;
  private long[] mVirtualKeyVibePattern;

  static
  {
    String[] arrayOfString1 = new String[3];
    arrayOfString1[0] = "persist.sys.haptic.long.weak";
    arrayOfString1[1] = "persist.sys.haptic.long.normal";
    arrayOfString1[2] = "persist.sys.haptic.long.strong";
    LONG_PRESS_PATTERN_PROPERTY = arrayOfString1;
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = "persist.sys.haptic.tap.weak";
    arrayOfString2[1] = "persist.sys.haptic.tap.normal";
    arrayOfString2[2] = "persist.sys.haptic.tap.strong";
    KEYBOARD_TAP_PATTERN_PROPERTY = arrayOfString2;
    String[] arrayOfString3 = new String[3];
    arrayOfString3[0] = "persist.sys.haptic.down.weak";
    arrayOfString3[1] = "persist.sys.haptic.down.normal";
    arrayOfString3[2] = "persist.sys.haptic.down.strong";
    VIRTUAL_DOWN_PATTERN_PROPERTY = arrayOfString3;
    String[] arrayOfString4 = new String[3];
    arrayOfString4[0] = "persist.sys.haptic.up.weak";
    arrayOfString4[1] = "persist.sys.haptic.up.normal";
    arrayOfString4[2] = "persist.sys.haptic.up.strong";
    VIRTUAL_UP_PATTERN_PROPERTY = arrayOfString4;
  }

  public HapticFeedbackUtil(Context paramContext, boolean paramBoolean)
  {
    this.mContext = paramContext;
    this.mVibrateEx = SystemProperties.getBoolean("ro.haptic.vibrate_ex.enabled", false);
    this.mVibrator = new Vibrator();
    if (paramBoolean)
      updateSettings();
    while (true)
    {
      return;
      new SettingsObserver(new Handler()).observe();
    }
  }

  private static long[] getLongIntArray(Resources paramResources, int paramInt)
  {
    int[] arrayOfInt = paramResources.getIntArray(paramInt);
    long[] arrayOfLong;
    if (arrayOfInt == null)
      arrayOfLong = null;
    while (true)
    {
      return arrayOfLong;
      arrayOfLong = new long[arrayOfInt.length];
      for (int i = 0; i < arrayOfInt.length; i++)
        arrayOfLong[i] = arrayOfInt[i];
    }
  }

  private long[] loadHaptic(String paramString, int paramInt)
  {
    String str = SystemProperties.get(paramString);
    if (TextUtils.isEmpty(str));
    for (long[] arrayOfLong = getLongIntArray(this.mContext.getResources(), paramInt); ; arrayOfLong = stringToLongArray(str))
      return arrayOfLong;
  }

  private long[] stringToLongArray(String paramString)
  {
    long[] arrayOfLong;
    if (paramString == null)
    {
      arrayOfLong = new long[1];
      arrayOfLong[0] = 0L;
    }
    while (true)
    {
      return arrayOfLong;
      String[] arrayOfString = paramString.split(",");
      int i = arrayOfString.length;
      arrayOfLong = new long[i];
      for (int j = 0; j < i; j++)
        arrayOfLong[j] = Long.parseLong(arrayOfString[j].trim());
    }
  }

  public boolean isSupportedEffect(int paramInt)
  {
    if (paramInt <= 3);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean performHapticFeedback(int paramInt, boolean paramBoolean)
  {
    int i = 0;
    int j;
    if (Settings.System.getInt(this.mContext.getContentResolver(), "haptic_feedback_enabled", 0) == 0)
    {
      j = 1;
      if ((paramBoolean) || (j == 0))
        break label38;
    }
    while (true)
    {
      return i;
      j = 0;
      break;
      label38: switch (paramInt)
      {
      default:
      case 0:
      case 1:
      case 3:
      case 2:
      }
    }
    long[] arrayOfLong = this.mLongPressVibePattern;
    while (true)
    {
      if ((arrayOfLong != null) && (arrayOfLong.length != 0))
        break label126;
      Log.w("HapticFeedbackUtil", "vibrate: null or empty pattern");
      break;
      arrayOfLong = this.mVirtualKeyVibePattern;
      continue;
      arrayOfLong = this.mKeyboardTapVibePattern;
      continue;
      arrayOfLong = this.mVirtualKeyUpVibePattern;
    }
    label126: if (this.mVibrateEx)
    {
      byte[] arrayOfByte = new byte[arrayOfLong.length];
      for (int k = 0; k < arrayOfLong.length; k++)
        arrayOfByte[k] = (byte)(int)arrayOfLong[k];
      this.mVibrator.vibrateEx(arrayOfByte);
    }
    while (true)
    {
      i = 1;
      break;
      if (arrayOfLong.length == 1)
      {
        this.mVibrator.vibrate(arrayOfLong[0]);
        continue;
      }
      this.mVibrator.vibrate(arrayOfLong, -1);
    }
  }

  public void updateSettings()
  {
    int i = Math.min(2, Math.max(0, Settings.System.getInt(this.mContext.getContentResolver(), "haptic_feedback_level", 1)));
    this.mLongPressVibePattern = loadHaptic(LONG_PRESS_PATTERN_PROPERTY[i], 17235999);
    this.mVirtualKeyVibePattern = loadHaptic(VIRTUAL_DOWN_PATTERN_PROPERTY[i], 17236000);
    this.mKeyboardTapVibePattern = loadHaptic(KEYBOARD_TAP_PATTERN_PROPERTY[i], 17236001);
    this.mVirtualKeyUpVibePattern = loadHaptic(VIRTUAL_UP_PATTERN_PROPERTY[i], 50724866);
  }

  class SettingsObserver extends ContentObserver
  {
    SettingsObserver(Handler arg2)
    {
      super();
    }

    void observe()
    {
      HapticFeedbackUtil.this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("haptic_feedback_level"), false, this);
      HapticFeedbackUtil.this.updateSettings();
    }

    public void onChange(boolean paramBoolean)
    {
      HapticFeedbackUtil.this.updateSettings();
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.HapticFeedbackUtil
 * JD-Core Version:    0.6.0
 */