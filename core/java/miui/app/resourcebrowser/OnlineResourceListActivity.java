package miui.app.resourcebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import miui.os.AsyncTaskObserver;

public class OnlineResourceListActivity extends ResourceListActivity
  implements AsyncTaskObserver<Void, Resource, List<Resource>>
{
  private static final int CONFIRM_MIUI_DISCLAIMER_REQUEST = 1000;
  private ArrayAdapter<ResourceCategory> mCategoryAdapter;
  private String mCategoryCode;
  private String mCloseText;
  private Set<DownloadFileTask> mDownloadSet = new HashSet();
  private TextView mHottestTab;
  private String mKeyword;
  private TextView mLatestTab;
  private String mOpenText;
  private View mSearchBar;
  private ImageView mSearchButton;
  private Animation mSearchInAnimation;
  private EditText mSearchInputText;
  private Animation mSearchOutAnimation;
  private TextView mSearchToggle;
  private View mTabBar;
  private Animation mTabInAnimation;
  private Animation mTabOutAnimation;
  private int mTabSwitchCount;

  private ResourceCategory getCategoryHeader()
  {
    ResourceCategory localResourceCategory = new ResourceCategory();
    localResourceCategory.setName(getString(51118119));
    return localResourceCategory;
  }

  private void setKeyword(String paramString)
  {
    this.mKeyword = paramString;
    ((OnlineResourceAdapter)this.mAdapter).setKeyword(this.mKeyword);
    this.mSearchInputText.setText(this.mKeyword);
  }

  protected ResourceAdapter getAdapter()
  {
    this.mMetaData.putString("com.miui.android.resourcebrowser.RESOURCE_SET_SUBPACKAGE", ".online.hottest");
    return new OnlineResourceAdapter(this, this.mMetaData);
  }

  protected int getContentView()
  {
    return 50528280;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 1000) && (paramInt2 == 0))
      finish();
  }

  public void onBackPressed()
  {
    if (!TextUtils.isEmpty(this.mKeyword))
    {
      setKeyword(null);
      updateSearchToggleState(false);
    }
    while (true)
    {
      return;
      super.onBackPressed();
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mAdapter.loadMoreData(false, this.mTabSwitchCount, true);
    if (Settings.System.getInt(getContentResolver(), "confirm_miui_disclaimer", 0) != 1)
      startActivityForResult(new Intent("android.intent.action.MIUI_DISCLAIMER"), 1000);
  }

  protected void onResume()
  {
    super.onResume();
  }

  protected void onStop()
  {
    super.onStop();
  }

  protected void requestCategories()
  {
    String str1 = OnlineHelper.getEncryptedURL(this.mMetaData.getString("com.miui.android.resourcebrowser.CATEGORY_URL"));
    String str2 = ResourceConstants.CATEGORY_PATH + this.mResourceSetCode;
    String str3 = OnlineHelper.getFilePathByURL(str2, str1);
    File localFile = new File(str3);
    if (localFile.exists())
    {
      setCategories(OnlineHelper.readCategories(str3, this.mMetaData));
      if (new Date().getTime() - localFile.lastModified() >= 600000L);
    }
    while (true)
    {
      return;
      DownloadCategoryListTask localDownloadCategoryListTask = new DownloadCategoryListTask();
      localDownloadCategoryListTask.setId("category");
      localDownloadCategoryListTask.setTargetDirectory(str2);
      if (this.mDownloadSet.contains(localDownloadCategoryListTask))
        continue;
      this.mDownloadSet.add(localDownloadCategoryListTask);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = str1;
      localDownloadCategoryListTask.execute(arrayOfString);
    }
  }

  protected void setCategories(List<ResourceCategory> paramList)
  {
    if (paramList != null)
    {
      this.mCategoryAdapter.clear();
      this.mCategoryAdapter.add(getCategoryHeader());
      for (int i = 0; i < paramList.size(); i++)
        this.mCategoryAdapter.add(paramList.get(i));
    }
  }

  protected void setupUI()
  {
    super.setupUI();
    1 local1 = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        OnlineResourceListActivity localOnlineResourceListActivity = OnlineResourceListActivity.this;
        if (paramView.getId() == 51052646);
        for (boolean bool = true; ; bool = false)
        {
          localOnlineResourceListActivity.updateTabState(bool);
          OnlineResourceListActivity.this.mAdapter.refreshDataSet();
          OnlineResourceListActivity.this.mListView.setSelection(0);
          return;
        }
      }
    };
    this.mHottestTab = ((TextView)findViewById(51052646));
    this.mHottestTab.setOnClickListener(local1);
    this.mLatestTab = ((TextView)findViewById(51052647));
    this.mLatestTab.setOnClickListener(local1);
    updateTabState(true);
    if (this.mMetaData.getBoolean("com.miui.android.resourcebrowser.CATEGORY_SUPPORTED"))
    {
      this.mHottestTab.setText(51118110);
      this.mLatestTab.setText(51118111);
      Spinner localSpinner = (Spinner)findViewById(51052645);
      this.mCategoryAdapter = new ArrayAdapter(this, 17367048);
      this.mCategoryAdapter.setDropDownViewResource(17367049);
      localSpinner.setAdapter(this.mCategoryAdapter);
      localSpinner.setVisibility(0);
      localSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
      {
        public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          OnlineResourceListActivity.access$002(OnlineResourceListActivity.this, ((ResourceCategory)OnlineResourceListActivity.this.mCategoryAdapter.getItem(paramInt)).getCode());
          ((OnlineResourceAdapter)OnlineResourceListActivity.this.mAdapter).setCategoryCode(OnlineResourceListActivity.this.mCategoryCode);
        }

        public void onNothingSelected(AdapterView<?> paramAdapterView)
        {
        }
      });
      requestCategories();
    }
    this.mSearchInputText = ((EditText)findViewById(51052649));
    this.mSearchButton = ((ImageView)findViewById(51052650));
    this.mSearchButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        String str = OnlineResourceListActivity.this.mSearchInputText.getText().toString();
        if (!TextUtils.isEmpty(str))
          OnlineResourceListActivity.this.setKeyword(str);
      }
    });
    this.mSearchToggle = ((TextView)findViewById(51052651));
    this.mSearchToggle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        OnlineResourceListActivity.this.updateSearchToggleState(OnlineResourceListActivity.this.mOpenText.equals(OnlineResourceListActivity.this.mSearchToggle.getText()));
      }
    });
    this.mTabBar = findViewById(51052644);
    this.mSearchBar = findViewById(51052648);
    this.mOpenText = getString(17039372);
    this.mCloseText = getString(17039360);
    this.mTabInAnimation = AnimationUtils.loadAnimation(this, 17432578);
    this.mTabOutAnimation = AnimationUtils.loadAnimation(this, 50593799);
    this.mSearchInAnimation = AnimationUtils.loadAnimation(this, 50593798);
    this.mSearchOutAnimation = AnimationUtils.loadAnimation(this, 17432579);
  }

  public void startDetailActivityForResource(int paramInt)
  {
    this.mMetaData.putString("com.miui.android.resourcebrowser.CATEGORY_CODE", this.mCategoryCode);
    this.mMetaData.putString("com.miui.android.resourcebrowser.KEYWORD", this.mKeyword);
    super.startDetailActivityForResource(paramInt);
  }

  protected void updateSearchToggleState(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mTabBar.startAnimation(this.mTabOutAnimation);
      this.mTabBar.setVisibility(8);
      this.mSearchBar.startAnimation(this.mSearchInAnimation);
      this.mSearchBar.setVisibility(0);
      this.mSearchToggle.setText(this.mCloseText);
    }
    while (true)
    {
      return;
      this.mTabBar.startAnimation(this.mTabInAnimation);
      this.mTabBar.setVisibility(0);
      this.mSearchBar.startAnimation(this.mSearchOutAnimation);
      this.mSearchBar.setVisibility(8);
      this.mSearchToggle.setText(this.mOpenText);
      if (TextUtils.isEmpty(this.mKeyword))
        continue;
      setKeyword(null);
    }
  }

  protected void updateTabState(boolean paramBoolean)
  {
    boolean bool1 = true;
    this.mAdapter.stopMusic();
    TextView localTextView1 = this.mLatestTab;
    boolean bool2;
    label53: String str1;
    if (!paramBoolean)
    {
      bool2 = bool1;
      localTextView1.setSelected(bool2);
      this.mLatestTab.setEnabled(paramBoolean);
      this.mHottestTab.setSelected(paramBoolean);
      TextView localTextView2 = this.mHottestTab;
      if (paramBoolean)
        break label133;
      localTextView2.setEnabled(bool1);
      if (!paramBoolean)
        break label138;
      str1 = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_HOTTEST_URL");
    }
    for (String str2 = ".online.hottest"; ; str2 = ".online.latest")
    {
      this.mMetaData.putString("com.miui.android.resourcebrowser.RESOURCE_URL", str1);
      this.mMetaData.putString("com.miui.android.resourcebrowser.RESOURCE_SET_SUBPACKAGE", str2);
      this.mTabSwitchCount = (1 + this.mTabSwitchCount);
      ((OnlineResourceAdapter)this.mAdapter).setFlag(this.mTabSwitchCount);
      return;
      bool2 = false;
      break;
      label133: bool1 = false;
      break label53;
      label138: str1 = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_LATEST_URL");
    }
  }

  public class DownloadCategoryListTask extends DownloadFileTask
  {
    public DownloadCategoryListTask()
    {
    }

    protected void onPostExecute(List<DownloadFileTask.DownloadFileEntry> paramList)
    {
      String str = null;
      if (paramList.size() > 0)
        str = ((DownloadFileTask.DownloadFileEntry)paramList.get(0)).getPath();
      if (str != null)
        OnlineResourceListActivity.this.setCategories(OnlineHelper.readCategories(str, OnlineResourceListActivity.this.mMetaData));
      OnlineResourceListActivity.this.mDownloadSet.remove(this);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.OnlineResourceListActivity
 * JD-Core Version:    0.6.0
 */