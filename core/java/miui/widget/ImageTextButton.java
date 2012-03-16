package miui.widget;

import android.R.styleable;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageTextButton extends LinearLayout
{
  private TextView mTextView;

  public ImageTextButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(50528259, this);
    this.mTextView = ((TextView)findViewById(16908308));
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TextView);
    int i = localTypedArray.getDimensionPixelSize(2, (int)this.mTextView.getTextSize());
    this.mTextView.setTextSize(0, i);
    ColorStateList localColorStateList = localTypedArray.getColorStateList(5);
    if (localColorStateList != null)
      this.mTextView.setTextColor(localColorStateList);
    Drawable localDrawable = localTypedArray.getDrawable(50);
    if (localDrawable != null)
      this.mTextView.setCompoundDrawablesWithIntrinsicBounds(localDrawable, null, null, null);
    int j = localTypedArray.getDimensionPixelSize(52, 0);
    this.mTextView.setCompoundDrawablePadding(j);
    String str = localTypedArray.getString(18);
    this.mTextView.setText(str);
    localTypedArray.recycle();
  }

  public TextView getTextView()
  {
    return this.mTextView;
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.mTextView.setEnabled(paramBoolean);
    super.setEnabled(paramBoolean);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.ImageTextButton
 * JD-Core Version:    0.6.0
 */