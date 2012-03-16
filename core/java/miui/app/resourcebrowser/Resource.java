package miui.app.resourcebrowser;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class Resource
{
  private String mId;
  private Bundle mInformation;
  private String mLocalPath;
  private List<String> mLocalPreviews = new ArrayList();
  private List<String> mLocalThumbnails = new ArrayList();
  private long mModifiedTime;
  private String mOnlinePath;
  private List<String> mOnlinePreviews = new ArrayList();
  private List<String> mOnlineThumbnails = new ArrayList();
  private int mPlatformVersion;
  private int mStatus;
  private int mVersion;

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Resource));
    Resource localResource;
    for (boolean bool = false; ; bool = this.mLocalPath.equals(localResource.getLocalPath()))
    {
      return bool;
      localResource = (Resource)paramObject;
    }
  }

  public String getId()
  {
    return this.mId;
  }

  public Bundle getInformation()
  {
    return this.mInformation;
  }

  public String getLocalPath()
  {
    return this.mLocalPath;
  }

  public String getLocalPreview(int paramInt)
  {
    if (paramInt >= this.mLocalPreviews.size());
    for (String str = null; ; str = (String)this.mLocalPreviews.get(paramInt))
      return str;
  }

  public List<String> getLocalPreviews()
  {
    return this.mLocalPreviews;
  }

  public String getLocalThumbnail(int paramInt)
  {
    if (paramInt >= this.mLocalThumbnails.size());
    for (String str = null; ; str = (String)this.mLocalThumbnails.get(paramInt))
      return str;
  }

  public List<String> getLocalThumbnails()
  {
    return this.mLocalThumbnails;
  }

  public long getModifiedTime()
  {
    return this.mModifiedTime;
  }

  public String getOnlinePath()
  {
    return this.mOnlinePath;
  }

  public String getOnlinePreview(int paramInt)
  {
    if (paramInt >= this.mOnlinePreviews.size());
    for (String str = null; ; str = (String)this.mOnlinePreviews.get(paramInt))
      return str;
  }

  public List<String> getOnlinePreviews()
  {
    return this.mOnlinePreviews;
  }

  public String getOnlineThumbnail(int paramInt)
  {
    if (paramInt >= this.mOnlineThumbnails.size());
    for (String str = null; ; str = (String)this.mOnlineThumbnails.get(paramInt))
      return str;
  }

  public List<String> getOnlineThumbnails()
  {
    return this.mOnlineThumbnails;
  }

  public int getPlatformVersion()
  {
    return this.mPlatformVersion;
  }

  public int getStatus()
  {
    return this.mStatus;
  }

  public int getVersion()
  {
    return this.mVersion;
  }

  public int hashCode()
  {
    return this.mLocalPath.hashCode();
  }

  public void setId(String paramString)
  {
    this.mId = paramString;
  }

  public void setInformation(Bundle paramBundle)
  {
    this.mInformation = paramBundle;
    ArrayList localArrayList1 = paramBundle.getStringArrayList("local_thumbnail");
    if (localArrayList1 != null)
      this.mLocalThumbnails = localArrayList1;
    ArrayList localArrayList2 = paramBundle.getStringArrayList("local_preview");
    if (localArrayList2 != null)
      this.mLocalPreviews = localArrayList2;
    ArrayList localArrayList3 = paramBundle.getStringArrayList("online_thumbnail");
    if (localArrayList3 != null)
      this.mOnlineThumbnails = localArrayList3;
    ArrayList localArrayList4 = paramBundle.getStringArrayList("online_preview");
    if (localArrayList4 != null)
      this.mOnlinePreviews = localArrayList4;
    this.mLocalPath = paramBundle.getString("local_path");
    this.mOnlinePath = paramBundle.getString("online_path");
    try
    {
      this.mVersion = Integer.parseInt(paramBundle.getString("version"));
      this.mPlatformVersion = paramBundle.getInt("ui_version");
      this.mModifiedTime = paramBundle.getLong("m_lastupdate");
      String str = paramBundle.getString("x_id");
      if (str != null)
        this.mId = str;
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        this.mVersion = 0;
    }
  }

  public void setLocalPath(String paramString)
  {
    this.mLocalPath = paramString;
  }

  public void setModifiedTime(long paramLong)
  {
    this.mModifiedTime = paramLong;
  }

  public void setOnlinePath(String paramString)
  {
    this.mOnlinePath = paramString;
  }

  public void setPlatformVersion(int paramInt)
  {
    this.mPlatformVersion = paramInt;
  }

  public void setStatus(int paramInt)
  {
    this.mStatus = paramInt;
  }

  public void setVersion(int paramInt)
  {
    this.mVersion = paramInt;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.Resource
 * JD-Core Version:    0.6.0
 */