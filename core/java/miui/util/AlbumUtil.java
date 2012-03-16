package miui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class AlbumUtil
{
  private static final float ASPECT_RADIO_THRESHOLD = 0.1F;
  private final int mAlbumShadowHeight;
  private float mAspectRatio;
  private Context mContext;
  private final Matrix mRotateMatrix;

  public AlbumUtil(Context paramContext, int paramInt)
  {
    this.mContext = paramContext;
    this.mAlbumShadowHeight = paramInt;
    this.mRotateMatrix = new Matrix();
    this.mRotateMatrix.postScale(1.0F, -1.0F);
    this.mAspectRatio = 1.0F;
  }

  private float getAspectRadio(Bitmap paramBitmap)
  {
    return paramBitmap.getWidth() / paramBitmap.getHeight();
  }

  public Bitmap cutToDefauleAspectRadio(Bitmap paramBitmap)
  {
    float f = getAspectRadio(paramBitmap);
    if (Math.abs(f - this.mAspectRatio) < 0.1F)
      return paramBitmap;
    int i = paramBitmap.getHeight();
    int j = paramBitmap.getWidth();
    int m;
    int k;
    if (f > this.mAspectRatio)
    {
      m = i;
      k = (int)(i * this.mAspectRatio);
    }
    while (true)
    {
      Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, k, m, null, true);
      if (localBitmap != paramBitmap)
        paramBitmap.recycle();
      paramBitmap = localBitmap;
      break;
      k = j;
      m = (int)(j / this.mAspectRatio);
    }
  }

  // ERROR //
  public Bitmap getAlbumBitmap(android.net.Uri paramUri)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: getfield 21	miui/util/AlbumUtil:mContext	Landroid/content/Context;
    //   6: invokevirtual 75	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   9: astore_3
    //   10: aload_1
    //   11: ifnull +59 -> 70
    //   14: aconst_null
    //   15: astore 4
    //   17: new 77	android/graphics/BitmapFactory$Options
    //   20: dup
    //   21: invokespecial 78	android/graphics/BitmapFactory$Options:<init>	()V
    //   24: astore 5
    //   26: aload 5
    //   28: getstatic 84	android/graphics/Bitmap$Config:ARGB_8888	Landroid/graphics/Bitmap$Config;
    //   31: putfield 87	android/graphics/BitmapFactory$Options:inPreferredConfig	Landroid/graphics/Bitmap$Config;
    //   34: aload 5
    //   36: iconst_0
    //   37: putfield 91	android/graphics/BitmapFactory$Options:inDither	Z
    //   40: aload_3
    //   41: aload_1
    //   42: invokevirtual 97	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   45: astore 4
    //   47: aload 4
    //   49: aconst_null
    //   50: aload 5
    //   52: invokestatic 103	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   55: astore 13
    //   57: aload 13
    //   59: astore_2
    //   60: aload 4
    //   62: ifnull +8 -> 70
    //   65: aload 4
    //   67: invokevirtual 108	java/io/InputStream:close	()V
    //   70: aload_2
    //   71: areturn
    //   72: astore 8
    //   74: aload_3
    //   75: aload_1
    //   76: ldc 110
    //   78: invokevirtual 114	android/content/ContentResolver:openFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;
    //   81: astore 11
    //   83: aload 11
    //   85: ifnull +12 -> 97
    //   88: aload 11
    //   90: invokevirtual 120	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   93: invokestatic 124	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;)Landroid/graphics/Bitmap;
    //   96: astore_2
    //   97: aload_2
    //   98: ifnull +23 -> 121
    //   101: aload_2
    //   102: invokevirtual 128	android/graphics/Bitmap:getConfig	()Landroid/graphics/Bitmap$Config;
    //   105: ifnonnull +16 -> 121
    //   108: aload_2
    //   109: getstatic 84	android/graphics/Bitmap$Config:ARGB_8888	Landroid/graphics/Bitmap$Config;
    //   112: iconst_0
    //   113: invokevirtual 132	android/graphics/Bitmap:copy	(Landroid/graphics/Bitmap$Config;Z)Landroid/graphics/Bitmap;
    //   116: astore 12
    //   118: aload 12
    //   120: astore_2
    //   121: aload 4
    //   123: ifnull -53 -> 70
    //   126: aload 4
    //   128: invokevirtual 108	java/io/InputStream:close	()V
    //   131: goto -61 -> 70
    //   134: astore 10
    //   136: goto -66 -> 70
    //   139: astore 6
    //   141: aload 4
    //   143: ifnull +8 -> 151
    //   146: aload 4
    //   148: invokevirtual 108	java/io/InputStream:close	()V
    //   151: aload 6
    //   153: athrow
    //   154: astore 14
    //   156: goto -86 -> 70
    //   159: astore 7
    //   161: goto -10 -> 151
    //   164: astore 9
    //   166: goto -45 -> 121
    //
    // Exception table:
    //   from	to	target	type
    //   17	57	72	java/io/FileNotFoundException
    //   126	131	134	java/io/IOException
    //   17	57	139	finally
    //   74	118	139	finally
    //   65	70	154	java/io/IOException
    //   146	151	159	java/io/IOException
    //   74	118	164	java/io/FileNotFoundException
  }

  public Bitmap getAlbumShadow(Bitmap paramBitmap)
  {
    Bitmap localBitmap = null;
    if (paramBitmap == null);
    while (true)
    {
      return localBitmap;
      int i = paramBitmap.getHeight();
      int j = paramBitmap.getWidth();
      if ((i <= 0) || (j <= 0))
        continue;
      int k = this.mAlbumShadowHeight;
      int m = i - this.mAlbumShadowHeight;
      if (m <= 0)
      {
        m = 0;
        k = i;
      }
      localBitmap = setAlpha(Bitmap.createBitmap(paramBitmap, 0, m, j, k, this.mRotateMatrix, true), 50);
    }
  }

  public Bitmap getRoundedCornerBitmap(Bitmap paramBitmap, float paramFloat)
  {
    Bitmap localBitmap;
    if (paramBitmap == null)
      localBitmap = null;
    while (true)
    {
      return localBitmap;
      localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      Paint localPaint = new Paint();
      Rect localRect = new Rect(0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
      RectF localRectF = new RectF(localRect);
      localPaint.setAntiAlias(true);
      localCanvas.drawARGB(0, 0, 0, 0);
      localPaint.setColor(-12434878);
      localCanvas.drawRoundRect(localRectF, paramFloat, paramFloat, localPaint);
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      localCanvas.drawBitmap(paramBitmap, localRect, localRect, localPaint);
    }
  }

  public Bitmap setAlpha(Bitmap paramBitmap, int paramInt)
  {
    return setAlpha(paramBitmap, 0, paramBitmap.getHeight(), paramInt, false);
  }

  public Bitmap setAlpha(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    Bitmap localBitmap;
    if ((paramInt1 >= paramInt2) || (paramInt1 < 0) || (paramInt2 < 0))
      localBitmap = paramBitmap;
    while (true)
    {
      return localBitmap;
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      if (paramInt2 > j)
      {
        localBitmap = paramBitmap;
        continue;
      }
      int[] arrayOfInt = new int[i * j];
      paramBitmap.getPixels(arrayOfInt, 0, i, 0, 0, i, j);
      int k = paramInt2 - paramInt1;
      for (int m = paramInt1; m < paramInt2; m++)
      {
        if (paramBoolean);
        for (int n = paramInt3 * (m - paramInt1) / k; ; n = paramInt3 - paramInt3 * (m - paramInt1) / k)
          for (int i1 = 0; i1 < i; i1++)
            arrayOfInt[(i1 + m * i)] = (n << 24 | 0xFFFFFF & arrayOfInt[(i1 + m * i)]);
      }
      localBitmap = Bitmap.createBitmap(arrayOfInt, i, j, Bitmap.Config.ARGB_8888);
    }
  }

  public void setAspectRadio(float paramFloat)
  {
    this.mAspectRatio = paramFloat;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.AlbumUtil
 * JD-Core Version:    0.6.0
 */