package miui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

public class GuidePopupWindow extends PopupWindow
  implements View.OnTouchListener
{
  private View mAnchor;
  private Context mContext;
  private Runnable mDissmissRunnable = new Runnable()
  {
    public void run()
    {
      if (GuidePopupWindow.this.isShowing())
      {
        Animation localAnimation = AnimationUtils.loadAnimation(GuidePopupWindow.this.mContext, 17432577);
        localAnimation.setAnimationListener(new Animation.AnimationListener()
        {
          public void onAnimationEnd(Animation paramAnimation)
          {
            if (GuidePopupWindow.this.isShowing())
              GuidePopupWindow.this.dismiss();
          }

          public void onAnimationRepeat(Animation paramAnimation)
          {
          }

          public void onAnimationStart(Animation paramAnimation)
          {
          }
        });
        GuidePopupWindow.this.mPopupView.startAnimation(localAnimation);
      }
    }
  };
  private GuidePopupView mPopupView;

  public GuidePopupWindow(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    init();
  }

  private void init()
  {
    this.mPopupView = ((GuidePopupView)LayoutInflater.from(this.mContext).inflate(50528258, null, false));
    this.mPopupView.setGuidePopupWindow(this);
    setArrowMode(0);
    Resources localResources = this.mContext.getResources();
    setContentView(this.mPopupView);
    setWidth(-1);
    setHeight(-1);
    setFocusable(true);
    setTouchable(false);
    setLayoutInScreenEnabled(true);
    setSoftInputMode(3);
    setBackgroundDrawable(new BitmapDrawable(localResources));
    update();
  }

  public void enableOutSideWindowTouchDismiss(boolean paramBoolean)
  {
    if (paramBoolean);
    for (Object localObject = null; ; localObject = this)
    {
      setTouchInterceptor((View.OnTouchListener)localObject);
      return;
    }
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    int i = 0;
    int j = (int)paramMotionEvent.getX();
    int k = (int)paramMotionEvent.getY();
    int[] arrayOfInt = new int[2];
    this.mAnchor.getLocationInWindow(arrayOfInt);
    if ((paramMotionEvent.getAction() == 0) && (j >= arrayOfInt[0]) && (j <= this.mAnchor.getWidth() + arrayOfInt[0]) && (k >= arrayOfInt[1]) && (k <= this.mAnchor.getHeight() + arrayOfInt[1]))
    {
      dismiss();
      i = 1;
    }
    while (true)
    {
      return i;
      if (paramMotionEvent.getAction() != 4)
        break;
    }
  }

  public void setArrowMode(int paramInt)
  {
    this.mPopupView.setArrowMode(paramInt);
  }

  public void setGuideText(int paramInt)
  {
    this.mPopupView.setGuideText(paramInt);
  }

  public void setGuideText(String paramString)
  {
    this.mPopupView.setGuideText(paramString);
  }

  public void show(View paramView, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.mPopupView.setAnchor(paramView);
    this.mPopupView.setOffset(paramInt1, paramInt2);
    this.mAnchor = paramView;
    showAtLocation(paramView, 51, 0, 0);
    if (paramBoolean)
      this.mPopupView.postDelayed(this.mDissmissRunnable, 5000L);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.GuidePopupWindow
 * JD-Core Version:    0.6.0
 */