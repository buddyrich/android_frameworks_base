package miui.app.screenelement;

import org.w3c.dom.Element;

public class Task
{
  public static String TAG_ACTION;
  public static String TAG_CATEGORY;
  public static String TAG_CLASS;
  public static String TAG_ID = "id";
  public static String TAG_NAME;
  public static String TAG_PACKAGE;
  public static String TAG_TYPE;
  public String action;
  public String category;
  public String className;
  public String id;
  public String name;
  public String packageName;
  public String type;

  static
  {
    TAG_ACTION = "action";
    TAG_TYPE = "type";
    TAG_CATEGORY = "category";
    TAG_PACKAGE = "package";
    TAG_CLASS = "class";
    TAG_NAME = "name";
  }

  public static Task load(Element paramElement)
  {
    Task localTask;
    if (paramElement == null)
      localTask = null;
    while (true)
    {
      return localTask;
      localTask = new Task();
      localTask.id = paramElement.getAttribute("id");
      localTask.action = paramElement.getAttribute("action");
      localTask.type = paramElement.getAttribute("type");
      localTask.category = paramElement.getAttribute("category");
      localTask.packageName = paramElement.getAttribute("package");
      localTask.className = paramElement.getAttribute("class");
      localTask.name = paramElement.getAttribute("name");
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.Task
 * JD-Core Version:    0.6.0
 */