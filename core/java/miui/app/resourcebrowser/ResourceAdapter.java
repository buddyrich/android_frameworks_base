package miui.app.resourcebrowser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import miui.os.DaemonAsyncTask.StackJobPool;
import miui.widget.AsyncAdapter;
import miui.widget.AsyncImageAdapter;
import miui.widget.AsyncImageAdapter.AsyncLoadImageTask;

public abstract class ResourceAdapter extends AsyncImageAdapter<Resource>
  implements IntentConstants
{
  private BatchMediaPlayer mBatchPlayer;
  protected Activity mContext;
  private ImageView mCurrentPlayingButton;
  private Resource mCurrentPlayingResource;
  protected String mCurrentUsingPath;
  protected int mDisplayType;
  protected LayoutInflater mInflater;
  protected Bundle mMetaData;
  protected ResourceSet mResourceSet;
  protected int mResourceSetCategory;
  protected String mResourceSetCode;
  protected String mResourceSetPackage;
  protected String mResourceSetSubpackage;
  protected boolean mShowRingtoneName;
  private int mThumbnailHeight;
  private int mThumbnailWidth;

  public ResourceAdapter(Context paramContext, Bundle paramBundle)
  {
    this.mContext = ((Activity)paramContext);
    this.mMetaData = paramBundle;
    this.mResourceSetCode = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_CODE");
    this.mResourceSetCategory = this.mMetaData.getInt("com.miui.android.resourcebrowser.RESOURCE_SET_CATEGORY");
    this.mShowRingtoneName = this.mMetaData.getBoolean("com.miui.android.resourcebrowser.SHOW_RINGTONE_NAME", false);
    refreshDataSet();
    this.mDisplayType = this.mMetaData.getInt("com.miui.android.resourcebrowser.DISPLAY_TYPE");
    if (ResourceHelper.isCombineView(this.mDisplayType))
      setDataPerLine(3);
    setAutoLoadMoreStyle(2);
    setPreloadOffset(30 / (2 * getDataPerLine()));
    this.mInflater = ((LayoutInflater)this.mContext.getSystemService("layout_inflater"));
    View localView = this.mInflater.inflate(ResourceHelper.getViewResource(this.mDisplayType), null).findViewById(51052605);
    ViewGroup.LayoutParams localLayoutParams1 = localView.getLayoutParams();
    this.mThumbnailWidth = localLayoutParams1.width;
    this.mThumbnailHeight = localLayoutParams1.height;
    if (((this.mThumbnailWidth <= 0) || (this.mThumbnailHeight <= 0)) && (ResourceHelper.isCombineView(this.mDisplayType)))
    {
      ViewParent localViewParent = localView.getParent();
      if ((localViewParent instanceof ViewGroup))
      {
        ViewGroup.LayoutParams localLayoutParams2 = ((ViewGroup)localViewParent).getLayoutParams();
        this.mThumbnailWidth = localLayoutParams2.width;
        this.mThumbnailHeight = localLayoutParams2.height;
      }
    }
  }

  private void bindCombineView(View paramView, int paramInt1, List<Resource> paramList, int paramInt2, int paramInt3, int paramInt4)
  {
    Resource localResource = null;
    if (paramInt3 < paramList.size())
      localResource = (Resource)paramList.get(paramInt3);
    ImageView localImageView = (ImageView)paramView.findViewById(paramInt1);
    if (localResource != null)
    {
      localImageView.setTag(Integer.valueOf(paramInt2 + paramInt3));
      localImageView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          Integer localInteger = (Integer)paramView.getTag();
          if (localInteger == null);
          while (true)
          {
            return;
            ((ResourceListActivity)ResourceAdapter.this.mContext).startDetailActivityForResource(localInteger.intValue());
          }
        }
      });
      localImageView.setVisibility(0);
      View localView2 = (View)localImageView.getParent();
      localView2.setVisibility(0);
      setResourceFlag(localView2, localResource);
      bindText(paramView, paramInt4, localResource, "m_title");
    }
    while (true)
    {
      return;
      ((View)localImageView.getParent()).setVisibility(4);
      View localView1 = paramView.findViewById(paramInt4);
      if (localView1 == null)
        continue;
      localView1.setVisibility(4);
    }
  }

  private void bindText(View paramView, int paramInt, Resource paramResource, String paramString)
  {
    TextView localTextView = (TextView)paramView.findViewById(paramInt);
    if (localTextView != null)
    {
      localTextView.setText(paramResource.getInformation().getString(paramString));
      localTextView.setVisibility(0);
    }
  }

  private void initPlayer()
  {
    this.mBatchPlayer = new BatchMediaPlayer(this.mContext);
    this.mBatchPlayer.setListener(new BatchMediaPlayer.BatchPlayerListener()
    {
      public void finish(boolean paramBoolean)
      {
        if (ResourceAdapter.this.mCurrentPlayingButton != null)
        {
          ResourceAdapter.this.mCurrentPlayingButton.setImageResource(50462937);
          if (ResourceAdapter.this.mShowRingtoneName)
          {
            View localView = (View)ResourceAdapter.this.mCurrentPlayingButton.getParent();
            ResourceAdapter.this.bindText(localView, 51052612, ResourceAdapter.this.mCurrentPlayingResource, "m_title");
          }
        }
        if (paramBoolean)
          Toast.makeText(ResourceAdapter.this.mContext, 51118112, 0).show();
        ResourceAdapter.access$002(ResourceAdapter.this, null);
        ResourceAdapter.access$202(ResourceAdapter.this, null);
      }

      public void play(String paramString, int paramInt1, int paramInt2)
      {
        if ((ResourceAdapter.this.mShowRingtoneName) && (ResourceAdapter.this.mCurrentPlayingButton != null))
        {
          TextView localTextView = (TextView)(TextView)((View)ResourceAdapter.this.mCurrentPlayingButton.getParent()).findViewById(51052612);
          if (localTextView != null)
            localTextView.setText(ResourceAdapter.this.getFormatPlayingRingtoneName(paramString, paramInt1, paramInt2));
        }
      }
    });
  }

  private void playMusic(ImageView paramImageView, Resource paramResource)
  {
    paramImageView.setImageResource(50462940);
    this.mCurrentPlayingResource = paramResource;
    this.mCurrentPlayingButton = paramImageView;
    if (this.mBatchPlayer == null)
      initPlayer();
    this.mBatchPlayer.setPlayList(getMusicPlayList(paramResource));
    this.mBatchPlayer.start();
  }

  private void setPlayerDataSource(MediaPlayer paramMediaPlayer, Resource paramResource)
  {
    try
    {
      String str1 = paramResource.getLocalPath();
      String str2 = paramResource.getOnlinePath();
      Uri localUri = ResourceHelper.getUriByPath(str1);
      if (new File(ResourceHelper.getPathByUri(this.mContext, localUri)).exists())
        paramMediaPlayer.setDataSource(this.mContext, localUri);
      else if (str2 != null)
        paramMediaPlayer.setDataSource(this.mContext, Uri.parse(str2));
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
    }
    catch (IllegalStateException localIllegalStateException)
    {
      localIllegalStateException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  private void setResourceFlag(View paramView, Resource paramResource)
  {
    if (paramResource != null)
      ((ImageView)paramView.findViewById(51052591)).setImageResource(getFlagId(paramResource));
  }

  private void setThumbnail(ImageView paramImageView, Resource paramResource, int paramInt, List<Object> paramList, boolean paramBoolean)
  {
    if (this.mResourceSetCategory == 2)
      if (paramResource.equals(this.mCurrentPlayingResource))
      {
        paramImageView.setImageResource(50462940);
        paramImageView.setOnClickListener(new View.OnClickListener(paramResource, paramImageView)
        {
          public void onClick(View paramView)
          {
            if (!this.val$data.equals(ResourceAdapter.this.mCurrentPlayingResource));
            for (int i = 1; ; i = 0)
            {
              ResourceAdapter.this.stopMusic();
              if ((i != 0) && (!TextUtils.isEmpty(this.val$data.getLocalPath())))
                ResourceAdapter.this.playMusic(this.val$view, this.val$data);
              return;
            }
          }
        });
      }
    while (true)
    {
      return;
      paramImageView.setImageResource(50462937);
      break;
      if ((paramList == null) || (paramList.isEmpty()))
      {
        if (paramBoolean)
        {
          paramImageView.setImageResource(50462774);
          continue;
        }
        paramImageView.setImageBitmap(null);
        continue;
      }
      paramImageView.setImageBitmap((Bitmap)paramList.get(paramInt));
    }
  }

  protected View bindContentView(View paramView, List<Resource> paramList, int paramInt)
  {
    if (paramView == null)
      paramView = this.mInflater.inflate(ResourceHelper.getViewResource(this.mDisplayType), null);
    if (ResourceHelper.isCombineView(this.mDisplayType))
    {
      paramView.setEnabled(false);
      bindCombineView(paramView, 51052605, paramList, paramInt, 0, 51052608);
      bindCombineView(paramView, 51052606, paramList, paramInt, 1, 51052609);
      bindCombineView(paramView, 51052607, paramList, paramInt, 2, 51052610);
    }
    while (true)
    {
      paramView.setDrawingCacheEnabled(true);
      return paramView;
      Resource localResource = (Resource)paramList.get(0);
      setResourceFlag(paramView, localResource);
      bindText(paramView, 51052612, localResource, "m_title");
    }
  }

  protected void bindPartialContentView(View paramView, Resource paramResource, int paramInt, List<Object> paramList)
  {
    if (ResourceHelper.isMultipleView(this.mDisplayType))
    {
      setThumbnail((ImageView)paramView.findViewById(51052605), paramResource, 0, paramList, true);
      setThumbnail((ImageView)paramView.findViewById(51052606), paramResource, 1, paramList, false);
      setThumbnail((ImageView)paramView.findViewById(51052607), paramResource, 2, paramList, false);
    }
    while (true)
    {
      return;
      switch (paramInt % getDataPerLine())
      {
      default:
        break;
      case 0:
        setThumbnail((ImageView)paramView.findViewById(51052605), paramResource, 0, paramList, true);
        break;
      case 1:
        setThumbnail((ImageView)paramView.findViewById(51052606), paramResource, 0, paramList, true);
        break;
      case 2:
        setThumbnail((ImageView)paramView.findViewById(51052607), paramResource, 0, paramList, true);
      }
    }
  }

  protected boolean checkResourceModifyTime()
  {
    return true;
  }

  protected List<Object> getCacheKeys(Resource paramResource)
  {
    ArrayList localArrayList = new ArrayList();
    if (ResourceHelper.isMultipleView(this.mDisplayType));
    for (int i = 3; ; i = 1)
      for (int j = 0; j < i; j++)
      {
        if (paramResource.getLocalThumbnail(j) == null)
          continue;
        localArrayList.add(paramResource.getLocalThumbnail(j));
      }
    return localArrayList;
  }

  protected int getFlagId(Resource paramResource)
  {
    int i = paramResource.getStatus();
    int j;
    if (i == 1)
      j = 50462779;
    while (true)
    {
      return j;
      if (i == 0)
      {
        if (paramResource.getLocalPath().equals(this.mCurrentUsingPath))
        {
          j = 50462780;
          continue;
        }
        j = 50462778;
        continue;
      }
      j = 0;
    }
  }

  protected String getFormatPlayingRingtoneName(String paramString, int paramInt1, int paramInt2)
  {
    return ResourceHelper.getDefaultFormatPlayingRingtoneName(paramString, paramInt1, paramInt2);
  }

  protected AsyncAdapter<Resource>.AsyncLoadPartialDataTask getLoadPartialDataTask()
  {
    AsyncImageAdapter.AsyncLoadImageTask localAsyncLoadImageTask = new AsyncImageAdapter.AsyncLoadImageTask(this);
    localAsyncLoadImageTask.setJobPool(new DaemonAsyncTask.StackJobPool());
    localAsyncLoadImageTask.setTargetSize(this.mThumbnailWidth, this.mThumbnailHeight);
    if (this.mDisplayType != 5);
    for (boolean bool = true; ; bool = false)
    {
      localAsyncLoadImageTask.setScaled(bool);
      return localAsyncLoadImageTask;
    }
  }

  protected List<String> getMusicPlayList(Resource paramResource)
  {
    return ResourceHelper.getDefaultMusicPlayList(this.mContext, paramResource);
  }

  public ResourceSet getResourceSet()
  {
    return this.mResourceSet;
  }

  protected boolean isValidKey(Object paramObject, Resource paramResource, int paramInt)
  {
    boolean bool = false;
    File localFile = new File((String)paramObject);
    if (!localFile.exists());
    while (true)
    {
      return bool;
      if ((checkResourceModifyTime()) && (localFile.lastModified() < paramResource.getModifiedTime()))
      {
        localFile.delete();
        continue;
      }
      bool = super.isValidKey(paramObject, paramResource, paramInt);
    }
  }

  public void refreshDataSet()
  {
    this.mResourceSetPackage = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_PACKAGE");
    this.mResourceSetSubpackage = this.mMetaData.getString("com.miui.android.resourcebrowser.RESOURCE_SET_SUBPACKAGE");
    this.mResourceSet = ResourceSet.getInstance(this.mResourceSetPackage + this.mResourceSetSubpackage);
    setDataSet(this.mResourceSet);
  }

  public void setCurrentUsingPath(String paramString)
  {
    this.mCurrentUsingPath = paramString;
    this.mMetaData.putString("com.miui.android.resourcebrowser.CURRENT_USING_PATH", this.mCurrentUsingPath);
  }

  public void stopMusic()
  {
    if (this.mBatchPlayer != null)
      this.mBatchPlayer.stop();
    this.mCurrentPlayingResource = null;
    this.mCurrentPlayingButton = null;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceAdapter
 * JD-Core Version:    0.6.0
 */