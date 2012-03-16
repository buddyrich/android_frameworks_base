package miui.app.resourcebrowser;

import java.io.File;
import miui.os.Environment;

public abstract interface ResourceConstants
{
  public static final String BUILTIN_ALARM_PATH = "/system/media/audio/alarms";
  public static final String BUILTIN_LOCKSCREEN_PATH = "/system/media/lockscreen";
  public static final String BUILTIN_NOTIFICATION_PATH = "/system/media/audio/notifications";
  public static final String BUILTIN_RESOURCE_BASE_PATH = "/system/media/";
  public static final String BUILTIN_RINGTONE_PATH = "/system/media/audio/ringtones";
  public static final String BUILTIN_THEME_PATH = "/system/media/theme";
  public static final String BUILTIN_WALLPAPER_PATH = "/system/media/wallpaper";
  public static final String CACHE_PATH;
  public static final String CATEGORY_PATH;
  public static final String DESCRIPTION = "description.xml";
  public static final String DOWNLOADED_ALARM_PATH;
  public static final String DOWNLOADED_LOCKSCREEN_PATH;
  public static final String DOWNLOADED_NOTIFICATION_PATH;
  public static final String DOWNLOADED_RESOURCE_BASE_PATH;
  public static final String DOWNLOADED_RINGTONE_PATH;
  public static final String DOWNLOADED_THEME_PATH;
  public static final String DOWNLOADED_WALLPAPER_PATH;
  public static final String LIST_PATH;
  public static final String MIUI_EXTERNAL_PATH;
  public static final String MIUI_INTERNAL_PATH;
  public static final String MIUI_PATH = Environment.getMIUIStorageDirectory().getAbsolutePath() + File.separator;
  public static final String[] PATHS;
  public static final String PREVIEW_PATH;
  public static final int RESOURCE_DEFAULT_LENGTH = 30;
  public static final int RESOURCE_LATEST_VERSION = 0;
  public static final int RESOURCE_NOT_EXIST = 2;
  public static final int RESOURCE_OLD_VERSION = 1;
  public static final String THUMBNAIL_PATH;
  public static final int TYPE_CATEGORY = 3;
  public static final int TYPE_LIST = 0;
  public static final int TYPE_PREVIEW = 2;
  public static final int TYPE_THUMBNAIL = 1;
  public static final int TYPE_VERSION = 4;
  public static final String VERSION_PATH;

  static
  {
    MIUI_INTERNAL_PATH = Environment.getMIUIInternalStorageDirectory().getAbsolutePath() + File.separator;
    MIUI_EXTERNAL_PATH = Environment.getMIUIExternalStorageDirectory().getAbsolutePath() + File.separator;
    CACHE_PATH = MIUI_PATH + ".cache" + File.separator + "ResourceBrowser" + File.separator;
    LIST_PATH = CACHE_PATH + "list" + File.separator;
    THUMBNAIL_PATH = CACHE_PATH + "thumbnail" + File.separator;
    PREVIEW_PATH = CACHE_PATH + "preview" + File.separator;
    CATEGORY_PATH = CACHE_PATH + "category" + File.separator;
    VERSION_PATH = CACHE_PATH + "version" + File.separator;
    String[] arrayOfString = new String[5];
    arrayOfString[0] = LIST_PATH;
    arrayOfString[1] = THUMBNAIL_PATH;
    arrayOfString[2] = PREVIEW_PATH;
    arrayOfString[3] = CATEGORY_PATH;
    arrayOfString[4] = VERSION_PATH;
    PATHS = arrayOfString;
    DOWNLOADED_RESOURCE_BASE_PATH = MIUI_PATH;
    DOWNLOADED_THEME_PATH = DOWNLOADED_RESOURCE_BASE_PATH + "theme";
    DOWNLOADED_WALLPAPER_PATH = DOWNLOADED_RESOURCE_BASE_PATH + "wallpaper";
    DOWNLOADED_LOCKSCREEN_PATH = DOWNLOADED_RESOURCE_BASE_PATH + "wallpaper";
    DOWNLOADED_RINGTONE_PATH = DOWNLOADED_RESOURCE_BASE_PATH + "ringtone";
    DOWNLOADED_NOTIFICATION_PATH = DOWNLOADED_RESOURCE_BASE_PATH + "ringtone";
    DOWNLOADED_ALARM_PATH = DOWNLOADED_RESOURCE_BASE_PATH + "ringtone";
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceConstants
 * JD-Core Version:    0.6.0
 */