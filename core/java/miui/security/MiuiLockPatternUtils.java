package miui.security;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView.Cell;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MiuiLockPatternUtils extends LockPatternUtils
{
  public static final String LOCK_AC_FILE = "access_control.key";
  private static final String SYSTEM_DIRECTORY = "/system/";
  private static final String TAG = "MiuiLockPatternUtils";
  public static final AtomicBoolean sHaveNonZeroACFile = new AtomicBoolean(false);
  public static String sLockACFilename;

  public MiuiLockPatternUtils(Context paramContext)
  {
    super(paramContext);
    AtomicBoolean localAtomicBoolean;
    if (sLockACFilename == null)
    {
      String str = Environment.getDataDirectory().getAbsolutePath() + "/system/";
      sLockACFilename = str + "access_control.key";
      localAtomicBoolean = sHaveNonZeroACFile;
      if (new File(sLockACFilename).length() <= 0L)
        break label90;
    }
    label90: for (boolean bool = true; ; bool = false)
    {
      localAtomicBoolean.set(bool);
      return;
    }
  }

  public boolean checkAccessControl(List<LockPatternView.Cell> paramList)
  {
    int i = 1;
    try
    {
      RandomAccessFile localRandomAccessFile = new RandomAccessFile(sLockACFilename, "r");
      byte[] arrayOfByte = new byte[(int)localRandomAccessFile.length()];
      int j = localRandomAccessFile.read(arrayOfByte, 0, arrayOfByte.length);
      localRandomAccessFile.close();
      if (j > 0)
      {
        boolean bool = Arrays.equals(arrayOfByte, LockPatternUtils.patternToHash(paramList));
        i = bool;
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
    }
    catch (IOException localIOException)
    {
    }
    return i;
  }

  public void saveACLockPattern(List<LockPatternView.Cell> paramList)
  {
    byte[] arrayOfByte = LockPatternUtils.patternToHash(paramList);
    try
    {
      RandomAccessFile localRandomAccessFile = new RandomAccessFile(sLockACFilename, "rw");
      if (paramList == null)
        localRandomAccessFile.setLength(0L);
      while (true)
      {
        localRandomAccessFile.close();
        break;
        localRandomAccessFile.write(arrayOfByte, 0, arrayOfByte.length);
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      Log.e("MiuiLockPatternUtils", "Unable to save lock pattern to " + sLockACFilename);
    }
    catch (IOException localIOException)
    {
      Log.e("MiuiLockPatternUtils", "Unable to save lock pattern to " + sLockACFilename);
    }
  }

  public boolean savedAccessControlExists()
  {
    return sHaveNonZeroACFile.get();
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.security.MiuiLockPatternUtils
 * JD-Core Version:    0.6.0
 */