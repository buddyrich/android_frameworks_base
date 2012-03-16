package miui.app.resourcebrowser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceSet extends ArrayList<Resource>
{
  private static Map<String, ResourceSet> instances = new HashMap();
  private static final long serialVersionUID = 1L;
  private Map<String, Object> mMetaData = new HashMap();

  /** @deprecated */
  public static ResourceSet getInstance(String paramString)
  {
    monitorenter;
    try
    {
      ResourceSet localResourceSet = (ResourceSet)instances.get(paramString);
      if (localResourceSet == null)
      {
        localResourceSet = new ResourceSet();
        instances.put(paramString, localResourceSet);
      }
      monitorexit;
      return localResourceSet;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public Object getMetaData(String paramString)
  {
    return this.mMetaData.get(paramString);
  }

  public void setAll(List<Resource> paramList)
  {
    clear();
    addAll(paramList);
  }

  public void setMetaData(String paramString, Object paramObject)
  {
    this.mMetaData.put(paramString, paramObject);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceSet
 * JD-Core Version:    0.6.0
 */