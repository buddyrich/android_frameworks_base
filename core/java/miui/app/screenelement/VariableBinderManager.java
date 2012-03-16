package miui.app.screenelement;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VariableBinderManager
  implements ContentProviderBinder.QueryCompleteListener
{
  private static final String LOG_TAG = "VariableBinderManager";
  public static final String TAG_NAME = "VariableBinders";
  private ArrayList<ContentProviderBinder> mContentProviderBinders = new ArrayList();
  private ScreenContext mContext;

  public VariableBinderManager(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    this.mContext = paramScreenContext;
    if (paramElement != null)
      load(paramElement);
  }

  private void load(Element paramElement)
    throws ScreenElementLoadException
  {
    if (paramElement == null)
    {
      Log.e("VariableBinderManager", "node is null");
      throw new ScreenElementLoadException("node is null");
    }
    loadContentProviderBinders(paramElement);
  }

  private void loadContentProviderBinders(Element paramElement)
    throws ScreenElementLoadException
  {
    NodeList localNodeList = paramElement.getElementsByTagName("ContentProviderBinder");
    for (int i = 0; i < localNodeList.getLength(); i++)
      this.mContentProviderBinders.add(new ContentProviderBinder((Element)localNodeList.item(i), this.mContext, this));
  }

  public ContentProviderBinder.Builder addContentProviderBinder(String paramString)
  {
    return addContentProviderBinder(new TextFormatter(paramString));
  }

  public ContentProviderBinder.Builder addContentProviderBinder(String paramString1, String paramString2)
  {
    return addContentProviderBinder(new TextFormatter(paramString1, paramString2));
  }

  public ContentProviderBinder.Builder addContentProviderBinder(TextFormatter paramTextFormatter)
  {
    ContentProviderBinder localContentProviderBinder = new ContentProviderBinder(this.mContext, this);
    localContentProviderBinder.mUriFormatter = paramTextFormatter;
    this.mContentProviderBinders.add(localContentProviderBinder);
    return new ContentProviderBinder.Builder(localContentProviderBinder);
  }

  public void finish()
  {
    Iterator localIterator = this.mContentProviderBinders.iterator();
    while (localIterator.hasNext())
      ((ContentProviderBinder)localIterator.next()).finish();
  }

  public void init()
  {
    Iterator localIterator = this.mContentProviderBinders.iterator();
    while (localIterator.hasNext())
      ((ContentProviderBinder)localIterator.next()).init();
  }

  public void onQueryCompleted(String paramString)
  {
    Iterator localIterator = this.mContentProviderBinders.iterator();
    while (localIterator.hasNext())
    {
      ContentProviderBinder localContentProviderBinder = (ContentProviderBinder)localIterator.next();
      String str = localContentProviderBinder.getDependency();
      if ((TextUtils.isEmpty(str)) || (!str.equals(paramString)))
        continue;
      localContentProviderBinder.startQuery();
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.VariableBinderManager
 * JD-Core Version:    0.6.0
 */