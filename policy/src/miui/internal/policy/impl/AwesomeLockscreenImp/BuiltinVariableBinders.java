package com.miui.internal.policy.impl.AwesomeLockScreenImp;

import miui.app.screenelement.ContentProviderBinder.Builder;
import miui.app.screenelement.VariableBinderManager;

public class BuiltinVariableBinders
{
  public static void fill(VariableBinderManager paramVariableBinderManager)
  {
    fillMissedCall(paramVariableBinderManager);
    fillUnreadSms(paramVariableBinderManager);
  }

  private static void fillMissedCall(VariableBinderManager paramVariableBinderManager)
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "number";
    paramVariableBinderManager.addContentProviderBinder("content://call_log/calls").setColumns(arrayOfString).setWhere("type=3 AND new=1").setCountName("call_missed_count");
  }

  private static void fillUnreadSms(VariableBinderManager paramVariableBinderManager)
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "_id";
    paramVariableBinderManager.addContentProviderBinder("content://mms-sms/complete-conversations").setColumns(arrayOfString).setWhere("seen=0").setCountName("sms_unread_count");
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AwesomeLockScreenImp.BuiltinVariableBinders
 * JD-Core Version:    0.6.0
 */