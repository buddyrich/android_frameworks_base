package miui.os;

import android.os.Build.VERSION;
import android.os.SystemProperties;
import android.text.TextUtils;

public class Build extends android.os.Build
{
  public static final boolean IS_DEFY;
  public static final boolean IS_DESIRE;
  public static final boolean IS_FAST_GPU_DEVICE;
  public static final boolean IS_FINAL_USER_BUILD;
  public static final boolean IS_GALAXYS2;
  public static final boolean IS_HTC_HD2;
  public static final boolean IS_I9000;
  public static final boolean IS_LOW_MEMORY_DEVICE;
  public static final boolean IS_MILESTONE;
  public static final boolean IS_MIONE;
  public static final boolean IS_MIONE_PLUS_CDMA;
  public static final boolean IS_NEED_UNCOMPRESSED_UCS2_SMS_DEVICE;
  public static final boolean IS_NEXUS_ONE;
  public static final boolean IS_NEXUS_S;
  public static final boolean IS_P990;
  public static final boolean IS_RICH_MEMORY_DEVICE;
  public static final boolean IS_T959;

  static
  {
    boolean bool1 = true;
    IS_DEFY = "jordan".equals(BOARD);
    IS_DESIRE = "bravo".equals(DEVICE);
    IS_GALAXYS2 = "galaxys2".equals(DEVICE);
    boolean bool2;
    boolean bool3;
    label109: boolean bool4;
    label171: boolean bool5;
    label214: boolean bool6;
    if (("htcleo".equals(DEVICE)) || ("leo".equals(DEVICE)))
    {
      bool2 = bool1;
      IS_HTC_HD2 = bool2;
      IS_I9000 = "aries".equals(BOARD);
      IS_MILESTONE = "umts_sholes".equals(DEVICE);
      if ((!"mione".equals(DEVICE)) && (!"mione_plus".equals(DEVICE)))
        break label285;
      bool3 = bool1;
      IS_MIONE = bool3;
      IS_NEXUS_ONE = "passion".equals(DEVICE);
      IS_NEXUS_S = "crespo".equals(DEVICE);
      IS_P990 = "p990".equals(DEVICE);
      IS_T959 = DEVICE.startsWith("vibrant");
      if ((!IS_MIONE) && (!IS_GALAXYS2))
        break label290;
      bool4 = bool1;
      IS_RICH_MEMORY_DEVICE = bool4;
      IS_LOW_MEMORY_DEVICE = IS_MILESTONE;
      if ((!IS_MIONE) && (!IS_I9000) && (!IS_P990) && (!IS_NEXUS_S) && (!IS_GALAXYS2))
        break label295;
      bool5 = bool1;
      IS_FAST_GPU_DEVICE = bool5;
      if ((!IS_MILESTONE) && (!IS_NEXUS_S) && (!IS_I9000) && (!IS_DEFY) && (!IS_GALAXYS2) && (!IS_P990))
        break label301;
      bool6 = bool1;
      label258: IS_NEED_UNCOMPRESSED_UCS2_SMS_DEVICE = bool6;
      if ((!IS_MIONE) || (!isMsm8660()))
        break label307;
    }
    while (true)
    {
      IS_MIONE_PLUS_CDMA = bool1;
      return;
      bool2 = false;
      break;
      label285: bool3 = false;
      break label109;
      label290: bool4 = false;
      break label171;
      label295: bool5 = false;
      break label214;
      label301: bool6 = false;
      break label258;
      label307: bool1 = false;
    }
  }

  public static boolean isDevelopmentVersion()
  {
    if ((!TextUtils.isEmpty(Build.VERSION.INCREMENTAL)) && (Build.VERSION.INCREMENTAL.matches("\\d+.\\d+.\\d+")));
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean isMsm8660()
  {
    String str = SystemProperties.get("ro.soc.name");
    if ((TextUtils.equals(str, "msm8660")) || (TextUtils.equals(str, "unkown")));
    for (int i = 1; ; i = 0)
      return i;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.os.Build
 * JD-Core Version:    0.6.0
 */