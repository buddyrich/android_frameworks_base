package android.content.res;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import miui.content.res.ThemeResources;

public final class MiuiResources extends Resources
{
  private String mAppPackageName;
  private Set<ThemeResources> mThemeResources = new CopyOnWriteArraySet();

  MiuiResources()
  {
  }

  public MiuiResources(AssetManager paramAssetManager, DisplayMetrics paramDisplayMetrics, Configuration paramConfiguration)
  {
    super(paramAssetManager, paramDisplayMetrics, paramConfiguration);
  }

  public MiuiResources(AssetManager paramAssetManager, DisplayMetrics paramDisplayMetrics, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo)
  {
    super(paramAssetManager, paramDisplayMetrics, paramConfiguration, paramCompatibilityInfo);
  }

  private TypedArray replaceTypedArray(TypedArray paramTypedArray, ThemeResources paramThemeResources)
  {
    if ((paramThemeResources != null) && (paramThemeResources.hasTypedTheme()))
    {
      int[] arrayOfInt = paramTypedArray.mData;
      int i = 0;
      if (i < arrayOfInt.length)
      {
        int j = arrayOfInt[(i + 0)];
        int k = arrayOfInt[(i + 3)];
        if ((j >= 16) && (j <= 31))
        {
          Integer localInteger2 = paramThemeResources.getThemeInt(k);
          if (localInteger2 != null)
            arrayOfInt[(i + 1)] = localInteger2.intValue();
        }
        while (true)
        {
          i += 6;
          break;
          if (j != 5)
            continue;
          Integer localInteger1 = paramThemeResources.getThemeDimension(k);
          if (localInteger1 == null)
            continue;
          arrayOfInt[(i + 1)] = localInteger1.intValue();
        }
      }
    }
    return paramTypedArray;
  }

  public CharSequence getText(int paramInt)
    throws Resources.NotFoundException
  {
    ThemeResources localThemeResources = getThemeResources(paramInt);
    CharSequence localCharSequence;
    if (localThemeResources != null)
    {
      localCharSequence = localThemeResources.getThemeCharSequence(paramInt);
      if (localCharSequence == null);
    }
    while (true)
    {
      return localCharSequence;
      localCharSequence = super.getText(paramInt);
    }
  }

  public CharSequence getText(int paramInt, CharSequence paramCharSequence)
  {
    ThemeResources localThemeResources = getThemeResources(paramInt);
    CharSequence localCharSequence;
    if (localThemeResources != null)
    {
      localCharSequence = localThemeResources.getThemeCharSequence(paramInt);
      if (localCharSequence == null);
    }
    while (true)
    {
      return localCharSequence;
      localCharSequence = super.getText(paramInt, paramCharSequence);
    }
  }

  // ERROR //
  ThemeResources getThemeResources(int paramInt)
  {
    // Byte code:
    //   0: iload_1
    //   1: ifeq +90 -> 91
    //   4: aload_0
    //   5: iload_1
    //   6: invokevirtual 78	android/content/res/MiuiResources:getResourcePackageName	(I)Ljava/lang/String;
    //   9: astore 4
    //   11: ldc 80
    //   13: aload 4
    //   15: invokevirtual 86	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   18: ifeq +10 -> 28
    //   21: invokestatic 90	miui/content/res/ThemeResources:getSystem	()Lmiui/content/res/ThemeResources;
    //   24: astore_2
    //   25: goto +78 -> 103
    //   28: ldc 92
    //   30: aload 4
    //   32: invokevirtual 86	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   35: ifeq +10 -> 45
    //   38: invokestatic 95	miui/content/res/ThemeResources:getMiuiResources	()Lmiui/content/res/ThemeResources;
    //   41: astore_2
    //   42: goto +61 -> 103
    //   45: aload_0
    //   46: getfield 97	android/content/res/MiuiResources:mAppPackageName	Ljava/lang/String;
    //   49: ifnonnull +20 -> 69
    //   52: aload_0
    //   53: monitorenter
    //   54: aload_0
    //   55: getfield 97	android/content/res/MiuiResources:mAppPackageName	Ljava/lang/String;
    //   58: ifnonnull +9 -> 67
    //   61: aload_0
    //   62: aload 4
    //   64: putfield 97	android/content/res/MiuiResources:mAppPackageName	Ljava/lang/String;
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_0
    //   70: aload 4
    //   72: invokestatic 100	miui/content/res/ThemeResources:getThemeResources	(Landroid/content/res/Resources;Ljava/lang/String;)Lmiui/content/res/ThemeResources;
    //   75: astore_2
    //   76: aload_0
    //   77: getfield 21	android/content/res/MiuiResources:mThemeResources	Ljava/util/Set;
    //   80: aload_2
    //   81: invokeinterface 105 2 0
    //   86: pop
    //   87: goto +16 -> 103
    //   90: astore_3
    //   91: aconst_null
    //   92: astore_2
    //   93: goto +10 -> 103
    //   96: astore 6
    //   98: aload_0
    //   99: monitorexit
    //   100: aload 6
    //   102: athrow
    //   103: aload_2
    //   104: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   4	54	90	android/content/res/Resources$NotFoundException
    //   69	87	90	android/content/res/Resources$NotFoundException
    //   100	103	90	android/content/res/Resources$NotFoundException
    //   54	69	96	finally
    //   98	100	96	finally
  }

  public void getValue(int paramInt, TypedValue paramTypedValue, boolean paramBoolean)
    throws Resources.NotFoundException
  {
    super.getValue(paramInt, paramTypedValue, paramBoolean);
    ThemeResources localThemeResources = getThemeResources(paramInt);
    if ((localThemeResources != null) && (paramTypedValue.type >= 16) && (paramTypedValue.type <= 31))
    {
      Integer localInteger = localThemeResources.getThemeInt(paramInt);
      if (localInteger != null)
        paramTypedValue.data = localInteger.intValue();
    }
  }

  Drawable loadOverlayDrawable(TypedValue paramTypedValue, int paramInt)
  {
    ThemeResources localThemeResources = getThemeResources(paramInt);
    Drawable localDrawable;
    String str;
    InputStream localInputStream;
    if (localThemeResources != null)
    {
      localDrawable = null;
      str = paramTypedValue.string.toString();
      localInputStream = localThemeResources.getThemeFileStream(paramTypedValue.assetCookie, str, this.mAppPackageName);
      if (localInputStream == null);
    }
    try
    {
      localDrawable = Drawable.createFromResourceStream(this, paramTypedValue, localInputStream, str, null);
      localInputStream.close();
      while (true)
      {
        label60: return localDrawable;
        localDrawable = super.loadOverlayDrawable(paramTypedValue, paramInt);
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      break label60;
    }
    catch (Exception localException)
    {
      break label60;
    }
  }

  public final Resources.Theme newTheme()
  {
    return new MIUITheme();
  }

  public TypedArray obtainAttributes(AttributeSet paramAttributeSet, int[] paramArrayOfInt)
  {
    ThemeResources localThemeResources = getThemeResources(paramArrayOfInt[0]);
    return replaceTypedArray(super.obtainAttributes(paramAttributeSet, paramArrayOfInt), localThemeResources);
  }

  public TypedArray obtainTypedArray(int paramInt)
    throws Resources.NotFoundException
  {
    ThemeResources localThemeResources = getThemeResources(paramInt);
    return replaceTypedArray(super.obtainTypedArray(paramInt), localThemeResources);
  }

  public InputStream openRawResource(int paramInt, TypedValue paramTypedValue)
    throws Resources.NotFoundException
  {
    ThemeResources localThemeResources = getThemeResources(paramInt);
    InputStream localInputStream;
    if (localThemeResources != null)
    {
      getValue(paramInt, paramTypedValue, true);
      String str = paramTypedValue.string.toString();
      localInputStream = localThemeResources.getThemeFileStream(paramTypedValue.assetCookie, str, this.mAppPackageName);
      if (localInputStream == null);
    }
    while (true)
    {
      return localInputStream;
      localInputStream = super.openRawResource(paramInt, paramTypedValue);
    }
  }

  public void updateConfiguration(Configuration paramConfiguration, DisplayMetrics paramDisplayMetrics, CompatibilityInfo paramCompatibilityInfo)
  {
    super.updateConfiguration(paramConfiguration, paramDisplayMetrics, paramCompatibilityInfo);
    if (this.mThemeResources == null);
    while (true)
    {
      return;
      Iterator localIterator = this.mThemeResources.iterator();
      while (localIterator.hasNext())
        ((ThemeResources)localIterator.next()).reload();
      ThemeResources.getSystem().reload();
    }
  }

  public final class MIUITheme extends Resources.Theme
  {
    public MIUITheme()
    {
      super();
    }

    public TypedArray obtainStyledAttributes(int paramInt, int[] paramArrayOfInt)
      throws Resources.NotFoundException
    {
      ThemeResources localThemeResources = MiuiResources.this.getThemeResources(paramArrayOfInt[0]);
      return MiuiResources.this.replaceTypedArray(super.obtainStyledAttributes(paramInt, paramArrayOfInt), localThemeResources);
    }

    public TypedArray obtainStyledAttributes(AttributeSet paramAttributeSet, int[] paramArrayOfInt, int paramInt1, int paramInt2)
    {
      ThemeResources localThemeResources = MiuiResources.this.getThemeResources(paramArrayOfInt[0]);
      return MiuiResources.this.replaceTypedArray(super.obtainStyledAttributes(paramAttributeSet, paramArrayOfInt, paramInt1, paramInt2), localThemeResources);
    }

    public TypedArray obtainStyledAttributes(int[] paramArrayOfInt)
    {
      ThemeResources localThemeResources = MiuiResources.this.getThemeResources(paramArrayOfInt[0]);
      return MiuiResources.this.replaceTypedArray(super.obtainStyledAttributes(paramArrayOfInt), localThemeResources);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.content.res.MiuiResources
 * JD-Core Version:    0.6.0
 */