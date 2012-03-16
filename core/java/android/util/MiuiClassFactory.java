package android.util;

import android.app.DownloadManager;
import android.app.MiuiDownloadManager;
import android.content.ContentResolver;

public class MiuiClassFactory
{
  public static DownloadManager newDownloadManager(ContentResolver paramContentResolver, String paramString)
  {
    return new MiuiDownloadManager(paramContentResolver, paramString);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     android.util.MiuiClassFactory
 * JD-Core Version:    0.6.0
 */