package miui.app.resourcebrowser;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ResourceTabActivity extends TabActivity
  implements IntentConstants
{
  protected Bundle mMetaData;

  protected Bundle buildDefaultMetaData(Bundle paramBundle, String paramString)
  {
    return ResourceHelper.buildDefaultMetaData(paramBundle, paramString, this);
  }

  protected Bundle buildLocalResourceListMetaData(Bundle paramBundle, String paramString)
  {
    return (Bundle)paramBundle.clone();
  }

  protected Bundle buildOnlineResourceListMetaData(Bundle paramBundle, String paramString)
  {
    return (Bundle)paramBundle.clone();
  }

  protected Pair<String, String> getLocalResourceListActivity()
  {
    return new Pair(this.mMetaData.getString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_PACKAGE"), this.mMetaData.getString("com.miui.android.resourcebrowser.LOCAL_LIST_ACTIVITY_CLASS"));
  }

  protected Pair<String, String> getOnlineResourceListActivity()
  {
    return new Pair(this.mMetaData.getString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_PACKAGE"), this.mMetaData.getString("com.miui.android.resourcebrowser.ONLINE_LIST_ACTIVITY_CLASS"));
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(50528284);
    Intent localIntent1 = getIntent();
    Object localObject = localIntent1.getBundleExtra("META_DATA");
    Bundle localBundle = localIntent1.getExtras();
    if ((localObject == null) && (localBundle != null))
      localObject = localBundle;
    while (true)
    {
      String str1 = localIntent1.getAction();
      this.mMetaData = buildDefaultMetaData((Bundle)localObject, str1);
      String str2 = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_NAME");
      TabHost localTabHost = getTabHost();
      Pair localPair1 = getLocalResourceListActivity();
      Intent localIntent2 = ((Intent)localIntent1.clone()).setClassName((String)localPair1.first, (String)localPair1.second);
      localIntent2.putExtra("META_DATA", buildLocalResourceListMetaData(this.mMetaData, str1));
      TabHost.TabSpec localTabSpec1 = localTabHost.newTabSpec("local");
      String str3 = getString(51118100);
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = str2;
      localTabHost.addTab(localTabSpec1.setIndicator(String.format(str3, arrayOfObject1)).setContent(localIntent2));
      Pair localPair2 = getOnlineResourceListActivity();
      Intent localIntent3 = ((Intent)localIntent1.clone()).setClassName((String)localPair2.first, (String)localPair2.second);
      localIntent3.putExtra("META_DATA", buildOnlineResourceListMetaData(this.mMetaData, str1));
      TabHost.TabSpec localTabSpec2 = localTabHost.newTabSpec("online");
      String str4 = getString(51118101);
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = str2;
      localTabHost.addTab(localTabSpec2.setIndicator(String.format(str4, arrayOfObject2)).setContent(localIntent3));
      localTabHost.setCurrentTab(0);
      return;
      if (localObject == null)
      {
        localObject = new Bundle();
        continue;
      }
      if (localBundle == null)
        continue;
      localBundle.remove("META_DATA");
      ((Bundle)localObject).putAll(localBundle);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceTabActivity
 * JD-Core Version:    0.6.0
 */