package com.android.internal.policy.impl;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.IActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;
import android.os.LocalPowerManager;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.view.IWindowManager;
import android.view.InputChannel;
import android.view.InputHandler;
import android.view.InputQueue;
import android.view.InputQueue.FinishedCallback;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerPolicy.ScreenOnListener;
import android.view.WindowManagerPolicy.WindowManagerFuncs;
import android.view.WindowManagerPolicy.WindowState;
import android.widget.Toast;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.view.BaseInputHandler;
import java.util.HashSet;
import miui.app.ExtraStatusBarManager;
import miui.provider.ExtraSettings.System;
import miui.util.HapticFeedbackUtil;

public class MiuiPhoneWindowManager extends PhoneWindowManager
{
  private static HashSet<String> sBackLongPressWhiteList = new HashSet();
  private BackLongPressRunnable mBackLongPress = new BackLongPressRunnable(null);
  boolean mBackLongPressed;
  private Binder mBinder = new Binder();
  boolean mCameraKeyWakeScreen;
  private HapticFeedbackUtil mHapticFeedbackUtil;
  private boolean mHasCameraFlash = false;
  private boolean mIsStatusBarVisibleInFullscreen;
  private boolean mMenuPressed;
  Runnable mPowerLongPressOriginal = this.mPowerLongPress;
  boolean mScreenButtonsDisabled;
  BroadcastReceiver mScreenshotReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      MiuiPhoneWindowManager.this.mHandler.removeCallbacks(MiuiPhoneWindowManager.this.mScreenshotChordLongPress);
      MiuiPhoneWindowManager.this.mHandler.postDelayed(MiuiPhoneWindowManager.this.mScreenshotChordLongPress, paramIntent.getLongExtra("capture_delay", 1000L));
    }
  };
  private boolean mScreenshotTrigger;
  BroadcastReceiver mStatusBarExitFullscreenReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      MiuiPhoneWindowManager.this.setStatusBarInFullscreen(false);
    }
  };
  private InputChannel mStatusBarInputChannel;
  private final InputHandler mStatusBarInputHandler = new BaseInputHandler()
  {
    private float mDownX;
    private float mDownY;

    public void handleMotion(MotionEvent paramMotionEvent, InputQueue.FinishedCallback paramFinishedCallback)
    {
      boolean bool1 = false;
      while (true)
      {
        try
        {
          if (((0x2 & paramMotionEvent.getSource()) == 0) || (MiuiPhoneWindowManager.this.mStatusBarService == null))
            continue;
          boolean bool2 = MiuiPhoneWindowManager.this.mIsStatusBarVisibleInFullscreen;
          if (!bool2)
            continue;
          paramFinishedCallback.finished(false);
          return;
          bool1 = true;
          synchronized (MiuiPhoneWindowManager.this.mLock)
          {
            switch (paramMotionEvent.getActionMasked())
            {
            default:
              paramFinishedCallback.finished(bool1);
              break;
            case 0:
              this.mDownX = paramMotionEvent.getRawX();
              this.mDownY = paramMotionEvent.getRawY();
            case 1:
            case 2:
            case 3:
            }
          }
        }
        finally
        {
          paramFinishedCallback.finished(bool1);
        }
        if (MiuiPhoneWindowManager.this.mStatusBarHeight / 2 < this.mDownY)
          continue;
        float f1 = Math.abs(this.mDownX - paramMotionEvent.getRawX());
        float f2 = Math.abs(this.mDownY - paramMotionEvent.getRawY());
        if ((2.0F * f1 > f2) || (2 * MiuiPhoneWindowManager.this.mStatusBarHeight > f2))
          continue;
        MiuiPhoneWindowManager.this.setStatusBarInFullscreen(true);
        this.mDownY = MiuiPhoneWindowManager.this.mStatusBarHeight;
      }
    }
  };
  private boolean mTorchEnabled = false;
  private boolean mVolumeDownPressed;
  private boolean mVolumeUpPressed;

  static
  {
    sBackLongPressWhiteList.add("com.android.systemui");
    sBackLongPressWhiteList.add("com.android.phone");
    sBackLongPressWhiteList.add("com.android.mms");
    sBackLongPressWhiteList.add("com.android.contacts");
    sBackLongPressWhiteList.add("com.miui.home");
    sBackLongPressWhiteList.add("com.miui.fmradio");
  }

  private boolean isEnableKeyguardTorch()
  {
    if ((this.mHasCameraFlash) && (this.mKeyguardMediator.isShowingAndNotHidden()));
    for (int i = 1; ; i = 0)
      return i;
  }

  private void setMaxBacklightBrightness()
  {
    IPowerManager localIPowerManager = IPowerManager.Stub.asInterface(ServiceManager.getService("power"));
    try
    {
      localIPowerManager.setBacklightBrightness(255);
      label18: return;
    }
    catch (RemoteException localRemoteException)
    {
      break label18;
    }
  }

  private void setStatusBarInFullscreen(boolean paramBoolean)
  {
    this.mIsStatusBarVisibleInFullscreen = paramBoolean;
    try
    {
      IStatusBarService localIStatusBarService = this.mStatusBarService;
      if (paramBoolean);
      for (int i = 536870912; ; i = 0)
      {
        localIStatusBarService.disable(i, this.mBinder, "android");
        if (this.mStatusBar != null)
        {
          if (!paramBoolean)
            break;
          this.mStatusBar.showLw(false);
        }
        return;
      }
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
      {
        localRemoteException.printStackTrace();
        continue;
        this.mStatusBar.hideLw(true);
      }
    }
  }

  private void setTorch(boolean paramBoolean)
  {
    this.mTorchEnabled = paramBoolean;
    Intent localIntent = new Intent("miui.intent.action.TOGGLE_TORCH");
    localIntent.putExtra("miui.intent.extra.IS_ENABLE", paramBoolean);
    this.mContext.sendBroadcast(localIntent);
  }

  public int finishAnimationLw()
  {
    WindowManagerPolicy.WindowState localWindowState = this.mStatusBar;
    if (this.mIsStatusBarVisibleInFullscreen)
      this.mStatusBar = null;
    int i = super.finishAnimationLw();
    this.mStatusBar = localWindowState;
    if ((ExtraStatusBarManager.isExpandableUnderFullscreen(this.mContext)) && (this.mStatusBar != null) && (!this.mIsStatusBarVisibleInFullscreen) && ((!this.mTopIsFullscreen) || (this.mKeyguardMediator.isShowing()) || (this.mStatusBarInputChannel == null)));
    try
    {
      this.mStatusBarInputChannel = this.mWindowManager.monitorInput("StatusBarView");
      InputQueue.registerInputChannel(this.mStatusBarInputChannel, this.mStatusBarInputHandler, this.mHandler.getLooper().getQueue());
      while (true)
      {
        label111: return i;
        if (this.mStatusBarInputChannel == null)
          continue;
        InputQueue.unregisterInputChannel(this.mStatusBarInputChannel);
        this.mStatusBarInputChannel.dispose();
        this.mStatusBarInputChannel = null;
      }
    }
    catch (RemoteException localRemoteException)
    {
      break label111;
    }
  }

  public void init(Context paramContext, IWindowManager paramIWindowManager, WindowManagerPolicy.WindowManagerFuncs paramWindowManagerFuncs, LocalPowerManager paramLocalPowerManager)
  {
    super.init(paramContext, paramIWindowManager, paramWindowManagerFuncs, paramLocalPowerManager);
    new MiuiSettingsObserver(this.mHandler).observe();
    this.mPowerLongPress = new Runnable()
    {
      public void run()
      {
        if ((!MiuiPhoneWindowManager.this.mKeyguardMediator.isShowing()) || (Settings.System.getInt(MiuiPhoneWindowManager.this.mContext.getContentResolver(), "keyguard_disable_power_key_long_press", 0) == 0))
          MiuiPhoneWindowManager.this.mPowerLongPressOriginal.run();
        while (true)
        {
          return;
          MiuiPhoneWindowManager.this.mPowerKeyHandled = true;
        }
      }
    };
    IntentFilter localIntentFilter1 = new IntentFilter();
    localIntentFilter1.addAction("android.intent.action.CAPTURE_SCREENSHOT");
    paramContext.registerReceiver(this.mScreenshotReceiver, localIntentFilter1);
    IntentFilter localIntentFilter2 = new IntentFilter();
    localIntentFilter2.addAction("com.miui.app.ExtraStatusBarManager.EXIT_FULLSCREEN");
    paramContext.registerReceiver(this.mStatusBarExitFullscreenReceiver, localIntentFilter2);
    this.mHasCameraFlash = paramContext.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    this.mHapticFeedbackUtil = new HapticFeedbackUtil(paramContext, false);
  }

  public long interceptKeyBeforeDispatching(WindowManagerPolicy.WindowState paramWindowState, KeyEvent paramKeyEvent, int paramInt)
  {
    int i = paramKeyEvent.getKeyCode();
    int j = paramKeyEvent.getRepeatCount();
    int k;
    if (paramKeyEvent.getAction() == 0)
    {
      k = 1;
      if (this.mScreenButtonsDisabled);
      switch (i)
      {
      default:
        if (i != 3)
          break label192;
        if ((k == 0) || (!isEnableKeyguardTorch()))
          break label168;
        if (j != 0)
          break;
        this.mTorchEnabled = false;
      case 3:
      case 4:
      case 82:
      case 84:
      }
    }
    long l;
    while (true)
    {
      l = -1L;
      while (true)
      {
        return l;
        k = 0;
        break;
        l = -1L;
      }
      if (this.mTorchEnabled)
      {
        if (j % 10 != 6)
          continue;
        this.mKeyguardMediator.pokeWakelock();
        continue;
      }
      if ((0x80 & paramKeyEvent.getFlags()) == 0)
        continue;
      setTorch(true);
    }
    label168: if (this.mTorchEnabled)
      setTorch(false);
    label180: label192: 
    do
    {
      l = super.interceptKeyBeforeDispatching(paramWindowState, paramKeyEvent, paramInt);
      break;
    }
    while (i != 4);
    WindowManager.LayoutParams localLayoutParams;
    if (k != 0)
      if (j == 0)
      {
        this.mBackLongPressed = false;
        if (paramWindowState == null)
          break label343;
        localLayoutParams = paramWindowState.getAttrs();
        if (((localLayoutParams == null) || (sBackLongPressWhiteList.contains(localLayoutParams.packageName)) || (localLayoutParams.type < 1) || (localLayoutParams.type > 99)) && ((localLayoutParams.type < 1000) || (localLayoutParams.type > 1999)));
      }
    while (true)
    {
      try
      {
        int m = Settings.System.getInt(this.mContext.getContentResolver(), "back_key_long_press_timeout");
        this.mBackLongPress.setTarget(paramWindowState);
        if (m <= 0)
          continue;
        this.mHandler.postDelayed(this.mBackLongPress, m);
        if (!this.mBackLongPressed)
          break label180;
        l = -1L;
        break;
        label343: localLayoutParams = null;
      }
      catch (Settings.SettingNotFoundException localSettingNotFoundException)
      {
        int n = ViewConfiguration.getLongPressTimeout();
        Settings.System.putInt(this.mContext.getContentResolver(), "back_key_long_press_timeout", 0);
        continue;
      }
      this.mHandler.removeCallbacks(this.mBackLongPress);
    }
  }

  public int interceptKeyBeforeQueueing(KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
  {
    int i = 0;
    int j = paramKeyEvent.getKeyCode();
    boolean bool1;
    int k;
    label30: label88: boolean bool2;
    if (paramKeyEvent.getAction() == 0)
    {
      bool1 = true;
      if ((0x1000000 & paramInt) == 0)
        break label146;
      k = 1;
      if (this.mScreenButtonsDisabled);
      switch (j)
      {
      default:
        if ((paramBoolean) || (k != 0))
        {
          if (!paramBoolean)
            break;
          bool2 = this.mKeyguardMediator.isShowingAndNotHidden();
          label110: if ((this.mCameraKeyWakeScreen) && (bool2) && (j == 27) && (bool1))
            i = 4;
        }
      case 4:
      case 82:
      case 84:
      case 3:
      case 26:
      }
    }
    while (true)
    {
      return i;
      bool1 = false;
      break;
      label146: k = 0;
      break label30;
      this.mHomePressed = bool1;
      continue;
      if (!this.mHomePressed)
        break label88;
      if (bool1)
        continue;
      this.mHomePressed = false;
      interceptPowerKeyUp(false);
      this.mContext.sendBroadcast(new Intent("com.miui.app.ExtraStatusBarManager.TRIGGER_TOGGLE_SCREEN_BUTTONS"));
      continue;
      bool2 = this.mKeyguardMediator.isShowing();
      break label110;
      if (j == 82)
        this.mMenuPressed = bool1;
      while (true)
      {
        if ((this.mMenuPressed) && (this.mVolumeUpPressed))
          setMaxBacklightBrightness();
        if ((this.mMenuPressed) && (this.mVolumeDownPressed) && (!this.mScreenshotTrigger))
        {
          this.mScreenshotTrigger = true;
          this.mHandler.removeCallbacks(this.mScreenshotChordLongPress);
          this.mHandler.postDelayed(this.mScreenshotChordLongPress, 0L);
        }
        if (!this.mScreenshotTrigger)
          break label358;
        if ((this.mMenuPressed) && (this.mVolumeDownPressed))
          break;
        this.mScreenshotTrigger = false;
        break;
        if (j == 24)
        {
          this.mVolumeUpPressed = bool1;
          continue;
        }
        if (j != 25)
          continue;
        this.mVolumeDownPressed = bool1;
      }
      label358: i = super.interceptKeyBeforeQueueing(paramKeyEvent, paramInt, paramBoolean);
    }
  }

  public boolean performHapticFeedbackLw(WindowManagerPolicy.WindowState paramWindowState, int paramInt, boolean paramBoolean)
  {
    if (this.mHapticFeedbackUtil.isSupportedEffect(paramInt));
    for (boolean bool = this.mHapticFeedbackUtil.performHapticFeedback(paramInt, paramBoolean); ; bool = super.performHapticFeedbackLw(paramWindowState, paramInt, paramBoolean))
      return bool;
  }

  public void removeWindowLw(WindowManagerPolicy.WindowState paramWindowState)
  {
    if (this.mStatusBar == paramWindowState)
      this.mContext.sendBroadcast(new Intent("com.miui.app.ExtraStatusBarManager.UNLOADED"));
    super.removeWindowLw(paramWindowState);
  }

  public void screenTurnedOff(int paramInt)
  {
    this.mVolumeUpPressed = false;
    this.mVolumeDownPressed = false;
    this.mMenuPressed = false;
    super.screenTurnedOff(paramInt);
  }

  public void screenTurningOn(WindowManagerPolicy.ScreenOnListener paramScreenOnListener)
  {
    super.screenTurningOn(paramScreenOnListener);
    if (paramScreenOnListener == null)
      this.mKeyguardMediator.onScreenTurnedOn(new KeyguardViewManager.ShowListener()
      {
        public void onShown(IBinder paramIBinder)
        {
        }
      });
  }

  class MiuiSettingsObserver extends ContentObserver
  {
    MiuiSettingsObserver(Handler arg2)
    {
      super();
    }

    void observe()
    {
      ContentResolver localContentResolver = MiuiPhoneWindowManager.this.mContext.getContentResolver();
      localContentResolver.registerContentObserver(Settings.System.getUriFor("trackball_wake_screen"), false, this);
      localContentResolver.registerContentObserver(Settings.System.getUriFor("camera_key_preferred_action_type"), false, this);
      localContentResolver.registerContentObserver(Settings.System.getUriFor("camera_key_preferred_action_shortcut_id"), false, this);
      localContentResolver.registerContentObserver(Settings.System.getUriFor("volumekey_wake_screen"), false, this);
      localContentResolver.registerContentObserver(Settings.Secure.getUriFor("screen_buttons_state"), false, this);
      onChange(false);
    }

    public void onChange(boolean paramBoolean)
    {
      int i = 1;
      while (true)
      {
        int j;
        synchronized (MiuiPhoneWindowManager.this.mLock)
        {
          ContentResolver localContentResolver = MiuiPhoneWindowManager.this.mContext.getContentResolver();
          MiuiPhoneWindowManager localMiuiPhoneWindowManager1 = MiuiPhoneWindowManager.this;
          if (Settings.Secure.getInt(localContentResolver, "screen_buttons_state", 0) == 0)
            continue;
          j = i;
          localMiuiPhoneWindowManager1.mScreenButtonsDisabled = j;
          if (Settings.System.getInt(localContentResolver, "trackball_wake_screen", 0) != i)
            break label169;
          int k = i;
          if (Settings.System.getInt(localContentResolver, "volumekey_wake_screen", 0) != i)
            break label175;
          int n = i;
          if (i != Settings.System.getInt(localContentResolver, "camera_key_preferred_action_type", 0))
            continue;
          MiuiPhoneWindowManager localMiuiPhoneWindowManager2 = MiuiPhoneWindowManager.this;
          if (4 != Settings.System.getInt(localContentResolver, "camera_key_preferred_action_shortcut_id", -1))
            break label181;
          localMiuiPhoneWindowManager2.mCameraKeyWakeScreen = i;
          ((MiuiKeyguardViewMediator)MiuiPhoneWindowManager.this.mKeyguardMediator).setWakeKeys(k, MiuiPhoneWindowManager.this.mCameraKeyWakeScreen, n);
          return;
          MiuiPhoneWindowManager.this.mCameraKeyWakeScreen = false;
        }
        continue;
        label169: int m = 0;
        continue;
        label175: int i1 = 0;
        continue;
        label181: i = 0;
      }
    }
  }

  private class BackLongPressRunnable
    implements Runnable
  {
    private WindowManagerPolicy.WindowState mWin = null;

    private BackLongPressRunnable()
    {
    }

    private void showHint()
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(MiuiPhoneWindowManager.this.mContext);
      localBuilder.setMessage(51118447);
      localBuilder.setNegativeButton(17039369, null);
      localBuilder.setPositiveButton(17039379, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          Settings.System.putInt(MiuiPhoneWindowManager.this.mContext.getContentResolver(), "back_key_long_press_timeout", ExtraSettings.System.BACK_KEY_LONG_PRESS_TIMEOUT_DEFAULT);
        }
      });
      AlertDialog localAlertDialog = localBuilder.create();
      localAlertDialog.getWindow().setType(2008);
      localAlertDialog.getWindow().setFlags(131072, 131072);
      localAlertDialog.show();
    }

    public void run()
    {
      MiuiPhoneWindowManager.this.mBackLongPressed = true;
      MiuiPhoneWindowManager.this.performHapticFeedbackLw(null, 0, false);
      if (this.mWin == null)
        showHint();
      while (true)
      {
        return;
        String str1 = this.mWin.getAttrs().packageName;
        Object localObject = null;
        String str2 = (String)this.mWin.getAttrs().getTitle();
        int i = str2.lastIndexOf('/');
        ComponentName localComponentName;
        PackageManager localPackageManager;
        if (i >= 0)
        {
          localComponentName = new ComponentName(str1, (String)str2.subSequence(i + 1, str2.length()));
          localPackageManager = MiuiPhoneWindowManager.this.mContext.getPackageManager();
        }
        try
        {
          localObject = localPackageManager.getActivityInfo(localComponentName, 0).loadLabel(localPackageManager).toString();
          if (TextUtils.isEmpty((CharSequence)localObject))
          {
            String str3 = localPackageManager.getApplicationInfo(str1, 0).loadLabel(localPackageManager).toString();
            localObject = str3;
          }
          try
          {
            label160: ActivityManagerNative.getDefault().finishActivity(this.mWin.getAttrs().token, 0, null);
            ((ActivityManager)MiuiPhoneWindowManager.this.mContext.getSystemService("activity")).forceStopPackage(str1);
            Context localContext1 = MiuiPhoneWindowManager.this.mContext;
            Context localContext2 = MiuiPhoneWindowManager.this.mContext;
            Object[] arrayOfObject = new Object[1];
            if (!TextUtils.isEmpty((CharSequence)localObject))
            {
              arrayOfObject[0] = localObject;
              Toast.makeText(localContext1, localContext2.getString(51118448, arrayOfObject), 0).show();
            }
          }
          catch (RemoteException localRemoteException)
          {
            while (true)
            {
              localRemoteException.printStackTrace();
              continue;
              localObject = str1;
            }
          }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          break label160;
        }
      }
    }

    public void setTarget(WindowManagerPolicy.WindowState paramWindowState)
    {
      this.mWin = paramWindowState;
    }
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.android.internal.policy.impl.MiuiPhoneWindowManager
 * JD-Core Version:    0.6.0
 */