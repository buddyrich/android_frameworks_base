package miui.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;

public class AnimatorBuilder
{
  protected Animator mAnimator;

  public AnimatorBuilder(Animator paramAnimator)
  {
    this.mAnimator = paramAnimator;
  }

  public static AnimatorBuilder of(Animator paramAnimator)
  {
    return new AnimatorBuilder(paramAnimator);
  }

  public static ViewPropertyAnimator.Builder of(ViewPropertyAnimator paramViewPropertyAnimator)
  {
    return new ViewPropertyAnimator.Builder(paramViewPropertyAnimator);
  }

  public AnimatorBuilder addListener(Animator.AnimatorListener paramAnimatorListener)
  {
    this.mAnimator.addListener(paramAnimatorListener);
    return this;
  }

  public Animator animator()
  {
    return this.mAnimator;
  }

  public AnimatorBuilder setDuration(long paramLong)
  {
    this.mAnimator.setDuration(paramLong);
    return this;
  }

  public AnimatorBuilder setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    this.mAnimator.setInterpolator(paramTimeInterpolator);
    return this;
  }

  public AnimatorBuilder setStartDelay(long paramLong)
  {
    this.mAnimator.setStartDelay(paramLong);
    return this;
  }

  public Animator start()
  {
    this.mAnimator.start();
    return this.mAnimator;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.animation.AnimatorBuilder
 * JD-Core Version:    0.6.0
 */