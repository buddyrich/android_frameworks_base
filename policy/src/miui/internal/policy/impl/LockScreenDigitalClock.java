package com.miui.internal.policy.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.HashMap;

public class LockScreenDigitalClock extends LinearLayout
{
  private static final String M12 = "h:mm";
  private static final String M24 = "kk:mm";
  private static HashMap<String, Integer> sDigital2ResId = new HashMap();
  private boolean mAttached;
  private Calendar mCalendar;
  private TextView mDate;
  private ImageView mFirstDigital;
  private String mFormat;
  private ImageView mFouthDigital;
  private final Handler mHandler = new Handler();
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("android.intent.action.TIMEZONE_CHANGED"))
        LockScreenDigitalClock.access$002(LockScreenDigitalClock.this, Calendar.getInstance());
      LockScreenDigitalClock.this.mHandler.post(new Runnable()
      {
        public void run()
        {
          LockScreenDigitalClock.this.updateTime();
        }
      });
    }
  };
  private ImageView mSecondDigital;
  private ImageView mThirdDigital;

  static
  {
    sDigital2ResId.put("0", Integer.valueOf(50462842));
    sDigital2ResId.put("1", Integer.valueOf(50462843));
    sDigital2ResId.put("2", Integer.valueOf(50462844));
    sDigital2ResId.put("3", Integer.valueOf(50462845));
    sDigital2ResId.put("4", Integer.valueOf(50462846));
    sDigital2ResId.put("5", Integer.valueOf(50462847));
    sDigital2ResId.put("6", Integer.valueOf(50462848));
    sDigital2ResId.put("7", Integer.valueOf(50462849));
    sDigital2ResId.put("8", Integer.valueOf(50462850));
    sDigital2ResId.put("9", Integer.valueOf(50462851));
  }

  public LockScreenDigitalClock(Context paramContext)
  {
    this(paramContext, null);
  }

  public LockScreenDigitalClock(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setDateFormat()
  {
    if (DateFormat.is24HourFormat(getContext()));
    for (String str = "kk:mm"; ; str = "h:mm")
    {
      this.mFormat = str;
      return;
    }
  }

  private void updateTime()
  {
    this.mCalendar.setTimeInMillis(System.currentTimeMillis());
    CharSequence localCharSequence1 = DateFormat.format(this.mFormat, this.mCalendar);
    int i = 0;
    if (localCharSequence1.length() == 4)
      this.mFirstDigital.setVisibility(8);
    while (true)
    {
      int k = ((Integer)sDigital2ResId.get(String.valueOf(localCharSequence1.charAt(i)))).intValue();
      this.mSecondDigital.setImageResource(k);
      int m = ((Integer)sDigital2ResId.get(String.valueOf(localCharSequence1.charAt(i + 2)))).intValue();
      this.mThirdDigital.setImageResource(m);
      int n = ((Integer)sDigital2ResId.get(String.valueOf(localCharSequence1.charAt(i + 3)))).intValue();
      this.mFouthDigital.setImageResource(n);
      CharSequence localCharSequence2 = DateFormat.format(this.mContext.getString(51118125), this.mCalendar);
      this.mDate.setVisibility(0);
      this.mDate.setText(localCharSequence2);
      return;
      if (localCharSequence1.length() != 5)
        continue;
      int j = ((Integer)sDigital2ResId.get(String.valueOf(localCharSequence1.charAt(0)))).intValue();
      this.mFirstDigital.setImageResource(j);
      this.mFirstDigital.setVisibility(0);
      i = 1;
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (this.mAttached);
    while (true)
    {
      return;
      this.mAttached = true;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.TIME_TICK");
      localIntentFilter.addAction("android.intent.action.TIME_SET");
      localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
      this.mContext.registerReceiver(this.mIntentReceiver, localIntentFilter);
      updateTime();
    }
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (!this.mAttached);
    while (true)
    {
      return;
      this.mAttached = false;
      this.mContext.unregisterReceiver(this.mIntentReceiver);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mFirstDigital = ((ImageView)findViewById(51052637));
    this.mSecondDigital = ((ImageView)findViewById(51052638));
    this.mThirdDigital = ((ImageView)findViewById(51052640));
    this.mFouthDigital = ((ImageView)findViewById(51052641));
    this.mDate = ((TextView)findViewById(51052642));
    this.mCalendar = Calendar.getInstance();
    setDateFormat();
  }

  void updateTime(Calendar paramCalendar)
  {
    this.mCalendar = paramCalendar;
    updateTime();
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.LockScreenDigitalClock
 * JD-Core Version:    0.6.0
 */