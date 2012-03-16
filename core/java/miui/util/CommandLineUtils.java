package miui.util;

import android.text.TextUtils;
import java.io.InputStream;

public class CommandLineUtils
{
  public static String addQuoteMark(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (paramString.charAt(0) != '"') && (!paramString.contains("*")))
      paramString = "\"" + paramString + "\"";
    return paramString;
  }

  public static boolean chmod(String paramString1, String paramString2, String paramString3)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString2;
    arrayOfObject[1] = addQuoteMark(paramString1);
    return run(paramString3, "busybox chmod %s %s", arrayOfObject);
  }

  public static boolean chown(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = paramString2;
    arrayOfObject[1] = paramString3;
    arrayOfObject[2] = addQuoteMark(paramString1);
    return run(paramString4, "busybox chown %s.%s %s", arrayOfObject);
  }

  public static boolean cp(String paramString1, String paramString2, String paramString3)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = addQuoteMark(paramString1);
    arrayOfObject[1] = addQuoteMark(paramString2);
    return run(paramString3, "busybox cp -rf %s %s", arrayOfObject);
  }

  public static boolean mkdir(String paramString1, String paramString2)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = addQuoteMark(paramString1);
    return run(paramString2, "busybox mkdir -p %s", arrayOfObject);
  }

  public static boolean mv(String paramString1, String paramString2, String paramString3)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = addQuoteMark(paramString1);
    arrayOfObject[1] = addQuoteMark(paramString2);
    return run(paramString3, "busybox mv -f %s %s", arrayOfObject);
  }

  public static boolean rm(String paramString1, String paramString2)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = addQuoteMark(paramString1);
    return run(paramString2, "busybox rm -r %s", arrayOfObject);
  }

  public static boolean run(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    return run(false, paramString1, paramString2, paramArrayOfObject);
  }

  public static boolean run(boolean paramBoolean, String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    String str;
    String[] arrayOfString2;
    if (paramArrayOfObject.length > 0)
    {
      str = String.format(paramString2, paramArrayOfObject);
      if (!TextUtils.isEmpty(paramString1))
        break label60;
      arrayOfString2 = new String[3];
      arrayOfString2[0] = "sh";
      arrayOfString2[1] = "-c";
      arrayOfString2[2] = str;
    }
    label60: String[] arrayOfString1;
    for (boolean bool = runInner(paramBoolean, arrayOfString2); ; bool = runInner(paramBoolean, arrayOfString1))
    {
      return bool;
      str = paramString2;
      break;
      arrayOfString1 = new String[3];
      arrayOfString1[0] = "/system/xbin/invoke-as";
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramString1;
      arrayOfString1[1] = String.format("-u %s", arrayOfObject);
      arrayOfString1[2] = str;
    }
  }

  public static InputStream runAndOutput(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    String str;
    String[] arrayOfString2;
    if (paramArrayOfObject.length > 0)
    {
      str = String.format(paramString2, paramArrayOfObject);
      if (!TextUtils.isEmpty(paramString1))
        break label56;
      arrayOfString2 = new String[3];
      arrayOfString2[0] = "sh";
      arrayOfString2[1] = "-c";
      arrayOfString2[2] = str;
    }
    label56: String[] arrayOfString1;
    for (InputStream localInputStream = runAndOutputInner(arrayOfString2); ; localInputStream = runAndOutputInner(arrayOfString1))
    {
      return localInputStream;
      str = paramString2;
      break;
      arrayOfString1 = new String[3];
      arrayOfString1[0] = "/system/xbin/invoke-as";
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramString1;
      arrayOfString1[1] = String.format("-u %s", arrayOfObject);
      arrayOfString1[2] = str;
    }
  }

  private static InputStream runAndOutputInner(String[] paramArrayOfString)
  {
    InputStream localInputStream = null;
    try
    {
      Process localProcess = Runtime.getRuntime().exec(paramArrayOfString);
      localInputStream = localProcess.getInputStream();
      if (localProcess.waitFor() != 0)
      {
        localInputStream.close();
        localInputStream = null;
      }
      return localInputStream;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  private static boolean runInner(boolean paramBoolean, String[] paramArrayOfString)
  {
    int i = 1;
    try
    {
      Process localProcess = Runtime.getRuntime().exec(paramArrayOfString);
      if (!paramBoolean)
      {
        int j = localProcess.waitFor();
        if (j != 0)
          i = 0;
      }
      return i;
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        i = 0;
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.CommandLineUtils
 * JD-Core Version:    0.6.0
 */