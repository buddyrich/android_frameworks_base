package miui.app.resourcebrowser;

public abstract interface IntentConstants
{
  public static final String ACTION_PICK_RESOURCE = "android.intent.action.PICK_RESOURCE";
  public static final int AUDIO_RESOURCE = 2;
  public static final String CACHE_LIST_FOLDER = "com.miui.android.resourcebrowser.CACHE_LIST_FOLDER";
  public static final String CATEGORY_CODE = "com.miui.android.resourcebrowser.CATEGORY_CODE";
  public static final String CATEGORY_SUPPORTED = "com.miui.android.resourcebrowser.CATEGORY_SUPPORTED";
  public static final String CATEGORY_URL = "com.miui.android.resourcebrowser.CATEGORY_URL";
  public static final String CURRENT_USING_PATH = "com.miui.android.resourcebrowser.CURRENT_USING_PATH";
  public static final String CUSTOM_FLAG = "com.miui.android.resourcebrowser.CUSTOM_FLAG";
  public static final String DETAIL_ACTIVITY_CLASS = "com.miui.android.resourcebrowser.DETAIL_ACTIVITY_CLASS";
  public static final String DETAIL_ACTIVITY_PACKAGE = "com.miui.android.resourcebrowser.DETAIL_ACTIVITY_PACKAGE";
  public static final int DISPLAY_COMBINE = 1;
  public static final int DISPLAY_COMBINE_TEXT = 3;
  public static final int DISPLAY_COMBINE_THIN = 2;
  public static final int DISPLAY_MULTIPLE = 0;
  public static final int DISPLAY_SINGLE = 4;
  public static final int DISPLAY_SINGLE_DETAIL = 7;
  public static final int DISPLAY_SINGLE_SMALL = 6;
  public static final int DISPLAY_SINGLE_THIN = 5;
  public static final String DISPLAY_TYPE = "com.miui.android.resourcebrowser.DISPLAY_TYPE";
  public static final String DOWNLOAD_FOLDER = "com.miui.android.resourcebrowser.DOWNLOAD_FOLDER";
  public static final String FORMAT_VERSION_END = "com.miui.android.resourcebrowser.FORMAT_VERSION_END";
  public static final String FORMAT_VERSION_START = "com.miui.android.resourcebrowser.FORMAT_VERSION_START";
  public static final String FORMAT_VERSION_SUPPORTED = "com.miui.android.resourcebrowser.FORMAT_VERSION_SUPPORTED";
  public static final int IMAGE_RESOURCE = 1;
  public static final String KEYWORD = "com.miui.android.resourcebrowser.KEYWORD";
  public static final String LOCAL_LIST_ACTIVITY_CLASS = "com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_CLASS";
  public static final String LOCAL_LIST_ACTIVITY_PACKAGE = "com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_PACKAGE";
  public static final String META_DATA = "META_DATA";
  public static final String ONLINE_LIST_ACTIVITY_CLASS = "com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_CLASS";
  public static final String ONLINE_LIST_ACTIVITY_PACKAGE = "com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_PACKAGE";
  public static final String PICKED_RESOURCE = "com.miui.android.resourcebrowser.PICKED_RESOURCE";
  public static final String PLATFORM_VERSION_END = "com.miui.android.resourcebrowser.PLATFORM_VERSION_END";
  public static final String PLATFORM_VERSION_START = "com.miui.android.resourcebrowser.PLATFORM_VERSION_START";
  public static final String PLATFORM_VERSION_SUPPORTED = "com.miui.android.resourcebrowser.PLATFORM_VERSION_SUPPORTED";
  public static final String PREVIEW_PREFIX = "com.miui.android.resourcebrowser.PREVIEW_PREFIX";
  public static final String PREVIEW_PREFIX_INDICATOR = "com.miui.android.resourcebrowser.PREVIEW_PREFIX_INDICATOR";
  public static final int REQUEST_CODE = 1;
  public static final String RESOURCE_HOTTEST_URL = "com.miui.android.resourcebrowser.RESOURCE_HOTTEST_URL";
  public static final String RESOURCE_INDEX = "com.miui.android.resourcebrowser.RESOURCE_INDEX";
  public static final String RESOURCE_LATEST_URL = "com.miui.android.resourcebrowser.RESOURCE_LATEST_URL";
  public static final String RESOURCE_SET_CATEGORY = "com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY";
  public static final String RESOURCE_SET_CODE = "com.miui.android.resourcebrowser.RESOURCE_SET_CODE";
  public static final String RESOURCE_SET_NAME = "com.miui.android.resourcebrowser.RESOURCE_SET_NAME";
  public static final String RESOURCE_SET_PACKAGE = "com.miui.android.resourcebrowser.RESOURCE_SET_PACKAGE";
  public static final String RESOURCE_SET_SUBPACKAGE = "com.miui.android.resourcebrowser.RESOURCE_SET_SUBPACKAGE";
  public static final String RESOURCE_SET_SUBPACKAGE_LOCAL = ".local";
  public static final String RESOURCE_SET_SUBPACKAGE_ONLINE_HOTTEST = ".online.hottest";
  public static final String RESOURCE_SET_SUBPACKAGE_ONLINE_LATEST = ".online.latest";
  public static final String RESOURCE_SET_SUBPACKAGE_SINGLE = ".single";
  public static final String RESOURCE_TYPE_PARAMETER = "com.miui.android.resourcebrowser.RESOURCE_TYPE_PARAMETER";
  public static final String RESOURCE_URL = "com.miui.android.resourcebrowser.RESOURCE_URL";
  public static final String SHOW_RINGTONE_NAME = "com.miui.android.resourcebrowser.SHOW_RINGTONE_NAME";
  public static final String SOURCE_FOLDERS = "com.miui.android.resourcebrowser.SOURCE_FOLDERS";
  public static final String THUMBNAIL_PREFIX = "com.miui.android.resourcebrowser.THUMBNAIL_PREFIX";
  public static final String THUMBNAIL_PREFIX_INDICATOR = "com.miui.android.resourcebrowser.THUMBNAIL_PREFIX_INDICATOR";
  public static final String TRACK_ID = "com.miui.android.resourcebrowser.TRACK_ID";
  public static final String USING_PICKER = "com.miui.android.resourcebrowser.USING_PICKER";
  public static final String VERSION_SUPPORTED = "com.miui.android.resourcebrowser.VERSION_SUPPORTED";
  public static final String VERSION_URL = "com.miui.android.resourcebrowser.VERSION_URL";
  public static final int ZIP_RESOURCE;

  public static abstract interface ThemeWidget
  {
    public static final String ACTION_SELECT_THEME_RESOURCE = "android.intent.action.PICK_RESOURCE";
    public static final String CLOCK_COMPONENT_NAME = "clock";
    public static final String PHOTO_FRAME_COMPONENT_NAME = "photo_frame";
    public static final String RETURN_SELECT_THEME_PATH = "com.miui.android.resourcebrowser.PICKED_RESOURCE";
    public static final String SHOW_COMPONENT_NAME = "android.intent.extra.SHOW_COMPONENT_NAME";
    public static final String SHOW_COMPONENT_SIZE = "android.intent.extra.SHOW_COMPONENT_SIZE";
    public static final String WIDGET_SIZE_1x2 = "1x2";
    public static final String WIDGET_SIZE_2x2 = "2x2";
    public static final String WIDGET_SIZE_2x4 = "2x4";
    public static final String WIDGET_SIZE_4x4 = "4x4";
    public static final String sClockPath = "clock_";
    public static final String sPhotoFramePath = "photoframe_";
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.IntentConstants
 * JD-Core Version:    0.6.0
 */