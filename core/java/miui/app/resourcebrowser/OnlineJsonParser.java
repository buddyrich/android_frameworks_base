package miui.app.resourcebrowser;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlineJsonParser
  implements IntentConstants
{
  protected static OnlineJsonParser sDefaultParser = new OnlineJsonParser();

  public static OnlineJsonParser getDefault()
  {
    return sDefaultParser;
  }

  protected Bundle buildResourceInformation(JSONObject paramJSONObject, String paramString, Bundle paramBundle)
    throws JSONException
  {
    Bundle localBundle = new Bundle();
    Iterator localIterator = paramJSONObject.keys();
    while (localIterator.hasNext())
    {
      String str4 = (String)localIterator.next();
      try
      {
        localBundle.putSerializable(str4, (Serializable)paramJSONObject.get(str4));
      }
      catch (ClassCastException localClassCastException)
      {
      }
    }
    localBundle.putString("fileHost", paramString);
    localBundle.putLong("m_lastupdate", 1000L * localBundle.getInt("m_lastupdate"));
    String str1 = paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE");
    int i = paramBundle.getInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY");
    localBundle.putString("online_path", getDownloadURL(localBundle, i));
    localBundle.putString("local_path", OnlineHelper.getFilePathByResource(paramBundle.getString("com.miui.android.resourcebrowser.DOWNLOAD_FOLDER"), localBundle));
    ArrayList localArrayList1 = getPreviewURLs(localBundle, i, paramBundle);
    localBundle.putStringArrayList("online_preview", localArrayList1);
    int j = localArrayList1.size();
    ArrayList localArrayList2 = new ArrayList(j);
    String str2 = ResourceConstants.PREVIEW_PATH + str1;
    for (int k = 0; k < j; k++)
      localArrayList2.add(OnlineHelper.getFilePathByURL(str2, (String)localArrayList1.get(k)));
    localBundle.putStringArrayList("local_preview", localArrayList2);
    ArrayList localArrayList3 = getThumbnailURLs(localBundle, i, paramBundle);
    localBundle.putStringArrayList("online_thumbnail", localArrayList3);
    int m = localArrayList3.size();
    ArrayList localArrayList4 = new ArrayList(m);
    String str3 = ResourceConstants.THUMBNAIL_PATH + str1;
    for (int n = 0; n < m; n++)
      localArrayList4.add(OnlineHelper.getFilePathByURL(str3, (String)localArrayList3.get(n)));
    localBundle.putStringArrayList("local_thumbnail", localArrayList4);
    return localBundle;
  }

  protected String getDownloadURL(Bundle paramBundle, int paramInt)
  {
    String str1 = paramBundle.getString("fileHost");
    String str2 = "";
    String str3;
    if (paramInt == 1)
    {
      String str5 = paramBundle.getString("extension");
      if (Build.DEVICE.equals("umts_sholes"))
        str2 = "_960_854";
      str3 = paramBundle.getString("url") + str2 + "." + str5;
    }
    while (true)
    {
      return str1 + str3;
      if (paramInt == 2)
      {
        String str4 = paramBundle.getString("extension");
        str3 = paramBundle.getString("url") + "." + str4;
        continue;
      }
      str3 = paramBundle.getString("url");
    }
  }

  protected ArrayList<String> getImageURLs(Bundle paramBundle1, int paramInt, Bundle paramBundle2, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    String str1 = paramBundle1.getString("fileHost");
    String str2 = paramBundle1.getString("extension");
    String str4;
    if (paramInt == 1)
      if (paramBoolean)
      {
        str4 = "_640_480";
        String str5 = paramBundle1.getString("url") + str4 + "." + str2;
        localArrayList.add(str1 + str5);
      }
    while (true)
    {
      return localArrayList;
      str4 = "_140_105";
      break;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = str1;
      arrayOfObject[1] = paramBundle1.getString("preview");
      arrayOfObject[2] = paramBundle1.getString("m_id");
      String str3 = String.format("%s%s/%s_", arrayOfObject);
      if (paramBoolean)
      {
        localArrayList.addAll(getImageURLs(paramBundle1, str3, "com.miui.android.resourcebrowser.PREVIEW_PREFIX", "com.miui.android.resourcebrowser.PREVIEW_PREFIX_INDICATOR", "big_", str2, paramBundle2));
        continue;
      }
      localArrayList.addAll(getImageURLs(paramBundle1, str3, "com.miui.android.resourcebrowser.THUMBNAIL_PREFIX", "com.miui.android.resourcebrowser.THUMBNAIL_PREFIX_INDICATOR", "small_", str2, paramBundle2));
    }
  }

  protected ArrayList<String> getImageURLs(Bundle paramBundle1, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Bundle paramBundle2)
  {
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString1 = paramBundle2.getStringArray(paramString2);
    String[] arrayOfString2 = paramBundle2.getStringArray(paramString3);
    for (int i = 0; i < arrayOfString1.length; i++)
    {
      if (TextUtils.isEmpty(arrayOfString1[i]))
        continue;
      String str1 = arrayOfString1[i] + paramString4;
      int j = 1;
      String str3;
      if (arrayOfString2 != null)
      {
        str3 = paramBundle1.getString(arrayOfString2[i]);
        if (str3 == null);
      }
      try
      {
        int m = Integer.parseInt(str3);
        j = m;
        if ("clock".equals(paramBundle2.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE")))
        {
          str2 = "png";
          for (int k = 0; k < j; k++)
            localArrayList.add(paramString1 + str1 + k + "." + str2);
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        while (true)
        {
          j = 0;
          continue;
          String str2 = "jpg";
        }
      }
    }
    return localArrayList;
  }

  protected ArrayList<String> getPreviewURLs(Bundle paramBundle1, int paramInt, Bundle paramBundle2)
  {
    return getImageURLs(paramBundle1, paramInt, paramBundle2, true);
  }

  protected ArrayList<String> getThumbnailURLs(Bundle paramBundle1, int paramInt, Bundle paramBundle2)
  {
    return getImageURLs(paramBundle1, paramInt, paramBundle2, false);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.OnlineJsonParser
 * JD-Core Version:    0.6.0
 */