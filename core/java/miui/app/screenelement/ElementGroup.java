package miui.app.screenelement;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ElementGroup extends AnimatedScreenElement
{
  private static final String LOG_TAG = "LockScreen_ElementGroup";
  public static final String TAG_NAME = "ElementGroup";
  protected ArrayList<ScreenElement> mElements = new ArrayList();

  public ElementGroup(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    load(paramElement);
  }

  public ScreenElement findElement(String paramString)
  {
    ScreenElement localScreenElement1 = super.findElement(paramString);
    Object localObject;
    if (localScreenElement1 != null)
      localObject = localScreenElement1;
    while (true)
    {
      return localObject;
      Iterator localIterator = this.mElements.iterator();
      while (true)
        if (localIterator.hasNext())
        {
          ScreenElement localScreenElement2 = ((ScreenElement)localIterator.next()).findElement(paramString);
          if (localScreenElement2 == null)
            continue;
          localObject = localScreenElement2;
          break;
        }
      localObject = null;
    }
  }

  public void finish()
  {
    super.finish();
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
      ((ScreenElement)localIterator.next()).finish();
  }

  public void init()
  {
    super.init();
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
      ((ScreenElement)localIterator.next()).init();
  }

  public void load(Element paramElement)
    throws ScreenElementLoadException
  {
    if (paramElement == null)
    {
      Log.e("LockScreen_ElementGroup", "node is null");
      throw new ScreenElementLoadException("node is null");
    }
    ScreenElementFactory localScreenElementFactory = this.mContext.mFactory;
    NodeList localNodeList = paramElement.getChildNodes();
    int i = 0;
    if (i < localNodeList.getLength())
    {
      Element localElement;
      if (localNodeList.item(i).getNodeType() == 1)
      {
        localElement = (Element)localNodeList.item(i);
        ScreenElement localScreenElement = localScreenElementFactory.createInstance(localElement, this.mContext);
        if (localScreenElement == null)
          break label114;
        this.mElements.add(localScreenElement);
      }
      while (true)
      {
        i++;
        break;
        label114: Log.w("LockScreen_ElementGroup", "unrecognized element: " + localElement.getNodeName());
      }
    }
  }

  public void onTouch(MotionEvent paramMotionEvent)
  {
    if (!isVisible());
    while (true)
    {
      return;
      super.onTouch(paramMotionEvent);
      Iterator localIterator = this.mElements.iterator();
      while (localIterator.hasNext())
        ((ScreenElement)localIterator.next()).onTouch(paramMotionEvent);
    }
  }

  public void pause()
  {
    super.pause();
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
      ((ScreenElement)localIterator.next()).pause();
  }

  public void render(Canvas paramCanvas)
  {
    if ((!isVisible()) || (this.mAni.getAlpha() <= 0));
    while (true)
    {
      return;
      float f1 = getX();
      float f2 = getY();
      int i = paramCanvas.save();
      paramCanvas.translate(f1, f2);
      Iterator localIterator = this.mElements.iterator();
      while (localIterator.hasNext())
        ((ScreenElement)localIterator.next()).render(paramCanvas);
      paramCanvas.restoreToCount(i);
    }
  }

  public void resume()
  {
    super.resume();
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
      ((ScreenElement)localIterator.next()).resume();
  }

  public void showCategory(String paramString, boolean paramBoolean)
  {
    super.showCategory(paramString, paramBoolean);
    Iterator localIterator = this.mElements.iterator();
    while (localIterator.hasNext())
      ((ScreenElement)localIterator.next()).showCategory(paramString, paramBoolean);
  }

  public void tick(long paramLong)
  {
    if (!isVisible());
    while (true)
    {
      return;
      super.tick(paramLong);
      Iterator localIterator = this.mElements.iterator();
      while (localIterator.hasNext())
        ((ScreenElement)localIterator.next()).tick(paramLong);
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ElementGroup
 * JD-Core Version:    0.6.0
 */