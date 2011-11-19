// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IMSManager.java

package com.motorola.android.ims;

import android.content.*;
import android.os.Handler;
import android.util.Log;
import com.motorola.android.ims.service.smsonims.SmsOnImsService;
import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package com.motorola.android.ims:
//            IMSService, IIMSServer, IMSException, ConnectionStateListener, 
//            IIMSNetwork

public class IMSManager
{

    private IMSManager(Context context)
    {
        mRegId = 0L;
        mIMSNetwork = null;
        mIMSServer = null;
        mLimitedAccess = false;
        _cls1 _lcls1 = new _cls1();
        mIMSServerConnection = _lcls1;
        _cls2 _lcls2 = new _cls2();
        mHandler = _lcls2;
        mApplicationConnectionStateListener = null;
        _cls3 _lcls3 = new _cls3();
        mMgrConnectionStateListener = _lcls3;
        int i = Log.d(TAG, "IMSManager create");
        mContext = context;
        LinkedList linkedlist = new LinkedList();
        mIMSServices = linkedlist;
        mManager = this;
        mState = 1;
        Context context1 = mContext;
        Intent intent = new Intent("com.motorola.android.intent.action.IMSSERVER_START");
        ServiceConnection serviceconnection = mIMSServerConnection;
        boolean flag = context1.bindService(intent, serviceconnection, 1);
    }

    private int getLocationIfServiceExist(String s)
    {
        Iterator iterator = mIMSServices.iterator();
        int i = 0;
        boolean flag = false;
        do
        {
label0:
            {
                if(iterator.hasNext())
                {
                    String s1 = ((IMSService)iterator.next()).getType();
                    if(!s.equals(s1))
                        break label0;
                    flag = true;
                }
                int j;
                if(flag)
                    j = i;
                else
                    j = -1;
                return j;
            }
            i++;
        } while(true);
    }

    public static IMSManager getManager(Context context)
    {
        if(mManager == null)
            mManager = new IMSManager(context);
        return mManager;
    }

    private boolean isValidService(String s)
    {
        boolean flag;
        if("smsonims".equals(s))
        {
            flag = true;
        } else
        {
            String s1 = TAG;
            String s2 = (new StringBuilder()).append("this is not the valid service: ").append(s).toString();
            int i = Log.d(s1, s2);
            flag = false;
        }
        return flag;
    }

    private boolean retrieveNetworkServer()
    {
        if(mIMSServer != null) goto _L2; else goto _L1
_L1:
        boolean flag;
        int i = Log.e(TAG, "retrieveNetworkServer, IIMSServer.Stub.asInterface return null!");
        flag = false;
_L4:
        return flag;
_L2:
label0:
        {
            if(mIMSNetwork != null)
            {
                int j = Log.e(TAG, "retrieveNetworkServer, mIMSNetwork is not null");
                flag = true;
                continue; /* Loop/switch isn't completed */
            }
            try
            {
                IIMSNetwork iimsnetwork = IIMSNetwork.Stub.asInterface(mIMSServer.getNetworkService());
                mIMSNetwork = iimsnetwork;
                if(mIMSNetwork != null)
                    break label0;
                int k = Log.e(TAG, "retrieveNetworkServer, mIMSNetwork is null!");
            }
            catch(Exception exception)
            {
                int l = Log.e(TAG, "retrieveNetworkServer, failed to get the network service.", exception);
                flag = false;
                continue; /* Loop/switch isn't completed */
            }
            flag = false;
            continue; /* Loop/switch isn't completed */
        }
        flag = true;
        if(true) goto _L4; else goto _L3
_L3:
    }

    private void setState(int i)
    {
        mState = i;
    }

    public IMSService createService(String s)
        throws IMSException, IllegalArgumentException
    {
        int i = Log.v(TAG, "createService");
        int j = isValidService(s);
        if(1 == j)
        {
            int k = getLocationIfServiceExist(s);
            String s1 = TAG;
            String s2 = (new StringBuilder()).append("createService location ").append(k).toString();
            int l = Log.v(s1, s2);
            Object obj;
            String s6;
            String s7;
            int l1;
            String s8;
            if(-1 != k)
                obj = (IMSService)mIMSServices.get(k);
            else
            if(s.equals("smsonims"))
            {
                int i1 = Log.v(TAG, "createService create SmsOnImsService");
                SmsOnImsService smsonimsservice = new SmsOnImsService(s);
                if(smsonimsservice == null)
                {
                    int j1 = Log.e(TAG, "Failed to create the SmsOnImsService");
                    String s3 = (new StringBuilder()).append("Can't create the service: ").append(s).toString();
                    throw new IMSException(s3);
                }
                boolean flag = mIMSServices.add(smsonimsservice);
                String s4 = TAG;
                String s5 = (new StringBuilder()).append("createService this.mIMSServices.add ").append(smsonimsservice).toString();
                int k1 = Log.v(s4, s5);
                obj = smsonimsservice;
            } else
            {
                obj = null;
            }
            return ((IMSService) (obj));
        } else
        {
            s6 = TAG;
            s7 = (new StringBuilder()).append("Illegal argument: ").append(s).toString();
            l1 = Log.e(s6, s7);
            s8 = (new StringBuilder()).append("Only support smsonims service, the ").append(s).append(" is not supported yet.").toString();
            throw new IllegalArgumentException(s8);
        }
    }

    Handler getHandler()
    {
        return mHandler;
    }

    IIMSServer getIMSServer()
    {
        return mIMSServer;
    }

    public boolean getLimitedAccessMode()
    {
        return mLimitedAccess;
    }

    long getRegId()
    {
        return mRegId;
    }

    public int getState()
    {
        return mState;
    }

    public void onDestroy()
    {
        if(!mBind)
        {
            return;
        } else
        {
            Context context = mContext;
            ServiceConnection serviceconnection = mIMSServerConnection;
            context.unbindService(serviceconnection);
            mBind = false;
            return;
        }
    }

    void removeService(IMSService imsservice)
    {
        int j;
        try
        {
            boolean flag = mIMSServices.remove(imsservice);
            String s = TAG;
            String s1 = (new StringBuilder()).append("removeService ").append(imsservice).toString();
            int i = Log.v(s, s1);
            return;
        }
        catch(UnsupportedOperationException unsupportedoperationexception)
        {
            j = Log.e(TAG, "Failed to remove the service from the list", unsupportedoperationexception);
        }
    }

    public void setListener(ConnectionStateListener connectionstatelistener)
    {
        int i = Log.d(TAG, "setListener, mApplicationConnectionStateListener");
        mApplicationConnectionStateListener = connectionstatelistener;
    }

    public static final String EXTRA_CODE = "Code";
    public static final String EXTRA_REREG = "reReg";
    public static final int IMS_CONNECTED = 0;
    public static final int IMS_DISCONNECTED = 1;
    public static final String IMS_REGISTRATION_STATUS = "com.motorola.ims.action.registration_status";
    public static final int IMS_REG_FAIL_403 = 3;
    public static final int IMS_REG_FAIL_404 = 4;
    public static final int IMS_REG_FAIL_LOCALIP_INVALID = 5;
    public static final int IMS_REG_GENERAL_FAIL = 2;
    public static final int STATE_OFFLINE = 1;
    public static final int STATE_ONLINE = 2;
    private static final String TAG = com/motorola/android/ims/IMSManager.getSimpleName();
    private static IMSManager mManager;
    private ConnectionStateListener mApplicationConnectionStateListener;
    private boolean mBind;
    private Context mContext;
    private Handler mHandler;
    protected IIMSNetwork mIMSNetwork;
    protected IIMSServer mIMSServer;
    private ServiceConnection mIMSServerConnection;
    protected LinkedList mIMSServices;
    private boolean mLimitedAccess;
    private ConnectionStateListener mMgrConnectionStateListener;
    private long mRegId;
    protected int mState;




/*
    static boolean access$102(IMSManager imsmanager, boolean flag)
    {
        imsmanager.mBind = flag;
        return flag;
    }

*/





/*
    static long access$402(IMSManager imsmanager, long l)
    {
        imsmanager.mRegId = l;
        return l;
    }

*/



/*
    static boolean access$502(IMSManager imsmanager, boolean flag)
    {
        imsmanager.mLimitedAccess = flag;
        return flag;
    }

*/



    private class _cls1
        implements ServiceConnection
    {

        public void onServiceConnected(ComponentName componentname, IBinder ibinder)
        {
            int i = Log.d(IMSManager.TAG, "onServiceConnected");
            boolean flag = mBind = 1;
            IMSManager imsmanager = IMSManager.this;
            IIMSServer iimsserver = IIMSServer.Stub.asInterface(ibinder);
            imsmanager.mIMSServer = iimsserver;
            int j = retrieveNetworkServer();
            if(1 != j)
                break MISSING_BLOCK_LABEL_398;
            if(mIMSNetwork.isImsRegistered() != 1) goto _L2; else goto _L1
_L1:
            setState(2);
            IMSManager imsmanager1 = IMSManager.this;
            long l = mIMSNetwork.getImsRegId();
            long l1 = imsmanager1.mRegId = l;
            String s = IMSManager.TAG;
            StringBuilder stringbuilder = (new StringBuilder()).append("getImsRegId(): ");
            long l2 = mRegId;
            String s1 = stringbuilder.append(l2).toString();
            int k = Log.d(s, s1);
            IMSManager imsmanager2 = IMSManager.this;
            boolean flag1 = mIMSNetwork.isLimitedAccessMode();
            boolean flag2 = imsmanager2.mLimitedAccess = flag1;
            String s2 = IMSManager.TAG;
            StringBuilder stringbuilder1 = (new StringBuilder()).append("mLimitedAccess: ");
            boolean flag3 = mLimitedAccess;
            String s3 = stringbuilder1.append(flag3).toString();
            int i1 = Log.d(s2, s3);
_L3:
            if(mApplicationConnectionStateListener != null)
            {
                ConnectionStateListener connectionstatelistener = mApplicationConnectionStateListener;
                boolean flag4 = mLimitedAccess;
                connectionstatelistener.processImsConnected(flag4);
            }
_L5:
            IIMSNetwork iimsnetwork = mIMSNetwork;
            IConnectionStateListener iconnectionstatelistener = mMgrConnectionStateListener.mCallback;
            iimsnetwork.setListener(iconnectionstatelistener, true);
_L4:
            String s4 = IMSManager.TAG;
            StringBuilder stringbuilder2 = (new StringBuilder()).append("onServiceConnected, mState: ");
            int j1 = mState;
            String s5 = stringbuilder2.append(j1).toString();
            int k1 = Log.i(s4, s5);
            return;
            RemoteException remoteexception;
            remoteexception;
            int i2 = Log.e(IMSManager.TAG, "Failed to getImsRegId", remoteexception);
              goto _L3
            RemoteException remoteexception1;
            remoteexception1;
            int j2 = Log.e(IMSManager.TAG, "onServiceConnected RemoteException", remoteexception1);
              goto _L4
_L2:
            mState = 1;
              goto _L5
            int k2 = Log.e(IMSManager.TAG, "onServiceConnected: retrieveNetworkServer fail");
              goto _L4
        }

        public void onServiceDisconnected(ComponentName componentname)
        {
            int i = Log.d(IMSManager.TAG, "onServiceDisconnected");
            boolean flag = mBind = 0;
            mIMSServer = null;
            mIMSNetwork = null;
        }

        final IMSManager this$0;

        _cls1()
        {
            this$0 = IMSManager.this;
            super();
        }
    }


    private class _cls2 extends Handler
    {

        public void handleMessage(Message message)
        {
            String s = IMSManager.TAG;
            StringBuilder stringbuilder = (new StringBuilder()).append("on handleMessage with ");
            int i = message.what;
            String s1 = stringbuilder.append(i).append(" event.").toString();
            int j = Log.d(s, s1);
            IMSService imsservice;
            switch(message.what)
            {
            default:
                return;

            case 1: // '\001'
                imsservice = (IMSService)message.obj;
                break;
            }
            int k = mState;
            if(1 == k)
            {
                Handler handler = imsservice.getHandler();
                Boolean boolean1 = Boolean.valueOf(false);
                Message.obtain(handler, 2, boolean1).sendToTarget();
                return;
            } else
            {
                Handler handler1 = imsservice.getHandler();
                Boolean boolean2 = Boolean.valueOf(true);
                Message.obtain(handler1, 2, boolean2).sendToTarget();
                return;
            }
        }

        final IMSManager this$0;

        _cls2()
        {
            this$0 = IMSManager.this;
            super();
        }
    }


    private class _cls3 extends ConnectionStateListener
    {

        public void processImsConnected(boolean flag)
        {
            int i = Log.v(IMSManager.TAG, "on mMgrConnectionStateListener.processImsConnected");
            setState(2);
            int i1;
            try
            {
                IMSManager imsmanager = IMSManager.this;
                long l = mIMSNetwork.getImsRegId();
                long l1 = imsmanager.mRegId = l;
                String s = IMSManager.TAG;
                StringBuilder stringbuilder = (new StringBuilder()).append("getImsRegId(): ");
                long l2 = mRegId;
                String s1 = stringbuilder.append(l2).toString();
                int j = Log.d(s, s1);
                boolean flag1 = mLimitedAccess = flag;
                String s2 = IMSManager.TAG;
                StringBuilder stringbuilder1 = (new StringBuilder()).append("mLimitedAccess: ");
                boolean flag2 = mLimitedAccess;
                String s3 = stringbuilder1.append(flag2).toString();
                int k = Log.d(s2, s3);
            }
            catch(RemoteException remoteexception)
            {
                i1 = Log.e(IMSManager.TAG, "Failed to getImsRegId", remoteexception);
            }
            if(mApplicationConnectionStateListener == null)
            {
                return;
            } else
            {
                ConnectionStateListener connectionstatelistener = mApplicationConnectionStateListener;
                boolean flag3 = mLimitedAccess;
                connectionstatelistener.processImsConnected(flag3);
                return;
            }
        }

        public void processImsDisconnected()
        {
            int i = Log.v(IMSManager.TAG, "on mMgrConnectionStateListener.processImsDisconnected");
            setState(1);
            boolean flag = mLimitedAccess = 0;
            Handler handler;
            Boolean boolean1;
            for(Iterator iterator = mIMSServices.iterator(); iterator.hasNext(); Message.obtain(handler, 2, boolean1).sendToTarget())
            {
                handler = ((IMSService)iterator.next()).getHandler();
                boolean1 = Boolean.valueOf(false);
            }

            if(mApplicationConnectionStateListener == null)
            {
                return;
            } else
            {
                mApplicationConnectionStateListener.processImsDisconnected();
                return;
            }
        }

        final IMSManager this$0;

        _cls3()
        {
            this$0 = IMSManager.this;
            super();
        }
    }

}
