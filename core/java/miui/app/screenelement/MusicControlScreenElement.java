package miui.app.screenelement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;

public class MusicControlScreenElement extends ElementGroup
  implements ButtonScreenElement.ButtonActionListener
{
  private static final String BUTTON_MUSIC_ALBUM_COVER = "music_album_cover";
  private static final String BUTTON_MUSIC_DISPLAY = "music_display";
  private static final String BUTTON_MUSIC_NEXT = "music_next";
  private static final String BUTTON_MUSIC_PAUSE = "music_pause";
  private static final String BUTTON_MUSIC_PLAY = "music_play";
  private static final String BUTTON_MUSIC_PREV = "music_prev";
  private static final int CHECK_STREAM_MUSIC_DELAY = 1000;
  private static final String LOG_TAG = "MusicControlScreenElement";
  private static final int MUSIC_SHOW_NONE = 0;
  private static final int MUSIC_SHOW_PLAY = 2;
  private static final int MUSIC_SHOW_STOP = 1;
  public static final String TAG_NAME = "MusicControl";
  private boolean isPaused = false;
  private Bitmap mAlbumCoverBm;
  private String mAlbumName;
  private String mArtistName;
  private AudioManager mAudioManager;
  private boolean mAutoShow;
  private ButtonScreenElement mButtonNext;
  private ButtonScreenElement mButtonPause;
  private ButtonScreenElement mButtonPlay;
  private ButtonScreenElement mButtonPrev;
  private Runnable mCheckStreamMusicRunnable = new Runnable()
  {
    public void run()
    {
      MusicControlScreenElement.this.updateMusic();
      MusicControlScreenElement.this.updateSpectrumVisualizer();
      MusicControlScreenElement.this.mHandler.postDelayed(this, 1000L);
    }
  };
  private Bitmap mDefaultAlbumCoverBm;
  private Runnable mEnableSpectrumVisualizerRunnable = new Runnable()
  {
    public void run()
    {
      MusicControlScreenElement.this.mHandler.removeCallbacks(MusicControlScreenElement.this.mEnableSpectrumVisualizerRunnable);
      MusicControlScreenElement.this.updateSpectrumVisualizer();
    }
  };
  private final Handler mHandler = new Handler();
  private ImageScreenElement mImageAlbumCover;
  private boolean mIsOnlineSongBlocking;
  private IndexedNumberVariable mMusicStateVar;
  private int mMusicStatus;
  private BroadcastReceiver mPlayerStatusListener = new BroadcastReceiver()
  {
    private void setTrackInfo(Intent paramIntent)
    {
      if (MusicControlScreenElement.this.mTextDisplay == null);
      String str1;
      String str2;
      while (true)
      {
        return;
        str1 = paramIntent.getStringExtra("track");
        str2 = paramIntent.getStringExtra("artist");
        MusicControlScreenElement.access$302(MusicControlScreenElement.this, false);
        if ((!TextUtils.isEmpty(str1)) || (!TextUtils.isEmpty(str2)))
          break;
        MusicControlScreenElement.this.mTextDisplay.show(false);
      }
      if (TextUtils.isEmpty(str1))
        MusicControlScreenElement.this.mTextDisplay.setText(str2);
      while (true)
      {
        MusicControlScreenElement.this.mTextDisplay.show(true);
        break;
        if (TextUtils.isEmpty(str2))
        {
          MusicControlScreenElement.this.mTextDisplay.setText(str1);
          continue;
        }
        TextScreenElement localTextScreenElement = MusicControlScreenElement.this.mTextDisplay;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = str1;
        arrayOfObject[1] = str2;
        localTextScreenElement.setText(String.format("%s   %s", arrayOfObject));
      }
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str1 = paramIntent.getAction();
      if (!paramIntent.getBooleanExtra("playing", false));
      while (true)
      {
        return;
        if ("com.miui.player.metachanged".equals(str1))
        {
          String str2 = paramIntent.getStringExtra("other");
          if ("meta_changed_track".equals(str2))
          {
            setTrackInfo(paramIntent);
            MusicControlScreenElement.this.requestAlbum(paramIntent);
            continue;
          }
          if (!"meta_changed_album".equals(str2))
            continue;
          MusicControlScreenElement.this.requestAlbum(paramIntent, true);
          continue;
        }
        if ("lockscreen.action.SONG_METADATA_UPDATED".equals(str1))
        {
          setTrackInfo(paramIntent);
          MusicControlScreenElement.this.setAlbumCover(paramIntent);
          continue;
        }
        if ("com.miui.player.refreshprogress".equals(str1))
        {
          MusicControlScreenElement.access$302(MusicControlScreenElement.this, paramIntent.getBooleanExtra("block", false));
          continue;
        }
        if (!"com.miui.player.playstatechanged".equals(str1))
          continue;
        if ((MusicControlScreenElement.this.mTextDisplay != null) && (!MusicControlScreenElement.this.mTextDisplay.isVisible()))
          setTrackInfo(paramIntent);
        MusicControlScreenElement.this.requestAlbum(paramIntent);
      }
    }
  };
  private SpectrumVisualizerScreenElement mSpectrumVisualizer;
  private TextScreenElement mTextDisplay;
  private IndexedNumberVariable mVisibilityVar;

  public MusicControlScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
    {
      ScreenElement localScreenElement = (ScreenElement)localIterator.next();
      if (localScreenElement.getName().equals("music_prev"))
      {
        this.mButtonPrev = ((ButtonScreenElement)localScreenElement);
        continue;
      }
      if (localScreenElement.getName().equals("music_next"))
      {
        this.mButtonNext = ((ButtonScreenElement)localScreenElement);
        continue;
      }
      if (localScreenElement.getName().equals("music_play"))
      {
        this.mButtonPlay = ((ButtonScreenElement)localScreenElement);
        continue;
      }
      if (localScreenElement.getName().equals("music_pause"))
      {
        this.mButtonPause = ((ButtonScreenElement)localScreenElement);
        continue;
      }
      if (localScreenElement.getName().equals("music_display"))
      {
        this.mTextDisplay = ((TextScreenElement)localScreenElement);
        continue;
      }
      if (localScreenElement.getName().equals("music_album_cover"))
      {
        this.mImageAlbumCover = ((ImageScreenElement)localScreenElement);
        continue;
      }
      if (!(localScreenElement instanceof SpectrumVisualizerScreenElement))
        continue;
      this.mSpectrumVisualizer = ((SpectrumVisualizerScreenElement)localScreenElement);
    }
    if ((this.mButtonPrev == null) || (this.mButtonNext == null) || (this.mButtonPlay == null) || (this.mButtonPause == null))
      throw new ScreenElementLoadException("invalid music control");
    setupButton(this.mButtonPrev);
    setupButton(this.mButtonNext);
    setupButton(this.mButtonPlay);
    setupButton(this.mButtonPause);
    this.mButtonPause.show(false);
    String str;
    if (this.mImageAlbumCover != null)
    {
      str = paramElement.getAttribute("defAlbumCover");
      if (TextUtils.isEmpty(str))
        break label456;
    }
    label456: for (this.mDefaultAlbumCoverBm = this.mContext.mResourceManager.getBitmap(str); ; this.mDefaultAlbumCoverBm = BitmapFactory.decodeResource(this.mContext.mContext.getResources(), 50462889))
    {
      this.mAutoShow = Boolean.parseBoolean(paramElement.getAttribute("autoShow"));
      this.mAudioManager = ((AudioManager)paramScreenContext.mContext.getSystemService("audio"));
      if (this.mHasName)
      {
        this.mMusicStateVar = new IndexedNumberVariable(this.mName, "music_state", paramScreenContext.mVariables);
        this.mVisibilityVar = new IndexedNumberVariable(this.mName, "visibility", paramScreenContext.mVariables);
      }
      return;
    }
  }

  private int getKeyCode(String paramString)
  {
    int i;
    if ("music_prev".equals(paramString))
      i = 88;
    while (true)
    {
      return i;
      if ("music_next".equals(paramString))
      {
        i = 87;
        continue;
      }
      if (("music_play".equals(paramString)) || ("music_pause".equals(paramString)))
      {
        i = 85;
        continue;
      }
      i = -1;
    }
  }

  private void requestAlbum()
  {
    if (this.mImageAlbumCover == null);
    while (true)
    {
      return;
      Intent localIntent = new Intent("lockscreen.action.SONG_METADATA_REQUEST");
      this.mContext.mContext.sendBroadcast(localIntent);
    }
  }

  private void requestAlbum(Intent paramIntent)
  {
    requestAlbum(paramIntent, false);
  }

  private void requestAlbum(Intent paramIntent, boolean paramBoolean)
  {
    if (this.mImageAlbumCover == null);
    while (true)
    {
      return;
      String str1 = paramIntent.getStringExtra("album");
      String str2 = paramIntent.getStringExtra("artist");
      if ((!paramBoolean) && (Utils.equals(str1, this.mAlbumName)) && (Utils.equals(str2, this.mArtistName)) && (this.mAlbumCoverBm != null))
        continue;
      Uri localUri = (Uri)paramIntent.getParcelableExtra("album_uri");
      String str3 = paramIntent.getStringExtra("album_path");
      this.mAlbumCoverBm = null;
      if ((localUri != null) || (str3 != null))
      {
        requestAlbum();
        continue;
      }
      this.mImageAlbumCover.setBitmap(this.mDefaultAlbumCoverBm);
    }
  }

  private void sendMediaButtonBroadcast(int paramInt1, int paramInt2)
  {
    long l = SystemClock.uptimeMillis();
    KeyEvent localKeyEvent = new KeyEvent(l, l, paramInt1, paramInt2, 0);
    Intent localIntent = new Intent("android.intent.action.MEDIA_BUTTON", null);
    localIntent.putExtra("android.intent.extra.KEY_EVENT", KeyEvent.changeFlags(localKeyEvent, 8));
    localIntent.putExtra("forbid_double_click", false);
    this.mContext.mContext.sendOrderedBroadcast(localIntent, null);
  }

  private void setAlbumCover(Intent paramIntent)
  {
    if (this.mImageAlbumCover == null);
    ImageScreenElement localImageScreenElement1;
    while (true)
    {
      return;
      this.mAlbumName = paramIntent.getStringExtra("album");
      this.mArtistName = paramIntent.getStringExtra("artist");
      try
      {
        String str = paramIntent.getStringExtra("tmp_album_path");
        if (str != null)
          this.mAlbumCoverBm = BitmapFactory.decodeFile(str);
        ImageScreenElement localImageScreenElement3 = this.mImageAlbumCover;
        if (this.mAlbumCoverBm != null);
        for (Bitmap localBitmap3 = this.mAlbumCoverBm; ; localBitmap3 = this.mDefaultAlbumCoverBm)
        {
          localImageScreenElement3.setBitmap(localBitmap3);
          this.mContext.mShouldUpdate = true;
          break;
        }
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        Log.e("MusicControlScreenElement", "failed to load album cover bitmap: " + localOutOfMemoryError.toString());
        ImageScreenElement localImageScreenElement2 = this.mImageAlbumCover;
        if (this.mAlbumCoverBm != null);
        for (Bitmap localBitmap2 = this.mAlbumCoverBm; ; localBitmap2 = this.mDefaultAlbumCoverBm)
        {
          localImageScreenElement2.setBitmap(localBitmap2);
          this.mContext.mShouldUpdate = true;
          break;
        }
      }
      finally
      {
        localImageScreenElement1 = this.mImageAlbumCover;
        if (this.mAlbumCoverBm == null);
      }
    }
    for (Bitmap localBitmap1 = this.mAlbumCoverBm; ; localBitmap1 = this.mDefaultAlbumCoverBm)
    {
      localImageScreenElement1.setBitmap(localBitmap1);
      this.mContext.mShouldUpdate = true;
      throw localObject;
    }
  }

  private void setupButton(ButtonScreenElement paramButtonScreenElement)
  {
    if (paramButtonScreenElement != null)
    {
      paramButtonScreenElement.setListener(this);
      paramButtonScreenElement.setParent(this);
    }
  }

  private void updateMusic()
  {
    boolean bool1 = false;
    boolean bool2 = this.mAudioManager.isMusicActive();
    boolean bool3;
    if (!bool2)
    {
      bool3 = true;
      if (this.mIsOnlineSongBlocking)
        bool3 = false;
      this.mButtonPlay.show(bool3);
      ButtonScreenElement localButtonScreenElement = this.mButtonPause;
      if (!bool3)
        bool1 = true;
      localButtonScreenElement.show(bool1);
      switch (this.mMusicStatus)
      {
      default:
      case 2:
      case 1:
      }
    }
    while (true)
    {
      return;
      bool3 = false;
      break;
      if (bool2)
        continue;
      this.mMusicStatus = 1;
      if (!this.mHasName)
        continue;
      this.mMusicStateVar.set(0.0D);
      continue;
      if (!bool2)
        continue;
      this.mMusicStatus = 2;
      if (!this.mHasName)
        continue;
      this.mMusicStateVar.set(1.0D);
    }
  }

  public void finish()
  {
    this.mHandler.removeCallbacks(this.mCheckStreamMusicRunnable);
    this.mContext.mContext.unregisterReceiver(this.mPlayerStatusListener);
  }

  public void init()
  {
    super.init();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.miui.player.metachanged");
    localIntentFilter.addAction("lockscreen.action.SONG_METADATA_UPDATED");
    localIntentFilter.addAction("com.miui.player.refreshprogress");
    localIntentFilter.addAction("com.miui.player.playstatechanged");
    this.mContext.mContext.registerReceiver(this.mPlayerStatusListener, localIntentFilter, null, this.mHandler);
    boolean bool = this.mAudioManager.isMusicActive();
    if (bool)
    {
      this.mMusicStatus = 2;
      Intent localIntent = new Intent("lockscreen.action.SONG_METADATA_REQUEST");
      this.mContext.mContext.sendBroadcast(localIntent);
      if (this.mAutoShow)
        show(true);
    }
    IndexedNumberVariable localIndexedNumberVariable;
    double d;
    if (this.mHasName)
    {
      localIndexedNumberVariable = this.mMusicStateVar;
      if (!bool)
        break label142;
      d = 1.0D;
    }
    while (true)
    {
      localIndexedNumberVariable.set(d);
      return;
      label142: d = 0.0D;
    }
  }

  public boolean onButtonDoubleClick(String paramString)
  {
    return false;
  }

  public boolean onButtonDown(String paramString)
  {
    int i = getKeyCode(paramString);
    if (i == -1);
    while (true)
    {
      return false;
      sendMediaButtonBroadcast(0, i);
    }
  }

  public boolean onButtonLongClick(String paramString)
  {
    return false;
  }

  public boolean onButtonUp(String paramString)
  {
    int i = 1;
    int j = getKeyCode(paramString);
    if (j == -1)
      i = 0;
    while (true)
    {
      return i;
      sendMediaButtonBroadcast(i, j);
      this.mHandler.post(new Runnable(paramString)
      {
        public void run()
        {
          if ("music_pause".equals(this.val$_name))
          {
            MusicControlScreenElement.this.mButtonPause.show(false);
            MusicControlScreenElement.this.mButtonPlay.show(true);
            MusicControlScreenElement.access$1002(MusicControlScreenElement.this, 2);
            if (MusicControlScreenElement.this.mHasName)
              MusicControlScreenElement.this.mMusicStateVar.set(1.0D);
          }
          while (true)
          {
            MusicControlScreenElement.this.mHandler.removeCallbacks(MusicControlScreenElement.this.mCheckStreamMusicRunnable);
            MusicControlScreenElement.this.mHandler.postDelayed(MusicControlScreenElement.this.mCheckStreamMusicRunnable, 3000L);
            MusicControlScreenElement.this.mHandler.removeCallbacks(MusicControlScreenElement.this.mEnableSpectrumVisualizerRunnable);
            MusicControlScreenElement.this.mHandler.postDelayed(MusicControlScreenElement.this.mEnableSpectrumVisualizerRunnable, 500L);
            return;
            if (!"music_play".equals(this.val$_name))
              continue;
            MusicControlScreenElement.this.mButtonPlay.show(false);
            MusicControlScreenElement.this.mButtonPause.show(true);
            MusicControlScreenElement.access$1002(MusicControlScreenElement.this, 1);
            if (!MusicControlScreenElement.this.mHasName)
              continue;
            MusicControlScreenElement.this.mMusicStateVar.set(0.0D);
          }
        }
      });
    }
  }

  public void pause()
  {
    super.pause();
    this.isPaused = true;
    this.mHandler.removeCallbacks(this.mCheckStreamMusicRunnable);
    if (this.mSpectrumVisualizer != null)
      this.mSpectrumVisualizer.enableUpdate(false);
  }

  public void resume()
  {
    super.pause();
    this.isPaused = false;
    this.mHandler.postDelayed(this.mCheckStreamMusicRunnable, 1000L);
  }

  public void show(boolean paramBoolean)
  {
    super.show(paramBoolean);
    IndexedNumberVariable localIndexedNumberVariable;
    double d;
    if (!paramBoolean)
    {
      this.mMusicStatus = 0;
      this.mHandler.removeCallbacks(this.mCheckStreamMusicRunnable);
      if (this.mSpectrumVisualizer != null)
        this.mSpectrumVisualizer.enableUpdate(false);
      if (this.mHasName)
      {
        localIndexedNumberVariable = this.mVisibilityVar;
        if (!paramBoolean)
          break label88;
        d = 1.0D;
      }
    }
    while (true)
    {
      localIndexedNumberVariable.set(d);
      return;
      updateMusic();
      this.mHandler.postDelayed(this.mCheckStreamMusicRunnable, 1000L);
      break;
      label88: d = 0.0D;
    }
  }

  protected void updateSpectrumVisualizer()
  {
    boolean bool1 = this.mAudioManager.isMusicActive();
    SpectrumVisualizerScreenElement localSpectrumVisualizerScreenElement;
    if (this.mSpectrumVisualizer != null)
    {
      localSpectrumVisualizerScreenElement = this.mSpectrumVisualizer;
      if ((!bool1) || (!isVisible()) || (this.isPaused))
        break label46;
    }
    label46: for (boolean bool2 = true; ; bool2 = false)
    {
      localSpectrumVisualizerScreenElement.enableUpdate(bool2);
      return;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.MusicControlScreenElement
 * JD-Core Version:    0.6.0
 */