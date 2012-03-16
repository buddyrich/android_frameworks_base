package miui.telephony;

import android.content.res.Resources;

public class Connection
{
  public static int PRESENTATION_ALLOWED = 1;
  public static int PRESENTATION_PAYPHONE;
  public static int PRESENTATION_RESTRICTED = 2;
  public static int PRESENTATION_UNKNOWN = 3;

  static
  {
    PRESENTATION_PAYPHONE = 4;
  }

  public static String getPresentationString(int paramInt)
  {
    String str;
    if (paramInt == PRESENTATION_RESTRICTED)
      str = Resources.getSystem().getString(51118141);
    while (true)
    {
      return str;
      if (paramInt == PRESENTATION_PAYPHONE)
      {
        str = Resources.getSystem().getString(51118142);
        continue;
      }
      str = Resources.getSystem().getString(51118140);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.telephony.Connection
 * JD-Core Version:    0.6.0
 */