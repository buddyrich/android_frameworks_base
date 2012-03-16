package miui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class GuidePopupView extends FrameLayout
{
  public static final int ARROW_BOTTOM_MODE = 1;
  public static final int ARROW_TOP_MODE;
  private View mAnchor;
  private ImageView mArrowLeft;
  private int mArrowMode;
  private ImageView mArrowRight;
  private TextView mContentText;
  private GuidePopupWindow mGuidePopupWindow;
  private Runnable mLayoutRunnable = new Runnable()
  {
    public void run()
    {
      int i = GuidePopupView.this.getWidth();
      int j = GuidePopupView.this.mContentText.getMeasuredWidth();
      int k = GuidePopupView.this.mContentText.getMeasuredHeight();
      int[] arrayOfInt = new int[2];
      GuidePopupView.this.mAnchor.getLocationOnScreen(arrayOfInt);
      int m = GuidePopupView.this.mAnchor.getWidth();
      int n = GuidePopupView.this.mAnchor.getHeight();
      int i1 = arrayOfInt[0] + m / 2;
      int i2 = i - i1;
      int i3 = j / 2;
      int i4 = j - i3;
      int i5 = 0;
      int i6;
      if (GuidePopupView.this.mArrowMode == 1)
      {
        i5 = arrayOfInt[1] - k;
        if (i5 < 0)
          i5 = 0;
        i6 = i5 + GuidePopupView.this.getTop();
        if ((i1 < i3) || (i2 < i4))
          break label300;
        i1 -= i3;
        i2 -= i4;
      }
      while (true)
      {
        int i7 = i6 + GuidePopupView.this.mOffsetY;
        int i8 = i1 + GuidePopupView.this.mOffsetX;
        int i9 = i2 - GuidePopupView.this.mOffsetX;
        GuidePopupView.this.mContentText.layout(i8, i7, i8 + j, i7 + k);
        GuidePopupView.this.mArrowLeft.layout(i8, i7, i8 + i3, i7 + k);
        GuidePopupView.this.mArrowRight.layout(i - i9 - i4, i7, i - i9, i7 + k);
        return;
        if (GuidePopupView.this.mArrowMode != 0)
          break;
        i5 = n + arrayOfInt[1];
        break;
        label300: if (i2 < i4)
        {
          i4 = i2;
          i2 = 0;
          i1 = i - j;
          i3 = j - i4;
          continue;
        }
        if (i1 >= i3)
          continue;
        i3 = i1;
        i1 = 0;
        i2 = i - j;
        i4 = j - i3;
      }
    }
  };
  private int mOffsetX;
  private int mOffsetY;

  public GuidePopupView(Context paramContext)
  {
    super(paramContext);
  }

  public GuidePopupView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = 1;
    if ((paramKeyEvent.getKeyCode() == 82) && (this.mGuidePopupWindow != null))
      if (paramKeyEvent.getAction() == i)
        this.mGuidePopupWindow.dismiss();
    while (true)
    {
      return i;
      boolean bool = super.dispatchKeyEvent(paramKeyEvent);
    }
  }

  public int getArrowMode()
  {
    return this.mArrowMode;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ((ViewGroup)getParent()).requestChildFocus(this, null);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mArrowLeft = ((ImageView)findViewById(51052597));
    this.mArrowRight = ((ImageView)findViewById(51052598));
    this.mContentText = ((TextView)findViewById(51052599));
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    post(this.mLayoutRunnable);
  }

  public void setAnchor(View paramView)
  {
    this.mAnchor = paramView;
  }

  public void setArrowMode(int paramInt)
  {
    this.mArrowMode = paramInt;
    switch (paramInt)
    {
    default:
    case 1:
    case 0:
    }
    while (true)
    {
      return;
      this.mArrowLeft.setImageResource(50462781);
      this.mArrowRight.setImageResource(50462782);
      this.mContentText.setBackgroundResource(50462783);
      continue;
      this.mArrowLeft.setImageResource(50462784);
      this.mArrowRight.setImageResource(50462785);
      this.mContentText.setBackgroundResource(50462786);
    }
  }

  public void setGuidePopupWindow(GuidePopupWindow paramGuidePopupWindow)
  {
    this.mGuidePopupWindow = paramGuidePopupWindow;
  }

  public void setGuideText(int paramInt)
  {
    this.mContentText.setText(paramInt);
  }

  public void setGuideText(String paramString)
  {
    this.mContentText.setText(paramString);
  }

  public void setOffset(int paramInt1, int paramInt2)
  {
    this.mOffsetX = paramInt1;
    this.mOffsetY = paramInt2;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.GuidePopupView
 * JD-Core Version:    0.6.0
 */