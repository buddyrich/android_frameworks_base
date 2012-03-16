package miui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class AlphabetFastIndexer extends ImageView
{
  private static final int FADE_DELAYED = 1500;
  private static final int MSG_FADE = 1;
  private static final int R_DEFAULT_INDEXER_ALPHABET_TABLE = 50724867;
  private static final int R_DEFAULT_INDEXER_BG = 50462720;
  private static final int R_DEFAULT_INDEXER_OVERLAY = 50462723;
  private static final int R_DEFAULT_INDEXER_OVERLAY_OFFSET = 50987018;
  private static final int R_DEFAULT_INDEXER_OVERLAY_TEXT_COLOR = 50790403;
  private static final int R_DEFAULT_INDEXER_OVERLAY_TEXT_SIZE = 50987015;
  private static final int R_DEFAULT_INDEXER_OVERLAY_TOP_PADDING = 50987016;
  private static final int R_DEFAULT_INDEXER_POS_MASK = 50462724;
  private static final int R_DEFAULT_INDEXER_TEXT_COLOR = 50790420;
  private static final int R_DEFAULT_INDEXER_TEXT_SIZE = 50987014;
  public static final int STATE_DRAGGING = 1;
  public static final int STATE_NONE;
  private String[] mAlphabetTable;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case 1:
      }
      while (true)
      {
        return;
        if (AlphabetFastIndexer.this.mOverlay != null)
        {
          AlphabetFastIndexer.this.mOverlay.setVisibility(8);
          continue;
        }
      }
    }
  };
  private int mLastAlphabetIndex;
  private AbsListView mListView;
  private TextView mOverlay;
  private int mOverlayLeftMargin;
  private int mOverlayTopMargin;
  private final TextPaint mPaint;
  private ImageView mPosMask;
  private Runnable mRefreshMaskRunnable = new Runnable()
  {
    public void run()
    {
      AlphabetFastIndexer.this.refreshMask();
    }
  };
  private int mState = 0;
  private final Rect mTextBounds = new Rect();
  private int mVerticalPosition;

  public AlphabetFastIndexer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = paramContext.getResources();
    TextPaint localTextPaint = new TextPaint();
    localTextPaint.setTextSize(localResources.getDimension(50987014));
    localTextPaint.setAntiAlias(true);
    localTextPaint.setTextAlign(Paint.Align.CENTER);
    localTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
    localTextPaint.setColor(localResources.getColor(50790420));
    this.mPaint = localTextPaint;
    this.mAlphabetTable = localResources.getStringArray(50724867);
    this.mOverlayLeftMargin = (int)localResources.getDimension(50987018);
    this.mOverlayTopMargin = 0;
    setVisibility(8);
    setBackgroundDrawable(localResources.getDrawable(50462720));
    this.mVerticalPosition = 5;
  }

  private void drawThumbInternal(CharSequence paramCharSequence)
  {
    if (this.mListView == null);
    while (true)
    {
      return;
      this.mOverlay.setText(paramCharSequence);
      this.mOverlay.setVisibility(0);
      this.mHandler.removeMessages(1);
      Message localMessage = this.mHandler.obtainMessage(1);
      this.mHandler.sendMessageDelayed(localMessage, 1500L);
    }
  }

  private int getListOffset()
  {
    if ((this.mListView instanceof ListView));
    for (int i = ((ListView)this.mListView).getHeaderViewsCount(); ; i = 0)
      return i;
  }

  private float getPostion(float paramFloat, SectionIndexer paramSectionIndexer)
  {
    float f = -1.0F;
    Object[] arrayOfObject = paramSectionIndexer.getSections();
    if (arrayOfObject == null);
    while (true)
    {
      return f;
      int i = getPaddingTop();
      int j = getPaddingBottom();
      int k = getHeight() - i - j;
      if (k <= 0)
        continue;
      int m = (int)(0.5F + (paramFloat - i) / k * this.mAlphabetTable.length);
      if (m < 0)
        continue;
      if (m >= this.mAlphabetTable.length)
      {
        f = 1.0F;
        continue;
      }
      int n = Arrays.binarySearch(arrayOfObject, this.mAlphabetTable[m]);
      if (n < 0)
        n = -2 + -n;
      f = n / arrayOfObject.length;
    }
  }

  private SectionIndexer getSectionIndexer()
  {
    SectionIndexer localSectionIndexer;
    if (this.mListView == null)
      localSectionIndexer = null;
    while (true)
    {
      return localSectionIndexer;
      localSectionIndexer = null;
      ListAdapter localListAdapter = (ListAdapter)this.mListView.getAdapter();
      if ((localListAdapter instanceof SectionIndexer))
      {
        localSectionIndexer = (SectionIndexer)localListAdapter;
        continue;
      }
      if (!(localListAdapter instanceof HeaderViewListAdapter))
        continue;
      HeaderViewListAdapter localHeaderViewListAdapter = (HeaderViewListAdapter)localListAdapter;
      if (!(localHeaderViewListAdapter.getWrappedAdapter() instanceof SectionIndexer))
        continue;
      localSectionIndexer = (SectionIndexer)localHeaderViewListAdapter.getWrappedAdapter();
    }
  }

  private void scrollTo(SectionIndexer paramSectionIndexer, float paramFloat)
  {
    int i = this.mListView.getCount();
    int j = getListOffset();
    float f1 = 1.0F / i / 8.0F;
    Object[] arrayOfObject = paramSectionIndexer.getSections();
    int m;
    if ((arrayOfObject != null) && (arrayOfObject.length > 1))
    {
      int n = arrayOfObject.length;
      int i1 = (int)(paramFloat * n);
      if (i1 >= n)
        i1 = n - 1;
      int i2 = i1;
      m = i1;
      int i3 = paramSectionIndexer.getPositionForSection(i1);
      int i4 = i;
      int i5 = i3;
      int i6 = i1;
      int i7 = i1 + 1;
      int i8 = n - 1;
      if (i1 < i8)
        i4 = paramSectionIndexer.getPositionForSection(i1 + 1);
      if (i4 == i3)
        if (i1 > 0)
        {
          i1--;
          i5 = paramSectionIndexer.getPositionForSection(i1);
          if (i5 == i3)
            break label204;
          i6 = i1;
        }
      for (m = i1; ; m = 0)
      {
        int i9 = i7 + 1;
        while ((i9 < n) && (paramSectionIndexer.getPositionForSection(i9) == i4))
        {
          i9++;
          i7++;
        }
        label204: if (i1 != 0)
          break;
      }
      float f2 = i6 / n;
      float f3 = i7 / n;
      int i10;
      if ((i6 == i2) && (paramFloat - f2 < f1))
      {
        i10 = i5;
        if (i10 > i - 1)
          i10 = i - 1;
        if (!(this.mListView instanceof ExpandableListView))
          break label366;
        ExpandableListView localExpandableListView2 = (ExpandableListView)this.mListView;
        localExpandableListView2.setSelectionFromTop(localExpandableListView2.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(i10 + j)), 0);
      }
      while (true)
      {
        if (m >= 0)
        {
          String str = arrayOfObject[m].toString();
          if (!TextUtils.isEmpty(str))
            drawThumbInternal(str.subSequence(0, 1));
        }
        return;
        i10 = i5 + (int)((i4 - i5) * (paramFloat - f2) / (f3 - f2));
        break;
        label366: if ((this.mListView instanceof ListView))
        {
          ((ListView)this.mListView).setSelectionFromTop(i10 + j, 0);
          continue;
        }
        this.mListView.setSelection(i10 + j);
      }
    }
    int k = (int)(paramFloat * i);
    if ((this.mListView instanceof ExpandableListView))
    {
      ExpandableListView localExpandableListView1 = (ExpandableListView)this.mListView;
      localExpandableListView1.setSelectionFromTop(localExpandableListView1.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(k + j)), 0);
    }
    while (true)
    {
      m = -1;
      break;
      if ((this.mListView instanceof ListView))
      {
        ((ListView)this.mListView).setSelectionFromTop(k + j, 0);
        continue;
      }
      this.mListView.setSelection(k + j);
    }
  }

  public void attatch(AbsListView paramAbsListView)
  {
    if (this.mListView == paramAbsListView);
    while (true)
    {
      return;
      detach();
      if (paramAbsListView == null)
        continue;
      this.mLastAlphabetIndex = -1;
      this.mListView = paramAbsListView;
      Context localContext = getContext();
      this.mPosMask = new ImageView(localContext);
      this.mPosMask.setImageResource(50462724);
      FrameLayout localFrameLayout = (FrameLayout)getParent();
      FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-2, -2, 0x30 | this.mVerticalPosition);
      this.mPosMask.setLayoutParams(localLayoutParams1);
      this.mPosMask.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
      this.mPosMask.setScaleType(ImageView.ScaleType.CENTER);
      this.mPosMask.setVisibility(0);
      localFrameLayout.addView(this.mPosMask, 1);
      this.mOverlay = new TextView(localContext);
      FrameLayout.LayoutParams localLayoutParams2 = new FrameLayout.LayoutParams(-2, -2, 17);
      localLayoutParams2.leftMargin = this.mOverlayLeftMargin;
      localLayoutParams2.topMargin = this.mOverlayTopMargin;
      this.mOverlay.setLayoutParams(localLayoutParams2);
      this.mOverlay.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
      this.mOverlay.setVisibility(8);
      this.mOverlay.setBackgroundResource(50462723);
      this.mOverlay.setGravity(49);
      Resources localResources = localContext.getResources();
      this.mOverlay.setPadding(0, (int)localResources.getDimension(50987016), 0, 0);
      this.mOverlay.setTextSize(localResources.getDimension(50987015));
      this.mOverlay.setTextColor(localResources.getColor(50790403));
      localFrameLayout.addView(this.mOverlay);
      FrameLayout.LayoutParams localLayoutParams3 = (FrameLayout.LayoutParams)getLayoutParams();
      localLayoutParams3.gravity = (0x30 | this.mVerticalPosition);
      localLayoutParams3.width = getIndexerIntrinsicWidth();
      setLayoutParams(localLayoutParams3);
      setVisibility(0);
      refreshMask();
    }
  }

  public AbsListView.OnScrollListener decorateScrollListener(AbsListView.OnScrollListener paramOnScrollListener)
  {
    return new OnScrollerDecorator(this, paramOnScrollListener);
  }

  public void detach()
  {
    if (this.mListView != null)
    {
      stop(0);
      FrameLayout localFrameLayout = (FrameLayout)getParent();
      localFrameLayout.removeView(this.mOverlay);
      localFrameLayout.removeView(this.mPosMask);
      setVisibility(8);
      this.mListView = null;
    }
  }

  public void drawThumb(CharSequence paramCharSequence)
  {
    if (this.mState == 0)
      drawThumbInternal(paramCharSequence);
  }

  public int getIndexerIntrinsicWidth()
  {
    Drawable localDrawable = getBackground();
    if (localDrawable != null);
    for (int i = localDrawable.getIntrinsicWidth(); ; i = 0)
      return i;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i = getPaddingTop();
    int j = getHeight() - i - getPaddingBottom();
    if (j <= 0);
    while (true)
    {
      return;
      TextPaint localTextPaint = this.mPaint;
      Rect localRect = this.mTextBounds;
      String[] arrayOfString = this.mAlphabetTable;
      float f1 = j / this.mAlphabetTable.length;
      float f2 = getWidth() / 2.0F;
      for (int k = 0; k < arrayOfString.length; k++)
      {
        String str = arrayOfString[k];
        localTextPaint.getTextBounds(str, 0, str.length(), localRect);
        float f3 = f1 * k + i + (f1 - (localRect.top - localRect.bottom)) / 2.0F;
        paramCanvas.drawText(str, 0, str.length(), f2, f3, localTextPaint);
      }
    }
  }

  public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mLastAlphabetIndex = -1;
    if (this.mPosMask != null)
    {
      this.mPosMask.removeCallbacks(this.mRefreshMaskRunnable);
      this.mPosMask.post(this.mRefreshMaskRunnable);
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = 0;
    if (this.mListView == null)
      stop(0);
    SectionIndexer localSectionIndexer;
    while (true)
    {
      return i;
      localSectionIndexer = getSectionIndexer();
      if (localSectionIndexer != null)
        break;
      stop(0);
    }
    switch (0xFF & paramMotionEvent.getAction())
    {
    case 1:
    default:
      stop(1500);
    case 0:
    case 2:
    }
    while (true)
    {
      i = 1;
      break;
      this.mState = 1;
      setPressed(true);
      float f = getPostion(paramMotionEvent.getY(), localSectionIndexer);
      if (f < 0.0F)
      {
        this.mListView.setSelection(0);
        continue;
      }
      scrollTo(localSectionIndexer, f);
    }
  }

  public void refreshMask()
  {
    if (this.mListView == null);
    while (true)
    {
      return;
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.mPosMask.getLayoutParams();
      if (localLayoutParams == null)
        continue;
      int i = 0;
      SectionIndexer localSectionIndexer = getSectionIndexer();
      if (localSectionIndexer != null)
      {
        int k = localSectionIndexer.getSectionForPosition(this.mListView.getFirstVisiblePosition() - getListOffset());
        if (k != -1)
        {
          String str = (String)localSectionIndexer.getSections()[k];
          if (!TextUtils.isEmpty(str))
            i = Arrays.binarySearch(this.mAlphabetTable, str);
        }
      }
      if ((this.mLastAlphabetIndex == i) || (this.mPosMask == null))
        continue;
      int j = this.mPosMask.getHeight();
      if (j <= 0)
        continue;
      this.mLastAlphabetIndex = i;
      float f1 = getPaddingTop();
      float f2 = getPaddingBottom();
      float f3 = (getHeight() - f1 - f2) / this.mAlphabetTable.length;
      localLayoutParams.topMargin = (int)(0.5F + (f1 + f3 * i - (j - f3) / 2.0F));
      this.mPosMask.setLayoutParams(localLayoutParams);
    }
  }

  public void setOverlayOffset(int paramInt1, int paramInt2)
  {
    this.mOverlayLeftMargin = paramInt1;
    this.mOverlayTopMargin = paramInt2;
  }

  public void setVerticalPosition(boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 5; ; i = 3)
    {
      this.mVerticalPosition = i;
      return;
    }
  }

  void stop(int paramInt)
  {
    setPressed(false);
    this.mState = 0;
    this.mHandler.removeMessages(1);
    if (paramInt <= 0)
      if (this.mOverlay != null)
        this.mOverlay.setVisibility(8);
    while (true)
    {
      return;
      Message localMessage = this.mHandler.obtainMessage(1);
      this.mHandler.sendMessageDelayed(localMessage, paramInt);
    }
  }

  private static class OnScrollerDecorator
    implements AbsListView.OnScrollListener
  {
    private final WeakReference<AlphabetFastIndexer> mIndexerRef;
    private final AbsListView.OnScrollListener mListener;

    public OnScrollerDecorator(AlphabetFastIndexer paramAlphabetFastIndexer, AbsListView.OnScrollListener paramOnScrollListener)
    {
      this.mIndexerRef = new WeakReference(paramAlphabetFastIndexer);
      this.mListener = paramOnScrollListener;
    }

    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
    {
      AlphabetFastIndexer localAlphabetFastIndexer = (AlphabetFastIndexer)this.mIndexerRef.get();
      if (localAlphabetFastIndexer != null)
        localAlphabetFastIndexer.refreshMask();
      if (this.mListener != null)
        this.mListener.onScroll(paramAbsListView, paramInt1, paramInt2, paramInt3);
    }

    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
    {
      if (this.mListener != null)
        this.mListener.onScrollStateChanged(paramAbsListView, paramInt);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.AlphabetFastIndexer
 * JD-Core Version:    0.6.0
 */