package miui.os;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.Window;

public abstract class AsyncTaskWithProgress<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
  protected ProgressDialog mDialog;

  public AsyncTaskWithProgress(ProgressDialog paramProgressDialog)
  {
    init(paramProgressDialog);
  }

  public AsyncTaskWithProgress(Context paramContext, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    ProgressDialog localProgressDialog = new ProgressDialog(paramContext, paramInt2);
    Object localObject1;
    if (paramInt1 == 0)
    {
      localObject1 = null;
      localProgressDialog.setTitle((CharSequence)localObject1);
      if (paramInt3 != 0)
        break label83;
    }
    label83: for (Object localObject2 = null; ; localObject2 = paramContext.getResources().getString(paramInt3))
    {
      localProgressDialog.setMessage((CharSequence)localObject2);
      localProgressDialog.setIndeterminate(true);
      localProgressDialog.setCancelable(paramBoolean);
      localProgressDialog.setOnCancelListener(null);
      init(localProgressDialog);
      return;
      localObject1 = paramContext.getResources().getString(paramInt1);
      break;
    }
  }

  public AsyncTaskWithProgress(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this(paramContext, paramInt1, 3, paramInt2, paramBoolean);
  }

  private void dismissDialog()
  {
    try
    {
      if ((this.mDialog != null) && (this.mDialog.isShowing()))
        this.mDialog.dismiss();
      label24: return;
    }
    catch (Exception localException)
    {
      break label24;
    }
  }

  private void init(ProgressDialog paramProgressDialog)
  {
    this.mDialog = paramProgressDialog;
    if (this.mDialog != null)
      this.mDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramDialogInterface)
        {
          AsyncTaskWithProgress.this.cancel(true);
        }
      });
  }

  public void onCancelled()
  {
    dismissDialog();
  }

  protected void onPostExecute(Result paramResult)
  {
    dismissDialog();
  }

  protected void onPreExecute()
  {
    if (this.mDialog != null)
      this.mDialog.show();
  }

  public void setDialogType(int paramInt)
  {
    if (this.mDialog != null)
      this.mDialog.getWindow().setType(paramInt);
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.os.AsyncTaskWithProgress
 * JD-Core Version:    0.6.0
 */