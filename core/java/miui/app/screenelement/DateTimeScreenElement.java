package miui.app.screenelement;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.Log;
import java.util.Calendar;
import miui.util.LunarDate;
import org.w3c.dom.Element;

public class DateTimeScreenElement extends TextScreenElement
{
  public static final String TAG_NAME = "DateTime";
  protected Calendar mCalendar = Calendar.getInstance();
  private int mCurDay = -1;
  private String mLunarDate;
  private long mPreValue;
  private String mText;
  private Expression mValue;

  public DateTimeScreenElement(Element paramElement, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    super(paramElement, paramScreenContext);
    this.mValue = Expression.build(paramElement.getAttribute("value"));
  }

  protected String getText()
  {
    long l;
    if (this.mValue != null)
    {
      l = ()this.mValue.evaluate(this.mContext.mVariables);
      if (l - this.mPreValue >= 200L)
        break label52;
    }
    for (String str2 = this.mText; ; str2 = this.mText)
    {
      return str2;
      l = System.currentTimeMillis();
      break;
      label52: this.mCalendar.setTimeInMillis(l);
      String str1 = getFormat();
      if (str1.contains("NNNN"))
      {
        if (this.mCalendar.get(5) != this.mCurDay)
        {
          Resources localResources = this.mContext.mContext.getResources();
          this.mLunarDate = LunarDate.getString(localResources, this.mCalendar);
          String str3 = LunarDate.getSolarTerm(localResources, this.mCalendar);
          if (str3 != null)
            this.mLunarDate = (this.mLunarDate + " " + str3);
          this.mCurDay = this.mCalendar.get(5);
          Log.i("DateTimeScreenElement", "get lunar date:" + this.mLunarDate);
        }
        str1 = str1.replace("NNNN", this.mLunarDate);
      }
      this.mText = DateFormat.format(str1, this.mCalendar).toString();
      this.mPreValue = l;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.DateTimeScreenElement
 * JD-Core Version:    0.6.0
 */