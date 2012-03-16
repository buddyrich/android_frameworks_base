package miui.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import java.io.FileOutputStream;
import libcore.io.Streams;

public class ImageUtils
{
  public static int computeSampleSize(InputStreamLoader paramInputStreamLoader, int paramInt)
  {
    BitmapFactory.Options localOptions = getBitmapSize(paramInputStreamLoader);
    double d = Math.sqrt(localOptions.outWidth * localOptions.outHeight / paramInt);
    int i = 1;
    while (i * 2 <= d)
      i <<= 1;
    return i;
  }

  public static final Bitmap getBitmap(InputStreamLoader paramInputStreamLoader, int paramInt)
  {
    BitmapFactory.Options localOptions = getDefaultOptions();
    localOptions.inSampleSize = computeSampleSize(paramInputStreamLoader, paramInt);
    Object localObject1 = null;
    int i = 0;
    while (true)
    {
      int j = i + 1;
      if (i < 3);
      try
      {
        Bitmap localBitmap = BitmapFactory.decodeStream(paramInputStreamLoader.get(), null, localOptions);
        localObject1 = localBitmap;
        return localObject1;
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        System.gc();
        localOptions.inSampleSize = (2 * localOptions.inSampleSize);
        paramInputStreamLoader.close();
        i = j;
        continue;
      }
      catch (Exception localException)
      {
        while (true)
        {
          localException.printStackTrace();
          paramInputStreamLoader.close();
        }
      }
      finally
      {
        paramInputStreamLoader.close();
      }
    }
    throw localObject2;
  }

  // ERROR //
  public static final BitmapFactory.Options getBitmapSize(InputStreamLoader paramInputStreamLoader)
  {
    // Byte code:
    //   0: new 16	android/graphics/BitmapFactory$Options
    //   3: dup
    //   4: invokespecial 68	android/graphics/BitmapFactory$Options:<init>	()V
    //   7: astore_1
    //   8: aload_1
    //   9: iconst_1
    //   10: putfield 72	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   13: aload_0
    //   14: invokevirtual 50	miui/util/InputStreamLoader:get	()Ljava/io/InputStream;
    //   17: aconst_null
    //   18: aload_1
    //   19: invokestatic 56	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   22: pop
    //   23: aload_0
    //   24: invokevirtual 59	miui/util/InputStreamLoader:close	()V
    //   27: aload_1
    //   28: areturn
    //   29: astore_3
    //   30: aload_0
    //   31: invokevirtual 59	miui/util/InputStreamLoader:close	()V
    //   34: goto -7 -> 27
    //   37: astore_2
    //   38: aload_0
    //   39: invokevirtual 59	miui/util/InputStreamLoader:close	()V
    //   42: aload_2
    //   43: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   8	23	29	java/lang/Exception
    //   8	23	37	finally
  }

  public static BitmapFactory.Options getDefaultOptions()
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inDither = false;
    localOptions.inJustDecodeBounds = false;
    localOptions.inSampleSize = 1;
    localOptions.inScaled = false;
    return localOptions;
  }

  public static boolean saveBitmapToLocal(InputStreamLoader paramInputStreamLoader, String paramString, int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInputStreamLoader == null) || (paramString == null) || (paramInt1 < 1) || (paramInt2 < 1))
      bool = false;
    while (true)
    {
      return bool;
      bool = false;
      BitmapFactory.Options localOptions = getBitmapSize(paramInputStreamLoader);
      if ((localOptions.outWidth <= 0) || (localOptions.outHeight <= 0))
        continue;
      if ((localOptions.outWidth == paramInt1) && (localOptions.outHeight == paramInt2))
      {
        bool = saveToFile(paramInputStreamLoader, paramString);
        continue;
      }
      int i = 4 * (paramInt1 * paramInt2);
      try
      {
        Bitmap localBitmap1 = getBitmap(paramInputStreamLoader, i);
        int j = localBitmap1.getWidth();
        int k = localBitmap1.getHeight();
        Matrix localMatrix = new Matrix();
        float f = Math.max(1.0F * paramInt1 / j, 1.0F * paramInt2 / k);
        localMatrix.preScale(f, f);
        localMatrix.postTranslate((paramInt1 - f * j) / 2.0F, (paramInt2 - f * k) / 2.0F);
        Bitmap localBitmap2 = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas();
        localCanvas.setBitmap(localBitmap2);
        localCanvas.drawBitmap(localBitmap1, localMatrix, null);
        FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
        localBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, localFileOutputStream);
        localFileOutputStream.close();
        if (localBitmap2 != localBitmap1)
          localBitmap2.recycle();
        localBitmap1.recycle();
        bool = true;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
      }
    }
  }

  public static boolean saveToFile(InputStreamLoader paramInputStreamLoader, String paramString)
  {
    int i = 0;
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
      Streams.copy(paramInputStreamLoader.get(), localFileOutputStream);
      localFileOutputStream.close();
      paramInputStreamLoader.close();
      i = 1;
      label30: return i;
    }
    catch (Exception localException)
    {
      break label30;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.ImageUtils
 * JD-Core Version:    0.6.0
 */