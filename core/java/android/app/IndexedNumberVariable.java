package miui.app.screenelement;

public class IndexedNumberVariable
{
  private int mIndex = -1;
  private Variables mVars;

  public IndexedNumberVariable(String paramString1, String paramString2, Variables paramVariables)
  {
    this.mIndex = paramVariables.registerNumberVariable(paramString1, paramString2);
    this.mVars = paramVariables;
  }

  public IndexedNumberVariable(String paramString, Variables paramVariables)
  {
    this(null, paramString, paramVariables);
  }

  public Double get()
  {
    return this.mVars.getNum(this.mIndex);
  }

  public void set(double paramDouble)
  {
    set(Double.valueOf(paramDouble));
  }

  public void set(Double paramDouble)
  {
    this.mVars.putNum(this.mIndex, paramDouble);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.IndexedNumberVariable
 * JD-Core Version:    0.6.0
 */