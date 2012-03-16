package miui.app.screenelement;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class Variables
{
  public static final String ACTUAL_H = "actual_h";
  public static final String ACTUAL_W = "actual_w";
  public static final String ACTUAL_X = "actual_x";
  public static final String ACTUAL_Y = "actual_y";
  public static final String AMPM = "ampm";
  public static final String BATTERY_LEVEL = "battery_level";
  public static final String BATTERY_STATE = "battery_state";
  public static final int BATTERY_STATE_CHARGING = 1;
  public static final int BATTERY_STATE_FULL = 3;
  public static final int BATTERY_STATE_LOW = 2;
  public static final int BATTERY_STATE_UNPLUGGED = 0;
  public static final String BLUETOOTH_STATE = "bluetooth_state";
  public static final String CALL_MISSED_COUNT = "call_missed_count";
  public static final String DATA_STATE = "data_state";
  public static final String DATE = "date";
  public static final String DAY_OF_WEEK = "day_of_week";
  private static boolean DBG = false;
  public static final String FRAME_RATE = "frame_rate";
  private static final String GLOBAL = "__global";
  public static final String HOUR12 = "hour12";
  public static final String HOUR24 = "hour24";
  private static final String LOG_TAG = "Variables";
  public static final String MILLISECOND = "msec";
  public static final String MINUTE = "minute";
  public static final String MONTH = "month";
  public static final String MOVE_DIST = "move_dist";
  public static final String MOVE_X = "move_x";
  public static final String MOVE_Y = "move_y";
  public static final String MUSIC_STATE = "music_state";
  public static final int MUSIC_STATE_PLAY = 1;
  public static final int MUSIC_STATE_STOP = 0;
  public static final String NEXT_ALARM_TIME = "next_alarm_time";
  public static final String PLMN = "plmn";
  public static final String RING_MODE = "ring_mode";
  public static final String SCREEN_HEIGHT = "screen_height";
  public static final String SCREEN_WIDTH = "screen_width";
  public static final String SECOND = "second";
  public static final String SMS_BODY_PREVIEW = "sms_body_preview";
  public static final String SMS_UNREAD_COUNT = "sms_unread_count";
  public static final String SPN = "spn";
  public static final String STATE = "state";
  public static final String TEXT_WIDTH = "text_width";
  public static final String TIME = "time";
  public static final String TIME_SYS = "time_sys";
  public static final String TOUCH_BEGIN_X = "touch_begin_x";
  public static final String TOUCH_BEGIN_Y = "touch_begin_y";
  public static final String TOUCH_X = "touch_x";
  public static final String TOUCH_Y = "touch_y";
  public static final int UNLOCKER_STATE_NORMAL = 0;
  public static final int UNLOCKER_STATE_PRESSED = 1;
  public static final int UNLOCKER_STATE_REACHED = 2;
  public static final String USB_MODE = "usb_mode";
  public static final String VISIBILITY = "visibility";
  public static final int VISIBILITY_FALSE = 0;
  public static final int VISIBILITY_TRUE = 1;
  public static final String WIFI_STATE = "wifi_state";
  public static final String YEAR = "year";
  private ArrayList<Double> mDoubleArray = new ArrayList();
  private int mNextDoubleIndex = 0;
  private int mNextStringIndex = 0;
  private Object mNumLock = new Object();
  private HashMap<String, HashMap<String, Integer>> mNumObjects = new HashMap();
  private Object mStrLock = new Object();
  private HashMap<String, HashMap<String, Integer>> mStrObjects = new HashMap();
  private ArrayList<String> mStringArray = new ArrayList();

  private int getIndex(HashMap<String, HashMap<String, Integer>> paramHashMap, String paramString1, String paramString2, int paramInt)
  {
    if (paramString1 == null)
      paramString1 = "__global";
    HashMap localHashMap = (HashMap)paramHashMap.get(paramString1);
    if (localHashMap == null)
    {
      localHashMap = new HashMap();
      paramHashMap.put(paramString1, localHashMap);
    }
    Integer localInteger = (Integer)localHashMap.get(paramString2);
    if (localInteger == null)
    {
      localInteger = Integer.valueOf(paramInt);
      localHashMap.put(paramString2, localInteger);
    }
    return localInteger.intValue();
  }

  public Double getNum(int paramInt)
  {
    Object localObject1 = this.mNumLock;
    monitorenter;
    if (paramInt >= -1);
    Double localDouble;
    try
    {
      if (paramInt > -1 + this.mDoubleArray.size())
      {
        localDouble = null;
        monitorexit;
      }
      else
      {
        localDouble = (Double)this.mDoubleArray.get(paramInt);
        monitorexit;
      }
    }
    finally
    {
      localObject2 = finally;
      monitorexit;
      throw localObject2;
    }
    return localDouble;
  }

  public String getStr(int paramInt)
  {
    Object localObject1 = this.mStrLock;
    monitorenter;
    if (paramInt >= -1);
    String str;
    try
    {
      if (paramInt > -1 + this.mStringArray.size())
      {
        str = null;
        monitorexit;
      }
      else
      {
        str = (String)this.mStringArray.get(paramInt);
        monitorexit;
      }
    }
    finally
    {
      localObject2 = finally;
      monitorexit;
      throw localObject2;
    }
    return str;
  }

  public void putNum(int paramInt, double paramDouble)
  {
    putNum(paramInt, Double.valueOf(paramDouble));
  }

  // ERROR //
  public void putNum(int paramInt, Double paramDouble)
  {
    // Byte code:
    //   0: iload_1
    //   1: ifge +4 -> 5
    //   4: return
    //   5: aload_0
    //   6: getfield 199	miui/app/screenelement/Variables:mNumLock	Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: bipush 255
    //   15: aload_0
    //   16: getfield 195	miui/app/screenelement/Variables:mDoubleArray	Ljava/util/ArrayList;
    //   19: invokevirtual 226	java/util/ArrayList:size	()I
    //   22: iadd
    //   23: if_icmple +22 -> 45
    //   26: aload_0
    //   27: getfield 195	miui/app/screenelement/Variables:mDoubleArray	Ljava/util/ArrayList;
    //   30: aconst_null
    //   31: invokevirtual 247	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   34: pop
    //   35: goto -23 -> 12
    //   38: astore 4
    //   40: aload_3
    //   41: monitorexit
    //   42: aload 4
    //   44: athrow
    //   45: aload_0
    //   46: getfield 195	miui/app/screenelement/Variables:mDoubleArray	Ljava/util/ArrayList;
    //   49: iload_1
    //   50: aload_2
    //   51: invokevirtual 251	java/util/ArrayList:set	(ILjava/lang/Object;)Ljava/lang/Object;
    //   54: pop
    //   55: aload_3
    //   56: monitorexit
    //   57: goto -53 -> 4
    //
    // Exception table:
    //   from	to	target	type
    //   12	42	38	finally
    //   45	57	38	finally
  }

  // ERROR //
  public void putStr(int paramInt, String paramString)
  {
    // Byte code:
    //   0: iload_1
    //   1: ifge +4 -> 5
    //   4: return
    //   5: aload_0
    //   6: getfield 201	miui/app/screenelement/Variables:mStrLock	Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: monitorenter
    //   12: iload_1
    //   13: bipush 255
    //   15: aload_0
    //   16: getfield 197	miui/app/screenelement/Variables:mStringArray	Ljava/util/ArrayList;
    //   19: invokevirtual 226	java/util/ArrayList:size	()I
    //   22: iadd
    //   23: if_icmple +22 -> 45
    //   26: aload_0
    //   27: getfield 197	miui/app/screenelement/Variables:mStringArray	Ljava/util/ArrayList;
    //   30: aconst_null
    //   31: invokevirtual 247	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   34: pop
    //   35: goto -23 -> 12
    //   38: astore 4
    //   40: aload_3
    //   41: monitorexit
    //   42: aload 4
    //   44: athrow
    //   45: aload_0
    //   46: getfield 197	miui/app/screenelement/Variables:mStringArray	Ljava/util/ArrayList;
    //   49: iload_1
    //   50: aload_2
    //   51: invokevirtual 251	java/util/ArrayList:set	(ILjava/lang/Object;)Ljava/lang/Object;
    //   54: pop
    //   55: aload_3
    //   56: monitorexit
    //   57: goto -53 -> 4
    //
    // Exception table:
    //   from	to	target	type
    //   12	42	38	finally
    //   45	57	38	finally
  }

  public int registerNumberVariable(String paramString1, String paramString2)
  {
    synchronized (this.mNumLock)
    {
      int i = getIndex(this.mNumObjects, paramString1, paramString2, this.mNextDoubleIndex);
      if (i == this.mNextDoubleIndex)
        this.mNextDoubleIndex = (1 + this.mNextDoubleIndex);
      if (DBG)
        Log.d("Variables", "registerNumberVariable: " + paramString1 + "." + paramString2 + "  index:" + i);
      return i;
    }
  }

  public int registerStringVariable(String paramString1, String paramString2)
  {
    synchronized (this.mStrLock)
    {
      int i = getIndex(this.mStrObjects, paramString1, paramString2, this.mNextStringIndex);
      if (i == this.mNextStringIndex)
        this.mNextStringIndex = (1 + this.mNextStringIndex);
      if (DBG)
        Log.d("Variables", "registerStringVariable: " + paramString1 + "." + paramString2 + "  index:" + i);
      return i;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.Variables
 * JD-Core Version:    0.6.0
 */