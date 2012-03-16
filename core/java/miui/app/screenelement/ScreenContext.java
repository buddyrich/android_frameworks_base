package miui.app.screenelement;

import android.content.Context;
import android.view.View;

public class ScreenContext
{
  public int mBatteryLevel;
  public final Context mContext;
  public final ScreenElementFactory mFactory;
  public boolean mPluggedIn;
  public final ResourceManager mResourceManager;
  public ScreenElementRoot mRoot;
  public boolean mShouldUpdate;
  public boolean mShowingBatteryInfo;
  public Variables mVariables = new Variables();
  public View mView;

  public ScreenContext(Context paramContext, ResourceManager paramResourceManager)
  {
    this(paramContext, paramResourceManager, new ScreenElementFactory());
  }

  public ScreenContext(Context paramContext, ResourceManager paramResourceManager, ScreenElementFactory paramScreenElementFactory)
  {
    this.mContext = paramContext;
    this.mResourceManager = paramResourceManager;
    this.mFactory = paramScreenElementFactory;
  }

  public void setResourceDensity(int paramInt)
  {
    this.mResourceManager.setResourceDensity(paramInt);
  }

  public void setTargetDensity(int paramInt)
  {
    this.mResourceManager.setTargetDensity(paramInt);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.screenelement.ScreenContext
 * JD-Core Version:    0.6.0
 */