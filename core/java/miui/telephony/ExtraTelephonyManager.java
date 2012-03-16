package miui.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings.System;
import com.android.internal.telephony.Call.State;

public class ExtraTelephonyManager
{
  public static final String ACTION_ENTER_INCALL_SCREEN_DURING_CALL = "android.intent.action.ENTER_INCALL_SCREEN_DURING_CALL";
  public static final String ACTION_LEAVE_INCALL_SCREEN_DURING_CALL = "android.intent.action.LEAVE_INCALL_SCREEN_DURING_CALL";
  public static final String EXTRA_BASE_TIME = "base_time";
  public static final String EXTRA_CALL_STATE = "call_state";
  public static final String EXTRA_CALL_STATE_ACTIVE;
  public static final String EXTRA_CALL_STATE_DIALING = Call.State.DIALING.toString();
  public static final String EXTRA_CALL_STATE_HOLDING;

  static
  {
    EXTRA_CALL_STATE_ACTIVE = Call.State.ACTIVE.toString();
    EXTRA_CALL_STATE_HOLDING = Call.State.HOLDING.toString();
  }

  public static String getSimOperator(Context paramContext)
  {
    Object localObject = SystemProperties.get("gsm.sim.operator.numeric");
    if (paramContext == null);
    while (true)
    {
      return localObject;
      if ((!"com.android.vending".equals(paramContext.getPackageName())) || (!PhoneNumberUtils.isChineseOperator((String)localObject)))
        continue;
      String str = Settings.System.getString(paramContext.getContentResolver(), "fake_mobile_operator_for_vending");
      if (str != null)
      {
        if (str.length() == 0)
          continue;
        localObject = str;
        continue;
      }
      localObject = "310410";
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.telephony.ExtraTelephonyManager
 * JD-Core Version:    0.6.0
 */