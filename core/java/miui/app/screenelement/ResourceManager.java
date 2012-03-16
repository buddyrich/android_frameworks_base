package miui.app.screenelement;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.MemoryFile;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.Element;

public class ResourceManager
{
  private static final String LOG_TAG = "ResourceManager";
  private final HashMap<String, BitmapInfo> mBitmaps = new HashMap();
  private final HashSet<String> mFailedBitmaps = new HashSet();
  private SoftReference<Bitmap> mMaskBitmap;
  private HashMap<String, SoftReference<Bitmap>> mMaskBitmaps;
  private final HashMap<String, NinePatch> mNinePatches = new HashMap();
  private int mResourceDensity;
  private final ResourceLoader mResourceLoader;
  private int mTargetDensity;

  public ResourceManager(ResourceLoader paramResourceLoader)
  {
    this.mResourceLoader = paramResourceLoader;
  }

  private BitmapInfo getBitmapInfo(String paramString)
  {
    BitmapInfo localBitmapInfo;
    if (TextUtils.isEmpty(paramString))
      localBitmapInfo = null;
    while (true)
    {
      return localBitmapInfo;
      localBitmapInfo = (BitmapInfo)this.mBitmaps.get(paramString);
      if (localBitmapInfo != null)
        continue;
      if (this.mFailedBitmaps.contains(paramString))
      {
        localBitmapInfo = null;
        continue;
      }
      localBitmapInfo = this.mResourceLoader.getBitmapInfo(paramString);
      if (localBitmapInfo != null)
      {
        localBitmapInfo.mBitmap.setDensity(this.mResourceDensity);
        this.mBitmaps.put(paramString, localBitmapInfo);
        continue;
      }
      this.mFailedBitmaps.add(paramString);
      Log.e("ResourceManager", "fail to load image: " + paramString);
    }
  }

  public void clear()
  {
    Iterator localIterator = this.mBitmaps.values().iterator();
    while (localIterator.hasNext())
    {
      BitmapInfo localBitmapInfo = (BitmapInfo)localIterator.next();
      if (localBitmapInfo.mBitmap == null)
        continue;
      localBitmapInfo.mBitmap.recycle();
    }
    this.mBitmaps.clear();
    this.mNinePatches.clear();
  }

  public Bitmap getBitmap(String paramString)
  {
    BitmapInfo localBitmapInfo = getBitmapInfo(paramString);
    if (localBitmapInfo != null);
    for (Bitmap localBitmap = localBitmapInfo.mBitmap; ; localBitmap = null)
      return localBitmap;
  }

  public Drawable getDrawable(String paramString)
  {
    BitmapInfo localBitmapInfo = getBitmapInfo(paramString);
    Object localObject;
    if ((localBitmapInfo == null) || (localBitmapInfo.mBitmap == null))
      localObject = null;
    while (true)
    {
      return localObject;
      Bitmap localBitmap = localBitmapInfo.mBitmap;
      if (localBitmap.getNinePatchChunk() != null)
      {
        localObject = new NinePatchDrawable(localBitmap, localBitmap.getNinePatchChunk(), localBitmapInfo.mPadding, paramString);
        ((NinePatchDrawable)localObject).setTargetDensity(this.mTargetDensity);
        continue;
      }
      BitmapDrawable localBitmapDrawable = new BitmapDrawable(localBitmap);
      localBitmapDrawable.setTargetDensity(this.mTargetDensity);
      localObject = localBitmapDrawable;
    }
  }

  public MemoryFile getFile(String paramString)
  {
    return this.mResourceLoader.getFile(paramString);
  }

  public Element getManifestRoot()
  {
    return this.mResourceLoader.getManifestRoot();
  }

  public Bitmap getMaskBufferBitmap(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    if ((paramBoolean) && (this.mMaskBitmaps == null))
      this.mMaskBitmaps = new HashMap();
    int i = 0;
    SoftReference localSoftReference;
    Bitmap localBitmap;
    if (paramBoolean)
    {
      localSoftReference = (SoftReference)this.mMaskBitmaps.get(paramString);
      if (localSoftReference == null)
        break label148;
      localBitmap = (Bitmap)localSoftReference.get();
      label59: if ((localBitmap == null) || (localBitmap.getHeight() < paramInt2) || (localBitmap.getWidth() < paramInt1))
      {
        localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
        localBitmap.setDensity(this.mResourceDensity);
        localSoftReference = new SoftReference(localBitmap);
        i = 1;
      }
      if (i != 0)
      {
        if (!paramBoolean)
          break label154;
        this.mMaskBitmaps.put(paramString, localSoftReference);
      }
    }
    while (true)
    {
      return localBitmap;
      localSoftReference = this.mMaskBitmap;
      break;
      label148: localBitmap = null;
      break label59;
      label154: this.mMaskBitmap = localSoftReference;
    }
  }

  public NinePatch getNinePatch(String paramString)
  {
    NinePatch localNinePatch = (NinePatch)this.mNinePatches.get(paramString);
    if (localNinePatch == null)
    {
      Bitmap localBitmap = getBitmap(paramString);
      if ((localBitmap != null) && (localBitmap.getNinePatchChunk() != null))
      {
        localNinePatch = new NinePatch(localBitmap, localBitmap.getNinePatchChunk(), null);
        this.mNinePatches.put(paramString, localNinePatch);
      }
    }
    return localNinePatch;
  }

  public void setResourceDensity(int paramInt)
  {
    this.mResourceDensity = paramInt;
  }

  public void setTargetDensity(int paramInt)
  {
    this.mTargetDensity = paramInt;
  }

  public static abstract interface ResourceLoader
  {
    public abstract ResourceManager.BitmapInfo getBitmapInfo(String paramString);

    public abstract MemoryFile getFile(String paramString);

    public abstract Element getManifestRoot();
  }

  public static class BitmapInfo
  {
    public final Bitmap mBitmap;
    public final Rect mPadding;

    public BitmapInfo(Bitmap paramBitmap, Rect paramRect)
    {
      this.mBitmap = paramBitmap;
      this.mPadding = paramRect;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ResourceManager
 * JD-Core Version:    0.6.0
 */