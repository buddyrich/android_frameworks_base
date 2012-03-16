package miui.app.screenelement;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ButtonScreenElement extends AnimatedScreenElement
{
  private static final String LOG_TAG = "ButtonScreenElement";
  public static final String TAG_NAME = "Button";
  private ButtonActionListener mListener;
  private String mListenerName;
  private ElementGroup mNormalElements;
  private AnimatedScreenElement mParent;
  private boolean mPressed;
  private ElementGroup mPressedElements;
  private float mPreviousTapPositionX;
  private float mPreviousTapPositionY;
  private long mPreviousTapUpTime;
  private ArrayList<Trigger> mTriggers = new ArrayList();

  public ButtonScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    load(paramElement);
    if (paramElement != null)
      this.mListenerName = paramElement.getAttribute("listener");
  }

  private ElementGroup getCur()
  {
    if ((this.mPressed) && (this.mPressedElements != null));
    for (ElementGroup localElementGroup = this.mPressedElements; ; localElementGroup = this.mNormalElements)
      return localElementGroup;
  }

  private void performAction(ButtonAction paramButtonAction)
  {
    Iterator localIterator = this.mTriggers.iterator();
    while (localIterator.hasNext())
    {
      Trigger localTrigger = (Trigger)localIterator.next();
      if (localTrigger.getAction() != paramButtonAction)
        continue;
      localTrigger.perform();
    }
    this.mContext.mRoot.onButtonInteractive(this, paramButtonAction);
  }

  public void finish()
  {
    if (this.mNormalElements != null)
      this.mNormalElements.finish();
    if (this.mPressedElements != null)
      this.mPressedElements.finish();
    Iterator localIterator = this.mTriggers.iterator();
    while (localIterator.hasNext())
      ((Trigger)localIterator.next()).finish();
  }

  public void init()
  {
    if (this.mNormalElements != null)
      this.mNormalElements.init();
    if (this.mPressedElements != null)
      this.mPressedElements.init();
    Iterator localIterator = this.mTriggers.iterator();
    while (localIterator.hasNext())
      ((Trigger)localIterator.next()).init();
    ScreenElement localScreenElement;
    if ((this.mListener == null) && (!TextUtils.isEmpty(this.mListenerName)))
      localScreenElement = this.mContext.mRoot.findElement(this.mListenerName);
    try
    {
      this.mListener = ((ButtonActionListener)localScreenElement);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      while (true)
        Log.e("ButtonScreenElement", "button listener designated by the name is not actually a listener: " + this.mListenerName);
    }
  }

  public void load(Element paramElement)
    throws ScreenElementLoadException
  {
    if (paramElement == null)
    {
      Log.e("ButtonScreenElement", "node is null");
      throw new ScreenElementLoadException("node is null");
    }
    Element localElement1 = Utils.getChild(paramElement, "Normal");
    if (localElement1 != null)
      this.mNormalElements = new ElementGroup(localElement1, this.mContext);
    Element localElement2 = Utils.getChild(paramElement, "Pressed");
    if (localElement2 != null)
      this.mPressedElements = new ElementGroup(localElement2, this.mContext);
    Element localElement3 = Utils.getChild(paramElement, "Triggers");
    if (localElement3 != null)
    {
      NodeList localNodeList = localElement3.getChildNodes();
      int i = 0;
      if (i < localNodeList.getLength())
      {
        Element localElement4;
        if (localNodeList.item(i).getNodeType() == 1)
        {
          localElement4 = (Element)localNodeList.item(i);
          if (localElement4.getNodeName().equals("Trigger"))
            break label166;
        }
        while (true)
        {
          i++;
          break;
          label166: this.mTriggers.add(new Trigger(localElement4));
        }
      }
    }
  }

  public void onTouch(MotionEvent paramMotionEvent)
  {
    if (!isVisible());
    while (true)
    {
      return;
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      switch (paramMotionEvent.getActionMasked())
      {
      default:
        break;
      case 0:
        if (!touched(f1, f2))
          continue;
        this.mPressed = true;
        if (this.mListener != null)
          this.mListener.onButtonDown(this.mName);
        performAction(ButtonAction.Down);
        if (SystemClock.uptimeMillis() - this.mPreviousTapUpTime <= ViewConfiguration.getDoubleTapTimeout())
        {
          float f3 = f1 - this.mPreviousTapPositionX;
          float f4 = f2 - this.mPreviousTapPositionY;
          float f5 = f3 * f3 + f4 * f4;
          int i = ViewConfiguration.get(this.mContext.mContext).getScaledDoubleTapSlop();
          if (f5 < i * i)
          {
            if (this.mListener != null)
              this.mListener.onButtonDoubleClick(this.mName);
            performAction(ButtonAction.Double);
          }
        }
        this.mPreviousTapPositionX = f1;
        this.mPreviousTapPositionY = f2;
        break;
      case 2:
        this.mPressed = touched(f1, f2);
        break;
      case 1:
        if (touched(f1, f2))
        {
          if (this.mListener != null)
            this.mListener.onButtonUp(this.mName);
          performAction(ButtonAction.Up);
          this.mPreviousTapUpTime = SystemClock.uptimeMillis();
        }
        this.mPressed = false;
      }
    }
  }

  public void pause()
  {
    if (this.mNormalElements != null)
      this.mNormalElements.pause();
    if (this.mPressedElements != null)
      this.mPressedElements.pause();
    this.mPressed = false;
  }

  public void render(Canvas paramCanvas)
  {
    if (!isVisible());
    while (true)
    {
      return;
      ElementGroup localElementGroup = getCur();
      if (localElementGroup == null)
        continue;
      localElementGroup.render(paramCanvas);
    }
  }

  public void resume()
  {
    if (this.mNormalElements != null)
      this.mNormalElements.resume();
    if (this.mPressedElements != null)
      this.mPressedElements.resume();
  }

  public void setListener(ButtonActionListener paramButtonActionListener)
  {
    this.mListener = paramButtonActionListener;
  }

  public void setParent(AnimatedScreenElement paramAnimatedScreenElement)
  {
    this.mParent = paramAnimatedScreenElement;
  }

  public void showCategory(String paramString, boolean paramBoolean)
  {
    if (this.mNormalElements != null)
      this.mNormalElements.showCategory(paramString, paramBoolean);
    if (this.mPressedElements != null)
      this.mPressedElements.showCategory(paramString, paramBoolean);
  }

  public void tick(long paramLong)
  {
    if (!isVisible());
    while (true)
    {
      return;
      ElementGroup localElementGroup = getCur();
      if (localElementGroup == null)
        continue;
      localElementGroup.tick(paramLong);
    }
  }

  public boolean touched(float paramFloat1, float paramFloat2)
  {
    float f1;
    float f2;
    if (this.mParent != null)
    {
      f1 = this.mParent.getX();
      if (this.mParent == null)
        break label114;
      f2 = this.mParent.getY();
      label31: float f3 = this.mAni.getX();
      float f4 = this.mAni.getY();
      if ((paramFloat1 < f1 + f3) || (paramFloat1 > f1 + f3 + this.mAni.getWidth()) || (paramFloat2 < f2 + f4) || (paramFloat2 > f2 + f4 + this.mAni.getHeight()))
        break label120;
    }
    label114: label120: for (int i = 1; ; i = 0)
    {
      return i;
      f1 = 0.0F;
      break;
      f2 = 0.0F;
      break label31;
    }
  }

  private class Trigger
  {
    private ButtonScreenElement.ButtonAction mAction = ButtonScreenElement.ButtonAction.Invalid;
    private ArrayList<ActionCommand> mCommands = new ArrayList();
    private ButtonScreenElement.Property pro;

    public Trigger(Element arg2)
      throws ScreenElementLoadException
    {
      Element localElement;
      load(localElement);
    }

    private void load(Element paramElement)
      throws ScreenElementLoadException
    {
      if (paramElement != null)
      {
        String str1 = paramElement.getAttribute("target");
        String str2 = paramElement.getAttribute("property");
        String str3 = paramElement.getAttribute("value");
        if ((str2.equals("visibility")) && (!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str3)))
          this.pro = new ButtonScreenElement.VisibilityProperty(ButtonScreenElement.this, str1, str3);
        String str4 = paramElement.getAttribute("action");
        int i;
        label120: Element localElement;
        if (!TextUtils.isEmpty(str4))
        {
          if (str4.equalsIgnoreCase("down"))
            this.mAction = ButtonScreenElement.ButtonAction.Down;
        }
        else
        {
          NodeList localNodeList = paramElement.getChildNodes();
          i = 0;
          if (i >= localNodeList.getLength())
            return;
          if (localNodeList.item(i).getNodeType() == 1)
          {
            localElement = (Element)localNodeList.item(i);
            if (localElement.getNodeName().equals("Command"))
              break label255;
          }
        }
        while (true)
        {
          i++;
          break label120;
          if (str4.equalsIgnoreCase("up"))
          {
            this.mAction = ButtonScreenElement.ButtonAction.Up;
            break;
          }
          if (str4.equalsIgnoreCase("double"))
          {
            this.mAction = ButtonScreenElement.ButtonAction.Double;
            break;
          }
          if (str4.equalsIgnoreCase("long"))
          {
            this.mAction = ButtonScreenElement.ButtonAction.Long;
            break;
          }
          throw new ScreenElementLoadException("invalid trigger action");
          label255: ActionCommand localActionCommand = ActionCommand.create(ButtonScreenElement.this.mContext, localElement);
          if (localActionCommand == null)
            continue;
          this.mCommands.add(localActionCommand);
        }
      }
    }

    public void finish()
    {
      Iterator localIterator = this.mCommands.iterator();
      while (localIterator.hasNext())
        ((ActionCommand)localIterator.next()).finish();
    }

    public ButtonScreenElement.ButtonAction getAction()
    {
      return this.mAction;
    }

    public void init()
    {
      Iterator localIterator = this.mCommands.iterator();
      while (localIterator.hasNext())
        ((ActionCommand)localIterator.next()).init();
    }

    public void perform()
    {
      if (this.pro != null)
        this.pro.perform();
      Iterator localIterator = this.mCommands.iterator();
      while (localIterator.hasNext())
        ((ActionCommand)localIterator.next()).perform();
    }
  }

  public static enum ButtonAction
  {
    static
    {
      Double = new ButtonAction("Double", 2);
      Long = new ButtonAction("Long", 3);
      Invalid = new ButtonAction("Invalid", 4);
      ButtonAction[] arrayOfButtonAction = new ButtonAction[5];
      arrayOfButtonAction[0] = Down;
      arrayOfButtonAction[1] = Up;
      arrayOfButtonAction[2] = Double;
      arrayOfButtonAction[3] = Long;
      arrayOfButtonAction[4] = Invalid;
      $VALUES = arrayOfButtonAction;
    }
  }

  private class VisibilityProperty extends ButtonScreenElement.Property
  {
    private boolean mIsShow;
    private boolean mIsToggle;

    protected VisibilityProperty(String paramString1, String arg3)
    {
      super(paramString1);
      Object localObject;
      if (localObject.equalsIgnoreCase("toggle"))
        this.mIsToggle = true;
      while (true)
      {
        return;
        if (localObject.equalsIgnoreCase("true"))
        {
          this.mIsShow = true;
          continue;
        }
        if (!localObject.equalsIgnoreCase("false"))
          continue;
        this.mIsShow = false;
      }
    }

    public void perform()
    {
      ScreenElement localScreenElement = getTarget();
      boolean bool;
      if (localScreenElement != null)
      {
        if (!this.mIsToggle)
          break label36;
        if (localScreenElement.isVisible())
          break label31;
        bool = true;
        localScreenElement.show(bool);
      }
      while (true)
      {
        return;
        label31: bool = false;
        break;
        label36: localScreenElement.show(this.mIsShow);
      }
    }
  }

  private abstract class Property
  {
    protected String mTarget;
    protected ScreenElement mTargetElement;

    protected Property(String arg2)
    {
      Object localObject;
      this.mTarget = localObject;
    }

    protected ScreenElement getTarget()
    {
      if ((this.mTarget != null) && (this.mTargetElement == null))
      {
        this.mTargetElement = ButtonScreenElement.this.mContext.mRoot.findElement(this.mTarget);
        if (this.mTargetElement == null)
        {
          Log.w("ButtonScreenElement", "could not find trigger target, name: " + this.mTarget);
          this.mTarget = null;
        }
      }
      return this.mTargetElement;
    }

    public abstract void perform();
  }

  public static abstract interface ButtonActionListener
  {
    public abstract boolean onButtonDoubleClick(String paramString);

    public abstract boolean onButtonDown(String paramString);

    public abstract boolean onButtonLongClick(String paramString);

    public abstract boolean onButtonUp(String paramString);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ButtonScreenElement
 * JD-Core Version:    0.6.0
 */