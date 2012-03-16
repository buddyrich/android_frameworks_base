package miui.app.resourcebrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.HashMap;
import java.util.List;
import miui.os.AsyncTaskObserver;
import miui.widget.AsyncAdapter;
import miui.widget.AsyncAdapter.AsyncLoadDataTask;
import miui.widget.AsyncAdapter.AsyncLoadDataVisitor;

public class ResourceFilterListActivity extends LocalResourceListActivity
{
  public static final String FILTER = "type";

  protected ResourceAdapter getAdapter()
  {
    this.mMetaData.putString("com.miui.android.resourcebrowser.RESOURCE_SET_SUBPACKAGE", ".local");
    return new FilterAdapter(this, this.mMetaData, getIntent().getIntExtra("type", 0));
  }

  public static class FilterAdapter extends LocalResourceAdapter
  {
    private final int mFilterCode;

    public FilterAdapter(Context paramContext, Bundle paramBundle, int paramInt)
    {
      super(paramBundle);
      this.mFilterCode = paramInt;
    }

    protected AsyncAdapter<Resource>.AsyncLoadDataTask getLoadDataTask()
    {
      AsyncClockStyleLoadTask localAsyncClockStyleLoadTask = new AsyncClockStyleLoadTask();
      localAsyncClockStyleLoadTask.addObserver((AsyncTaskObserver)this.mContext);
      List localList = getVisitors();
      for (int i = 0; i < localList.size(); i++)
        localAsyncClockStyleLoadTask.addVisitor((AsyncAdapter.AsyncLoadDataVisitor)localList.get(i));
      return localAsyncClockStyleLoadTask;
    }

    public class AsyncClockStyleLoadTask extends AsyncAdapter.AsyncLoadDataTask
    {
      public AsyncClockStyleLoadTask()
      {
        super();
      }

      protected Resource[] loadData(int paramInt)
      {
        return null;
      }

      public void onLoadData(Resource[] paramArrayOfResource)
      {
        boolean[] arrayOfBoolean;
        int i;
        int j;
        HashMap localHashMap;
        if (paramArrayOfResource != null)
        {
          arrayOfBoolean = new boolean[paramArrayOfResource.length];
          i = 0;
          j = 0;
          if (j < paramArrayOfResource.length)
          {
            localHashMap = (HashMap)paramArrayOfResource[j].getInformation().getSerializable("RESOURCE_NVP");
            if (localHashMap != null);
          }
        }
        while (true)
        {
          j++;
          break;
          String str = (String)localHashMap.get("type");
          try
          {
            if ((ResourceFilterListActivity.FilterAdapter.this.mFilterCode & Integer.valueOf(str).intValue()) != 0);
            for (int i1 = 1; ; i1 = 0)
            {
              arrayOfBoolean[j] = i1;
              int i2 = arrayOfBoolean[j];
              if (i2 == 0)
                break;
              i++;
              break;
            }
            Resource[] arrayOfResource = new Resource[i];
            int k = 0;
            for (int m = 0; m < arrayOfResource.length; m++)
            {
              if (arrayOfBoolean[m] == 0)
                continue;
              int n = k + 1;
              arrayOfResource[k] = paramArrayOfResource[m];
              k = n;
            }
            paramArrayOfResource = arrayOfResource;
            super.onLoadData(paramArrayOfResource);
            return;
          }
          catch (NumberFormatException localNumberFormatException)
          {
          }
        }
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.app.resourcebrowser.ResourceFilterListActivity
 * JD-Core Version:    0.6.0
 */