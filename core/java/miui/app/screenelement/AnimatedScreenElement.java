package miui.app.screenelement;

import org.w3c.dom.Element;

public abstract class AnimatedScreenElement extends ScreenElement
{
  private IndexedNumberVariable mActualXVar;
  private IndexedNumberVariable mActualYVar;
  protected AnimatedElement mAni = new AnimatedElement(paramElement, this.mContext);

  public AnimatedScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    if (this.mHasName)
    {
      this.mActualXVar = new IndexedNumberVariable(this.mName, "actual_x", paramScreenContext.mVariables);
      this.mActualYVar = new IndexedNumberVariable(this.mName, "actual_y", paramScreenContext.mVariables);
    }
  }

  public int getAlpha()
  {
    return this.mAni.getAlpha();
  }

  public float getAngle()
  {
    return this.mAni.getRotationAngle();
  }

  public float getCenterX()
  {
    return this.mAni.getCenterX();
  }

  public float getCenterY()
  {
    return this.mAni.getCenterY();
  }

  public float getHeight()
  {
    return this.mAni.getHeight();
  }

  public float getMaxHeight()
  {
    return this.mAni.getMaxHeight();
  }

  public float getMaxWidth()
  {
    return this.mAni.getMaxWidth();
  }

  public float getWidth()
  {
    return this.mAni.getWidth();
  }

  public float getX()
  {
    return this.mAni.getX();
  }

  public float getY()
  {
    return this.mAni.getY();
  }

  public void init()
  {
    this.mAni.init();
  }

  public void tick(long paramLong)
  {
    if (!isVisible());
    while (true)
    {
      return;
      this.mAni.tick(paramLong);
      if (!this.mHasName)
        continue;
      this.mActualXVar.set(descale(this.mAni.getX()));
      this.mActualYVar.set(descale(this.mAni.getY()));
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.AnimatedScreenElement
 * JD-Core Version:    0.6.0
 */