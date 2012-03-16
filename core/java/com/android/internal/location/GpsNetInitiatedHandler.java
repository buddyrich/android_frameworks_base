package com.android.internal.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.app.NetInitiatedActivity;
import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;

public class GpsNetInitiatedHandler
{
  public static final String ACTION_NI_VERIFY = "android.intent.action.NETWORK_INITIATED_VERIFY";
  private static final boolean DEBUG = true;
  public static final int GPS_ENC_NONE = 0;
  public static final int GPS_ENC_SUPL_GSM_DEFAULT = 1;
  public static final int GPS_ENC_SUPL_UCS2 = 3;
  public static final int GPS_ENC_SUPL_UTF8 = 2;
  public static final int GPS_ENC_UNKNOWN = -1;
  public static final int GPS_NI_NEED_NOTIFY = 1;
  public static final int GPS_NI_NEED_VERIFY = 2;
  public static final int GPS_NI_PRIVACY_OVERRIDE = 4;
  public static final int GPS_NI_RESPONSE_ACCEPT = 1;
  public static final int GPS_NI_RESPONSE_DENY = 2;
  public static final int GPS_NI_RESPONSE_NORESP = 3;
  public static final int GPS_NI_TYPE_UMTS_CTRL_PLANE = 3;
  public static final int GPS_NI_TYPE_UMTS_SUPL = 2;
  public static final int GPS_NI_TYPE_VOICE = 1;
  public static final String NI_EXTRA_CMD_NOTIF_ID = "notif_id";
  public static final String NI_EXTRA_CMD_RESPONSE = "response";
  public static final String NI_INTENT_KEY_DEFAULT_RESPONSE = "default_resp";
  public static final String NI_INTENT_KEY_MESSAGE = "message";
  public static final String NI_INTENT_KEY_NOTIF_ID = "notif_id";
  public static final String NI_INTENT_KEY_TIMEOUT = "timeout";
  public static final String NI_INTENT_KEY_TITLE = "title";
  public static final String NI_RESPONSE_EXTRA_CMD = "send_ni_response";
  private static final String TAG = "GpsNetInitiatedHandler";
  private static final boolean VERBOSE;
  private static boolean mIsHexInput = true;
  private final Context mContext;
  private final LocationManager mLocationManager;
  private Notification mNiNotification;
  private boolean mPlaySounds = false;
  private boolean mPopupImmediately = true;
  private boolean visible = true;

  public GpsNetInitiatedHandler(Context paramContext)
  {
    this.mContext = paramContext;
    this.mLocationManager = ((LocationManager)paramContext.getSystemService("location"));
  }

  static String decodeGSMPackedString(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    int j = i * 8 / 7;
    if ((i % 7 == 0) && (i > 0) && (paramArrayOfByte[(i - 1)] >> 1 == 0))
      j--;
    String str = GsmAlphabet.gsm7BitPackedToString(paramArrayOfByte, 0, j);
    if (str == null)
    {
      Log.e("GpsNetInitiatedHandler", "Decoding of GSM packed string failed");
      str = "";
    }
    return str;
  }

  private static String decodeString(String paramString, boolean paramBoolean, int paramInt)
  {
    String str = paramString;
    byte[] arrayOfByte = stringToByteArray(paramString, paramBoolean);
    switch (paramInt)
    {
    default:
      Log.e("GpsNetInitiatedHandler", "Unknown encoding " + paramInt + " for NI text " + paramString);
    case 0:
    case 1:
    case 2:
    case 3:
    case -1:
    }
    while (true)
    {
      return str;
      str = paramString;
      continue;
      str = decodeGSMPackedString(arrayOfByte);
      continue;
      str = decodeUTF8String(arrayOfByte);
      continue;
      str = decodeUCS2String(arrayOfByte);
      continue;
      str = paramString;
    }
  }

  static String decodeUCS2String(byte[] paramArrayOfByte)
  {
    try
    {
      String str = new String(paramArrayOfByte, "UTF-16");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new AssertionError();
  }

  static String decodeUTF8String(byte[] paramArrayOfByte)
  {
    try
    {
      String str = new String(paramArrayOfByte, "UTF-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new AssertionError();
  }

  private static String getDialogMessage(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return getNotifMessage(paramGpsNiNotification, paramContext);
  }

  public static String getDialogTitle(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return getNotifTitle(paramGpsNiNotification, paramContext);
  }

  private Intent getDlgIntent(GpsNiNotification paramGpsNiNotification)
  {
    Intent localIntent = new Intent();
    String str1 = getDialogTitle(paramGpsNiNotification, this.mContext);
    String str2 = getDialogMessage(paramGpsNiNotification, this.mContext);
    localIntent.setFlags(268435456);
    localIntent.setClass(this.mContext, NetInitiatedActivity.class);
    localIntent.putExtra("notif_id", paramGpsNiNotification.notificationId);
    localIntent.putExtra("title", str1);
    localIntent.putExtra("message", str2);
    localIntent.putExtra("timeout", paramGpsNiNotification.timeout);
    localIntent.putExtra("default_resp", paramGpsNiNotification.defaultResponse);
    Log.d("GpsNetInitiatedHandler", "generateIntent, title: " + str1 + ", message: " + str2 + ", timeout: " + paramGpsNiNotification.timeout);
    return localIntent;
  }

  private static String getNotifMessage(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    String str = paramContext.getString(17040486);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = decodeString(paramGpsNiNotification.requestorId, mIsHexInput, paramGpsNiNotification.requestorIdEncoding);
    arrayOfObject[1] = decodeString(paramGpsNiNotification.text, mIsHexInput, paramGpsNiNotification.textEncoding);
    return String.format(str, arrayOfObject);
  }

  private static String getNotifTicker(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    String str = paramContext.getString(17040484);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = decodeString(paramGpsNiNotification.requestorId, mIsHexInput, paramGpsNiNotification.requestorIdEncoding);
    arrayOfObject[1] = decodeString(paramGpsNiNotification.text, mIsHexInput, paramGpsNiNotification.textEncoding);
    return String.format(str, arrayOfObject);
  }

  private static String getNotifTitle(GpsNiNotification paramGpsNiNotification, Context paramContext)
  {
    return String.format(paramContext.getString(17040485), new Object[0]);
  }

  private void openNiDialog(GpsNiNotification paramGpsNiNotification)
  {
    Intent localIntent = getDlgIntent(paramGpsNiNotification);
    Log.d("GpsNetInitiatedHandler", "openNiDialog, notifyId: " + paramGpsNiNotification.notificationId + ", requestorId: " + paramGpsNiNotification.requestorId + ", text: " + paramGpsNiNotification.text);
    this.mContext.startActivity(localIntent);
  }

  /** @deprecated */
  private void setNiNotification(GpsNiNotification paramGpsNiNotification)
  {
    monitorenter;
    while (true)
    {
      NotificationManager localNotificationManager;
      try
      {
        localNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
        if (localNotificationManager == null)
          return;
        String str1 = getNotifTitle(paramGpsNiNotification, this.mContext);
        String str2 = getNotifMessage(paramGpsNiNotification, this.mContext);
        Log.d("GpsNetInitiatedHandler", "setNiNotification, notifyId: " + paramGpsNiNotification.notificationId + ", title: " + str1 + ", message: " + str2);
        if (this.mNiNotification != null)
          continue;
        this.mNiNotification = new Notification();
        this.mNiNotification.icon = 17302815;
        this.mNiNotification.when = 0L;
        if (this.mPlaySounds)
        {
          Notification localNotification2 = this.mNiNotification;
          localNotification2.defaults = (0x1 | localNotification2.defaults);
          this.mNiNotification.flags = 18;
          this.mNiNotification.tickerText = getNotifTicker(paramGpsNiNotification, this.mContext);
          if (this.mPopupImmediately)
            break label271;
          localIntent = getDlgIntent(paramGpsNiNotification);
          PendingIntent localPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, localIntent, 0);
          this.mNiNotification.setLatestEventInfo(this.mContext, str1, str2, localPendingIntent);
          if (!this.visible)
            break label283;
          localNotificationManager.notify(paramGpsNiNotification.notificationId, this.mNiNotification);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      Notification localNotification1 = this.mNiNotification;
      localNotification1.defaults = (0xFFFFFFFE & localNotification1.defaults);
      continue;
      label271: Intent localIntent = new Intent();
      continue;
      label283: localNotificationManager.cancel(paramGpsNiNotification.notificationId);
    }
  }

  static byte[] stringToByteArray(String paramString, boolean paramBoolean)
  {
    int i;
    if (paramBoolean)
      i = paramString.length() / 2;
    byte[] arrayOfByte;
    while (true)
    {
      arrayOfByte = new byte[i];
      if (paramBoolean)
      {
        for (int k = 0; k < i; k++)
          arrayOfByte[k] = (byte)Integer.parseInt(paramString.substring(k * 2, 2 + k * 2), 16);
        i = paramString.length();
        continue;
      }
      for (int j = 0; j < i; j++)
        arrayOfByte[j] = (byte)paramString.charAt(j);
    }
    return arrayOfByte;
  }

  public void handleNiNotification(GpsNiNotification paramGpsNiNotification)
  {
    Log.d("GpsNetInitiatedHandler", "handleNiNotification notificationId: " + paramGpsNiNotification.notificationId + " requestorId: " + paramGpsNiNotification.requestorId + " text: " + paramGpsNiNotification.text);
    if ((paramGpsNiNotification.needNotify) && (paramGpsNiNotification.needVerify) && (this.mPopupImmediately))
      openNiDialog(paramGpsNiNotification);
    if (((paramGpsNiNotification.needNotify) && (!paramGpsNiNotification.needVerify)) || ((paramGpsNiNotification.needNotify) && (paramGpsNiNotification.needVerify) && (!this.mPopupImmediately)))
      setNiNotification(paramGpsNiNotification);
    if (((paramGpsNiNotification.needNotify) && (!paramGpsNiNotification.needVerify)) || ((!paramGpsNiNotification.needNotify) && (!paramGpsNiNotification.needVerify)) || (paramGpsNiNotification.privacyOverride))
      this.mLocationManager.sendNiResponse(paramGpsNiNotification.notificationId, 1);
  }

  public static class GpsNiResponse
  {
    Bundle extras;
    int userResponse;
  }

  public static class GpsNiNotification
  {
    public int defaultResponse;
    public Bundle extras;
    public boolean needNotify;
    public boolean needVerify;
    public int niType;
    public int notificationId;
    public boolean privacyOverride;
    public String requestorId;
    public int requestorIdEncoding;
    public String text;
    public int textEncoding;
    public int timeout;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     com.android.internal.location.GpsNetInitiatedHandler
 * JD-Core Version:    0.6.0
 */