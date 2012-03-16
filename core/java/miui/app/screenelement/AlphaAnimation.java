package miui.app.screenelement;

import android.text.TextUtils;
import org.w3c.dom.Element;

public class AlphaAnimation extends BaseAnimation
{
  public static final String INNER_TAG_NAME = "Alpha";
  public static final String TAG_NAME = "AlphaAnimation";
  private int mCurrentAlpha;
  private int mDelayValue;

  public AlphaAnimation(Element paramElement, String paramString, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramString, paramScreenContext);
    String str = paramElement.getAttribute("delayValue");
    if (!TextUtils.isEmpty(str));
    try
    {
      this.mDelayValue = Integer.parseInt(str);
      label34: return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      break label34;
    }
  }

  public AlphaAnimation(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, "Alpha", paramScreenContext);
    Utils.asserts(paramElement.getNodeName().equalsIgnoreCase("AlphaAnimation"), "wrong tag name:" + paramElement.getNodeName());
  }

  public final int getAlpha()
  {
    return this.mCurrentAlpha;
  }

  protected BaseAnimation.AnimationItem onCreateItem()
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "a";
    return new BaseAnimation.AnimationItem(arrayOfString, this.mContext);
  }

  protected void onTick(BaseAnimation.AnimationItem paramAnimationItem1, BaseAnimation.AnimationItem paramAnimationItem2, float paramFloat)
  {
    if ((paramAnimationItem1 == null) && (paramAnimationItem2 == null))
    {
      this.mCurrentAlpha = this.mDelayValue;
      return;
    }
    double d;
    if (paramAnimationItem1 == null)
      d = 255.0D;
    while (true)
    {
      this.mCurrentAlpha = (int)Math.round(d + (paramAnimationItem2.get(0) - d) * paramFloat);
      break;
      d = paramAnimationItem1.get(0);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.AlphaAnimation
 * JD-Core Version:    0.6.0
 */