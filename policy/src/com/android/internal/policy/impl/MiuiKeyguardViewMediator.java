package com.android.internal.policy.impl;

import android.app.StatusBarManager;
import android.content.Context;
import android.os.LocalPowerManager;
import miui.app.ExtraStatusBarManager;
import miui.net.FirewallManager;

public class MiuiKeyguardViewMediator extends KeyguardViewMediator
{
  private static final int BTN_MOUSE = 272;
  boolean mCameraKeyWakeScreen;
  int mScreenOffReason;
  boolean mTrackballWakeScreen;
  boolean mVolumeKeyWakeScreen;

  public MiuiKeyguardViewMediator(Context paramContext, PhoneWindowManager paramPhoneWindowManager, LocalPowerManager paramLocalPowerManager)
  {
    super(paramContext, paramPhoneWindowManager, paramLocalPowerManager);
  }

  void adjustStatusBarLocked()
  {
    super.adjustStatusBarLocked();
    int i;
    int j;
    if (this.mStatusBarManager != null)
    {
      i = 0;
      if (isShowing())
      {
        j = 0x0 | 0x1000000;
        if (!isShowingAndNotHidden())
          break label70;
      }
    }
    label70: for (int k = -2147483648; ; k = 0)
    {
      i = j | k;
      if ((isSecure()) || (!ExtraStatusBarManager.isExpandableUnderKeyguard(this.mContext)))
        i |= 589824;
      this.mStatusBarManager.disable(i);
      return;
    }
  }

  boolean isWakeKeyWhenKeyguardShowing(int paramInt, boolean paramBoolean)
  {
    boolean bool;
    if (this.mScreenOffReason == 4)
      bool = false;
    while (true)
    {
      return bool;
      switch (paramInt)
      {
      default:
        bool = super.isWakeKeyWhenKeyguardShowing(paramInt, paramBoolean);
        break;
      case 272:
        bool = this.mTrackballWakeScreen;
        break;
      case 24:
      case 25:
        bool = this.mVolumeKeyWakeScreen;
        break;
      case 27:
        bool = this.mCameraKeyWakeScreen;
      }
    }
  }

  public void onScreenTurnedOff(int paramInt)
  {
    monitorenter;
    try
    {
      notifyScreenOffLocked();
      this.mScreenOffReason = paramInt;
      FirewallManager.getInstance().removeAccessControlPass("*");
      monitorexit;
      super.onScreenTurnedOff(paramInt);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void setWakeKeys(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.mTrackballWakeScreen = paramBoolean1;
    this.mCameraKeyWakeScreen = paramBoolean2;
    this.mVolumeKeyWakeScreen = paramBoolean3;
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiKeyguardViewMediator
 * JD-Core Version:    0.6.0
 */