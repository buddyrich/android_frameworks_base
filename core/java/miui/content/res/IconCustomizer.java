package miui.content.res;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import miui.util.CommandLineUtils;

public class IconCustomizer
{
  private static final int sAlphaShift = 24;
  private static HashMap<String, SoftReference<Bitmap>> sCache;
  private static final Canvas sCanvas;
  private static final int sColorShift = 8;
  private static final int sCustomizedIconHeight = 90;
  private static final int sCustomizedIconWidth = 90;
  private static int sDensity = 0;
  private static Boolean sExcludeAll;
  private static HashSet<String> sExcludes;
  private static final int sIconHeight = 72;
  private static final int sIconWidth = 72;
  private static final Rect sOldBounds;
  private static final String sPathPrefix = "/data/system/customized_icons/";
  private static final int sRGBMask = 16777215;
  private static Resources sSystemResource;

  static
  {
    int i = 1;
    sSystemResource = Resources.getSystem();
    sDensity = sSystemResource.getDisplayMetrics().densityDpi;
    sOldBounds = new Rect();
    sCanvas = new Canvas();
    sCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
    sCache = new HashMap();
    if (SystemProperties.getInt("sys.ui.app-icon-background", i) != i);
    while (true)
    {
      sExcludeAll = Boolean.valueOf(i);
      return;
      int j = 0;
    }
  }

  private static int RGBToColor(int[] paramArrayOfInt)
  {
    return ((paramArrayOfInt[0] << 8) + paramArrayOfInt[1] << 8) + paramArrayOfInt[2];
  }

  public static void clearCache()
  {
    synchronized (sExcludeAll)
    {
      sExcludes = null;
      sCache.clear();
      return;
    }
  }

  public static void clearCustomizedIcons(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      CommandLineUtils.rm("/data/system/customized_icons/*", "root");
      sCache.clear();
    }
    while (true)
    {
      return;
      CommandLineUtils.rm("/data/system/customized_icons/" + paramString + "*", "root");
    }
  }

  private static int[] colorToRGB(int paramInt)
  {
    int[] arrayOfInt = new int[3];
    arrayOfInt[0] = ((0xFF0000 & paramInt) >> 16);
    arrayOfInt[1] = ((0xFF00 & paramInt) >> 8);
    arrayOfInt[2] = (paramInt & 0xFF);
    return arrayOfInt;
  }

  private static Bitmap composeIcon(Bitmap paramBitmap)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int[] arrayOfInt1 = new int[i * j];
    paramBitmap.getPixels(arrayOfInt1, 0, i, 0, 0, i, j);
    paramBitmap.recycle();
    cutEdge(i, j, arrayOfInt1);
    int k = 0;
    int[] arrayOfInt2 = new int[3];
    arrayOfInt2[0] = 0;
    arrayOfInt2[1] = 0;
    arrayOfInt2[2] = 0;
    for (int m = -1 + i * j; m >= 0; m--)
    {
      int i12 = 0xFFFFFF & arrayOfInt1[m];
      if (i12 <= 0)
        continue;
      int[] arrayOfInt7 = colorToRGB(i12);
      arrayOfInt2[0] += arrayOfInt7[0];
      arrayOfInt2[1] += arrayOfInt7[1];
      arrayOfInt2[2] += arrayOfInt7[2];
      k++;
    }
    if (k > 0)
    {
      arrayOfInt2[0] /= k;
      arrayOfInt2[1] /= k;
      arrayOfInt2[2] /= k;
    }
    int n = RGBToColor(arrayOfInt2);
    int i6;
    if (getSaturation(n) < 0.02D)
      i6 = 0;
    Bitmap localBitmap1;
    Canvas localCanvas;
    while (true)
    {
      int[] arrayOfInt5 = colorToRGB(i6);
      localBitmap1 = Bitmap.createBitmap(90, 90, Bitmap.Config.ARGB_8888);
      localCanvas = new Canvas(localBitmap1);
      Bitmap localBitmap2 = getCachedBitmap("icon_background.png");
      if (localBitmap2 == null)
        break;
      int i7 = localBitmap2.getWidth();
      int i8 = localBitmap2.getHeight();
      int[] arrayOfInt6 = new int[i7 * i8];
      localBitmap2.getPixels(arrayOfInt6, 0, i7, 0, 0, i7, i8);
      int i9 = -1 + i7 * i8;
      while (true)
        if (i9 >= 0)
        {
          int i10 = arrayOfInt6[i9];
          arrayOfInt6[i9] = (0xFF000000 & i10 | 0xFF0000 & (0xFF0000 & i10) * arrayOfInt5[0] >>> 8 | 0xFF00 & (0xFF00 & i10) * arrayOfInt5[1] >>> 8 | 0xFF & (i10 & 0xFF) * arrayOfInt5[2] >>> 8);
          i9--;
          continue;
          int[][] arrayOfInt = new int[2][];
          int[] arrayOfInt3 = new int[2];
          arrayOfInt3[0] = 100;
          arrayOfInt3[1] = 110;
          arrayOfInt[0] = arrayOfInt3;
          int[] arrayOfInt4 = new int[2];
          arrayOfInt4[0] = 190;
          arrayOfInt4[1] = 275;
          arrayOfInt[1] = arrayOfInt4;
          int i1 = 0;
          for (int i2 = 0; ; i2++)
          {
            int i3 = arrayOfInt.length;
            if (i2 >= i3)
              break;
            i1 += arrayOfInt[i2][1] - arrayOfInt[i2][0];
          }
          float f = getHue(n) * i1 / 360.0F;
          int i4 = 0;
          while (true)
          {
            int i5 = arrayOfInt.length;
            if (i4 >= i5)
              break;
            int i11 = arrayOfInt[i4][1] - arrayOfInt[i4][0];
            if (f > i11)
            {
              f -= i11;
              i4++;
              continue;
            }
            f += arrayOfInt[i4][0];
          }
          i6 = setSaturation(setValue(setHue(n, f), 0.6F), 0.4F);
          break;
        }
      localCanvas.drawBitmap(arrayOfInt6, 0, i7, 0, 0, i7, i8, true, null);
    }
    Bitmap localBitmap3 = getCachedBitmap("icon_pattern.png");
    if (localBitmap3 != null)
      localCanvas.drawBitmap(localBitmap3, 0.0F, 0.0F, null);
    localCanvas.drawBitmap(arrayOfInt1, 0, i, (90 - i) / 2, (90 - j) / 2, i, j, true, null);
    Bitmap localBitmap4 = getCachedBitmap("icon_border.png");
    if (localBitmap4 != null)
      localCanvas.drawBitmap(localBitmap4, 0.0F, 0.0F, null);
    return localBitmap1;
  }

  private static Bitmap composeShortcutIcon(Bitmap paramBitmap)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int[] arrayOfInt = new int[i * j];
    paramBitmap.getPixels(arrayOfInt, 0, i, 0, 0, i, j);
    paramBitmap.recycle();
    cutEdge(i, j, arrayOfInt);
    Bitmap localBitmap1 = Bitmap.createBitmap(90, 90, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap1);
    Bitmap localBitmap2 = getCachedBitmap("icon_shortcut.png");
    if (localBitmap2 != null)
      localCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, null);
    localCanvas.drawBitmap(arrayOfInt, 0, i, (90 - i) / 2, (90 - j) / 2, i, j, true, null);
    Bitmap localBitmap3 = getCachedBitmap("icon_shortcut_arrow.png");
    if (localBitmap3 != null)
      localCanvas.drawBitmap(localBitmap3, 0.0F, 0.0F, null);
    return localBitmap1;
  }

  private static void cutEdge(int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    Bitmap localBitmap = getCachedBitmap("icon_mask.png");
    if (localBitmap == null);
    while (true)
    {
      return;
      int i = localBitmap.getWidth();
      int j = localBitmap.getHeight();
      if ((i < paramInt1) || (j < paramInt2))
        continue;
      int[] arrayOfInt = new int[i * j];
      localBitmap.getPixels(arrayOfInt, 0, paramInt1, (i - paramInt1) / 2, (j - paramInt2) / 2, paramInt1, paramInt2);
      for (int k = -1 + paramInt1 * paramInt2; k >= 0; k--)
        paramArrayOfInt[k] &= 16777215 + ((paramArrayOfInt[k] >>> 24) * (arrayOfInt[k] >>> 24) / 255 << 24);
    }
  }

  private static Bitmap drawableToBitmap(Drawable paramDrawable)
  {
    Canvas localCanvas1 = sCanvas;
    monitorenter;
    int i = 72;
    int j = 72;
    while (true)
    {
      int k;
      int m;
      float f;
      try
      {
        if (!(paramDrawable instanceof PaintDrawable))
          continue;
        PaintDrawable localPaintDrawable = (PaintDrawable)paramDrawable;
        localPaintDrawable.setIntrinsicWidth(72);
        localPaintDrawable.setIntrinsicHeight(72);
        k = paramDrawable.getIntrinsicWidth();
        m = paramDrawable.getIntrinsicHeight();
        if ((k <= 0) || (k <= 0))
          continue;
        if ((i >= k) && (j >= m))
          break label242;
        f = k / m;
        if (k <= m)
          continue;
        j = (int)(i / f);
        Bitmap localBitmap = Bitmap.createBitmap(72, 72, Bitmap.Config.ARGB_8888);
        Canvas localCanvas2 = sCanvas;
        localCanvas2.setBitmap(localBitmap);
        int n = (72 - i) / 2;
        int i1 = (72 - j) / 2;
        sOldBounds.set(paramDrawable.getBounds());
        paramDrawable.setBounds(n, i1, n + i, i1 + j);
        paramDrawable.draw(localCanvas2);
        paramDrawable.setBounds(sOldBounds);
        return localBitmap;
        if (!(paramDrawable instanceof BitmapDrawable))
          continue;
        BitmapDrawable localBitmapDrawable = (BitmapDrawable)paramDrawable;
        if (localBitmapDrawable.getBitmap().getDensity() != 0)
          continue;
        localBitmapDrawable.setTargetDensity(sSystemResource.getDisplayMetrics());
      }
      finally
      {
        monitorexit;
      }
      i = (int)(f * j);
      continue;
      label242: if ((k >= i) || (m >= j))
        continue;
      i = k;
      j = m;
    }
  }

  public static BitmapDrawable generateIconDrawable(Drawable paramDrawable)
  {
    return scaleBitmap(composeIcon(drawableToBitmap(paramDrawable)));
  }

  public static BitmapDrawable generateShortcutIconDrawable(Drawable paramDrawable)
  {
    return scaleBitmap(composeShortcutIcon(drawableToBitmap(paramDrawable)));
  }

  private static Bitmap getCachedBitmap(String paramString)
  {
    Bitmap localBitmap = null;
    SoftReference localSoftReference = (SoftReference)sCache.get(paramString);
    if (localSoftReference != null)
      localBitmap = (Bitmap)localSoftReference.get();
    if (localBitmap == null)
    {
      localBitmap = ThemeResources.getSystem().getIcon(sSystemResource, paramString);
      sCache.put(paramString, new SoftReference(localBitmap));
    }
    return localBitmap;
  }

  public static BitmapDrawable getCustomizedIconDrawable(String paramString)
  {
    Bitmap localBitmap = ThemeResources.getSystem().getIcon(sSystemResource, paramString);
    if (localBitmap == null)
    {
      String str = "/data/system/customized_icons/" + paramString;
      File localFile = new File(str);
      if (localFile.exists())
      {
        localBitmap = BitmapFactory.decodeFile(str);
        if (localBitmap == null)
          localFile.delete();
      }
    }
    return scaleBitmap(localBitmap);
  }

  public static String getFileName(String paramString1, String paramString2)
  {
    Object[] arrayOfObject = new Object[1];
    if (paramString2 != null);
    while (true)
    {
      arrayOfObject[0] = paramString2;
      return String.format("%s.png", arrayOfObject);
      paramString2 = paramString1;
    }
  }

  private static float getHue(int paramInt)
  {
    int[] arrayOfInt = colorToRGB(paramInt);
    int i = Math.min(arrayOfInt[0], Math.min(arrayOfInt[1], arrayOfInt[2]));
    int j = Math.max(arrayOfInt[0], Math.max(arrayOfInt[1], arrayOfInt[2]));
    int k = j - i;
    float f;
    if (k == 0)
      f = 0.0F;
    while (true)
    {
      return f;
      for (int m = 0; (m < 2) && (i != arrayOfInt[m]); m++);
      f = 120 * ((m + 1) % 3) + 60.0F * (arrayOfInt[((m + 2) % 3)] - i) / k + 60.0F * (j - arrayOfInt[((m + 1) % 3)]) / k;
    }
  }

  private static float getSaturation(int paramInt)
  {
    int[] arrayOfInt = colorToRGB(paramInt);
    int i = Math.min(arrayOfInt[0], Math.min(arrayOfInt[1], arrayOfInt[2]));
    int j = Math.max(arrayOfInt[0], Math.max(arrayOfInt[1], arrayOfInt[2]));
    float f;
    if ((j == 0) || (j == i))
      f = paramInt;
    while (true)
    {
      return f;
      f = 1.0F * (j - i) / j;
    }
  }

  private static float getValue(int paramInt)
  {
    int[] arrayOfInt = colorToRGB(paramInt);
    return 1.0F * Math.max(arrayOfInt[0], Math.max(arrayOfInt[1], arrayOfInt[2])) / 255.0F;
  }

  public static boolean isExclude(String paramString)
  {
    while (true)
    {
      synchronized (sExcludeAll)
      {
        if (sExcludes != null)
          continue;
        sExcludes = new HashSet();
        if (!ThemeResources.getSystem().hasIcon("exclude_list.txt"))
          continue;
        sExcludes.add("com.android.browser");
        sExcludes.add("com.android.calendar");
        sExcludes.add("com.android.camera");
        sExcludes.add("com.android.contacts");
        sExcludes.add("com.android.deskclock");
        sExcludes.add("com.android.email");
        sExcludes.add("com.android.fileexplorer");
        sExcludes.add("com.android.gallery");
        sExcludes.add("com.android.launcher");
        sExcludes.add("com.android.mms");
        sExcludes.add("com.android.monitor");
        sExcludes.add("com.android.music");
        sExcludes.add("com.android.phone");
        sExcludes.add("com.android.providers.contacts");
        sExcludes.add("com.android.providers.downloads.ui");
        sExcludes.add("com.android.providers.telephony");
        sExcludes.add("com.android.quicksearchbox");
        sExcludes.add("com.android.settings");
        sExcludes.add("com.android.soundrecorder");
        sExcludes.add("com.android.spare_parts");
        sExcludes.add("com.android.stk");
        sExcludes.add("com.android.thememanager");
        sExcludes.add("com.android.updater");
        sExcludes.add("com.miui.antispam");
        sExcludes.add("com.miui.backup");
        sExcludes.add("com.miui.bugreport");
        sExcludes.add("com.miui.camera");
        sExcludes.add("com.miui.cit");
        sExcludes.add("com.miui.compass");
        sExcludes.add("com.miui.fmradio");
        sExcludes.add("com.miui.lockv4");
        sExcludes.add("com.miui.notes");
        sExcludes.add("com.miui.player");
        sExcludes.add("com.miui.supermarket");
        sExcludes.add("com.miui.uac");
        sExcludes.add("com.miui.userbook");
        sExcludes.add("com.miui.weather2");
        if (!sExcludeAll.booleanValue())
        {
          if (!sExcludes.contains(paramString))
            break label440;
          break label435;
          return i;
        }
      }
      label435: int i = 1;
      continue;
      label440: i = 0;
    }
  }

  public static void prepareCustomizedIcons(Context paramContext)
  {
    prepareCustomizedIcons(paramContext, null);
  }

  public static void prepareCustomizedIcons(Context paramContext, CustomizedIconsListener paramCustomizedIconsListener)
  {
    Intent localIntent = new Intent("android.intent.action.MAIN", null);
    localIntent.addCategory("android.intent.category.LAUNCHER");
    PackageManager localPackageManager = paramContext.getPackageManager();
    List localList = localPackageManager.queryIntentActivities(localIntent, 0);
    if (paramCustomizedIconsListener != null)
      paramCustomizedIconsListener.beforePrepareIcon(localList.size());
    int i = 0;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      ((ResolveInfo)localIterator.next()).activityInfo.loadIcon(localPackageManager);
      if (paramCustomizedIconsListener == null)
        continue;
      int j = i + 1;
      paramCustomizedIconsListener.finishPrepareIcon(i);
      i = j;
    }
    if (paramCustomizedIconsListener != null)
      paramCustomizedIconsListener.finishAllIcons();
  }

  // ERROR //
  public static void saveCustomizedIconBitmap(String paramString, Bitmap paramBitmap)
  {
    // Byte code:
    //   0: new 132	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 133	java/lang/StringBuilder:<init>	()V
    //   7: ldc 36
    //   9: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   12: aload_0
    //   13: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: invokevirtual 143	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   19: astore 4
    //   21: new 330	java/io/File
    //   24: dup
    //   25: aload 4
    //   27: invokespecial 332	java/io/File:<init>	(Ljava/lang/String;)V
    //   30: astore 5
    //   32: aconst_null
    //   33: astore 6
    //   35: new 536	java/io/FileOutputStream
    //   38: dup
    //   39: aload 5
    //   41: invokespecial 539	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   44: astore 7
    //   46: aload 4
    //   48: sipush 436
    //   51: bipush 255
    //   53: bipush 255
    //   55: invokestatic 545	android/os/FileUtils:setPermissions	(Ljava/lang/String;III)I
    //   58: pop
    //   59: aload 7
    //   61: astore 6
    //   63: aload 6
    //   65: ifnonnull +56 -> 121
    //   68: aload 5
    //   70: invokevirtual 549	java/io/File:getParentFile	()Ljava/io/File;
    //   73: astore 10
    //   75: aload 10
    //   77: invokevirtual 552	java/io/File:mkdirs	()Z
    //   80: pop
    //   81: aload 10
    //   83: invokevirtual 555	java/io/File:getPath	()Ljava/lang/String;
    //   86: sipush 1023
    //   89: bipush 255
    //   91: bipush 255
    //   93: invokestatic 545	android/os/FileUtils:setPermissions	(Ljava/lang/String;III)I
    //   96: pop
    //   97: new 536	java/io/FileOutputStream
    //   100: dup
    //   101: aload 5
    //   103: invokespecial 539	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   106: astore 6
    //   108: aload 4
    //   110: sipush 436
    //   113: bipush 255
    //   115: bipush 255
    //   117: invokestatic 545	android/os/FileUtils:setPermissions	(Ljava/lang/String;III)I
    //   120: pop
    //   121: aload_1
    //   122: getstatic 561	android/graphics/Bitmap$CompressFormat:PNG	Landroid/graphics/Bitmap$CompressFormat;
    //   125: bipush 100
    //   127: aload 6
    //   129: invokevirtual 565	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   132: pop
    //   133: aload 6
    //   135: invokevirtual 568	java/io/FileOutputStream:flush	()V
    //   138: aload 6
    //   140: invokevirtual 571	java/io/FileOutputStream:close	()V
    //   143: return
    //   144: astore_3
    //   145: aload_3
    //   146: invokevirtual 574	java/io/FileNotFoundException:printStackTrace	()V
    //   149: goto -6 -> 143
    //   152: astore_2
    //   153: aload_2
    //   154: invokevirtual 575	java/io/IOException:printStackTrace	()V
    //   157: goto -14 -> 143
    //   160: astore 15
    //   162: goto -99 -> 63
    //   165: astore 8
    //   167: aload 7
    //   169: astore 6
    //   171: goto -108 -> 63
    //
    // Exception table:
    //   from	to	target	type
    //   0	32	144	java/io/FileNotFoundException
    //   68	143	144	java/io/FileNotFoundException
    //   0	32	152	java/io/IOException
    //   35	46	152	java/io/IOException
    //   46	59	152	java/io/IOException
    //   68	143	152	java/io/IOException
    //   35	46	160	java/io/FileNotFoundException
    //   46	59	165	java/io/FileNotFoundException
  }

  private static BitmapDrawable scaleBitmap(Bitmap paramBitmap)
  {
    BitmapDrawable localBitmapDrawable;
    if (paramBitmap != null)
    {
      paramBitmap.setDensity(240);
      localBitmapDrawable = new BitmapDrawable(sSystemResource, paramBitmap);
      localBitmapDrawable.setTargetDensity(240);
    }
    while (true)
    {
      return localBitmapDrawable;
      localBitmapDrawable = null;
    }
  }

  private static int setHue(int paramInt, float paramFloat)
  {
    int[] arrayOfInt = colorToRGB(paramInt);
    int i = Math.min(arrayOfInt[0], Math.min(arrayOfInt[1], arrayOfInt[2]));
    int j = Math.max(arrayOfInt[0], Math.max(arrayOfInt[1], arrayOfInt[2]));
    int k = j - i;
    if (k == 0);
    while (true)
    {
      return paramInt;
      while (paramFloat < 0.0F)
        paramFloat += 360.0F;
      while (paramFloat > 360.0F)
        paramFloat -= 360.0F;
      int m = (int)Math.floor(paramFloat / 120.0F);
      float f = paramFloat - m * 120;
      int n = (m + 2) % 3;
      arrayOfInt[n] = i;
      arrayOfInt[((n + 2) % 3)] = (int)(i + k * Math.min(f, 60.0F) / 60.0F);
      arrayOfInt[((n + 1) % 3)] = (int)(j - k * Math.max(0.0F, f - 60.0F) / 60.0F);
      paramInt = RGBToColor(arrayOfInt);
    }
  }

  private static int setSaturation(int paramInt, float paramFloat)
  {
    int[] arrayOfInt = colorToRGB(paramInt);
    int i = Math.min(arrayOfInt[0], Math.min(arrayOfInt[1], arrayOfInt[2]));
    int j = Math.max(arrayOfInt[0], Math.max(arrayOfInt[1], arrayOfInt[2]));
    if ((j == 0) || (j == i));
    while (true)
    {
      return paramInt;
      float f = 1.0F * (j - i) / j;
      arrayOfInt[0] = (int)(j - paramFloat * (j - arrayOfInt[0]) / f);
      arrayOfInt[1] = (int)(j - paramFloat * (j - arrayOfInt[1]) / f);
      arrayOfInt[2] = (int)(j - paramFloat * (j - arrayOfInt[2]) / f);
      paramInt = RGBToColor(arrayOfInt);
    }
  }

  private static int setValue(int paramInt, float paramFloat)
  {
    int[] arrayOfInt = colorToRGB(paramInt);
    int i = Math.max(arrayOfInt[0], Math.max(arrayOfInt[1], arrayOfInt[2]));
    if (i == 0);
    while (true)
    {
      return paramInt;
      float f = 1.0F * i / 255.0F;
      arrayOfInt[0] = (int)(paramFloat * arrayOfInt[0] / f);
      arrayOfInt[1] = (int)(paramFloat * arrayOfInt[1] / f);
      arrayOfInt[2] = (int)(paramFloat * arrayOfInt[2] / f);
      paramInt = RGBToColor(arrayOfInt);
    }
  }

  public static abstract interface CustomizedIconsListener
  {
    public abstract void beforePrepareIcon(int paramInt);

    public abstract void finishAllIcons();

    public abstract void finishPrepareIcon(int paramInt);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.content.res.IconCustomizer
 * JD-Core Version:    0.6.0
 */