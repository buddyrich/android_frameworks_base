package com.android.internal.policy.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings.System;

public class MiuiKeyguardUpdateMonitor extends KeyguardUpdateMonitor
{
  private Context mContext;
  private int mLowBatteryThreshold;
  private ContentObserver mLowBatteryThresholdObserver;

  public MiuiKeyguardUpdateMonitor(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.mLowBatteryThresholdObserver = new ContentObserver(null)
    {
      public void onChange(boolean paramBoolean)
      {
        super.onChange(paramBoolean);
        MiuiKeyguardUpdateMonitor.access$002(MiuiKeyguardUpdateMonitor.this, MiuiKeyguardUpdateMonitor.this.mContext.getResources().getInteger(50855938));
        MiuiKeyguardUpdateMonitor.access$002(MiuiKeyguardUpdateMonitor.this, Settings.System.getInt(MiuiKeyguardUpdateMonitor.this.mContext.getContentResolver(), "battery_level_low_customized", MiuiKeyguardUpdateMonitor.this.mLowBatteryThreshold));
      }
    };
    this.mLowBatteryThresholdObserver.onChange(true);
    this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("battery_level_low_customized"), false, this.mLowBatteryThresholdObserver);
  }

  protected void finalize()
    throws Throwable
  {
    this.mContext.getContentResolver().unregisterContentObserver(this.mLowBatteryThresholdObserver);
    super.finalize();
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiKeyguardUpdateMonitor
 * JD-Core Version:    0.6.0
 */