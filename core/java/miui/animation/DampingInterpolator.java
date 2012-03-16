package miui.animation;

import android.animation.TimeInterpolator;

public class DampingInterpolator
  implements TimeInterpolator
{
  private final double mAtanValue;
  private final float mFactor;

  public DampingInterpolator(float paramFloat)
  {
    this.mFactor = paramFloat;
    this.mAtanValue = Math.atan(this.mFactor);
  }

  public float getInterpolation(float paramFloat)
  {
    return (float)(Math.atan(paramFloat * this.mFactor) / this.mAtanValue);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.animation.DampingInterpolator
 * JD-Core Version:    0.6.0
 */