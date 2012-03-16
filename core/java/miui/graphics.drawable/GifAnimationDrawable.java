package miui.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import miui.util.GifDecoder;

public class GifAnimationDrawable extends AnimationDrawable
{
  private int mMaxDecodeSize = 1048576;

  public boolean load(Resources paramResources, InputStream paramInputStream)
  {
    int i = 0;
    if (paramInputStream == null);
    while (true)
    {
      return i;
      GifDecoder localGifDecoder = new GifDecoder();
      localGifDecoder.setMaxDecodeSize(this.mMaxDecodeSize);
      if (localGifDecoder.read(paramInputStream) != 0)
        continue;
      int j = localGifDecoder.getFrameCount();
      if (j > 0)
      {
        for (int k = 0; k < j; k++)
          addFrame(new BitmapDrawable(paramResources, localGifDecoder.getFrame(k)), localGifDecoder.getDelay(k));
        setOneShot(false);
        selectDrawable(0);
      }
      i = 1;
    }
  }

  public boolean load(Resources paramResources, String paramString)
  {
    int i = 0;
    if (paramString == null);
    while (true)
    {
      return i;
      File localFile = new File(paramString);
      if (!localFile.exists())
        continue;
      try
      {
        load(paramResources, new FileInputStream(localFile));
        i = 1;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        localFileNotFoundException.printStackTrace();
      }
    }
  }

  public void setMaxDecodeSize(int paramInt)
  {
    this.mMaxDecodeSize = paramInt;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.graphics.drawable.GifAnimationDrawable
 * JD-Core Version:    0.6.0
 */