package miui.app.screenelement;

import org.w3c.dom.Element;

public class ScreenElementFactory
{
  public ScreenElement createInstance(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    String str = paramElement.getTagName();
    Object localObject;
    if (str.equalsIgnoreCase("Image"))
      localObject = new ImageScreenElement(paramElement, paramScreenContext);
    while (true)
    {
      return localObject;
      if (str.equalsIgnoreCase("Time"))
      {
        localObject = new TimepanelScreenElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("ImageNumber"))
      {
        localObject = new ImageNumberScreenElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("Text"))
      {
        localObject = new TextScreenElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("DateTime"))
      {
        localObject = new DateTimeScreenElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("Wallpaper"))
      {
        localObject = new WallpaperScreenElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("Button"))
      {
        localObject = new ButtonScreenElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("MusicControl"))
      {
        localObject = new MusicControlScreenElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("ElementGroup"))
      {
        localObject = new ElementGroup(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("Var"))
      {
        localObject = new VariableElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("VarArray"))
      {
        localObject = new VariableArrayElement(paramElement, paramScreenContext);
        continue;
      }
      if (str.equalsIgnoreCase("SpectrumVisualizer"))
      {
        localObject = new SpectrumVisualizerScreenElement(paramElement, paramScreenContext);
        continue;
      }
      localObject = null;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ScreenElementFactory
 * JD-Core Version:    0.6.0
 */