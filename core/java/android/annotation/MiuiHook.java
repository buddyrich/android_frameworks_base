package android.annotation;

import java.lang.annotation.Annotation;

public @interface MiuiHook
{
  public abstract MiuiHookType value();

  public static enum MiuiHookType
  {
    static
    {
      CHANGE_BASE_CLASS = new MiuiHookType("CHANGE_BASE_CLASS", 5);
      NEW_CLASS = new MiuiHookType("NEW_CLASS", 6);
      NEW_FIELD = new MiuiHookType("NEW_FIELD", 7);
      NEW_METHOD = new MiuiHookType("NEW_METHOD", 8);
      MiuiHookType[] arrayOfMiuiHookType = new MiuiHookType[9];
      arrayOfMiuiHookType[0] = CHANGE_ACCESS;
      arrayOfMiuiHookType[1] = CHANGE_CODE;
      arrayOfMiuiHookType[2] = CHANGE_CODE_AND_ACCESS;
      arrayOfMiuiHookType[3] = CHANGE_PARAMETER;
      arrayOfMiuiHookType[4] = CHANGE_PARAMETER_AND_ACCESS;
      arrayOfMiuiHookType[5] = CHANGE_BASE_CLASS;
      arrayOfMiuiHookType[6] = NEW_CLASS;
      arrayOfMiuiHookType[7] = NEW_FIELD;
      arrayOfMiuiHookType[8] = NEW_METHOD;
      $VALUES = arrayOfMiuiHookType;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.annotation.MiuiHook
 * JD-Core Version:    0.6.0
 */