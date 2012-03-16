package miui.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;
import java.util.Calendar;

public final class ExtraTelephony
{
  private static final String[] COLUMNS;
  private static final int DATA_COLUMN = 0;
  private static final String[] KEYWORD_COLUMNS;
  private static final int NAME_COLUMN = 0;
  private static final int STARRED_COLUMN = 1;
  private static final String TAG = "ExtraTelephony";

  static
  {
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "display_name";
    arrayOfString1[1] = "starred";
    COLUMNS = arrayOfString1;
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = "data";
    KEYWORD_COLUMNS = arrayOfString2;
  }

  // ERROR //
  public static boolean checkFirewallForSms(Context paramContext, byte[][] paramArrayOfByte)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +253 -> 254
    //   4: aload_1
    //   5: arraylength
    //   6: istore 5
    //   8: iload 5
    //   10: iconst_1
    //   11: if_icmplt +243 -> 254
    //   14: aload_1
    //   15: iconst_0
    //   16: aaload
    //   17: invokestatic 79	android/telephony/SmsMessage:createFromPdu	([B)Landroid/telephony/SmsMessage;
    //   20: invokevirtual 83	android/telephony/SmsMessage:getOriginatingAddress	()Ljava/lang/String;
    //   23: astore 7
    //   25: new 85	java/lang/StringBuilder
    //   28: dup
    //   29: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   32: astore 8
    //   34: iconst_0
    //   35: istore 9
    //   37: aload_1
    //   38: arraylength
    //   39: istore 10
    //   41: iload 9
    //   43: iload 10
    //   45: if_icmpge +31 -> 76
    //   48: aload 8
    //   50: aload_1
    //   51: iload 9
    //   53: aaload
    //   54: invokestatic 79	android/telephony/SmsMessage:createFromPdu	([B)Landroid/telephony/SmsMessage;
    //   57: invokevirtual 89	android/telephony/SmsMessage:getDisplayMessageBody	()Ljava/lang/String;
    //   60: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: iinc 9 1
    //   67: goto -30 -> 37
    //   70: astore 6
    //   72: iconst_0
    //   73: istore_2
    //   74: iload_2
    //   75: ireturn
    //   76: iconst_0
    //   77: istore 11
    //   79: iconst_0
    //   80: istore 12
    //   82: aload_0
    //   83: aload 7
    //   85: aload 8
    //   87: invokevirtual 96	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   90: invokestatic 100	miui/provider/ExtraTelephony:shouldBlockByFirewall	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)I
    //   93: istore 13
    //   95: iload 13
    //   97: iconst_1
    //   98: if_icmpne +177 -> 275
    //   101: iconst_1
    //   102: istore 11
    //   104: iconst_4
    //   105: istore 12
    //   107: iload 11
    //   109: ifeq +145 -> 254
    //   112: aload_0
    //   113: invokestatic 104	miui/provider/ExtraTelephony:getSmsAct	(Landroid/content/Context;)I
    //   116: iconst_2
    //   117: if_icmpeq +10 -> 127
    //   120: iload 12
    //   122: ldc 105
    //   124: if_icmpne +130 -> 254
    //   127: iload 12
    //   129: iconst_4
    //   130: if_icmpne +95 -> 225
    //   133: ldc 50
    //   135: ldc 107
    //   137: invokestatic 113	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   140: pop
    //   141: new 115	android/content/ContentValues
    //   144: dup
    //   145: invokespecial 116	android/content/ContentValues:<init>	()V
    //   148: astore 15
    //   150: aload 15
    //   152: ldc 118
    //   154: aload 7
    //   156: invokevirtual 122	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   159: aload 15
    //   161: ldc 124
    //   163: invokestatic 130	java/lang/System:currentTimeMillis	()J
    //   166: invokestatic 136	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   169: invokevirtual 139	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   172: aload 15
    //   174: ldc 141
    //   176: iconst_2
    //   177: invokestatic 146	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   180: invokevirtual 149	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   183: aload 15
    //   185: ldc 151
    //   187: aload 8
    //   189: invokevirtual 96	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   192: invokevirtual 122	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   195: aload 15
    //   197: ldc 153
    //   199: iload 12
    //   201: invokestatic 146	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   204: invokevirtual 149	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   207: aload_0
    //   208: invokevirtual 159	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   211: getstatic 163	miui/provider/ExtraTelephony$FirewallLog:CONTENT_URI	Landroid/net/Uri;
    //   214: aload 15
    //   216: invokevirtual 169	android/content/ContentResolver:insert	(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
    //   219: pop
    //   220: iconst_1
    //   221: istore_2
    //   222: goto -148 -> 74
    //   225: iload 12
    //   227: sipush 260
    //   230: if_icmpne +29 -> 259
    //   233: ldc 50
    //   235: ldc 171
    //   237: invokestatic 113	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   240: pop
    //   241: goto -100 -> 141
    //   244: astore_3
    //   245: ldc 50
    //   247: ldc 173
    //   249: aload_3
    //   250: invokestatic 177	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   253: pop
    //   254: iconst_0
    //   255: istore_2
    //   256: goto -182 -> 74
    //   259: ldc 50
    //   261: ldc 179
    //   263: invokestatic 113	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   266: pop
    //   267: goto -126 -> 141
    //   270: astore 19
    //   272: goto -208 -> 64
    //   275: iload 13
    //   277: iconst_2
    //   278: if_icmpne +14 -> 292
    //   281: iconst_1
    //   282: istore 11
    //   284: sipush 260
    //   287: istore 12
    //   289: goto -182 -> 107
    //   292: iload 13
    //   294: iconst_3
    //   295: if_icmpne -188 -> 107
    //   298: iconst_1
    //   299: istore 11
    //   301: ldc 105
    //   303: istore 12
    //   305: goto -198 -> 107
    //
    // Exception table:
    //   from	to	target	type
    //   14	25	70	java/lang/NullPointerException
    //   4	8	244	java/lang/Exception
    //   14	25	244	java/lang/Exception
    //   25	41	244	java/lang/Exception
    //   48	64	244	java/lang/Exception
    //   82	241	244	java/lang/Exception
    //   259	267	244	java/lang/Exception
    //   48	64	270	java/lang/NullPointerException
  }

  public static boolean checkFirewallForWapPush(Context paramContext, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null);
    while (true)
    {
      int m;
      try
      {
        GenericPdu localGenericPdu = new PduParser(paramArrayOfByte).parse();
        if (localGenericPdu != null)
          continue;
        i = 0;
        break label248;
        EncodedStringValue localEncodedStringValue = localGenericPdu.getFrom();
        if (localEncodedStringValue != null)
          continue;
        i = 0;
        break label248;
        String str = PduPersister.toIsoString(localEncodedStringValue.getTextString());
        j = 0;
        k = 0;
        m = shouldBlockByFirewall(paramContext, str);
        if (m != 1)
          break label250;
        j = 1;
        k = 6;
        if ((j != 0) && (getSmsAct(paramContext) == 2))
        {
          if (k != 6)
            continue;
          Log.d("ExtraTelephony", "直接删除黑名单成员彩信.");
          ContentValues localContentValues = new ContentValues();
          localContentValues.put("number", str);
          localContentValues.put("date", Long.valueOf(System.currentTimeMillis()));
          localContentValues.put("type", Integer.valueOf(3));
          localContentValues.put("data1", "<" + paramContext.getString(51118445) + ">");
          localContentValues.put("reason", Integer.valueOf(k));
          paramContext.getContentResolver().insert(FirewallLog.CONTENT_URI, localContentValues);
          i = 1;
          break label248;
          if (k != 262)
            continue;
          Log.d("ExtraTelephony", "直接删除白名单成员彩信.");
          continue;
        }
      }
      catch (Exception localException)
      {
        Log.e("ExtraTelephony", "防打扰发生异常", localException);
      }
      int i = 0;
      label248: return i;
      label250: if (m != 2)
        continue;
      int j = 1;
      int k = 262;
    }
  }

  public static int getCallAct(Context paramContext)
  {
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    return Settings.System.getInt(paramContext.getContentResolver(), "firewall_call_act", 0);
  }

  public static int getSmsAct(Context paramContext)
  {
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    return Settings.System.getInt(paramContext.getContentResolver(), "firewall_sms_act", 2);
  }

  public static boolean isAntiPrivateEnabled(Context paramContext)
  {
    int i = 0;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    if (Settings.System.getInt(paramContext.getContentResolver(), "anti_private_call", 0) != 0)
      i = 1;
    return i;
  }

  public static boolean isAntiStrangerEnabled(Context paramContext)
  {
    int i = 1;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    if (Settings.System.getInt(paramContext.getContentResolver(), "anti_stranger_call", i) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  private static boolean isBlacklistEnabled(Context paramContext)
  {
    int i = 1;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    if (Settings.System.getInt(paramContext.getContentResolver(), "blacklist_enabled", i) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public static boolean isFilterSmsEnabled(Context paramContext)
  {
    int i = 1;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    if (Settings.System.getInt(paramContext.getContentResolver(), "filter_stranger_sms", i) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public static boolean isFirewallEnabled(Context paramContext)
  {
    int i = 1;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    if (Settings.System.getInt(paramContext.getContentResolver(), "firewall_enabled", i) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  private static boolean isInBlacklist(Context paramContext, String paramString)
  {
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    int i;
    if (TextUtils.isEmpty(paramString))
      i = 0;
    label75: 
    while (true)
    {
      return i;
      Cursor localCursor = paramContext.getContentResolver().query(Uri.withAppendedPath(Blacklist.CONTENT_URI, paramString), null, null, null, null);
      if ((localCursor != null) && (localCursor.moveToFirst()));
      for (i = 1; ; i = 0)
      {
        if (localCursor == null)
          break label75;
        localCursor.close();
        break;
      }
    }
  }

  private static boolean isInFirewallTimeLimit(Context paramContext)
  {
    int i = 1;
    int j = 0;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    int k;
    if (Settings.System.getInt(paramContext.getContentResolver(), "firewall_time_limit_enabled", 0) != 0)
    {
      k = i;
      if (k != 0)
        break label45;
    }
    while (true)
    {
      return i;
      k = 0;
      break;
      label45: int m = Settings.System.getInt(paramContext.getContentResolver(), "firewall_start_time", 0);
      int n = Settings.System.getInt(paramContext.getContentResolver(), "firewall_end_time", 420);
      if (m == n)
      {
        i = 0;
        continue;
      }
      Calendar localCalendar = Calendar.getInstance();
      int i1 = 60 * localCalendar.get(11) + localCalendar.get(12);
      if (m < n)
      {
        if ((m <= i1) && (i1 <= n))
          continue;
        i = 0;
        continue;
      }
      if ((i1 >= m) || (i1 <= n))
        j = i;
      i = j;
    }
  }

  private static boolean isInWhitelist(Context paramContext, String paramString)
  {
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    int i;
    if (TextUtils.isEmpty(paramString))
      i = 0;
    while (true)
    {
      return i;
      Cursor localCursor = paramContext.getContentResolver().query(Uri.withAppendedPath(Whitelist.CONTENT_URI, paramString), null, null, null, null);
      i = 0;
      if ((localCursor != null) && (localCursor.moveToFirst()))
        i = 1;
      if (localCursor == null)
        continue;
      localCursor.close();
    }
  }

  public static boolean isSkipInCallLogForFirewall(Context paramContext)
  {
    int i = 1;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    if (Settings.System.getInt(paramContext.getContentResolver(), "firewall_hide_calllog", i) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  private static boolean isStranger(Context paramContext, String paramString)
  {
    int i = 1;
    Cursor localCursor = paramContext.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, paramString), COLUMNS, null, null, null);
    if (localCursor != null)
    {
      if ((localCursor.moveToFirst()) && (!TextUtils.isEmpty(localCursor.getString(0))))
        i = 0;
      localCursor.close();
    }
    return i;
  }

  private static boolean isWhitelistEnabled(Context paramContext)
  {
    int i = 1;
    if (paramContext == null)
      throw new IllegalArgumentException("context should not be null!");
    if (Settings.System.getInt(paramContext.getContentResolver(), "whitelist_enabled", i) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public static int shouldBlockByFirewall(Context paramContext, String paramString)
  {
    return shouldBlockByFirewall(paramContext, paramString, true, true);
  }

  public static int shouldBlockByFirewall(Context paramContext, String paramString1, String paramString2)
  {
    int i = shouldBlockByFirewall(paramContext, paramString1, true, true);
    if (i == 0)
      i = shouldFilter(paramContext, paramString1, paramString2);
    return i;
  }

  public static int shouldBlockByFirewall(Context paramContext, String paramString, boolean paramBoolean)
  {
    return shouldBlockByFirewall(paramContext, paramString, paramBoolean, false);
  }

  private static int shouldBlockByFirewall(Context paramContext, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    if (!isFirewallEnabled(paramContext));
    while (true)
    {
      return i;
      if (!isInFirewallTimeLimit(paramContext))
        continue;
      if ((isBlacklistEnabled(paramContext)) && (isInBlacklist(paramContext, paramString)))
      {
        i = 1;
        continue;
      }
      if ((isWhitelistEnabled(paramContext)) && (isInWhitelist(paramContext, paramString)))
        continue;
      int j = Settings.System.getInt(paramContext.getContentResolver(), "incoming_call_limit_setting", 0);
      if (j == 3)
      {
        i = 2;
        continue;
      }
      if (j != 1)
        continue;
      if (TextUtils.isEmpty(paramString))
      {
        i = 2;
        continue;
      }
      if (paramBoolean2)
        paramBoolean1 = isStranger(paramContext, paramString);
      if (!paramBoolean1)
        continue;
      i = 2;
    }
  }

  private static int shouldFilter(Context paramContext, String paramString1, String paramString2)
  {
    int i = 0;
    if (TextUtils.isEmpty(paramString2));
    do
      return i;
    while ((!isFilterSmsEnabled(paramContext)) || (!isStranger(paramContext, paramString1)) || (isInWhitelist(paramContext, paramString1)));
    Cursor localCursor = paramContext.getContentResolver().query(Keyword.CONTENT_URI, KEYWORD_COLUMNS, null, null, null);
    int j = 0;
    if (localCursor != null)
    {
      while (localCursor.moveToNext())
      {
        String str = localCursor.getString(0).trim();
        if ((TextUtils.isEmpty(str)) || (!paramString2.contains(str)))
          continue;
        j = 1;
      }
      localCursor.close();
    }
    if (j != 0);
    for (int k = 3; ; k = 0)
    {
      i = k;
      break;
    }
  }

  public static final class FirewallLog
    implements BaseColumns
  {
    public static final int BLOCK_BY_BL = 1;
    public static final int BLOCK_BY_WL = 2;
    public static final int BLOCK_FILTER = 3;
    public static final int BLOCK_NONE = 0;
    public static final int BLOCK_PRVIATE_CALL = 4;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/firewall-log";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/firewall-log";
    public static final Uri CONTENT_URI = Uri.parse("content://firewall/log");
    public static final Uri CONTENT_URI_LOG_CONVERSATION = Uri.parse("content://firewall/logconversation");
    public static final String DATA1 = "data1";
    public static final String DATA2 = "data2";
    public static final String DATE = "date";
    public static final String NUMBER = "number";
    public static final String READ = "read";
    public static final String REASON = "reason";
    public static final int REASON_BL_DELMMS = 6;
    public static final int REASON_BL_DELSMS = 4;
    public static final int REASON_BL_HANGUP = 1;
    public static final int REASON_BL_MUTE = 2;
    public static final int REASON_BL_MUTEMMS = 5;
    public static final int REASON_BL_MUTESMS = 3;
    public static final int REASON_FILTER_SMS = 65536;
    public static final int REASON_IMPORT_SMS = 131072;
    public static final int REASON_NONE = 0;
    public static final int REASON_PRIVATE_CALL_HANGUP = 7;
    public static final int REASON_WL_DELMMS = 262;
    public static final int REASON_WL_DELSMS = 260;
    public static final int REASON_WL_FLAG = 256;
    public static final int REASON_WL_HANGUP = 257;
    public static final int REASON_WL_MUTE = 258;
    public static final int REASON_WL_MUTEMMS = 261;
    public static final int REASON_WL_MUTESMS = 259;
    public static final String TYPE = "type";
    public static final int TYPE_CALL = 1;
    public static final int TYPE_MMS = 3;
    public static final int TYPE_SMS = 2;
    public static final int TYPE_UNKNOWN;
  }

  public static final class Keyword
    implements BaseColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/firewall-keyword";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/firewall-keyword";
    public static final Uri CONTENT_URI = Uri.parse("content://firewall/keyword");
    public static final String DATA = "data";
  }

  public static final class Whitelist
    implements BaseColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/firewall-whitelist";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/firewall-whitelist";
    public static final Uri CONTENT_URI = Uri.parse("content://firewall/whitelist");
    public static final String NOTES = "notes";
  }

  public static final class Blacklist
    implements BaseColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/firewall-blacklist";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/firewall-blacklist";
    public static final Uri CONTENT_URI = Uri.parse("content://firewall/blacklist");
    public static final String NOTES = "notes";
  }

  public static final class SmsPhrase
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/smsphrase";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/smsphrase";
  }

  public static final class MmsSms
  {
    public static final Uri CONTENT_ALL_LOCKED_URI;
    public static final Uri CONTENT_EXPIRED_URI;
    public static final Uri CONTENT_PREVIEW_URI = Uri.parse("content://mms-sms/message/preview");
    public static final Uri CONTENT_RECENT_RECIPIENTS_URI;
    public static final int PREVIEW_ADDRESS_COLUMN_INDEX = 1;
    public static final int PREVIEW_BODY_COLUMN_INDEX = 4;
    public static final int PREVIEW_CHARSET_COLUMN_INDEX = 5;
    public static final int PREVIEW_DATE_COLUMN_INDEX = 2;
    public static final int PREVIEW_ID_COLUMN_INDEX = 0;
    public static final int PREVIEW_THREAD_ID_COLUMN_INDEX = 6;
    public static final int PREVIEW_TYPE_COLUMN_INDEX = 3;
    public static final int SYNC_STATE_DIRTY = 0;
    public static final int SYNC_STATE_SYNCED = 2;
    public static final int SYNC_STATE_SYNCING = 1;

    static
    {
      CONTENT_ALL_LOCKED_URI = Uri.parse("content://mms-sms/locked/all");
      CONTENT_EXPIRED_URI = Uri.parse("content://mms-sms/expired");
      CONTENT_RECENT_RECIPIENTS_URI = Uri.parse("content://mms-sms/recent-recipients");
    }
  }

  public static final class Threads
    implements ExtraTelephony.ThreadsColumns
  {
    public static final class Intents
    {
      public static final String THREADS_OBSOLETED_ACTION = "android.intent.action.SMS_THREADS_OBSOLETED_ACTION";
    }
  }

  public static abstract interface ThreadsColumns
  {
    public static final String STATE = "state";
    public static final String UNREAD_COUNT = "unread_count";
  }

  public static final class Mms
  {
    public static final String DATE_FULL = "date_full";
    public static final String DATE_MS_PART = "date_ms_part";
    public static final String DELETED = "deleted";
    public static final String MARKER = "marker";
    public static final String SOURCE = "source";
    public static final String SYNC_STATE = "sync_state";
    public static final String TIMED = "timed";
  }

  public static final class Sms
    implements ExtraTelephony.TextBasedSmsColumns
  {
    public static final String DELETED = "deleted";
    public static final String MARKER = "marker";
    public static final String SOURCE = "source";
    public static final String SYNC_STATE = "sync_state";
    public static final String TIMED = "timed";

    public static final class Intents
    {
      public static final String DISMISS_NEW_MESSAGE_NOTIFICATION_ACTION = "android.provider.Telephony.DISMISS_NEW_MESSAGE_NOTIFICATION";
    }
  }

  public static abstract interface TextBasedSmsColumns
  {
    public static final int MESSAGE_TYPE_INVALID = 7;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.provider.ExtraTelephony
 * JD-Core Version:    0.6.0
 */