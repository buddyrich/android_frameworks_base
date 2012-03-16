package android.content.res;

import android.util.DisplayMetrics;

public class MiuiClassFactory
{
  static Resources newResources()
  {
    return new MiuiResources();
  }

  public static Resources newResources(AssetManager paramAssetManager, DisplayMetrics paramDisplayMetrics, Configuration paramConfiguration)
  {
    return new MiuiResources(paramAssetManager, paramDisplayMetrics, paramConfiguration);
  }

  public static Resources newResources(AssetManager paramAssetManager, DisplayMetrics paramDisplayMetrics, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo)
  {
    return new MiuiResources(paramAssetManager, paramDisplayMetrics, paramConfiguration, paramCompatibilityInfo);
  }

  static TypedArray newTypedArray(Resources paramResources, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    return new MiuiTypedArray(paramResources, paramArrayOfInt1, paramArrayOfInt2, paramInt);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.content.res.MiuiClassFactory
 * JD-Core Version:    0.6.0
 */