// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IMSService.java

package com.motorola.android.ims;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

// Referenced classes of package com.motorola.android.ims:
//            IMSManager, IMSServiceListener, IIMSServer, IMSException, 
//            IllegalStateException, IIMSCSms

public abstract class IMSService
{

    public IMSService(String s)
    {
        _cls1 _lcls1 = new _cls1();
        mHandler = _lcls1;
        serviceType = s;
        mState = 0;
        IMSManager imsmanager = IMSManager.getManager(null);
        mManager = imsmanager;
    }

    private void selfClose()
    {
        mState;
        JVM INSTR tableswitch 0 2: default 32
    //                   0 66
    //                   1 58
    //                   2 58;
           goto _L1 _L2 _L3 _L3
_L1:
        removeCallBack();
        mState = 4;
        if(mServiceListener == null)
        {
            return;
        } else
        {
            mServiceListener.processServiceClosed(this);
            return;
        }
_L3:
        mState = 3;
        continue; /* Loop/switch isn't completed */
_L2:
        mState = 4;
        if(true) goto _L1; else goto _L4
_L4:
    }

    protected abstract void addCallBack();

    public void close()
    {
        selfClose();
        mManager.removeService(this);
        mServiceListener = null;
    }

    protected abstract String getFeatureTags();

    Handler getHandler()
    {
        return mHandler;
    }

    protected long getRegId()
    {
        return mManager.getRegId();
    }

    protected IIMSCSms getSMSServer()
    {
        IIMSCSms iimscsms;
        iimscsms = null;
        if(mManager == null)
            break MISSING_BLOCK_LABEL_37;
        IIMSCSms iimscsms1;
        if(mManager.getIMSServer() == null)
            break MISSING_BLOCK_LABEL_37;
        iimscsms1 = IIMSCSms.Stub.asInterface(mManager.getIMSServer().getSMSService());
        iimscsms = iimscsms1;
_L2:
        return iimscsms;
        Exception exception;
        exception;
        int i = Log.e("IMSService", "failed to get the network service.", exception);
        if(true) goto _L2; else goto _L1
_L1:
    }

    public int getState()
    {
        return mState;
    }

    public String getType()
    {
        return serviceType;
    }

    public void open()
        throws IMSException, IllegalStateException
    {
        int i = Log.v("IMSService", "open");
        if(mState != 0 && mState != 4)
        {
            StringBuilder stringbuilder = (new StringBuilder()).append("The service is in ");
            int j = mState;
            String s = stringbuilder.append(j).append(" state, not in INIT state.").toString();
            throw new IllegalStateException(s);
        } else
        {
            Message.obtain(mManager.getHandler(), 1, this).sendToTarget();
            mState = 1;
            return;
        }
    }

    protected abstract void removeCallBack();

    public void setListener(IMSServiceListener imsservicelistener)
    {
        mServiceListener = imsservicelistener;
    }

    static final int MESSAGE_CONN_STATE = 1;
    static final int MESSAGE_SERVICE_STATE = 2;
    public static final String SERVICE_TYPE_SMSONIMS = "smsonims";
    public static final int STATE_CLOSED = 4;
    public static final int STATE_CLOSING = 3;
    public static final int STATE_INIT = 0;
    public static final int STATE_OPEN = 2;
    public static final int STATE_OPENING = 1;
    private static final String TAG = "IMSService";
    private Handler mHandler;
    protected IMSManager mManager;
    protected IMSService mService;
    protected IMSServiceListener mServiceListener;
    protected int mState;
    protected String serviceType;


    private class _cls1 extends Handler
    {

        public void handleMessage(Message message)
        {
            StringBuilder stringbuilder = (new StringBuilder()).append("on handleMessage with ");
            int i = message.what;
            String s = stringbuilder.append(i).append(" event").toString();
            int j = Log.e("IMSService", s);
            message.what;
            JVM INSTR tableswitch 2 2: default 64
        //                       2 65;
               goto _L1 _L2
_L1:
            return;
_L2:
            boolean flag = ((Boolean)message.obj).booleanValue();
            StringBuilder stringbuilder1 = (new StringBuilder()).append("Current service state is ");
            int k = mState;
            String s1 = stringbuilder1.append(k).append(" The connection state is ").append(flag).toString();
            int l = Log.d("IMSService", s1);
            if(1 != flag)
                break; /* Loop/switch isn't completed */
            switch(mState)
            {
            default:
                return;

            case 1: // '\001'
                mState = 2;
                addCallBack();
                if(mServiceListener == null)
                {
                    return;
                } else
                {
                    IMSServiceListener imsservicelistener = mServiceListener;
                    IMSService imsservice = mService;
                    imsservicelistener.processServiceOpen(imsservice, true);
                    return;
                }

            case 3: // '\003'
                selfClose();
                return;

            case 0: // '\0'
            case 2: // '\002'
            case 4: // '\004'
                break;
            }
            if(true) goto _L1; else goto _L3
_L3:
            switch(mState)
            {
            default:
                return;

            case 1: // '\001'
                mState = 4;
                if(mServiceListener == null)
                {
                    return;
                } else
                {
                    IMSServiceListener imsservicelistener1 = mServiceListener;
                    IMSService imsservice1 = mService;
                    imsservicelistener1.processServiceOpen(imsservice1, false);
                    return;
                }

            case 2: // '\002'
            case 3: // '\003'
                selfClose();
                return;

            case 0: // '\0'
            case 4: // '\004'
                break;
            }
            if(true) goto _L1; else goto _L4
_L4:
        }

        final IMSService this$0;

        _cls1()
        {
            this$0 = IMSService.this;
            super();
        }
    }

}
