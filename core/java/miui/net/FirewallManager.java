package miui.net;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import com.android.internal.telephony.ApnSetting;

public class FirewallManager
{
  private static final boolean DEBUG = true;
  private static final String LOG_TAG = "FirewallService";
  public static final String SERVICE_NAME = "miui.Firewall";
  private static FirewallManager sInstance = new FirewallManager();
  private IFirewall mService;

  private FirewallManager()
  {
    ensureService();
  }

  public static void checkAccessControl(Activity paramActivity, ContentResolver paramContentResolver, String paramString1, PackageManager paramPackageManager, IApplicationThread paramIApplicationThread, IBinder paramIBinder, String paramString2)
  {
    if (paramActivity != null);
    while (true)
    {
      return;
      if (1 != Settings.Secure.getInt(paramContentResolver, "access_control_lock_enabled", 0))
        continue;
      try
      {
        ApplicationInfo localApplicationInfo2 = paramPackageManager.getApplicationInfo(paramString1, 0);
        localApplicationInfo1 = localApplicationInfo2;
        if ((localApplicationInfo1 == null) || ((0x80000000 & localApplicationInfo1.flags) != -2147483648) || (getInstance().checkAccessControlPass(paramString1)))
          continue;
        Intent localIntent = new Intent("android.app.action.CONFIRM_ACCESS_CONTROL");
        localIntent.putExtra("android.intent.extra.shortcut.NAME", paramString1);
        try
        {
          ActivityManagerNative.getDefault().startActivity(paramIApplicationThread, localIntent, localIntent.resolveTypeIfNeeded(paramContentResolver), null, 0, paramIBinder, paramString2, -1, true, false, null, null, false);
        }
        catch (RemoteException localRemoteException)
        {
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        while (true)
          ApplicationInfo localApplicationInfo1 = null;
      }
    }
  }

  public static ApnSetting decodeApnSetting(String paramString)
  {
    String[] arrayOfString = paramString.split("\\s*,\\s*");
    return new ApnSetting(Integer.parseInt(arrayOfString[0]), arrayOfString[1], arrayOfString[2], arrayOfString[3], arrayOfString[4], arrayOfString[5], arrayOfString[6], arrayOfString[7], arrayOfString[8], arrayOfString[9], arrayOfString[10], Integer.parseInt(arrayOfString[11]), arrayOfString[12].split("\\s*\\|\\s*"), arrayOfString[13], arrayOfString[14], Boolean.parseBoolean(arrayOfString[15]), Integer.parseInt(arrayOfString[16]));
  }

  public static String encodeApnSetting(ApnSetting paramApnSetting)
  {
    return paramApnSetting.id + ',' + paramApnSetting.numeric + ',' + paramApnSetting.carrier + ',' + paramApnSetting.apn + ',' + paramApnSetting.proxy + ',' + paramApnSetting.port + ',' + paramApnSetting.mmsc + ',' + paramApnSetting.mmsProxy + ',' + paramApnSetting.mmsPort + ',' + paramApnSetting.user + ',' + paramApnSetting.password + ',' + paramApnSetting.authType + ',' + TextUtils.join("|", paramApnSetting.types) + ',' + paramApnSetting.protocol + ',' + paramApnSetting.roamingProtocol + ',' + paramApnSetting.carrierEnabled + ',' + paramApnSetting.bearer;
  }

  private void ensureService()
  {
    if (this.mService == null)
    {
      monitorenter;
      try
      {
        this.mService = IFirewall.Stub.asInterface(ServiceManager.getService("miui.Firewall"));
        monitorexit;
      }
      finally
      {
        localObject = finally;
        monitorexit;
        throw localObject;
      }
    }
  }

  public static FirewallManager getInstance()
  {
    return sInstance;
  }

  public static boolean isAccessControlProtected(Context paramContext, String paramString)
  {
    int i = 0;
    try
    {
      ApplicationInfo localApplicationInfo = paramContext.getPackageManager().getApplicationInfo(paramString, 0);
      if ((localApplicationInfo != null) && ((0x80000000 & localApplicationInfo.flags) == -2147483648))
        i = 1;
      label33: return i;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      break label33;
    }
  }

  public void addAccessControlPass(String paramString)
  {
    try
    {
      ensureService();
      if (this.mService != null)
        this.mService.addAccessControlPass(paramString);
      label21: return;
    }
    catch (RemoteException localRemoteException)
    {
      break label21;
    }
  }

  public boolean checkAccessControlPass(String paramString)
  {
    int i = 0;
    try
    {
      ensureService();
      if (this.mService != null)
      {
        boolean bool = this.mService.checkAccessControlPass(paramString);
        i = bool;
      }
      label28: return i;
    }
    catch (RemoteException localRemoteException)
    {
      break label28;
    }
  }

  public void onDataConnected(int paramInt, String paramString1, String paramString2)
  {
    try
    {
      ensureService();
      if (this.mService != null)
        this.mService.onDataConnected(paramInt, paramString1, paramString2);
      label23: return;
    }
    catch (RemoteException localRemoteException)
    {
      break label23;
    }
  }

  public void onDataDisconnected(int paramInt, String paramString)
  {
    try
    {
      ensureService();
      if (this.mService != null)
        this.mService.onDataDisconnected(paramInt, paramString);
      label22: return;
    }
    catch (RemoteException localRemoteException)
    {
      break label22;
    }
  }

  public void onStartUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      ensureService();
      if (this.mService != null)
        this.mService.onStartUsingNetworkFeature(paramInt1, paramInt2, paramInt3);
      label23: return;
    }
    catch (RemoteException localRemoteException)
    {
      break label23;
    }
  }

  public void onStopUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      ensureService();
      if (this.mService != null)
        this.mService.onStopUsingNetworkFeature(paramInt1, paramInt2, paramInt3);
      label23: return;
    }
    catch (RemoteException localRemoteException)
    {
      break label23;
    }
  }

  public void removeAccessControlPass(String paramString)
  {
    try
    {
      ensureService();
      if (this.mService != null)
        this.mService.removeAccessControlPass(paramString);
      label21: return;
    }
    catch (RemoteException localRemoteException)
    {
      break label21;
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.net.FirewallManager
 * JD-Core Version:    0.6.0
 */