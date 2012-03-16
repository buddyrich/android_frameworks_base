package miui.app.screenelement;

import android.text.TextUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils
{
  public static double Dist(Point paramPoint1, Point paramPoint2, boolean paramBoolean)
  {
    double d1 = paramPoint1.x - paramPoint2.x;
    double d2 = paramPoint1.y - paramPoint2.y;
    double d3;
    if (paramBoolean)
      d3 = Math.sqrt(d1 * d1 + d2 * d2);
    while (true)
    {
      return d3;
      d3 = d1 * d1 + d2 * d2;
    }
  }

  public static String addFileNameSuffix(String paramString1, String paramString2)
  {
    int i = paramString1.indexOf('.');
    return paramString1.substring(0, i) + '_' + paramString2 + paramString1.substring(i);
  }

  public static void asserts(boolean paramBoolean)
    throws ScreenElementLoadException
  {
    asserts(paramBoolean, "assert error");
  }

  public static void asserts(boolean paramBoolean, String paramString)
    throws ScreenElementLoadException
  {
    if (!paramBoolean)
      throw new ScreenElementLoadException(paramString);
  }

  public static String doubleToString(double paramDouble)
  {
    String str = String.valueOf(paramDouble);
    if (str.endsWith(".0"))
      str = str.substring(0, -2 + str.length());
    return str;
  }

  public static boolean equals(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 != paramObject2)
      if (paramObject1 != null);
    for (int i = 0; ; i = 1)
    {
      return i;
      if (!paramObject1.equals(paramObject2))
        break;
    }
  }

  public static int getAttrAsInt(Element paramElement, String paramString, int paramInt)
  {
    try
    {
      int i = Integer.parseInt(paramElement.getAttribute(paramString));
      paramInt = i;
      label15: return paramInt;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      break label15;
    }
  }

  public static int getAttrAsIntThrows(Element paramElement, String paramString)
    throws ScreenElementLoadException
  {
    Object[] arrayOfObject;
    try
    {
      int i = Integer.parseInt(paramElement.getAttribute(paramString));
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      arrayOfObject = new Object[2];
      arrayOfObject[0] = paramString;
      arrayOfObject[1] = paramElement.toString();
    }
    throw new ScreenElementLoadException(String.format("fail to get attribute name: %s of Element %s", arrayOfObject));
  }

  public static long getAttrAsLong(Element paramElement, String paramString, long paramLong)
  {
    try
    {
      long l = Long.parseLong(paramElement.getAttribute(paramString));
      paramLong = l;
      label15: return paramLong;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      break label15;
    }
  }

  public static Element getChild(Element paramElement, String paramString)
  {
    Element localElement;
    if (paramElement == null)
      localElement = null;
    while (true)
    {
      return localElement;
      NodeList localNodeList = paramElement.getChildNodes();
      for (int i = 0; ; i++)
      {
        if (i >= localNodeList.getLength())
          break label79;
        Node localNode = localNodeList.item(i);
        if ((localNode.getNodeType() != 1) || (!localNode.getNodeName().equalsIgnoreCase(paramString)))
          continue;
        localElement = (Element)localNode;
        break;
      }
      label79: localElement = null;
    }
  }

  public static Point pointProjectionOnSegment(Point paramPoint1, Point paramPoint2, Point paramPoint3, boolean paramBoolean)
  {
    Point localPoint1 = paramPoint2.minus(paramPoint1);
    Point localPoint2 = paramPoint3.minus(paramPoint1);
    double d = (localPoint1.x * localPoint2.x + localPoint1.y * localPoint2.y) / Dist(paramPoint1, paramPoint2, false);
    Point localPoint3;
    if ((d < 0.0D) || (d > 1.0D))
      if (!paramBoolean)
      {
        paramPoint1 = null;
        localPoint3 = paramPoint1;
      }
    while (true)
    {
      return localPoint3;
      if (d < 0.0D)
        break;
      paramPoint1 = paramPoint2;
      break;
      localPoint3 = localPoint1;
      localPoint3.x = (d * localPoint3.x);
      localPoint3.y = (d * localPoint3.y);
      localPoint3.Offset(paramPoint1);
    }
  }

  public static void traverseXmlElementChildren(Element paramElement, String paramString, XmlTraverseListener paramXmlTraverseListener)
  {
    NodeList localNodeList = paramElement.getChildNodes();
    for (int i = 0; i < localNodeList.getLength(); i++)
    {
      Node localNode = localNodeList.item(i);
      if ((localNode.getNodeType() != 1) || ((paramString != null) && (!TextUtils.equals(localNode.getNodeName(), paramString))))
        continue;
      paramXmlTraverseListener.onChild((Element)localNode);
    }
  }

  public static abstract interface XmlTraverseListener
  {
    public abstract void onChild(Element paramElement);
  }

  public static class Point
  {
    public double x;
    public double y;

    public Point(double paramDouble1, double paramDouble2)
    {
      this.x = paramDouble1;
      this.y = paramDouble2;
    }

    public void Offset(Point paramPoint)
    {
      this.x += paramPoint.x;
      this.y += paramPoint.y;
    }

    Point minus(Point paramPoint)
    {
      return new Point(this.x - paramPoint.x, this.y - paramPoint.y);
    }
  }

  public static class GetChildWrapper
  {
    private Element mEle;

    public GetChildWrapper(Element paramElement)
    {
      this.mEle = paramElement;
    }

    public GetChildWrapper getChild(String paramString)
    {
      return new GetChildWrapper(Utils.getChild(this.mEle, paramString));
    }

    public Element getElement()
    {
      return this.mEle;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.Utils
 * JD-Core Version:    0.6.0
 */