package miui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioSystem;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import miui.R.styleable;

public class SpectrumVisualizer extends ImageView
{
  private static final int CONSIDER_SAMPLE_LENGTH = 160;
  public static boolean IS_LPA_DECODE = false;
  private static final int RES_DEFAULT_SLIDING_DOT_BAR_ID = 50462982;
  private static final int RES_DEFAULT_SLIDING_PANEL_ID = 50462981;
  private static final int RES_DEFAULT_SLIDING_SHADOW_DOT_BAR_ID = 50462983;
  private static final String TAG = "SpectrumVisualizer";
  private static final int VISUALIZATION_SAMPLE_LENGTH = 256;
  private float INDEX_SCALE_FACTOR;
  private final int MAX_VALID_SAMPLE = 20;
  private float SAMPLE_SCALE_FACTOR;
  private float VISUALIZE_DESC_HEIGHT;
  int mAlphaWidthNum;
  private Bitmap mCachedBitmap;
  private Canvas mCachedCanvas;
  int mCellSize;
  int mDotbarHeight;
  private DotBarDrawer mDrawer;
  private boolean mEnableDrawing;
  private boolean mIsEnableUpdate;
  private boolean mIsNeedCareStreamActive;
  private Visualizer.OnDataCaptureListener mOnDataCaptureListener = new Visualizer.OnDataCaptureListener()
  {
    public void onFftDataCapture(Visualizer paramVisualizer, byte[] paramArrayOfByte, int paramInt)
    {
      SpectrumVisualizer.this.update(paramArrayOfByte);
    }

    public void onWaveFormDataCapture(Visualizer paramVisualizer, byte[] paramArrayOfByte, int paramInt)
    {
    }
  };
  Paint mPaint = new Paint();
  int[] mPixels;
  float[] mPointData;
  private short[] mSampleBuf = new short[' '];
  int mShadowDotbarHeight;
  int[] mShadowPixels;
  private boolean mSoftDrawEnabled = true;
  private int mVisualizationHeight;
  int mVisualizationHeightNum;
  private int mVisualizationWidth;
  int mVisualizationWidthNum;
  private Visualizer mVisualizer;

  public SpectrumVisualizer(Context paramContext)
  {
    super(paramContext);
    init(paramContext, null);
  }

  public SpectrumVisualizer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }

  public SpectrumVisualizer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }

  private void drawInternal(Canvas paramCanvas)
  {
    this.mPaint.setAlpha(255);
    int i = this.mVisualizationWidthNum - this.mAlphaWidthNum;
    for (int j = this.mAlphaWidthNum; j < i; j++)
      this.mDrawer.drawDotBar(paramCanvas, j);
    for (int k = this.mAlphaWidthNum; k > 0; k--)
    {
      this.mPaint.setAlpha(k * 255 / this.mAlphaWidthNum);
      this.mDrawer.drawDotBar(paramCanvas, k - 1);
      this.mDrawer.drawDotBar(paramCanvas, this.mVisualizationWidthNum - k);
    }
  }

  private Bitmap drawToBitmap()
  {
    Bitmap localBitmap = this.mCachedBitmap;
    Canvas localCanvas = this.mCachedCanvas;
    if ((localBitmap != null) && ((localBitmap.getWidth() != getWidth()) || (localBitmap.getHeight() != getHeight())))
    {
      localBitmap.recycle();
      localBitmap = null;
    }
    if (localBitmap == null)
    {
      localBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
      this.mCachedBitmap = localBitmap;
      localCanvas = new Canvas(localBitmap);
      this.mCachedCanvas = localCanvas;
    }
    localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    drawInternal(localCanvas);
    return localBitmap;
  }

  private void init(Context paramContext, AttributeSet paramAttributeSet)
  {
    Drawable localDrawable1 = null;
    Drawable localDrawable2 = null;
    Drawable localDrawable3 = null;
    boolean bool = false;
    this.mEnableDrawing = true;
    this.mIsNeedCareStreamActive = true;
    this.mAlphaWidthNum = 0;
    if (paramAttributeSet != null)
    {
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SpectrumVisualizer);
      localDrawable1 = localTypedArray.getDrawable(3);
      localDrawable2 = localTypedArray.getDrawable(1);
      localDrawable3 = localTypedArray.getDrawable(2);
      bool = localTypedArray.getBoolean(0, false);
      this.mAlphaWidthNum = localTypedArray.getInt(4, this.mAlphaWidthNum);
      this.mIsEnableUpdate = localTypedArray.getBoolean(5, false);
      this.mIsNeedCareStreamActive = localTypedArray.getBoolean(6, false);
      localTypedArray.recycle();
    }
    if (localDrawable1 == null)
      localDrawable1 = paramContext.getResources().getDrawable(50462981);
    Bitmap localBitmap1 = ((BitmapDrawable)localDrawable1).getBitmap();
    if (localDrawable2 == null)
      localDrawable2 = paramContext.getResources().getDrawable(50462982);
    Bitmap localBitmap2 = ((BitmapDrawable)localDrawable2).getBitmap();
    Bitmap localBitmap3 = null;
    if (bool)
    {
      if (localDrawable3 == null)
        localDrawable3 = paramContext.getResources().getDrawable(50462983);
      localBitmap3 = ((BitmapDrawable)localDrawable3).getBitmap();
    }
    setBitmaps(localBitmap1, localBitmap2, localBitmap3);
  }

  public void enableDrawing(boolean paramBoolean)
  {
    this.mEnableDrawing = paramBoolean;
  }

  public void enableUpdate(boolean paramBoolean)
  {
    try
    {
      if (this.mIsEnableUpdate != paramBoolean)
      {
        if ((paramBoolean) && (this.mVisualizer == null))
          if (IS_LPA_DECODE)
            Log.v("SpectrumVisualizer", "lpa decode is on, can't enable");
        while (true)
        {
          this.mIsEnableUpdate = paramBoolean;
          break;
          this.mVisualizer = new Visualizer(0);
          if (this.mVisualizer.getEnabled())
            continue;
          this.mVisualizer.setCaptureSize(512);
          this.mVisualizer.setDataCaptureListener(this.mOnDataCaptureListener, Visualizer.getMaxCaptureRate(), false, true);
          this.mVisualizer.setEnabled(true);
          continue;
          if ((paramBoolean) || (this.mVisualizer == null))
            continue;
          this.mVisualizer.setEnabled(false);
          this.mVisualizer.release();
          this.mVisualizer = null;
        }
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      label139: break label139;
    }
  }

  public int getVisualHeight()
  {
    return this.mVisualizationHeight;
  }

  public int getVisualWidth()
  {
    return this.mVisualizationWidth;
  }

  public boolean isUpdateEnabled()
  {
    return this.mIsEnableUpdate;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (!this.mEnableDrawing);
    while (true)
    {
      return;
      if (this.mSoftDrawEnabled)
      {
        paramCanvas.drawBitmap(drawToBitmap(), 0.0F, 0.0F, null);
        continue;
      }
      drawInternal(paramCanvas);
    }
  }

  public void setBitmaps(Bitmap paramBitmap1, Bitmap paramBitmap2, Bitmap paramBitmap3)
  {
    setImageBitmap(paramBitmap1);
    this.mVisualizationWidth = paramBitmap1.getWidth();
    this.mVisualizationHeight = paramBitmap1.getHeight();
    this.mCellSize = paramBitmap2.getWidth();
    this.mDotbarHeight = paramBitmap2.getHeight();
    if (this.mDotbarHeight > this.mVisualizationHeight)
      this.mDotbarHeight = this.mVisualizationHeight;
    this.mPixels = new int[this.mCellSize * this.mDotbarHeight];
    paramBitmap2.getPixels(this.mPixels, 0, this.mCellSize, 0, 0, this.mCellSize, this.mDotbarHeight);
    this.mVisualizationWidthNum = (this.mVisualizationWidth / this.mCellSize);
    this.mVisualizationHeightNum = (this.mDotbarHeight / this.mCellSize);
    this.SAMPLE_SCALE_FACTOR = (20.0F / this.mVisualizationHeightNum);
    this.INDEX_SCALE_FACTOR = (float)Math.log(this.mVisualizationWidthNum / 3);
    this.VISUALIZE_DESC_HEIGHT = (1.0F / this.mVisualizationHeightNum);
    this.mPointData = new float[this.mVisualizationWidthNum];
    if (this.mAlphaWidthNum == 0)
      this.mAlphaWidthNum = (this.mVisualizationWidthNum / 2);
    this.mShadowPixels = null;
    if (paramBitmap3 != null)
    {
      this.mShadowDotbarHeight = paramBitmap3.getHeight();
      if (this.mShadowDotbarHeight + this.mDotbarHeight > this.mVisualizationHeight)
        this.mShadowDotbarHeight = (this.mVisualizationHeight - this.mDotbarHeight);
      if (this.mShadowDotbarHeight < this.mCellSize)
        this.mDrawer = new AsymmetryDotBar();
    }
    while (true)
    {
      return;
      this.mShadowPixels = new int[this.mCellSize * this.mShadowDotbarHeight];
      paramBitmap3.getPixels(this.mShadowPixels, 0, this.mCellSize, 0, 0, this.mCellSize, this.mShadowDotbarHeight);
      this.mDrawer = new SymmetryDotBar();
      continue;
      this.mDrawer = new AsymmetryDotBar();
    }
  }

  public void setSoftDrawEnabled(boolean paramBoolean)
  {
    this.mSoftDrawEnabled = paramBoolean;
    if ((!paramBoolean) && (this.mCachedBitmap != null))
    {
      this.mCachedBitmap.recycle();
      this.mCachedBitmap = null;
      this.mCachedCanvas = null;
    }
  }

  void update(byte[] paramArrayOfByte)
  {
    if ((this.mIsNeedCareStreamActive) && (!AudioSystem.isStreamActive(3, 0)))
      enableDrawing(false);
    while (true)
    {
      return;
      enableDrawing(true);
      if (paramArrayOfByte == null)
        continue;
      short[] arrayOfShort = this.mSampleBuf;
      int i = arrayOfShort.length;
      int j = 0;
      if (j < i)
      {
        int i2 = paramArrayOfByte[(j * 2)];
        int i3 = paramArrayOfByte[(1 + j * 2)];
        int i4 = (int)Math.sqrt(i2 * i2 + i3 * i3);
        if (i4 < 32767);
        while (true)
        {
          arrayOfShort[j] = (short)i4;
          j++;
          break;
          i4 = 32767;
        }
      }
      int k = 0;
      int m = 0;
      int n = 0;
      if (n < this.mVisualizationWidthNum)
      {
        int i1 = 0;
        while (m < i)
        {
          i1 = Math.max(i1, arrayOfShort[k]);
          k++;
          m += this.mVisualizationWidthNum;
        }
        m -= i;
        float f1;
        label207: float f2;
        if (i1 > 1)
        {
          float f3 = (float)(Math.log(n + 2) / this.INDEX_SCALE_FACTOR);
          f1 = f3 * (f3 * (i1 - 1));
          if (f1 <= 20.0F)
            break label265;
          f2 = this.mVisualizationHeightNum;
        }
        while (true)
        {
          this.mPointData[n] = Math.max(f2 / this.mVisualizationHeightNum, this.mPointData[n] - this.VISUALIZE_DESC_HEIGHT);
          n++;
          break;
          f1 = 0.0F;
          break label207;
          label265: f2 = f1 / this.SAMPLE_SCALE_FACTOR;
        }
      }
      invalidate();
    }
  }

  class SymmetryDotBar
    implements SpectrumVisualizer.DotBarDrawer
  {
    SymmetryDotBar()
    {
    }

    public void drawDotBar(Canvas paramCanvas, int paramInt)
    {
      int i = (int)(0.5D + SpectrumVisualizer.this.mDotbarHeight * (1.0F - SpectrumVisualizer.this.mPointData[paramInt]) / SpectrumVisualizer.this.mCellSize) * SpectrumVisualizer.this.mCellSize;
      if (i < SpectrumVisualizer.this.mDotbarHeight)
        paramCanvas.drawBitmap(SpectrumVisualizer.this.mPixels, i * SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mCellSize, paramInt * SpectrumVisualizer.this.mCellSize, i, SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mDotbarHeight - i, true, SpectrumVisualizer.this.mPaint);
      int j = (int)(0.5D + SpectrumVisualizer.this.mShadowDotbarHeight * SpectrumVisualizer.this.mPointData[paramInt] / SpectrumVisualizer.this.mCellSize) * SpectrumVisualizer.this.mCellSize;
      if (j > SpectrumVisualizer.this.mShadowDotbarHeight)
        j = SpectrumVisualizer.this.mShadowDotbarHeight;
      if (j > 0)
        paramCanvas.drawBitmap(SpectrumVisualizer.this.mShadowPixels, 0, SpectrumVisualizer.this.mCellSize, paramInt * SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mDotbarHeight, SpectrumVisualizer.this.mCellSize, j, true, SpectrumVisualizer.this.mPaint);
    }
  }

  class AsymmetryDotBar
    implements SpectrumVisualizer.DotBarDrawer
  {
    AsymmetryDotBar()
    {
    }

    public void drawDotBar(Canvas paramCanvas, int paramInt)
    {
      int i = (int)(0.5D + SpectrumVisualizer.this.mDotbarHeight * (1.0F - SpectrumVisualizer.this.mPointData[paramInt]) / SpectrumVisualizer.this.mCellSize) * SpectrumVisualizer.this.mCellSize;
      if (i < SpectrumVisualizer.this.mDotbarHeight)
        paramCanvas.drawBitmap(SpectrumVisualizer.this.mPixels, i * SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mCellSize, paramInt * SpectrumVisualizer.this.mCellSize, i, SpectrumVisualizer.this.mCellSize, SpectrumVisualizer.this.mDotbarHeight - i, true, SpectrumVisualizer.this.mPaint);
    }
  }

  private static abstract interface DotBarDrawer
  {
    public abstract void drawDotBar(Canvas paramCanvas, int paramInt);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.SpectrumVisualizer
 * JD-Core Version:    0.6.0
 */