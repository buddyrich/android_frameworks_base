package miui.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ArrayAdapter;

public abstract class AsyncImageAdapter<T> extends AsyncAdapter<T>
{
  public AsyncImageAdapter()
  {
  }

  public AsyncImageAdapter(ArrayAdapter<T> paramArrayAdapter)
  {
    super(paramArrayAdapter);
  }

  public class AsyncLoadImageTask extends AsyncAdapter.AsyncLoadPartialDataTask
  {
    private boolean mScaled;
    private int mTargetHeight;
    private int mTargetWidth;

    public AsyncLoadImageTask()
    {
      super();
    }

    protected Object doJob(Object paramObject)
    {
      Object localObject;
      if ((this.mTargetWidth <= 0) || (this.mTargetHeight <= 0))
        localObject = null;
      while (true)
      {
        return localObject;
        localObject = null;
        BitmapFactory.Options localOptions1 = new BitmapFactory.Options();
        localOptions1.inJustDecodeBounds = true;
        BitmapFactory.decodeFile((String)paramObject, localOptions1);
        int i = localOptions1.outWidth;
        int j = localOptions1.outHeight;
        BitmapFactory.Options localOptions2 = new BitmapFactory.Options();
        Bitmap localBitmap1 = null;
        try
        {
          if (!this.mScaled)
          {
            int n = Math.min(i, this.mTargetWidth);
            int i1 = Math.min(j, this.mTargetHeight);
            int i2 = (i - n) / 2;
            int i3 = (j - i1) / 2;
            localBitmap1 = BitmapFactory.decodeFile((String)paramObject, localOptions2);
            if (localBitmap1 != null)
            {
              Bitmap localBitmap3 = Bitmap.createBitmap(localBitmap1, i2, i3, n, i1);
              localObject = localBitmap3;
            }
          }
          while ((localObject != localBitmap1) && (localBitmap1 != null))
          {
            localBitmap1.recycle();
            break;
            float f = Math.min(Math.min(1.0F, 1.0F * this.mTargetWidth / i), 1.0F * this.mTargetHeight / j);
            int k = (int)(f * i);
            int m = (int)(f * j);
            localOptions2.inSampleSize = (int)Math.pow(2.0D, (int)(Math.log(1.0F / f) / Math.log(2.0D)));
            localBitmap1 = BitmapFactory.decodeFile((String)paramObject, localOptions2);
            if (localBitmap1 == null)
              continue;
            Bitmap localBitmap2 = Bitmap.createScaledBitmap(localBitmap1, k, m, false);
            localObject = localBitmap2;
          }
        }
        catch (Exception localException)
        {
          while (true)
            localException.printStackTrace();
        }
        catch (OutOfMemoryError localOutOfMemoryError)
        {
          while (true)
            localOutOfMemoryError.printStackTrace();
        }
      }
    }

    public void setScaled(boolean paramBoolean)
    {
      this.mScaled = paramBoolean;
    }

    public void setTargetSize(int paramInt1, int paramInt2)
    {
      this.mTargetWidth = paramInt1;
      this.mTargetHeight = paramInt2;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.AsyncImageAdapter
 * JD-Core Version:    0.6.0
 */