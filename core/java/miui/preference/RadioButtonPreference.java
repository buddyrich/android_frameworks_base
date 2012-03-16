package miui.preference;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;

public class RadioButtonPreference extends CheckBoxPreference
{
  public RadioButtonPreference(Context paramContext)
  {
    this(paramContext, null);
  }

  public RadioButtonPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setWidgetLayoutResource(50528281);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.preference.RadioButtonPreference
 * JD-Core Version:    0.6.0
 */