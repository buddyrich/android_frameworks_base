package miui.app.screenelement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import java.util.Calendar;
import org.w3c.dom.Element;

public class TimepanelScreenElement extends ImageScreenElement
{
  private static final String LOG_TAG = "TimepanelScreenElement";
  private static final String M12 = "hh:mm";
  private static final String M24 = "kk:mm";
  public static final String TAG_NAME = "Time";
  private int mBmpHeight;
  private int mBmpWidth;
  protected Calendar mCalendar = Calendar.getInstance();
  private String mFormat = "kk:mm";
  private final Handler mHandler = new Handler();
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("android.intent.action.TIMEZONE_CHANGED"))
        TimepanelScreenElement.this.mCalendar = Calendar.getInstance();
      TimepanelScreenElement.this.mHandler.post(new Runnable()
      {
        public void run()
        {
          try
          {
            TimepanelScreenElement.this.updateTime();
            return;
          }
          catch (Exception localException)
          {
            while (true)
            {
              Log.e("TimepanelScreenElement", "fail to updateTime: " + localException.toString());
              localException.printStackTrace();
            }
          }
        }
      });
    }
  };
  private Paint mPaint = new Paint();
  private boolean mRegistered;

  public TimepanelScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
  }

  private Bitmap getDigitBmp(char paramChar)
  {
    String str1;
    if (TextUtils.isEmpty(this.mAni.getBmp()))
    {
      str1 = "time.png";
      if (paramChar != ':')
        break label52;
    }
    label52: for (String str2 = "dot"; ; str2 = String.valueOf(paramChar))
    {
      return this.mContext.mResourceManager.getBitmap(Utils.addFileNameSuffix(str1, str2));
      str1 = this.mAni.getBmp();
      break;
    }
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
      this.mContext.mContext.registerReceiver(this.mIntentReceiver, localIntentFilter);
      this.mRegistered = true;
    }
  }

  private void setDateFormat()
  {
    if (DateFormat.is24HourFormat(this.mContext.mContext));
    for (String str = "kk:mm"; ; str = "hh:mm")
    {
      this.mFormat = str;
      return;
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
    Bitmap localBitmap2;
    Bitmap localBitmap3;
    if (this.mBitmap == null)
    {
      localBitmap2 = getDigitBmp('0');
      localBitmap3 = getDigitBmp(':');
      if ((localBitmap2 != null) && (localBitmap3 != null));
    }
    while (true)
    {
      return;
      this.mBmpHeight = localBitmap2.getHeight();
      this.mBitmap = Bitmap.createBitmap(4 * localBitmap2.getWidth() + localBitmap3.getWidth(), localBitmap2.getHeight(), Bitmap.Config.ARGB_8888);
      this.mBitmap.setDensity(localBitmap2.getDensity());
      setActualHeight(this.mBmpHeight);
      Canvas localCanvas = new Canvas(this.mBitmap);
      localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
      this.mCalendar.setTimeInMillis(System.currentTimeMillis());
      CharSequence localCharSequence = DateFormat.format(this.mFormat, this.mCalendar);
      int i = 0;
      int j;
      for (int k = 0; k < localCharSequence.length(); k++)
      {
        Bitmap localBitmap1 = getDigitBmp(localCharSequence.charAt(k));
        if (localBitmap1 == null)
          continue;
        localCanvas.drawBitmap(localBitmap1, i, 0.0F, null);
        i += localBitmap1.getWidth();
      }
      this.mBmpWidth = j;
      setActualWidth(this.mBmpWidth);
      this.mContext.mShouldUpdate = true;
    }
  }

  public void finish()
  {
    unregisterReceiver();
  }

  public void init()
  {
    super.init();
    setDateFormat();
    registerReceiver();
    updateTime();
  }

  public void pause()
  {
    unregisterReceiver();
  }

  public void resume()
  {
    updateTime();
    registerReceiver();
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.TimepanelScreenElement
 * JD-Core Version:    0.6.0
 */