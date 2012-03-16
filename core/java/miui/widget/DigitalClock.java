package miui.widget;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class DigitalClock extends RelativeLayout
{
  private static final String M12 = "h:mm";
  private static final String M24 = "kk:mm";
  private static final String SYSTEM = "/system/fonts/";
  private static final String SYSTEM_FONT_TIME_BACKGROUND = "/system/fonts/AndroidClock.ttf";
  private static final String SYSTEM_FONT_TIME_FOREGROUND = "/system/fonts/AndroidClock_Highlight.ttf";
  private static final Typeface sBackgroundFont = Typeface.createFromFile("/system/fonts/AndroidClock.ttf");
  private static final Typeface sForegroundFont = Typeface.createFromFile("/system/fonts/AndroidClock_Highlight.ttf");
  private AmPm mAmPm;
  private int mAttached = 0;
  private Calendar mCalendar;
  private String mFormat;
  private ContentObserver mFormatChangeObserver;
  private final Handler mHandler = new Handler();
  private BroadcastReceiver mIntentReceiver;
  private TextView mTimeDisplayBackground;
  private TextView mTimeDisplayForeground;

  public DigitalClock(Context paramContext)
  {
    this(paramContext, null);
  }

  public DigitalClock(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setDateFormat()
  {
    if (DateFormat.is24HourFormat(getContext()));
    for (String str = "kk:mm"; ; str = "h:mm")
    {
      this.mFormat = str;
      this.mAmPm.setShowAmPm(this.mFormat.equals("h:mm"));
      return;
    }
  }

  private void updateTime()
  {
    this.mCalendar.setTimeInMillis(System.currentTimeMillis());
    CharSequence localCharSequence = DateFormat.format(this.mFormat, this.mCalendar);
    this.mTimeDisplayBackground.setText(localCharSequence);
    this.mTimeDisplayForeground.setText(localCharSequence);
    AmPm localAmPm = this.mAmPm;
    if (this.mCalendar.get(9) == 0);
    for (boolean bool = true; ; bool = false)
    {
      localAmPm.setIsMorning(bool);
      return;
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mAttached = (1 + this.mAttached);
    if (this.mIntentReceiver == null)
    {
      this.mIntentReceiver = new TimeChangedReceiver(this);
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.TIME_TICK");
      localIntentFilter.addAction("android.intent.action.TIME_SET");
      localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
      this.mContext.registerReceiver(this.mIntentReceiver, localIntentFilter);
    }
    if (this.mFormatChangeObserver == null)
    {
      this.mFormatChangeObserver = new FormatChangeObserver(this);
      this.mContext.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, this.mFormatChangeObserver);
    }
    updateTime();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mAttached = (-1 + this.mAttached);
    if (this.mIntentReceiver != null)
      this.mContext.unregisterReceiver(this.mIntentReceiver);
    if (this.mFormatChangeObserver != null)
      this.mContext.getContentResolver().unregisterContentObserver(this.mFormatChangeObserver);
    this.mFormatChangeObserver = null;
    this.mIntentReceiver = null;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTimeDisplayBackground = ((TextView)findViewById(51052563));
    this.mTimeDisplayBackground.setTypeface(sBackgroundFont);
    this.mTimeDisplayBackground.setVisibility(4);
    this.mTimeDisplayForeground = ((TextView)findViewById(51052564));
    this.mTimeDisplayForeground.setTypeface(sForegroundFont);
    this.mAmPm = new AmPm(this, null);
    this.mCalendar = Calendar.getInstance();
    setDateFormat();
  }

  void updateTime(Calendar paramCalendar)
  {
    this.mCalendar = paramCalendar;
    updateTime();
  }

  private static class FormatChangeObserver extends ContentObserver
  {
    private WeakReference<DigitalClock> mClock;
    private Context mContext;

    public FormatChangeObserver(DigitalClock paramDigitalClock)
    {
      super();
      this.mClock = new WeakReference(paramDigitalClock);
      this.mContext = paramDigitalClock.getContext();
    }

    public void onChange(boolean paramBoolean)
    {
      DigitalClock localDigitalClock = (DigitalClock)this.mClock.get();
      if (localDigitalClock != null)
      {
        localDigitalClock.setDateFormat();
        localDigitalClock.updateTime();
      }
      while (true)
      {
        return;
        try
        {
          this.mContext.getContentResolver().unregisterContentObserver(this);
        }
        catch (RuntimeException localRuntimeException)
        {
        }
      }
    }
  }

  static class AmPm
  {
    private TextView mAmPmTextView;
    private String mAmString;
    private String mPmString;

    AmPm(View paramView, Typeface paramTypeface)
    {
      if ((this.mAmPmTextView != null) && (paramTypeface != null))
        this.mAmPmTextView.setTypeface(paramTypeface);
      String[] arrayOfString = new DateFormatSymbols().getAmPmStrings();
      this.mAmString = arrayOfString[0];
      this.mPmString = arrayOfString[1];
    }

    void setIsMorning(boolean paramBoolean)
    {
      TextView localTextView;
      if (this.mAmPmTextView != null)
      {
        localTextView = this.mAmPmTextView;
        if (!paramBoolean)
          break label27;
      }
      label27: for (String str = this.mAmString; ; str = this.mPmString)
      {
        localTextView.setText(str);
        return;
      }
    }

    void setShowAmPm(boolean paramBoolean)
    {
      TextView localTextView;
      if (this.mAmPmTextView != null)
      {
        localTextView = this.mAmPmTextView;
        if (!paramBoolean)
          break label24;
      }
      label24: for (int i = 0; ; i = 8)
      {
        localTextView.setVisibility(i);
        return;
      }
    }
  }

  private static class TimeChangedReceiver extends BroadcastReceiver
  {
    private WeakReference<DigitalClock> mClock;
    private Context mContext;

    public TimeChangedReceiver(DigitalClock paramDigitalClock)
    {
      this.mClock = new WeakReference(paramDigitalClock);
      this.mContext = paramDigitalClock.getContext();
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      boolean bool = paramIntent.getAction().equals("android.intent.action.TIMEZONE_CHANGED");
      DigitalClock localDigitalClock = (DigitalClock)this.mClock.get();
      if (localDigitalClock != null)
        localDigitalClock.mHandler.post(new Runnable(bool, localDigitalClock)
        {
          public void run()
          {
            if (this.val$timezoneChanged)
              DigitalClock.access$002(this.val$clock, Calendar.getInstance());
            this.val$clock.updateTime();
          }
        });
      while (true)
      {
        return;
        try
        {
          this.mContext.unregisterReceiver(this);
        }
        catch (RuntimeException localRuntimeException)
        {
        }
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.DigitalClock
 * JD-Core Version:    0.6.0
 */