package miui.text.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.miui.internal.R.drawable;
import java.lang.reflect.Field;

public class EmojiSmileys
{
  private static final int EMOJI_CODE_FIRST = 57345;
  private static final int EMOJI_CODE_LAST = 58679;
  private static final int EMOJI_SIZE_PX = 48;
  private static final boolean[] mInitialized = new boolean[1335];
  private static final LazySmileyDrawable[] mSmileyBitmaps = new LazySmileyDrawable[1335];

  private static LazySmileyDrawable getDrawableByResName(String paramString)
  {
    LazySmileyDrawable localLazySmileyDrawable = null;
    try
    {
      int i = R.drawable.class.getField(paramString).getInt(null);
      localLazySmileyDrawable = new LazySmileyDrawable(i);
      label24: return localLazySmileyDrawable;
    }
    catch (SecurityException localSecurityException)
    {
      break label24;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      break label24;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label24;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label24;
    }
  }

  public static Bitmap getEmojiBitmap(int paramInt)
  {
    LazySmileyDrawable localLazySmileyDrawable = (LazySmileyDrawable)getEmojiDrawable(paramInt);
    if (localLazySmileyDrawable == null);
    for (Bitmap localBitmap = null; ; localBitmap = localLazySmileyDrawable.getBitmap())
      return localBitmap;
  }

  public static Drawable getEmojiDrawable(int paramInt)
  {
    int i;
    if ((paramInt >= 57345) && (paramInt <= 58679))
    {
      i = paramInt - 57345;
      if (mInitialized[i] == 0)
      {
        mInitialized[i] = true;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(paramInt);
        String str = String.format("emoji_%x", arrayOfObject);
        mSmileyBitmaps[i] = getDrawableByResName(str);
      }
    }
    for (LazySmileyDrawable localLazySmileyDrawable = mSmileyBitmaps[i]; ; localLazySmileyDrawable = null)
      return localLazySmileyDrawable;
  }

  public static boolean isEmoji(int paramInt)
  {
    if (getEmojiDrawable(paramInt) != null);
    for (int i = 1; ; i = 0)
      return i;
  }

  private static class LazySmileyDrawable extends Drawable
  {
    private Bitmap mBitmap;
    private final Paint mPaint = new Paint();
    private final int mResId;

    public LazySmileyDrawable(int paramInt)
    {
      this.mResId = paramInt;
      setBounds(0, 0, 48, 48);
    }

    public void draw(Canvas paramCanvas)
    {
      paramCanvas.drawBitmap(getBitmap(), null, getBounds(), this.mPaint);
    }

    public Bitmap getBitmap()
    {
      Bitmap localBitmap;
      if (this.mBitmap != null)
        localBitmap = this.mBitmap;
      while (true)
      {
        return localBitmap;
        Drawable localDrawable = Resources.getSystem().getDrawable(this.mResId);
        if ((localDrawable instanceof BitmapDrawable))
        {
          this.mBitmap = ((BitmapDrawable)localDrawable).getBitmap();
          localBitmap = this.mBitmap;
          continue;
        }
        localBitmap = null;
      }
    }

    public int getIntrinsicHeight()
    {
      return 48;
    }

    public int getIntrinsicWidth()
    {
      return 48;
    }

    public int getOpacity()
    {
      if ((getBitmap() == null) || (getBitmap().hasAlpha()) || (this.mPaint.getAlpha() < 255));
      for (int i = -3; ; i = -1)
        return i;
    }

    public void setAlpha(int paramInt)
    {
      this.mPaint.setAlpha(paramInt);
    }

    public void setColorFilter(ColorFilter paramColorFilter)
    {
      this.mPaint.setColorFilter(paramColorFilter);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.text.util.EmojiSmileys
 * JD-Core Version:    0.6.0
 */