package miui.app.screenelement;

import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;

public class SizeAnimation extends BaseAnimation
{
  public static final String INNER_TAG_NAME = "Size";
  public static final String TAG_NAME = "SizeAnimation";
  private double mCurrentH;
  private double mCurrentW;
  private double mMaxH;
  private double mMaxW;

  public SizeAnimation(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, "Size", paramScreenContext);
    Utils.asserts(paramElement.getNodeName().equalsIgnoreCase("SizeAnimation"), "wrong tag name:" + paramElement.getNodeName());
    Iterator localIterator = this.mItems.iterator();
    while (localIterator.hasNext())
    {
      BaseAnimation.AnimationItem localAnimationItem = (BaseAnimation.AnimationItem)localIterator.next();
      if (localAnimationItem.get(0) > this.mMaxW)
        this.mMaxW = localAnimationItem.get(0);
      if (localAnimationItem.get(1) <= this.mMaxH)
        continue;
      this.mMaxH = localAnimationItem.get(0);
    }
  }

  public final double getHeight()
  {
    return this.mCurrentH;
  }

  public final double getMaxHeight()
  {
    return this.mMaxH;
  }

  public final double getMaxWidth()
  {
    return this.mMaxW;
  }

  public final double getWidth()
  {
    return this.mCurrentW;
  }

  protected BaseAnimation.AnimationItem onCreateItem()
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "w";
    arrayOfString[1] = "h";
    return new BaseAnimation.AnimationItem(arrayOfString, this.mContext);
  }

  protected void onTick(BaseAnimation.AnimationItem paramAnimationItem1, BaseAnimation.AnimationItem paramAnimationItem2, float paramFloat)
  {
    double d1 = 0.0D;
    if ((paramAnimationItem1 == null) && (paramAnimationItem2 == null))
    {
      this.mCurrentW = d1;
      this.mCurrentH = d1;
      return;
    }
    double d2;
    if (paramAnimationItem1 == null)
    {
      d2 = d1;
      label32: if (paramAnimationItem1 != null)
        break label85;
    }
    while (true)
    {
      this.mCurrentW = (d2 + (paramAnimationItem2.get(0) - d2) * paramFloat);
      this.mCurrentH = (d1 + (paramAnimationItem2.get(1) - d1) * paramFloat);
      break;
      d2 = paramAnimationItem1.get(0);
      break label32;
      label85: d1 = paramAnimationItem1.get(1);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.SizeAnimation
 * JD-Core Version:    0.6.0
 */