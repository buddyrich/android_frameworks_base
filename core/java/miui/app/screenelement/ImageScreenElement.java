package miui.app.screenelement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ImageScreenElement extends AnimatedScreenElement
{
  private static final String LOG_TAG = "ImageScreenElement";
  public static final String MASK_TAG_NAME = "Mask";
  public static final String TAG_NAME = "Image";
  private boolean mAntiAlias;
  protected Bitmap mBitmap;
  private Rect mDesRect = new Rect();
  private String mKey;
  private Paint mMaskPaint = new Paint();
  private ArrayList<AnimatedElement> mMasks;
  private int mOldDensity;
  private Paint mPaint = new Paint();

  public ImageScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    load(paramElement);
    this.mAntiAlias = Boolean.parseBoolean(paramElement.getAttribute("antiAlias"));
    this.mPaint.setFilterBitmap(this.mAntiAlias);
    this.mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
  }

  private String getKey()
  {
    if (this.mBitmap != null)
      if (this.mKey == null)
        this.mKey = UUID.randomUUID().toString();
    for (String str = this.mKey; ; str = this.mAni.getBmp())
      return str;
  }

  private void loadMask(Element paramElement)
    throws ScreenElementLoadException
  {
    if (this.mMasks == null)
      this.mMasks = new ArrayList();
    this.mMasks.clear();
    NodeList localNodeList = paramElement.getElementsByTagName("Mask");
    for (int i = 0; i < localNodeList.getLength(); i++)
      this.mMasks.add(new AnimatedElement((Element)localNodeList.item(i), this.mContext));
  }

  private void renderWithMask(Canvas paramCanvas, AnimatedElement paramAnimatedElement, float paramFloat1, float paramFloat2)
  {
    paramCanvas.save();
    Bitmap localBitmap = this.mContext.mResourceManager.getBitmap(paramAnimatedElement.getBmp());
    if (localBitmap == null)
      return;
    float f1 = descale(paramAnimatedElement.getX());
    float f2 = descale(paramAnimatedElement.getY());
    float f3 = descale(paramFloat1);
    float f4 = descale(paramFloat2);
    float f5 = paramAnimatedElement.getRotationAngle();
    label101: int i;
    int j;
    int k;
    if (paramAnimatedElement.isAlignAbsolute())
    {
      if (this.mAni.getRotationAngle() == 0.0F)
      {
        f1 -= f3;
        f2 -= f4;
      }
    }
    else
    {
      paramCanvas.rotate(f5, f1 + descale(paramAnimatedElement.getCenterX()), f2 + descale(paramAnimatedElement.getCenterY()));
      i = Math.round(f1);
      j = Math.round(f2);
      if (paramAnimatedElement.getWidth() <= 0.0F)
        break label343;
      k = Math.round(descale(paramAnimatedElement.getWidth()));
      label165: if (paramAnimatedElement.getHeight() <= 0.0F)
        break label353;
    }
    label343: label353: for (int m = Math.round(descale(paramAnimatedElement.getHeight())); ; m = localBitmap.getHeight())
    {
      this.mDesRect.set(i, j, i + k, j + m);
      paramCanvas.drawBitmap(localBitmap, null, this.mDesRect, this.mMaskPaint);
      paramCanvas.restore();
      break;
      f5 -= this.mAni.getRotationAngle();
      Utils.Point localPoint1 = new Utils.Point(f3, f4);
      Utils.Point localPoint2 = new Utils.Point(f1, f2);
      double d1 = Utils.Dist(localPoint1, localPoint2, true);
      double d2 = Math.atan((f2 - f4) / (f1 - f3)) - 3.1415926535897D * this.mAni.getRotationAngle() / 180.0D;
      f1 = (int)(d1 * Math.cos(d2));
      f2 = (int)(d1 * Math.sin(d2));
      break label101;
      k = localBitmap.getWidth();
      break label165;
    }
  }

  protected Bitmap getBitmap()
  {
    if (this.mBitmap != null);
    for (Bitmap localBitmap = this.mBitmap; ; localBitmap = this.mContext.mResourceManager.getBitmap(this.mAni.getBmp()))
      return localBitmap;
  }

  public void init()
  {
    super.init();
    if (this.mMasks != null)
    {
      Iterator localIterator = this.mMasks.iterator();
      while (localIterator.hasNext())
        ((AnimatedElement)localIterator.next()).init();
    }
  }

  public void load(Element paramElement)
    throws ScreenElementLoadException
  {
    if (paramElement == null)
    {
      Log.e("ImageScreenElement", "node is null");
      throw new ScreenElementLoadException("node is null");
    }
    loadMask(paramElement);
  }

  public void render(Canvas paramCanvas)
  {
    if (!isVisible());
    int i;
    Bitmap localBitmap1;
    do
    {
      do
      {
        return;
        i = this.mAni.getAlpha();
      }
      while (i <= 0);
      localBitmap1 = getBitmap();
    }
    while (localBitmap1 == null);
    boolean bool = paramCanvas.isHardwareAccelerated();
    this.mOldDensity = paramCanvas.getDensity();
    int j;
    label58: float f1;
    label90: float f2;
    label108: float f3;
    label123: float f4;
    label138: float f5;
    float f6;
    if ((!shouldScaleBitmap()) || (bool))
    {
      j = 0;
      paramCanvas.setDensity(j);
      this.mPaint.setAlpha(i);
      if (!shouldScaleBitmap())
        break label307;
      f1 = scale(localBitmap1.getWidth());
      if (!shouldScaleBitmap())
        break label317;
      f2 = scale(localBitmap1.getHeight());
      if (getWidth() < 0.0F)
        break label327;
      f3 = getWidth();
      if (getHeight() < 0.0F)
        break label334;
      f4 = getHeight();
      f5 = getLeft(getX(), f3);
      f6 = getTop(getY(), f4);
      if (this.mMasks.size() != 0)
        break label465;
      paramCanvas.save();
      paramCanvas.rotate(getAngle(), f5 + getCenterX(), f6 + getCenterY());
      if (localBitmap1.getNinePatchChunk() == null)
        break label376;
      NinePatch localNinePatch = this.mContext.mResourceManager.getNinePatch(this.mAni.getBmp());
      if (localNinePatch == null)
        break label341;
      this.mDesRect.set(Math.round(f5), Math.round(f6), Math.round(f5 + f3), Math.round(f6 + f4));
      localNinePatch.draw(paramCanvas, this.mDesRect, this.mPaint);
    }
    while (true)
    {
      paramCanvas.restore();
      paramCanvas.setDensity(this.mOldDensity);
      break;
      j = this.mContext.mRoot.getTargetDensity();
      break label58;
      label307: f1 = localBitmap1.getWidth();
      break label90;
      label317: f2 = localBitmap1.getHeight();
      break label108;
      label327: f3 = f1;
      break label123;
      label334: f4 = f2;
      break label138;
      label341: Log.e("ImageScreenElement", "the image contains ninepatch chunk but couldn't get NinePatch object: " + this.mAni.getBmp());
      continue;
      label376: if ((getWidth() > 0.0F) || (getHeight() > 0.0F) || (bool))
      {
        this.mDesRect.set(Math.round(f5), Math.round(f6), Math.round(f5 + f3), Math.round(f6 + f4));
        paramCanvas.drawBitmap(localBitmap1, null, this.mDesRect, this.mPaint);
        continue;
      }
      paramCanvas.drawBitmap(localBitmap1, f5, f6, this.mPaint);
    }
    label465: float f7 = getMaxWidth();
    float f8;
    float f10;
    label507: Bitmap localBitmap2;
    Canvas localCanvas;
    if (f7 >= 0.0F)
    {
      f8 = descale(f7);
      float f9 = getMaxHeight();
      if (f9 < 0.0F)
        break label676;
      f10 = descale(f9);
      int k = (int)Math.ceil(f8);
      int m = (int)Math.ceil(f10);
      localBitmap2 = this.mContext.mResourceManager.getMaskBufferBitmap(k, m, getKey(), bool);
      localCanvas = new Canvas(localBitmap2);
      localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
      if ((getWidth() <= 0.0F) && (getHeight() <= 0.0F))
        break label683;
      this.mDesRect.set(0, 0, Math.round(descale(f3)), Math.round(descale(f4)));
      localCanvas.drawBitmap(localBitmap1, null, this.mDesRect, this.mPaint);
    }
    while (true)
    {
      Iterator localIterator = this.mMasks.iterator();
      while (localIterator.hasNext())
        renderWithMask(localCanvas, (AnimatedElement)localIterator.next(), f5, f6);
      f8 = f1;
      break;
      label676: f10 = f2;
      break label507;
      label683: localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, null);
    }
    paramCanvas.save();
    paramCanvas.rotate(getAngle(), f5 + getCenterX(), f6 + getCenterY());
    this.mPaint.setAlpha(i);
    float f11;
    label754: float f12;
    if (bool)
      if (shouldScaleBitmap())
      {
        f11 = scale(localBitmap2.getWidth());
        if (!shouldScaleBitmap())
          break label839;
        f12 = scale(localBitmap2.getHeight());
        label773: this.mDesRect.set(Math.round(f5), Math.round(f6), Math.round(f5 + f11), Math.round(f6 + f12));
        paramCanvas.drawBitmap(localBitmap2, null, this.mDesRect, this.mPaint);
      }
    while (true)
    {
      paramCanvas.restore();
      break;
      f11 = localBitmap2.getWidth();
      break label754;
      label839: f12 = localBitmap2.getHeight();
      break label773;
      paramCanvas.drawBitmap(localBitmap2, f5, f6, this.mPaint);
    }
  }

  public void setBitmap(Bitmap paramBitmap)
  {
    this.mBitmap = paramBitmap;
  }

  protected boolean shouldScaleBitmap()
  {
    return true;
  }

  public void tick(long paramLong)
  {
    if (!isVisible());
    while (true)
    {
      return;
      super.tick(paramLong);
      if (this.mMasks == null)
        continue;
      Iterator localIterator = this.mMasks.iterator();
      while (localIterator.hasNext())
        ((AnimatedElement)localIterator.next()).tick(paramLong);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ImageScreenElement
 * JD-Core Version:    0.6.0
 */