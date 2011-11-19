// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IIMSNetwork.java

package com.motorola.android.ims;

import android.os.*;

// Referenced classes of package com.motorola.android.ims:
//            IConnectionStateListener

public interface IIMSNetwork
    extends IInterface
{
    public static abstract class Stub extends Binder
        implements IIMSNetwork
    {
        private static class Proxy
            implements IIMSNetwork
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public long getImsRegId()
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                long l;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSNetwork");
                boolean flag = mRemote.transact(2, parcel, parcel1, 0);
                parcel1.readException();
                l = parcel1.readLong();
                long l1 = l;
                parcel1.recycle();
                parcel.recycle();
                return l1;
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public String getInterfaceDescriptor()
            {
                return "com.motorola.android.ims.IIMSNetwork";
            }

            public boolean isImsRegistered()
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                int i;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSNetwork");
                boolean flag = mRemote.transact(3, parcel, parcel1, 0);
                parcel1.readException();
                i = parcel1.readInt();
                boolean flag1;
                if(i != 0)
                    flag1 = true;
                else
                    flag1 = false;
                parcel1.recycle();
                parcel.recycle();
                return flag1;
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public boolean isLimitedAccessMode()
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                int i;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSNetwork");
                boolean flag = mRemote.transact(4, parcel, parcel1, 0);
                parcel1.readException();
                i = parcel1.readInt();
                boolean flag1;
                if(i != 0)
                    flag1 = true;
                else
                    flag1 = false;
                parcel1.recycle();
                parcel.recycle();
                return flag1;
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public void setListener(IConnectionStateListener iconnectionstatelistener, boolean flag)
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSNetwork");
                if(iconnectionstatelistener == null) goto _L2; else goto _L1
_L1:
                int i = iconnectionstatelistener.asBinder();
_L5:
                parcel.writeStrongBinder(i);
                if(!flag) goto _L4; else goto _L3
_L3:
                i = null;
_L6:
                parcel.writeInt(i);
                boolean flag1 = mRemote.transact(1, parcel, parcel1, 0);
                parcel1.readException();
                parcel1.recycle();
                parcel.recycle();
                return;
_L2:
                i = null;
                  goto _L5
_L4:
                i = null;
                  goto _L6
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
                  goto _L5
            }

            private IBinder mRemote;

            Proxy(IBinder ibinder)
            {
                mRemote = ibinder;
            }
        }


        public static IIMSNetwork asInterface(IBinder ibinder)
        {
            Object obj;
            if(ibinder == null)
            {
                obj = null;
            } else
            {
                IInterface iinterface = ibinder.queryLocalInterface("com.motorola.android.ims.IIMSNetwork");
                if(iinterface != null && (iinterface instanceof IIMSNetwork))
                    obj = (IIMSNetwork)iinterface;
                else
                    obj = new Proxy(ibinder);
            }
            return ((IIMSNetwork) (obj));
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            i;
            JVM INSTR lookupswitch 5: default 52
        //                       1: 78
        //                       2: 129
        //                       3: 157
        //                       4: 199
        //                       1598968902: 66;
               goto _L1 _L2 _L3 _L4 _L5 _L6
_L1:
            boolean flag = super.onTransact(i, parcel, parcel1, j);
_L8:
            return flag;
_L6:
            parcel1.writeString("com.motorola.android.ims.IIMSNetwork");
            flag = true;
            continue; /* Loop/switch isn't completed */
_L2:
            parcel.enforceInterface("com.motorola.android.ims.IIMSNetwork");
            IConnectionStateListener iconnectionstatelistener = IConnectionStateListener.Stub.asInterface(parcel.readStrongBinder());
            boolean flag1;
            if(parcel.readInt() != 0)
                flag1 = true;
            else
                flag1 = false;
            setListener(iconnectionstatelistener, flag1);
            parcel1.writeNoException();
            flag = true;
            continue; /* Loop/switch isn't completed */
_L3:
            parcel.enforceInterface("com.motorola.android.ims.IIMSNetwork");
            long l = getImsRegId();
            parcel1.writeNoException();
            parcel1.writeLong(l);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L4:
            parcel.enforceInterface("com.motorola.android.ims.IIMSNetwork");
            boolean flag2 = isImsRegistered();
            parcel1.writeNoException();
            int k;
            if(flag2)
                k = 1;
            else
                k = 0;
            parcel1.writeInt(k);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L5:
            parcel.enforceInterface("com.motorola.android.ims.IIMSNetwork");
            boolean flag3 = isLimitedAccessMode();
            parcel1.writeNoException();
            int i1;
            if(flag3)
                i1 = 1;
            else
                i1 = 0;
            parcel1.writeInt(i1);
            flag = true;
            if(true) goto _L8; else goto _L7
_L7:
        }

        private static final String DESCRIPTOR = "com.motorola.android.ims.IIMSNetwork";
        static final int TRANSACTION_getImsRegId = 2;
        static final int TRANSACTION_isImsRegistered = 3;
        static final int TRANSACTION_isLimitedAccessMode = 4;
        static final int TRANSACTION_setListener = 1;

        public Stub()
        {
            attachInterface(this, "com.motorola.android.ims.IIMSNetwork");
        }
    }


    public abstract long getImsRegId()
        throws RemoteException;

    public abstract boolean isImsRegistered()
        throws RemoteException;

    public abstract boolean isLimitedAccessMode()
        throws RemoteException;

    public abstract void setListener(IConnectionStateListener iconnectionstatelistener, boolean flag)
        throws RemoteException;
}
