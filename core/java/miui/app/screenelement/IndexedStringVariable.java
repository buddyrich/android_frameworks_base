package miui.app.screenelement;

public class IndexedStringVariable
{
  private int mIndex = -1;
  private Variables mVars;

  public IndexedStringVariable(String paramString1, String paramString2, Variables paramVariables)
  {
    this.mIndex = paramVariables.registerStringVariable(paramString1, paramString2);
    this.mVars = paramVariables;
  }

  public IndexedStringVariable(String paramString, Variables paramVariables)
  {
    this(null, paramString, paramVariables);
  }

  public String get()
  {
    return this.mVars.getStr(this.mIndex);
  }

  public void set(String paramString)
  {
    this.mVars.putStr(this.mIndex, paramString);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.IndexedStringVariable
 * JD-Core Version:    0.6.0
 */