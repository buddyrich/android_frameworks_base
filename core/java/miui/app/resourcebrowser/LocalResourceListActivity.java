package miui.app.resourcebrowser;

import android.os.Bundle;
import java.util.List;
import miui.os.AsyncTaskObserver;
import miui.util.CommandLineUtils;

public class LocalResourceListActivity extends ResourceListActivity
  implements AsyncTaskObserver<Void, Resource, List<Resource>>
{
  protected ResourceAdapter getAdapter()
  {
    this.mMetaData.putString("com.miui.android.resourcebrowser.RESOURCE_SET_SUBPACKAGE", ".local");
    return new LocalResourceAdapter(this, this.mMetaData);
  }

  protected int getContentView()
  {
    return 50528269;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    ResourceHelper.addNoMedia(ResourceConstants.CACHE_PATH);
    if (ResourceConstants.MIUI_PATH.equals(ResourceConstants.MIUI_EXTERNAL_PATH))
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = ResourceConstants.PREVIEW_PATH;
      CommandLineUtils.run(true, null, "rm -r %s_data_sdcard_*", arrayOfObject);
    }
  }

  protected void onResume()
  {
    super.onResume();
    this.mAdapter.loadData();
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.LocalResourceListActivity
 * JD-Core Version:    0.6.0
 */