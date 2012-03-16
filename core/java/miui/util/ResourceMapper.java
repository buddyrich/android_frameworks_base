package miui.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ResourceMapper
{
  public static int resolveReference(Context paramContext, int paramInt)
  {
    TypedValue localTypedValue = new TypedValue();
    paramContext.getResources().getValue(paramInt, localTypedValue, true);
    if (localTypedValue.resourceId == 0);
    while (true)
    {
      return paramInt;
      paramInt = localTypedValue.resourceId;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.ResourceMapper
 * JD-Core Version:    0.6.0
 */