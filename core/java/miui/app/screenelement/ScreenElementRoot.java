package miui.app.screenelement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Handler;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import java.util.Calendar;
import miui.net.FirewallManager;
import miui.security.ChooseLockSettingsHelper;
import org.w3c.dom.Element;

public class ScreenElementRoot extends ScreenElement
  implements InteractiveListener
{
  private static final int DEFAULT_FRAME_RATE = 30;
  private static final int DEFAULT_SCREEN_WIDTH = 480;
  private static final String LOG_TAG = "ScreenElementRoot";
  private static final int RES_DENSITY = 240;
  private IndexedNumberVariable mAmPm;
  private IndexedNumberVariable mBatteryLevel;
  protected Calendar mCalendar = Calendar.getInstance();
  private IndexedNumberVariable mDate;
  private IndexedNumberVariable mDayOfWeek;
  private int mDefaultScreenWidth;
  protected ElementGroup mElementGroup;
  private int mFrameRate = 30;
  private final Handler mHandler = new Handler();
  private IndexedNumberVariable mHour12;
  private IndexedNumberVariable mHour24;
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("android.intent.action.BATTERY_CHANGED"))
      {
        int i = paramIntent.getIntExtra("level", -1);
        if (i != -1)
        {
          ScreenContext localScreenContext = ScreenElementRoot.this.mContext;
          if (i >= 100)
            i = 100;
          localScreenContext.mBatteryLevel = i;
          ScreenElementRoot.this.mBatteryLevel.set(ScreenElementRoot.this.mContext.mBatteryLevel);
          ScreenElementRoot.this.mContext.mShouldUpdate = true;
        }
      }
      while (true)
      {
        return;
        if (paramIntent.getAction().equals("android.intent.action.TIMEZONE_CHANGED"))
          ScreenElementRoot.this.mCalendar = Calendar.getInstance();
        ScreenElementRoot.this.mHandler.post(new Runnable()
        {
          public void run()
          {
            ScreenElementRoot.this.updateTime();
          }
        });
      }
    }
  };
  private IndexedNumberVariable mMinute;
  private IndexedNumberVariable mMonth;
  private IndexedStringVariable mNextAlarm;
  private boolean mRegistered;
  private float mScale;
  private IndexedNumberVariable mScreenHeight;
  private IndexedNumberVariable mScreenWidth;
  private IndexedNumberVariable mSecond;
  private int mTargetDensity;
  private IndexedNumberVariable mTime;
  private IndexedNumberVariable mTimeSys;
  private IndexedNumberVariable mTouchBeginX;
  private IndexedNumberVariable mTouchBeginY;
  private IndexedNumberVariable mTouchX;
  private IndexedNumberVariable mTouchY;
  protected VariableBinderManager mVariableBinderManager;
  private IndexedNumberVariable mYear;

  public ScreenElementRoot(ScreenContext paramScreenContext)
  {
    super(null, paramScreenContext);
    this.mContext.mRoot = this;
    this.mAmPm = new IndexedNumberVariable("ampm", this.mContext.mVariables);
    this.mHour12 = new IndexedNumberVariable("hour12", this.mContext.mVariables);
    this.mHour24 = new IndexedNumberVariable("hour24", this.mContext.mVariables);
    this.mMinute = new IndexedNumberVariable("minute", this.mContext.mVariables);
    this.mSecond = new IndexedNumberVariable("second", this.mContext.mVariables);
    this.mYear = new IndexedNumberVariable("year", this.mContext.mVariables);
    this.mMonth = new IndexedNumberVariable("month", this.mContext.mVariables);
    this.mDate = new IndexedNumberVariable("date", this.mContext.mVariables);
    this.mDayOfWeek = new IndexedNumberVariable("day_of_week", this.mContext.mVariables);
    this.mTime = new IndexedNumberVariable("time", this.mContext.mVariables);
    this.mTimeSys = new IndexedNumberVariable("time_sys", this.mContext.mVariables);
    this.mBatteryLevel = new IndexedNumberVariable("battery_level", this.mContext.mVariables);
    this.mScreenWidth = new IndexedNumberVariable("screen_width", this.mContext.mVariables);
    this.mScreenHeight = new IndexedNumberVariable("screen_height", this.mContext.mVariables);
    this.mTouchX = new IndexedNumberVariable("touch_x", this.mContext.mVariables);
    this.mTouchY = new IndexedNumberVariable("touch_y", this.mContext.mVariables);
    this.mTouchBeginX = new IndexedNumberVariable("touch_begin_x", this.mContext.mVariables);
    this.mTouchBeginY = new IndexedNumberVariable("touch_begin_y", this.mContext.mVariables);
    this.mNextAlarm = new IndexedStringVariable("next_alarm_time", this.mContext.mVariables);
    initEarlyVariables();
    updateTime();
  }

  private void refreshAlarm()
  {
    String str = Settings.System.getString(this.mContext.mContext.getContentResolver(), "next_alarm_formatted");
    this.mNextAlarm.set(str);
  }

  private void registerReceiver()
  {
    if (this.mRegistered);
    while (true)
    {
      return;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.TIME_TICK");
      localIntentFilter.addAction("android.intent.action.TIME_SET");
      localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
      localIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
      this.mContext.mContext.registerReceiver(this.mIntentReceiver, localIntentFilter);
      this.mRegistered = true;
    }
  }

  private void setScreen()
  {
    int i = 1;
    Display localDisplay = ((WindowManager)this.mContext.mContext.getSystemService("window")).getDefaultDisplay();
    int j = localDisplay.getWidth();
    int k = localDisplay.getHeight();
    int m = localDisplay.getRotation();
    int n;
    label60: int i1;
    if ((m == i) || (m == 3))
    {
      if (i == 0)
        break label180;
      n = k;
      if (i == 0)
        break label186;
      i1 = j;
      label67: if (this.mTargetDensity != 0)
        break label193;
      this.mScale = (n / this.mDefaultScreenWidth);
      this.mTargetDensity = Math.round(240.0F * this.mScale);
    }
    while (true)
    {
      Log.i("ScreenElementRoot", "init target density: " + this.mTargetDensity);
      this.mContext.setTargetDensity(this.mTargetDensity);
      this.mScreenWidth.set(n / this.mScale);
      this.mScreenHeight.set(i1 / this.mScale);
      return;
      i = 0;
      break;
      label180: n = j;
      break label60;
      label186: i1 = k;
      break label67;
      label193: this.mScale = (this.mTargetDensity / getResourceDensity());
    }
  }

  private void unregisterReceiver()
  {
    if (!this.mRegistered);
    while (true)
    {
      return;
      this.mContext.mContext.unregisterReceiver(this.mIntentReceiver);
      this.mRegistered = false;
    }
  }

  private void updateTime()
  {
    long l = System.currentTimeMillis();
    this.mTimeSys.set(l);
    this.mCalendar.setTimeInMillis(l);
    this.mAmPm.set(this.mCalendar.get(9));
    this.mHour12.set(this.mCalendar.get(10));
    this.mHour24.set(this.mCalendar.get(11));
    this.mMinute.set(this.mCalendar.get(12));
    this.mYear.set(this.mCalendar.get(1));
    this.mMonth.set(this.mCalendar.get(2));
    this.mDate.set(this.mCalendar.get(5));
    this.mDayOfWeek.set(this.mCalendar.get(7));
    this.mSecond.set(this.mCalendar.get(13));
  }

  protected ElementGroup createElementGroup(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    return new ElementGroup(paramElement, this.mContext);
  }

  public ScreenElement findElement(String paramString)
  {
    if (this.mElementGroup != null);
    for (ScreenElement localScreenElement = this.mElementGroup.findElement(paramString); ; localScreenElement = null)
      return localScreenElement;
  }

  public void finish()
  {
    if (this.mElementGroup != null)
      this.mElementGroup.finish();
    this.mElementGroup = null;
    if (this.mVariableBinderManager != null)
      this.mVariableBinderManager.finish();
    this.mContext.mResourceManager.clear();
    unregisterReceiver();
  }

  public ScreenContext getContext()
  {
    return this.mContext;
  }

  public int getDefaultScreenWidth()
  {
    return this.mDefaultScreenWidth;
  }

  public int getFrameRate()
  {
    return this.mFrameRate;
  }

  public int getResourceDensity()
  {
    return 240;
  }

  public float getScale()
  {
    float f;
    if (this.mScale == 0.0F)
    {
      Log.w("ScreenElementRoot", "scale not initialized!");
      f = 1.0F;
    }
    while (true)
    {
      return f;
      f = this.mScale;
    }
  }

  public int getTargetDensity()
  {
    return this.mTargetDensity;
  }

  public void init()
  {
    if (this.mElementGroup != null)
      this.mElementGroup.init();
    if (this.mVariableBinderManager != null)
      this.mVariableBinderManager.init();
    registerReceiver();
    refreshAlarm();
  }

  protected void initEarlyVariables()
  {
    int i = 1;
    this.mTimeSys.set(System.currentTimeMillis());
    this.mBatteryLevel.set(this.mContext.mBatteryLevel);
    IndexedNumberVariable localIndexedNumberVariable;
    double d;
    if ((Settings.System.getInt(this.mContext.mContext.getContentResolver(), "pref_key_enable_notification_body", i) == i) && (!FirewallManager.isAccessControlProtected(this.mContext.mContext, "com.android.mms")) && (!new ChooseLockSettingsHelper(this.mContext.mContext).isPrivacyModeEnabled()))
    {
      localIndexedNumberVariable = new IndexedNumberVariable("sms_body_preview", this.mContext.mVariables);
      if (i == 0)
        break label120;
      d = 1.0D;
    }
    while (true)
    {
      localIndexedNumberVariable.set(d);
      return;
      i = 0;
      break;
      label120: d = 0.0D;
    }
  }

  public boolean load()
  {
    int i = 0;
    try
    {
      Element localElement = this.mContext.mResourceManager.getManifestRoot();
      if (localElement != null)
      {
        this.mFrameRate = Utils.getAttrAsInt(localElement, "frameRate", 30);
        int j = Utils.getAttrAsInt(localElement, "screenWidth", 0);
        if (j > 0);
        while (true)
        {
          this.mDefaultScreenWidth = j;
          this.mContext.setResourceDensity(getResourceDensity());
          this.mElementGroup = createElementGroup(localElement, this.mContext);
          this.mVariableBinderManager = new VariableBinderManager(Utils.getChild(localElement, "VariableBinders"), this.mContext);
          setScreen();
          boolean bool = onLoad(localElement);
          i = bool;
          break;
          j = 480;
        }
      }
    }
    catch (ScreenElementLoadException localScreenElementLoadException)
    {
      localScreenElementLoadException.printStackTrace();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return i;
  }

  public void onButtonInteractive(ButtonScreenElement paramButtonScreenElement, ButtonScreenElement.ButtonAction paramButtonAction)
  {
  }

  protected boolean onLoad(Element paramElement)
  {
    return true;
  }

  public void onTouch(MotionEvent paramMotionEvent)
  {
    if (this.mElementGroup == null)
      return;
    float f1 = descale(paramMotionEvent.getX());
    float f2 = descale(paramMotionEvent.getY());
    this.mTouchX.set(f1);
    this.mTouchY.set(f2);
    switch (paramMotionEvent.getActionMasked())
    {
    case 1:
    case 2:
    default:
    case 0:
    }
    while (true)
    {
      this.mElementGroup.onTouch(paramMotionEvent);
      break;
      this.mTouchBeginX.set(f1);
      this.mTouchBeginY.set(f2);
    }
  }

  public void pause()
  {
    if (this.mElementGroup != null)
      this.mElementGroup.pause();
    unregisterReceiver();
  }

  public void render(Canvas paramCanvas)
  {
    if (this.mElementGroup != null)
      this.mElementGroup.render(paramCanvas);
  }

  public void resume()
  {
    if (this.mElementGroup != null)
      this.mElementGroup.resume();
    registerReceiver();
    refreshAlarm();
  }

  public void setTargetDensity(int paramInt)
  {
    this.mTargetDensity = paramInt;
  }

  public void tick(long paramLong)
  {
    this.mTime.set(paramLong);
    updateTime();
    if (this.mElementGroup != null)
      this.mElementGroup.tick(paramLong);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ScreenElementRoot
 * JD-Core Version:    0.6.0
 */