package miui.os;

public abstract interface AsyncTaskObserver<Params, Progress, Result>
{
  public abstract void onCancelled();

  public abstract void onPostExecute(Result paramResult);

  public abstract void onPreExecute();

  public abstract void onProgressUpdate(Progress[] paramArrayOfProgress);
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.os.AsyncTaskObserver
 * JD-Core Version:    0.6.0
 */