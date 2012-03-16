package miui.os;

import android.content.ContentResolver;
import android.os.Process;
import android.os.StatFs;
import java.io.File;
import miui.provider.ExtraSettings.Secure;

public class Environment extends android.os.Environment
{
  private static final File EXTERNAL_STORAGE_MIUI_DIRECTORY;
  private static final File INTERNAL_STORAGE_DIRECTORY = new File("/data/sdcard");
  private static final File INTERNAL_STORAGE_MIUI_DIRECTORY;
  private static long sTotalMemory;
  private static long sTotalPhysicalMemory;

  static
  {
    EXTERNAL_STORAGE_MIUI_DIRECTORY = new File(getExternalStorageDirectory(), "MIUI");
    INTERNAL_STORAGE_MIUI_DIRECTORY = new File(INTERNAL_STORAGE_DIRECTORY, "MIUI");
  }

  public static File getInternalStorageDirectory()
  {
    return INTERNAL_STORAGE_DIRECTORY;
  }

  public static File getMIUIExternalStorageDirectory()
  {
    return EXTERNAL_STORAGE_MIUI_DIRECTORY;
  }

  public static File getMIUIInternalStorageDirectory()
  {
    return INTERNAL_STORAGE_MIUI_DIRECTORY;
  }

  public static File getMIUIStorageDirectory()
  {
    if (isExternalStorageMounted());
    for (File localFile = EXTERNAL_STORAGE_MIUI_DIRECTORY; ; localFile = INTERNAL_STORAGE_MIUI_DIRECTORY)
      return localFile;
  }

  public static File getStorageDirectory()
  {
    if (isExternalStorageMounted());
    for (File localFile = getExternalStorageDirectory(); ; localFile = INTERNAL_STORAGE_DIRECTORY)
      return localFile;
  }

  public static long getTotalMemory()
  {
    if (sTotalMemory == 0L);
    try
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "MemTotal:";
      long[] arrayOfLong = new long[arrayOfString.length];
      Process.readProcLines("/proc/meminfo", arrayOfString, arrayOfLong);
      sTotalMemory = arrayOfLong[0];
      label36: return sTotalMemory;
    }
    catch (Exception localException)
    {
      break label36;
    }
  }

  public static long getTotalPhysicalMemory()
  {
    if (sTotalPhysicalMemory == 0L)
      sTotalPhysicalMemory = 1024L * (256L * (1L + getTotalMemory() / 262144L));
    return sTotalPhysicalMemory;
  }

  public static void init(File paramFile1, File paramFile2)
  {
    ExtraFileUtils.mkdirs(new File(paramFile1, "customized_icons"), 1023, -1, -1);
    ExtraFileUtils.mkdirs(new File(paramFile2, "sdcard"), 511, -1, -1);
  }

  public static boolean isExternalPath(String paramString)
  {
    if ((paramString != null) && ((paramString.startsWith("/sdcard")) || (paramString.startsWith("/mnt/sdcard"))));
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isExternalStorageMounted()
  {
    return "mounted".equals(getExternalStorageState());
  }

  public static boolean isInternalStorageLow(ContentResolver paramContentResolver)
  {
    int i = 1;
    try
    {
      StatFs localStatFs = new StatFs(INTERNAL_STORAGE_DIRECTORY.getPath());
      long l1 = localStatFs.getAvailableBlocks() * localStatFs.getBlockSize();
      long l2 = ExtraSettings.Secure.getStorageThreshold(paramContentResolver);
      if (l1 < l2);
      while (true)
      {
        label43: return i;
        i = 0;
      }
    }
    catch (Exception localException)
    {
      break label43;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.os.Environment
 * JD-Core Version:    0.6.0
 */