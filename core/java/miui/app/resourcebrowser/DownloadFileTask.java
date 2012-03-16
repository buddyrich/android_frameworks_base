package miui.app.resourcebrowser;

import android.os.AsyncTask;
import android.os.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadFileTask extends AsyncTask<String, DownloadFileEntry, List<DownloadFileEntry>>
{
  private static final int BUFFER_SIZE = 1024;
  private static final String SUFFIX = ".temp";
  private String mId = Integer.valueOf(super.hashCode()).toString();
  private String mTargetDirectory;

  private DownloadFileEntry handleDownloadFile(String paramString)
  {
    DownloadFileEntry localDownloadFileEntry = new DownloadFileEntry();
    String str = OnlineHelper.getFilePathByURL(this.mTargetDirectory, paramString);
    File localFile1 = new File(str);
    File localFile2 = new File(str + ".temp");
    try
    {
      writeToFile(paramString, localFile2);
      localFile2.renameTo(localFile1);
      localDownloadFileEntry.setPath(str);
      return localDownloadFileEntry;
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        localIOException.printStackTrace();
        localDownloadFileEntry = null;
        if (!localFile2.exists())
          continue;
        localFile2.delete();
      }
    }
  }

  private void writeToFile(String paramString, File paramFile)
    throws IOException
  {
    if (!paramFile.exists())
      paramFile.createNewFile();
    InputStream localInputStream = OnlineHelper.getURLInputStream(paramString);
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    byte[] arrayOfByte = new byte[1024];
    while (true)
    {
      int i = localInputStream.read(arrayOfByte);
      if (i < 0)
        break;
      localFileOutputStream.write(arrayOfByte, 0, i);
    }
    FileUtils.setPermissions(paramFile.getAbsolutePath(), 511, -1, -1);
  }

  protected List<DownloadFileEntry> doInBackground(String[] paramArrayOfString)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      DownloadFileEntry localDownloadFileEntry = handleDownloadFile(paramArrayOfString[i]);
      if (localDownloadFileEntry != null)
      {
        localDownloadFileEntry.setIndex(i);
        localArrayList.add(localDownloadFileEntry);
      }
      DownloadFileEntry[] arrayOfDownloadFileEntry = new DownloadFileEntry[1];
      arrayOfDownloadFileEntry[0] = localDownloadFileEntry;
      publishProgress(arrayOfDownloadFileEntry);
    }
    return localArrayList;
  }

  public List<DownloadFileEntry> doInForeground(String[] paramArrayOfString)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      DownloadFileEntry localDownloadFileEntry = handleDownloadFile(paramArrayOfString[i]);
      if (localDownloadFileEntry == null)
        continue;
      localDownloadFileEntry.setIndex(i);
      localArrayList.add(localDownloadFileEntry);
    }
    return localArrayList;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DownloadFileTask)));
    for (boolean bool = false; ; bool = ((DownloadFileTask)paramObject).getId().equals(getId()))
      return bool;
  }

  public String getId()
  {
    return this.mId;
  }

  public String getTargetDirectory()
  {
    return this.mTargetDirectory;
  }

  public int hashCode()
  {
    return getId().hashCode();
  }

  public void setId(String paramString)
  {
    this.mId = paramString;
  }

  public void setTargetDirectory(String paramString)
  {
    this.mTargetDirectory = paramString;
  }

  public static class DownloadFileEntry
  {
    private int index;
    private String path;

    public int getIndex()
    {
      return this.index;
    }

    public String getPath()
    {
      return this.path;
    }

    public void setIndex(int paramInt)
    {
      this.index = paramInt;
    }

    public void setPath(String paramString)
    {
      this.path = paramString;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.DownloadFileTask
 * JD-Core Version:    0.6.0
 */