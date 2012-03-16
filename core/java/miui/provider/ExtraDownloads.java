package miui.provider;

import android.provider.BaseColumns;

public class ExtraDownloads
{
  public static final class Impl
    implements BaseColumns
  {
    public static final String COLUMN_APPOINT_NAME = "appointname";
    public static final String COLUMN_IF_RANGE_ID = "if_range_id";
    public static final String COLUMN_SUB_DIRECTORY = "subdirectory";
    public static final int CONTROL_PAUSED_WITHOUT_WIFI = 2;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.provider.ExtraDownloads
 * JD-Core Version:    0.6.0
 */