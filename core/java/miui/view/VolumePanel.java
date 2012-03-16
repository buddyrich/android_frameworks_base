package miui.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.AudioService;
import android.media.AudioSystem;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.util.HashMap;

public class VolumePanel extends Handler
  implements SeekBar.OnSeekBarChangeListener, View.OnClickListener
{
  private static final int BEEP_DURATION = 150;
  private static final int FREE_DELAY = 10000;
  private static boolean LOGD = false;
  private static final int MAX_VOLUME = 100;
  private static final int MSG_FREE_RESOURCES = 1;
  private static final int MSG_PLAY_SOUND = 2;
  private static final int MSG_RINGER_MODE_CHANGED = 6;
  private static final int MSG_STOP_SOUNDS = 3;
  private static final int MSG_TIMEOUT = 5;
  private static final int MSG_VIBRATE = 4;
  private static final int MSG_VOLUME_CHANGED = 0;
  public static final int PLAY_SOUND_DELAY = 300;
  private static final StreamResources[] STREAMS;
  private static final String TAG = "VolumePanel";
  private static final int TIMEOUT_DELAY = 3000;
  public static final int VIBRATE_DELAY = 300;
  private static final int VIBRATE_DURATION = 300;
  private int mActiveStreamType = -1;
  private AudioManager mAudioManager;
  protected AudioService mAudioService;
  protected Context mContext;
  private final Dialog mDialog;
  private final View mDivider;
  private final View mMoreButton;
  private final ViewGroup mPanel;
  private boolean mRingIsSilent;
  private boolean mShowCombinedVolumes;
  private final ViewGroup mSliderGroup;
  private HashMap<Integer, StreamControl> mStreamControls;
  private ToneGenerator[] mToneGenerators;
  private Vibrator mVibrator;
  private final View mView;
  private boolean mVoiceCapable;

  static
  {
    StreamResources[] arrayOfStreamResources = new StreamResources[6];
    arrayOfStreamResources[0] = StreamResources.BluetoothSCOStream;
    arrayOfStreamResources[1] = StreamResources.RingerStream;
    arrayOfStreamResources[2] = StreamResources.VoiceStream;
    arrayOfStreamResources[3] = StreamResources.MediaStream;
    arrayOfStreamResources[4] = StreamResources.NotificationStream;
    arrayOfStreamResources[5] = StreamResources.AlarmStream;
    STREAMS = arrayOfStreamResources;
  }

  public VolumePanel(Context paramContext, AudioService paramAudioService)
  {
    this.mContext = paramContext;
    this.mAudioManager = ((AudioManager)paramContext.getSystemService("audio"));
    this.mAudioService = paramAudioService;
    this.mView = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(50528296, null);
    this.mView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        VolumePanel.this.resetTimeout();
        return false;
      }
    });
    this.mPanel = ((ViewGroup)this.mView.findViewById(51052557));
    this.mSliderGroup = ((ViewGroup)this.mView.findViewById(51052558));
    this.mMoreButton = ((ImageView)this.mView.findViewById(51052559));
    this.mDivider = ((ImageView)this.mView.findViewById(51052560));
    this.mDialog = new Dialog(paramContext, 51183653)
    {
      public boolean onTouchEvent(MotionEvent paramMotionEvent)
      {
        if ((isShowing()) && (paramMotionEvent.getAction() == 4))
          VolumePanel.this.forceTimeout();
        for (int i = 1; ; i = 0)
          return i;
      }
    };
    this.mDialog.setTitle("Volume control");
    this.mDialog.setContentView(this.mView);
    this.mDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
    {
      public void onDismiss(DialogInterface paramDialogInterface)
      {
        VolumePanel.access$202(VolumePanel.this, -1);
        VolumePanel.this.mAudioManager.forceVolumeControlStream(VolumePanel.this.mActiveStreamType);
      }
    });
    Window localWindow = this.mDialog.getWindow();
    localWindow.setGravity(48);
    WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
    localLayoutParams.token = null;
    localLayoutParams.y = this.mContext.getResources().getDimensionPixelOffset(17104964);
    localLayoutParams.type = 2020;
    localLayoutParams.width = -2;
    localLayoutParams.height = -2;
    localWindow.setAttributes(localLayoutParams);
    localWindow.addFlags(262184);
    this.mToneGenerators = new ToneGenerator[AudioSystem.getNumStreamTypes()];
    this.mVibrator = new Vibrator();
    this.mVoiceCapable = paramContext.getResources().getBoolean(50921481);
    boolean bool;
    if (!this.mVoiceCapable)
    {
      bool = true;
      this.mShowCombinedVolumes = bool;
      if (this.mShowCombinedVolumes)
        break label345;
      this.mMoreButton.setVisibility(8);
      this.mDivider.setVisibility(8);
    }
    while (true)
    {
      listenToRingerMode();
      return;
      bool = false;
      break;
      label345: this.mMoreButton.setOnClickListener(this);
    }
  }

  private void addOtherVolumes()
  {
    if (!this.mShowCombinedVolumes)
      return;
    int i = 0;
    label10: int j;
    if (i < STREAMS.length)
    {
      j = STREAMS[i].streamType;
      if ((STREAMS[i].show) && (j != this.mActiveStreamType))
        break label52;
    }
    while (true)
    {
      i++;
      break label10;
      break;
      label52: StreamControl localStreamControl = (StreamControl)this.mStreamControls.get(Integer.valueOf(j));
      this.mSliderGroup.addView(localStreamControl.group);
      updateSlider(localStreamControl);
    }
  }

  private void collapse()
  {
    this.mMoreButton.setVisibility(0);
    this.mDivider.setVisibility(0);
    int i = this.mSliderGroup.getChildCount();
    for (int j = 1; j < i; j++)
      this.mSliderGroup.getChildAt(j).setVisibility(8);
  }

  private void createSliders()
  {
    LayoutInflater localLayoutInflater = (LayoutInflater)this.mContext.getSystemService("layout_inflater");
    this.mStreamControls = new HashMap(STREAMS.length);
    Resources localResources = this.mContext.getResources();
    int i = 0;
    if (i < STREAMS.length)
    {
      StreamResources localStreamResources = STREAMS[i];
      int j = localStreamResources.streamType;
      if ((this.mVoiceCapable) && (localStreamResources == StreamResources.NotificationStream))
        localStreamResources = StreamResources.RingerStream;
      StreamControl localStreamControl = new StreamControl(null);
      localStreamControl.streamType = j;
      localStreamControl.group = ((ViewGroup)localLayoutInflater.inflate(50528297, null));
      localStreamControl.group.setTag(localStreamControl);
      localStreamControl.icon = ((ImageView)localStreamControl.group.findViewById(51052561));
      localStreamControl.icon.setTag(localStreamControl);
      localStreamControl.icon.setContentDescription(localResources.getString(localStreamResources.descRes));
      localStreamControl.iconRes = localStreamResources.iconRes;
      localStreamControl.iconMuteRes = localStreamResources.iconMuteRes;
      localStreamControl.icon.setImageResource(localStreamControl.iconRes);
      localStreamControl.seekbarView = ((SeekBar)localStreamControl.group.findViewById(51052562));
      if ((j == 6) || (j == 0));
      for (int k = 1; ; k = 0)
      {
        localStreamControl.seekbarView.setMax(k + this.mAudioManager.getStreamMaxVolume(j));
        localStreamControl.seekbarView.setOnSeekBarChangeListener(this);
        localStreamControl.seekbarView.setTag(localStreamControl);
        this.mStreamControls.put(Integer.valueOf(j), localStreamControl);
        i++;
        break;
      }
    }
  }

  private void expand()
  {
    int i = this.mSliderGroup.getChildCount();
    for (int j = 0; j < i; j++)
      this.mSliderGroup.getChildAt(j).setVisibility(0);
    this.mMoreButton.setVisibility(4);
    this.mDivider.setVisibility(4);
  }

  private void forceTimeout()
  {
    removeMessages(5);
    sendMessage(obtainMessage(5));
  }

  private ToneGenerator getOrCreateToneGenerator(int paramInt)
  {
    monitorenter;
    try
    {
      ToneGenerator localToneGenerator1 = this.mToneGenerators[paramInt];
      if (localToneGenerator1 == null);
      try
      {
        this.mToneGenerators[paramInt] = new ToneGenerator(paramInt, 100);
        ToneGenerator localToneGenerator2 = this.mToneGenerators[paramInt];
        monitorexit;
        return localToneGenerator2;
      }
      catch (RuntimeException localRuntimeException)
      {
        while (true)
        {
          if (!LOGD)
            continue;
          Log.d("VolumePanel", "ToneGenerator constructor failed with RuntimeException: " + localRuntimeException);
        }
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private boolean isExpanded()
  {
    if (this.mMoreButton.getVisibility() != 0);
    for (int i = 1; ; i = 0)
      return i;
  }

  private boolean isMuted(int paramInt)
  {
    return this.mAudioManager.isStreamMute(paramInt);
  }

  private void listenToRingerMode()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.media.RINGER_MODE_CHANGED");
    this.mContext.registerReceiver(new BroadcastReceiver()
    {
      public void onReceive(Context paramContext, Intent paramIntent)
      {
        if ("android.media.RINGER_MODE_CHANGED".equals(paramIntent.getAction()))
        {
          VolumePanel.this.removeMessages(6);
          VolumePanel.this.sendMessage(VolumePanel.this.obtainMessage(6));
        }
      }
    }
    , localIntentFilter);
  }

  private void reorderSliders(int paramInt)
  {
    this.mSliderGroup.removeAllViews();
    StreamControl localStreamControl = (StreamControl)this.mStreamControls.get(Integer.valueOf(paramInt));
    if (localStreamControl == null)
    {
      Log.e("VolumePanel", "Missing stream type! - " + paramInt);
      this.mActiveStreamType = -1;
    }
    while (true)
    {
      addOtherVolumes();
      return;
      this.mSliderGroup.addView(localStreamControl.group);
      this.mActiveStreamType = paramInt;
      localStreamControl.group.setVisibility(0);
      updateSlider(localStreamControl);
    }
  }

  private void resetTimeout()
  {
    removeMessages(5);
    sendMessageDelayed(obtainMessage(5), 3000L);
  }

  private void setMusicIcon(int paramInt1, int paramInt2)
  {
    StreamControl localStreamControl = (StreamControl)this.mStreamControls.get(Integer.valueOf(3));
    ImageView localImageView;
    if (localStreamControl != null)
    {
      localStreamControl.iconRes = paramInt1;
      localStreamControl.iconMuteRes = paramInt2;
      localImageView = localStreamControl.icon;
      if (!isMuted(localStreamControl.streamType))
        break label60;
    }
    label60: for (int i = localStreamControl.iconMuteRes; ; i = localStreamControl.iconRes)
    {
      localImageView.setImageResource(i);
      return;
    }
  }

  private void updateSlider(StreamControl paramStreamControl)
  {
    paramStreamControl.seekbarView.setProgress(this.mAudioManager.getLastAudibleStreamVolume(paramStreamControl.streamType));
    boolean bool = isMuted(paramStreamControl.streamType);
    ImageView localImageView = paramStreamControl.icon;
    if (bool);
    for (int i = paramStreamControl.iconMuteRes; ; i = paramStreamControl.iconRes)
    {
      localImageView.setImageResource(i);
      if ((paramStreamControl.streamType == 2) && (bool) && (this.mAudioManager.shouldVibrate(0)))
        paramStreamControl.icon.setImageResource(50462999);
      return;
    }
  }

  private void updateStates()
  {
    int i = this.mSliderGroup.getChildCount();
    for (int j = 0; j < i; j++)
      updateSlider((StreamControl)this.mSliderGroup.getChildAt(j).getTag());
  }

  public void handleMessage(Message paramMessage)
  {
    switch (paramMessage.what)
    {
    default:
    case 0:
    case 1:
    case 3:
    case 2:
    case 4:
    case 5:
    case 6:
    }
    while (true)
    {
      return;
      onVolumeChanged(paramMessage.arg1, paramMessage.arg2);
      continue;
      onFreeResources();
      continue;
      onStopSounds();
      continue;
      onPlaySound(paramMessage.arg1, paramMessage.arg2);
      continue;
      onVibrate();
      continue;
      if (!this.mDialog.isShowing())
        continue;
      this.mDialog.dismiss();
      this.mActiveStreamType = -1;
      continue;
      if (!this.mDialog.isShowing())
        continue;
      updateStates();
    }
  }

  public void onClick(View paramView)
  {
    if (paramView == this.mMoreButton)
      expand();
    resetTimeout();
  }

  protected void onFreeResources()
  {
    monitorenter;
    try
    {
      for (int i = -1 + this.mToneGenerators.length; i >= 0; i--)
      {
        if (this.mToneGenerators[i] != null)
          this.mToneGenerators[i].release();
        this.mToneGenerators[i] = null;
      }
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  protected void onPlaySound(int paramInt1, int paramInt2)
  {
    if (hasMessages(3))
    {
      removeMessages(3);
      onStopSounds();
    }
    monitorenter;
    try
    {
      ToneGenerator localToneGenerator = getOrCreateToneGenerator(paramInt1);
      if (localToneGenerator != null)
      {
        localToneGenerator.startTone(24);
        sendMessageDelayed(obtainMessage(3), 150L);
      }
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
  {
    Object localObject = paramSeekBar.getTag();
    if ((paramBoolean) && ((localObject instanceof StreamControl)))
    {
      StreamControl localStreamControl = (StreamControl)localObject;
      if (this.mAudioManager.getStreamVolume(localStreamControl.streamType) != paramInt)
        this.mAudioManager.setStreamVolume(localStreamControl.streamType, paramInt, 0);
    }
    resetTimeout();
  }

  protected void onShowVolumeChanged(int paramInt1, int paramInt2)
  {
    int i;
    int j;
    if (this.mAudioService.isStreamMute(paramInt1))
    {
      i = this.mAudioService.getLastAudibleStreamVolume(paramInt1);
      this.mRingIsSilent = false;
      if (LOGD)
        Log.d("VolumePanel", "onShowVolumeChanged(streamType: " + paramInt1 + ", flags: " + paramInt2 + "), index: " + i);
      j = this.mAudioService.getStreamMaxVolume(paramInt1);
      switch (paramInt1)
      {
      case 1:
      case 4:
      default:
      case 2:
      case 3:
      case 0:
      case 5:
      case 6:
      }
    }
    while (true)
    {
      StreamControl localStreamControl = (StreamControl)this.mStreamControls.get(Integer.valueOf(paramInt1));
      if (localStreamControl != null)
      {
        if (localStreamControl.seekbarView.getMax() != j)
          localStreamControl.seekbarView.setMax(j);
        localStreamControl.seekbarView.setProgress(i);
      }
      if (!this.mDialog.isShowing())
      {
        this.mAudioManager.forceVolumeControlStream(paramInt1);
        this.mDialog.setContentView(this.mView);
        if (this.mShowCombinedVolumes)
          collapse();
        this.mDialog.show();
      }
      if (((paramInt2 & 0x10) != 0) && (this.mAudioService.isStreamAffectedByRingerMode(paramInt1)) && (this.mAudioService.getRingerMode() == 1) && (this.mAudioService.shouldVibrate(0)))
        sendMessageDelayed(obtainMessage(4), 300L);
      return;
      i = this.mAudioService.getStreamVolume(paramInt1);
      break;
      if (RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1) != null)
        continue;
      this.mRingIsSilent = true;
      continue;
      if ((0x380 & this.mAudioManager.getDevicesForStream(3)) != 0)
      {
        setMusicIcon(50463062, 50463063);
        continue;
      }
      setMusicIcon(50463000, 50463001);
      continue;
      i++;
      j++;
      continue;
      if (RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 2) != null)
        continue;
      this.mRingIsSilent = true;
      continue;
      i++;
      j++;
    }
  }

  public void onStartTrackingTouch(SeekBar paramSeekBar)
  {
  }

  protected void onStopSounds()
  {
    monitorenter;
    while (true)
    {
      int i;
      try
      {
        i = -1 + AudioSystem.getNumStreamTypes();
        if (i < 0)
          continue;
        ToneGenerator localToneGenerator = this.mToneGenerators[i];
        if (localToneGenerator != null)
        {
          localToneGenerator.stopTone();
          break label39;
          monitorexit;
          return;
        }
      }
      finally
      {
        localObject = finally;
        monitorexit;
        throw localObject;
      }
      label39: i--;
    }
  }

  public void onStopTrackingTouch(SeekBar paramSeekBar)
  {
  }

  protected void onVibrate()
  {
    if (this.mAudioService.getRingerMode() != 1);
    while (true)
    {
      return;
      this.mVibrator.vibrate(300L);
    }
  }

  protected void onVolumeChanged(int paramInt1, int paramInt2)
  {
    if (LOGD)
      Log.d("VolumePanel", "onVolumeChanged(streamType: " + paramInt1 + ", flags: " + paramInt2 + ")");
    if ((paramInt2 & 0x1) != 0)
    {
      if (this.mActiveStreamType == -1)
        reorderSliders(paramInt1);
      onShowVolumeChanged(paramInt1, paramInt2);
    }
    if (((paramInt2 & 0x4) != 0) && (!this.mRingIsSilent))
    {
      removeMessages(2);
      sendMessageDelayed(obtainMessage(2, paramInt1, paramInt2), 300L);
    }
    if ((paramInt2 & 0x8) != 0)
    {
      removeMessages(2);
      removeMessages(4);
      onStopSounds();
    }
    removeMessages(1);
    sendMessageDelayed(obtainMessage(1), 10000L);
    resetTimeout();
  }

  public void postVolumeChanged(int paramInt1, int paramInt2)
  {
    if (hasMessages(0));
    while (true)
    {
      return;
      if (this.mStreamControls == null)
        createSliders();
      removeMessages(1);
      obtainMessage(0, paramInt1, paramInt2).sendToTarget();
      if (this.mStreamControls.get(Integer.valueOf(paramInt1)) != null)
        continue;
      StreamControl localStreamControl = (StreamControl)this.mStreamControls.get(Integer.valueOf(3));
      if (localStreamControl != null)
      {
        localStreamControl.streamType = paramInt1;
        this.mStreamControls.put(Integer.valueOf(paramInt1), localStreamControl);
        continue;
      }
      Log.e("VolumePanel", "Unkown stream type " + paramInt1 + " was not bind any streamcontrol");
    }
  }

  private class StreamControl
  {
    ViewGroup group;
    ImageView icon;
    int iconMuteRes;
    int iconRes;
    SeekBar seekbarView;
    int streamType;

    private StreamControl()
    {
    }
  }

  private static enum StreamResources
  {
    int descRes;
    int iconMuteRes;
    int iconRes;
    boolean show;
    int streamType;

    static
    {
      AlarmStream = new StreamResources("AlarmStream", 3, 4, 51118502, 50463060, 50463061, false);
      MediaStream = new StreamResources("MediaStream", 4, 3, 51118500, 50463000, 50463001, true);
      NotificationStream = new StreamResources("NotificationStream", 5, 5, 51118501, 50463064, 50463065, true);
      StreamResources[] arrayOfStreamResources = new StreamResources[6];
      arrayOfStreamResources[0] = BluetoothSCOStream;
      arrayOfStreamResources[1] = RingerStream;
      arrayOfStreamResources[2] = VoiceStream;
      arrayOfStreamResources[3] = AlarmStream;
      arrayOfStreamResources[4] = MediaStream;
      arrayOfStreamResources[5] = NotificationStream;
      $VALUES = arrayOfStreamResources;
    }

    private StreamResources(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
    {
      this.streamType = paramInt1;
      this.descRes = paramInt2;
      this.iconRes = paramInt3;
      this.iconMuteRes = paramInt4;
      this.show = paramBoolean;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.view.VolumePanel
 * JD-Core Version:    0.6.0
 */