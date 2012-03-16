package com.android.internal.policy.impl;

import android.content.Context;
import com.android.internal.telephony.IccCard.State;
import com.android.internal.widget.LockPatternUtils;

public class MiuiLockPatternKeyguardViewProperties
  implements KeyguardViewProperties
{
  private final LockPatternUtils mLockPatternUtils;
  private final KeyguardUpdateMonitor mUpdateMonitor;

  public MiuiLockPatternKeyguardViewProperties(LockPatternUtils paramLockPatternUtils, KeyguardUpdateMonitor paramKeyguardUpdateMonitor)
  {
    this.mLockPatternUtils = paramLockPatternUtils;
    this.mUpdateMonitor = paramKeyguardUpdateMonitor;
  }

  private boolean isSimPinSecure()
  {
    IccCard.State localState = this.mUpdateMonitor.getSimState();
    if ((localState == IccCard.State.PIN_REQUIRED) || (localState == IccCard.State.PUK_REQUIRED) || (localState == IccCard.State.PERM_DISABLED));
    for (int i = 1; ; i = 0)
      return i;
  }

  public KeyguardViewBase createKeyguardView(Context paramContext, KeyguardUpdateMonitor paramKeyguardUpdateMonitor, KeyguardWindowController paramKeyguardWindowController)
  {
    return new MiuiLockPatternKeyguardView(paramContext, paramKeyguardUpdateMonitor, this.mLockPatternUtils, paramKeyguardWindowController);
  }

  public boolean isSecure()
  {
    if ((this.mLockPatternUtils.isSecure()) || (isSimPinSecure()));
    for (int i = 1; ; i = 0)
      return i;
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiLockPatternKeyguardViewProperties
 * JD-Core Version:    0.6.0
 */