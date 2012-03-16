package miui.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ViewPropertyAnimator extends Animator
{
  public static final int ALPHA = 512;
  public static final float CURRENT_HALF_HEIGHT = 0.01F;
  public static final float CURRENT_HALF_WIDTH = 0.007F;
  public static final float CURRENT_HEIGHT = 0.008F;
  public static final float CURRENT_HEIGHT_NEGATIVE = 0.009F;
  public static final float CURRENT_VALUE = 3.4028235E+38F;
  public static final float CURRENT_WIDTH = 0.005F;
  public static final float CURRENT_WIDTH_NEGATIVE = 0.006F;
  private static final int DISPLAY_METRICES_HEIGHT = 0;
  private static final int DISPLAY_METRICES_WIDTH = 0;
  public static final float NO_FINAL_VALUE = 1.4E-45F;
  public static final int NO_FINAL_VISIBILITY = -1;
  public static final float OUT_BOTTOM_SCREEN = 0.002F;
  public static final float OUT_LEFT_SCREEN = 0.003F;
  public static final float OUT_RIGHT_SCREEN = 0.004F;
  public static final float OUT_TOP_SCREEN = 0.001F;
  public static final int ROTATION = 16;
  public static final int ROTATION_X = 32;
  public static final int ROTATION_Y = 64;
  public static final int SCALE_X = 4;
  public static final int SCALE_Y = 8;
  public static final int TRANSLATION_X = 1;
  public static final int TRANSLATION_Y = 2;
  public static final int X = 128;
  public static final int Y = 256;
  private static final HashMap<Object, Animator> mAnimatorMap;
  private ValueAnimator mAnimator;
  private float mFinalValue;
  private int mFinalVisibility;
  private float mFromValue;
  private int mProperty;
  private float mToValue;
  private View mView;

  static
  {
    DISPLAY_METRICES_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    mAnimatorMap = new HashMap();
  }

  public ViewPropertyAnimator(View paramView, int paramInt, float paramFloat1, float paramFloat2)
  {
    float[] arrayOfFloat = new float[1];
    arrayOfFloat[0] = 1.0F;
    this.mAnimator = ValueAnimator.ofFloat(arrayOfFloat);
    this.mFinalVisibility = -1;
    this.mFinalValue = 1.4E-45F;
    this.mView = paramView;
    this.mProperty = paramInt;
    this.mFromValue = paramFloat1;
    this.mToValue = paramFloat2;
    this.mAnimator.addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnimator)
      {
        ViewPropertyAnimator.access$100(ViewPropertyAnimator.this.mView, null);
        ViewPropertyAnimator.this.setFinalValues();
        ArrayList localArrayList = ViewPropertyAnimator.this.getListeners();
        if (localArrayList != null)
        {
          Iterator localIterator = ((ArrayList)localArrayList.clone()).iterator();
          while (localIterator.hasNext())
            ((Animator.AnimatorListener)localIterator.next()).onAnimationCancel(ViewPropertyAnimator.this);
        }
      }

      public void onAnimationEnd(Animator paramAnimator)
      {
        ViewPropertyAnimator.access$100(ViewPropertyAnimator.this.mView, null);
        ViewPropertyAnimator.this.setFinalValues();
        ArrayList localArrayList = ViewPropertyAnimator.this.getListeners();
        if (localArrayList != null)
        {
          Iterator localIterator = ((ArrayList)localArrayList.clone()).iterator();
          while (localIterator.hasNext())
            ((Animator.AnimatorListener)localIterator.next()).onAnimationEnd(ViewPropertyAnimator.this);
        }
      }

      public void onAnimationRepeat(Animator paramAnimator)
      {
        ArrayList localArrayList = ViewPropertyAnimator.this.getListeners();
        if (localArrayList != null)
        {
          Iterator localIterator = ((ArrayList)localArrayList.clone()).iterator();
          while (localIterator.hasNext())
            ((Animator.AnimatorListener)localIterator.next()).onAnimationRepeat(ViewPropertyAnimator.this);
        }
      }

      public void onAnimationStart(Animator paramAnimator)
      {
        ArrayList localArrayList = ViewPropertyAnimator.this.getListeners();
        if (localArrayList != null)
        {
          Iterator localIterator = ((ArrayList)localArrayList.clone()).iterator();
          while (localIterator.hasNext())
            ((Animator.AnimatorListener)localIterator.next()).onAnimationStart(ViewPropertyAnimator.this);
        }
      }
    });
    this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramValueAnimator)
      {
        ViewPropertyAnimator.this.setValue(ViewPropertyAnimator.this.mFromValue + paramValueAnimator.getAnimatedFraction() * (ViewPropertyAnimator.this.mToValue - ViewPropertyAnimator.this.mFromValue));
      }
    });
  }

  private float calulateAnimatorValue(float paramFloat)
  {
    if (paramFloat == 0.003F)
      if ((this.mProperty == 128) || (this.mProperty == 1))
      {
        int[] arrayOfInt4 = new int[2];
        this.mView.getLocationOnScreen(arrayOfInt4);
        paramFloat = DISPLAY_METRICES_WIDTH - arrayOfInt4[0];
        if (this.mProperty == 128)
          paramFloat += this.mView.getLeft();
      }
    while (true)
    {
      return paramFloat;
      if (paramFloat == 0.004F)
      {
        if ((this.mProperty != 128) && (this.mProperty != 1))
          continue;
        int[] arrayOfInt3 = new int[2];
        this.mView.getLocationOnScreen(arrayOfInt3);
        paramFloat = -(getWidthOrMeasureWidth(this.mView) + arrayOfInt3[0]);
        if (this.mProperty != 128)
          continue;
        paramFloat += this.mView.getLeft();
        continue;
      }
      if (paramFloat == 0.001F)
      {
        if ((this.mProperty != 256) && (this.mProperty != 2))
          continue;
        int[] arrayOfInt2 = new int[2];
        this.mView.getLocationOnScreen(arrayOfInt2);
        paramFloat = -(getHeightOrMeasureHeight(this.mView) + arrayOfInt2[1]);
        if (this.mProperty != 256)
          continue;
        paramFloat += this.mView.getTop();
        continue;
      }
      if (paramFloat == 0.002F)
      {
        if ((this.mProperty != 256) && (this.mProperty != 2))
          continue;
        int[] arrayOfInt1 = new int[2];
        this.mView.getLocationOnScreen(arrayOfInt1);
        paramFloat = DISPLAY_METRICES_HEIGHT - arrayOfInt1[1];
        if (this.mProperty != 256)
          continue;
        paramFloat += this.mView.getTop();
        continue;
      }
      if (paramFloat == 0.008F)
      {
        paramFloat = getHeightOrMeasureHeight(this.mView);
        continue;
      }
      if (paramFloat == 0.005F)
      {
        paramFloat = getWidthOrMeasureWidth(this.mView);
        continue;
      }
      if (paramFloat == 0.009F)
      {
        paramFloat = -getHeightOrMeasureHeight(this.mView);
        continue;
      }
      if (paramFloat == 0.006F)
      {
        paramFloat = -getWidthOrMeasureWidth(this.mView);
        continue;
      }
      if (paramFloat == 0.01F)
      {
        paramFloat = getHeightOrMeasureHeight(this.mView) / 2.0F;
        continue;
      }
      if (paramFloat == 0.007F)
      {
        paramFloat = getWidthOrMeasureWidth(this.mView) / 2.0F;
        continue;
      }
      if (paramFloat != 3.4028235E+38F)
        continue;
      paramFloat = getValue();
    }
  }

  public static void cancelAnimator(View paramView)
  {
    Animator localAnimator = (Animator)mAnimatorMap.get(paramView);
    if (localAnimator != null)
      localAnimator.cancel();
  }

  public static float getHeightOrMeasureHeight(View paramView)
  {
    int i = paramView.getHeight();
    if (i == 0)
    {
      paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
      i = paramView.getMeasuredHeight();
    }
    return i;
  }

  private float getValue()
  {
    float f = 1.4E-45F;
    switch (this.mProperty)
    {
    default:
    case 1:
    case 2:
    case 16:
    case 32:
    case 64:
    case 4:
    case 8:
    case 128:
    case 256:
    case 512:
    }
    while (true)
    {
      return f;
      f = this.mView.getTranslationX();
      continue;
      f = this.mView.getTranslationY();
      continue;
      f = this.mView.getRotation();
      continue;
      f = this.mView.getRotationX();
      continue;
      f = this.mView.getRotationY();
      continue;
      f = this.mView.getScaleX();
      continue;
      f = this.mView.getScaleY();
      continue;
      f = this.mView.getX();
      continue;
      f = this.mView.getY();
      continue;
      f = this.mView.getAlpha();
    }
  }

  public static Builder of(View paramView, int paramInt, float paramFloat1, float paramFloat2)
  {
    return new Builder(new ViewPropertyAnimator(paramView, paramInt, paramFloat1, paramFloat2));
  }

  private static void setAnimator(View paramView, Animator paramAnimator)
  {
    if (paramAnimator != null)
      mAnimatorMap.put(paramView, paramAnimator);
    while (true)
    {
      return;
      mAnimatorMap.remove(paramView);
    }
  }

  private void setFinalValues()
  {
    if (this.mFinalVisibility != -1)
      this.mView.setVisibility(this.mFinalVisibility);
    if (this.mFinalValue != 1.4E-45F)
      setValue(this.mFinalValue);
  }

  private void setValue(float paramFloat)
  {
    switch (this.mProperty)
    {
    default:
    case 1:
    case 2:
    case 16:
    case 32:
    case 64:
    case 4:
    case 8:
    case 128:
    case 256:
    case 512:
    }
    while (true)
    {
      return;
      this.mView.setTranslationX(paramFloat);
      continue;
      this.mView.setTranslationY(paramFloat);
      continue;
      this.mView.setRotation(paramFloat);
      continue;
      this.mView.setRotationX(paramFloat);
      continue;
      this.mView.setRotationY(paramFloat);
      continue;
      this.mView.setScaleX(paramFloat);
      continue;
      this.mView.setScaleY(paramFloat);
      continue;
      this.mView.setX(paramFloat);
      continue;
      this.mView.setY(paramFloat);
      continue;
      this.mView.setAlpha(paramFloat);
    }
  }

  private void setupValues()
  {
    this.mFromValue = calulateAnimatorValue(this.mFromValue);
    this.mToValue = calulateAnimatorValue(this.mToValue);
    this.mFinalValue = calulateAnimatorValue(this.mFinalValue);
  }

  public void cancel()
  {
    this.mAnimator.cancel();
  }

  public Animator clone()
  {
    ViewPropertyAnimator localViewPropertyAnimator = (ViewPropertyAnimator)super.clone();
    localViewPropertyAnimator.mAnimator = this.mAnimator.clone();
    localViewPropertyAnimator.mView = this.mView;
    localViewPropertyAnimator.mProperty = this.mProperty;
    localViewPropertyAnimator.mFromValue = this.mFromValue;
    localViewPropertyAnimator.mToValue = this.mToValue;
    localViewPropertyAnimator.mFinalVisibility = this.mFinalVisibility;
    localViewPropertyAnimator.mFinalValue = this.mFinalValue;
    return localViewPropertyAnimator;
  }

  public void end()
  {
    this.mAnimator.end();
  }

  public long getDuration()
  {
    return this.mAnimator.getDuration();
  }

  public float getFinalValue()
  {
    return this.mFinalValue;
  }

  public int getFinalVisibility()
  {
    return this.mFinalVisibility;
  }

  public int getRepeatCount()
  {
    return this.mAnimator.getRepeatCount();
  }

  public int getRepeatMode()
  {
    return this.mAnimator.getRepeatMode();
  }

  public long getStartDelay()
  {
    return this.mAnimator.getStartDelay();
  }

  public float getWidthOrMeasureWidth(View paramView)
  {
    int i = paramView.getWidth();
    if (i == 0)
    {
      paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
      i = paramView.getMeasuredWidth();
    }
    return i;
  }

  public boolean isRunning()
  {
    return this.mAnimator.isRunning();
  }

  public Animator setDuration(long paramLong)
  {
    this.mAnimator.setDuration(paramLong);
    return this;
  }

  public void setFinalValue(float paramFloat)
  {
    this.mFinalValue = paramFloat;
  }

  public void setFinalVisibility(int paramInt)
  {
    this.mFinalVisibility = paramInt;
  }

  public void setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    this.mAnimator.setInterpolator(paramTimeInterpolator);
  }

  public void setRepeatCount(int paramInt)
  {
    this.mAnimator.setRepeatCount(paramInt);
  }

  public void setRepeatMode(int paramInt)
  {
    this.mAnimator.setRepeatMode(paramInt);
  }

  public void setStartDelay(long paramLong)
  {
    this.mAnimator.setStartDelay(paramLong);
  }

  public void start()
  {
    cancelAnimator(this.mView);
    Animation localAnimation = this.mView.getAnimation();
    if (localAnimation != null)
      localAnimation.cancel();
    this.mView.animate().cancel();
    setAnimator(this.mView, this);
    setupValues();
    if (this.mView.getVisibility() != 0)
      this.mView.setVisibility(0);
    this.mAnimator.start();
  }

  public static class Builder extends AnimatorBuilder
  {
    public Builder(ViewPropertyAnimator paramViewPropertyAnimator)
    {
      super();
    }

    public Builder addListener(Animator.AnimatorListener paramAnimatorListener)
    {
      this.mAnimator.addListener(paramAnimatorListener);
      return this;
    }

    public Builder setDuration(long paramLong)
    {
      this.mAnimator.setDuration(paramLong);
      return this;
    }

    public Builder setFinalValue(float paramFloat)
    {
      ((ViewPropertyAnimator)this.mAnimator).setFinalValue(paramFloat);
      return this;
    }

    public Builder setFinalVisibility(int paramInt)
    {
      ((ViewPropertyAnimator)this.mAnimator).setFinalVisibility(paramInt);
      return this;
    }

    public Builder setInterpolator(TimeInterpolator paramTimeInterpolator)
    {
      this.mAnimator.setInterpolator(paramTimeInterpolator);
      return this;
    }

    public Builder setRepeatCount(int paramInt)
    {
      ((ViewPropertyAnimator)this.mAnimator).setRepeatCount(paramInt);
      return this;
    }

    public Builder setRepeatMode(int paramInt)
    {
      ((ViewPropertyAnimator)this.mAnimator).setRepeatMode(paramInt);
      return this;
    }

    public Builder setStartDelay(long paramLong)
    {
      this.mAnimator.setStartDelay(paramLong);
      return this;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.animation.ViewPropertyAnimator
 * JD-Core Version:    0.6.0
 */