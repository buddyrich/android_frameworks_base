package miui.app.screenelement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import org.w3c.dom.Element;

public class ImageNumberScreenElement extends AnimatedScreenElement
{
  private static final String LOG_TAG = "ImageNumberScreenElement";
  public static final String TAG_NAME = "ImageNumber";
  private IndexedNumberVariable mActualWidthVar;
  private Expression mNumExpression;
  private Paint mPaint = new Paint();

  public ImageNumberScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    load(paramElement);
    if (this.mHasName)
      this.mActualWidthVar = new IndexedNumberVariable(this.mName, "actual_w", paramScreenContext.mVariables);
  }

  protected Bitmap getBitmap(char paramChar)
  {
    String str = Utils.addFileNameSuffix(this.mAni.getBmp(), String.valueOf(paramChar));
    return this.mContext.mResourceManager.getBitmap(str);
  }

  public void load(Element paramElement)
    throws ScreenElementLoadException
  {
    if (paramElement == null)
    {
      Log.e("ImageNumberScreenElement", "node is null");
      throw new ScreenElementLoadException("node is null");
    }
    this.mNumExpression = Expression.build(paramElement.getAttribute("number"));
  }

  public void render(Canvas paramCanvas)
  {
    if (!isVisible());
    while (true)
    {
      return;
      int i = this.mAni.getAlpha();
      if (i <= 0)
        continue;
      String str = String.valueOf((int)this.mNumExpression.evaluate(this.mContext.mVariables));
      float f1 = this.mAni.getX();
      float f2 = 0.0F;
      this.mPaint.setAlpha(i);
      for (int j = 0; j < str.length(); j++)
      {
        Bitmap localBitmap = getBitmap(str.charAt(j));
        if (localBitmap == null)
          continue;
        paramCanvas.drawBitmap(localBitmap, f1 + f2, this.mAni.getY(), this.mPaint);
        f2 += localBitmap.getWidth();
      }
      if (!this.mHasName)
        continue;
      this.mActualWidthVar.set(f2);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ImageNumberScreenElement
 * JD-Core Version:    0.6.0
 */