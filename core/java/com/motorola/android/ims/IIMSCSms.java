// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IIMSCSms.java

package com.motorola.android.ims;

import android.os.*;

// Referenced classes of package com.motorola.android.ims:
//            IIMSCSmsCallback

public interface IIMSCSms
    extends IInterface
{
    public static abstract class Stub extends Binder
        implements IIMSCSms
    {
        private static class Proxy
            implements IIMSCSms
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public boolean cancelMessage(long l, long l1)
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                int i;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSms");
                parcel.writeLong(l);
                parcel.writeLong(l1);
                boolean flag = mRemote.transact(5, parcel, parcel1, 0);
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

            public String getInterfaceDescriptor()
            {
                return "com.motorola.android.ims.IIMSCSms";
            }

            public int getSmsProtocolType()
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                int i;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSms");
                boolean flag = mRemote.transact(6, parcel, parcel1, 0);
                parcel1.readException();
                i = parcel1.readInt();
                int j = i;
                parcel1.recycle();
                parcel.recycle();
                return j;
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public int registerCallback(IIMSCSmsCallback iimscsmscallback)
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                IBinder ibinder;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSms");
                if(iimscsmscallback == null)
                    break MISSING_BLOCK_LABEL_72;
                ibinder = iimscsmscallback.asBinder();
_L1:
                int i;
                parcel.writeStrongBinder(ibinder);
                boolean flag = mRemote.transact(1, parcel, parcel1, 0);
                parcel1.readException();
                i = parcel1.readInt();
                int j = i;
                parcel1.recycle();
                parcel.recycle();
                return j;
                ibinder = null;
                  goto _L1
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public long sendMessage(long l, String s, int i, byte abyte0[])
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                long l1;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSms");
                parcel.writeLong(l);
                parcel.writeString(s);
                parcel.writeInt(i);
                parcel.writeByteArray(abyte0);
                boolean flag = mRemote.transact(3, parcel, parcel1, 0);
                parcel1.readException();
                l1 = parcel1.readLong();
                long l2 = l1;
                parcel1.recycle();
                parcel.recycle();
                return l2;
                Exception exception;
                exception;
                parcel1.recycle();
                parcel.recycle();
                throw exception;
            }

            public boolean sendResponse(long l, long l1, int i, byte abyte0[])
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                int j;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSms");
                parcel.writeLong(l);
                parcel.writeLong(l1);
                parcel.writeInt(i);
                parcel.writeByteArray(abyte0);
                boolean flag = mRemote.transact(4, parcel, parcel1, 0);
                parcel1.readException();
                j = parcel1.readInt();
                boolean flag1;
                if(j != 0)
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

            public int unRegisterCallback(IIMSCSmsCallback iimscsmscallback)
                throws RemoteException
            {
                Parcel parcel;
                Parcel parcel1;
                parcel = Parcel.obtain();
                parcel1 = Parcel.obtain();
                IBinder ibinder;
                parcel.writeInterfaceToken("com.motorola.android.ims.IIMSCSms");
                if(iimscsmscallback == null)
                    break MISSING_BLOCK_LABEL_72;
                ibinder = iimscsmscallback.asBinder();
_L1:
                int i;
                parcel.writeStrongBinder(ibinder);
                boolean flag = mRemote.transact(2, parcel, parcel1, 0);
                parcel1.readException();
                i = parcel1.readInt();
                int j = i;
                parcel1.recycle();
                parcel.recycle();
                return j;
                ibinder = null;
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


        public static IIMSCSms asInterface(IBinder ibinder)
        {
            Object obj;
            if(ibinder == null)
            {
                obj = null;
            } else
            {
                IInterface iinterface = ibinder.queryLocalInterface("com.motorola.android.ims.IIMSCSms");
                if(iinterface != null && (iinterface instanceof IIMSCSms))
                    obj = (IIMSCSms)iinterface;
                else
                    obj = new Proxy(ibinder);
            }
            return ((IIMSCSms) (obj));
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            i;
            JVM INSTR lookupswitch 7: default 68
        //                       1: 102
        //                       2: 165
        //                       3: 228
        //                       4: 304
        //                       5: 410
        //                       6: 496
        //                       1598968902: 82;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8
_L1:
            boolean flag = super.onTransact(i, parcel, parcel1, j);
_L10:
            return flag;
_L8:
            Parcel parcel2 = parcel1;
            String s = "com.motorola.android.ims.IIMSCSms";
            parcel2.writeString(s);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L2:
            Parcel parcel3 = parcel;
            String s1 = "com.motorola.android.ims.IIMSCSms";
            parcel3.enforceInterface(s1);
            IIMSCSmsCallback iimscsmscallback = IIMSCSmsCallback.Stub.asInterface(parcel.readStrongBinder());
            Stub stub = this;
            IIMSCSmsCallback iimscsmscallback1 = iimscsmscallback;
            int k = stub.registerCallback(iimscsmscallback1);
            parcel1.writeNoException();
            Parcel parcel4 = parcel1;
            int l = k;
            parcel4.writeInt(l);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L3:
            Parcel parcel5 = parcel;
            String s2 = "com.motorola.android.ims.IIMSCSms";
            parcel5.enforceInterface(s2);
            IIMSCSmsCallback iimscsmscallback2 = IIMSCSmsCallback.Stub.asInterface(parcel.readStrongBinder());
            Stub stub1 = this;
            IIMSCSmsCallback iimscsmscallback3 = iimscsmscallback2;
            int i1 = stub1.unRegisterCallback(iimscsmscallback3);
            parcel1.writeNoException();
            Parcel parcel6 = parcel1;
            int j1 = i1;
            parcel6.writeInt(j1);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L4:
            Parcel parcel7 = parcel;
            String s3 = "com.motorola.android.ims.IIMSCSms";
            parcel7.enforceInterface(s3);
            long l1 = parcel.readLong();
            String s4 = parcel.readString();
            int k1 = parcel.readInt();
            byte abyte0[] = parcel.createByteArray();
            long l2 = sendMessage(l1, s4, k1, abyte0);
            parcel1.writeNoException();
            Parcel parcel8 = parcel1;
            long l3 = l2;
            parcel8.writeLong(l3);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L5:
            Parcel parcel9 = parcel;
            String s5 = "com.motorola.android.ims.IIMSCSms";
            parcel9.enforceInterface(s5);
            long l4 = parcel.readLong();
            long l5 = parcel.readLong();
            int i2 = parcel.readInt();
            byte abyte1[] = parcel.createByteArray();
            Stub stub2 = this;
            long l6 = l4;
            int j2 = i2;
            byte abyte2[] = abyte1;
            boolean flag1 = stub2.sendResponse(l6, l5, j2, abyte2);
            parcel1.writeNoException();
            boolean flag2;
            Parcel parcel10;
            int k2;
            if(flag1)
                flag2 = true;
            else
                flag2 = false;
            parcel10 = parcel1;
            k2 = ((flag2) ? 1 : 0);
            parcel10.writeInt(k2);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L6:
            Parcel parcel11 = parcel;
            String s6 = "com.motorola.android.ims.IIMSCSms";
            parcel11.enforceInterface(s6);
            long l7 = parcel.readLong();
            long l8 = parcel.readLong();
            Stub stub3 = this;
            long l9 = l7;
            long l10 = l8;
            boolean flag3 = stub3.cancelMessage(l9, l10);
            parcel1.writeNoException();
            boolean flag4;
            Parcel parcel12;
            int i3;
            if(flag3)
                flag4 = true;
            else
                flag4 = false;
            parcel12 = parcel1;
            i3 = ((flag4) ? 1 : 0);
            parcel12.writeInt(i3);
            flag = true;
            continue; /* Loop/switch isn't completed */
_L7:
            Parcel parcel13 = parcel;
            String s7 = "com.motorola.android.ims.IIMSCSms";
            parcel13.enforceInterface(s7);
            int j3 = getSmsProtocolType();
            parcel1.writeNoException();
            Parcel parcel14 = parcel1;
            int k3 = j3;
            parcel14.writeInt(k3);
            flag = true;
            if(true) goto _L10; else goto _L9
_L9:
        }

        private static final String DESCRIPTOR = "com.motorola.android.ims.IIMSCSms";
        static final int TRANSACTION_cancelMessage = 5;
        static final int TRANSACTION_getSmsProtocolType = 6;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_sendMessage = 3;
        static final int TRANSACTION_sendResponse = 4;
        static final int TRANSACTION_unRegisterCallback = 2;

        public Stub()
        {
            attachInterface(this, "com.motorola.android.ims.IIMSCSms");
        }
    }


    public abstract boolean cancelMessage(long l, long l1)
        throws RemoteException;

    public abstract int getSmsProtocolType()
        throws RemoteException;

    public abstract int registerCallback(IIMSCSmsCallback iimscsmscallback)
        throws RemoteException;

    public abstract long sendMessage(long l, String s, int i, byte abyte0[])
        throws RemoteException;

    public abstract boolean sendResponse(long l, long l1, int i, byte abyte0[])
        throws RemoteException;

    public abstract int unRegisterCallback(IIMSCSmsCallback iimscsmscallback)
        throws RemoteException;
}
