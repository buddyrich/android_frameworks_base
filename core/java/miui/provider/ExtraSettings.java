package miui.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.view.ViewConfiguration;
import java.io.File;
import miui.os.Build;

public class ExtraSettings
{
  public static class Secure
  {
    public static final String ACCESS_CONTROL_LOCK_ENABLED = "access_control_lock_enabled";
    public static String MOBILE_DOWNLOAD_FILE_SIZE_PROMPT_POPUP_ENABLED = "mobile_download_file_size_prompt_popup_enabled";
    public static final String PASSWORD_FOR_PRIVACYMODE = "password_for_privacymode";
    public static final String PRIVACY_MODE_ENABLED = "privacy_mode_enabled";
    public static final String SCREEN_BUTTONS_HAS_BEEN_DISABLED = "screen_buttons_has_been_disabled";
    public static final String SCREEN_BUTTONS_STATE = "screen_buttons_state";
    public static final String USB_MODE = "usb_mode";
    public static final int USB_MODE_ASK_USER = 0;
    public static final int USB_MODE_CHARGE_ONLY = 1;
    public static final int USB_MODE_MOUNT_STORAGE = 2;
    private static long sStorageThreshold = 0L;

    public static Cursor checkPrivacyAndReturnCursor(Context paramContext)
    {
      int i;
      String[] arrayOfString;
      if (1 == Settings.Secure.getInt(paramContext.getContentResolver(), "privacy_mode_enabled", 0))
      {
        i = 1;
        if (i == 0)
          break label46;
        arrayOfString = new String[1];
        arrayOfString[0] = "_id";
      }
      label46: for (MatrixCursor localMatrixCursor = new MatrixCursor(arrayOfString); ; localMatrixCursor = null)
      {
        return localMatrixCursor;
        i = 0;
        break;
      }
    }

    public static long getStorageThreshold(ContentResolver paramContentResolver)
    {
      if (sStorageThreshold == 0L)
      {
        int i = Settings.Secure.getInt(paramContentResolver, "sys_storage_threshold_percentage", 10);
        StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
        sStorageThreshold = localStatFs.getBlockCount() * localStatFs.getBlockSize() * i / 100L;
      }
      return sStorageThreshold;
    }
  }

  public static final class System
  {
    public static final String ALWAYS_ENABLE_MMS = "always_enable_mms";
    public static final String ANTI_PRIVATE_CALL_ENABLED = "anti_private_call";
    public static final String ANTI_STRANGER_CALL_ENABLED = "anti_stranger_call";
    public static final String AUTOIP_PREFIX = "autoip_prefix";
    public static final String AUTOIP_SWITCH = "button_autoip";
    public static final int AUTOIP_SWITCH_DEFAULT = 0;
    public static final String AUTO_COUNTRY_CODE = "auto_country_code";
    public static final int AUTO_COUNTRY_CODE_DEFAULT = 0;
    public static final String BACK_KEY_LONG_PRESS_TIMEOUT = "back_key_long_press_timeout";
    public static final int BACK_KEY_LONG_PRESS_TIMEOUT_DEFAULT = 0;
    public static final String BATTERY_INDICATOR_STYLE = "battery_indicator_style";
    public static final int BATTERY_INDICATOR_STYLE_GRAPHIC = 0;
    public static final int BATTERY_INDICATOR_STYLE_NUMBER = 1;
    public static final int BATTERY_INDICATOR_STYLE_TOP = 2;
    public static final String BATTERY_LEVEL_LOW_CUSTOMIZED = "battery_level_low_customized";
    public static final String BLACKLIST_ENABLED = "blacklist_enabled";
    public static final String BYPASS_LOCK_SCREEN = "bypass_lock_screen";
    public static final String CALL_BREATHING_LIGHT_COLOR = "call_breathing_light_color";
    public static final int CALL_BREATHING_LIGHT_COLOR_DEFAULT = 0;
    public static final String CALL_BREATHING_LIGHT_FREQ = "call_breathing_light_freq";
    public static final int CALL_BREATHING_LIGHT_FREQ_DEFAULT = 0;
    public static final String CAMERA_KEY_PREFERRED_ACTION_APP_COMPONENT = "camera_key_preferred_action_app_component";
    public static final boolean CAMERA_KEY_PREFERRED_ACTION_ENABLED = false;
    public static final String CAMERA_KEY_PREFERRED_ACTION_SHORTCUT_ID = "camera_key_preferred_action_shortcut_id";
    public static final int CAMERA_KEY_PREFERRED_ACTION_SHORTCUT_ID_CALL = 3;
    public static final int CAMERA_KEY_PREFERRED_ACTION_SHORTCUT_ID_HOME = 0;
    public static final int CAMERA_KEY_PREFERRED_ACTION_SHORTCUT_ID_SCREENSHOT = 1;
    public static final int CAMERA_KEY_PREFERRED_ACTION_SHORTCUT_ID_SEARCH = 2;
    public static final int CAMERA_KEY_PREFERRED_ACTION_SHORTCUT_ID_WAKE = 4;
    public static final String CAMERA_KEY_PREFERRED_ACTION_TOGGLE_ID = "camera_key_preferred_action_toggle_id";
    public static final String CAMERA_KEY_PREFERRED_ACTION_TYPE = "camera_key_preferred_action_type";
    public static final int CAMERA_KEY_PREFERRED_ACTION_TYPE_APP = 3;
    public static final int CAMERA_KEY_PREFERRED_ACTION_TYPE_DEFAULT = 0;
    public static final int CAMERA_KEY_PREFERRED_ACTION_TYPE_NONE = 0;
    public static final int CAMERA_KEY_PREFERRED_ACTION_TYPE_SHORTCUT = 1;
    public static final int CAMERA_KEY_PREFERRED_ACTION_TYPE_TOGGLE = 2;
    public static final String CHECK_UPDATE_ONLY_WIFI_AVAILABLE = "check_update_only_wifi_available";
    public static final int CHECK_UPDATE_ONLY_WIFI_AVAILABLE_DEFAULT = 1;
    public static final String CONFIRM_MIUI_DISCLAIMER = "confirm_miui_disclaimer";
    public static final String CONTACT_COUNTRYCODE = "persist.radio.countrycode";
    public static final String CURRENT_AREACODE = "current_areacode";
    public static final Uri DEFAULT_SMS_DELIVERED_RINGTONE_URI;
    public static final Uri DEFAULT_SMS_RECEIVED_RINGTONE_URI;
    public static final String DIALER_AUTO_DIALPAD = "dialer_auto_dialpad";
    public static final int DIALER_AUTO_DIALPAD_DEFAULT = 1;
    public static final String DIALER_CLICK = "dialer_click_setting";
    public static final int DIALER_CLICK_DEFAULT = 0;
    public static final int DIALER_CLICK_DIAL = 0;
    public static final int DIALER_CLICK_VIEW_CONTACT = 1;
    public static final String DIALER_SHOW_CALL_LOG_NUMBER = "dialer_show_call_log_number";
    public static final String DOWNLOAD_ONLY_ON_WIFI = "download_only_on_wifi";
    public static final String ELECTRON_BEAM_ANIMATION_OFF = "electron_beam_animation_off";
    public static final String ELECTRON_BEAM_ANIMATION_ON = "electron_beam_animation_on";
    public static final String ENABLE_TELOCATION = "enable_telocation";
    public static final int ENABLE_TELOCATION_DEFAULT = 1;
    public static final String FAKE_MOBILE_OPERATOR_FOR_VENDING = "fake_mobile_operator_for_vending";
    public static final String FILTER_STRANGER_SMS_ENABLED = "filter_stranger_sms";
    public static final String FIREWALL_CALL_ACT = "firewall_call_act";
    public static final int FIREWALL_CALL_ACT_HANGUP = 0;
    public static final int FIREWALL_CALL_ACT_MUTE = 1;
    public static final String FIREWALL_ENABLED = "firewall_enabled";
    public static final String FIREWALL_END_TIME = "firewall_end_time";
    public static final String FIREWALL_HIDE_CALLLOG = "firewall_hide_calllog";
    public static final String FIREWALL_SMS_ACT = "firewall_sms_act";
    public static final int FIREWALL_SMS_ACT_DELETE = 2;
    public static final int FIREWALL_SMS_ACT_IGNORE = 0;
    public static final int FIREWALL_SMS_ACT_MUTE = 1;
    public static final String FIREWALL_START_TIME = "firewall_start_time";
    public static final String FIREWALL_TIME_LIMIT_ENABLED = "firewall_time_limit_enabled";
    public static final String FONT_SIZE = "font_size";
    public static final String HAPTIC_FEEDBACK_LEVEL = "haptic_feedback_level";
    public static final int HAPTIC_FEEDBACK_LEVEL_DEFAULT = 1;
    public static final int HAPTIC_FEEDBACK_LEVEL_MAX = 2;
    public static final int HAPTIC_FEEDBACK_LEVEL_MIN = 0;
    public static final String INCOMING_CALL_LIMIT = "incoming_call_limit_setting";
    public static final int INCOMING_CALL_LIMIT_ADDRESSBOOK_CONTACT_ONLY = 1;
    public static final int INCOMING_CALL_LIMIT_BLOCK_ALL = 3;
    public static final int INCOMING_CALL_LIMIT_NO_LIMIT = 0;
    public static final int INCOMING_CALL_LIMIT_STARRED_CONTACT_ONLY = 2;
    public static final String KEEP_CONTACTS_IN_MEMORY = "keep_contacts_in_memory";
    public static final int KEEP_CONTACTS_IN_MEMORY_DEFAULT = 0;
    public static final String KEEP_LAUNCHER_IN_MEMORY = "keep_launcher_in_memory";
    public static final int KEEP_LAUNCHER_IN_MEMORY_DEFAULT = 1;
    public static final String KEEP_MMS_IN_MEMORY = "keep_mms_in_memory";
    public static final int KEEP_MMS_IN_MEMORY_DEFAULT = 1;
    public static final String KEYGUARD_DISABLE_POWER_KEY_LONG_PRESS = "keyguard_disable_power_key_long_press";
    public static final int KEYGUARD_DISABLE_POWER_KEY_LONG_PRESS_DEFAULT = 0;
    public static final int LAST_MINUTE_OF_DAY = 1439;
    public static final String MAX_DOWNLOADS = "max_downloads";
    public static final String MAX_DOWNLOADS_PER_DOMAIN = "max_downloads_per_domain";
    public static final int MIUI_DISCLAIMER_ACCEPT = 1;
    public static final int MIUI_DISCLAIMER_REFUSE = 0;
    public static final String SMS_DELIVERED_SOUND = "sms_delivered_sound";
    public static final String SMS_NOTIFICATION_BODY_ENABLED = "pref_key_enable_notification_body";
    public static final String SMS_NOTIFICATION_ENABLED = "pref_key_enable_notification";
    public static final String SMS_RECEIVED_SOUND = "sms_received_sound";
    public static final String SMS_WAKE_UP_SCREEN_ENABLED = "pref_key_enable_wake_up_screen";
    public static final String STATUS_BAR_EXPANDABLE_UNDER_FULLSCREEN = "status_bar_expandable_under_fullscreen";
    public static final String STATUS_BAR_EXPANDABLE_UNDER_KEYGUARD = "status_bar_expandable_under_keyguard";
    public static final String STATUS_BAR_NOTIFICATION_FILTER_BLACK_LIST = "status_bar_notification_filter_black_list";
    public static final String STATUS_BAR_NOTIFICATION_FILTER_ENABLED = "status_bar_notification_filter_enabled";
    public static final int STATUS_BAR_NOTIFICATION_FILTER_ENABLED_DEFAULT = 1;
    public static final String STATUS_BAR_NOTIFICATION_FILTER_MODE = "status_bar_notification_filter_mode";
    public static final int STATUS_BAR_NOTIFICATION_FILTER_MODE_BLACK_LIST = 0;
    public static final int STATUS_BAR_NOTIFICATION_FILTER_MODE_DEFAULT = 1;
    public static final int STATUS_BAR_NOTIFICATION_FILTER_MODE_WHITE_LIST = 1;
    public static final String STATUS_BAR_NOTIFICATION_FILTER_WHITE_LIST = "status_bar_notification_filter_white_list";
    public static final String STATUS_BAR_STYLE = "status_bar_style";
    public static final int STATUS_BAR_STYLE_DEFAULT = 1;
    public static final int STATUS_BAR_STYLE_LIST = 0;
    public static final int STATUS_BAR_STYLE_PAGE = 1;
    public static final String STATUS_BAR_TOGGLE_LIST = "status_bar_toggle_list";
    public static final String STATUS_BAR_TOGGLE_PAGE = "status_bar_toggle_page";
    public static final String TORCH_STATE = "torch_state";
    public static final String TRACKBALL_WAKE_SCREEN = "trackball_wake_screen";
    public static final String UPDATE_STABLE_VERSION_ONLY = "update_stable_version_only";
    public static final int UPDATE_STABLE_VERSION_ONLY_DEFAULT = 0;
    public static final String UPLOAD_LOG = "upload_log_pref";
    public static final String USER_APPS_KEPT_IN_MEMORY = "user_apps_kept_in_memory";
    public static final String USER_APPS_KEPT_IN_MEMORY_DEFAULT = "";
    public static final String USER_GUIDE_LOCK_SCREEN_UNLOCK = "user_guide_lock_screen_unlock";
    public static final String USER_GUIDE_STATUS_BAR_CALL = "user_guide_status_bar_call";
    public static final String USER_GUIDE_STATUS_BAR_NOTIFICATION = "user_guide_status_bar_notification";
    public static final String USER_GUIDE_STATUS_BAR_TOGGLE = "user_guide_status_bar_toggle";
    public static final String USER_GUIDE_STATUS_BAR_TOGGLE_LIST = "user_guide_status_bar_toggle_list";
    public static final String VIBRATE_IN_NORMAL = "vibrate_in_normal";
    public static final String VIBRATE_MMS_PREF = "vibrate_mms";
    public static final int VIBRATE_MMS_PREF_DEFAULT = 1;
    public static final String VIBRATE_NOTIFICATION_PREF = "vibrate_notification";
    public static final int VIBRATE_NOTIFICATION_PREF_DEFAULT = 1;
    public static final String VIBRATE_RINGER_PREF = "vibrate_ringer";
    public static final int VIBRATE_RINGER_PREF_DEFAULT = 1;
    public static final String VOLUMEKEY_WAKE_SCREEN = "volumekey_wake_screen";
    public static final String WHITELIST_ENABLED = "whitelist_enabled";
    public static final String XIAOMI_ACCOUNT_MITALK_ENABLE_PROPERTY = "persist.sys.mitalk.enable";
    public static final boolean XIAOMI_ACCOUNT_MITALK_ENABLE_PROPERTY_DEFAULT = true;

    static
    {
      if ((Build.IS_MIONE) || (Build.IS_MILESTONE) || ("vision".equals(Build.DEVICE)));
      for (boolean bool = true; ; bool = false)
      {
        CAMERA_KEY_PREFERRED_ACTION_ENABLED = bool;
        CALL_BREATHING_LIGHT_COLOR_DEFAULT = Resources.getSystem().getColor(50790409);
        CALL_BREATHING_LIGHT_FREQ_DEFAULT = Resources.getSystem().getColor(50855937);
        DEFAULT_SMS_RECEIVED_RINGTONE_URI = Settings.System.getUriFor("sms_received_sound");
        DEFAULT_SMS_DELIVERED_RINGTONE_URI = Settings.System.getUriFor("sms_delivered_sound");
        return;
      }
    }

    private static int getDefaultPref(String paramString)
    {
      if ("vibrate_mms".equals(paramString));
      do
        return 1;
      while (("vibrate_ringer".equals(paramString)) || ("vibrate_notification".equals(paramString)));
      throw new IllegalArgumentException("non-support default value for " + paramString);
    }

    /** @deprecated */
    public static String getString(ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      monitorenter;
      try
      {
        String str1 = Settings.System.getString(paramContentResolver, paramString1);
        String str2 = str1;
        if (str2 == null)
          str2 = paramString2;
        monitorexit;
        return str2;
      }
      finally
      {
        localObject = finally;
        monitorexit;
      }
      throw localObject;
    }

    public static boolean isVibratePrefOn(Context paramContext, String paramString)
    {
      int i = 1;
      if (Settings.System.getInt(paramContext.getContentResolver(), paramString, getDefaultPref(paramString)) == i);
      while (true)
      {
        return i;
        i = 0;
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.provider.ExtraSettings
 * JD-Core Version:    0.6.0
 */