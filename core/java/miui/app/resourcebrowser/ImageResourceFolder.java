package miui.app.resourcebrowser;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import miui.widget.AsyncAdapter;
import miui.widget.AsyncAdapter.AsyncLoadDataTask;

public class ImageResourceFolder extends ResourceFolder
{
  private Resource mEmptyTranslatePaper;
  private BitmapFactory.Options mOptions = new BitmapFactory.Options();

  public ImageResourceFolder(Context paramContext, Bundle paramBundle, String paramString)
  {
    super(paramContext, paramBundle, paramString);
    this.mOptions.inJustDecodeBounds = true;
  }

  private Resource getResource(String paramString)
  {
    File localFile = new File(paramString);
    Resource localResource = new Resource();
    Bundle localBundle = new Bundle();
    localBundle.putString("m_title", localFile.getName());
    localBundle.putString("filesize", ResourceHelper.getFormattedSize(this.mContext, localFile.length()));
    localBundle.putLong("m_lastupdate", localFile.lastModified());
    localBundle.putString("local_path", paramString);
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(paramString);
    localBundle.putStringArrayList("local_preview", localArrayList);
    localBundle.putStringArrayList("local_thumbnail", localArrayList);
    localResource.setInformation(localBundle);
    return localResource;
  }

  protected void addItem(String paramString)
  {
    BitmapFactory.decodeFile(paramString, this.mOptions);
    Map localMap = this.mFileFlags;
    long l;
    if (this.mOptions.outHeight == -1)
      l = 0L;
    while (true)
    {
      localMap.put(paramString, Long.valueOf(l));
      return;
      l = 1L;
    }
  }

  protected Resource buildResource(String paramString)
  {
    Resource localResource;
    if (((Long)this.mFileFlags.get(paramString)).longValue() == 0L)
      localResource = null;
    while (true)
    {
      return localResource;
      localResource = super.buildResource(paramString);
      Bundle localBundle = localResource.getInformation();
      ArrayList localArrayList = new ArrayList(1);
      localArrayList.add(paramString);
      localBundle.putStringArrayList("local_preview", localArrayList);
      localBundle.putStringArrayList("local_thumbnail", localArrayList);
      localResource.setInformation(localBundle);
    }
  }

  public void enableTransparentWallpaper(boolean paramBoolean)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = ResourceHelper.getFormattedDirectoryPath(this.mFolderPath);
    arrayOfObject[1] = this.mContext.getString(51118085);
    String str = String.format("%s%s.png", arrayOfObject);
    File localFile = new File(str);
    if (!localFile.exists())
      ResourceHelper.writeTo(this.mContext.getResources().openRawResource(50659328), str);
    if ((localFile.exists()) && (paramBoolean));
    for (this.mEmptyTranslatePaper = getResource(str); ; this.mEmptyTranslatePaper = null)
      return;
  }

  public void loadData(AsyncAdapter<Resource>.AsyncLoadDataTask paramAsyncAdapter)
  {
    if (this.mEmptyTranslatePaper != null)
    {
      Resource[] arrayOfResource = new Resource[1];
      arrayOfResource[0] = this.mEmptyTranslatePaper;
      paramAsyncAdapter.onLoadData(arrayOfResource);
    }
    super.loadData(paramAsyncAdapter);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ImageResourceFolder
 * JD-Core Version:    0.6.0
 */