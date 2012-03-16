package miui.preference;

import android.content.Context;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;

public class RadioButtonPreferenceCategory extends PreferenceCategory
  implements Preference.OnPreferenceChangeListener
{
  private static final String TAG = "RadioButtonPreferenceCategory";
  private int mCheckedPosition = -1;
  private RadioButtonPreference mRadioButtonPreference = null;

  public RadioButtonPreferenceCategory(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean addPreference(Preference paramPreference)
  {
    if (!(paramPreference instanceof RadioButtonPreference))
      throw new IllegalArgumentException("Only CheckBoxPreference can be added toRadioButtonPreferenceCategory");
    boolean bool = super.addPreference(paramPreference);
    if (bool)
      paramPreference.setOnPreferenceChangeListener(this);
    return bool;
  }

  public int getCheckedPosition()
  {
    return this.mCheckedPosition;
  }

  public Preference getCheckedPreference()
  {
    return this.mRadioButtonPreference;
  }

  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    if (paramPreference != this.mRadioButtonPreference)
      setCheckedPreference(paramPreference);
    Preference.OnPreferenceClickListener localOnPreferenceClickListener = getOnPreferenceClickListener();
    if (localOnPreferenceClickListener != null)
      localOnPreferenceClickListener.onPreferenceClick(this);
    return false;
  }

  public void setCheckedPosition(int paramInt)
  {
    setCheckedPreference(getPreference(paramInt));
  }

  public void setCheckedPreference(Preference paramPreference)
  {
    int i = getPreferenceCount();
    int j = 0;
    if (j < i)
    {
      RadioButtonPreference localRadioButtonPreference = (RadioButtonPreference)getPreference(j);
      if (localRadioButtonPreference == paramPreference)
      {
        this.mRadioButtonPreference = localRadioButtonPreference;
        this.mCheckedPosition = j;
        localRadioButtonPreference.setChecked(true);
      }
      while (true)
      {
        j++;
        break;
        localRadioButtonPreference.setChecked(false);
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.preference.RadioButtonPreferenceCategory
 * JD-Core Version:    0.6.0
 */