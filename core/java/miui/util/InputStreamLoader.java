package miui.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class InputStreamLoader
{
  ByteArrayInputStream mByteArrayInputStream;
  private Context mContext;
  private InputStream mInputStream;
  private String mPath;
  private Uri mUri;
  private ZipFile mZipFile;
  private String mZipPath;

  public InputStreamLoader(Context paramContext, Uri paramUri)
  {
    if ("file".equals(paramUri.getScheme()))
      this.mPath = paramUri.getPath();
    while (true)
    {
      return;
      this.mContext = paramContext;
      this.mUri = paramUri;
    }
  }

  public InputStreamLoader(String paramString)
  {
    this.mPath = paramString;
  }

  public InputStreamLoader(String paramString1, String paramString2)
  {
    this.mZipPath = paramString1;
    this.mPath = paramString2;
  }

  public InputStreamLoader(byte[] paramArrayOfByte)
  {
    this.mByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
  }

  public void close()
  {
    try
    {
      if (this.mInputStream != null)
        this.mInputStream.close();
      if (this.mZipFile != null)
        this.mZipFile.close();
      label28: return;
    }
    catch (IOException localIOException)
    {
      break label28;
    }
  }

  public InputStream get()
  {
    close();
    try
    {
      if (this.mUri != null)
        this.mInputStream = this.mContext.getContentResolver().openInputStream(this.mUri);
      while (true)
      {
        label29: if ((this.mInputStream != null) && (!(this.mInputStream instanceof ByteArrayInputStream)))
          this.mInputStream = new BufferedInputStream(this.mInputStream, 16384);
        return this.mInputStream;
        if (this.mZipPath != null)
        {
          this.mZipFile = new ZipFile(this.mZipPath);
          this.mInputStream = this.mZipFile.getInputStream(this.mZipFile.getEntry(this.mPath));
          continue;
        }
        if (this.mPath != null)
        {
          this.mInputStream = new FileInputStream(this.mPath);
          continue;
        }
        if (this.mByteArrayInputStream == null)
          continue;
        this.mByteArrayInputStream.reset();
        this.mInputStream = this.mByteArrayInputStream;
      }
    }
    catch (Exception localException)
    {
      break label29;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.util.InputStreamLoader
 * JD-Core Version:    0.6.0
 */