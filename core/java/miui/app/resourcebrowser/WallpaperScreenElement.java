package miui.app.screenelement;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import miui.content.res.ThemeResources;
import org.w3c.dom.Element;

public class WallpaperScreenElement extends ImageScreenElement
{
  public static final String TAG_NAME = "Wallpaper";

  public WallpaperScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    BitmapDrawable localBitmapDrawable = (BitmapDrawable)ThemeResources.getLockWallpaperCache(this.mContext.mContext);
    if (localBitmapDrawable != null)
      this.mBitmap = localBitmapDrawable.getBitmap();
  }

  public float getHeight()
  {
    float f;
    if (this.mBitmap == null)
      f = 0.0F;
    while (true)
    {
      return f;
      f = this.mBitmap.getHeight();
    }
  }

  public float getMaxHeight()
  {
    float f1 = this.mAni.getMaxHeight();
    float f2;
    if (f1 >= 0.0F)
      f2 = descale(f1);
    while (true)
    {
      return f2;
      f2 = getHeight();
    }
  }

  public float getMaxWidth()
  {
    float f1 = this.mAni.getMaxWidth();
    float f2;
    if (f1 >= 0.0F)
      f2 = descale(f1);
    while (true)
    {
      return f2;
      f2 = getWidth();
    }
  }

  public float getWidth()
  {
    float f;
    if (this.mBitmap == null)
      f = 0.0F;
    while (true)
    {
      return f;
      f = this.mBitmap.getWidth();
    }
  }

  protected boolean shouldScaleBitmap()
  {
    return false;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.WallpaperScreenElement
 * JD-Core Version:    0.6.0
 */