package com.miui.internal.policy.impl.AwesomeLockScreenImp;

import android.content.Intent;
import android.view.MotionEvent;
import miui.app.screenelement.ButtonScreenElement;
import miui.app.screenelement.ButtonScreenElement.ButtonAction;
import miui.app.screenelement.ElementGroup;
import miui.app.screenelement.IndexedNumberVariable;
import miui.app.screenelement.ScreenContext;
import miui.app.screenelement.ScreenElementLoadException;
import miui.app.screenelement.ScreenElementRoot;
import miui.app.screenelement.Task;
import org.w3c.dom.Element;

public class LockScreenRoot extends ScreenElementRoot
  implements UnlockerListener
{
  private static final String LOG_TAG = "LockScreenRoot";
  private static final String TAG_NAME_BATTERYFULL = "BatteryFull";
  private static final String TAG_NAME_CHARGING = "Charging";
  private static final String TAG_NAME_LOWBATTERY = "BatteryLow";
  private static final String TAG_NAME_NORMAL = "Normal";
  private String curCategory = "Normal";
  private IndexedNumberVariable mBatteryState;
  private boolean mDisplayDesktop;
  private SoundManager mSoundManager;
  private final UnlockerCallback mUnlockerCallback;

  public LockScreenRoot(ScreenContext paramScreenContext, UnlockerCallback paramUnlockerCallback)
  {
    super(paramScreenContext);
    this.mContext.mRoot = this;
    this.mUnlockerCallback = paramUnlockerCallback;
    this.mBatteryState = new IndexedNumberVariable("battery_state", this.mContext.mVariables);
    this.mSoundManager = new SoundManager(paramScreenContext.mContext, paramScreenContext.mResourceManager);
  }

  private void updateBatteryInfo()
  {
    String str = "Normal";
    int i = 0;
    if (this.mContext.mShowingBatteryInfo)
    {
      if (!this.mContext.mPluggedIn)
        break label59;
      if (this.mContext.mBatteryLevel >= 100)
      {
        str = "BatteryFull";
        i = 3;
      }
    }
    else
    {
      if (str != this.curCategory)
        break label67;
    }
    while (true)
    {
      return;
      str = "Charging";
      i = 1;
      break;
      label59: str = "BatteryLow";
      i = 2;
      break;
      label67: this.mBatteryState.set(i);
      this.mElementGroup.showCategory(this.curCategory, false);
      this.mElementGroup.showCategory(str, true);
      this.curCategory = str;
    }
  }

  protected ElementGroup createElementGroup(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    return new LockScreenElementGroup(paramElement, this.mContext);
  }

  public void endUnlockMoving(UnlockerScreenElement paramUnlockerScreenElement)
  {
    if (this.mElementGroup != null)
      ((LockScreenElementGroup)this.mElementGroup).endUnlockMoving(paramUnlockerScreenElement);
  }

  public void finish()
  {
    super.finish();
    this.mSoundManager.release();
  }

  public void init()
  {
    super.init();
    if (this.mElementGroup != null)
    {
      this.mElementGroup.showCategory("BatteryFull", false);
      this.mElementGroup.showCategory("Charging", false);
      this.mElementGroup.showCategory("BatteryLow", false);
    }
  }

  public boolean isDisplayDesktop()
  {
    return this.mDisplayDesktop;
  }

  public void onButtonInteractive(ButtonScreenElement paramButtonScreenElement, ButtonScreenElement.ButtonAction paramButtonAction)
  {
    this.mUnlockerCallback.pokeWakelock();
  }

  protected boolean onLoad(Element paramElement)
  {
    this.mDisplayDesktop = Boolean.parseBoolean(paramElement.getAttribute("displayDesktop"));
    BuiltinVariableBinders.fill(this.mVariableBinderManager);
    return true;
  }

  public void onTouch(MotionEvent paramMotionEvent)
  {
    if (this.mElementGroup == null)
      this.mUnlockerCallback.unlocked(null);
    while (true)
    {
      return;
      super.onTouch(paramMotionEvent);
    }
  }

  public void playSound(String paramString)
  {
    if (this.mUnlockerCallback.isSoundEnable())
      this.mSoundManager.playSound(paramString, true);
  }

  public void startUnlockMoving(UnlockerScreenElement paramUnlockerScreenElement)
  {
    if (this.mElementGroup != null)
      ((LockScreenElementGroup)this.mElementGroup).startUnlockMoving(paramUnlockerScreenElement);
  }

  public void tick(long paramLong)
  {
    super.tick(paramLong);
    if (this.mElementGroup != null)
      updateBatteryInfo();
  }

  public static abstract interface UnlockerCallback
  {
    public abstract Task findTask(String paramString);

    public abstract void haptic(int paramInt);

    public abstract boolean isSoundEnable();

    public abstract void pokeWakelock();

    public abstract void pokeWakelock(int paramInt);

    public abstract void unlocked(Intent paramIntent);
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenRoot
 * JD-Core Version:    0.6.0
 */