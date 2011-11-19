// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IIMSServer.java

package com.motorola.android.ims;

import android.os.*;

public interface IIMSServer
    extends IInterface
{
    public static abstract class Stub extends Binder
        implements IIMSServer
    {
        private static class Proxy
            implements IIMSServer
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public String getInterfaceDescriptor()
            {
                return "com.motorola.android.ims.IIMSServer";
            }

            public IBinder getNetworkService()
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                IBinder ibinder;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSServer");
                boolean flag = mRemote.transact(1, parcel, parcel1, 0);
                parcel1.readException();
                ibinder = parcel1.readStrongBinder();
                IBinder ibinder1 = ibinder;
                parcel1.recycle();
                parcel.recycle();
                return ibinder1;
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public IBinder getSMSService()
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                IBinder ibinder;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSServer");
                boolean flag = mRemote.transact(2, parcel, parcel1, 0);
                parcel1.readException();
                ibinder = parcel1.readStrongBinder();
                IBinder ibinder1 = ibinder;
                parcel1.recycle();
                parcel.recycle();
                return ibinder1;
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


        public static IIMSServer asInterface(IBinder ibinder)
        {
            Object obj;
            if(ibinder == null)
            {
                obj = null;
            } else
            {
                IInterface iinterface = ibinder.queryLocalInterface("com.motorola.android.ims.IIMSServer");
                if(iinterface != null && (iinterface instanceof IIMSServer))
                    obj = (IIMSServer)iinterface;
                else
                    obj = new Proxy(ibinder);
            }
            return ((IIMSServer) (obj));
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            i;
            JVM INSTR lookupswitch 3: default 36
        //                       1: 62
        //                       2: 90
        //                       1598968902: 50;
               goto _L1 _L2 _L3 _L4
_L1:
            boolean flag = super.onTransact(i, parcel, parcel1, j);
_L6:
            return flag;
_L4:
            parcel1.writeString("com.motorola.android.ims.IIMSServer");
            flag = true;
            continue; /* Loop/switch isn't completed */
_L2:
            parcel.enforceInterface("com.motorola.android.ims.IIMSServer");
            IBinder ibinder = getNetworkService();
            parcel1.writeNoException();
            parcel1.writeStrongBinder(ibinder);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L3:
            parcel.enforceInterface("com.motorola.android.ims.IIMSServer");
            IBinder ibinder1 = getSMSService();
            parcel1.writeNoException();
            parcel1.writeStrongBinder(ibinder1);
            flag = true;
            if(true) goto _L6; else goto _L5
_L5:
        }

        private static final String DESCRIPTOR = "com.motorola.android.ims.IIMSServer";
        static final int TRANSACTION_getNetworkService = 1;
        static final int TRANSACTION_getSMSService = 2;

        public Stub()
        {
            attachInterface(this, "com.motorola.android.ims.IIMSServer");
        }
    }


    public abstract IBinder getNetworkService()
        throws RemoteException;

    public abstract IBinder getSMSService()
        throws RemoteException;
}
