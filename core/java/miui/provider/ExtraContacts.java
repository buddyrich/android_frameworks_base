package miui.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.BaseColumns;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.util.Log;
import com.android.internal.telephony.CallerInfo;

public class ExtraContacts
{
  private static final String LOG_TAG = "ExtraContacts";

  public static final class XiaomiId
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/xiaomiId";
    public static final String VALUE = "data1";
  }

  public static final class Characteristic
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/characteristic";
    public static final String VALUE = "data1";
  }

  public static final class Schools
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/schools";
    public static final String TYPE = "data2";
    public static final int TYPE_HIGHSCHOOL = 1;
    public static final int TYPE_UNIVERSITY = 2;
    public static final String VALUE = "data1";
  }

  public static final class Degree
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/degree";
    public static final String VALUE = "data1";
  }

  public static final class Hobby
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/hobby";
    public static final String VALUE = "data1";
  }

  public static final class Interest
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/interest";
    public static final int TYPE_BOY = 2;
    public static final int TYPE_FRIENDS = 3;
    public static final int TYPE_GIRL = 1;
    public static final String VALUE = "data1";
  }

  public static final class EmotionStatus
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/emotionStatus";
    public static final int TYPE_MARRIED = 3;
    public static final int TYPE_SINGLEL = 1;
    public static final int TYPE_UNMARRIED = 2;
    public static final String VALUE = "data1";
  }

  public static final class AnimalSign
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/animalSign";
    public static final int TYPE_DOG = 11;
    public static final int TYPE_DRAGON = 5;
    public static final int TYPE_GOAT = 8;
    public static final int TYPE_HORSE = 7;
    public static final int TYPE_MONKEY = 9;
    public static final int TYPE_OX = 2;
    public static final int TYPE_PIG = 12;
    public static final int TYPE_RABBIT = 4;
    public static final int TYPE_RAT = 1;
    public static final int TYPE_ROOSTER = 10;
    public static final int TYPE_SNAKE = 6;
    public static final int TYPE_TIGER = 3;
    public static final String VALUE = "data1";
  }

  public static final class Constellation
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/constellation";
    public static final int TYPE_AQUARIUS = 11;
    public static final int TYPE_ARIES = 1;
    public static final int TYPE_CANCER = 4;
    public static final int TYPE_CAPRICORN = 10;
    public static final int TYPE_GEMINI = 3;
    public static final int TYPE_LEO = 5;
    public static final int TYPE_LIBRA = 7;
    public static final int TYPE_PISCES = 12;
    public static final int TYPE_SAGITTARIUS = 9;
    public static final int TYPE_SCORPION = 8;
    public static final int TYPE_TAURUS = 2;
    public static final int TYPE_VIRGO = 6;
    public static final String VALUE = "data1";
  }

  public static final class BloodType
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/bloodType";
    public static final int TYPE_A = 1;
    public static final int TYPE_AB = 3;
    public static final int TYPE_B = 2;
    public static final int TYPE_O = 4;
    public static final String VALUE = "data1";
  }

  public static final class Gender
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.com.miui.cursor.item/gender";
    public static final int TYPE_FEMALE = 2;
    public static final int TYPE_MALE = 1;
    public static final String VALUE = "data1";
  }

  public static abstract interface Preferences
  {
    public static final String CHECK_UNSYNCHRONIZED_ACCOUNTS = "android.contacts.CHECK_UNSYNCHRONIZED_ACCOUNTS";
    public static final int FREQUENCY_NONE = 3;
    public static final int FREQUENCY_THREE_DAYS = 1;
    public static final int FREQUENCY_WEEKLY = 2;
  }

  public static abstract interface SimAccount
  {
    public static final String NAME = "SIM";
    public static final String SIM_NEW_NUMBER = "newNumber";
    public static final String SIM_NEW_TAG = "newTag";
    public static final String SIM_NUMBER = "number";
    public static final String SIM_TAG = "tag";
    public static final String TYPE = "com.android.contacts.sim";
    public static final String URI_ADN = "content://icc/adn";
    public static final String URI_ADN_CAPACITY = "content://icc/adncapacity";
    public static final String URI_FREE_ADN = "content://icc/freeadn";
  }

  public static abstract interface DefaultAccount
  {
    public static final String NAME = "default";
    public static final String TYPE = "com.android.contacts.default";
  }

  public static abstract interface Insert
  {
    public static final String SIP_ADDRESS = "sip_address";
  }

  public static abstract interface UI
  {
    public static final String GROUP_ID_EXTRA_KEY = "com.android.contacts.extra.GROUP_ID";
  }

  public static abstract interface Nickname
  {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/nickname";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, "nickname");
  }

  public static abstract interface Phone
  {
    public static final String COMPANY = "company";
    public static final String NAMES = "names";
    public static final String NICKNAME = "nickname";
    public static final String NUMBERS = "numbers";
    public static final String PHONE_NUMBER_COUNT = "number_count";
    public static final String PRIMARY_PHONE_NUMBER = "primary_number";
  }

  public static abstract interface Groups
  {
    public static final String CUSTOM_RINGTONE = "custom_ringtone";
    public static final String GROUP_ORDER = "group_order";
  }

  public static abstract interface RawContacts
  {
    public static final String SORT_KEY_CUSTOM = "sort_key_custom";
  }

  public static abstract interface Contacts
  {
    public static final String COMPANY = "company";
    public static final String CONTACT_ACCOUNT_TYPE = "contact_account_type";
    public static final Uri CONTENT_ACCOUNT_NOT_GROUP_URI;
    public static final Uri CONTENT_ACCOUNT_URI;
    public static final Uri CONTENT_GROUP_IDS_URI;
    public static final Uri CONTENT_GROUP_ID_URI;
    public static final String CONTENT_MULTIPLE_TYPE = "vnd.android.cursor.dir/contact_multiple";
    public static final String CONTENT_PICK_MULTI_TYPE = "vnd.android.cursor.dir/contact_pick_multi";
    public static final String CONTENT_PICK_SINGLE_TYPE = "vnd.android.cursor.dir/contact_pick_single";
    public static final String CONTENT_PREVIEW_CONTACT_TYPE = "vnd.android.cursor.dir/preview_contact";
    public static final Uri CONTENT_RECENT_CONTACTS_URI;
    public static final Uri CONTENT_URI = Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts");
    public static final String CUSTOM_CALL_INCOMING_PHOTO = "custom_call_incoming_photo";
    public static final String CUSTOM_RINGTONE = "custom_ringtone";
    public static final String MILIAO_STATUS = "miliao_status";
    public static final String NICKNAME = "nickname";
    public static final String PHONE_NUMBER_COUNT = "number_count";
    public static final String PRIMARY_PHONE_NUMBER = "primary_number";

    static
    {
      CONTENT_GROUP_ID_URI = Uri.withAppendedPath(CONTENT_URI, "group_id");
      CONTENT_GROUP_IDS_URI = Uri.withAppendedPath(CONTENT_URI, "group_ids");
      CONTENT_ACCOUNT_NOT_GROUP_URI = Uri.withAppendedPath(CONTENT_URI, "account_not_group");
      CONTENT_ACCOUNT_URI = Uri.withAppendedPath(CONTENT_URI, "account");
      CONTENT_RECENT_CONTACTS_URI = Uri.withAppendedPath(CONTENT_URI, "recent_contacts");
    }
  }

  public static class Calls
  {
    public static final String BACKUP_PARAM = "backup";
    public static final String CONTACT_ID = "contact_id";
    public static final Uri CONTENT_QUERY_URI = Uri.parse("content://call_log/calls_query");
    public static final Uri CONTENT_URI_WITH_BACKUP = CallLog.Calls.CONTENT_URI.buildUpon().appendQueryParameter("backup", "true").build();
    public static final String FIREWALL_TYPE = "firewalltype";
    public static final String FORWARDED_CALL = "forwarded_call";
    public static final int INCOMING_FORWARDING_CALL = 1;
    public static final int INCOMING_MUTE_TYPE = 2;
    public static final int INCOMING_NO_FIREWALL_TYPE = 0;
    public static final int INCOMING_REJECTED_TYPE = 1;
    public static final String MARK_DELETED = "mark_deleted";
    public static final String MY_NUMBER = "my_number";
    public static final int NEWCONTACT_TYPE = 10;
    public static final int NORMAL_NUMBER_TYPE = 0;
    public static final String NUMBER_TYPE = "number_type";
    public static final String SOURCE_ID = "source_id";
    public static final int SP_NUMBER_TYPE = 1;
    public static final String SYNC1 = "sync_1";
    public static final String SYNC2 = "sync_2";
    public static final String SYNC3 = "sync_3";

    public static Uri addCall(CallerInfo paramCallerInfo, Context paramContext, String paramString, int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4, int paramInt5)
    {
      Uri localUri = CallLog.Calls.addCall(paramCallerInfo, paramContext, paramString, paramInt1, paramInt2, paramLong, paramInt3);
      ContentValues localContentValues = new ContentValues(2);
      localContentValues.put("firewalltype", Integer.valueOf(paramInt4));
      localContentValues.put("forwarded_call", Integer.valueOf(paramInt5));
      if (paramContext.getContentResolver().update(localUri, localContentValues, null, null) == 1);
      while (true)
      {
        return localUri;
        Log.d("ExtraContacts", "The inserted uri is:" + localUri.toSafeString());
        if (paramContext.getContentResolver().delete(localUri, null, null) == 1)
        {
          localUri = null;
          continue;
        }
        Log.e("ExtraContacts", "The addCall transaction was not correclty commited");
        localUri = null;
      }
    }
  }

  public static abstract interface MiliaoColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.xiaomi.channel.profile";
    public static final String MILIAO_ACCOUNT = "data1";
    public static final String MILIAO_NAME = "data2";
    public static final String MILIAO_NUMBER = "data3";
  }

  public static abstract interface T9Query
  {
    public static final String[] COLUMNS;
    public static final int CONTACT_ID = 1;
    public static final int COUNT = 5;
    public static final int COUNTRY_ISO = 16;
    public static final int DATA_ID = 20;
    public static final int DATE = 8;
    public static final int DURATION = 9;
    public static final int FIREWALL_TYPE = 13;
    public static final int FORWARDED_CALL = 14;
    public static final int MISSED_COUNT = 10;
    public static final int NAME = 2;
    public static final int NEW = 6;
    public static final int NORMALIZED_NUMBER = 18;
    public static final int NUMBER = 3;
    public static final int NUMBER_TYPE = 19;
    public static final int PHOTO_ID = 4;
    public static final int T9DISPLAY_STRING = 15;
    public static final int T9KEY_TYPE = 11;
    public static final int T9MATCH_DETAIL = 12;
    public static final int TYPE = 7;
    public static final int VOICEMAIL_URI = 17;
    public static final int _ID;

    static
    {
      String[] arrayOfString = new String[21];
      arrayOfString[0] = "_id";
      arrayOfString[1] = "contact_id";
      arrayOfString[2] = "name";
      arrayOfString[3] = "number";
      arrayOfString[4] = "photo_id";
      arrayOfString[5] = "call_count";
      arrayOfString[6] = "is_new";
      arrayOfString[7] = "call_type";
      arrayOfString[8] = "call_date";
      arrayOfString[9] = "call_duration";
      arrayOfString[10] = "missed_count";
      arrayOfString[11] = "key_type";
      arrayOfString[12] = "match_detail";
      arrayOfString[13] = "firewall_type";
      arrayOfString[14] = "forwarded_call";
      arrayOfString[15] = "display_string";
      arrayOfString[16] = "country_iso";
      arrayOfString[17] = "voicemail_uri";
      arrayOfString[18] = "normalized_number";
      arrayOfString[19] = "number_type";
      arrayOfString[20] = "data_id";
      COLUMNS = arrayOfString;
    }
  }

  public static final class T9MatchLevel
  {
    public static final int FULLNAME = 3;
    public static final int NUMBER = 0;
    public static final int PARTIAL = 1;
    public static final int PINYIN = 2;
  }

  public static final class T9LookupType
  {
    public static final int NAME = 2;
    public static final int NUMBER = 0;
    public static final int PINYIN = 1;
  }

  public static abstract interface T9LookupColumns
  {
    public static final String CONTACT_ID = "contact_id";
    public static final String DATA = "data";
    public static final String DATA_ID = "data_id";
    public static final String DISPLAY_NAME = "display_name";
    public static final String DISPLAY_STRING = "display_string";
    public static final String KEY_TYPE = "key_type";
    public static final String MATCH_DETAIL = "match_detail";
    public static final String MATCH_LEVEL = "match_level";
    public static final String NORMALIZED_DATA = "normalized_data";
    public static final String PHOTO_ID = "photo_id";
    public static final String RAW_CONTACT_ID = "raw_contact_id";
    public static final String T9_KEY = "t9_key";
    public static final String TIMES_CONTACTED = "times_contacted";
  }

  public static final class SmartDialer
    implements BaseColumns
  {
    public static final String CALL_COUNT = "call_count";
    public static final String CALL_DATE = "call_date";
    public static final String CALL_DURATION = "call_duration";
    public static final String CALL_TYPE = "call_type";
    public static final String CONTACT_ID = "contact_id";
    public static final Uri CONTENT_REBUILDT9_URI;
    public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "search_t9");
    public static final String COUNTRY_ISO = "country_iso";
    public static final int CREATE_CONTACT_TAG = -7;
    public static final int CREATE_OR_EDIT_CONTACT_TAG = -6;
    public static final String DATA_ID = "data_id";
    public static final String FIREWALL_TYPE = "firewall_type";
    public static final String FORWARDED_CALL = "forwarded_call";
    public static final int INFORMATION_ONLY_TAG = -8;
    public static final String IS_NEW = "is_new";
    public static final String MISSED_COUNT = "missed_count";
    public static final String NAME = "name";
    public static final String NORMALIZED_NUMBER = "normalized_number";
    public static final String NUMBER = "number";
    public static final String NUMBER_TYPE = "number_type";
    public static final String PHOTO_ID = "photo_id";
    public static final int SEND_SMS_TAG = -9;
    public static final int SP_CONTACT_START_ID = -100;
    public static final String UPDATE_CALL_LOG_CACHED_CONTACT_TAG = "update_all_log_cached_contact_tag";
    public static final String VOICEMAIL_URI = "voicemail_uri";

    static
    {
      CONTENT_REBUILDT9_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "rebuild_t9_index");
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.provider.ExtraContacts
 * JD-Core Version:    0.6.0
 */