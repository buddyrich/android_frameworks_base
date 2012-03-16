package miui.app.screenelement;

import org.w3c.dom.Element;

public class PositionAnimation extends BaseAnimation
{
  public static final String INNER_TAG_NAME = "Position";
  public static final String TAG_NAME = "PositionAnimation";
  protected double mCurrentX;
  protected double mCurrentY;

  public PositionAnimation(Element paramElement, String paramString, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramString, paramScreenContext);
  }

  public PositionAnimation(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, "Position", paramScreenContext);
    Utils.asserts(paramElement.getNodeName().equalsIgnoreCase("PositionAnimation"), "wrong tag name:" + paramElement.getNodeName());
  }

  public final double getX()
  {
    return this.mCurrentX;
  }

  public final double getY()
  {
    return this.mCurrentY;
  }

  protected BaseAnimation.AnimationItem onCreateItem()
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "x";
    arrayOfString[1] = "y";
    return new BaseAnimation.AnimationItem(arrayOfString, this.mContext);
  }

  protected void onTick(BaseAnimation.AnimationItem paramAnimationItem1, BaseAnimation.AnimationItem paramAnimationItem2, float paramFloat)
  {
    double d1 = 0.0D;
    if ((paramAnimationItem1 == null) && (paramAnimationItem2 == null))
    {
      this.mCurrentX = d1;
      this.mCurrentY = d1;
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
      this.mCurrentX = (d2 + (paramAnimationItem2.get(0) - d2) * paramFloat);
      this.mCurrentY = (d1 + (paramAnimationItem2.get(1) - d1) * paramFloat);
      break;
      d2 = paramAnimationItem1.get(0);
      break label32;
      label85: d1 = paramAnimationItem1.get(1);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.PositionAnimation
 * JD-Core Version:    0.6.0
 */