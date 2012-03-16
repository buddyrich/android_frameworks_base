package android.preference;

import android.R.styleable;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MiuiVolumePreference extends SeekBarDialogPreference
  implements PreferenceManager.OnActivityStopListener, View.OnKeyListener
{
  private static final String TAG = "VolumePreference";
  private SeekBarVolumizer mSeekBarVolumizer;
  private int mStreamType;

  public MiuiVolumePreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.VolumePreference, 0, 0);
    this.mStreamType = localTypedArray.getInt(0, 0);
    localTypedArray.recycle();
  }

  private void cleanup()
  {
    getPreferenceManager().unregisterOnActivityStopListener(this);
    if (this.mSeekBarVolumizer != null)
    {
      Dialog localDialog = getDialog();
      if ((localDialog != null) && (localDialog.isShowing()))
      {
        View localView = localDialog.getWindow().getDecorView().findViewById(51052562);
        if (localView != null)
          localView.setOnKeyListener(null);
        this.mSeekBarVolumizer.revertVolume();
      }
      this.mSeekBarVolumizer.stop();
      this.mSeekBarVolumizer = null;
    }
  }

  public void onActivityStop()
  {
    if (this.mSeekBarVolumizer != null)
      this.mSeekBarVolumizer.stopSample();
  }

  protected void onBindDialogView(View paramView)
  {
    super.onBindDialogView(paramView);
    SeekBar localSeekBar = (SeekBar)paramView.findViewById(51052562);
    this.mSeekBarVolumizer = new SeekBarVolumizer(getContext(), localSeekBar, this.mStreamType);
    getPreferenceManager().registerOnActivityStopListener(this);
    paramView.setOnKeyListener(this);
    paramView.setFocusableInTouchMode(true);
    paramView.requestFocus();
  }

  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if ((!paramBoolean) && (this.mSeekBarVolumizer != null))
      this.mSeekBarVolumizer.revertVolume();
    cleanup();
  }

  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    int i = 1;
    if (this.mSeekBarVolumizer == null);
    while (true)
    {
      label10: return i;
      if (paramKeyEvent.getAction() == 0);
      for (int j = i; ; j = 0)
        switch (paramInt)
        {
        default:
          i = 0;
          break label10;
        case 25:
        case 24:
        case 164:
        }
      if (j == 0)
        continue;
      this.mSeekBarVolumizer.changeVolumeBy(-1);
      continue;
      if (j == 0)
        continue;
      this.mSeekBarVolumizer.changeVolumeBy(i);
      continue;
      if (j == 0)
        continue;
      this.mSeekBarVolumizer.muteVolume();
    }
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable == null) || (!paramParcelable.getClass().equals(SavedState.class)))
      super.onRestoreInstanceState(paramParcelable);
    while (true)
    {
      return;
      SavedState localSavedState = (SavedState)paramParcelable;
      super.onRestoreInstanceState(localSavedState.getSuperState());
      if (this.mSeekBarVolumizer == null)
        continue;
      this.mSeekBarVolumizer.onRestoreInstanceState(localSavedState.getVolumeStore());
    }
  }

  protected void onSampleStarting(SeekBarVolumizer paramSeekBarVolumizer)
  {
    if ((this.mSeekBarVolumizer != null) && (paramSeekBarVolumizer != this.mSeekBarVolumizer))
      this.mSeekBarVolumizer.stopSample();
  }

  protected Parcelable onSaveInstanceState()
  {
    Object localObject = super.onSaveInstanceState();
    if (isPersistent());
    while (true)
    {
      return localObject;
      SavedState localSavedState = new SavedState((Parcelable)localObject);
      if (this.mSeekBarVolumizer != null)
        this.mSeekBarVolumizer.onSaveInstanceState(localSavedState.getVolumeStore());
      localObject = localSavedState;
    }
  }

  public void setStreamType(int paramInt)
  {
    this.mStreamType = paramInt;
  }

  public class SeekBarVolumizer
    implements SeekBar.OnSeekBarChangeListener, Runnable
  {
    private AudioManager mAudioManager;
    private Context mContext;
    private Handler mHandler = new Handler();
    private int mLastProgress = -1;
    private int mOriginalStreamVolume;
    private Ringtone mRingtone;
    private SeekBar mSeekBar;
    private int mStreamType;
    private int mVolumeBeforeMute = -1;
    private ContentObserver mVolumeObserver = new ContentObserver(this.mHandler)
    {
      public void onChange(boolean paramBoolean)
      {
        super.onChange(paramBoolean);
        if ((MiuiVolumePreference.SeekBarVolumizer.this.mSeekBar != null) && (MiuiVolumePreference.SeekBarVolumizer.this.mAudioManager != null))
          if (!MiuiVolumePreference.SeekBarVolumizer.this.mAudioManager.isStreamMute(MiuiVolumePreference.SeekBarVolumizer.this.mStreamType))
            break label75;
        label75: for (int i = MiuiVolumePreference.SeekBarVolumizer.this.mAudioManager.getLastAudibleStreamVolume(MiuiVolumePreference.SeekBarVolumizer.this.mStreamType); ; i = MiuiVolumePreference.SeekBarVolumizer.this.mAudioManager.getStreamVolume(MiuiVolumePreference.SeekBarVolumizer.this.mStreamType))
        {
          MiuiVolumePreference.SeekBarVolumizer.this.mSeekBar.setProgress(i);
          return;
        }
      }
    };

    public SeekBarVolumizer(Context paramSeekBar, SeekBar paramInt, int arg4)
    {
      this(paramSeekBar, paramInt, i, null);
    }

    public SeekBarVolumizer(Context paramSeekBar, SeekBar paramInt, int paramUri, Uri arg5)
    {
      this.mContext = paramSeekBar;
      this.mAudioManager = ((AudioManager)paramSeekBar.getSystemService("audio"));
      this.mStreamType = paramUri;
      this.mSeekBar = paramInt;
      Uri localUri;
      initSeekBar(paramInt, localUri);
    }

    private void initSeekBar(SeekBar paramSeekBar, Uri paramUri)
    {
      paramSeekBar.setMax(this.mAudioManager.getStreamMaxVolume(this.mStreamType));
      this.mOriginalStreamVolume = this.mAudioManager.getStreamVolume(this.mStreamType);
      paramSeekBar.setProgress(this.mOriginalStreamVolume);
      paramSeekBar.setOnSeekBarChangeListener(this);
      this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.VOLUME_SETTINGS[this.mStreamType]), false, this.mVolumeObserver);
      if (paramUri == null)
      {
        if (this.mStreamType != 2)
          break label116;
        paramUri = Settings.System.DEFAULT_RINGTONE_URI;
      }
      while (true)
      {
        this.mRingtone = RingtoneManager.getRingtone(this.mContext, paramUri);
        if (this.mRingtone != null)
          this.mRingtone.setStreamType(this.mStreamType);
        return;
        label116: if (this.mStreamType == 5)
        {
          paramUri = Settings.System.DEFAULT_NOTIFICATION_URI;
          continue;
        }
        paramUri = Settings.System.DEFAULT_ALARM_ALERT_URI;
      }
    }

    public void changeVolumeBy(int paramInt)
    {
      this.mSeekBar.incrementProgressBy(paramInt);
      if (!isSamplePlaying())
        startSample();
      postSetVolume(this.mSeekBar.getProgress());
      this.mVolumeBeforeMute = -1;
    }

    public SeekBar getSeekBar()
    {
      return this.mSeekBar;
    }

    public boolean isSamplePlaying()
    {
      if ((this.mRingtone != null) && (this.mRingtone.isPlaying()));
      for (int i = 1; ; i = 0)
        return i;
    }

    public void muteVolume()
    {
      if (this.mVolumeBeforeMute != -1)
      {
        this.mSeekBar.setProgress(this.mVolumeBeforeMute);
        startSample();
        postSetVolume(this.mVolumeBeforeMute);
        this.mVolumeBeforeMute = -1;
      }
      while (true)
      {
        return;
        this.mVolumeBeforeMute = this.mSeekBar.getProgress();
        this.mSeekBar.setProgress(0);
        stopSample();
        postSetVolume(0);
      }
    }

    public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
    {
      if (!paramBoolean);
      while (true)
      {
        return;
        postSetVolume(paramInt);
      }
    }

    public void onRestoreInstanceState(MiuiVolumePreference.VolumeStore paramVolumeStore)
    {
      if (paramVolumeStore.volume != -1)
      {
        this.mOriginalStreamVolume = paramVolumeStore.originalVolume;
        this.mLastProgress = paramVolumeStore.volume;
        postSetVolume(this.mLastProgress);
      }
    }

    public void onSaveInstanceState(MiuiVolumePreference.VolumeStore paramVolumeStore)
    {
      if (this.mLastProgress >= 0)
      {
        paramVolumeStore.volume = this.mLastProgress;
        paramVolumeStore.originalVolume = this.mOriginalStreamVolume;
      }
    }

    public void onStartTrackingTouch(SeekBar paramSeekBar)
    {
    }

    public void onStopTrackingTouch(SeekBar paramSeekBar)
    {
      if (!isSamplePlaying())
        startSample();
    }

    void postSetVolume(int paramInt)
    {
      this.mLastProgress = paramInt;
      this.mHandler.removeCallbacks(this);
      this.mHandler.post(this);
    }

    public void revertVolume()
    {
      this.mAudioManager.setStreamVolume(this.mStreamType, this.mOriginalStreamVolume, 0);
    }

    public void run()
    {
      this.mAudioManager.setStreamVolume(this.mStreamType, this.mLastProgress, 0);
    }

    public void startSample()
    {
      MiuiVolumePreference.this.onSampleStarting(this);
      if (this.mRingtone != null)
        this.mRingtone.play();
    }

    public void stop()
    {
      stopSample();
      this.mContext.getContentResolver().unregisterContentObserver(this.mVolumeObserver);
      this.mSeekBar.setOnSeekBarChangeListener(null);
    }

    public void stopSample()
    {
      if (this.mRingtone != null)
        this.mRingtone.stop();
    }
  }

  private static class SavedState extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public MiuiVolumePreference.SavedState createFromParcel(Parcel paramParcel)
      {
        return new MiuiVolumePreference.SavedState(paramParcel);
      }

      public MiuiVolumePreference.SavedState[] newArray(int paramInt)
      {
        return new MiuiVolumePreference.SavedState[paramInt];
      }
    };
    MiuiVolumePreference.VolumeStore mVolumeStore = new MiuiVolumePreference.VolumeStore();

    public SavedState(Parcel paramParcel)
    {
      super();
      this.mVolumeStore.volume = paramParcel.readInt();
      this.mVolumeStore.originalVolume = paramParcel.readInt();
    }

    public SavedState(Parcelable paramParcelable)
    {
      super();
    }

    MiuiVolumePreference.VolumeStore getVolumeStore()
    {
      return this.mVolumeStore;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(this.mVolumeStore.volume);
      paramParcel.writeInt(this.mVolumeStore.originalVolume);
    }
  }

  public static class VolumeStore
  {
    public int originalVolume = -1;
    public int volume = -1;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.preference.MiuiVolumePreference
 * JD-Core Version:    0.6.0
 */