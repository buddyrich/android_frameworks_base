// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IConnectionStateListener.java

package com.motorola.android.ims;

import android.os.*;

public interface IConnectionStateListener
    extends IInterface
{
    public static abstract class Stub extends Binder
        implements IConnectionStateListener
    {
        private static class Proxy
            implements IConnectionStateListener
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public String getInterfaceDescriptor()
            {
                return "com.motorola.android.ims.IConnectionStateListener";
            }

            public void onConnectionStateChanged(int i, boolean flag)
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                int j;
                parcel.writeInterfaceToken("com.motorola.android.ims.IConnectionStateListener");
                parcel.writeInt(i);
                if(!flag)
                    break MISSING_BLOCK_LABEL_64;
                j = 1;
_L1:
                parcel.writeInt(j);
                boolean flag1 = mRemote.transact(1, parcel, parcel1, 0);
                parcel1.readException();
                parcel1.recycle();
                parcel.recycle();
                return;
                j = 0;
                  goto _L1
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            private IBinder mRemote;

            Proxy(IBinder ibinder)
            {
                mRemote = ibinder;
            }
        }


        public static IConnectionStateListener asInterface(IBinder ibinder)
        {
            Object obj;
            if(ibinder == null)
            {
                obj = null;
            } else
            {
                IInterface iinterface = ibinder.queryLocalInterface("com.motorola.android.ims.IConnectionStateListener");
                if(iinterface != null && (iinterface instanceof IConnectionStateListener))
                    obj = (IConnectionStateListener)iinterface;
                else
                    obj = new Proxy(ibinder);
            }
            return ((IConnectionStateListener) (obj));
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            i;
            JVM INSTR lookupswitch 2: default 28
        //                       1: 54
        //                       1598968902: 42;
               goto _L1 _L2 _L3
_L1:
            boolean flag = super.onTransact(i, parcel, parcel1, j);
_L5:
            return flag;
_L3:
            parcel1.writeString("com.motorola.android.ims.IConnectionStateListener");
            flag = true;
            continue; /* Loop/switch isn't completed */
_L2:
            parcel.enforceInterface("com.motorola.android.ims.IConnectionStateListener");
            int k = parcel.readInt();
            boolean flag1;
            if(parcel.readInt() != 0)
                flag1 = true;
            else
                flag1 = false;
            onConnectionStateChanged(k, flag1);
            parcel1.writeNoException();
            flag = true;
            if(true) goto _L5; else goto _L4
_L4:
        }

        private static final String DESCRIPTOR = "com.motorola.android.ims.IConnectionStateListener";
        static final int TRANSACTION_onConnectionStateChanged = 1;

        public Stub()
        {
            attachInterface(this, "com.motorola.android.ims.IConnectionStateListener");
        }
    }


    public abstract void onConnectionStateChanged(int i, boolean flag)
        throws RemoteException;
}
