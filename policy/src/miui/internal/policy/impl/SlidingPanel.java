package com.miui.internal.policy.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import miui.util.HapticFeedbackUtil;
import miui.widget.GuidePopupWindow;

public class SlidingPanel extends LinearLayout
{
  private static final int ANIM_MODE_FLY_DOWN = 1;
  private static final int ANIM_MODE_RESET = 0;
  public static final int BACKGROUND_MODE_BATTERY_CHARGING = 2;
  public static final int BACKGROUND_MODE_BATTERY_FULL = 3;
  public static final int BACKGROUND_MODE_BATTERY_LOW = 1;
  public static final int BACKGROUND_MODE_NORMAL = 0;
  private static final long DOUBLE_CLICK_THRESHOLD = 500L;
  private static final String LOG_TAG = "SlidingPanel";
  private static final long SINGLE_CLICK_THRESHOLD = 150L;
  public static final int SLIDER_LEFT = 0;
  public static final int SLIDER_MIDDLE = 1;
  public static final int SLIDER_RIGHT = 2;
  private int MIN_MOVING_THRESHOLD = 14;
  private int MOVING_THRESHOLD;
  private SlidingPanelAnimation mAnimation;
  private int mBackgroundMode = -1;
  private BatteryInfo mBattery;
  private AnimationDrawable mBottomAnimationDrawable;
  private FrameLayout mButtonRegion;
  private int mButtonRegionHeight;
  private FrameLayout mContentArea;
  private FrameLayout mControlRegion;
  private View mCurrentDragView;
  private int mDisplayHeight;
  private int mDisplayWidth;
  private int mDownY = 0;
  private int mFooterRegionHeight;
  private int mGrabbedState = 0;
  private GuidePopupWindow mGuidePopupWindow;
  private HapticFeedbackUtil mHapticFeedbackUtil;
  private boolean mIsPaused = false;
  private boolean mIsPressing;
  private boolean mIsShowBatteryLevel;
  private long mLastDownTime;
  private long mLastSetGrabstateTime = 0L;
  private Slider mLeftSlider;
  private boolean mLongVibrate = false;
  private boolean mMoving;
  private OnTriggerListener mOnTriggerListener;
  private Slider mRightSlider;
  private Runnable mSingleClick = new Runnable()
  {
    public void run()
    {
      SlidingPanel.this.removeCallbacks(SlidingPanel.this.mSingleClick);
      if (!SlidingPanel.this.mMoving)
        SlidingPanel.this.setGrabbedState(3);
      SlidingPanel.access$1102(SlidingPanel.this, 0L);
    }
  };
  private FrameLayout mTimeRegion;
  private int[] mTmpLocation = new int[2];
  private int mTrackingPointerId = -1;
  private boolean mTriggered = false;
  private boolean mWaitForHandleMotionEvent;

  public SlidingPanel(Context paramContext)
  {
    this(paramContext, null);
  }

  public SlidingPanel(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = getResources();
    this.mDisplayWidth = localResources.getDisplayMetrics().widthPixels;
    this.mDisplayHeight = localResources.getDisplayMetrics().heightPixels;
    if (this.mDisplayWidth > this.mDisplayHeight)
    {
      int i = this.mDisplayWidth;
      this.mDisplayWidth = this.mDisplayHeight;
      this.mDisplayHeight = i;
    }
    if (!localResources.getBoolean(50921473));
    for (boolean bool = true; ; bool = false)
    {
      this.mIsShowBatteryLevel = bool;
      setOrientation(1);
      setGravity(80);
      setChildrenDrawnWithCacheEnabled(true);
      this.mBattery = new BatteryInfo(null);
      setupContentArea();
      setHapticFeedbackEnabled(true);
      setBackgroundFor(0);
      this.MOVING_THRESHOLD = (this.mButtonRegionHeight / 8);
      if (this.MOVING_THRESHOLD < this.MIN_MOVING_THRESHOLD)
        this.MOVING_THRESHOLD = this.MIN_MOVING_THRESHOLD;
      this.mAnimation = new SlidingPanelAnimation(this, 0);
      this.mAnimation.setDuration(80L);
      this.mAnimation.setAnimationListener(new Animation.AnimationListener()
      {
        public void onAnimationEnd(Animation paramAnimation)
        {
          if (!SlidingPanel.this.mIsPressing)
            SlidingPanel.this.resetSlidingPanel();
        }

        public void onAnimationRepeat(Animation paramAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnimation)
        {
        }
      });
      this.mHapticFeedbackUtil = new HapticFeedbackUtil(this.mContext, true);
      if (Settings.System.getInt(this.mContext.getContentResolver(), "user_guide_lock_screen_unlock", 1) == 1)
        post(new Runnable()
        {
          public void run()
          {
            SlidingPanel.access$502(SlidingPanel.this, new GuidePopupWindow(SlidingPanel.this.mContext));
            SlidingPanel.this.mGuidePopupWindow.setGuideText(51118122);
            SlidingPanel.this.mGuidePopupWindow.setArrowMode(1);
            SlidingPanel.this.mGuidePopupWindow.show(SlidingPanel.this.mTimeRegion, 0, 0, false);
          }
        });
      return;
    }
  }

  private void dispatchTriggerEvent(int paramInt)
  {
    if (this.mOnTriggerListener != null)
      this.mOnTriggerListener.onTrigger(this, paramInt);
  }

  private Slider getSlider(int paramInt)
  {
    Slider localSlider = null;
    switch (paramInt)
    {
    case 1:
    default:
    case 0:
    case 2:
    }
    while (true)
    {
      return localSlider;
      localSlider = this.mLeftSlider;
      continue;
      localSlider = this.mRightSlider;
    }
  }

  private void handleMotionDown(float paramFloat1, float paramFloat2)
  {
    boolean bool1 = withinView(paramFloat1, paramFloat2, this.mLeftSlider);
    boolean bool2 = withinView(paramFloat1, paramFloat2, this.mRightSlider);
    boolean bool3 = withinView(paramFloat1, paramFloat2, this.mTimeRegion);
    if ((!bool1) && (!bool2) && (!bool3))
    {
      this.mWaitForHandleMotionEvent = true;
      return;
    }
    this.mMoving = false;
    this.mTriggered = false;
    this.mDownY = (int)paramFloat2;
    this.mHapticFeedbackUtil.performHapticFeedback(1, false);
    if (bool3)
    {
      this.mCurrentDragView = this.mTimeRegion;
      if (System.currentTimeMillis() - this.mLastDownTime > 500L)
      {
        this.mLastDownTime = System.currentTimeMillis();
        setGrabbedState(0);
        postDelayed(this.mSingleClick, 150L);
      }
    }
    while (true)
    {
      this.mLastSetGrabstateTime = SystemClock.elapsedRealtime();
      if (this.mCurrentDragView != null)
      {
        this.mIsPressing = true;
        this.mCurrentDragView.setPressed(true);
        setBackgroundFor(this.mBackgroundMode);
      }
      this.mWaitForHandleMotionEvent = false;
      if ((this.mGuidePopupWindow == null) || (!this.mGuidePopupWindow.isShowing()))
        break;
      this.mGuidePopupWindow.dismiss();
      Settings.System.putInt(this.mContext.getContentResolver(), "user_guide_lock_screen_unlock", 0);
      break;
      this.mLastDownTime = 0L;
      setGrabbedState(6);
      continue;
      if (bool1)
      {
        this.mCurrentDragView = this.mLeftSlider;
        setGrabbedState(1);
        continue;
      }
      if (bool2)
      {
        this.mCurrentDragView = this.mRightSlider;
        setGrabbedState(2);
        continue;
      }
      setGrabbedState(4);
    }
  }

  private boolean hitDownThreshold()
  {
    if (-this.mScrollY >= 2 * this.mFooterRegionHeight / 3);
    for (int i = 1; ; i = 0)
      return i;
  }

  private boolean hitVibrateThreshold()
  {
    if (-this.mScrollY >= this.mFooterRegionHeight);
    for (int i = 1; ; i = 0)
      return i;
  }

  private void movePanel(float paramFloat1, float paramFloat2)
  {
    int i = Math.min((int)paramFloat2 - this.mDownY, this.mFooterRegionHeight);
    if (i > 0)
      scrollTo(0, -i);
  }

  private void resetSlidingPanel()
  {
    setBackgroundFor(this.mBackgroundMode);
    if (!hitDownThreshold())
      setGrabbedState(0);
  }

  private void setGrabbedState(int paramInt)
  {
    if (paramInt == 4)
      if (this.mOnTriggerListener != null)
        this.mOnTriggerListener.onGrabbedStateChange(this, this.mGrabbedState);
    while (true)
    {
      return;
      if (paramInt != this.mGrabbedState)
      {
        this.mGrabbedState = paramInt;
        if (this.mOnTriggerListener == null)
          continue;
        this.mOnTriggerListener.onGrabbedStateChange(this, this.mGrabbedState);
        continue;
      }
    }
  }

  private void setupContentArea()
  {
    FrameLayout localFrameLayout = new FrameLayout(this.mContext);
    localFrameLayout.addView(new View(this.mContext), new FrameLayout.LayoutParams(-1, -1, 83));
    this.mControlRegion = new FrameLayout(this.mContext);
    this.mControlRegion.setVisibility(8);
    localFrameLayout.addView(this.mControlRegion, new FrameLayout.LayoutParams(-1, -2, 83));
    addView(localFrameLayout, new LinearLayout.LayoutParams(-1, 0, 1.0F));
    this.mContentArea = new FrameLayout(this.mContext);
    addView(this.mContentArea, new FrameLayout.LayoutParams(-1, -2, 80));
    this.mButtonRegion = new FrameLayout(this.mContext);
    this.mButtonRegion.setDrawingCacheEnabled(true);
    this.mButtonRegion.setBackgroundResource(50462864);
    this.mContentArea.addView(this.mButtonRegion, new FrameLayout.LayoutParams(-1, -2, 80));
    this.mLeftSlider = new Slider(this.mContext);
    this.mLeftSlider.setBackgroundResource(50462831);
    this.mLeftSlider.setImage(50462830);
    this.mButtonRegion.addView(this.mLeftSlider, new FrameLayout.LayoutParams(-1, -1, 17));
    this.mTimeRegion = new FrameLayout(this.mContext);
    this.mTimeRegion.setBackgroundResource(50462852);
    this.mButtonRegion.addView(this.mTimeRegion, new FrameLayout.LayoutParams(-1, -1, 17));
    this.mRightSlider = new Slider(this.mContext);
    this.mRightSlider.setBackgroundResource(50462837);
    this.mRightSlider.setImage(50462836);
    this.mButtonRegion.addView(this.mRightSlider, new FrameLayout.LayoutParams(-1, -1, 17));
    Drawable localDrawable = this.mButtonRegion.getBackground();
    Rect localRect = new Rect();
    localDrawable.getPadding(localRect);
    this.mButtonRegionHeight = (localDrawable.getIntrinsicHeight() - localRect.top - localRect.bottom);
    this.mFooterRegionHeight = localRect.bottom;
    ImageView localImageView1 = new ImageView(this.mContext);
    localImageView1.setImageResource(50462874);
    this.mBottomAnimationDrawable = ((AnimationDrawable)localImageView1.getDrawable());
    this.mContentArea.addView(localImageView1, new FrameLayout.LayoutParams(-1, -2, 81));
    ImageView localImageView2 = new ImageView(this.mContext);
    localImageView2.setImageResource(50462866);
    this.mContentArea.addView(localImageView2, new FrameLayout.LayoutParams(-1, -2, 80));
  }

  private boolean withinTouchArea(float paramFloat, View paramView)
  {
    int i = 1;
    paramView.getLocationOnScreen(this.mTmpLocation);
    int j = this.mTmpLocation[i] - this.mContext.getResources().getDimensionPixelOffset(50987013);
    int k = this.mTmpLocation[i] + paramView.getHeight() - paramView.getPaddingBottom();
    if ((j <= paramFloat) && (paramFloat <= k));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  private boolean withinView(float paramFloat1, float paramFloat2, View paramView)
  {
    int i = 1;
    paramView.getLocationOnScreen(this.mTmpLocation);
    int j = this.mTmpLocation[0] + paramView.getPaddingLeft();
    int k = this.mTmpLocation[0] + paramView.getWidth() - paramView.getPaddingRight();
    int m = this.mTmpLocation[i];
    int n = this.mTmpLocation[i] + paramView.getHeight();
    if ((j <= paramFloat1) && (paramFloat1 <= k) && (m <= paramFloat2) && (paramFloat2 <= n));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public void clearBatteryAnimations()
  {
    this.mBattery.clearBatteryAnimations();
  }

  public int getBottomHeight()
  {
    return this.mButtonRegionHeight + this.mFooterRegionHeight;
  }

  public FrameLayout getControlView()
  {
    return this.mControlRegion;
  }

  public int getSliderTextVisibility(int paramInt)
  {
    return getSlider(paramInt).getTextVisibility();
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean)
      this.mBattery.onResume();
  }

  public void onPause()
  {
    this.mIsPaused = true;
    this.mBattery.onPasue();
    if (this.mBottomAnimationDrawable != null)
      this.mBottomAnimationDrawable.stop();
  }

  public void onResume()
  {
    this.mIsPaused = false;
    scrollTo(0, 0);
    setBackgroundFor(this.mBackgroundMode);
    if (this.mBottomAnimationDrawable != null)
      this.mBottomAnimationDrawable.start();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    getLocationOnScreen(this.mTmpLocation);
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (this.mTrackingPointerId < 0)
      if ((i == 5) || (i == 0))
      {
        float f3 = paramMotionEvent.getX(j);
        float f4 = paramMotionEvent.getY(j) + this.mTmpLocation[1];
        if (withinTouchArea(f4, this.mButtonRegion))
        {
          this.mTrackingPointerId = paramMotionEvent.getPointerId(j);
          handleMotionDown(f3, f4);
        }
      }
    while (true)
    {
      return true;
      int k = paramMotionEvent.findPointerIndex(this.mTrackingPointerId);
      if (k < 0)
        paramMotionEvent.setAction(3);
      label114: int m;
      switch (i)
      {
      case 4:
      case 5:
      default:
        break;
      case 1:
        this.mIsPressing = false;
        removeCallbacks(this.mSingleClick);
        if (this.mTriggered)
        {
          this.mTrackingPointerId = -1;
          this.mWaitForHandleMotionEvent = false;
          if (this.mMoving)
            if (this.mCurrentDragView == this.mLeftSlider)
              m = 1;
        }
      case 2:
      case 6:
        while (true)
        {
          dispatchTriggerEvent(m);
          break;
          f1 = paramMotionEvent.getX(k);
          f2 = paramMotionEvent.getY(k) + this.mTmpLocation[1];
          break label114;
          if (this.mWaitForHandleMotionEvent)
            handleMotionDown(f1, f2);
          if ((this.mWaitForHandleMotionEvent) || ((!this.mMoving) && (Math.abs(f2 - this.mDownY) < this.MOVING_THRESHOLD)))
            break;
          if (!this.mMoving)
          {
            setGrabbedState(5);
            this.mMoving = true;
          }
          if (SystemClock.elapsedRealtime() - this.mLastSetGrabstateTime >= 4000L)
          {
            setGrabbedState(4);
            this.mLastSetGrabstateTime = SystemClock.elapsedRealtime();
          }
          movePanel(f1, f2);
          if ((!this.mTriggered) && (hitDownThreshold()))
            this.mTriggered = true;
          if (!hitDownThreshold())
            this.mTriggered = false;
          if ((!this.mLongVibrate) && (hitVibrateThreshold()))
          {
            this.mLongVibrate = true;
            this.mHapticFeedbackUtil.performHapticFeedback(0, false);
          }
          if (hitVibrateThreshold())
            break;
          this.mLongVibrate = false;
          break;
          if (paramMotionEvent.getActionIndex() == k)
            break label155;
          break;
          if (this.mCurrentDragView == this.mRightSlider)
          {
            m = 2;
            continue;
          }
          m = 3;
        }
        resetSlidingPanel();
        continue;
        this.mHapticFeedbackUtil.performHapticFeedback(2, false);
      case 3:
        label155: this.mIsPressing = false;
        removeCallbacks(this.mSingleClick);
        this.mTrackingPointerId = -1;
        this.mWaitForHandleMotionEvent = false;
        this.mTriggered = false;
        if (this.mCurrentDragView != null)
        {
          this.mCurrentDragView.setPressed(false);
          this.mCurrentDragView = null;
        }
        this.mDownY = 0;
        if (this.mMoving)
        {
          startAnimation(this.mAnimation);
          continue;
        }
        resetSlidingPanel();
      }
    }
  }

  public void setBackgroundFor(int paramInt)
  {
    this.mBattery.setBackgroundFor(paramInt);
  }

  public void setBatteryLevel(int paramInt)
  {
    this.mBattery.setBatteryLevel(paramInt);
  }

  public void setOnTriggerListener(OnTriggerListener paramOnTriggerListener)
  {
    this.mOnTriggerListener = paramOnTriggerListener;
  }

  public void setSliderText(int paramInt, String paramString)
  {
    getSlider(paramInt).setText(paramString);
  }

  public void setTimeView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (paramLayoutParams == null)
      paramLayoutParams = new FrameLayout.LayoutParams(-2, -2, 17);
    this.mTimeRegion.removeAllViews();
    this.mTimeRegion.addView(paramView, paramLayoutParams);
    this.mTimeRegion.setVisibility(0);
  }

  private class Slider extends FrameLayout
  {
    private FrameLayout zImageBackground = new FrameLayout(this.mContext);
    private TextView zText;

    public Slider(Context arg2)
    {
      super();
      this.zImageBackground.setVisibility(8);
      addView(this.zImageBackground, new FrameLayout.LayoutParams(-2, -2, 17));
      this.zText = new TextView(this.mContext);
      this.zText.setBackgroundResource(50462908);
      this.zText.setGravity(17);
      this.zText.setTextColor(-1);
      this.zText.setShadowLayer(0.0F, 0.0F, -1.0F, -1610612736);
      this.zText.setTextSize(2, 12.0F);
      this.zText.setVisibility(8);
      FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2);
      localLayoutParams.gravity = 53;
      this.zImageBackground.addView(this.zText, localLayoutParams);
    }

    public int getTextVisibility()
    {
      return this.zText.getVisibility();
    }

    public void setImage(int paramInt)
    {
      this.zImageBackground.setBackgroundResource(paramInt);
      this.zImageBackground.setVisibility(0);
    }

    public void setText(String paramString)
    {
      this.zText.setText(paramString);
      TextView localTextView = this.zText;
      if (TextUtils.isEmpty(paramString));
      for (int i = 8; ; i = 0)
      {
        localTextView.setVisibility(i);
        return;
      }
    }
  }

  private class AnimationSequenceListener
    implements Animation.AnimationListener
  {
    private Animation zNext;
    private View zTarget;

    public AnimationSequenceListener(View paramAnimation, Animation arg3)
    {
      this.zTarget = paramAnimation;
      Object localObject;
      this.zNext = localObject;
    }

    public void onAnimationEnd(Animation paramAnimation)
    {
      this.zTarget.startAnimation(this.zNext);
    }

    public void onAnimationRepeat(Animation paramAnimation)
    {
    }

    public void onAnimationStart(Animation paramAnimation)
    {
    }
  }

  private class BatteryInfo
  {
    private int MAX_BATTERY_LEVEL = 100;
    private ImageView mBatteryAnimationArrow;
    private ImageView mBatteryAnimationLight;
    private ImageView mBatteryAnimationProgress;
    private Runnable mBatteryAnimationRunnable;
    private FrameLayout mBatteryArea;
    private int mBatteryLevel;

    private BatteryInfo()
    {
    }

    public void clearBatteryAnimations()
    {
      if (this.mBatteryArea == null);
      while (true)
      {
        return;
        SlidingPanel.this.removeCallbacks(this.mBatteryAnimationRunnable);
        this.mBatteryAnimationArrow.clearAnimation();
        this.mBatteryAnimationLight.clearAnimation();
      }
    }

    public boolean needLayoutBattery()
    {
      if ((this.mBatteryAnimationArrow != null) && (this.mBatteryAnimationArrow.getAnimation() != null) && (this.mBatteryAnimationArrow.getDrawable() != null));
      for (int i = 1; ; i = 0)
        return i;
    }

    public void onPasue()
    {
      if (this.mBatteryArea != null)
      {
        this.mBatteryArea.setVisibility(8);
        clearBatteryAnimations();
      }
    }

    public void onResume()
    {
      if ((this.mBatteryArea != null) && (SlidingPanel.this.mBattery.needLayoutBattery()))
      {
        clearBatteryAnimations();
        setBatteryAnimations();
      }
    }

    public void setBackgroundFor(int paramInt)
    {
      SlidingPanel.access$1202(SlidingPanel.this, paramInt);
      if (SlidingPanel.this.mIsPaused);
      int j;
      int k;
      int m;
      int n;
      label100: 
      do
      {
        return;
        if (((paramInt != 0) || (SlidingPanel.this.mIsShowBatteryLevel)) && (this.mBatteryArea == null))
          SlidingPanel.this.mBattery.setupBatteryArea();
        int i = 50462906;
        j = 0;
        k = 0;
        m = 0;
        n = -1;
        switch (paramInt)
        {
        case 3:
        default:
          if (SlidingPanel.this.mIsPressing)
            i = 50462907;
          SlidingPanel.this.mButtonRegion.setBackgroundResource(i);
        case 2:
        case 1:
        case 0:
        }
      }
      while (this.mBatteryArea == null);
      int i1;
      if (j != 0)
      {
        i1 = 0;
        label139: this.mBatteryArea.setVisibility(i1);
        this.mBatteryAnimationProgress.setVisibility(i1);
        this.mBatteryAnimationProgress.setImageResource(j);
        if (n != -1)
        {
          FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.mBatteryAnimationProgress.getLayoutParams();
          localLayoutParams.leftMargin = n;
          this.mBatteryAnimationProgress.setLayoutParams(localLayoutParams);
        }
        if (k == 0)
          break label354;
      }
      label354: for (int i2 = 0; ; i2 = 8)
      {
        this.mBatteryAnimationArrow.setVisibility(i2);
        this.mBatteryAnimationLight.setVisibility(i2);
        this.mBatteryAnimationArrow.setImageResource(k);
        this.mBatteryAnimationLight.setImageResource(m);
        clearBatteryAnimations();
        if ((m == 0) || (k == 0) || (j == 0))
          break;
        setBatteryAnimations();
        break;
        m = 50462860;
        j = 50462857;
        k = 50462859;
        break label100;
        j = 50462863;
        n = 0;
        break label100;
        if (!SlidingPanel.this.mIsShowBatteryLevel)
          break label100;
        j = 50462857;
        n = (int)(0.5F + SlidingPanel.this.mDisplayWidth * this.mBatteryLevel / this.MAX_BATTERY_LEVEL) - SlidingPanel.this.mDisplayWidth;
        break label100;
        i1 = 8;
        break label139;
      }
    }

    public void setBatteryAnimations()
    {
      int i = (int)(0.5F + SlidingPanel.this.mDisplayWidth * this.mBatteryLevel / this.MAX_BATTERY_LEVEL);
      int j = SlidingPanel.this.mDisplayWidth - i;
      int k = j;
      NinePatchDrawable localNinePatchDrawable = (NinePatchDrawable)this.mBatteryAnimationProgress.getDrawable();
      if (localNinePatchDrawable != null)
      {
        Rect localRect = new Rect();
        localNinePatchDrawable.getPadding(localRect);
        i -= localRect.right;
        k += localRect.right;
      }
      FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)this.mBatteryAnimationProgress.getLayoutParams();
      localLayoutParams1.leftMargin = (-j);
      this.mBatteryAnimationProgress.setLayoutParams(localLayoutParams1);
      FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams)this.mBatteryAnimationLight.getLayoutParams();
      localLayoutParams2.leftMargin = i;
      this.mBatteryAnimationLight.setLayoutParams(localLayoutParams2);
      Drawable localDrawable = this.mBatteryAnimationArrow.getDrawable();
      FrameLayout.LayoutParams localLayoutParams3 = (FrameLayout.LayoutParams)this.mBatteryAnimationArrow.getLayoutParams();
      localLayoutParams3.rightMargin = (-localDrawable.getIntrinsicWidth());
      this.mBatteryAnimationArrow.setLayoutParams(localLayoutParams3);
      TranslateAnimation localTranslateAnimation1 = new TranslateAnimation(0.0F, -k, 0.0F, 0.0F);
      localTranslateAnimation1.setDuration(1000);
      localTranslateAnimation1.setInterpolator(new AccelerateInterpolator());
      int m = k + 2 * localDrawable.getIntrinsicWidth();
      TranslateAnimation localTranslateAnimation2 = new TranslateAnimation(-k, -m, 0.0F, 0.0F);
      AlphaAnimation localAlphaAnimation1 = new AlphaAnimation(1.0F, 0.0F);
      AnimationSet localAnimationSet = new AnimationSet(false);
      localAnimationSet.addAnimation(localTranslateAnimation2);
      localAnimationSet.addAnimation(localAlphaAnimation1);
      localAnimationSet.setDuration(1000);
      SlidingPanel.AnimationSequenceListener localAnimationSequenceListener1 = new SlidingPanel.AnimationSequenceListener(SlidingPanel.this, this.mBatteryAnimationArrow, localAnimationSet);
      localTranslateAnimation1.setAnimationListener(localAnimationSequenceListener1);
      this.mBatteryAnimationArrow.startAnimation(localTranslateAnimation1);
      AlphaAnimation localAlphaAnimation2 = new AlphaAnimation(0.0F, 0.0F);
      localAlphaAnimation2.setFillAfter(true);
      localAlphaAnimation2.setDuration(1000);
      AlphaAnimation localAlphaAnimation3 = new AlphaAnimation(1.0F, 0.0F);
      localAlphaAnimation3.setFillAfter(true);
      localAlphaAnimation3.setDuration(1000);
      SlidingPanel.AnimationSequenceListener localAnimationSequenceListener2 = new SlidingPanel.AnimationSequenceListener(SlidingPanel.this, this.mBatteryAnimationLight, localAlphaAnimation3);
      localAlphaAnimation2.setAnimationListener(localAnimationSequenceListener2);
      this.mBatteryAnimationLight.startAnimation(localAlphaAnimation2);
      if (this.mBatteryAnimationRunnable == null)
      {
        1 local1 = new Runnable(localTranslateAnimation1, localAlphaAnimation2)
        {
          public void run()
          {
            SlidingPanel.this.removeCallbacks(SlidingPanel.BatteryInfo.this.mBatteryAnimationRunnable);
            SlidingPanel.BatteryInfo.this.mBatteryAnimationArrow.startAnimation(this.val$barTranslate1);
            SlidingPanel.BatteryInfo.this.mBatteryAnimationLight.startAnimation(this.val$lightAlpha1);
            SlidingPanel.this.postDelayed(SlidingPanel.BatteryInfo.this.mBatteryAnimationRunnable, 3500L);
          }
        };
        this.mBatteryAnimationRunnable = local1;
      }
      SlidingPanel.this.postDelayed(this.mBatteryAnimationRunnable, 3500L);
    }

    public void setBatteryLevel(int paramInt)
    {
      if (paramInt > this.MAX_BATTERY_LEVEL)
        paramInt = this.MAX_BATTERY_LEVEL;
      this.mBatteryLevel = paramInt;
    }

    public void setupBatteryArea()
    {
      this.mBatteryArea = new FrameLayout(SlidingPanel.this.mContext);
      this.mBatteryArea.setVisibility(8);
      this.mBatteryArea.setBackgroundResource(50462856);
      this.mBatteryAnimationProgress = new ImageView(SlidingPanel.this.mContext);
      this.mBatteryAnimationProgress.setVisibility(8);
      this.mBatteryArea.addView(this.mBatteryAnimationProgress, new FrameLayout.LayoutParams(-2, -2, 19));
      this.mBatteryAnimationArrow = new ImageView(SlidingPanel.this.mContext);
      this.mBatteryAnimationArrow.setVisibility(8);
      this.mBatteryArea.addView(this.mBatteryAnimationArrow, new FrameLayout.LayoutParams(-2, -2, 21));
      this.mBatteryAnimationLight = new ImageView(SlidingPanel.this.mContext);
      this.mBatteryAnimationLight.setVisibility(8);
      this.mBatteryArea.addView(this.mBatteryAnimationLight, new FrameLayout.LayoutParams(-2, -2, 19));
      SlidingPanel.this.mContentArea.addView(this.mBatteryArea, new FrameLayout.LayoutParams(-1, -2, 49));
    }
  }

  private class SlidingPanelAnimation extends Animation
  {
    private int mDeltaY;
    private int mInitBottom;
    private int mMode = 0;
    private SlidingPanel mPanel;

    public SlidingPanelAnimation(SlidingPanel paramInt, int arg3)
    {
      this.mPanel = paramInt;
      int i;
      this.mMode = i;
    }

    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      this.mPanel.scrollTo(0, this.mInitBottom + (int)(paramFloat * this.mDeltaY));
    }

    public void reset()
    {
      super.reset();
      this.mInitBottom = SlidingPanel.this.mScrollY;
      if (this.mMode == 0)
        this.mDeltaY = (-this.mInitBottom);
      while (true)
      {
        return;
        if (this.mMode == 1)
        {
          this.mDeltaY = (-SlidingPanel.this.mDisplayHeight + this.mInitBottom);
          continue;
        }
      }
    }
  }

  public static abstract interface OnTriggerListener
  {
    public static final int DOUBLE_CLICK_HANDLE = 6;
    public static final int LEFT_HANDLE = 1;
    public static final int MIDDLE_HANDLE = 3;
    public static final int NO_HANDLE = 0;
    public static final int POKE_HANDLE = 4;
    public static final int RIGHT_HANDLE = 2;
    public static final int SLIDING_HANDLE = 5;

    public abstract void onGrabbedStateChange(View paramView, int paramInt);

    public abstract void onTrigger(View paramView, int paramInt);
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.SlidingPanel
 * JD-Core Version:    0.6.0
 */