package com.miui.internal.policy.impl.AwesomeLockScreenImp;

import java.util.ArrayList;
import java.util.Iterator;
import miui.app.screenelement.ElementGroup;
import miui.app.screenelement.ScreenContext;
import miui.app.screenelement.ScreenElement;
import miui.app.screenelement.ScreenElementLoadException;
import org.w3c.dom.Element;

public class LockScreenElementGroup extends ElementGroup
  implements UnlockerListener
{
  public LockScreenElementGroup(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
  }

  public void endUnlockMoving(UnlockerScreenElement paramUnlockerScreenElement)
  {
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
    {
      ScreenElement localScreenElement = (ScreenElement)localIterator.next();
      if (!(localScreenElement instanceof UnlockerListener))
        continue;
      ((UnlockerListener)localScreenElement).endUnlockMoving(paramUnlockerScreenElement);
    }
  }

  public void startUnlockMoving(UnlockerScreenElement paramUnlockerScreenElement)
  {
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
    {
      ScreenElement localScreenElement = (ScreenElement)localIterator.next();
      if (!(localScreenElement instanceof UnlockerListener))
        continue;
      ((UnlockerListener)localScreenElement).startUnlockMoving(paramUnlockerScreenElement);
    }
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenElementGroup
 * JD-Core Version:    0.6.0
 */