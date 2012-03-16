package com.miui.internal.policy.impl.AwesomeLockScreenImp;

import miui.app.screenelement.ScreenContext;
import miui.app.screenelement.ScreenElement;
import miui.app.screenelement.ScreenElementFactory;
import miui.app.screenelement.ScreenElementLoadException;
import org.w3c.dom.Element;

public class LockScreenElementFactory extends ScreenElementFactory
{
  private final LockScreenRoot.UnlockerCallback mUnlockerCallback;
  private final UnlockerListener mUnlockerListener;

  public LockScreenElementFactory(LockScreenRoot.UnlockerCallback paramUnlockerCallback, UnlockerListener paramUnlockerListener)
  {
    this.mUnlockerCallback = paramUnlockerCallback;
    this.mUnlockerListener = paramUnlockerListener;
  }

  public ScreenElement createInstance(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    if (paramElement.getTagName().equalsIgnoreCase("Unlocker"));
    for (Object localObject = new UnlockerScreenElement(paramElement, paramScreenContext, this.mUnlockerCallback, this.mUnlockerListener); ; localObject = super.createInstance(paramElement, paramScreenContext))
      return localObject;
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenElementFactory
 * JD-Core Version:    0.6.0
 */