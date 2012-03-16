package android.content.res;

import miui.content.res.ThemeResources;

public class MiuiTypedArray extends TypedArray
{
  MiuiTypedArray(Resources paramResources, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    super(paramResources, paramArrayOfInt1, paramArrayOfInt2, paramInt);
  }

  private CharSequence loadStringValueAt(int paramInt)
  {
    CharSequence localCharSequence;
    if (this.mData[(paramInt + 0)] == 3)
    {
      int i = this.mData[(paramInt + 3)];
      ThemeResources localThemeResources = ((MiuiResources)getResources()).getThemeResources(i);
      if (localThemeResources != null)
      {
        localCharSequence = localThemeResources.getThemeCharSequence(i);
        if (localCharSequence == null);
      }
    }
    while (true)
    {
      return localCharSequence;
      localCharSequence = null;
    }
  }

  public String getString(int paramInt)
  {
    CharSequence localCharSequence = loadStringValueAt(paramInt * 6);
    if (localCharSequence != null);
    for (String str = localCharSequence.toString(); ; str = super.getString(paramInt))
      return str;
  }

  public CharSequence getText(int paramInt)
  {
    CharSequence localCharSequence = loadStringValueAt(paramInt * 6);
    if (localCharSequence != null);
    while (true)
    {
      return localCharSequence;
      localCharSequence = super.getText(paramInt);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.content.res.MiuiTypedArray
 * JD-Core Version:    0.6.0
 */