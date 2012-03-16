package com.android.internal.policy.impl;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings.System;
import android.view.KeyEvent;
import android.view.Window.Callback;

public class MiuiPhoneWindow extends PhoneWindow
{
  private Context mContext;
  private KeyguardManager mKeyguardManager;

  public MiuiPhoneWindow(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
  }

  private KeyguardManager getKeyguardManager()
  {
    if (this.mKeyguardManager == null)
      this.mKeyguardManager = ((KeyguardManager)getContext().getSystemService("keyguard"));
    return this.mKeyguardManager;
  }

  protected boolean handleCameraKeyEvent(PhoneWindow.DecorView paramDecorView, KeyEvent paramKeyEvent, int paramInt)
  {
    int i = paramKeyEvent.getKeyCode();
    int j;
    int n;
    label100: KeyEvent localKeyEvent1;
    boolean bool;
    label137: KeyEvent localKeyEvent2;
    if (paramKeyEvent.getAction() == 0)
    {
      j = 1;
      Window.Callback localCallback = getCallback();
      k = 0;
      if ((i == 27) && (j == 0))
      {
        int m = 0;
        if (1 == Settings.System.getInt(this.mContext.getContentResolver(), "camera_key_preferred_action_type", 0))
          m = Settings.System.getInt(this.mContext.getContentResolver(), "camera_key_preferred_action_shortcut_id", -1);
        n = 0;
        switch (m)
        {
        default:
          if (n == 0)
            break;
          localKeyEvent1 = new KeyEvent(0, n);
          if ((localCallback != null) && (paramInt < 0))
          {
            bool = localCallback.dispatchKeyEvent(localKeyEvent1);
            localKeyEvent2 = new KeyEvent(1, n);
            if ((localCallback == null) || (paramInt >= 0))
              break label206;
            if (!localCallback.dispatchKeyEvent(localKeyEvent2))
              break label215;
          }
        case 3:
        case 2:
        }
      }
    }
    label170: for (int k = 1; ; k = 0)
    {
      return k;
      j = 0;
      break;
      n = 5;
      break label100;
      n = 84;
      break label100;
      bool = paramDecorView.superDispatchKeyEvent(localKeyEvent1);
      break label137;
      label206: if (paramDecorView.dispatchKeyEvent(localKeyEvent2))
        break label170;
      label215: if (bool)
        break label170;
    }
  }

  protected boolean onKeyUp(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    boolean bool = super.onKeyUp(paramInt1, paramInt2, paramKeyEvent);
    if ((27 == paramInt2) && (!getKeyguardManager().inKeyguardRestrictedInputMode()))
    {
      if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
        this.mContext.sendBroadcast(new Intent("com.miui.app.ExtraStatusBarManager.TRIGGER_CAMERA_KEY"));
      bool = true;
    }
    return bool;
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiPhoneWindow
 * JD-Core Version:    0.6.0
 */