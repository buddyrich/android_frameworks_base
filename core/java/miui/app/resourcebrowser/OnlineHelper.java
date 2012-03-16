package miui.app.resourcebrowser;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miui.os.ExtraFileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlineHelper
  implements IntentConstants
{
  private static final int BUFFER_SIZE = 1024;
  private static final int CONNECTION_TIMEOUT = 20000;
  public static final int MIN_VERSION = 1;
  private static final int READ_TIMEOUT = 20000;
  private static Map<String, String> mCacheFileNameMap = new HashMap();

  public static String appendVersionInfo(Context paramContext, String paramString)
  {
    try
    {
      String str1 = getMIUIAccountToken(paramContext);
      StringBuilder localStringBuilder1 = new StringBuilder();
      Object[] arrayOfObject = new Object[6];
      arrayOfObject[0] = paramString;
      arrayOfObject[1] = getUserId(paramContext);
      arrayOfObject[2] = getModVersion();
      arrayOfObject[3] = getBoardString();
      arrayOfObject[4] = getDeviceString();
      arrayOfObject[5] = getShortIMEI();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(String.format("%s&g=%s&v=%s&b=%s&d=%s&i=%s", arrayOfObject));
      if (str1 != null);
      for (String str2 = "&t=" + str1; ; str2 = "")
      {
        paramString = str2;
        break;
      }
    }
    catch (Exception localException)
    {
    }
    return paramString;
  }

  public static List<ResourceCategory> buildCategories(JSONObject paramJSONObject, Bundle paramBundle)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = null;
    try
    {
      JSONArray localJSONArray = paramJSONObject.getJSONArray("order");
      localObject = localJSONArray;
      JSONObject localJSONObject;
      try
      {
        localJSONObject = paramJSONObject.getJSONObject("data");
        if (localObject != null)
          break label186;
        Iterator localIterator = localJSONObject.keys();
        while (localIterator.hasNext())
        {
          ResourceCategory localResourceCategory2 = new ResourceCategory();
          String str2 = (String)localIterator.next();
          localResourceCategory2.setCode(str2);
          localResourceCategory2.setName(localJSONObject.getString(str2));
          localArrayList.add(localResourceCategory2);
        }
      }
      catch (JSONException localJSONException2)
      {
        localJSONException2.printStackTrace();
      }
      while (true)
      {
        return localArrayList;
        Collections.sort(localArrayList);
        continue;
        while (i < localObject.length())
        {
          ResourceCategory localResourceCategory1 = new ResourceCategory();
          String str1 = localObject.get(i).toString();
          localResourceCategory1.setCode(str1);
          localResourceCategory1.setName(localJSONObject.getString(str1));
          localArrayList.add(localResourceCategory1);
          i++;
        }
      }
    }
    catch (JSONException localJSONException1)
    {
      while (true)
      {
        continue;
        label186: int i = 0;
      }
    }
  }

  public static List<Resource> buildResources(JSONObject paramJSONObject, Bundle paramBundle)
    throws JSONException
  {
    ArrayList localArrayList = new ArrayList();
    JSONArray localJSONArray = paramJSONObject.getJSONArray("data");
    String str = paramJSONObject.getString("fileHost");
    if (str.endsWith("/"))
      str = str.substring(0, -1 + str.length());
    for (int i = 0; i < localJSONArray.length(); i++)
    {
      JSONObject localJSONObject = localJSONArray.getJSONObject(i);
      Resource localResource = new Resource();
      localResource.setInformation(OnlineJsonParser.getDefault().buildResourceInformation(localJSONObject, str, paramBundle));
      localResource.setStatus(ResourceHelper.getResourceStatus(localResource, paramBundle));
      localArrayList.add(localResource);
    }
    return localArrayList;
  }

  public static Map<String, Resource> buildSpecifiedResources(JSONObject paramJSONObject, Bundle paramBundle)
    throws JSONException
  {
    HashMap localHashMap = new HashMap();
    JSONObject localJSONObject1 = paramJSONObject.getJSONObject("data");
    String str1 = paramJSONObject.getString("fileHost");
    if (str1.endsWith("/"))
      str1 = str1.substring(0, -1 + str1.length());
    Iterator localIterator = localJSONObject1.keys();
    while (localIterator.hasNext())
    {
      String str2 = (String)localIterator.next();
      JSONObject localJSONObject2 = localJSONObject1.getJSONObject(str2);
      Resource localResource = new Resource();
      localResource.setInformation(OnlineJsonParser.getDefault().buildResourceInformation(localJSONObject2, str1, paramBundle));
      localHashMap.put(str2, localResource);
    }
    return localHashMap;
  }

  private static String getBoardString()
  {
    return SystemProperties.get("ro.product.board");
  }

  private static String getDeviceString()
  {
    return Build.DEVICE;
  }

  public static String getEncryptedURL(String paramString)
  {
    Object localObject = paramString;
    try
    {
      String str1 = Base64.encodeToString(paramString.getBytes(), 2);
      String str2 = str1 + "05wZlFTY";
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(str2.getBytes());
      String str3 = new BigInteger(1, localMessageDigest.digest()).toString(16);
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = str3;
      String str4 = String.format("%32s", arrayOfObject1).replace(' ', '0');
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = paramString;
      arrayOfObject2[1] = str4;
      String str5 = String.format("http://res.api.miui.com/api?%s&key=%s", arrayOfObject2);
      localObject = str5;
      return localObject;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      while (true)
        localNoSuchAlgorithmException.printStackTrace();
    }
  }

  public static String getFilePathByResource(String paramString, Bundle paramBundle)
  {
    String str1 = paramBundle.getString("m_title");
    String str2 = paramBundle.getString("x_id");
    int i = 1;
    try
    {
      int j = Integer.parseInt(paramBundle.getString("version"));
      i = j;
      label35: String str3 = paramBundle.getString("online_path");
      String str4 = str3.substring(1 + str3.lastIndexOf("."));
      if (str4.contains("/"))
      {
        Object[] arrayOfObject2 = new Object[3];
        arrayOfObject2[0] = str3;
        arrayOfObject2[1] = str1;
        arrayOfObject2[2] = str2;
        Log.w("ResourceBrowser", String.format("invalid url (%s) for %s (%s)", arrayOfObject2));
        str4 = "mtz";
      }
      ExtraFileUtils.mkdirs(new File(paramString), 511, -1, -1);
      Object[] arrayOfObject1 = new Object[5];
      arrayOfObject1[0] = ResourceHelper.getFormattedDirectoryPath(paramString);
      arrayOfObject1[1] = str1;
      arrayOfObject1[2] = str2;
      arrayOfObject1[3] = Integer.valueOf(i);
      arrayOfObject1[4] = str4;
      return String.format("%s%s_(%s.%d).%s", arrayOfObject1);
    }
    catch (Exception localException)
    {
      break label35;
    }
  }

  public static String getFilePathByURL(String paramString1, String paramString2)
  {
    ExtraFileUtils.mkdirs(new File(paramString1), 511, -1, -1);
    String str = (String)mCacheFileNameMap.get(paramString2);
    if (TextUtils.isEmpty(str))
    {
      String[] arrayOfString = paramString2.split("/");
      for (str = arrayOfString[(-1 + arrayOfString.length)]; str.length() > 128; str = str.substring(0, str.length() / 2) + str.hashCode());
      mCacheFileNameMap.put(paramString2, str);
    }
    return ResourceHelper.getFormattedDirectoryPath(paramString1) + str;
  }

  private static String getIMEI()
  {
    String str = TelephonyManager.getDefault().getDeviceId();
    if (TextUtils.isEmpty(str))
      str = "";
    return str;
  }

  public static JSONObject getJSONInformation(File paramFile)
    throws IOException, JSONException
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramFile)), 1024);
    StringBuffer localStringBuffer = new StringBuffer();
    for (String str = localBufferedReader.readLine(); str != null; str = localBufferedReader.readLine())
      localStringBuffer.append(str);
    return new JSONObject(localStringBuffer.toString());
  }

  public static JSONObject getJSONInformation(String paramString)
    throws IOException
  {
    Object localObject = null;
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(getURLInputStream(paramString)), 1024);
      StringBuffer localStringBuffer = new StringBuffer();
      for (String str = localBufferedReader.readLine(); str != null; str = localBufferedReader.readLine())
        localStringBuffer.append(str);
      JSONObject localJSONObject = new JSONObject(localStringBuffer.toString());
      localObject = localJSONObject;
      return localObject;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localJSONException.printStackTrace();
    }
  }

  public static String getListURL(int paramInt1, int paramInt2, String paramString1, String paramString2, Bundle paramBundle)
  {
    if (paramInt2 == 0)
      paramInt2 = 30;
    String str1 = paramBundle.getString("com.miui.android.resourcebrowser.RESOURCE_URL");
    Object[] arrayOfObject = new Object[6];
    arrayOfObject[0] = Integer.valueOf(paramInt1 + 1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    arrayOfObject[2] = Integer.valueOf(paramBundle.getInt("com.miui.android.resourcebrowser.PLATFORM_VERSION_START"));
    arrayOfObject[3] = Integer.valueOf(paramBundle.getInt("com.miui.android.resourcebrowser.PLATFORM_VERSION_END"));
    arrayOfObject[4] = Integer.valueOf(paramBundle.getInt("com.miui.android.resourcebrowser.FORMAT_VERSION_START"));
    arrayOfObject[5] = Integer.valueOf(paramBundle.getInt("com.miui.android.resourcebrowser.FORMAT_VERSION_END"));
    String str2 = String.format(str1, arrayOfObject);
    if (!TextUtils.isEmpty(paramString1))
      str2 = str2 + "&category=" + paramString1;
    if (!TextUtils.isEmpty(paramString2));
    try
    {
      String str3 = URLEncoder.encode(paramString2, "UTF-8");
      paramString2 = str3;
      str2 = str2 + "&keyword=" + paramString2;
      return getEncryptedURL(str2);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
        localUnsupportedEncodingException.printStackTrace();
    }
  }

  private static String getMIUIAccountToken(Context paramContext)
  {
    AccountManager localAccountManager = AccountManager.get(paramContext);
    Account[] arrayOfAccount = localAccountManager.getAccountsByType("com.miui.auth");
    Object localObject = null;
    if (arrayOfAccount.length > 0);
    try
    {
      String str1 = localAccountManager.getUserData(arrayOfAccount[0], "token");
      if (str1 != null)
      {
        String str2 = URLEncoder.encode(str1, "UTF-8");
        localObject = str2;
      }
      return localObject;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
        localUnsupportedEncodingException.printStackTrace();
    }
  }

  private static String getModVersion()
  {
    String str = SystemProperties.get("ro.modversion");
    if (TextUtils.isEmpty(str))
      str = "Unknown";
    return str;
  }

  private static String getShortIMEI()
  {
    String str = getIMEI();
    if (!TextUtils.isEmpty(str))
    {
      int i = str.length();
      if (i >= 9)
        str = str.substring(i - 9, i);
    }
    if (TextUtils.isEmpty(str))
      str = "";
    return str;
  }

  public static InputStream getURLInputStream(String paramString)
    throws IOException
  {
    HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(paramString).openConnection();
    localHttpURLConnection.setConnectTimeout(20000);
    localHttpURLConnection.setReadTimeout(20000);
    localHttpURLConnection.connect();
    return localHttpURLConnection.getInputStream();
  }

  private static String getUserId(Context paramContext)
  {
    return Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
  }

  public static List<ResourceCategory> readCategories(String paramString, Bundle paramBundle)
  {
    Object localObject = null;
    File localFile = new File(paramString);
    try
    {
      if (localFile.exists())
      {
        List localList = buildCategories(getJSONInformation(localFile), paramBundle);
        localObject = localList;
      }
      return localObject;
    }
    catch (IOException localIOException)
    {
      while (true)
        localFile.delete();
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localFile.delete();
    }
  }

  public static List<Resource> readResources(String paramString, Bundle paramBundle)
  {
    Object localObject = null;
    File localFile = new File(paramString);
    try
    {
      if (localFile.exists())
      {
        List localList = buildResources(getJSONInformation(localFile), paramBundle);
        localObject = localList;
      }
      return localObject;
    }
    catch (IOException localIOException)
    {
      while (true)
        localFile.delete();
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localFile.delete();
    }
  }

  public static Map<String, Resource> readSpecifiedResources(String paramString, Bundle paramBundle)
  {
    Object localObject = null;
    File localFile = new File(paramString);
    try
    {
      if (localFile.exists())
      {
        Map localMap = buildSpecifiedResources(getJSONInformation(localFile), paramBundle);
        localObject = localMap;
      }
      if (localObject == null)
        localObject = new HashMap();
      return localObject;
    }
    catch (IOException localIOException)
    {
      while (true)
        localFile.delete();
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localFile.delete();
    }
  }

  public static void sendUserAction(String paramString, Bundle paramBundle, Context paramContext)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramBundle.getString("x_id");
    arrayOfObject[1] = paramBundle.getString("m_id");
    new AsyncTask(getEncryptedURL(appendVersionInfo(paramContext, String.format(paramString, arrayOfObject))))
    {
      protected Void doInBackground(Void[] paramArrayOfVoid)
      {
        try
        {
          OnlineHelper.getURLInputStream(OnlineHelper.this);
          label8: return null;
        }
        catch (IOException localIOException)
        {
          break label8;
        }
      }
    }
    .execute(new Void[0]);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.OnlineHelper
 * JD-Core Version:    0.6.0
 */