package com.miui.server.wm;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.SurfaceSession;

public class RoundedCornersSurface
{
  private static final int[] CORNER_IDS;
  private static final int[][] MATRIX;
  private static final int NUM_CORNERS = 4;
  private static final int[] ZORDER_OFFSET;
  private final Drawable[] mCorners = new Drawable[4];
  private boolean mDrawNeeded = true;
  private final int[] mHeight = new int[4];
  private int mLastDH;
  private int mLastDW;
  private final Surface[] mSurfaces = new Surface[4];
  private final int[] mWidth = new int[4];

  static
  {
    int[] arrayOfInt1 = new int[4];
    arrayOfInt1[0] = 50462945;
    arrayOfInt1[1] = 50462946;
    arrayOfInt1[2] = 50462943;
    arrayOfInt1[3] = 50462944;
    CORNER_IDS = arrayOfInt1;
    int[][] arrayOfInt = new int[4][];
    int[] arrayOfInt2 = new int[4];
    arrayOfInt2[0] = 0;
    arrayOfInt2[1] = 0;
    arrayOfInt2[2] = 0;
    arrayOfInt2[3] = 0;
    arrayOfInt[0] = arrayOfInt2;
    int[] arrayOfInt3 = new int[4];
    arrayOfInt3[0] = 1;
    arrayOfInt3[1] = 0;
    arrayOfInt3[2] = -1;
    arrayOfInt3[3] = 0;
    arrayOfInt[1] = arrayOfInt3;
    int[] arrayOfInt4 = new int[4];
    arrayOfInt4[0] = 0;
    arrayOfInt4[1] = 1;
    arrayOfInt4[2] = 0;
    arrayOfInt4[3] = -1;
    arrayOfInt[2] = arrayOfInt4;
    int[] arrayOfInt5 = new int[4];
    arrayOfInt5[0] = 1;
    arrayOfInt5[1] = 1;
    arrayOfInt5[2] = -1;
    arrayOfInt5[3] = -1;
    arrayOfInt[3] = arrayOfInt5;
    MATRIX = arrayOfInt;
    int[] arrayOfInt6 = new int[4];
    arrayOfInt6[0] = -1;
    arrayOfInt6[1] = -1;
    arrayOfInt6[2] = -1;
    arrayOfInt6[3] = -1;
    ZORDER_OFFSET = arrayOfInt6;
  }

  public RoundedCornersSurface(Context paramContext, SurfaceSession paramSurfaceSession, int paramInt1, int paramInt2, int paramInt3)
  {
    Resources localResources = paramContext.getResources();
    int i = 0;
    if (i < 4)
    {
      this.mCorners[i] = localResources.getDrawable(CORNER_IDS[i]);
      if (this.mCorners[i] == null)
        this.mSurfaces[i] = null;
      while (true)
      {
        i++;
        break;
        this.mWidth[i] = this.mCorners[i].getMinimumWidth();
        this.mHeight[i] = this.mCorners[i].getMinimumHeight();
        if (this.mWidth[i] == 0)
        {
          this.mSurfaces[i] = null;
          continue;
        }
        try
        {
          this.mSurfaces[i] = new Surface(paramSurfaceSession, 0, "RoundedCorner", -1, this.mWidth[i], this.mHeight[i], -3, 0);
          this.mSurfaces[i].setLayer(paramInt1 + ZORDER_OFFSET[i]);
          this.mSurfaces[i].show();
        }
        catch (Surface.OutOfResourcesException localOutOfResourcesException)
        {
        }
      }
    }
  }

  public void drawIfNeeded()
  {
    if (!this.mDrawNeeded)
      return;
    this.mDrawNeeded = false;
    int i = 0;
    label15: if (i < 4)
      if (this.mSurfaces[i] != null)
        break label35;
    while (true)
    {
      i++;
      break label15;
      break;
      label35: Rect localRect = new Rect(0, 0, this.mWidth[i], this.mHeight[i]);
      Object localObject = null;
      try
      {
        Canvas localCanvas = this.mSurfaces[i].lockCanvas(localRect);
        localObject = localCanvas;
        label74: if (localObject == null)
          continue;
        this.mCorners[i].setBounds(localRect);
        this.mCorners[i].draw(localObject);
        this.mSurfaces[i].unlockCanvasAndPost(localObject);
      }
      catch (Surface.OutOfResourcesException localOutOfResourcesException)
      {
        break label74;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        break label74;
      }
    }
  }

  public void positionSurface(int paramInt1, int paramInt2)
  {
    if ((this.mLastDW == paramInt1) && (this.mLastDH == paramInt2))
      return;
    this.mLastDW = paramInt1;
    this.mLastDH = paramInt2;
    int i = 0;
    label29: if (i < 4)
      if (this.mSurfaces[i] != null)
        break label49;
    while (true)
    {
      i++;
      break label29;
      break;
      label49: this.mSurfaces[i].setPosition(paramInt1 * MATRIX[i][0] + this.mWidth[i] * MATRIX[i][2], paramInt2 * MATRIX[i][1] + this.mHeight[i] * MATRIX[i][3]);
    }
  }
}

/* Location:           /home/dhacker29/miui/framework_dex2jar.jar
 * Qualified Name:     com.miui.server.wm.RoundedCornersSurface
 * JD-Core Version:    0.6.0
 */