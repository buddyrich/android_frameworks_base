// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IIMSCSmsCallback.java

package com.motorola.android.ims;

import android.os.*;

public interface IIMSCSmsCallback
    extends IInterface
{
    public static abstract class Stub extends Binder
        implements IIMSCSmsCallback
    {
        private static class Proxy
            implements IIMSCSmsCallback
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public String getInterfaceDescriptor()
            {
                return "com.motorola.android.ims.IIMSCSmsCallback";
            }

            public long getRegistrationId()
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                long l;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSmsCallback");
                boolean flag = mRemote.transact(3, parcel, parcel1, 0);
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

            public void onMessageReceived(long l, long l1, String s, int i, byte abyte0[])
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSmsCallback");
                parcel.writeLong(l);
                parcel.writeLong(l1);
                parcel.writeString(s);
                parcel.writeInt(i);
                parcel.writeByteArray(abyte0);
                boolean flag = mRemote.transact(1, parcel, parcel1, 0);
                parcel1.readException();
                parcel1.recycle();
                parcel.recycle();
                return;
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public void onMessageResponse(long l, long l1, int i, byte abyte0[])
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSmsCallback");
                parcel.writeLong(l);
                parcel.writeLong(l1);
                parcel.writeInt(i);
                parcel.writeByteArray(abyte0);
                boolean flag = mRemote.transact(2, parcel, parcel1, 0);
                parcel1.readException();
                parcel1.recycle();
                parcel.recycle();
                return;
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


        public static IIMSCSmsCallback asInterface(IBinder ibinder)
        {
            Object obj;
            if(ibinder == null)
            {
                obj = null;
            } else
            {
                IInterface iinterface = ibinder.queryLocalInterface("com.motorola.android.ims.IIMSCSmsCallback");
                if(iinterface != null && (iinterface instanceof IIMSCSmsCallback))
                    obj = (IIMSCSmsCallback)iinterface;
                else
                    obj = new Proxy(ibinder);
            }
            return ((IIMSCSmsCallback) (obj));
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            i;
            JVM INSTR lookupswitch 4: default 44
        //                       1: 70
        //                       2: 130
        //                       3: 182
        //                       1598968902: 58;
               goto _L1 _L2 _L3 _L4 _L5
_L1:
            boolean flag = super.onTransact(i, parcel, parcel1, j);
_L7:
            return flag;
_L5:
            parcel1.writeString("com.motorola.android.ims.IIMSCSmsCallback");
            flag = true;
            continue; /* Loop/switch isn't completed */
_L2:
            parcel.enforceInterface("com.motorola.android.ims.IIMSCSmsCallback");
            long l = parcel.readLong();
            long l1 = parcel.readLong();
            String s = parcel.readString();
            int k = parcel.readInt();
            byte abyte0[] = parcel.createByteArray();
            onMessageReceived(l, l1, s, k, abyte0);
            parcel1.writeNoException();
            flag = true;
            continue; /* Loop/switch isn't completed */
_L3:
            parcel.enforceInterface("com.motorola.android.ims.IIMSCSmsCallback");
            long l2 = parcel.readLong();
            long l3 = parcel.readLong();
            int i1 = parcel.readInt();
            byte abyte1[] = parcel.createByteArray();
            onMessageResponse(l2, l3, i1, abyte1);
            parcel1.writeNoException();
            flag = true;
            continue; /* Loop/switch isn't completed */
_L4:
            parcel.enforceInterface("com.motorola.android.ims.IIMSCSmsCallback");
            long l4 = getRegistrationId();
            parcel1.writeNoException();
            parcel1.writeLong(l4);
            flag = true;
            if(true) goto _L7; else goto _L6
_L6:
        }

        private static final String DESCRIPTOR = "com.motorola.android.ims.IIMSCSmsCallback";
        static final int TRANSACTION_getRegistrationId = 3;
        static final int TRANSACTION_onMessageReceived = 1;
        static final int TRANSACTION_onMessageResponse = 2;

        public Stub()
        {
            attachInterface(this, "com.motorola.android.ims.IIMSCSmsCallback");
        }
    }


    public abstract long getRegistrationId()
        throws RemoteException;

    public abstract void onMessageReceived(long l, long l1, String s, int i, byte abyte0[])
        throws RemoteException;

    public abstract void onMessageResponse(long l, long l1, int i, byte abyte0[])
        throws RemoteException;
}
