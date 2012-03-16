package miui.content.res;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ThemeZipFile
{
  private boolean mExists = true;
  private long mLastModifyTime;
  private String mPath;
  private ZipFile mZipFile;

  public ThemeZipFile(String paramString)
  {
    this(paramString, true);
  }

  public ThemeZipFile(String paramString, boolean paramBoolean)
  {
    this.mPath = paramString;
    if (!paramBoolean)
      openZipFile();
  }

  private void clean()
  {
    if (this.mZipFile != null);
    try
    {
      this.mZipFile.close();
      label14: this.mZipFile = null;
      this.mLastModifyTime = -1L;
      this.mExists = true;
      return;
    }
    catch (Exception localException)
    {
      break label14;
    }
  }

  private void openZipFile()
  {
    clean();
    File localFile = new File(this.mPath);
    if (!localFile.exists())
      this.mExists = false;
    while (true)
    {
      return;
      try
      {
        this.mZipFile = new ZipFile(localFile);
        this.mLastModifyTime = localFile.lastModified();
      }
      catch (Exception localException)
      {
        this.mZipFile = null;
        this.mLastModifyTime = 0L;
      }
    }
  }

  public boolean confirmReset()
  {
    if (new File(this.mPath).lastModified() != this.mLastModifyTime);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean containsEntry(String paramString)
  {
    if ((isValid()) && (this.mZipFile.getEntry(paramString) != null));
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean exists()
  {
    return new File(this.mPath).exists();
  }

  protected void finalize()
    throws Throwable
  {
    super.finalize();
    clean();
  }

  public InputStream getInputStream(String paramString, int[] paramArrayOfInt)
  {
    try
    {
      if (isValid())
      {
        ZipEntry localZipEntry = this.mZipFile.getEntry(paramString);
        if (localZipEntry != null)
        {
          if ((paramArrayOfInt != null) && (paramArrayOfInt.length > 0))
            paramArrayOfInt[0] = (int)localZipEntry.getSize();
          InputStream localInputStream2 = this.mZipFile.getInputStream(localZipEntry);
          localInputStream1 = localInputStream2;
          return localInputStream1;
        }
      }
    }
    catch (Exception localException)
    {
      while (true)
        InputStream localInputStream1 = null;
    }
  }

  public boolean isValid()
  {
    if ((this.mZipFile == null) && (this.mExists))
      openZipFile();
    if (this.mZipFile != null);
    for (int i = 1; ; i = 0)
      return i;
  }

  public void reset()
  {
    if (confirmReset())
      clean();
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.content.res.ThemeZipFile
 * JD-Core Version:    0.6.0
 */