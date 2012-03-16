package com.android.internal.policy.impl;

import android.content.Context;
import android.os.LocalPowerManager;
import com.android.internal.widget.LockPatternUtils;

public class MiuiClassFactory
{
  public static KeyguardViewBase createKeyguardView(Context paramContext, KeyguardUpdateMonitor paramKeyguardUpdateMonitor, LockPatternUtils paramLockPatternUtils, KeyguardWindowController paramKeyguardWindowController)
  {
    return new MiuiLockPatternKeyguardView(paramContext, paramKeyguardUpdateMonitor, paramLockPatternUtils, paramKeyguardWindowController);
  }

  public static KeyguardViewMediator createKeyguardViewMediator(Context paramContext, PhoneWindowManager paramPhoneWindowManager, LocalPowerManager paramLocalPowerManager)
  {
    return new MiuiKeyguardViewMediator(paramContext, paramPhoneWindowManager, paramLocalPowerManager);
  }

  public static KeyguardViewProperties createKeyguardViewProperties(LockPatternUtils paramLockPatternUtils, KeyguardUpdateMonitor paramKeyguardUpdateMonitor)
  {
    return new MiuiLockPatternKeyguardViewProperties(paramLockPatternUtils, paramKeyguardUpdateMonitor);
  }

  public static PhoneWindow createPhoneWindow(Context paramContext)
  {
    return new MiuiPhoneWindow(paramContext);
  }

  public static PhoneWindowManager createPhoneWindowManager()
  {
    return new MiuiPhoneWindowManager();
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiClassFactory
 * JD-Core Version:    0.6.0
 */