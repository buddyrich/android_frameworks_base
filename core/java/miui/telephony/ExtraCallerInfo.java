package miui.telephony;

import android.content.Context;
import android.database.Cursor;

public class ExtraCallerInfo
{
  public String company;
  public String displayName;
  public boolean isSpNumber;

  public static ExtraCallerInfo getExtraCallerInfo(Context paramContext, com.android.internal.telephony.CallerInfo paramCallerInfo, Cursor paramCursor)
  {
    ExtraCallerInfo localExtraCallerInfo = paramCallerInfo.extra;
    localExtraCallerInfo.isSpNumber = false;
    localExtraCallerInfo.company = null;
    int i = paramCursor.getColumnIndex("company");
    if (i != -1)
      localExtraCallerInfo.company = paramCursor.getString(i);
    CallerInfo.updateDisplayName(paramContext, paramCallerInfo, paramCursor);
    return localExtraCallerInfo;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.telephony.ExtraCallerInfo
 * JD-Core Version:    0.6.0
 */