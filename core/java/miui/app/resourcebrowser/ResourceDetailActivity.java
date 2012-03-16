package miui.app.resourcebrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.MiuiDownloadManager;
import android.app.MiuiDownloadManager.Query;
import android.app.MiuiDownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.WebAddress;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import miui.util.CommandLineUtils;
import miui.widget.ScrollableScreenView;
import miui.widget.ScrollableScreenView.OnScrollOutListener;

public class ResourceDetailActivity extends Activity
  implements SDCardMonitor.SDCardStatusListener, IntentConstants
{
  protected BatchMediaPlayer mBatchPlayer;
  protected String mCategoryCode;
  protected ImageButton mDeleteButton;
  protected Button mDownloadButton;
  protected DownloadManager mDownloadManager;
  protected DownloadResourceReceiver mDownloadResourceReceiver;
  protected Set<DownloadFileTask> mDownloadSet = new HashSet();
  protected boolean mFullScreen;
  protected boolean mHasNext = true;
  protected boolean mHasPrevious = true;
  protected String mKeyword;
  protected String mLocalPath;
  protected Bundle mMetaData;
  private ArrayList<SoftReference<Bitmap>> mNeedRecyclePreviewList = new ArrayList();
  protected ImageView mNextButton;
  protected String mOnlinePath;
  protected boolean mOnlineResourceSet;
  protected ImageView mPlayButton;
  protected ScrollableScreenView mPreview;
  protected int mPreviewHeight;
  protected ImageView mPreviousButton;
  protected boolean mReachBottom;
  protected int mResourceIndex;
  protected ResourceSet mResourceSet;
  protected int mResourceSetCategory;
  protected String mResourceSetCode;
  protected String mResourceSetPackage;
  protected String mResourceSetSubpackage;
  protected TextView mRingtoneName;
  protected SDCardMonitor mSDCardMonitor;
  protected ScrollableScreenView.OnScrollOutListener mScrollOutListener;
  protected ScrollView mScrollView;
  protected String mTitle;
  protected Handler mhandler = new Handler();

  private void bindScreenImageView()
  {
    Bundle localBundle = getCurrentResourceInformation();
    ArrayList localArrayList1 = localBundle.getStringArrayList("local_thumbnail");
    ArrayList localArrayList2 = localBundle.getStringArrayList("local_preview");
    ArrayList localArrayList3 = localBundle.getStringArrayList("online_preview");
    recyclePreviewImage();
    int i;
    int j;
    int k;
    label69: ImageView localImageView1;
    Bitmap localBitmap;
    if (getCurrentResource().getStatus() == 2)
    {
      i = 1;
      long l = localBundle.getLong("m_lastupdate");
      j = Math.min(15, localArrayList2.size());
      k = 0;
      if (k >= j)
        break label377;
      localImageView1 = new ImageView(this);
      localImageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
      String str1 = (String)localArrayList2.get(k);
      File localFile = new File(str1);
      if ((localArrayList3 != null) && (i != 0) && (localFile.exists()) && (localFile.lastModified() < l))
        localFile.delete();
      localBitmap = null;
      if (!localFile.exists())
        break label262;
      localBitmap = BitmapFactory.decodeFile(str1);
    }
    while (true)
    {
      if (localBitmap != null)
      {
        localImageView1.setImageBitmap(localBitmap);
        ArrayList localArrayList4 = this.mNeedRecyclePreviewList;
        SoftReference localSoftReference = new SoftReference(localBitmap);
        localArrayList4.add(localSoftReference);
      }
      FrameLayout localFrameLayout1 = new FrameLayout(this);
      localImageView1.setTag(Integer.valueOf(k));
      localFrameLayout1.addView(localImageView1);
      enterNormalMode(localFrameLayout1);
      this.mPreview.addView(localFrameLayout1);
      k++;
      break label69;
      i = 0;
      break;
      label262: if (localArrayList3 == null)
        continue;
      String str2 = (String)localArrayList3.get(k);
      if (TextUtils.isEmpty(str2))
        continue;
      DownloadPreviewTask localDownloadPreviewTask = new DownloadPreviewTask();
      localDownloadPreviewTask.setOffset(k);
      localDownloadPreviewTask.setTargetDirectory(ResourceConstants.PREVIEW_PATH + this.mResourceSetCode);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = str2;
      localDownloadPreviewTask.execute(arrayOfString);
      if (localArrayList1 == null)
        continue;
      localBitmap = BitmapFactory.decodeFile((String)localArrayList1.get(k));
    }
    label377: if (j == 0)
    {
      ImageView localImageView2 = new ImageView(this);
      localImageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
      localImageView2.setImageResource(50462774);
      FrameLayout localFrameLayout2 = new FrameLayout(this);
      localFrameLayout2.addView(localImageView2);
      enterNormalMode(localFrameLayout2);
      this.mPreview.addView(localFrameLayout2);
    }
  }

  private void bindScreenView()
  {
    this.mPreview.removeAllScreens();
    ScrollableScreenView localScrollableScreenView;
    if (this.mResourceSetCategory == 2)
    {
      bindScreenRingtoneView();
      stopMusic();
      localScrollableScreenView = this.mPreview;
      if (this.mPreview.getScreenCount() <= 1)
        break label84;
    }
    label84: for (int i = 0; ; i = 8)
    {
      localScrollableScreenView.setSeekBarVisibility(i);
      this.mPreview.setCurrentScreen(0);
      this.mPreviewHeight = computePreviewZoneHeight();
      this.mPreview.getLayoutParams().height = this.mPreviewHeight;
      return;
      bindScreenImageView();
      break;
    }
  }

  private int computePreviewZoneHeight()
  {
    int i = getWindowManager().getDefaultDisplay().getHeight();
    int j = getResources().getDimensionPixelSize(50987008);
    int k = getResources().getDimensionPixelSize(50987019);
    return i - j - k;
  }

  private void delete()
  {
    File localFile = new File(this.mLocalPath);
    if (localFile.exists())
    {
      localFile.delete();
      String str = this.mLocalPath.replace('/', '_');
      CommandLineUtils.rm(ResourceConstants.PREVIEW_PATH + str, null);
      if (!this.mOnlineResourceSet)
        break label81;
      getCurrentResource().setStatus(2);
      setResourceStatus();
    }
    while (true)
    {
      return;
      label81: finish();
    }
  }

  private void downloadResource()
  {
    String str1 = getConfirmedDownloadUrl(this.mOnlinePath);
    if (getUriFromUrl(str1) == null);
    while (true)
    {
      return;
      MiuiDownloadManager.Request localRequest = new MiuiDownloadManager.Request(Uri.parse(str1));
      localRequest.setShowRunningNotification(true);
      localRequest.setMimeType(getMimeType(str1));
      localRequest.setTitle(this.mTitle);
      localRequest.setDestinationUri(Uri.fromFile(new File(this.mLocalPath)));
      String str2 = this.mLocalPath + ".temp";
      new File(str2).delete();
      localRequest.setAppointName(str2);
      localRequest.setVisibleInDownloadsUi(true);
      localRequest.setAppData(this.mLocalPath);
      this.mDownloadManager.enqueue(localRequest);
    }
  }

  private void enterFullScreenMode(int paramInt)
  {
    getWindow().setFlags(1024, 1024);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
    LinearLayout localLinearLayout1 = (LinearLayout)findViewById(51052618);
    LinearLayout localLinearLayout2 = (LinearLayout)findViewById(51052652);
    ((FrameLayout)findViewById(51052657)).removeView(this.mPreview);
    localLinearLayout1.addView(this.mPreview, localLayoutParams);
    localLinearLayout2.setVisibility(8);
    this.mPreview.setSeekBarVisibility(8);
    for (int i = 0; i < this.mPreview.getScreenCount(); i++)
      enterFullScreenMode(this.mPreview.getScreen(i));
    this.mPreview.setOnScrollOutListener(null);
    this.mPreview.setCurrentScreen(paramInt);
    this.mFullScreen = true;
  }

  private void enterFullScreenMode(View paramView)
  {
    FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-1, -1, 17);
    FrameLayout.LayoutParams localLayoutParams2 = new FrameLayout.LayoutParams(-1, -1, 17);
    paramView.setLayoutParams(localLayoutParams1);
    paramView.setPadding(0, 0, 0, 0);
    ImageView localImageView = (ImageView)((ViewGroup)paramView).getChildAt(0);
    localImageView.setBackgroundResource(0);
    localImageView.setBackgroundColor(-16777216);
    localImageView.setAdjustViewBounds(false);
    localImageView.setLayoutParams(localLayoutParams2);
    localImageView.setPadding(0, 0, 0, 0);
    localImageView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ResourceDetailActivity.this.enterNormalMode();
      }
    });
  }

  private void enterNormalMode()
  {
    getWindow().clearFlags(1024);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, this.mPreviewHeight);
    LinearLayout localLinearLayout1 = (LinearLayout)findViewById(51052618);
    LinearLayout localLinearLayout2 = (LinearLayout)findViewById(51052652);
    FrameLayout localFrameLayout = (FrameLayout)findViewById(51052657);
    localLinearLayout1.removeView(this.mPreview);
    localFrameLayout.addView(this.mPreview, localLayoutParams);
    localLinearLayout2.setVisibility(0);
    ScrollableScreenView localScrollableScreenView = this.mPreview;
    if (this.mPreview.getScreenCount() > 1);
    for (int i = 0; ; i = 8)
    {
      localScrollableScreenView.setSeekBarVisibility(i);
      for (int j = 0; j < this.mPreview.getScreenCount(); j++)
        enterNormalMode(this.mPreview.getScreen(j));
    }
    this.mPreview.setOnScrollOutListener(this.mScrollOutListener);
    this.mPreview.requestFocus();
    this.mhandler.postDelayed(new Runnable()
    {
      public void run()
      {
        ResourceDetailActivity.this.mPreview.setCurrentScreen(ResourceDetailActivity.this.mPreview.getCurrentScreenIndex());
      }
    }
    , 50L);
    this.mFullScreen = false;
  }

  private void enterNormalMode(View paramView)
  {
    FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-2, -1, 17);
    FrameLayout.LayoutParams localLayoutParams2 = new FrameLayout.LayoutParams(-2, -2, 17);
    paramView.setLayoutParams(localLayoutParams1);
    paramView.setMinimumWidth(322);
    paramView.setPadding(10, 40, 10, 60);
    ImageView localImageView = (ImageView)((ViewGroup)paramView).getChildAt(0);
    localImageView.setBackgroundResource(50462915);
    localImageView.setAdjustViewBounds(true);
    localImageView.setLayoutParams(localLayoutParams2);
    if (localImageView.getTag() == null);
    for (Object localObject = null; ; localObject = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ResourceDetailActivity.this.enterFullScreenMode(((Integer)paramView.getTag()).intValue());
      }
    })
    {
      localImageView.setOnClickListener((View.OnClickListener)localObject);
      return;
    }
  }

  private String getMimeType(String paramString)
  {
    String str1 = paramString;
    String str2 = "";
    int i = paramString.lastIndexOf('/');
    if (-1 != i)
      str1 = paramString.substring(i + 1);
    int j = str1.lastIndexOf('.');
    if (-1 != j)
      str2 = str1.substring(j + 1);
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(str2);
  }

  private URI getUriFromUrl(String paramString)
  {
    try
    {
      WebAddress localWebAddress = new WebAddress(new String(URLUtil.decode(paramString.getBytes())));
      String str1 = null;
      String str2 = null;
      String str3 = localWebAddress.getPath();
      if (str3.length() > 0)
      {
        int i = str3.lastIndexOf('#');
        if (i != -1)
        {
          str1 = str3.substring(i + 1);
          str3 = str3.substring(0, i);
        }
        int j = str3.lastIndexOf('?');
        if (j != -1)
        {
          str2 = str3.substring(j + 1);
          str3 = str3.substring(0, j);
        }
      }
      URI localURI2 = new URI(localWebAddress.getScheme(), localWebAddress.getAuthInfo(), localWebAddress.getHost(), localWebAddress.getPort(), str3, str2, str1);
      localURI1 = localURI2;
      return localURI1;
    }
    catch (Exception localException)
    {
      while (true)
      {
        Toast.makeText(this, getString(51118117) + paramString, 0).show();
        URI localURI1 = null;
      }
    }
  }

  private void initPlayer()
  {
    this.mBatchPlayer = new BatchMediaPlayer(this);
    this.mBatchPlayer.setListener(new BatchMediaPlayer.BatchPlayerListener()
    {
      public void finish(boolean paramBoolean)
      {
        ResourceDetailActivity.this.mPlayButton.setImageResource(50462933);
        if (ResourceDetailActivity.this.mRingtoneName != null)
          ResourceDetailActivity.this.mRingtoneName.setText(ResourceDetailActivity.this.getFormatTitleBeforePlayingRingtone());
        if (paramBoolean)
          Toast.makeText(ResourceDetailActivity.this, 51118112, 0).show();
      }

      public void play(String paramString, int paramInt1, int paramInt2)
      {
        if (ResourceDetailActivity.this.mRingtoneName != null)
          ResourceDetailActivity.this.mRingtoneName.setText(ResourceDetailActivity.this.getFormatPlayingRingtoneName(paramString, paramInt1, paramInt2));
      }
    });
    this.mBatchPlayer.setPlayList(getMusicPlayList(getCurrentResource()));
  }

  private boolean isDownloading()
  {
    MiuiDownloadManager.Query localQuery = new MiuiDownloadManager.Query();
    localQuery.setFilterByAppData(this.mLocalPath.replace("'", "''"));
    Cursor localCursor = this.mDownloadManager.query(localQuery);
    boolean bool = false;
    if ((localCursor != null) && (localCursor.moveToFirst()))
    {
      bool = MiuiDownloadManager.isDownloading(localCursor);
      localCursor.close();
    }
    return bool;
  }

  private void navigateToNextResource()
  {
    int i = this.mResourceSet.size();
    if (this.mResourceIndex < i - 1)
    {
      this.mResourceIndex = (1 + this.mResourceIndex);
      changeCurrentResource();
      this.mPreviousButton.setEnabled(true);
      this.mHasPrevious = true;
      if (this.mResourceIndex == i - 1)
      {
        this.mNextButton.setEnabled(false);
        this.mHasNext = false;
        if ((!this.mReachBottom) && (this.mOnlineResourceSet))
          requestResources(i, 0);
      }
      this.mMetaData.putInt("com.miui.android.resourcebrowser.RESOURCE_INDEX", this.mResourceIndex);
    }
  }

  private void navigateToPreviousResource()
  {
    if (this.mResourceIndex > 0)
    {
      this.mResourceIndex = (-1 + this.mResourceIndex);
      changeCurrentResource();
      this.mNextButton.setEnabled(true);
      this.mHasNext = true;
      if (this.mResourceIndex == 0)
      {
        this.mPreviousButton.setEnabled(false);
        this.mHasPrevious = false;
      }
      this.mMetaData.putInt("com.miui.android.resourcebrowser.RESOURCE_INDEX", this.mResourceIndex);
    }
  }

  private void requestResources(int paramInt1, int paramInt2)
  {
    int i = paramInt1 * 1;
    if ((i - this.mResourceSet.size()) / 1 != 0);
    while (true)
    {
      return;
      String str1 = OnlineHelper.getListURL(i, paramInt2, this.mCategoryCode, this.mKeyword, this.mMetaData);
      String str2 = ResourceHelper.getFormattedDirectoryPath(this.mMetaData.getString("com.miui.android.resourcebrowser.CACHE_LIST_FOLDER")) + this.mResourceSetCode;
      if (i == 0)
      {
        String str3 = OnlineHelper.getFilePathByURL(str2, str1);
        File localFile = new File(str3);
        if (localFile.exists())
        {
          List localList = OnlineHelper.readResources(str3, this.mMetaData);
          if (localList != null)
            this.mResourceSet.setAll(localList);
          if (new Date().getTime() - localFile.lastModified() < 600000L)
            continue;
        }
      }
      DownloadRankingListTask localDownloadRankingListTask = new DownloadRankingListTask();
      localDownloadRankingListTask.setId("list_" + i);
      localDownloadRankingListTask.setTargetDirectory(str2);
      localDownloadRankingListTask.setOffset(i);
      if (this.mDownloadSet.contains(localDownloadRankingListTask))
        continue;
      this.mDownloadSet.add(localDownloadRankingListTask);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = str1;
      localDownloadRankingListTask.execute(arrayOfString);
    }
  }

  private void setupButton()
  {
    this.mDownloadButton = ((Button)findViewById(51052660));
    this.mDownloadButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        CharSequence localCharSequence = ResourceDetailActivity.this.mDownloadButton.getText();
        if (localCharSequence.equals(ResourceDetailActivity.this.getString(51118107)))
          ResourceDetailActivity.this.pick();
        while (true)
        {
          ResourceDetailActivity.this.mDownloadButton.setEnabled(false);
          ResourceDetailActivity.this.mhandler.postDelayed(new Runnable()
          {
            public void run()
            {
              CharSequence localCharSequence = ResourceDetailActivity.this.mDownloadButton.getText();
              if ((localCharSequence.equals(ResourceDetailActivity.this.getString(51118107))) || (localCharSequence.equals(ResourceDetailActivity.this.getString(51118105))) || (localCharSequence.equals(ResourceDetailActivity.this.getString(51118103))) || (localCharSequence.equals(ResourceDetailActivity.this.getString(51118106))))
                ResourceDetailActivity.this.mDownloadButton.setEnabled(true);
            }
          }
          , 300L);
          return;
          if (localCharSequence.equals(ResourceDetailActivity.this.getString(51118105)))
          {
            ResourceDetailActivity.this.apply();
            continue;
          }
          if (localCharSequence.equals(ResourceDetailActivity.this.getString(51118103)))
          {
            ResourceDetailActivity.this.download();
            continue;
          }
          if (!localCharSequence.equals(ResourceDetailActivity.this.getString(51118106)))
            continue;
          ResourceDetailActivity.this.update();
        }
      }
    });
    this.mDeleteButton = ((ImageButton)findViewById(51052661));
    this.mDeleteButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        new AlertDialog.Builder(ResourceDetailActivity.this).setIcon(17301659).setMessage(51118118).setNegativeButton(17039360, null).setPositiveButton(17039370, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            ResourceDetailActivity.this.delete();
          }
        }).create().show();
      }
    });
  }

  private void setupNavigator()
  {
    this.mPreviousButton = ((ImageView)findViewById(51052654));
    this.mNextButton = ((ImageView)findViewById(51052655));
    this.mPreviousButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ResourceDetailActivity.this.navigateToPreviousResource();
      }
    });
    this.mNextButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ResourceDetailActivity.this.navigateToNextResource();
      }
    });
    int i = this.mResourceSet.size();
    if (this.mResourceIndex == 0)
    {
      this.mPreviousButton.setEnabled(false);
      this.mHasPrevious = false;
    }
    if (this.mResourceIndex == i - 1)
    {
      this.mNextButton.setEnabled(false);
      this.mHasNext = false;
      if ((!this.mReachBottom) && (this.mOnlineResourceSet))
        requestResources(i, 0);
    }
  }

  private void stopMusic()
  {
    if (this.mBatchPlayer != null)
    {
      this.mBatchPlayer.stop();
      this.mBatchPlayer = null;
    }
  }

  protected void apply()
  {
  }

  protected void bindScreenRingtoneView()
  {
    View localView = ((LayoutInflater)getSystemService("layout_inflater")).inflate(50528283, null);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2, 17);
    this.mPreview.addView(localView, localLayoutParams);
    this.mPlayButton = ((ImageView)localView.findViewById(51052672));
    this.mPlayButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (ResourceDetailActivity.this.mBatchPlayer == null)
        {
          ResourceDetailActivity.this.mPlayButton.setImageResource(50462930);
          ResourceDetailActivity.this.initPlayer();
          ResourceDetailActivity.this.mBatchPlayer.start();
        }
        while (true)
        {
          return;
          if (ResourceDetailActivity.this.mBatchPlayer.isPlaying())
          {
            ResourceDetailActivity.this.mPlayButton.setImageResource(50462933);
            ResourceDetailActivity.this.mBatchPlayer.pause();
            continue;
          }
          ResourceDetailActivity.this.mPlayButton.setImageResource(50462930);
          ResourceDetailActivity.this.mBatchPlayer.start();
        }
      }
    });
    if (TextUtils.isEmpty(this.mLocalPath))
      this.mPlayButton.setVisibility(4);
    if (this.mMetaData.getBoolean("com.miui.android.resourcebrowser.SHOW_RINGTONE_NAME", false))
    {
      this.mRingtoneName = ((TextView)localView.findViewById(51052671));
      this.mRingtoneName.setVisibility(0);
      this.mRingtoneName.setText(getFormatTitleBeforePlayingRingtone());
    }
  }

  protected Bundle buildDefaultMetaData(Bundle paramBundle, String paramString)
  {
    return ResourceHelper.buildDefaultMetaData(paramBundle, paramString, this);
  }

  protected boolean changeCurrentResource()
  {
    Bundle localBundle = getCurrentResourceInformation();
    int i;
    if (localBundle == null)
    {
      i = 0;
      return i;
    }
    this.mOnlinePath = localBundle.getString("online_path");
    this.mLocalPath = ResourceHelper.getVersionPath(getCurrentResource(), this.mMetaData);
    this.mTitle = localBundle.getString("m_title");
    ((TextView)findViewById(51052653)).setText(this.mTitle);
    String str1 = localBundle.getString("designer");
    if (str1 == null)
      str1 = getString(51118092);
    ((TextView)findViewById(51052663)).setText(str1);
    String str2 = localBundle.getString("nickname");
    if (TextUtils.isEmpty(str2))
      str2 = getString(51118092);
    ((TextView)findViewById(51052664)).setText(str2);
    String str3 = localBundle.getString("m_addtime");
    ((TextView)findViewById(51052667)).setText(str3);
    String str4 = localBundle.getString("version");
    ((TextView)findViewById(51052666)).setText(str4);
    String str5 = localBundle.getString("filesize");
    ((TextView)findViewById(51052665)).setText(str5);
    String str6 = localBundle.getString("m_download");
    ((TextView)findViewById(51052668)).setText(str6);
    TextView localTextView = (TextView)findViewById(51052669);
    if ((!TextUtils.isEmpty(this.mLocalPath)) && (new File(this.mLocalPath).exists()))
      localTextView.setText(ResourceHelper.getFileName(this.mLocalPath));
    while (true)
    {
      setResourceStatus();
      bindScreenView();
      i = 1;
      break;
      localTextView.setText("");
    }
  }

  protected void download()
  {
    downloadResource();
    setResourceStatus();
    OnlineHelper.sendUserAction("r=xmXshare/download&xid=%s&aid=%s", getCurrentResourceInformation(), this);
  }

  protected String getConfirmedDownloadUrl(String paramString)
  {
    return paramString;
  }

  protected Resource getCurrentResource()
  {
    return (Resource)this.mResourceSet.get(this.mResourceIndex);
  }

  protected Bundle getCurrentResourceInformation()
  {
    Resource localResource = getCurrentResource();
    if (localResource == null);
    for (Bundle localBundle = null; ; localBundle = localResource.getInformation())
      return localBundle;
  }

  protected String getFormatPlayingRingtoneName(String paramString, int paramInt1, int paramInt2)
  {
    return ResourceHelper.getDefaultFormatPlayingRingtoneName(paramString, paramInt1, paramInt2);
  }

  protected String getFormatTitleBeforePlayingRingtone()
  {
    return "";
  }

  protected List<String> getMusicPlayList(Resource paramResource)
  {
    return ResourceHelper.getDefaultMusicPlayList(this, paramResource);
  }

  public void onBackPressed()
  {
    if (this.mFullScreen)
      enterNormalMode();
    while (true)
    {
      return;
      super.onBackPressed();
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(50528282);
    Intent localIntent = getIntent();
    if (localIntent != null)
      this.mMetaData = localIntent.getBundleExtra("META_DATA");
    if (this.mMetaData == null)
    {
      this.mMetaData = buildDefaultMetaData(new Bundle(), localIntent.getAction());
      localIntent.putExtra("META_DATA", this.mMetaData);
    }
    pickMetaData(this.mMetaData);
    Resource localResource;
    if ("android.intent.action.VIEW".equals(localIntent.getAction()))
    {
      localResource = responseToViewAction();
      if (localResource == null)
        finish();
    }
    while (true)
    {
      return;
      this.mResourceSet.clear();
      this.mResourceSet.add(localResource);
      for (this.mResourceIndex = 0; ; this.mResourceIndex = this.mMetaData.getInt("com.miui.android.resourcebrowser.RESOURCE_INDEX"))
      {
        if (!this.mResourceSet.isEmpty())
          break label163;
        finish();
        break;
      }
      label163: setupUI();
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
      changeCurrentResource();
    }
  }

  protected void onDestroy()
  {
    if (this.mDownloadResourceReceiver != null)
      unregisterReceiver(this.mDownloadResourceReceiver);
    if (this.mSDCardMonitor != null)
      this.mSDCardMonitor.removeListener(this);
    stopMusic();
    recyclePreviewImage();
    super.onDestroy();
  }

  protected void onPause()
  {
    stopMusic();
    super.onPause();
  }

  public void onStatusChanged(boolean paramBoolean)
  {
    ResourceHelper.exit(this);
  }

  protected void pick()
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("com.miui.android.resourcebrowser.PICKED_RESOURCE", this.mLocalPath);
    localIntent.putExtra("com.miui.android.resourcebrowser.TRACK_ID", this.mMetaData.getString("com.miui.android.resourcebrowser.TRACK_ID"));
    if (this.mResourceSetCategory == 2)
      localIntent.putExtra("android.intent.extra.ringtone.PICKED_URI", ResourceHelper.getUriByPath(this.mLocalPath));
    setResult(-1, localIntent);
    finish();
  }

  protected void pickMetaData(Bundle paramBundle)
  {
    this.mResourceSetPackage = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_PACKAGE");
    this.mResourceSetSubpackage = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_SUBPACKAGE");
    this.mResourceSetCode = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE");
    this.mResourceSetCategory = this.mMetaData.getInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY");
    if (this.mResourceSetSubpackage == null)
      this.mResourceSetSubpackage = ".single";
    this.mResourceSet = ResourceSet.getInstance(this.mResourceSetPackage + this.mResourceSetSubpackage);
    if ((this.mResourceSetSubpackage.startsWith(".online.hottest")) || (this.mResourceSetSubpackage.startsWith(".online.latest")));
    for (boolean bool = true; ; bool = false)
    {
      this.mOnlineResourceSet = bool;
      this.mCategoryCode = this.mMetaData.getString("com.miui.android.resourcebrowser.CATEGORY_CODE");
      this.mKeyword = this.mMetaData.getString("com.miui.android.resourcebrowser.KEYWORD");
      return;
    }
  }

  void recyclePreviewImage()
  {
    Iterator localIterator = this.mNeedRecyclePreviewList.iterator();
    while (localIterator.hasNext())
    {
      Bitmap localBitmap = (Bitmap)((SoftReference)localIterator.next()).get();
      if (localBitmap == null)
        continue;
      localBitmap.recycle();
    }
    this.mNeedRecyclePreviewList.clear();
  }

  protected Resource responseToViewAction()
  {
    Resource localResource = null;
    this.mLocalPath = getIntent().getData().getPath();
    ZipResourceInfo localZipResourceInfo = ZipResourceInfo.createZipResourceInfo(this, this.mLocalPath, null, new Object[0]);
    if (localZipResourceInfo == null);
    while (true)
    {
      return localResource;
      localResource = new Resource();
      localResource.setInformation(localZipResourceInfo.getInformation());
    }
  }

  protected void setApplyStatus(boolean paramBoolean)
  {
    this.mDownloadButton.setText(getString(51118105));
    this.mDownloadButton.setEnabled(true);
    ImageButton localImageButton = this.mDeleteButton;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localImageButton.setVisibility(i);
      return;
    }
  }

  protected void setDownloadStatus()
  {
    this.mDownloadButton.setText(getString(51118103));
    this.mDownloadButton.setEnabled(true);
    this.mDeleteButton.setVisibility(8);
  }

  protected void setDownloadingStatus()
  {
    this.mDownloadButton.setText(getString(51118104));
    this.mDownloadButton.setEnabled(false);
    this.mDeleteButton.setVisibility(8);
  }

  protected void setPickStatus(boolean paramBoolean)
  {
    this.mDownloadButton.setText(getString(51118107));
    this.mDownloadButton.setEnabled(true);
    ImageButton localImageButton = this.mDeleteButton;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localImageButton.setVisibility(i);
      return;
    }
  }

  protected void setResourceStatus()
  {
    int i = 1;
    int j = getCurrentResource().getStatus();
    if ((this.mMetaData.getBoolean("com.miui.android.resourcebrowser.USING_PICKER")) && (j != 2))
      if ((!TextUtils.isEmpty(this.mLocalPath)) && (!ResourceHelper.isSystemResource(this.mLocalPath)))
        setPickStatus(i);
    while (true)
    {
      return;
      i = 0;
      break;
      if (isDownloading())
      {
        setDownloadingStatus();
        continue;
      }
      if (TextUtils.isEmpty(this.mLocalPath))
      {
        setApplyStatus(false);
        continue;
      }
      if (j == 0)
      {
        if (!this.mLocalPath.startsWith("/system"));
        while (true)
        {
          setApplyStatus(i);
          break;
          i = 0;
        }
      }
      if (j == i)
      {
        setUpdateStatus();
        continue;
      }
      setDownloadStatus();
    }
  }

  protected void setUpdateStatus()
  {
    this.mDownloadButton.setText(getString(51118106));
    this.mDownloadButton.setEnabled(true);
    this.mDeleteButton.setVisibility(8);
  }

  protected void setupUI()
  {
    this.mPreview = ((ScrollableScreenView)findViewById(51052658));
    this.mPreview.setBackgroundColor(0);
    this.mPreview.setOverScrollRatio(0.2F);
    this.mPreview.setOvershootTension(0.0F);
    this.mPreview.setScreenAlignment(2);
    this.mScrollOutListener = new ScrollableScreenView.OnScrollOutListener()
    {
      public boolean onScrollOut(View paramView, int paramInt)
      {
        int i = 1;
        if ((paramInt == 0) && (ResourceDetailActivity.this.mHasPrevious))
          ResourceDetailActivity.this.navigateToPreviousResource();
        while (true)
        {
          return i;
          if ((paramInt == i) && (ResourceDetailActivity.this.mHasNext))
          {
            ResourceDetailActivity.this.navigateToNextResource();
            continue;
          }
          i = 0;
        }
      }
    };
    this.mPreview.setOnScrollOutListener(this.mScrollOutListener);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2, 81);
    localLayoutParams.setMargins(0, 0, 0, 30);
    this.mPreview.setSeekBarPosition(localLayoutParams);
    this.mScrollView = ((ScrollView)findViewById(51052656));
    this.mPreview.setParentScrollView(this.mScrollView);
    setupButton();
    setupNavigator();
  }

  protected void update()
  {
    downloadResource();
    setResourceStatus();
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
          localCursor = ResourceDetailActivity.this.mDownloadManager.query(localQuery);
          if ((localCursor != null) && (localCursor.moveToFirst()))
          {
            if (MiuiDownloadManager.isDownloadSuccess(localCursor))
              break label115;
            Toast.makeText(ResourceDetailActivity.this, 51118115, 0).show();
          }
        }
      }
      while (true)
      {
        localCursor.close();
        return;
        label115: ResourceHelper.setResourceStatus(Uri.decode(Uri.parse(localCursor.getString(localCursor.getColumnIndex("local_uri"))).getEncodedPath()), ResourceDetailActivity.this.mResourceSet, 0);
        ResourceDetailActivity.this.setResourceStatus();
      }
    }
  }

  public class DownloadRankingListTask extends DownloadFileTask
  {
    private int offset = 0;

    public DownloadRankingListTask()
    {
    }

    public int getOffset()
    {
      return this.offset;
    }

    protected void onPostExecute(List<DownloadFileTask.DownloadFileEntry> paramList)
    {
      String str = null;
      if (paramList.size() > 0)
        str = ((DownloadFileTask.DownloadFileEntry)paramList.get(0)).getPath();
      if (str == null)
      {
        ResourceDetailActivity.this.mReachBottom = true;
        Toast.makeText(ResourceDetailActivity.this, 51118112, 0).show();
      }
      List localList;
      while (true)
      {
        ResourceDetailActivity.this.mDownloadSet.remove(this);
        return;
        localList = OnlineHelper.readResources(str, ResourceDetailActivity.this.mMetaData);
        if ((localList != null) && (localList.size() != 0))
          break;
        ResourceDetailActivity.this.mReachBottom = true;
      }
      if (this.offset == 0)
        ResourceDetailActivity.this.mResourceSet.setAll(localList);
      while (true)
      {
        ResourceDetailActivity.this.mNextButton.setEnabled(true);
        ResourceDetailActivity.this.mHasNext = true;
        break;
        int i = ResourceDetailActivity.this.mResourceSet.size();
        if (this.offset != i)
          continue;
        ResourceDetailActivity.this.mResourceSet.addAll(localList);
      }
    }

    public void setOffset(int paramInt)
    {
      this.offset = paramInt;
    }
  }

  public class DownloadPreviewTask extends DownloadFileTask
  {
    private int offset;
    private int validIndex;

    public DownloadPreviewTask()
    {
    }

    public int getOffset()
    {
      return this.offset;
    }

    protected void onPreExecute()
    {
      this.validIndex = ResourceDetailActivity.this.mResourceIndex;
    }

    protected void onProgressUpdate(DownloadFileTask.DownloadFileEntry[] paramArrayOfDownloadFileEntry)
    {
      if (this.validIndex == ResourceDetailActivity.this.mResourceIndex)
      {
        DownloadFileTask.DownloadFileEntry localDownloadFileEntry = paramArrayOfDownloadFileEntry[0];
        if (localDownloadFileEntry == null)
          break label88;
        ImageView localImageView = (ImageView)((ViewGroup)ResourceDetailActivity.this.mPreview.getScreen(localDownloadFileEntry.getIndex() + this.offset)).getChildAt(0);
        Bitmap localBitmap = BitmapFactory.decodeFile(localDownloadFileEntry.getPath());
        localImageView.setImageBitmap(localBitmap);
        ResourceDetailActivity.this.mNeedRecyclePreviewList.add(new SoftReference(localBitmap));
      }
      while (true)
      {
        return;
        label88: Toast.makeText(ResourceDetailActivity.this, 51118116, 0).show();
      }
    }

    public void setOffset(int paramInt)
    {
      this.offset = paramInt;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceDetailActivity
 * JD-Core Version:    0.6.0
 */