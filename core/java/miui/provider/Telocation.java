package miui.provider;

import android.net.Uri;

public class Telocation
{
  public static final String ACTION_ADD_CUSTOM_LOCATION = "action_add_custom_location";
  public static final String ACTION_EDIT_CUSTOM_LOCATION = "action_edit_custom_location";
  public static final int AREACODE_COLUMN_INDEX = 2;
  public static final String AUTHORITY = "telocation";
  public static final String CONTENT_CUSTOM_LOCATION_TYPE = "vnd.android.cursor.dir/custom_telocations";
  public static final Uri CONTENT_CUSTOM_LOCATION_URI = Uri.parse("content://telocation/customlocations");
  public static final String CONTENT_MOBILE_ITEM_TYPE = "vnd.android.cursor.item/telocation.mobile";
  public static final String CONTENT_TEL_ITEM_TYPE = "vnd.android.cursor.item/telocation.tel";
  public static final String CONTENT_TEL_TYPE = "vnd.android.cursor.dir/telocation.tel";
  public static final int CUSTOM_ID_COLUMN_INDEX = 0;
  public static final String CUSTOM_LOCATION_COLUMN = "location";
  public static final int CUSTOM_LOCATION_COLUMN_INDEX = 2;
  public static final String CUSTOM_NUMBER_COLUMN = "number";
  public static final int CUSTOM_NUMBER_COLUMN_INDEX = 1;
  public static final String CUSTOM_TYPE_COLUMN = "type";
  public static final int CUSTOM_TYPE_COLUMN_INDEX = 3;
  public static final int ID_COLUMN_INDEX = 0;
  public static final String LOCATION_COLUMN = "location";
  public static final int LOCATION_COLUMN_INDEX = 1;
  public static final String LOCATION_MATCH = "customlocations";
  public static final String MOBILE_CONTENT_URI_PREFIX = "content://telocation/mobile/";
  public static final String MOBILE_ITEM_MATCH = "mobile/#";
  public static final String TEL_CONTENT_URI = "content://telocation/tel";
  public static final String TEL_CONTENT_URI_PREFIX = "content://telocation/tel/";
  public static final String TEL_ITEM_MATCH = "tel/#";
  public static final String TEL_MATCH = "tel";
  public static final int TYPE_JITUAN = 2;
  public static final int TYPE_MOB = 1;
  public static final int TYPE_TEL;
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.provider.Telocation
 * JD-Core Version:    0.6.0
 */