package miui.app.screenelement;

import org.w3c.dom.Element;

public class SourcesAnimation extends PositionAnimation
{
  public static final String TAG_NAME = "SourcesAnimation";
  private String mCurrentBitmap;

  public SourcesAnimation(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, "Source", paramScreenContext);
  }

  public final String getSrc()
  {
    return this.mCurrentBitmap;
  }

  protected BaseAnimation.AnimationItem onCreateItem()
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "x";
    arrayOfString[1] = "y";
    return new Source(arrayOfString, this.mContext);
  }

  protected void onTick(BaseAnimation.AnimationItem paramAnimationItem1, BaseAnimation.AnimationItem paramAnimationItem2, float paramFloat)
  {
    this.mCurrentX = paramAnimationItem2.get(0);
    this.mCurrentY = paramAnimationItem2.get(1);
    this.mCurrentBitmap = ((Source)paramAnimationItem2).mSrc;
  }

  public static class Source extends BaseAnimation.AnimationItem
  {
    public static final String TAG_NAME = "Source";
    public String mSrc;

    public Source(String[] paramArrayOfString, ScreenContext paramScreenContext)
    {
      super(paramScreenContext);
    }

    public BaseAnimation.AnimationItem load(Element paramElement)
      throws ScreenElementLoadException
    {
      this.mSrc = paramElement.getAttribute("src");
      return super.load(paramElement);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.SourcesAnimation
 * JD-Core Version:    0.6.0
 */