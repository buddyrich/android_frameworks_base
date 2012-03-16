package miui.app.screenelement;

import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;

public class VariableArrayElement extends ScreenElement
{
  public static final String TAG_NAME = "VarArray";
  private ArrayList<Item> mArray = new ArrayList();
  private boolean mIsStringType;
  private ArrayList<Var> mVars = new ArrayList();

  public VariableArrayElement(Element paramElement, ScreenContext paramScreenContext)
  {
    super(paramElement, paramScreenContext);
    if (paramElement != null)
    {
      this.mIsStringType = "string".equalsIgnoreCase(paramElement.getAttribute("type"));
      Utils.traverseXmlElementChildren(Utils.getChild(paramElement, "Vars"), "Var", new Utils.XmlTraverseListener()
      {
        public void onChild(Element paramElement)
        {
          VariableArrayElement.this.mVars.add(new VariableArrayElement.Var(VariableArrayElement.this, paramElement));
        }
      });
      Utils.traverseXmlElementChildren(Utils.getChild(paramElement, "Items"), "Item", new Utils.XmlTraverseListener()
      {
        public void onChild(Element paramElement)
        {
          VariableArrayElement.this.mArray.add(new VariableArrayElement.Item(VariableArrayElement.this, paramElement));
        }
      });
    }
  }

  public void init()
  {
    Iterator localIterator = this.mVars.iterator();
    while (localIterator.hasNext())
      ((Var)localIterator.next()).init();
  }

  public void render(Canvas paramCanvas)
  {
  }

  public void tick(long paramLong)
  {
    Iterator localIterator = this.mVars.iterator();
    while (localIterator.hasNext())
      ((Var)localIterator.next()).tick();
  }

  private class Item
  {
    public Expression mExpression;
    public double mNumValue;
    public String mStrValue;

    public Item(Element arg2)
    {
      Object localObject;
      if (localObject != null)
      {
        this.mExpression = Expression.build(localObject.getAttribute("expression"));
        this.mStrValue = localObject.getAttribute("value");
        if (VariableArrayElement.this.mIsStringType);
      }
      try
      {
        this.mNumValue = Double.parseDouble(this.mStrValue);
        label58: return;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        break label58;
      }
    }

    public Double evaluate(Variables paramVariables)
    {
      Double localDouble;
      if (this.mExpression != null)
        if (this.mExpression.isNull(paramVariables))
          localDouble = null;
      while (true)
      {
        return localDouble;
        localDouble = Double.valueOf(this.mExpression.evaluate(paramVariables));
        continue;
        localDouble = Double.valueOf(this.mNumValue);
      }
    }

    public String evaluateStr(Variables paramVariables)
    {
      if (this.mExpression != null);
      for (String str = this.mExpression.evaluateStr(paramVariables); ; str = this.mStrValue)
        return str;
    }

    public boolean isExpression()
    {
      if (this.mExpression != null);
      for (int i = 1; ; i = 0)
        return i;
    }
  }

  private class Var
  {
    private boolean mConst;
    private boolean mCurrentItemIsExpression;
    private int mIndex = -1;
    private Expression mIndexExpression;
    private String mName;
    private IndexedNumberVariable mNumberVar;
    private IndexedStringVariable mStringVar;

    public Var(Element arg2)
    {
      Object localObject;
      if (localObject != null)
      {
        this.mName = localObject.getAttribute("name");
        this.mIndexExpression = Expression.build(localObject.getAttribute("index"));
        this.mConst = Boolean.parseBoolean(localObject.getAttribute("const"));
        if (!VariableArrayElement.this.mIsStringType)
          break label91;
        this.mStringVar = new IndexedStringVariable(this.mName, VariableArrayElement.this.mContext.mVariables);
      }
      while (true)
      {
        return;
        label91: this.mNumberVar = new IndexedNumberVariable(this.mName, VariableArrayElement.this.mContext.mVariables);
      }
    }

    private void update()
    {
      if (this.mIndexExpression == null);
      while (true)
      {
        return;
        Variables localVariables = VariableArrayElement.this.mContext.mVariables;
        int i = (int)this.mIndexExpression.evaluate(localVariables);
        if ((i < 0) || (i >= VariableArrayElement.this.mArray.size()))
        {
          if (VariableArrayElement.this.mIsStringType)
          {
            this.mStringVar.set(null);
            continue;
          }
          this.mNumberVar.set(null);
          continue;
        }
        if ((this.mIndex == i) && (!this.mCurrentItemIsExpression))
          continue;
        VariableArrayElement.Item localItem = (VariableArrayElement.Item)VariableArrayElement.this.mArray.get(i);
        if (this.mIndex != i)
        {
          this.mIndex = i;
          this.mCurrentItemIsExpression = localItem.isExpression();
        }
        if (VariableArrayElement.this.mIsStringType)
        {
          this.mStringVar.set(localItem.evaluateStr(localVariables));
          continue;
        }
        this.mNumberVar.set(localItem.evaluate(localVariables));
      }
    }

    public void init()
    {
      update();
    }

    public void tick()
    {
      if (!this.mConst)
        update();
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.VariableArrayElement
 * JD-Core Version:    0.6.0
 */