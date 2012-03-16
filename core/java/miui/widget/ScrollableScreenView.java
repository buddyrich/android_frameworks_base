package miui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ScrollView;

public class ScrollableScreenView extends ScreenView
{
  private static final int DEFAULT_SCREEN_SNAP_DURATION = 100;
  private OnScrollOutListener mScrollOutListener;
  private ScrollView mScrollView;

  public ScrollableScreenView(Context paramContext)
  {
    super(paramContext);
  }

  public ScrollableScreenView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ScrollableScreenView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void makeWholeViewVisiable()
  {
    if (this.mScrollView == null);
    while (true)
    {
      return;
      int i = getTop();
      ViewParent localViewParent = getParent();
      while ((localViewParent != null) && (localViewParent != this.mScrollView))
      {
        if ((localViewParent instanceof View))
        {
          i += ((View)localViewParent).getTop();
          localViewParent = localViewParent.getParent();
          continue;
        }
        localViewParent = null;
      }
      if ((localViewParent != this.mScrollView) || (this.mScrollView.getScrollY() <= i))
        continue;
      this.mScrollView.startAnimation(new ScrollAnimation(this.mScrollView.getScrollY(), i));
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (2 == (0xFF & paramMotionEvent.getAction()))
      requestDisallowInterceptTouchEvent(true);
    return super.onTouchEvent(paramMotionEvent);
  }

  public void setOnScrollOutListener(OnScrollOutListener paramOnScrollOutListener)
  {
    this.mScrollOutListener = paramOnScrollOutListener;
  }

  public void setParentScrollView(ScrollView paramScrollView)
  {
    this.mScrollView = paramScrollView;
    if (this.mScrollView != null)
      setScreenSnapDuration(100);
  }

  protected void snapToScreen(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.mNextScreen = Math.max(0, Math.min(paramInt1, getScreenCount() - this.mVisibleRange));
    if ((this.mNextScreen == this.mCurrentScreen) && (this.mScrollOutListener != null))
      if ((this.mScrollX < this.mScrollLeftBound + 0.2D * (this.mChildScreenWidth * this.mOverScrollRatio)) && (this.mScrollOutListener.onScrollOut(this, 0)))
        setCurrentScreen(-1 + getScreenCount());
    while (true)
    {
      makeWholeViewVisiable();
      return;
      if ((this.mScrollX > this.mScrollRightBound - 0.2D * (this.mChildScreenWidth * this.mOverScrollRatio)) && (this.mScrollOutListener.onScrollOut(this, 1)))
      {
        setCurrentScreen(0);
        continue;
      }
      super.snapToScreen(paramInt1, paramInt2, paramBoolean);
      continue;
      super.snapToScreen(paramInt1, paramInt2, paramBoolean);
    }
  }

  private class ScrollAnimation extends Animation
  {
    private int mFromY = 0;
    private int mToY = 0;

    public ScrollAnimation(int paramInt1, int arg3)
    {
      this.mFromY = paramInt1;
      int i;
      this.mToY = i;
      setDuration(500L);
    }

    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      int i = (int)(paramFloat * (this.mToY - this.mFromY));
      ScrollableScreenView.this.mScrollView.scrollTo(ScrollableScreenView.this.mScrollView.getScrollX(), i + this.mFromY);
    }
  }

  public static abstract interface OnScrollOutListener
  {
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;

    public abstract boolean onScrollOut(View paramView, int paramInt);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.ScrollableScreenView
 * JD-Core Version:    0.6.0
 */