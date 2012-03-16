package miui.os;

import android.os.FileUtils;
import java.io.File;

public class ExtraFileUtils
{
  public static boolean mkdirs(File paramFile, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    if (paramFile.exists());
    while (true)
    {
      return i;
      String str = paramFile.getParent();
      if (str != null)
        mkdirs(new File(str), paramInt1, paramInt2, paramInt3);
      if (!paramFile.mkdir())
        continue;
      FileUtils.setPermissions(paramFile.getPath(), paramInt1, paramInt2, paramInt3);
      i = 1;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.os.ExtraFileUtils
 * JD-Core Version:    0.6.0
 */