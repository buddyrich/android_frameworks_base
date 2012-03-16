package miui.app.screenelement;

import android.graphics.Canvas;
import org.w3c.dom.Element;

public class VariableElement extends ScreenElement
{
  public static final String TAG_NAME = "Var";
  private boolean mConst;
  private Expression mExpression;
  private boolean mIsStringType;
  private IndexedNumberVariable mNumberVar;
  private IndexedStringVariable mStringVar;

  public VariableElement(Element paramElement, ScreenContext paramScreenContext)
  {
    super(paramElement, paramScreenContext);
    if (paramElement != null)
    {
      this.mExpression = Expression.build(paramElement.getAttribute("expression"));
      this.mIsStringType = "string".equalsIgnoreCase(paramElement.getAttribute("type"));
      this.mConst = Boolean.parseBoolean(paramElement.getAttribute("const"));
      if (!this.mIsStringType)
        break label84;
      this.mStringVar = new IndexedStringVariable(this.mName, paramScreenContext.mVariables);
    }
    while (true)
    {
      return;
      label84: this.mNumberVar = new IndexedNumberVariable(this.mName, paramScreenContext.mVariables);
    }
  }

  private void update()
  {
    if (this.mExpression == null);
    Variables localVariables;
    while (true)
    {
      return;
      localVariables = this.mContext.mVariables;
      if (!this.mIsStringType)
        break;
      this.mStringVar.set(this.mExpression.evaluateStr(localVariables));
    }
    IndexedNumberVariable localIndexedNumberVariable = this.mNumberVar;
    if (this.mExpression.isNull(localVariables));
    for (Double localDouble = null; ; localDouble = Double.valueOf(this.mExpression.evaluate(localVariables)))
    {
      localIndexedNumberVariable.set(localDouble);
      break;
    }
  }

  public void init()
  {
    update();
  }

  public void render(Canvas paramCanvas)
  {
  }

  public void tick(long paramLong)
  {
    if (!this.mConst)
      update();
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.VariableElement
 * JD-Core Version:    0.6.0
 */