package miui.app.screenelement;

import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class BaseAnimation
{
  private static final String LOG_TAG = "BaseAnimation";
  protected ScreenContext mContext;
  private long mDelay;
  protected ArrayList<AnimationItem> mItems = new ArrayList();
  private long mStartTime;
  private long mTimeRange;

  public BaseAnimation(Element paramElement, String paramString, ScreenContext paramScreenContext)
    throws ScreenElementLoadException
  {
    this.mContext = paramScreenContext;
    load(paramElement, paramString);
  }

  private void load(Element paramElement, String paramString)
    throws ScreenElementLoadException
  {
    this.mItems.clear();
    String str = paramElement.getAttribute("delay");
    if (!TextUtils.isEmpty(str));
    try
    {
      this.mDelay = Long.parseLong(str);
      NodeList localNodeList = paramElement.getElementsByTagName(paramString);
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        Element localElement = (Element)localNodeList.item(i);
        this.mItems.add(onCreateItem().load(localElement));
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        Log.w("BaseAnimation", "invalid delay attribute");
    }
    if (this.mItems.size() > 0);
    for (boolean bool = true; ; bool = false)
    {
      Utils.asserts(bool, "BaseAnimation: empty items");
      this.mTimeRange = ((AnimationItem)this.mItems.get(-1 + this.mItems.size())).mTime;
      return;
    }
  }

  public void init()
  {
    this.mStartTime = 0L;
  }

  protected abstract AnimationItem onCreateItem();

  protected abstract void onTick(AnimationItem paramAnimationItem1, AnimationItem paramAnimationItem2, float paramFloat);

  public final void tick(long paramLong)
  {
    if (this.mStartTime == 0L)
      this.mStartTime = paramLong;
    long l1 = paramLong - this.mStartTime;
    if (l1 < this.mDelay)
      onTick(null, null, 0.0F);
    label175: label194: 
    while (true)
    {
      return;
      long l2 = (l1 - this.mDelay) % this.mTimeRange;
      Object localObject = null;
      for (int i = 0; ; i++)
      {
        if (i >= this.mItems.size())
          break label194;
        AnimationItem localAnimationItem1 = (AnimationItem)this.mItems.get(i);
        if (l2 > localAnimationItem1.mTime)
          continue;
        long l3 = 0L;
        long l4;
        label109: float f;
        if (i == 0)
        {
          l4 = localAnimationItem1.mTime;
          if (l4 != 0L)
            break label175;
          f = 1.0F;
        }
        while (true)
        {
          onTick((AnimationItem)localObject, localAnimationItem1, f);
          break;
          AnimationItem localAnimationItem2 = (AnimationItem)this.mItems.get(i - 1);
          localObject = localAnimationItem2;
          l4 = localAnimationItem1.mTime - localAnimationItem2.mTime;
          l3 = localAnimationItem2.mTime;
          break label109;
          f = (float)(l2 - l3) / (float)l4;
        }
      }
    }
  }

  public static class AnimationItem
  {
    private String[] mAttrs;
    private ScreenContext mContext;
    public Expression[] mExps;
    public long mTime;

    public AnimationItem(String[] paramArrayOfString, ScreenContext paramScreenContext)
    {
      this.mAttrs = paramArrayOfString;
      this.mContext = paramScreenContext;
    }

    public double get(int paramInt)
    {
      double d = 0.0D;
      if ((paramInt < 0) || (paramInt >= this.mExps.length) || (this.mExps == null))
        Log.e("BaseAnimation", "fail to get number in AnimationItem:" + paramInt);
      while (true)
      {
        return d;
        if (this.mExps[paramInt] == null)
          continue;
        d = this.mExps[paramInt].evaluate(this.mContext.mVariables);
      }
    }

    public AnimationItem load(Element paramElement)
      throws ScreenElementLoadException
    {
      try
      {
        this.mTime = Long.parseLong(paramElement.getAttribute("time"));
        if (this.mAttrs != null)
        {
          this.mExps = new Expression[this.mAttrs.length];
          String[] arrayOfString = this.mAttrs;
          int i = arrayOfString.length;
          int j = 0;
          int m;
          for (int k = 0; j < i; k = m)
          {
            String str = arrayOfString[j];
            Expression[] arrayOfExpression = this.mExps;
            m = k + 1;
            arrayOfExpression[k] = Expression.build(paramElement.getAttribute(str));
            j++;
          }
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        Log.e("BaseAnimation", "fail to get time attribute");
        throw new ScreenElementLoadException("fail to get time attribute");
      }
      return this;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.BaseAnimation
 * JD-Core Version:    0.6.0
 */