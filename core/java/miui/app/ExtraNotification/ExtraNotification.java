package miui.app;

public class ExtraNotification
{
  public static int[] getLedPwmOffOn(int paramInt)
  {
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = (3 * (paramInt / 4));
    arrayOfInt[1] = (paramInt - arrayOfInt[0]);
    return arrayOfInt;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.ExtraNotification
 * JD-Core Version:    0.6.0
 */