package com.miui.internal.policy.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.View;

public class AlbumFrameView extends View
{
  public static final int MATRIX_VALUES_SIZE = 9;
  private Bitmap mDisplayBitmap = null;
  private final Matrix mDisplayMatrix = new Matrix();
  private Bitmap mFilterBitmap = null;
  private Rect mFilterBitmapPaddingRect;
  private Paint mFilterPaint = new Paint();
  private Paint mMaskPaint = new Paint();
  private final float[] mMatrixValuesTemp = new float[9];
  private PaintFlagsDrawFilter mPaintFlags = new PaintFlagsDrawFilter(0, 7);
  private float[] mPrepareValues = null;
  private Bitmap mTempBitmap = null;

  public AlbumFrameView(Context paramContext)
  {
    this(paramContext, null);
  }

  public AlbumFrameView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public AlbumFrameView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mFilterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    this.mFilterBitmapPaddingRect = new Rect();
  }

  private void fitCenter()
  {
    if (this.mDisplayBitmap == null);
    while (true)
    {
      return;
      if (this.mFilterBitmap != null)
      {
        float f1 = this.mFilterBitmap.getWidth() - this.mFilterBitmapPaddingRect.left - this.mFilterBitmapPaddingRect.right;
        float f2 = this.mFilterBitmap.getHeight() - this.mFilterBitmapPaddingRect.top - this.mFilterBitmapPaddingRect.bottom;
        float f3 = this.mDisplayBitmap.getWidth();
        float f4 = this.mDisplayBitmap.getHeight();
        float f5 = Math.max(f1 / f3, f2 / f4);
        this.mDisplayMatrix.reset();
        this.mDisplayMatrix.postScale(f5, f5);
        this.mDisplayMatrix.postTranslate(this.mFilterBitmapPaddingRect.left, this.mFilterBitmapPaddingRect.top);
        continue;
      }
    }
  }

  private void updateTempBitmap()
  {
    int i = getHeight();
    int j = getWidth();
    if ((this.mTempBitmap == null) || (this.mTempBitmap.getHeight() != i) || (this.mTempBitmap.getWidth() != j))
      if ((j > 0) && (i > 0));
    while (true)
    {
      return;
      this.mTempBitmap = Bitmap.createBitmap(j, i, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(this.mTempBitmap);
      localCanvas.setDrawFilter(this.mPaintFlags);
      localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
      Bitmap localBitmap = this.mDisplayBitmap;
      fitCenter();
      if (localBitmap != null)
      {
        localCanvas.save();
        if (this.mDisplayMatrix != null)
          localCanvas.concat(this.mDisplayMatrix);
        localCanvas.drawBitmap(localBitmap, 0.0F, 0.0F, null);
        localCanvas.restore();
      }
      int k = getWidth();
      int m = getHeight();
      int n = 0;
      int i1 = 0;
      if (this.mFilterBitmap != null)
      {
        n = (k - this.mFilterBitmap.getWidth()) / 2;
        i1 = (m - this.mFilterBitmap.getHeight()) / 2;
        localCanvas.drawBitmap(this.mFilterBitmap, n, i1, this.mFilterPaint);
      }
      if ((n <= 0) || (i1 <= 0))
        continue;
      localCanvas.clipRect(n, i1, k - n, m - i1, Region.Op.DIFFERENCE);
      localCanvas.drawRect(0.0F, 0.0F, k, m, this.mMaskPaint);
    }
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.setDrawFilter(this.mPaintFlags);
    updateTempBitmap();
    paramCanvas.drawBitmap(this.mTempBitmap, 0.0F, 0.0F, null);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean)
    {
      if (this.mPrepareValues != null)
        break label27;
      fitCenter();
    }
    while (true)
    {
      return;
      label27: System.arraycopy(this.mPrepareValues, 0, this.mMatrixValuesTemp, 0, 9);
      this.mDisplayMatrix.setValues(this.mMatrixValuesTemp);
    }
  }

  public void recyleFilterBitmap()
  {
    if (this.mFilterBitmap != null)
    {
      this.mFilterBitmap.recycle();
      this.mFilterBitmap = null;
    }
  }

  public void setDisplayBitmap(Bitmap paramBitmap)
  {
    this.mDisplayBitmap = paramBitmap;
    invalidate();
  }

  public void setFilterBitmap(Bitmap paramBitmap)
  {
    recyleFilterBitmap();
    this.mFilterBitmap = paramBitmap;
    invalidate();
  }

  public void setMatrixValues(float[] paramArrayOfFloat)
  {
    if (getWidth() == 0)
    {
      float[] arrayOfFloat = new float[9];
      System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, 9);
      this.mPrepareValues = arrayOfFloat;
    }
    while (true)
    {
      return;
      this.mPrepareValues = null;
      System.arraycopy(paramArrayOfFloat, 0, this.mMatrixValuesTemp, 0, 9);
      this.mDisplayMatrix.setValues(this.mMatrixValuesTemp);
      invalidate();
    }
  }

  public void setRect(Rect paramRect)
  {
    this.mFilterBitmapPaddingRect = paramRect;
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AlbumFrameView
 * JD-Core Version:    0.6.0
 */