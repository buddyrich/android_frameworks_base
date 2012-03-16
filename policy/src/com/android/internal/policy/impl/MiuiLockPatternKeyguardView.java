package com.android.internal.policy.impl;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.android.internal.telephony.IccCard.State;
import com.android.internal.widget.LockPatternUtils;
import miui.content.res.ThemeResources;

public class MiuiLockPatternKeyguardView extends LockPatternKeyguardView
{
  private boolean mBackDown;
  private com.miui.internal.policy.impl.KeyguardScreenCallback mKeyguardScreenCallback;
  private ImageView mTorchCover;
  private BroadcastReceiver mTorchStateReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      boolean bool = paramIntent.getBooleanExtra("miui.intent.extra.IS_ENABLE", false);
      MiuiLockPatternKeyguardView.this.post(new Runnable(bool)
      {
        public void run()
        {
          MiuiLockPatternKeyguardView.this.updateTorchCover(this.val$enabled);
        }
      });
    }
  };

  public MiuiLockPatternKeyguardView(Context paramContext, KeyguardUpdateMonitor paramKeyguardUpdateMonitor, LockPatternUtils paramLockPatternUtils, KeyguardWindowController paramKeyguardWindowController)
  {
    super(paramContext, paramKeyguardUpdateMonitor, paramLockPatternUtils, paramKeyguardWindowController);
  }

  private void updateTorchCover(boolean paramBoolean)
  {
    if (this.mTorchCover == null)
    {
      this.mTorchCover = new ImageView(this.mContext);
      this.mTorchCover.setClickable(true);
      this.mTorchCover.setImageResource(50462910);
      this.mTorchCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
      this.mTorchCover.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    }
    if (paramBoolean)
      addView(this.mTorchCover);
    while (true)
    {
      return;
      removeView(this.mTorchCover);
    }
  }

  protected com.miui.internal.policy.impl.KeyguardScreenCallback createKeyguardScreenCallback()
  {
    this.mKeyguardScreenCallback = new KeyguardScreenCallbackImpl(super.createKeyguardScreenCallback());
    return this.mKeyguardScreenCallback;
  }

  protected View createLockScreen()
  {
    boolean bool = ThemeResources.getSystem().hasAwesomeLockscreen();
    Drawable localDrawable = null;
    if (bool);
    for (Object localObject = new AwesomeLockScreen(this.mContext, getConfiguration(), getLockPatternUtils(), getUpdateMonitor(), this.mKeyguardScreenCallback); ; localObject = new MiuiLockScreen(this.mContext, getConfiguration(), getLockPatternUtils(), getUpdateMonitor(), this.mKeyguardScreenCallback))
    {
      setBackgroundDrawable(localDrawable);
      return localObject;
      localDrawable = ThemeResources.getLockWallpaperCache(this.mContext);
    }
  }

  View createUnlockScreenFor(LockPatternKeyguardView.UnlockMode paramUnlockMode)
  {
    View localView = super.createUnlockScreenFor(paramUnlockMode);
    localView.setPadding(0, this.mContext.getResources().getDimensionPixelSize(50987008), 0, 0);
    return localView;
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = 1;
    int j = paramKeyEvent.getKeyCode();
    if (paramKeyEvent.getAction() == 0)
      if (j == 4)
        this.mBackDown = i;
    while (true)
    {
      i = super.dispatchKeyEvent(paramKeyEvent);
      while (true)
      {
        return i;
        if (j == 24)
          break;
        this.mBackDown = false;
        break;
        if (paramKeyEvent.getAction() != i)
          break;
        if ((j != 24) || (!this.mBackDown))
          break label84;
        this.mBackDown = false;
        this.mKeyguardScreenCallback.goToUnlockScreen();
      }
      label84: this.mBackDown = false;
    }
  }

  protected void onAttachedToWindow()
  {
    IntentFilter localIntentFilter = new IntentFilter("miui.intent.action.TOGGLE_TORCH");
    localIntentFilter.setPriority(1000);
    this.mContext.registerReceiver(this.mTorchStateReceiver, localIntentFilter);
    super.onAttachedToWindow();
  }

  protected void onDetachedFromWindow()
  {
    this.mContext.unregisterReceiver(this.mTorchStateReceiver);
    super.onDetachedFromWindow();
  }

  protected void recreateLockScreen()
  {
    if ((this.mMode == LockPatternKeyguardView.Mode.LockScreen) && (this.mLockScreen != null) && ((this.mLockScreen instanceof AwesomeLockScreen)));
    while (true)
    {
      return;
      super.recreateLockScreen();
    }
  }

  public void show()
  {
    this.mScreenOn = true;
    updateTorchCover(false);
    super.show();
  }

  class KeyguardScreenCallbackImpl extends KeyguardScreenCallbackProxy
    implements com.miui.internal.policy.impl.KeyguardScreenCallback
  {
    private Intent mPendingIntent = null;

    public KeyguardScreenCallbackImpl(KeyguardScreenCallback arg2)
    {
      super();
    }

    public void goToUnlockScreen()
    {
      IccCard.State localState = MiuiLockPatternKeyguardView.this.getUpdateMonitor().getSimState();
      if ((MiuiLockPatternKeyguardView.this.stuckOnLockScreenBecauseSimMissing()) || (localState == IccCard.State.PUK_REQUIRED));
      while (true)
      {
        return;
        if (!isSecure())
        {
          keyguardDone(true);
          continue;
        }
        super.goToUnlockScreen();
      }
    }

    public void keyguardDone(boolean paramBoolean)
    {
      super.keyguardDone(paramBoolean);
      if ((paramBoolean) && (this.mPendingIntent != null));
      try
      {
        MiuiLockPatternKeyguardView.this.mContext.startActivity(this.mPendingIntent);
        label30: return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        break label30;
      }
    }

    public void setPendingIntent(Intent paramIntent)
    {
      this.mPendingIntent = paramIntent;
    }
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiLockPatternKeyguardView
 * JD-Core Version:    0.6.0
 */