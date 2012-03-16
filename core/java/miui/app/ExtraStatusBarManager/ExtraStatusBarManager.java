package miui.app;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings.System;

public class ExtraStatusBarManager
{
  public static final String ACTION_EXIT_FULLSCREEN = "com.miui.app.ExtraStatusBarManager.EXIT_FULLSCREEN";
  public static final String ACTION_EXPAND_TOGGLES_TAB = "com.miui.app.ExtraStatusBarManager.EXPAND_TOGGLE_TAB";
  public static final String ACTION_REQUEST_RESTART = "com.miui.app.ExtraStatusBarManager.REQUEST_RESTART";
  public static final String ACTION_STATUSBAR_LOADED = "com.miui.app.ExtraStatusBarManager.LOADED";
  public static final String ACTION_STATUSBAR_UNLOADED = "com.miui.app.ExtraStatusBarManager.UNLOADED";
  public static final String ACTION_TRIGGER_CAMERA_KEY = "com.miui.app.ExtraStatusBarManager.TRIGGER_CAMERA_KEY";
  public static final String ACTION_TRIGGER_TOGGLE_SCREEN_BUTTONS = "com.miui.app.ExtraStatusBarManager.TRIGGER_TOGGLE_SCREEN_BUTTONS";
  public static final int DISABLE_BACKGROUND = 1073741824;
  public static final int DISABLE_FOR_KEYGUARD = -2147483648;
  public static final int DISABLE_FULLSCREEN = 536870912;

  public static boolean isExpandableUnderFullscreen(Context paramContext)
  {
    int i = 1;
    if (Settings.System.getInt(paramContext.getContentResolver(), "status_bar_expandable_under_fullscreen", i) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public static boolean isExpandableUnderKeyguard(Context paramContext)
  {
    int i = 0;
    if (Settings.System.getInt(paramContext.getContentResolver(), "status_bar_expandable_under_keyguard", 0) != 0)
      i = 1;
    return i;
  }

  public static void setExpandableUnderFullscreen(Context paramContext, boolean paramBoolean)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      Settings.System.putInt(localContentResolver, "status_bar_expandable_under_fullscreen", i);
      return;
    }
  }

  public static void setExpandableUnderKeyguard(Context paramContext, boolean paramBoolean)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      Settings.System.putInt(localContentResolver, "status_bar_expandable_under_keyguard", i);
      return;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.ExtraStatusBarManager
 * JD-Core Version:    0.6.0
 */