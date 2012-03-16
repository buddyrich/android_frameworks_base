package miui.app.screenelement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImagesInOne
{
  private Bitmap mBitmap;
  private int mCount;
  private Rect mDst = new Rect();
  private int mOneWidth;
  private Rect mSrc = new Rect();

  public ImagesInOne(Bitmap paramBitmap, int paramInt)
  {
    this.mBitmap = paramBitmap;
    this.mOneWidth = paramInt;
    this.mCount = (this.mBitmap.getWidth() / this.mOneWidth);
    if (this.mBitmap.getWidth() % this.mOneWidth != 0)
      throw new IllegalArgumentException("invalid width");
  }

  public void draw(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, Paint paramPaint)
  {
    if (paramInt3 > -1 + this.mCount)
      throw new IllegalArgumentException("invalid index");
    this.mDst.set(paramInt1, paramInt2, paramInt1 + this.mOneWidth, paramInt2 + this.mBitmap.getHeight());
    int i = paramInt3 * this.mOneWidth;
    this.mSrc.set(i, 0, i + this.mOneWidth, this.mBitmap.getHeight());
    paramCanvas.drawBitmap(this.mBitmap, this.mSrc, this.mDst, paramPaint);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ImagesInOne
 * JD-Core Version:    0.6.0
 */