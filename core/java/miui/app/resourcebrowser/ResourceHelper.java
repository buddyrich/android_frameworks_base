package miui.app.resourcebrowser;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import miui.os.ExtraFileUtils;
import org.apache.http.entity.InputStreamEntity;

public class ResourceHelper
  implements IntentConstants
{
  private static Map<String, FolderInfo> mFolderInfoCache = new HashMap();

  public static void addNoMedia(String paramString)
  {
    File localFile = new File(paramString);
    if (localFile.isDirectory());
    try
    {
      new File(localFile, ".nomedia").createNewFile();
      label30: return;
    }
    catch (IOException localIOException)
    {
      break label30;
    }
  }

  public static Bundle buildDefaultMetaData(Bundle paramBundle, String paramString, Context paramContext)
  {
    boolean bool;
    LinkedHashSet localLinkedHashSet;
    label156: label176: String str1;
    if ("android.intent.action.RINGTONE_PICKER".equals(paramString))
    {
      paramBundle.putBoolean("com.miui.android.resourcebrowser.USING_PICKER", true);
      paramBundle.putInt("com.miui.android.resourcebrowser.DISPLAY_TYPE", 6);
      paramBundle.putBoolean("com.miui.android.resourcebrowser.CATEGORY_SUPPORTED", true);
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_SET_NAME", paramBundle.getString("android.intent.extra.ringtone.TITLE"));
      Uri localUri1 = (Uri)paramBundle.getParcelable("android.intent.extra.ringtone.EXISTING_URI");
      Uri localUri2 = (Uri)paramBundle.getParcelable("android.intent.extra.ringtone.DEFAULT_URI");
      if (localUri1 != null)
      {
        if (!localUri1.equals(localUri2))
        {
          bool = true;
          paramBundle.putString("com.miui.android.resourcebrowser.CURRENT_USING_PATH", getPathByUri(paramContext, localUri1, bool));
        }
      }
      else
      {
        localLinkedHashSet = new LinkedHashSet();
        switch (paramBundle.getInt("android.intent.extra.ringtone.TYPE", 7))
        {
        case 3:
        case 5:
        case 6:
        default:
          paramBundle.putStringArray("com.miui.android.resourcebrowser.SOURCE_FOLDERS", (String[])localLinkedHashSet.toArray(new String[0]));
          if (paramBundle.getInt("com.miui.android.resourcebrowser.DISPLAY_TYPE", -1) == -1)
            paramBundle.putInt("com.miui.android.resourcebrowser.DISPLAY_TYPE", 4);
          if (paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_SET_PACKAGE") == null)
            paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_SET_PACKAGE", paramContext.getPackageName());
          if (paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_SET_NAME") == null)
            paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_SET_NAME", paramContext.getString(51118120));
          if (paramBundle.getInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY", -1) == -1)
            paramBundle.putInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY", 0);
          if (paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE") == null);
          switch (paramBundle.getInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY"))
          {
          default:
            label296: if (paramBundle.getStringArray("com.miui.android.resourcebrowser.PREVIEW_PREFIX") == null)
            {
              String[] arrayOfString2 = new String[1];
              arrayOfString2[0] = ("preview_" + paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE") + "_");
              paramBundle.putStringArray("com.miui.android.resourcebrowser.PREVIEW_PREFIX", arrayOfString2);
            }
            if (paramBundle.getStringArray("com.miui.android.resourcebrowser.PREVIEW_PREFIX_INDICATOR") == null)
              paramBundle.putStringArray("com.miui.android.resourcebrowser.PREVIEW_PREFIX_INDICATOR", paramBundle.getStringArray("com.miui.android.resourcebrowser.PREVIEW_PREFIX"));
            if (paramBundle.getStringArray("com.miui.android.resourcebrowser.THUMBNAIL_PREFIX") == null)
              paramBundle.putStringArray("com.miui.android.resourcebrowser.THUMBNAIL_PREFIX", paramBundle.getStringArray("com.miui.android.resourcebrowser.PREVIEW_PREFIX"));
            if (paramBundle.getStringArray("com.miui.android.resourcebrowser.THUMBNAIL_PREFIX_INDICATOR") == null)
              paramBundle.putStringArray("com.miui.android.resourcebrowser.THUMBNAIL_PREFIX_INDICATOR", paramBundle.getStringArray("com.miui.android.resourcebrowser.THUMBNAIL_PREFIX"));
            if (paramBundle.getStringArray("com.miui.android.resourcebrowser.SOURCE_FOLDERS") == null)
            {
              String[] arrayOfString1 = new String[1];
              arrayOfString1[0] = (ResourceConstants.MIUI_PATH + paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE"));
              paramBundle.putStringArray("com.miui.android.resourcebrowser.SOURCE_FOLDERS", arrayOfString1);
            }
            if (paramBundle.getString("com.miui.android.resourcebrowser.DOWNLOAD_FOLDER") == null)
              paramBundle.putString("com.miui.android.resourcebrowser.DOWNLOAD_FOLDER", ResourceConstants.MIUI_PATH + paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE"));
            if (paramBundle.getString("com.miui.android.resourcebrowser.CACHE_LIST_FOLDER") == null)
            {
              String str2 = ResourceConstants.LIST_PATH;
              if (paramContext.getFilesDir() != null)
                str2 = paramContext.getFilesDir().getAbsolutePath();
              paramBundle.putString("com.miui.android.resourcebrowser.CACHE_LIST_FOLDER", str2);
            }
            str1 = paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_TYPE_PARAMETER");
            if (str1 == null)
              switch (paramBundle.getInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY"))
              {
              default:
                label588: if (paramBundle.getString("com.miui.android.resourcebrowser.VERSION_URL") == null)
                  paramBundle.putString("com.miui.android.resourcebrowser.VERSION_URL", "r=xmXshare/themeDetails&id=%s");
                if (paramBundle.getString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_CLASS") == null)
                {
                  paramBundle.putString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_CLASS", LocalResourceListActivity.class.getName());
                  if (paramBundle.getString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_PACKAGE") == null)
                    paramBundle.putString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_PACKAGE", "android");
                  label642: if (paramBundle.getString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_CLASS") != null)
                    break label1173;
                  paramBundle.putString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_CLASS", OnlineResourceListActivity.class.getName());
                  if (paramBundle.getString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_PACKAGE") == null)
                    paramBundle.putString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_PACKAGE", "android");
                  label679: if (paramBundle.getString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_CLASS") != null)
                    break label1195;
                  paramBundle.putString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_CLASS", ResourceDetailActivity.class.getName());
                  if (paramBundle.getString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_PACKAGE") == null)
                    paramBundle.putString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_PACKAGE", "android");
                }
              case 0:
              case 1:
              case 2:
              }
          case 0:
          case 1:
          case 2:
          }
        case 1:
        case 2:
        case 4:
        case 7:
        }
      }
    }
    while (true)
    {
      return paramBundle;
      bool = false;
      break;
      localLinkedHashSet.add("/system/media/audio/ringtones");
      localLinkedHashSet.add(ResourceConstants.DOWNLOADED_RINGTONE_PATH);
      break label156;
      localLinkedHashSet.add("/system/media/audio/notifications");
      localLinkedHashSet.add(ResourceConstants.DOWNLOADED_NOTIFICATION_PATH);
      break label156;
      localLinkedHashSet.add("/system/media/audio/alarms");
      localLinkedHashSet.add(ResourceConstants.DOWNLOADED_ALARM_PATH);
      break label156;
      localLinkedHashSet.add("/system/media/audio/ringtones");
      localLinkedHashSet.add("/system/media/audio/notifications");
      localLinkedHashSet.add("/system/media/audio/alarms");
      localLinkedHashSet.add(ResourceConstants.DOWNLOADED_RINGTONE_PATH);
      localLinkedHashSet.add(ResourceConstants.DOWNLOADED_NOTIFICATION_PATH);
      localLinkedHashSet.add(ResourceConstants.DOWNLOADED_ALARM_PATH);
      break label156;
      if ("android.intent.action.SET_WALLPAPER".equals(paramString))
      {
        paramBundle.putBoolean("com.miui.android.resourcebrowser.USING_PICKER", false);
        paramBundle.putInt("com.miui.android.resourcebrowser.DISPLAY_TYPE", 1);
        paramBundle.putBoolean("com.miui.android.resourcebrowser.CATEGORY_SUPPORTED", true);
        break label176;
      }
      if ("android.intent.action.SET_LOCKSCREEN_WALLPAPER".equals(paramString))
      {
        paramBundle.putBoolean("com.miui.android.resourcebrowser.USING_PICKER", false);
        paramBundle.putInt("com.miui.android.resourcebrowser.DISPLAY_TYPE", 2);
        paramBundle.putBoolean("com.miui.android.resourcebrowser.CATEGORY_SUPPORTED", true);
        break label176;
      }
      if ("android.intent.action.PICK_RESOURCE".equals(paramString))
      {
        paramBundle.putBoolean("com.miui.android.resourcebrowser.USING_PICKER", true);
        break label176;
      }
      paramBundle.putBoolean("com.miui.android.resourcebrowser.USING_PICKER", false);
      break label176;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE", "theme");
      break label296;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE", "wallpaper");
      break label296;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE", "ringtone");
      break label296;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_TYPE_PARAMETER", "1");
      break label588;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_TYPE_PARAMETER", "2");
      break label588;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_TYPE_PARAMETER", "3");
      break label588;
      StringBuilder localStringBuilder1 = new StringBuilder();
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = str1;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_HOTTEST_URL", String.format("r=xmXshare/index&type=%s&", arrayOfObject1) + "begin=%d&length=%d&sort=hot&uistart=%d&uiend=%d&formatstart=%d&formatend=%d");
      StringBuilder localStringBuilder2 = new StringBuilder();
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = str1;
      paramBundle.putString("com.miui.android.resourcebrowser.RESOURCE_LATEST_URL", String.format("r=xmXshare/index&type=%s&", arrayOfObject2) + "begin=%d&length=%d&sort=time&uistart=%d&uiend=%d&formatstart=%d&formatend=%d");
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = str1;
      paramBundle.putString("com.miui.android.resourcebrowser.CATEGORY_URL", String.format("r=xmXshare/category&type=%s", arrayOfObject3));
      break label588;
      if (paramBundle.getString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_PACKAGE") != null)
        break label642;
      paramBundle.putString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_PACKAGE", paramContext.getPackageName());
      break label642;
      label1173: if (paramBundle.getString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_PACKAGE") != null)
        break label679;
      paramBundle.putString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_PACKAGE", paramContext.getPackageName());
      break label679;
      label1195: if (paramBundle.getString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_PACKAGE") != null)
        continue;
      paramBundle.putString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_PACKAGE", paramContext.getPackageName());
    }
  }

  public static void copyIntData(Bundle paramBundle1, Bundle paramBundle2, String paramString)
  {
    paramBundle2.putInt(paramString, paramBundle1.getInt(paramString));
  }

  public static void copyLongData(Bundle paramBundle1, Bundle paramBundle2, String paramString)
  {
    paramBundle2.putLong(paramString, paramBundle1.getLong(paramString));
  }

  public static void copyResourceInformation(Resource paramResource1, Resource paramResource2)
  {
    Bundle localBundle1 = paramResource1.getInformation();
    Bundle localBundle2 = paramResource2.getInformation();
    copyStringData(localBundle1, localBundle2, "online_path");
    copyStringData(localBundle1, localBundle2, "version");
    copyIntData(localBundle1, localBundle2, "ui_version");
    copyLongData(localBundle1, localBundle2, "m_lastupdate");
    paramResource2.setInformation(localBundle2);
  }

  public static void copyStringData(Bundle paramBundle1, Bundle paramBundle2, String paramString)
  {
    paramBundle2.putString(paramString, paramBundle1.getString(paramString));
  }

  public static void exit(Activity paramActivity)
  {
    AlertDialog localAlertDialog = new AlertDialog.Builder(paramActivity).setIcon(17301659).setTitle(51118083).setMessage(51118084).setPositiveButton(17039370, null).create();
    localAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener(paramActivity)
    {
      public void onDismiss(DialogInterface paramDialogInterface)
      {
        ((ActivityManager)ResourceHelper.this.getSystemService("activity")).forceStopPackage(ResourceHelper.this.getPackageName());
      }
    });
    localAlertDialog.show();
  }

  public static Uri getAndInsertMediaEntryByPath(Context paramContext, String paramString)
  {
    Uri localUri;
    if (TextUtils.isEmpty(paramString))
      localUri = null;
    while (true)
    {
      return localUri;
      localUri = getMediaEntryByPath(paramContext, paramString);
      if (localUri != null)
        continue;
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("_data", paramString);
      localContentValues.put("is_music", Integer.valueOf(0));
      localUri = paramContext.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(paramString), localContentValues);
    }
  }

  public static String getContentColumnValue(Context paramContext, Uri paramUri, String paramString)
  {
    String str = null;
    try
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      String[] arrayOfString = new String[1];
      arrayOfString[0] = paramString;
      Cursor localCursor = localContentResolver.query(paramUri, arrayOfString, null, null, null);
      if (localCursor != null)
      {
        if (localCursor.moveToFirst())
          str = localCursor.getString(0);
        localCursor.close();
      }
      if (str != null)
        return str;
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        continue;
        str = "";
      }
    }
  }

  public static String getDefaultFormatPlayingRingtoneName(String paramString, int paramInt1, int paramInt2)
  {
    int i = paramString.lastIndexOf('.');
    if (i < 0)
      i = paramString.length();
    String str = paramString.substring(1 + paramString.lastIndexOf(File.separatorChar), i);
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = str;
    arrayOfObject[1] = Integer.valueOf(paramInt1 + 1);
    arrayOfObject[2] = Integer.valueOf(paramInt2);
    return String.format("%s (%d/%d)", arrayOfObject);
  }

  public static List<String> getDefaultMusicPlayList(Context paramContext, Resource paramResource)
  {
    ArrayList localArrayList = new ArrayList();
    String str1 = paramResource.getLocalPath();
    String str2 = paramResource.getOnlinePath();
    String str3 = getPathByUri(paramContext, getUriByPath(str1));
    if (new File(str3).exists())
      localArrayList.add(str3);
    while (true)
    {
      return localArrayList;
      if (str2 == null)
        continue;
      localArrayList.add(str2);
    }
  }

  public static String getDirectoryPath(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = "";
    while (true)
    {
      return paramString;
      int i = paramString.lastIndexOf(File.separator);
      if (i <= -1)
        continue;
      paramString = paramString.substring(0, i);
    }
  }

  private static FileInfo getFileInfoById(String paramString1, String paramString2)
  {
    FolderInfo localFolderInfo = getFolderInfoCache(paramString2);
    if (localFolderInfo != null);
    for (FileInfo localFileInfo = (FileInfo)localFolderInfo.ids.get(paramString1); ; localFileInfo = null)
      return localFileInfo;
  }

  public static String getFileName(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = "";
    while (true)
    {
      return paramString;
      int i = paramString.lastIndexOf(File.separator);
      if (i <= -1)
        continue;
      paramString = paramString.substring(i + 1);
    }
  }

  public static FolderInfo getFolderInfoCache(String paramString)
  {
    refreshFolderInfoCache(paramString);
    return (FolderInfo)mFolderInfoCache.get(paramString);
  }

  public static String getFormattedDirectoryPath(String paramString)
  {
    if (paramString.endsWith(File.separator));
    while (true)
    {
      return paramString;
      paramString = paramString + File.separator;
    }
  }

  public static String getFormattedSize(Context paramContext, long paramLong)
  {
    Object[] arrayOfObject2;
    if (paramLong < 1048576.0D)
    {
      arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Double.valueOf(paramLong / 1024.0D);
      arrayOfObject2[1] = paramContext.getResources().getString(51118459);
    }
    Object[] arrayOfObject1;
    for (String str = String.format("%.0f%s", arrayOfObject2); ; str = String.format("%.1f%s", arrayOfObject1))
    {
      return str;
      arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = Double.valueOf(paramLong / 1048576.0D);
      arrayOfObject1[1] = paramContext.getResources().getString(51118460);
    }
  }

  public static Uri getMediaEntryByPath(Context paramContext, String paramString)
  {
    Uri localUri1 = null;
    Uri localUri2 = MediaStore.Audio.Media.getContentUriForPath(paramString);
    ContentResolver localContentResolver = paramContext.getContentResolver();
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "_id";
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = paramString;
    Cursor localCursor = localContentResolver.query(localUri2, arrayOfString1, "_data=?", arrayOfString2, null);
    if (localCursor != null)
    {
      if (localCursor.moveToFirst())
        localUri1 = ContentUris.withAppendedId(localUri2, localCursor.getLong(0));
      localCursor.close();
    }
    return localUri1;
  }

  private static String getPathById(String paramString1, int paramInt, String paramString2)
  {
    FileInfo localFileInfo = getFileInfoById(paramString1, paramString2);
    if ((localFileInfo != null) && (localFileInfo.version == paramInt));
    for (String str = localFileInfo.path; ; str = null)
      return str;
  }

  public static String getPathByUri(Context paramContext, Uri paramUri)
  {
    return getPathByUri(paramContext, paramUri, true);
  }

  public static String getPathByUri(Context paramContext, Uri paramUri, boolean paramBoolean)
  {
    String str1;
    if (paramUri == null)
      str1 = "";
    while (true)
    {
      return str1;
      str1 = paramUri.toString();
      String str2 = paramUri.getScheme();
      if (("content".equals(str2)) && (paramBoolean))
      {
        String str3 = paramUri.getAuthority();
        if ("settings".equals(str3))
          str1 = getContentColumnValue(paramContext, paramUri, "value");
        while (true)
        {
          Uri localUri = Uri.parse(str1);
          if ((localUri.getScheme() == null) || (localUri.equals(paramUri)))
            break;
          str1 = getPathByUri(paramContext, localUri, true);
          break;
          if (!"media".equals(str3))
            continue;
          str1 = getContentColumnValue(paramContext, paramUri, "_data");
        }
      }
      if (!"file".equals(str2))
        continue;
      str1 = paramUri.getPath();
    }
  }

  public static int getResourceStatus(Resource paramResource, Bundle paramBundle)
  {
    int i = 0;
    String str1 = paramResource.getLocalPath();
    if (TextUtils.isEmpty(str1));
    while (true)
    {
      return i;
      File localFile = new File(str1);
      if (paramBundle.getBoolean("com.miui.android.resourcebrowser.VERSION_SUPPORTED"))
      {
        int j = paramResource.getVersion();
        String str2 = paramResource.getId();
        if ((j == 0) || (TextUtils.isEmpty(str2)))
        {
          if (localFile.exists())
            continue;
          i = 2;
          continue;
        }
        FileInfo localFileInfo = getFileInfoById(str2, getDirectoryPath(str1));
        if (localFileInfo != null);
        for (int k = localFileInfo.version; ; k = 0)
        {
          if (k == j)
            break label125;
          if ((k <= 0) || (k >= j))
            break label127;
          i = 1;
          break;
        }
        label125: continue;
        label127: if (localFile.exists())
          continue;
        i = 2;
        continue;
      }
      if (localFile.exists())
        continue;
      i = 2;
    }
  }

  public static Uri getUriByPath(String paramString)
  {
    Uri localUri;
    if (TextUtils.isEmpty(paramString))
      localUri = null;
    while (true)
    {
      return localUri;
      localUri = Uri.parse(paramString);
      if (localUri.getScheme() != null)
        continue;
      localUri = Uri.fromFile(new File(paramString));
    }
  }

  public static String getVersionPath(Resource paramResource, Bundle paramBundle)
  {
    String str1 = paramResource.getLocalPath();
    String str3;
    String str2;
    if ((!TextUtils.isEmpty(str1)) && (paramBundle.getBoolean("com.miui.android.resourcebrowser.VERSION_SUPPORTED")))
    {
      str3 = getDirectoryPath(str1);
      int i = paramResource.getVersion();
      String str4 = paramResource.getId();
      if ((i >= 1) && (!TextUtils.isEmpty(str4)))
      {
        str2 = getPathById(str4, i, str3);
        if (TextUtils.isEmpty(str2));
      }
    }
    while (true)
    {
      return str2;
      str2 = OnlineHelper.getFilePathByResource(str3, paramResource.getInformation());
      continue;
      str2 = str1;
    }
  }

  public static int getViewResource(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    default:
    case 4:
    case 0:
    case 1:
    case 2:
    case 3:
    case 5:
    case 6:
    case 7:
    }
    while (true)
    {
      return i;
      i = 50528265;
      continue;
      i = 50528264;
      continue;
      i = 50528261;
      continue;
      i = 50528263;
      continue;
      i = 50528262;
      continue;
      i = 50528268;
      continue;
      i = 50528267;
      continue;
      i = 50528266;
    }
  }

  public static boolean isCombineView(int paramInt)
  {
    int i = 1;
    if ((paramInt == i) || (paramInt == 2) || (paramInt == 3));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public static boolean isCompatiblePlatformVersion(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt2 <= paramInt1) && (paramInt1 <= paramInt3));
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isDataResource(String paramString)
  {
    if ((paramString != null) && (paramString.startsWith("/data")));
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isMultipleView(int paramInt)
  {
    if (paramInt == 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isSingleView(int paramInt)
  {
    if ((paramInt == 4) || (paramInt == 5) || (paramInt == 6) || (paramInt == 7));
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isSystemResource(String paramString)
  {
    if ((paramString != null) && (paramString.startsWith("/system")));
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isZipResource(String paramString)
  {
    int i = 0;
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return i;
      if ((!paramString.endsWith(".zip")) && (!paramString.endsWith(".mtz")))
        continue;
      File localFile = new File(paramString);
      if ((!localFile.exists()) || (localFile.isDirectory()))
        continue;
      i = 1;
    }
  }

  public static Pair<String, Integer> parseIdVersion(String paramString)
  {
    String str;
    int k;
    if ((paramString.endsWith(".mtz")) || (paramString.endsWith(".zip")))
    {
      int i = paramString.lastIndexOf("_(");
      int j = paramString.lastIndexOf(").");
      if ((i > 0) && (j > 0))
      {
        str = paramString.substring(i + 2, j);
        k = str.indexOf(".");
        if (k <= 0);
      }
    }
    while (true)
    {
      try
      {
        int m = Integer.parseInt(str.substring(0, k));
        int n = Integer.parseInt(str.substring(k + 1));
        localPair = new Pair(String.valueOf(m), Integer.valueOf(n));
        return localPair;
      }
      catch (Exception localException)
      {
      }
      Pair localPair = null;
    }
  }

  public static boolean refreshFolderInfoCache(String paramString)
  {
    File localFile1 = new File(paramString);
    String[] arrayOfString = localFile1.list();
    int i;
    if (arrayOfString == null)
      i = 0;
    while (true)
    {
      return i;
      i = 0;
      long l = localFile1.lastModified();
      int j = arrayOfString.length;
      FolderInfo localFolderInfo = (FolderInfo)mFolderInfoCache.get(paramString);
      if (localFolderInfo == null)
      {
        i = 1;
        localFolderInfo = new FolderInfo();
        localFolderInfo.modifiedTime = l;
        localFolderInfo.filesCount = j;
        localFolderInfo.files = new HashMap();
        localFolderInfo.ids = new HashMap();
        mFolderInfoCache.put(paramString, localFolderInfo);
      }
      if ((i == 0) && (localFolderInfo.modifiedTime == l) && (localFolderInfo.filesCount == j))
        continue;
      Log.i("ResourceBrowser", "refresh folder: " + paramString);
      i = 1;
      localFolderInfo.modifiedTime = l;
      localFolderInfo.filesCount = j;
      localFolderInfo.files.clear();
      localFolderInfo.ids.clear();
      for (int k = 0; k < arrayOfString.length; k++)
      {
        String str = getFormattedDirectoryPath(paramString) + arrayOfString[k];
        File localFile2 = new File(str);
        if (localFile2.isDirectory())
          continue;
        FileInfo localFileInfo1 = new FileInfo();
        localFileInfo1.path = str;
        localFileInfo1.modifiedTime = localFile2.lastModified();
        localFileInfo1.length = localFile2.length();
        Pair localPair = parseIdVersion(arrayOfString[k]);
        if (localPair != null)
        {
          localFileInfo1.id = ((String)localPair.first);
          localFileInfo1.version = ((Integer)localPair.second).intValue();
          FileInfo localFileInfo2 = (FileInfo)localFolderInfo.ids.get(localFileInfo1.id);
          if ((localFileInfo2 == null) || (localFileInfo2.version < localFileInfo1.version))
            localFolderInfo.ids.put(localFileInfo1.id, localFileInfo1);
        }
        localFolderInfo.files.put(localFileInfo1.path, localFileInfo1);
      }
    }
  }

  public static void setMusicVolumeType(Activity paramActivity, int paramInt)
  {
    int i;
    if (paramInt == 1)
      i = 2;
    while (true)
    {
      if (i >= 0)
        paramActivity.setVolumeControlStream(i);
      return;
      if (paramInt == 2)
      {
        i = 5;
        continue;
      }
      if (paramInt == 4)
      {
        i = 4;
        continue;
      }
      i = 3;
    }
  }

  public static void setResourceStatus(String paramString, ResourceSet paramResourceSet, int paramInt)
  {
    if (paramString != null)
      for (int i = 0; i < paramResourceSet.size(); i++)
      {
        Resource localResource = (Resource)paramResourceSet.get(i);
        if (!paramString.equals(localResource.getLocalPath()))
          continue;
        localResource.setStatus(paramInt);
      }
  }

  public static void writeTo(InputStream paramInputStream, String paramString)
  {
    try
    {
      ExtraFileUtils.mkdirs(new File(paramString).getParentFile(), 511, -1, -1);
      InputStreamEntity localInputStreamEntity = new InputStreamEntity(new BufferedInputStream(paramInputStream), -1L);
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString));
      FileUtils.setPermissions(paramString, 511, -1, -1);
      localInputStreamEntity.writeTo(localBufferedOutputStream);
      localBufferedOutputStream.close();
      paramInputStream.close();
      new File(paramString).setLastModified(System.currentTimeMillis());
      return;
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  public static class FileInfo
  {
    public String id;
    public long length;
    public long modifiedTime;
    public String path;
    public int version;
  }

  public static class FolderInfo
  {
    public Map<String, ResourceHelper.FileInfo> files = new HashMap();
    public int filesCount;
    public Map<String, ResourceHelper.FileInfo> ids = new HashMap();
    public long modifiedTime;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceHelper
 * JD-Core Version:    0.6.0
 */