package miui.app.screenelement;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import miui.widget.SpectrumVisualizer;
import org.w3c.dom.Element;

public class SpectrumVisualizerScreenElement extends ImageScreenElement
{
  public static final String TAG_NAME = "SpectrumVisualizer";
  private Canvas mCanvas;
  private String mDotbar;
  private String mPanel;
  private String mShadow;
  private SpectrumVisualizer mSpectrumVisualizer;

  public SpectrumVisualizerScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    this.mPanel = paramElement.getAttribute("panelSrc");
    this.mDotbar = paramElement.getAttribute("dotbarSrc");
    this.mShadow = paramElement.getAttribute("shadowSrc");
    this.mSpectrumVisualizer = new SpectrumVisualizer(paramScreenContext.mContext);
    this.mSpectrumVisualizer.enableUpdate(false);
  }

  public void enableUpdate(boolean paramBoolean)
  {
    this.mSpectrumVisualizer.enableUpdate(paramBoolean);
  }

  protected Bitmap getBitmap()
  {
    this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    this.mCanvas.setDensity(0);
    this.mSpectrumVisualizer.draw(this.mCanvas);
    this.mCanvas.setDensity(this.mContext.mRoot.getResourceDensity());
    return this.mBitmap;
  }

  public void init()
  {
    Bitmap localBitmap1;
    Bitmap localBitmap2;
    if (TextUtils.isEmpty(this.mPanel))
    {
      localBitmap1 = null;
      if (!TextUtils.isEmpty(this.mDotbar))
        break label150;
      localBitmap2 = null;
      label24: if (!TextUtils.isEmpty(this.mShadow))
        break label168;
    }
    label150: label168: for (Bitmap localBitmap3 = null; ; localBitmap3 = this.mContext.mResourceManager.getBitmap(this.mShadow))
    {
      if ((localBitmap1 != null) && (localBitmap2 != null))
        this.mSpectrumVisualizer.setBitmaps(localBitmap1, localBitmap2, localBitmap3);
      int i = this.mSpectrumVisualizer.getVisualWidth();
      int j = this.mSpectrumVisualizer.getVisualHeight();
      this.mSpectrumVisualizer.layout(0, 0, i, j);
      this.mBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
      this.mBitmap.setDensity(this.mContext.mRoot.getResourceDensity());
      this.mCanvas = new Canvas(this.mBitmap);
      return;
      localBitmap1 = this.mContext.mResourceManager.getBitmap(this.mPanel);
      break;
      localBitmap2 = this.mContext.mResourceManager.getBitmap(this.mDotbar);
      break label24;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.SpectrumVisualizerScreenElement
 * JD-Core Version:    0.6.0
 */