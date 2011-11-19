// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConnectionStateListener.java

package com.motorola.android.ims;

import android.os.Handler;

// Referenced classes of package com.motorola.android.ims:
//            IConnectionStateListener

public class ConnectionStateListener
{

    public ConnectionStateListener()
    {
        _cls1 _lcls1 = new _cls1();
        mCallback = _lcls1;
        _cls2 _lcls2 = new _cls2();
        mHandler = _lcls2;
    }

    public void processImsConnected(boolean flag)
    {
    }

    public void processImsDisconnected()
    {
    }

    protected static final int LISTEN_CONNECTION_STATE = 1;
    protected IConnectionStateListener mCallback;
    protected Handler mHandler;

    private class _cls1 extends IConnectionStateListener.Stub
    {

        public void onConnectionStateChanged(int i, boolean flag)
        {
            Handler handler = mHandler;
            int j;
            Integer integer;
            if(flag)
                j = 1;
            else
                j = 0;
            integer = Integer.valueOf(i);
            Message.obtain(handler, 1, j, 0, integer).sendToTarget();
        }

        final ConnectionStateListener this$0;

        _cls1()
        {
            this$0 = ConnectionStateListener.this;
            super();
        }
    }


    private class _cls2 extends Handler
    {

        public void handleMessage(Message message)
        {
            int i = ((Integer)message.obj).intValue();
            switch(message.what)
            {
            default:
                return;

            case 1: // '\001'
                break;
            }
            if(2 == i)
            {
                ConnectionStateListener connectionstatelistener = ConnectionStateListener.this;
                boolean flag;
                if(message.arg1 == 1)
                    flag = true;
                else
                    flag = false;
                connectionstatelistener.processImsConnected(flag);
                return;
            } else
            {
                processImsDisconnected();
                return;
            }
        }

        final ConnectionStateListener this$0;

        _cls2()
        {
            this$0 = ConnectionStateListener.this;
            super();
        }
    }

}
