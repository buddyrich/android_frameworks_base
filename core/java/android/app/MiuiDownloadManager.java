package android.app;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import java.util.List;
import miui.provider.ExtraSettings.Secure;

public class MiuiDownloadManager extends DownloadManager
{
  public static final String ACTION_DOWNLOAD_UPDATED = "android.intent.action.DOWNLOAD_UPDATED";
  public static final String ACTION_OPERATE_DOWNLOADSET_UPDATE_PROGRESS = "android.intent.action.OPERATE_DOWNLOADSET_UPDATE_PROGRESS";
  private static final ComponentName DOWNLOAD_UPDATE_RECEIVER_COMPONENT = new ComponentName("com.android.providers.downloads", "com.android.providers.downloads.DownloadUpdateReceiver");
  public static final String EXTRA_DOWNLOAD_CURRENT_BYTES = "extra_download_current_bytes";
  public static final String EXTRA_DOWNLOAD_STATUS = "extra_download_status";
  public static final String EXTRA_DOWNLOAD_TOTAL_BYTES = "extra_download_total_bytes";
  public static final String INTENT_EXTRA_APPLICATION_PACKAGENAME = "intent_extra_application_packagename";
  public static final String INTENT_EXTRA_REGISTER_DOWNLOADS_UPDATE_PROGRESS = "intent_extra_register_downloads_update_progress";
  public static final String INTENT_EXTRA_UNREGISTER_DOWNLOADS_UPDATE_PROGRESS = "intent_extra_unregister_downloads_update_progress";
  public static final int PAUSED_BY_APP = 5;
  private ContentResolver mResolver;

  public MiuiDownloadManager(ContentResolver paramContentResolver, String paramString)
  {
    super(paramContentResolver, paramString);
    this.mResolver = paramContentResolver;
  }

  public static Integer getMobileFileSizePromptEnabled(Context paramContext)
  {
    try
    {
      Integer localInteger2 = Integer.valueOf(Settings.Secure.getInt(paramContext.getContentResolver(), ExtraSettings.Secure.MOBILE_DOWNLOAD_FILE_SIZE_PROMPT_POPUP_ENABLED));
      localInteger1 = localInteger2;
      return localInteger1;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException)
    {
      while (true)
        Integer localInteger1 = null;
    }
  }

  public static boolean isDownloadSuccess(Cursor paramCursor)
  {
    CursorTranslator localCursorTranslator = (CursorTranslator)paramCursor;
    if (localCursorTranslator.getInt(localCursorTranslator.getColumnIndex("status")) == 8);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isDownloading(Cursor paramCursor)
  {
    int i = 0;
    CursorTranslator localCursorTranslator = (CursorTranslator)paramCursor;
    switch (localCursorTranslator.getInt(localCursorTranslator.getColumnIndex("status")))
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      return i;
      i = 1;
    }
  }

  public static void operateDownloadsNeedToUpdateProgress(Context paramContext, long[] paramArrayOfLong1, long[] paramArrayOfLong2)
  {
    Intent localIntent = new Intent("android.intent.action.OPERATE_DOWNLOADSET_UPDATE_PROGRESS");
    localIntent.setComponent(DOWNLOAD_UPDATE_RECEIVER_COMPONENT);
    localIntent.putExtra("intent_extra_register_downloads_update_progress", paramArrayOfLong1);
    localIntent.putExtra("intent_extra_unregister_downloads_update_progress", paramArrayOfLong2);
    paramContext.sendBroadcast(localIntent);
  }

  public static boolean setMobileFileSizePromptEnabled(Context paramContext, boolean paramBoolean)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    String str = ExtraSettings.Secure.MOBILE_DOWNLOAD_FILE_SIZE_PROMPT_POPUP_ENABLED;
    if (paramBoolean);
    for (int i = 1; ; i = 0)
      return Settings.Secure.putInt(localContentResolver, str, i);
  }

  public static boolean setRecommendedMaxBytesOverMobile(Context paramContext, long paramLong)
  {
    return Settings.Secure.putLong(paramContext.getContentResolver(), "download_manager_recommended_max_bytes_over_mobile", paramLong);
  }

  public void pauseDownload(long[] paramArrayOfLong)
  {
    Cursor localCursor = query(new Query().setFilterById(paramArrayOfLong));
    while (true)
    {
      try
      {
        localCursor.moveToFirst();
        if (localCursor.isAfterLast())
          break;
        int i = localCursor.getInt(localCursor.getColumnIndex("status"));
        if ((i != 1) && (i != 2))
          throw new IllegalArgumentException("Cannot pause non-running or non-pending download: " + localCursor.getLong(localCursor.getColumnIndex("_id")));
      }
      finally
      {
        localCursor.close();
      }
      localCursor.moveToNext();
    }
    localCursor.close();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Integer.valueOf(193));
    localContentValues.put("control", Integer.valueOf(1));
    this.mResolver.update(this.mBaseUri, localContentValues, getWhereClauseForIds(paramArrayOfLong), getWhereArgsForIds(paramArrayOfLong));
  }

  public Cursor query(DownloadManager.Query paramQuery)
  {
    Cursor localCursor = paramQuery.runQuery(this.mResolver, UNDERLYING_COLUMNS, this.mBaseUri);
    if (localCursor == null);
    for (CursorTranslator localCursorTranslator = null; ; localCursorTranslator = new CursorTranslator(localCursor, this.mBaseUri))
      return localCursorTranslator;
  }

  public void resumeDownload(long[] paramArrayOfLong)
  {
    Cursor localCursor = query(new Query().setFilterById(paramArrayOfLong));
    while (true)
    {
      try
      {
        localCursor.moveToFirst();
        if (localCursor.isAfterLast())
          break;
        if (localCursor.getInt(localCursor.getColumnIndex("status")) != 4)
          throw new IllegalArgumentException("Cannot resume non-paused download: " + localCursor.getLong(localCursor.getColumnIndex("_id")));
      }
      finally
      {
        localCursor.close();
      }
      localCursor.moveToNext();
    }
    localCursor.close();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Integer.valueOf(192));
    localContentValues.put("control", Integer.valueOf(0));
    this.mResolver.update(this.mBaseUri, localContentValues, getWhereClauseForIds(paramArrayOfLong), getWhereArgsForIds(paramArrayOfLong));
  }

  private static class CursorTranslator extends DownloadManager.CursorTranslator
  {
    public CursorTranslator(Cursor paramCursor, Uri paramUri)
    {
      super(paramUri);
    }

    long getPausedReason(int paramInt)
    {
      long l;
      if (paramInt == 193)
        l = 5L;
      while (true)
      {
        return l;
        l = super.getPausedReason(paramInt);
      }
    }
  }

  public static class Query extends DownloadManager.Query
  {
    private String mAppendedClause;
    private String mColumnAppData;
    private String mColumnNotificationPackage;

    void addExtraSelectionParts(List<String> paramList)
    {
      super.addExtraSelectionParts(paramList);
      if (!TextUtils.isEmpty(this.mColumnAppData))
      {
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = "entity";
        arrayOfObject2[1] = this.mColumnAppData;
        paramList.add(String.format("%s='%s'", arrayOfObject2));
      }
      if (!TextUtils.isEmpty(this.mColumnNotificationPackage))
      {
        Object[] arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = "notificationpackage";
        arrayOfObject1[1] = this.mColumnNotificationPackage;
        paramList.add(String.format("%s='%s'", arrayOfObject1));
      }
      if (!TextUtils.isEmpty(this.mAppendedClause))
        paramList.add(this.mAppendedClause);
    }

    public Query orderBy(String paramString, int paramInt)
    {
      if ((paramInt != 1) && (paramInt != 2))
        throw new IllegalArgumentException("Invalid direction: " + paramInt);
      if (paramString.equals("_id"))
      {
        this.mOrderByColumn = "_id";
        this.mOrderDirection = paramInt;
      }
      while (true)
      {
        return this;
        super.orderBy(paramString, paramInt);
      }
    }

    public Query setFilterByAppData(String paramString)
    {
      this.mColumnAppData = paramString;
      return this;
    }

    public Query setFilterByAppendedClause(String paramString)
    {
      this.mAppendedClause = paramString;
      return this;
    }

    public Query setFilterByNotificationPackage(String paramString)
    {
      this.mColumnNotificationPackage = paramString;
      return this;
    }
  }

  public static class Request extends DownloadManager.Request
  {
    private String mAppointName;
    private String mColumnAppData;
    private String mNotificationClass;
    private String mUserAgent;

    public Request(Uri paramUri)
    {
      super();
    }

    private void putIfNonNull(ContentValues paramContentValues, String paramString, Object paramObject)
    {
      if (paramObject != null)
        paramContentValues.put(paramString, paramObject.toString());
    }

    public Request setAppData(String paramString)
    {
      this.mColumnAppData = paramString;
      return this;
    }

    public Request setAppointName(String paramString)
    {
      this.mAppointName = paramString;
      return this;
    }

    public Request setNotificationClass(String paramString)
    {
      this.mNotificationClass = paramString;
      return this;
    }

    public void setUserAgent(String paramString)
    {
      this.mUserAgent = paramString;
    }

    protected ContentValues toContentValues(String paramString)
    {
      ContentValues localContentValues = super.toContentValues(paramString);
      putIfNonNull(localContentValues, "entity", this.mColumnAppData);
      putIfNonNull(localContentValues, "appointname", this.mAppointName);
      putIfNonNull(localContentValues, "notificationclass", this.mNotificationClass);
      putIfNonNull(localContentValues, "useragent", this.mUserAgent);
      return localContentValues;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.app.MiuiDownloadManager
 * JD-Core Version:    0.6.0
 */