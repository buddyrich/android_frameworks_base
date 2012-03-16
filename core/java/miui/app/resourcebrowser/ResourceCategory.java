package miui.app.resourcebrowser;

public class ResourceCategory
  implements Comparable<ResourceCategory>
{
  private String mCode;
  private String mName;
  private long mType;

  public int compareTo(ResourceCategory paramResourceCategory)
  {
    return this.mCode.compareTo(paramResourceCategory.getCode());
  }

  public String getCode()
  {
    return this.mCode;
  }

  public String getName()
  {
    return this.mName;
  }

  public long getType()
  {
    return this.mType;
  }

  public void setCode(String paramString)
  {
    this.mCode = paramString;
  }

  public void setName(String paramString)
  {
    this.mName = paramString;
  }

  public void setType(long paramLong)
  {
    this.mType = paramLong;
  }

  public String toString()
  {
    return this.mName;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceCategory
 * JD-Core Version:    0.6.0
 */