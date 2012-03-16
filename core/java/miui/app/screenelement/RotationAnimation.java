package miui.app.screenelement;

import org.w3c.dom.Element;

public class RotationAnimation extends BaseAnimation
{
  public static final String INNER_TAG_NAME = "Rotation";
  public static final String TAG_NAME = "RotationAnimation";
  private float mCurrentAngle;

  public RotationAnimation(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, "Rotation", paramScreenContext);
  }

  public final float getAngle()
  {
    return this.mCurrentAngle;
  }

  protected BaseAnimation.AnimationItem onCreateItem()
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "angle";
    return new BaseAnimation.AnimationItem(arrayOfString, this.mContext);
  }

  protected void onTick(BaseAnimation.AnimationItem paramAnimationItem1, BaseAnimation.AnimationItem paramAnimationItem2, float paramFloat)
  {
    if ((paramAnimationItem1 == null) && (paramAnimationItem2 == null))
    {
      this.mCurrentAngle = 0.0F;
      return;
    }
    double d;
    if (paramAnimationItem1 == null)
      d = 0.0D;
    while (true)
    {
      this.mCurrentAngle = (float)(d + (paramAnimationItem2.get(0) - d) * paramFloat);
      break;
      d = paramAnimationItem1.get(0);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.RotationAnimation
 * JD-Core Version:    0.6.0
 */