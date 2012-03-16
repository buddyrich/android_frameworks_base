package miui.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.format.DateUtils;
import java.util.Calendar;
import miui.widget.DateTimePicker;
import miui.widget.DateTimePicker.OnDateTimeChangedListener;

public class DateTimePickerDialog extends AlertDialog
  implements DialogInterface.OnClickListener
{
  private Calendar mDate;
  private DateTimePicker mDateTimePicker;
  private OnDateTimeSetListener mOnDateTimeSetListener;

  public DateTimePickerDialog(Context paramContext, long paramLong)
  {
    super(paramContext);
    this.mDateTimePicker = new DateTimePicker(paramContext);
    setView(this.mDateTimePicker);
    this.mDate = Calendar.getInstance();
    this.mDateTimePicker.setOnDateTimeChangedListener(new DateTimePicker.OnDateTimeChangedListener()
    {
      public void onDateTimeChanged(DateTimePicker paramDateTimePicker, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
      {
        DateTimePickerDialog.this.mDate.set(1, paramInt1);
        DateTimePickerDialog.this.mDate.set(2, paramInt2);
        DateTimePickerDialog.this.mDate.set(5, paramInt3);
        DateTimePickerDialog.this.mDate.set(11, paramInt4);
        DateTimePickerDialog.this.mDate.set(12, paramInt5);
        DateTimePickerDialog.this.updateTitle(DateTimePickerDialog.this.mDate.getTimeInMillis());
      }
    });
    this.mDate.setTimeInMillis(paramLong);
    this.mDateTimePicker.setCurrentDate(paramLong);
    setButton(paramContext.getString(17039370), this);
    setButton2(paramContext.getString(17039360), (DialogInterface.OnClickListener)null);
    updateTitle(paramLong);
  }

  private void updateTitle(long paramLong)
  {
    if (this.mDateTimePicker.is24HourView());
    for (int i = 128; ; i = 64)
    {
      int j = 0x15 | i;
      setTitle(DateUtils.formatDateTime(getContext(), paramLong, j));
      return;
    }
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (this.mOnDateTimeSetListener != null)
      this.mOnDateTimeSetListener.OnDateTimeSet(this, this.mDate.getTimeInMillis());
  }

  public void setOnDateTimeSetListener(OnDateTimeSetListener paramOnDateTimeSetListener)
  {
    this.mOnDateTimeSetListener = paramOnDateTimeSetListener;
  }

  public static abstract interface OnDateTimeSetListener
  {
    public abstract void OnDateTimeSet(AlertDialog paramAlertDialog, long paramLong);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.DateTimePickerDialog
 * JD-Core Version:    0.6.0
 */