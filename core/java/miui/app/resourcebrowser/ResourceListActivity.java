package miui.app.resourcebrowser;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.MiuiDownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import miui.os.AsyncTaskObserver;

public abstract class ResourceListActivity extends Activity
  implements AsyncTaskObserver<Void, Resource, List<Resource>>, SDCardMonitor.SDCardStatusListener, IntentConstants
{
  protected ResourceAdapter mAdapter;
  protected int mDisplayType;
  protected DownloadManager mDownloadManager;
  protected DownloadResourceReceiver mDownloadResourceReceiver;
  protected ListView mListView;
  protected Bundle mMetaData;
  protected View mProgressBar;
  protected int mResourceSetCategory;
  protected String mResourceSetCode;
  protected String mResourceSetName;
  protected String mResourceSetPackage;
  protected SDCardMonitor mSDCardMonitor;
  protected boolean mUsingPicker;

  protected abstract ResourceAdapter getAdapter();

  protected abstract int getContentView();

  protected View getHeaderView()
  {
    return null;
  }

  protected Pair<String, String> getResourceDetailActivity()
  {
    return new Pair(this.mMetaData.getString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_PACKAGE"), this.mMetaData.getString("com.miui.android.resourcebrowser.DETAIL_ACTIVITY_CLASS"));
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((this.mMetaData.getBoolean("com.miui.android.resourcebrowser.USING_PICKER")) && (paramIntent != null))
    {
      getParent().setResult(paramInt2, paramIntent);
      finish();
    }
  }

  public void onCancelled()
  {
    this.mProgressBar.setVisibility(8);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(getContentView());
    Intent localIntent = getIntent();
    if (localIntent != null)
      this.mMetaData = localIntent.getBundleExtra("META_DATA");
    if (this.mMetaData == null);
    while (true)
    {
      return;
      pickMetaData(this.mMetaData);
      setupUI();
      int i = this.mMetaData.getInt("android.intent.extra.ringtone.TYPE", -1);
      if (i >= 0)
        ResourceHelper.setMusicVolumeType(this, i);
      this.mDownloadManager = ((DownloadManager)getSystemService("download"));
      this.mDownloadResourceReceiver = new DownloadResourceReceiver();
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
      registerReceiver(this.mDownloadResourceReceiver, localIntentFilter);
      this.mSDCardMonitor = SDCardMonitor.getSDCardMonitor(this);
      this.mSDCardMonitor.addListener(this);
    }
  }

  protected void onDestroy()
  {
    if (this.mDownloadResourceReceiver != null)
      unregisterReceiver(this.mDownloadResourceReceiver);
    if (this.mSDCardMonitor != null)
      this.mSDCardMonitor.removeListener(this);
    super.onDestroy();
  }

  protected void onPause()
  {
    this.mAdapter.stopMusic();
    super.onPause();
  }

  public void onPostExecute(List<Resource> paramList)
  {
    this.mProgressBar.setVisibility(8);
  }

  public void onPreExecute()
  {
    this.mProgressBar.setVisibility(0);
  }

  public void onProgressUpdate(Resource[] paramArrayOfResource)
  {
  }

  protected void onResume()
  {
    super.onResume();
    this.mAdapter.setCurrentUsingPath(this.mMetaData.getString("com.miui.android.resourcebrowser.CURRENT_USING_PATH"));
    this.mAdapter.notifyDataSetChanged();
    String[] arrayOfString = this.mMetaData.getStringArray("com.miui.android.resourcebrowser.SOURCE_FOLDERS");
    for (int i = 0; i < arrayOfString.length; i++)
      ResourceHelper.refreshFolderInfoCache(arrayOfString[i]);
  }

  public void onStatusChanged(boolean paramBoolean)
  {
    ResourceHelper.exit(this);
  }

  protected void onStop()
  {
    this.mAdapter.onStop();
    super.onStop();
  }

  protected void pickMetaData(Bundle paramBundle)
  {
    this.mResourceSetPackage = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_PACKAGE");
    this.mResourceSetCode = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE");
    this.mResourceSetName = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_NAME");
    this.mResourceSetCategory = this.mMetaData.getInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY");
    this.mDisplayType = this.mMetaData.getInt("com.miui.android.resourcebrowser.DISPLAY_TYPE");
    this.mUsingPicker = this.mMetaData.getBoolean("com.miui.android.resourcebrowser.USING_PICKER");
  }

  protected void setupUI()
  {
    this.mListView = ((ListView)findViewById(51052619));
    View localView = getHeaderView();
    if (localView != null)
      this.mListView.addHeaderView(localView);
    this.mAdapter = getAdapter();
    this.mListView.setAdapter(this.mAdapter);
    this.mListView.setFastScrollEnabled(true);
    if (!ResourceHelper.isCombineView(this.mDisplayType))
      this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          ResourceListActivity.this.startDetailActivityForResource(paramInt);
        }
      });
    this.mProgressBar = findViewById(51052620);
  }

  public void startDetailActivityForResource(int paramInt)
  {
    Intent localIntent = new Intent();
    Pair localPair = getResourceDetailActivity();
    localIntent.setClassName((String)localPair.first, (String)localPair.second);
    localIntent.addFlags(67108864);
    this.mMetaData.putInt("com.miui.android.resourcebrowser.RESOURCE_INDEX", paramInt);
    localIntent.putExtra("META_DATA", this.mMetaData);
    startActivityForResult(localIntent, 1);
  }

  public class DownloadResourceReceiver extends BroadcastReceiver
  {
    public DownloadResourceReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      Cursor localCursor;
      if ("android.intent.action.DOWNLOAD_COMPLETE".equals(paramIntent.getAction()))
      {
        DownloadManager.Query localQuery = new DownloadManager.Query();
        long l = paramIntent.getLongExtra("extra_download_id", -1L);
        if (l != -1L)
        {
          long[] arrayOfLong = new long[1];
          arrayOfLong[0] = l;
          localQuery.setFilterById(arrayOfLong);
          localCursor = ResourceListActivity.this.mDownloadManager.query(localQuery);
          if ((localCursor != null) && (localCursor.moveToFirst()))
          {
            if (MiuiDownloadManager.isDownloadSuccess(localCursor))
              break label115;
            Toast.makeText(ResourceListActivity.this, 51118115, 0).show();
          }
        }
      }
      while (true)
      {
        localCursor.close();
        return;
        label115: ResourceHelper.setResourceStatus(Uri.decode(Uri.parse(localCursor.getString(localCursor.getColumnIndex("local_uri"))).getEncodedPath()), ResourceListActivity.this.mAdapter.getResourceSet(), 0);
        ResourceListActivity.this.mAdapter.notifyDataSetChanged();
        ResourceListActivity.this.mAdapter.loadData();
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceListActivity
 * JD-Core Version:    0.6.0
 */