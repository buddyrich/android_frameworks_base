package miui.app.resourcebrowser;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ZipResourceCache
{
  public String author;
  public String designer;
  public String filePath;
  public long fileSize;
  public long lastModifyTime;
  public HashMap<String, String> nvp = new HashMap();
  public int platformVersion;
  public ArrayList<String> previews = new ArrayList();
  public ArrayList<String> thumbnails = new ArrayList();
  public String title;
  public String version;

  protected boolean imageCached()
  {
    if ((this.thumbnails != null) && (this.previews != null) && (!this.thumbnails.isEmpty()) && (new File((String)this.thumbnails.get(0)).exists()));
    for (int i = 1; ; i = 0)
      return i;
  }

  protected void readBaiscInformation(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.filePath = ((String)paramObjectInputStream.readObject());
    this.fileSize = paramObjectInputStream.readLong();
    this.lastModifyTime = paramObjectInputStream.readLong();
    this.title = ((String)paramObjectInputStream.readObject());
    this.designer = ((String)paramObjectInputStream.readObject());
    this.author = ((String)paramObjectInputStream.readObject());
    this.version = ((String)paramObjectInputStream.readObject());
    this.platformVersion = paramObjectInputStream.readInt();
    this.nvp = ((HashMap)paramObjectInputStream.readObject());
  }

  public final void readCache(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    readBaiscInformation(paramObjectInputStream);
    readImageInformation(paramObjectInputStream);
  }

  protected void readImageInformation(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.thumbnails = new ArrayList();
    for (String str2 : (String[])(String[])paramObjectInputStream.readObject())
      this.thumbnails.add(str2);
    this.previews = new ArrayList();
    for (String str1 : (String[])(String[])paramObjectInputStream.readObject())
      this.previews.add(str1);
  }

  public boolean valid()
  {
    int i = 0;
    if (this.filePath != null)
    {
      File localFile = new File(this.filePath);
      if ((this.lastModifyTime < localFile.lastModified()) || (this.fileSize != localFile.length()) || (!imageCached()))
        break label56;
    }
    label56: for (i = 1; ; i = 0)
      return i;
  }

  protected void writeBaiscInformation(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeObject(this.filePath);
    paramObjectOutputStream.writeLong(this.fileSize);
    paramObjectOutputStream.writeLong(this.lastModifyTime);
    paramObjectOutputStream.writeObject(this.title);
    paramObjectOutputStream.writeObject(this.designer);
    paramObjectOutputStream.writeObject(this.author);
    paramObjectOutputStream.writeObject(this.version);
    paramObjectOutputStream.writeInt(this.platformVersion);
    paramObjectOutputStream.writeObject(this.nvp);
  }

  public final void writeCache(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    writeBaiscInformation(paramObjectOutputStream);
    writeImageInformation(paramObjectOutputStream);
  }

  protected void writeImageInformation(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    String[] arrayOfString1 = new String[this.thumbnails.size()];
    this.thumbnails.toArray(arrayOfString1);
    paramObjectOutputStream.writeObject(arrayOfString1);
    String[] arrayOfString2 = new String[this.previews.size()];
    this.previews.toArray(arrayOfString2);
    paramObjectOutputStream.writeObject(arrayOfString2);
  }

  public static final class DefaultZipCacheImpl extends ZipResourceCache
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      readCache(paramObjectInputStream);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      writeCache(paramObjectOutputStream);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ZipResourceCache
 * JD-Core Version:    0.6.0
 */