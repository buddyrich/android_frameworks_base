package miui.app.screenelement;

import android.text.TextUtils;
import android.util.Log;

public class Variable
{
  private String mObjectName;
  private String mPropertyName;

  public Variable(String paramString)
  {
    int i = paramString.indexOf('.');
    if (i == -1)
      this.mObjectName = null;
    for (this.mPropertyName = paramString; ; this.mPropertyName = paramString.substring(i + 1))
    {
      if (TextUtils.isEmpty(this.mPropertyName))
        Log.e("Variable", "invalid variable name:" + paramString);
      return;
      this.mObjectName = paramString.substring(0, i);
    }
  }

  public String getObjName()
  {
    return this.mObjectName;
  }

  public String getPropertyName()
  {
    return this.mPropertyName;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.Variable
 * JD-Core Version:    0.6.0
 */