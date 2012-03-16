package com.android.internal.policy.impl;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings.System;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.app.ShutdownThread;
import java.util.ArrayList;
import miui.security.ChooseLockSettingsHelper;

class MiuiGlobalActions
  implements DialogInterface.OnDismissListener, DialogInterface.OnClickListener
{
  private static final int DIALOG_DISMISS_DELAY = 300;
  private static final int MESSAGE_DISMISS = 0;
  private static final int MESSAGE_REFRESH = 1;
  private static final boolean SHOW_SILENT_TOGGLE = true;
  private static final String TAG = "GlobalActions";
  private MyAdapter mAdapter;
  private ToggleAction mAirplaneModeOn;
  private MiuiGlobalActions.ToggleAction.State mAirplaneState = MiuiGlobalActions.ToggleAction.State.Off;
  private final AudioManager mAudioManager;
  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str = paramIntent.getAction();
      if (("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(str)) || ("android.intent.action.SCREEN_OFF".equals(str)))
        if (!"globalactions".equals(paramIntent.getStringExtra("reason")))
          MiuiGlobalActions.this.mHandler.sendEmptyMessage(0);
      while (true)
      {
        return;
        if (("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED".equals(str)) && (!paramIntent.getBooleanExtra("PHONE_IN_ECM_STATE", false)) && (MiuiGlobalActions.this.mIsWaitingForEcmExit))
        {
          MiuiGlobalActions.access$002(MiuiGlobalActions.this, false);
          MiuiGlobalActions.this.changeAirplaneModeSystemSetting(true);
          continue;
        }
      }
    }
  };
  private final Context mContext;
  private boolean mDeviceProvisioned = false;
  private AlertDialog mDialog;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 0)
        if (MiuiGlobalActions.this.mDialog != null)
          MiuiGlobalActions.this.mDialog.dismiss();
      while (true)
      {
        return;
        if (paramMessage.what == 1)
        {
          MiuiGlobalActions.this.mAdapter.notifyDataSetChanged();
          continue;
        }
      }
    }
  };
  private boolean mIsWaitingForEcmExit = false;
  private ArrayList<Action> mItems;
  private boolean mKeyguardShowing = false;
  private SinglePressAction mMuteToggle;
  PhoneStateListener mPhoneStateListener = new PhoneStateListener()
  {
    public void onServiceStateChanged(ServiceState paramServiceState)
    {
      int i;
      MiuiGlobalActions localMiuiGlobalActions;
      if (paramServiceState.getState() == 3)
      {
        i = 1;
        localMiuiGlobalActions = MiuiGlobalActions.this;
        if (i == 0)
          break label64;
      }
      label64: for (MiuiGlobalActions.ToggleAction.State localState = MiuiGlobalActions.ToggleAction.State.On; ; localState = MiuiGlobalActions.ToggleAction.State.Off)
      {
        MiuiGlobalActions.access$302(localMiuiGlobalActions, localState);
        MiuiGlobalActions.this.mAirplaneModeOn.updateState(MiuiGlobalActions.this.mAirplaneState);
        MiuiGlobalActions.this.mAdapter.notifyDataSetChanged();
        return;
        i = 0;
        break;
      }
    }
  };
  private BroadcastReceiver mRingerModeReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("android.media.RINGER_MODE_CHANGED"))
        MiuiGlobalActions.this.mHandler.sendEmptyMessage(1);
    }
  };
  private ChooseLockSettingsHelper mSecurityHelper = null;
  private SilentModeAction mSilentModeAction;

  public MiuiGlobalActions(Context paramContext)
  {
    this.mContext = new ContextThemeWrapper(paramContext, 16973939);
    this.mAudioManager = ((AudioManager)this.mContext.getSystemService("audio"));
    this.mSecurityHelper = new ChooseLockSettingsHelper(paramContext);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
    localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
    localIntentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
    paramContext.registerReceiver(this.mBroadcastReceiver, localIntentFilter);
    ((TelephonyManager)paramContext.getSystemService("phone")).listen(this.mPhoneStateListener, 1);
  }

  private void changeAirplaneModeSystemSetting(boolean paramBoolean)
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      Settings.System.putInt(localContentResolver, "airplane_mode_on", i);
      Intent localIntent = new Intent("android.intent.action.AIRPLANE_MODE");
      localIntent.addFlags(536870912);
      localIntent.putExtra("state", paramBoolean);
      this.mContext.sendBroadcast(localIntent);
      return;
    }
  }

  private AlertDialog createDialog()
  {
    this.mSilentModeAction = new SilentModeAction(this.mAudioManager, this.mHandler);
    this.mAirplaneModeOn = new ToggleAction(50463002, 50463003, 51118472, 51118473, 51118474)
    {
      protected void changeStateFromPress(boolean paramBoolean)
      {
        if (!Boolean.parseBoolean(SystemProperties.get("ril.cdma.inecmmode")))
          if (!paramBoolean)
            break label37;
        label37: for (MiuiGlobalActions.ToggleAction.State localState = MiuiGlobalActions.ToggleAction.State.TurningOn; ; localState = MiuiGlobalActions.ToggleAction.State.TurningOff)
        {
          this.mState = localState;
          MiuiGlobalActions.access$302(MiuiGlobalActions.this, this.mState);
          return;
        }
      }

      void onToggle(boolean paramBoolean)
      {
        if (Boolean.parseBoolean(SystemProperties.get("ril.cdma.inecmmode")))
        {
          MiuiGlobalActions.access$002(MiuiGlobalActions.this, true);
          Intent localIntent = new Intent("android.intent.action.ACTION_SHOW_NOTICE_ECM_BLOCK_OTHERS", null);
          localIntent.addFlags(268435456);
          MiuiGlobalActions.this.mContext.startActivity(localIntent);
        }
        while (true)
        {
          return;
          MiuiGlobalActions.this.changeAirplaneModeSystemSetting(paramBoolean);
        }
      }

      public boolean showBeforeProvisioning()
      {
        return false;
      }

      public boolean showDuringKeyguard()
      {
        return true;
      }
    };
    this.mItems = new ArrayList();
    this.mItems.add(this.mAirplaneModeOn);
    this.mMuteToggle = new SinglePressAction(getMuteIconResId(), 51118485)
    {
      public void onPress()
      {
        int i = 2;
        if (MiuiGlobalActions.this.mAudioManager.getRingerMode() == i)
          i = 0;
        MiuiGlobalActions.this.mAudioManager.setRingerMode(i);
      }

      public boolean showBeforeProvisioning()
      {
        return true;
      }

      public boolean showDuringKeyguard()
      {
        return true;
      }
    };
    this.mItems.add(this.mMuteToggle);
    this.mItems.add(new SinglePressAction(50463005, 51118476)
    {
      public void onPress()
      {
        ShutdownThread.reboot(MiuiGlobalActions.this.mContext, null, true);
      }

      public boolean showBeforeProvisioning()
      {
        return true;
      }

      public boolean showDuringKeyguard()
      {
        return true;
      }
    });
    this.mItems.add(new SinglePressAction(50463004, 51118475)
    {
      public void onPress()
      {
        ShutdownThread.shutdown(MiuiGlobalActions.this.mContext, true);
      }

      public boolean showBeforeProvisioning()
      {
        return true;
      }

      public boolean showDuringKeyguard()
      {
        return true;
      }
    });
    this.mAdapter = new MyAdapter(null);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.mContext, 3);
    localBuilder.setAdapter(this.mAdapter, this).setInverseBackgroundForced(true);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getListView().setItemsCanFocus(true);
    localAlertDialog.getWindow().setType(2008);
    localAlertDialog.setOnDismissListener(this);
    return localAlertDialog;
  }

  private int getMuteIconResId()
  {
    int i;
    if (this.mAudioManager.getRingerMode() != 2)
    {
      i = 1;
      if (i == 0)
        break label28;
    }
    label28: for (int j = 50463001; ; j = 50463000)
    {
      return j;
      i = 0;
      break;
    }
  }

  private void prepareDialog()
  {
    this.mAirplaneModeOn.updateState(this.mAirplaneState);
    this.mMuteToggle.setIcon(getMuteIconResId());
    this.mAdapter.notifyDataSetChanged();
    if (this.mKeyguardShowing)
      this.mDialog.getWindow().setType(2009);
    while (true)
    {
      IntentFilter localIntentFilter = new IntentFilter("android.media.RINGER_MODE_CHANGED");
      this.mContext.registerReceiver(this.mRingerModeReceiver, localIntentFilter);
      return;
      this.mDialog.getWindow().setType(2008);
    }
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (!(this.mAdapter.getItem(paramInt) instanceof SilentModeAction))
      paramDialogInterface.dismiss();
    this.mAdapter.getItem(paramInt).onPress();
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    this.mContext.unregisterReceiver(this.mRingerModeReceiver);
  }

  public void showDialog(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mKeyguardShowing = paramBoolean1;
    this.mDeviceProvisioned = paramBoolean2;
    if (this.mDialog == null)
      this.mDialog = createDialog();
    prepareDialog();
    this.mDialog.show();
    this.mDialog.getWindow().getDecorView().setSystemUiVisibility(65536);
  }

  private static class SilentModeAction
    implements MiuiGlobalActions.Action, View.OnClickListener
  {
    private final int[] ICON_IDS;
    private final int[] ITEM_IDS;
    private final AudioManager mAudioManager;
    private final Handler mHandler;

    SilentModeAction(AudioManager paramAudioManager, Handler paramHandler)
    {
      int[] arrayOfInt1 = new int[3];
      arrayOfInt1[0] = 51052594;
      arrayOfInt1[1] = 51052595;
      arrayOfInt1[2] = 51052596;
      this.ITEM_IDS = arrayOfInt1;
      int[] arrayOfInt2 = new int[3];
      arrayOfInt2[0] = 50463001;
      arrayOfInt2[1] = 50462999;
      arrayOfInt2[2] = 50463000;
      this.ICON_IDS = arrayOfInt2;
      this.mAudioManager = paramAudioManager;
      this.mHandler = paramHandler;
    }

    private int indexToRingerMode(int paramInt)
    {
      return paramInt;
    }

    private int ringerModeToIndex(int paramInt)
    {
      return paramInt;
    }

    public View create(Context paramContext, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
    {
      View localView = paramLayoutInflater.inflate(50528290, paramViewGroup, false);
      int i = ringerModeToIndex(this.mAudioManager.getRingerMode());
      int j = 0;
      if (j < 3)
      {
        LinearLayout localLinearLayout = (LinearLayout)localView.findViewById(this.ITEM_IDS[j]);
        if (i == j);
        for (boolean bool = true; ; bool = false)
        {
          localLinearLayout.setSelected(bool);
          localLinearLayout.setTag(Integer.valueOf(j));
          localLinearLayout.setOnClickListener(this);
          ((ImageView)localLinearLayout.getChildAt(0)).setImageResource(this.ICON_IDS[j]);
          j++;
          break;
        }
      }
      return localView;
    }

    public boolean isEnabled()
    {
      return true;
    }

    public void onClick(View paramView)
    {
      if (!(paramView.getTag() instanceof Integer));
      while (true)
      {
        return;
        int i = ((Integer)paramView.getTag()).intValue();
        this.mAudioManager.setRingerMode(indexToRingerMode(i));
        this.mHandler.sendEmptyMessageDelayed(0, 300L);
      }
    }

    public void onPress()
    {
    }

    public boolean showBeforeProvisioning()
    {
      return false;
    }

    public boolean showDuringKeyguard()
    {
      return true;
    }

    void willCreate()
    {
    }
  }

  private static abstract class ToggleAction
    implements MiuiGlobalActions.Action
  {
    protected int mDisabledIconResid;
    protected int mDisabledStatusMessageResId;
    protected int mEnabledIconResId;
    protected int mEnabledStatusMessageResId;
    protected int mMessageResId;
    protected State mState = State.Off;

    public ToggleAction(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      this.mEnabledIconResId = paramInt1;
      this.mDisabledIconResid = paramInt2;
      this.mMessageResId = paramInt3;
      this.mEnabledStatusMessageResId = paramInt4;
      this.mDisabledStatusMessageResId = paramInt5;
    }

    protected void changeStateFromPress(boolean paramBoolean)
    {
      if (paramBoolean);
      for (State localState = State.On; ; localState = State.Off)
      {
        this.mState = localState;
        return;
      }
    }

    public View create(Context paramContext, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
    {
      int i = 0;
      willCreate();
      View localView = paramLayoutInflater.inflate(50528289, paramViewGroup, false);
      ImageView localImageView = (ImageView)localView.findViewById(51052592);
      TextView localTextView1 = (TextView)localView.findViewById(51052593);
      TextView localTextView2 = (TextView)localView.findViewById(51052577);
      boolean bool = isEnabled();
      if (localTextView1 != null)
      {
        localTextView1.setText(this.mMessageResId);
        localTextView1.setEnabled(bool);
      }
      if ((this.mState == State.On) || (this.mState == State.TurningOn))
        i = 1;
      int k;
      if (localImageView != null)
      {
        Resources localResources = paramContext.getResources();
        if (i != 0)
        {
          k = this.mEnabledIconResId;
          localImageView.setImageDrawable(localResources.getDrawable(k));
          localImageView.setEnabled(bool);
        }
      }
      else if (localTextView2 != null)
      {
        if (i == 0)
          break label201;
      }
      label201: for (int j = this.mEnabledStatusMessageResId; ; j = this.mDisabledStatusMessageResId)
      {
        localTextView2.setText(j);
        localTextView2.setVisibility(8);
        localTextView2.setEnabled(bool);
        localView.setEnabled(bool);
        return localView;
        k = this.mDisabledIconResid;
        break;
      }
    }

    public boolean isEnabled()
    {
      if (!this.mState.inTransition());
      for (int i = 1; ; i = 0)
        return i;
    }

    public final void onPress()
    {
      if (this.mState.inTransition())
      {
        Log.w("GlobalActions", "shouldn't be able to toggle when in transition");
        return;
      }
      if (this.mState != State.On);
      for (boolean bool = true; ; bool = false)
      {
        onToggle(bool);
        changeStateFromPress(bool);
        break;
      }
    }

    abstract void onToggle(boolean paramBoolean);

    public void updateState(State paramState)
    {
      this.mState = paramState;
    }

    void willCreate()
    {
    }

    static enum State
    {
      private final boolean inTransition;

      static
      {
        TurningOff = new State("TurningOff", 2, true);
        On = new State("On", 3, false);
        State[] arrayOfState = new State[4];
        arrayOfState[0] = Off;
        arrayOfState[1] = TurningOn;
        arrayOfState[2] = TurningOff;
        arrayOfState[3] = On;
        $VALUES = arrayOfState;
      }

      private State(boolean paramBoolean)
      {
        this.inTransition = paramBoolean;
      }

      public boolean inTransition()
      {
        return this.inTransition;
      }
    }
  }

  private static abstract class SinglePressAction
    implements MiuiGlobalActions.Action
  {
    private int mIconResId;
    private ImageView mIconView;
    private int mMessageResId;
    private TextView mTextview;

    protected SinglePressAction(int paramInt1, int paramInt2)
    {
      this.mIconResId = paramInt1;
      this.mMessageResId = paramInt2;
    }

    public View create(Context paramContext, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
    {
      View localView = paramLayoutInflater.inflate(50528289, paramViewGroup, false);
      this.mIconView = ((ImageView)localView.findViewById(51052592));
      this.mTextview = ((TextView)localView.findViewById(51052593));
      localView.findViewById(51052577).setVisibility(8);
      this.mIconView.setImageResource(this.mIconResId);
      this.mTextview.setText(this.mMessageResId);
      return localView;
    }

    public boolean isEnabled()
    {
      return true;
    }

    public abstract void onPress();

    public void setIcon(int paramInt)
    {
      this.mIconResId = paramInt;
      if (this.mIconView != null)
        this.mIconView.setImageResource(this.mIconResId);
    }

    public void setMessage(int paramInt)
    {
      this.mMessageResId = paramInt;
      if (this.mTextview != null)
        this.mTextview.setText(this.mMessageResId);
    }
  }

  private static abstract interface Action
  {
    public abstract View create(Context paramContext, View paramView, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater);

    public abstract boolean isEnabled();

    public abstract void onPress();

    public abstract boolean showBeforeProvisioning();

    public abstract boolean showDuringKeyguard();
  }

  private class MyAdapter extends BaseAdapter
  {
    private MyAdapter()
    {
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public int getCount()
    {
      int i = 0;
      int j = 0;
      if (j < MiuiGlobalActions.this.mItems.size())
      {
        MiuiGlobalActions.Action localAction = (MiuiGlobalActions.Action)MiuiGlobalActions.this.mItems.get(j);
        if ((MiuiGlobalActions.this.mKeyguardShowing) && (!localAction.showDuringKeyguard()));
        while (true)
        {
          j++;
          break;
          if ((!MiuiGlobalActions.this.mDeviceProvisioned) && (!localAction.showBeforeProvisioning()))
            continue;
          i++;
        }
      }
      return i;
    }

    public MiuiGlobalActions.Action getItem(int paramInt)
    {
      int i = 0;
      int j = 0;
      if (j < MiuiGlobalActions.this.mItems.size())
      {
        MiuiGlobalActions.Action localAction = (MiuiGlobalActions.Action)MiuiGlobalActions.this.mItems.get(j);
        if ((MiuiGlobalActions.this.mKeyguardShowing) && (!localAction.showDuringKeyguard()));
        while (true)
        {
          j++;
          break;
          if ((!MiuiGlobalActions.this.mDeviceProvisioned) && (!localAction.showBeforeProvisioning()))
            continue;
          if (i == paramInt)
            return localAction;
          i++;
        }
      }
      throw new IllegalArgumentException("position " + paramInt + " out of range of showable actions" + ", filtered count=" + getCount() + ", keyguardshowing=" + MiuiGlobalActions.this.mKeyguardShowing + ", provisioned=" + MiuiGlobalActions.this.mDeviceProvisioned);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return getItem(paramInt).create(MiuiGlobalActions.this.mContext, paramView, paramViewGroup, LayoutInflater.from(MiuiGlobalActions.this.mContext));
    }

    public boolean isEnabled(int paramInt)
    {
      return getItem(paramInt).isEnabled();
    }
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiGlobalActions
 * JD-Core Version:    0.6.0
 */