package miui.app.screenelement;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;

public class AnimatedElement
{
  private static final boolean DEBUG = false;
  private static final String LOG_TAG = "AnimatedElement";
  private boolean mAlignAbsolute;
  private Expression mAlphaExpression;
  private AlphaAnimation mAlphas;
  protected Expression mAngleExpression;
  private ArrayList<BaseAnimation> mAnimations = new ArrayList();
  protected Expression mBaseXExpression;
  protected Expression mBaseYExpression;
  protected Expression mCenterXExpression;
  protected Expression mCenterYExpression;
  private ScreenContext mContext;
  protected Expression mHeightExpression;
  private PositionAnimation mPositions;
  private RotationAnimation mRotations;
  private SizeAnimation mSizes;
  private SourcesAnimation mSources;
  private TextFormatter mSrcFormatter;
  protected Expression mSrcIdExpression;
  protected Expression mWidthExpression;

  public AnimatedElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    this.mContext = paramScreenContext;
    load(paramElement);
  }

  private Expression createExp(Element paramElement, String paramString1, String paramString2)
  {
    Expression localExpression = Expression.build(paramElement.getAttribute(paramString1));
    if ((localExpression == null) && (!TextUtils.isEmpty(paramString2)))
      localExpression = Expression.build(paramElement.getAttribute(paramString2));
    return localExpression;
  }

  private void loadAlphaAnimations(Element paramElement)
    throws ScreenElementLoadException
  {
    Element localElement = Utils.getChild(paramElement, "AlphaAnimation");
    if (localElement == null);
    while (true)
    {
      return;
      this.mAlphas = new AlphaAnimation(localElement, this.mContext);
      this.mAnimations.add(this.mAlphas);
    }
  }

  private void loadPositionAnimations(Element paramElement)
    throws ScreenElementLoadException
  {
    Element localElement = Utils.getChild(paramElement, "PositionAnimation");
    if (localElement == null);
    while (true)
    {
      return;
      this.mPositions = new PositionAnimation(localElement, this.mContext);
      this.mAnimations.add(this.mPositions);
    }
  }

  private void loadRotationAnimations(Element paramElement)
    throws ScreenElementLoadException
  {
    Element localElement = Utils.getChild(paramElement, "RotationAnimation");
    if (localElement == null);
    while (true)
    {
      return;
      this.mRotations = new RotationAnimation(localElement, this.mContext);
      this.mAnimations.add(this.mRotations);
    }
  }

  private void loadSizeAnimations(Element paramElement)
    throws ScreenElementLoadException
  {
    Element localElement = Utils.getChild(paramElement, "SizeAnimation");
    if (localElement == null);
    while (true)
    {
      return;
      this.mSizes = new SizeAnimation(localElement, this.mContext);
      this.mAnimations.add(this.mSizes);
    }
  }

  private void loadSourceAnimations(Element paramElement)
    throws ScreenElementLoadException
  {
    Element localElement = Utils.getChild(paramElement, "SourcesAnimation");
    if (localElement == null);
    while (true)
    {
      return;
      this.mSources = new SourcesAnimation(localElement, this.mContext);
      this.mAnimations.add(this.mSources);
    }
  }

  private float scale(double paramDouble)
  {
    return (float)Math.round(paramDouble * this.mContext.mRoot.getScale());
  }

  public int getAlpha()
  {
    int i;
    if (this.mAlphaExpression != null)
    {
      i = (int)this.mAlphaExpression.evaluate(this.mContext.mVariables);
      if (this.mAlphas == null)
        break label62;
    }
    label62: for (int j = this.mAlphas.getAlpha(); ; j = 255)
    {
      return (int)(i * 255 / 255.0F * j / 255.0F);
      i = 255;
      break;
    }
  }

  public String getBmp()
  {
    String str = this.mSrcFormatter.getText(this.mContext.mVariables);
    if (this.mSources != null)
      str = this.mSources.getSrc();
    if ((str != null) && (this.mSrcIdExpression != null))
      str = Utils.addFileNameSuffix(str, String.valueOf(()this.mSrcIdExpression.evaluate(this.mContext.mVariables)));
    return str;
  }

  public float getCenterX()
  {
    double d;
    if (this.mCenterXExpression != null)
      d = this.mCenterXExpression.evaluate(this.mContext.mVariables);
    while (true)
    {
      return scale(d);
      d = 0.0D;
    }
  }

  public float getCenterY()
  {
    double d;
    if (this.mCenterYExpression != null)
      d = this.mCenterYExpression.evaluate(this.mContext.mVariables);
    while (true)
    {
      return scale(d);
      d = 0.0D;
    }
  }

  public float getHeight()
  {
    float f;
    if (this.mSizes != null)
      f = scale(this.mSizes.getHeight());
    while (true)
    {
      return f;
      if (this.mHeightExpression != null)
      {
        f = scale(this.mHeightExpression.evaluate(this.mContext.mVariables));
        continue;
      }
      f = -1.0F;
    }
  }

  public float getMaxHeight()
  {
    float f;
    if (this.mSizes != null)
      f = scale(this.mSizes.getMaxHeight());
    while (true)
    {
      return f;
      if (this.mHeightExpression != null)
      {
        f = scale(this.mHeightExpression.evaluate(this.mContext.mVariables));
        continue;
      }
      f = -1.0F;
    }
  }

  public float getMaxWidth()
  {
    float f;
    if (this.mSizes != null)
      f = scale(this.mSizes.getMaxWidth());
    while (true)
    {
      return f;
      if (this.mWidthExpression != null)
      {
        f = scale(this.mWidthExpression.evaluate(this.mContext.mVariables));
        continue;
      }
      f = -1.0F;
    }
  }

  public float getRotationAngle()
  {
    double d;
    float f;
    if (this.mAngleExpression != null)
    {
      d = this.mAngleExpression.evaluate(this.mContext.mVariables);
      if (this.mRotations == null)
        break label48;
      f = this.mRotations.getAngle();
    }
    while (true)
    {
      return (float)(d + f);
      d = 0.0D;
      break;
      label48: f = 0.0F;
    }
  }

  public float getWidth()
  {
    float f;
    if (this.mSizes != null)
      f = scale(this.mSizes.getWidth());
    while (true)
    {
      return f;
      if (this.mWidthExpression != null)
      {
        f = scale(this.mWidthExpression.evaluate(this.mContext.mVariables));
        continue;
      }
      f = -1.0F;
    }
  }

  public float getX()
  {
    double d;
    if (this.mBaseXExpression != null)
      d = this.mBaseXExpression.evaluate(this.mContext.mVariables);
    while (true)
    {
      if (this.mSources != null)
        d += this.mSources.getX();
      if (this.mPositions != null)
        d += this.mPositions.getX();
      return scale(d);
      d = 0.0D;
    }
  }

  public float getY()
  {
    double d;
    if (this.mBaseYExpression != null)
      d = this.mBaseYExpression.evaluate(this.mContext.mVariables);
    while (true)
    {
      if (this.mSources != null)
        d += this.mSources.getY();
      if (this.mPositions != null)
        d += this.mPositions.getY();
      return scale(d);
      d = 0.0D;
    }
  }

  public void init()
  {
    Iterator localIterator = this.mAnimations.iterator();
    while (localIterator.hasNext())
      ((BaseAnimation)localIterator.next()).init();
  }

  public boolean isAlignAbsolute()
  {
    return this.mAlignAbsolute;
  }

  public void load(Element paramElement)
    throws ScreenElementLoadException
  {
    if (paramElement == null)
    {
      Log.e("AnimatedElement", "node is null");
      throw new ScreenElementLoadException("node is null");
    }
    this.mBaseXExpression = createExp(paramElement, "x", "left");
    this.mBaseYExpression = createExp(paramElement, "y", "top");
    this.mWidthExpression = createExp(paramElement, "w", "width");
    this.mHeightExpression = createExp(paramElement, "h", "height");
    this.mAngleExpression = createExp(paramElement, "angle", null);
    this.mCenterXExpression = createExp(paramElement, "centerX", null);
    this.mCenterYExpression = createExp(paramElement, "centerY", null);
    this.mSrcIdExpression = createExp(paramElement, "srcid", null);
    this.mAlphaExpression = createExp(paramElement, "alpha", null);
    this.mSrcFormatter = new TextFormatter(paramElement.getAttribute("src"), paramElement.getAttribute("srcFormat"), paramElement.getAttribute("srcParas"));
    if (paramElement.getAttribute("align").equalsIgnoreCase("absolute"))
      this.mAlignAbsolute = true;
    loadSourceAnimations(paramElement);
    loadPositionAnimations(paramElement);
    loadRotationAnimations(paramElement);
    loadSizeAnimations(paramElement);
    loadAlphaAnimations(paramElement);
  }

  public void tick(long paramLong)
  {
    Iterator localIterator = this.mAnimations.iterator();
    while (localIterator.hasNext())
      ((BaseAnimation)localIterator.next()).tick(paramLong);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.AnimatedElement
 * JD-Core Version:    0.6.0
 */