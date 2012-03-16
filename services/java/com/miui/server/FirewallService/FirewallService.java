package com.miui.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.ServiceManager;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.ApnSetting;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import miui.net.FirewallManager;
import miui.net.IFirewall.Stub;
import miui.util.CommandLineUtils;

public class FirewallService extends IFirewall.Stub
{
  private static final String ADD_CHAIN_FOR_REJECT_CMD;
  private static final String AWK_CMD;
  private static final String CLEAR_ALL_MIUI_CHAIN_CMD;
  private static final boolean DEBUG = false;
  private static final String GREP_CMD;
  private static final String INSERT_ACCEPT_RULE_TO_CHAIN_CMD;
  private static final String IPTABLES_CMD = getIptablesCommand();
  private static final String LOG_TAG = "FirewallService";
  private static final String MIUI_CHAIN_PREFIX = "miui_";
  private static final String REMOVE_CHAIN_CMD;
  private static final String REMOVE_RULE_OF_CHAIN_CMD;
  private final HashSet<String> mAccessControlPassPackages = new HashSet();
  private Context mContext;
  private String mCurrentMmsIfname = null;
  private HashMap<String, String> mIfnames = new HashMap();
  private HashMap<Integer, HashSet<Integer>> mLastUsingMmsUids = new HashMap();

  static
  {
    AWK_CMD = getAwkCommand();
    GREP_CMD = getGrepCommand();
    CLEAR_ALL_MIUI_CHAIN_CMD = "for chain in `" + IPTABLES_CMD + " -L | " + GREP_CMD + " \"^Chain " + "miui_" + "\" | " + AWK_CMD + " '{print $2}'`; do " + IPTABLES_CMD + " -D OUTPUT `" + IPTABLES_CMD + " -S OUTPUT | " + AWK_CMD + " -v chain=${chain} '$6==chain {print NR-2}'`; " + IPTABLES_CMD + " -F $chain; " + IPTABLES_CMD + " -X $chain; " + "done;";
    REMOVE_CHAIN_CMD = IPTABLES_CMD + " -D OUTPUT `" + IPTABLES_CMD + " -S OUTPUT | " + AWK_CMD + " '$6==\"%0%\" {print NR-1}'`; " + IPTABLES_CMD + " -F %0%; " + IPTABLES_CMD + " -X %0%; ";
    ADD_CHAIN_FOR_REJECT_CMD = IPTABLES_CMD + " -N %0%; " + IPTABLES_CMD + " -A %0% -j REJECT; " + IPTABLES_CMD + " -A OUTPUT -o %1% -j %0%; ";
    INSERT_ACCEPT_RULE_TO_CHAIN_CMD = IPTABLES_CMD + " -I %0% -m owner --uid-owner %1% -j ACCEPT; ";
    REMOVE_RULE_OF_CHAIN_CMD = IPTABLES_CMD + " -D %0% `" + IPTABLES_CMD + " -S %0% | " + AWK_CMD + " '$6==\"%1%\" {print NR-1}'`; ";
  }

  private FirewallService(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private String addQuoteMark(String paramString)
  {
    if ((paramString.startsWith("\"")) && (paramString.endsWith("\"")));
    while (true)
    {
      return paramString;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append('"');
      int i = paramString.length();
      int j = 0;
      if (j < i)
      {
        char c = paramString.charAt(j);
        switch (c)
        {
        default:
          localStringBuilder.append(c);
        case '"':
        case '\\':
        case '$':
        }
        while (true)
        {
          j++;
          break;
          localStringBuilder.append("\\\"");
          continue;
          localStringBuilder.append("\\\\");
          continue;
          localStringBuilder.append("\\$");
        }
      }
      localStringBuilder.append('"');
      paramString = localStringBuilder.toString();
    }
  }

  private void addWhiteListChain(String paramString1, String paramString2, Iterable<Integer> paramIterable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(REMOVE_CHAIN_CMD.replaceAll("%0%", paramString1));
    localStringBuilder.append(ADD_CHAIN_FOR_REJECT_CMD.replaceAll("%0%", paramString1).replaceAll("%1%", paramString2));
    if (paramIterable != null)
    {
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
      {
        int i = ((Integer)localIterator.next()).intValue();
        localStringBuilder.append(INSERT_ACCEPT_RULE_TO_CHAIN_CMD.replaceAll("%0%", paramString1).replaceAll("%1%", String.valueOf(i)));
      }
    }
    runCommands(localStringBuilder.toString());
  }

  private void clearChains()
  {
    runCommands(CLEAR_ALL_MIUI_CHAIN_CMD);
  }

  private static String getAwkCommand()
  {
    String str;
    if (hasCommand("awk"))
      str = "awk";
    while (true)
    {
      return str;
      if (hasCommand("busybox"))
      {
        str = "busybox awk";
        continue;
      }
      str = null;
    }
  }

  private static String getGrepCommand()
  {
    String str;
    if (hasCommand("grep"))
      str = "grep";
    while (true)
    {
      return str;
      if (hasCommand("busybox"))
      {
        str = "busybox grep";
        continue;
      }
      str = null;
    }
  }

  private static String getIptablesCommand()
  {
    if (hasCommand("iptables"));
    for (String str = "iptables"; ; str = null)
      return str;
  }

  private static boolean hasCommand(String paramString)
  {
    if ((new File("/system/xbin/", paramString).exists()) || (new File("/system/bin/", paramString).exists()));
    for (int i = 1; ; i = 0)
      return i;
  }

  private void insertWhiteListRule(String paramString, int paramInt)
  {
    runCommands(INSERT_ACCEPT_RULE_TO_CHAIN_CMD.replaceAll("%0%", paramString).replaceAll("%1%", String.valueOf(paramInt)));
  }

  private void removeChain(String paramString)
  {
    runCommands(REMOVE_CHAIN_CMD.replaceAll("%0%", paramString));
  }

  private void removeRule(String paramString, int paramInt)
  {
    runCommands(REMOVE_RULE_OF_CHAIN_CMD.replaceAll("%0%", paramString).replaceAll("%1%", String.valueOf(paramInt)));
  }

  private void runCommands(String paramString)
  {
    CommandLineUtils.run("root", "sh -c " + addQuoteMark(paramString), new Object[0]);
  }

  public static void setupService(Context paramContext)
  {
    if ((IPTABLES_CMD != null) && (GREP_CMD != null) && (AWK_CMD != null))
    {
      FirewallService localFirewallService = new FirewallService(paramContext);
      localFirewallService.clearChains();
      ServiceManager.addService("miui.Firewall", localFirewallService.asBinder());
    }
    while (true)
    {
      return;
      Log.e("FirewallService", "failed to setup service due to iptables=" + IPTABLES_CMD + ",grep=" + GREP_CMD + ",awk=" + AWK_CMD);
    }
  }

  public void addAccessControlPass(String paramString)
  {
    synchronized (this.mAccessControlPassPackages)
    {
      this.mAccessControlPassPackages.add(paramString);
      return;
    }
  }

  public boolean checkAccessControlPass(String paramString)
  {
    synchronized (this.mAccessControlPassPackages)
    {
      boolean bool = this.mAccessControlPassPackages.contains(paramString);
      return bool;
    }
  }

  public void onDataConnected(int paramInt, String paramString1, String paramString2)
  {
    this.mIfnames.put(paramString1, paramString2);
    if ((ConnectivityManager.isNetworkTypeMobile(paramInt)) && (Settings.Secure.getInt(this.mContext.getContentResolver(), "mobile_data", 1) == 0) && (FirewallManager.decodeApnSetting(paramString1).canHandleType("mms")))
    {
      this.mCurrentMmsIfname = paramString2;
      addWhiteListChain("miui_" + this.mCurrentMmsIfname, this.mCurrentMmsIfname, this.mLastUsingMmsUids.keySet());
    }
  }

  public void onDataDisconnected(int paramInt, String paramString)
  {
    String str = (String)this.mIfnames.remove(paramString);
    if (!TextUtils.isEmpty(str))
    {
      removeChain("miui_" + str);
      if (str.equals(this.mCurrentMmsIfname))
      {
        this.mCurrentMmsIfname = null;
        this.mLastUsingMmsUids.clear();
      }
    }
  }

  public void onStartUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 2)
    {
      HashSet localHashSet = (HashSet)this.mLastUsingMmsUids.get(Integer.valueOf(paramInt1));
      int i = 0;
      if (localHashSet == null)
      {
        i = 1;
        localHashSet = new HashSet();
        this.mLastUsingMmsUids.put(Integer.valueOf(paramInt1), localHashSet);
      }
      localHashSet.add(Integer.valueOf(paramInt2));
      if ((i != 0) && (!TextUtils.isEmpty(this.mCurrentMmsIfname)))
        insertWhiteListRule("miui_" + this.mCurrentMmsIfname, paramInt1);
    }
  }

  public void onStopUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 2)
    {
      HashSet localHashSet = (HashSet)this.mLastUsingMmsUids.get(Integer.valueOf(paramInt1));
      if (localHashSet != null)
      {
        localHashSet.remove(Integer.valueOf(paramInt2));
        if (localHashSet.size() == 0)
        {
          this.mLastUsingMmsUids.remove(Integer.valueOf(paramInt1));
          localHashSet = null;
        }
      }
      if ((localHashSet == null) && (!TextUtils.isEmpty(this.mCurrentMmsIfname)))
        removeRule("miui_" + this.mCurrentMmsIfname, paramInt1);
    }
  }

  public void removeAccessControlPass(String paramString)
  {
    synchronized (this.mAccessControlPassPackages)
    {
      if ("*".equals(paramString))
      {
        this.mAccessControlPassPackages.clear();
        return;
      }
      this.mAccessControlPassPackages.remove(paramString);
    }
  }
}

/* Location:           /home/dhacker29/miui/framework_dex2jar.jar
 * Qualified Name:     com.miui.server.FirewallService
 * JD-Core Version:    0.6.0
 */