package miui.app.resourcebrowser;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import java.util.ArrayList;
import java.util.List;

public class BatchMediaPlayer
{
  private Activity mActivity = null;
  private final Runnable mBatchPlayRun = new Runnable()
  {
    public void run()
    {
      BatchMediaPlayer.this.setPlayerDataSource();
    }
  };
  private int mCurrentItem = -1;
  private BatchPlayerListener mListener = null;
  private ArrayList<String> mPlayList = new ArrayList();
  private MediaPlayer mPlayer = null;
  private PlayState mState = PlayState.UNDEFINED;
  private Handler mhandler = new Handler();

  public BatchMediaPlayer(Activity paramActivity)
  {
    if (paramActivity == null)
      throw new IllegalArgumentException("activity cann't be null");
    this.mActivity = paramActivity;
  }

  private void realPlay()
  {
    if ((!isPaused()) && (this.mPlayer != null))
    {
      if (this.mListener != null)
        this.mListener.play((String)this.mPlayList.get(this.mCurrentItem), this.mCurrentItem, size());
      this.mPlayer.start();
      this.mState = PlayState.PLAYING;
    }
  }

  private void setPlayerDataSource()
  {
    try
    {
      int i = 1 + this.mCurrentItem;
      this.mCurrentItem = i;
      if (i < this.mPlayList.size())
      {
        this.mPlayer.reset();
        this.mPlayer.setDataSource(this.mActivity, ResourceHelper.getUriByPath((String)this.mPlayList.get(this.mCurrentItem)));
        this.mPlayer.prepareAsync();
      }
      else
      {
        stop(false);
      }
    }
    catch (Exception localException)
    {
    }
  }

  public void addPlayUri(String paramString)
  {
    this.mPlayList.add(paramString);
  }

  public boolean isPaused()
  {
    if (this.mState == PlayState.PAUSED);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean isPlaying()
  {
    if (this.mState == PlayState.PLAYING);
    for (int i = 1; ; i = 0)
      return i;
  }

  public void pause()
  {
    if (this.mPlayer != null)
      this.mPlayer.pause();
    if (isPlaying())
      this.mState = PlayState.PAUSED;
  }

  public void setListener(BatchPlayerListener paramBatchPlayerListener)
  {
    this.mListener = paramBatchPlayerListener;
  }

  public void setPlayList(List<String> paramList)
  {
    this.mPlayList.clear();
    if (paramList != null)
      this.mPlayList.addAll(paramList);
  }

  public int size()
  {
    return this.mPlayList.size();
  }

  public void start()
  {
    if (this.mPlayer != null)
    {
      this.mState = PlayState.PLAYING;
      realPlay();
    }
    while (true)
    {
      return;
      this.mPlayer = new MediaPlayer();
      this.mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener()
      {
        public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
        {
          BatchMediaPlayer.this.stop(true);
          return true;
        }
      });
      this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
      {
        public void onCompletion(MediaPlayer paramMediaPlayer)
        {
          long l = Math.max(Math.min(1000, 2000 - paramMediaPlayer.getDuration()), 500L);
          BatchMediaPlayer.this.mhandler.removeCallbacks(BatchMediaPlayer.this.mBatchPlayRun);
          BatchMediaPlayer.this.mhandler.postDelayed(BatchMediaPlayer.this.mBatchPlayRun, l);
        }
      });
      this.mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
      {
        public void onPrepared(MediaPlayer paramMediaPlayer)
        {
          if (BatchMediaPlayer.this.mActivity.hasWindowFocus())
          {
            paramMediaPlayer.seekTo(0);
            BatchMediaPlayer.this.realPlay();
          }
          while (true)
          {
            return;
            BatchMediaPlayer.this.stop(false);
          }
        }
      });
      this.mPlayer.setAudioStreamType(this.mActivity.getVolumeControlStream());
      setPlayerDataSource();
    }
  }

  public void stop()
  {
    stop(false);
  }

  public void stop(boolean paramBoolean)
  {
    if (this.mPlayer != null)
    {
      this.mPlayer.setOnPreparedListener(null);
      if (this.mPlayer.isPlaying())
        this.mPlayer.stop();
      this.mPlayer.release();
      this.mPlayer = null;
      if (this.mListener != null)
        this.mListener.finish(paramBoolean);
    }
    this.mCurrentItem = -1;
    this.mState = PlayState.UNDEFINED;
  }

  static abstract interface BatchPlayerListener
  {
    public abstract void finish(boolean paramBoolean);

    public abstract void play(String paramString, int paramInt1, int paramInt2);
  }

  private static enum PlayState
  {
    static
    {
      PLAYING = new PlayState("PLAYING", 1);
      PAUSED = new PlayState("PAUSED", 2);
      PlayState[] arrayOfPlayState = new PlayState[3];
      arrayOfPlayState[0] = UNDEFINED;
      arrayOfPlayState[1] = PLAYING;
      arrayOfPlayState[2] = PAUSED;
      $VALUES = arrayOfPlayState;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.BatchMediaPlayer
 * JD-Core Version:    0.6.0
 */