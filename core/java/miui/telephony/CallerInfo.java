package miui.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import com.android.internal.telephony.Connection;
import java.io.ByteArrayInputStream;
import java.util.HashSet;

public class CallerInfo
{
  public static final String PAYPHONE_NUMBER = "-3";
  public static final String PRIVATE_NUMBER = "-2";
  private static final int SP_NAME_COLUMN = 0;
  private static final int SP_PHOTO_COLUMN = 1;
  private static final String[] SP_PROJECTION;
  public static final String UNKNOWN_NUMBER = "-1";

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "name";
    arrayOfString[1] = "photo";
    SP_PROJECTION = arrayOfString;
  }

  public static com.android.internal.telephony.CallerInfo doSpNumberQuery(Context paramContext, String paramString, com.android.internal.telephony.CallerInfo paramCallerInfo)
  {
    Cursor localCursor;
    if ((!paramCallerInfo.contactExists) && (!paramCallerInfo.extra.isSpNumber))
    {
      PhoneNumberUtils.PhoneNumber localPhoneNumber = PhoneNumberUtils.PhoneNumber.parse(paramString);
      String str = paramString;
      if ((localPhoneNumber != null) && (!TextUtils.isEmpty(localPhoneNumber.getEffectiveNumber())))
        str = localPhoneNumber.getEffectiveNumber();
      ContentResolver localContentResolver = paramContext.getContentResolver();
      Uri localUri = Uri.parse("content://yellowpage/sp");
      String[] arrayOfString1 = SP_PROJECTION;
      String[] arrayOfString2 = new String[1];
      arrayOfString2[0] = str;
      localCursor = localContentResolver.query(localUri, arrayOfString1, "addr=?", arrayOfString2, null);
      if (localCursor == null);
    }
    try
    {
      if (localCursor.moveToFirst())
      {
        paramCallerInfo.extra.isSpNumber = true;
        paramCallerInfo.extra.displayName = localCursor.getString(0);
        byte[] arrayOfByte = localCursor.getBlob(1);
        if (arrayOfByte != null)
        {
          ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
          if (localByteArrayInputStream != null)
          {
            paramCallerInfo.cachedPhoto = Drawable.createFromStream(localByteArrayInputStream, paramString);
            paramCallerInfo.isCachedPhotoCurrent = true;
          }
        }
      }
      return paramCallerInfo;
    }
    finally
    {
      localCursor.close();
    }
    throw localObject;
  }

  public static int getColumnIndex(Uri paramUri, String paramString, Cursor paramCursor)
  {
    String str1 = paramUri.toString();
    String str2 = paramString;
    if ((str1.startsWith("content://com.android.contacts/data/phones")) || (str1.startsWith("content://com.android.contacts/data")))
    {
      if ("number".equals(paramString))
        str2 = "data1";
    }
    else
      if (str2 == null)
        break label105;
    label105: for (int i = paramCursor.getColumnIndex(str2); ; i = -1)
    {
      return i;
      if ("type".equals(paramString))
      {
        str2 = "data2";
        break;
      }
      if ("label".equals(paramString))
      {
        str2 = "data3";
        break;
      }
      if (!"normalized_number".equals(paramString))
        break;
      str2 = "data4";
      break;
    }
  }

  public static int getPresentation(String paramString)
  {
    int i;
    if ((TextUtils.isEmpty(paramString)) || (paramString.equals("-1")))
      i = Connection.PRESENTATION_UNKNOWN;
    while (true)
    {
      return i;
      if (paramString.equals("-2"))
      {
        i = Connection.PRESENTATION_RESTRICTED;
        continue;
      }
      if (paramString.equals("-3"))
      {
        i = Connection.PRESENTATION_PAYPHONE;
        continue;
      }
      i = Connection.PRESENTATION_ALLOWED;
    }
  }

  public static void updateDisplayName(Context paramContext, com.android.internal.telephony.CallerInfo paramCallerInfo, Cursor paramCursor)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramCallerInfo.name);
    int i;
    HashSet localHashSet;
    String str1;
    if (paramCursor.getCount() > 1)
    {
      i = paramCursor.getColumnIndex("display_name");
      if (i != -1)
      {
        localHashSet = new HashSet();
        localHashSet.add(paramCallerInfo.name);
        str1 = paramContext.getString(51118444);
      }
    }
    while (true)
    {
      String str2;
      if (paramCursor.moveToNext())
      {
        str2 = paramCursor.getString(i);
        if (localHashSet.contains(str2))
          continue;
        if (localHashSet.size() >= 3)
          localStringBuilder.append(' ').append(str1).append(" ...");
      }
      else
      {
        paramCallerInfo.extra.displayName = localStringBuilder.toString();
        return;
      }
      localStringBuilder.append(' ').append(str1).append(' ').append(str2);
      localHashSet.add(str2);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.telephony.CallerInfo
 * JD-Core Version:    0.6.0
 */