package miui.widget;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class DateTimePicker extends FrameLayout
{
  private static final int AMPM_SPINNER_MAX_VAL = 1;
  private static final int AMPM_SPINNER_MIN_VAL = 0;
  private static final int DATE_SPINNER_MAX_VAL = 6;
  private static final int DATE_SPINNER_MIN_VAL = 0;
  private static final int DAYS_IN_ALL_WEEK = 7;
  private static final boolean DEFAULT_ENABLE_STATE = true;
  private static final int HOURS_IN_ALL_DAY = 24;
  private static final int HOURS_IN_HALF_DAY = 12;
  private static final int HOUR_SPINNER_MAX_VAL_12_HOUR_VIEW = 12;
  private static final int HOUR_SPINNER_MAX_VAL_24_HOUR_VIEW = 23;
  private static final int HOUR_SPINNER_MIN_VAL_12_HOUR_VIEW = 1;
  private static final int HOUR_SPINNER_MIN_VAL_24_HOUR_VIEW = 0;
  private static final int MINUT_SPINNER_MAX_VAL = 59;
  private static final int MINUT_SPINNER_MIN_VAL;
  private final NumberPicker mAmPmSpinner;
  private Calendar mDate = Calendar.getInstance();
  private String[] mDateDisplayValues;
  private final NumberPicker mDateSpinner;
  private final NumberPicker mHourSpinner;
  private boolean mInitialising = true;
  private boolean mIs24HourView;
  private boolean mIsAm;
  private boolean mIsEnabled = true;
  private final NumberPicker mMinuteSpinner;
  private NumberPicker.OnValueChangeListener mOnAmPmChangedListener = new NumberPicker.OnValueChangeListener()
  {
    public void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2)
    {
      DateTimePicker localDateTimePicker = DateTimePicker.this;
      boolean bool;
      if (!DateTimePicker.this.mIsAm)
      {
        bool = true;
        DateTimePicker.access$402(localDateTimePicker, bool);
        if (!DateTimePicker.this.mIsAm)
          break label72;
        DateTimePicker.this.mDate.add(11, -12);
      }
      while (true)
      {
        DateTimePicker.this.updateAmPmControl();
        DateTimePicker.this.onDateTimeChanged();
        return;
        bool = false;
        break;
        label72: DateTimePicker.this.mDate.add(11, 12);
      }
    }
  };
  private NumberPicker.OnValueChangeListener mOnDateChangedListener = new NumberPicker.OnValueChangeListener()
  {
    public void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2)
    {
      DateTimePicker.this.mDate.add(6, paramInt2 - paramInt1);
      DateTimePicker.this.updateDateControl();
      DateTimePicker.this.onDateTimeChanged();
    }
  };
  private OnDateTimeChangedListener mOnDateTimeChangedListener;
  private NumberPicker.OnValueChangeListener mOnHourChangedListener = new NumberPicker.OnValueChangeListener()
  {
    public void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2)
    {
      int i = 0;
      Calendar localCalendar = Calendar.getInstance();
      boolean bool;
      label109: label124: int j;
      if (!DateTimePicker.this.mIs24HourView)
        if ((!DateTimePicker.this.mIsAm) && (paramInt1 == 11) && (paramInt2 == 12))
        {
          localCalendar.setTimeInMillis(DateTimePicker.this.mDate.getTimeInMillis());
          localCalendar.add(6, 1);
          i = 1;
          if (((paramInt1 == 11) && (paramInt2 == 12)) || ((paramInt1 == 12) && (paramInt2 == 11)))
          {
            DateTimePicker localDateTimePicker = DateTimePicker.this;
            if (DateTimePicker.this.mIsAm)
              break label277;
            bool = true;
            DateTimePicker.access$402(localDateTimePicker, bool);
            DateTimePicker.this.updateAmPmControl();
          }
          j = DateTimePicker.this.mHourSpinner.getValue() % 12;
          if (!DateTimePicker.this.mIsAm)
            break label362;
        }
      label277: label362: for (int k = 0; ; k = 12)
      {
        int m = j + k;
        DateTimePicker.this.mDate.set(11, m);
        DateTimePicker.this.onDateTimeChanged();
        if (i != 0)
        {
          DateTimePicker.this.setCurrentYear(localCalendar.get(1));
          DateTimePicker.this.setCurrentMonth(localCalendar.get(2));
          DateTimePicker.this.setCurrentDay(localCalendar.get(5));
        }
        return;
        if ((!DateTimePicker.this.mIsAm) || (paramInt1 != 12) || (paramInt2 != 11))
          break;
        localCalendar.setTimeInMillis(DateTimePicker.this.mDate.getTimeInMillis());
        localCalendar.add(6, -1);
        i = 1;
        break;
        bool = false;
        break label109;
        if ((paramInt1 == 23) && (paramInt2 == 0))
        {
          localCalendar.setTimeInMillis(DateTimePicker.this.mDate.getTimeInMillis());
          localCalendar.add(6, 1);
          i = 1;
          break label124;
        }
        if ((paramInt1 != 0) || (paramInt2 != 23))
          break label124;
        localCalendar.setTimeInMillis(DateTimePicker.this.mDate.getTimeInMillis());
        localCalendar.add(6, -1);
        i = 1;
        break label124;
      }
    }
  };
  private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new NumberPicker.OnValueChangeListener()
  {
    public void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2)
    {
      int i = DateTimePicker.this.mMinuteSpinner.getMinValue();
      int j = DateTimePicker.this.mMinuteSpinner.getMaxValue();
      int k = 0;
      if ((paramInt1 == j) && (paramInt2 == i))
      {
        k = 0 + 1;
        if (k != 0)
        {
          DateTimePicker.this.mDate.add(11, k);
          DateTimePicker.this.mHourSpinner.setValue(DateTimePicker.this.getCurrentHour());
          DateTimePicker.this.updateDateControl();
          if (DateTimePicker.this.getCurrentHourOfDay() < 12)
            break label156;
          DateTimePicker.access$402(DateTimePicker.this, false);
          DateTimePicker.this.updateAmPmControl();
        }
      }
      while (true)
      {
        DateTimePicker.this.mDate.set(12, paramInt2);
        DateTimePicker.this.onDateTimeChanged();
        return;
        if ((paramInt1 != i) || (paramInt2 != j))
          break;
        k = 0 - 1;
        break;
        label156: DateTimePicker.access$402(DateTimePicker.this, true);
        DateTimePicker.this.updateAmPmControl();
      }
    }
  };

  public DateTimePicker(Context paramContext)
  {
    this(paramContext, System.currentTimeMillis());
  }

  public DateTimePicker(Context paramContext, long paramLong)
  {
    this(paramContext, paramLong, DateFormat.is24HourFormat(paramContext));
  }

  public DateTimePicker(Context paramContext, long paramLong, boolean paramBoolean)
  {
    super(paramContext);
    if (getCurrentHourOfDay() >= 12);
    for (boolean bool = true; ; bool = false)
    {
      this.mIsAm = bool;
      inflate(paramContext, 50528287, this);
      this.mDateSpinner = ((NumberPicker)findViewById(51052581));
      this.mDateSpinner.setMinValue(0);
      this.mDateSpinner.setMaxValue(6);
      this.mDateSpinner.setOnValueChangedListener(this.mOnDateChangedListener);
      this.mHourSpinner = ((NumberPicker)findViewById(51052582));
      this.mHourSpinner.setOnValueChangedListener(this.mOnHourChangedListener);
      this.mMinuteSpinner = ((NumberPicker)findViewById(51052583));
      this.mMinuteSpinner.setMinValue(0);
      this.mMinuteSpinner.setMaxValue(59);
      this.mMinuteSpinner.setOnLongPressUpdateInterval(100L);
      this.mMinuteSpinner.setOnValueChangedListener(this.mOnMinuteChangedListener);
      this.mDateDisplayValues = new String[7];
      String[] arrayOfString = new DateFormatSymbols().getAmPmStrings();
      this.mAmPmSpinner = ((NumberPicker)findViewById(51052584));
      this.mAmPmSpinner.setMinValue(0);
      this.mAmPmSpinner.setMaxValue(1);
      this.mAmPmSpinner.setDisplayedValues(arrayOfString);
      this.mAmPmSpinner.setOnValueChangedListener(this.mOnAmPmChangedListener);
      updateDateControl();
      updateHourControl();
      updateAmPmControl();
      set24HourView(paramBoolean);
      setCurrentDate(paramLong);
      setEnabled(isEnabled());
      this.mInitialising = false;
      return;
    }
  }

  private int getCurrentHour()
  {
    int i;
    if (this.mIs24HourView)
      i = getCurrentHourOfDay();
    while (true)
    {
      return i;
      i = getCurrentHourOfDay();
      if (i > 12)
      {
        i -= 12;
        continue;
      }
      if (i != 0)
        continue;
      i = 12;
    }
  }

  private void onDateTimeChanged()
  {
    if (this.mOnDateTimeChangedListener != null)
      this.mOnDateTimeChangedListener.onDateTimeChanged(this, getCurrentYear(), getCurrentMonth(), getCurrentDay(), getCurrentHourOfDay(), getCurrentMinute());
  }

  private void updateAmPmControl()
  {
    if (this.mIs24HourView)
    {
      this.mAmPmSpinner.setVisibility(8);
      return;
    }
    if (this.mIsAm);
    for (int i = 0; ; i = 1)
    {
      this.mAmPmSpinner.setValue(i);
      this.mAmPmSpinner.setVisibility(0);
      break;
    }
  }

  private void updateDateControl()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(this.mDate.getTimeInMillis());
    localCalendar.add(6, -4);
    this.mDateSpinner.setDisplayedValues(null);
    for (int i = 0; i < 7; i++)
    {
      localCalendar.add(6, 1);
      this.mDateDisplayValues[i] = ((String)DateFormat.format("MM.dd EEEE", localCalendar));
    }
    this.mDateSpinner.setDisplayedValues(this.mDateDisplayValues);
    this.mDateSpinner.setValue(3);
    this.mDateSpinner.invalidate();
  }

  private void updateHourControl()
  {
    if (this.mIs24HourView)
    {
      this.mHourSpinner.setMinValue(0);
      this.mHourSpinner.setMaxValue(23);
    }
    while (true)
    {
      return;
      this.mHourSpinner.setMinValue(1);
      this.mHourSpinner.setMaxValue(12);
    }
  }

  public long getCurrentDateInTimeMillis()
  {
    return this.mDate.getTimeInMillis();
  }

  public int getCurrentDay()
  {
    return this.mDate.get(5);
  }

  public int getCurrentHourOfDay()
  {
    return this.mDate.get(11);
  }

  public int getCurrentMinute()
  {
    return this.mDate.get(12);
  }

  public int getCurrentMonth()
  {
    return this.mDate.get(2);
  }

  public int getCurrentYear()
  {
    return this.mDate.get(1);
  }

  public boolean is24HourView()
  {
    return this.mIs24HourView;
  }

  public boolean isEnabled()
  {
    return this.mIsEnabled;
  }

  public void set24HourView(boolean paramBoolean)
  {
    if (this.mIs24HourView == paramBoolean)
      return;
    this.mIs24HourView = paramBoolean;
    NumberPicker localNumberPicker = this.mAmPmSpinner;
    if (paramBoolean);
    for (int i = 8; ; i = 0)
    {
      localNumberPicker.setVisibility(i);
      int j = getCurrentHourOfDay();
      updateHourControl();
      setCurrentHour(j);
      updateAmPmControl();
      break;
    }
  }

  public void setCurrentDate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    setCurrentYear(paramInt1);
    setCurrentMonth(paramInt2);
    setCurrentDay(paramInt3);
    setCurrentHour(paramInt4);
    setCurrentMinute(paramInt5);
  }

  public void setCurrentDate(long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    setCurrentDate(localCalendar.get(1), localCalendar.get(2), localCalendar.get(5), localCalendar.get(11), localCalendar.get(12));
  }

  public void setCurrentDay(int paramInt)
  {
    if ((!this.mInitialising) && (paramInt == getCurrentDay()));
    while (true)
    {
      return;
      this.mDate.set(5, paramInt);
      updateDateControl();
      onDateTimeChanged();
    }
  }

  public void setCurrentHour(int paramInt)
  {
    if ((!this.mInitialising) && (paramInt == getCurrentHourOfDay()))
      return;
    this.mDate.set(11, paramInt);
    if (!this.mIs24HourView)
    {
      if (paramInt < 12)
        break label72;
      this.mIsAm = false;
      if (paramInt > 12)
        paramInt -= 12;
    }
    while (true)
    {
      updateAmPmControl();
      this.mHourSpinner.setValue(paramInt);
      onDateTimeChanged();
      break;
      label72: this.mIsAm = true;
      if (paramInt != 0)
        continue;
      paramInt = 12;
    }
  }

  public void setCurrentMinute(int paramInt)
  {
    if ((!this.mInitialising) && (paramInt == getCurrentMinute()));
    while (true)
    {
      return;
      this.mMinuteSpinner.setValue(paramInt);
      this.mDate.set(12, paramInt);
      onDateTimeChanged();
    }
  }

  public void setCurrentMonth(int paramInt)
  {
    if ((!this.mInitialising) && (paramInt == getCurrentMonth()));
    while (true)
    {
      return;
      this.mDate.set(2, paramInt);
      updateDateControl();
      onDateTimeChanged();
    }
  }

  public void setCurrentYear(int paramInt)
  {
    if ((!this.mInitialising) && (paramInt == getCurrentYear()));
    while (true)
    {
      return;
      this.mDate.set(1, paramInt);
      updateDateControl();
      onDateTimeChanged();
    }
  }

  public void setEnabled(boolean paramBoolean)
  {
    if (this.mIsEnabled == paramBoolean);
    while (true)
    {
      return;
      super.setEnabled(paramBoolean);
      this.mDateSpinner.setEnabled(paramBoolean);
      this.mMinuteSpinner.setEnabled(paramBoolean);
      this.mHourSpinner.setEnabled(paramBoolean);
      this.mAmPmSpinner.setEnabled(paramBoolean);
      this.mIsEnabled = paramBoolean;
    }
  }

  public void setOnDateTimeChangedListener(OnDateTimeChangedListener paramOnDateTimeChangedListener)
  {
    this.mOnDateTimeChangedListener = paramOnDateTimeChangedListener;
  }

  public static abstract interface OnDateTimeChangedListener
  {
    public abstract void onDateTimeChanged(DateTimePicker paramDateTimePicker, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.DateTimePicker
 * JD-Core Version:    0.6.0
 */