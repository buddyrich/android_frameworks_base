package com.android.internal.policy.impl;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.android.internal.telephony.IccCard.State;
import com.android.internal.widget.LockPatternUtils;
import com.miui.internal.policy.impl.AwesomeLockScreenImp.AwesomeLockScreenView;
import com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenElementFactory;
import com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenResourceLoader;
import com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenRoot;
import com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenRoot.UnlockerCallback;
import com.miui.internal.policy.impl.AwesomeLockScreenImp.UnlockerListener;
import com.miui.internal.policy.impl.AwesomeLockScreenImp.UnlockerScreenElement;
import com.miui.internal.policy.impl.KeyguardScreenCallback;
import miui.app.screenelement.IndexedNumberVariable;
import miui.app.screenelement.ResourceManager;
import miui.app.screenelement.ScreenContext;
import miui.app.screenelement.Task;
import miui.util.HapticFeedbackUtil;

class AwesomeLockScreen extends FrameLayout
  implements KeyguardScreen, KeyguardUpdateMonitor.InfoCallback, KeyguardUpdateMonitor.SimStateCallback, LockScreenRoot.UnlockerCallback, UnlockerListener, QcomCallback
{
  private static final boolean DBG = false;
  private static final String TAG = "AwesomeLockScreen";
  private static HapticFeedbackUtil mHapticFeedbackUtil;
  private boolean isPaused = false;
  private AudioManager mAudioManager;
  private IndexedNumberVariable mBatteryLevel;
  private final KeyguardScreenCallback mCallback;
  private LockPatternUtils mLockPatternUtils;
  private final ScreenContext mLockscreenContext;
  private AwesomeLockScreenView mLockscreenView;
  private LockScreenRoot mRoot;
  private KeyguardUpdateMonitor mUpdateMonitor;

  AwesomeLockScreen(Context paramContext, Configuration paramConfiguration, LockPatternUtils paramLockPatternUtils, KeyguardUpdateMonitor paramKeyguardUpdateMonitor, KeyguardScreenCallback paramKeyguardScreenCallback)
  {
    super(paramContext);
    this.mLockPatternUtils = paramLockPatternUtils;
    this.mUpdateMonitor = paramKeyguardUpdateMonitor;
    this.mCallback = paramKeyguardScreenCallback;
    setFocusable(true);
    setFocusableInTouchMode(true);
    if (mHapticFeedbackUtil == null)
      mHapticFeedbackUtil = new HapticFeedbackUtil(paramContext, true);
    this.mAudioManager = ((AudioManager)paramContext.getSystemService("audio"));
    paramKeyguardUpdateMonitor.registerInfoCallback(this);
    paramKeyguardUpdateMonitor.registerSimStateCallback(this);
    this.mLockscreenContext = new ScreenContext(this.mContext, new ResourceManager(new LockScreenResourceLoader()), new LockScreenElementFactory(this, this));
    onRefreshBatteryInfo(this.mUpdateMonitor.shouldShowBatteryInfo(), this.mUpdateMonitor.isDevicePluggedIn(), this.mUpdateMonitor.getBatteryLevel());
    this.mRoot = new LockScreenRoot(this.mLockscreenContext, this);
    this.mRoot.load();
    this.mLockscreenView = new AwesomeLockScreenView(this.mContext, this, this.mRoot);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -1);
    addView(this.mLockscreenView, localLayoutParams);
  }

  public void cleanUp()
  {
    this.mLockscreenView.cleanUp();
    this.mUpdateMonitor.removeCallback(this);
    this.mLockPatternUtils = null;
    this.mUpdateMonitor = null;
  }

  public void endUnlockMoving(UnlockerScreenElement paramUnlockerScreenElement)
  {
    if (this.mRoot != null)
      this.mRoot.endUnlockMoving(paramUnlockerScreenElement);
  }

  public Task findTask(String paramString)
  {
    return null;
  }

  public void haptic(int paramInt)
  {
    mHapticFeedbackUtil.performHapticFeedback(1, false);
  }

  public boolean isDisplayDesktop()
  {
    return this.mRoot.isDisplayDesktop();
  }

  public boolean isSoundEnable()
  {
    if (this.mAudioManager.getRingerMode() == 2);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean needsInput()
  {
    return false;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
  }

  public void onClockVisibilityChanged()
  {
  }

  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
  }

  public void onDeviceProvisioned()
  {
  }

  public void onPause()
  {
    this.mLockscreenView.onPause();
    this.isPaused = true;
  }

  public void onPhoneStateChanged(int paramInt)
  {
  }

  public void onPhoneStateChanged(String paramString)
  {
  }

  public void onRefreshBatteryInfo(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    if (this.mLockscreenContext != null)
    {
      this.mLockscreenContext.mShowingBatteryInfo = paramBoolean1;
      this.mLockscreenContext.mPluggedIn = paramBoolean2;
      this.mLockscreenContext.mBatteryLevel = paramInt;
      if (this.mBatteryLevel == null)
        this.mBatteryLevel = new IndexedNumberVariable("battery_level", this.mLockscreenContext.mVariables);
      this.mBatteryLevel.set(paramInt);
      this.mLockscreenContext.mShouldUpdate = true;
    }
  }

  public void onRefreshCarrierInfo(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
  }

  public void onRefreshCarrierInfo(CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt)
  {
  }

  public void onResume()
  {
    this.mLockscreenView.onResume();
    this.isPaused = false;
  }

  public void onRingerModeChanged(int paramInt)
  {
  }

  public void onSimStateChanged(IccCard.State paramState)
  {
  }

  public void onSimStateChanged(IccCard.State paramState, int paramInt)
  {
  }

  public void onTimeChanged()
  {
  }

  public void pokeWakelock()
  {
    this.mCallback.pokeWakelock();
  }

  public void pokeWakelock(int paramInt)
  {
    this.mCallback.pokeWakelock(paramInt);
  }

  public void startUnlockMoving(UnlockerScreenElement paramUnlockerScreenElement)
  {
    if (this.mRoot != null)
      this.mRoot.startUnlockMoving(paramUnlockerScreenElement);
  }

  public void unlocked(Intent paramIntent)
  {
    if (paramIntent != null)
      this.mCallback.setPendingIntent(paramIntent);
    post(new Runnable()
    {
      public void run()
      {
        try
        {
          AwesomeLockScreen.this.mCallback.goToUnlockScreen();
          return;
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
          while (true)
          {
            Log.e("AwesomeLockScreen", localActivityNotFoundException.toString());
            localActivityNotFoundException.printStackTrace();
          }
        }
      }
    });
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.AwesomeLockScreen
 * JD-Core Version:    0.6.0
 */