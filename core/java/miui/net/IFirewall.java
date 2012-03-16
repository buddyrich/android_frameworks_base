package miui.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IFirewall extends IInterface
{
  public abstract void addAccessControlPass(String paramString)
    throws RemoteException;

  public abstract boolean checkAccessControlPass(String paramString)
    throws RemoteException;

  public abstract void onDataConnected(int paramInt, String paramString1, String paramString2)
    throws RemoteException;

  public abstract void onDataDisconnected(int paramInt, String paramString)
    throws RemoteException;

  public abstract void onStartUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;

  public abstract void onStopUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;

  public abstract void removeAccessControlPass(String paramString)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IFirewall
  {
    private static final String DESCRIPTOR = "miui.net.IFirewall";
    static final int TRANSACTION_addAccessControlPass = 5;
    static final int TRANSACTION_checkAccessControlPass = 7;
    static final int TRANSACTION_onDataConnected = 3;
    static final int TRANSACTION_onDataDisconnected = 4;
    static final int TRANSACTION_onStartUsingNetworkFeature = 1;
    static final int TRANSACTION_onStopUsingNetworkFeature = 2;
    static final int TRANSACTION_removeAccessControlPass = 6;

    public Stub()
    {
      attachInterface(this, "miui.net.IFirewall");
    }

    public static IFirewall asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("miui.net.IFirewall");
        if ((localIInterface != null) && ((localIInterface instanceof IFirewall)))
        {
          localObject = (IFirewall)localIInterface;
          continue;
        }
        localObject = new Proxy(paramIBinder);
      }
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      int i = 1;
      switch (paramInt1)
      {
      default:
        i = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
        while (true)
        {
          return i;
          paramParcel2.writeString("miui.net.IFirewall");
          continue;
          paramParcel1.enforceInterface("miui.net.IFirewall");
          onStartUsingNetworkFeature(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          continue;
          paramParcel1.enforceInterface("miui.net.IFirewall");
          onStopUsingNetworkFeature(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          continue;
          paramParcel1.enforceInterface("miui.net.IFirewall");
          onDataConnected(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          continue;
          paramParcel1.enforceInterface("miui.net.IFirewall");
          onDataDisconnected(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          continue;
          paramParcel1.enforceInterface("miui.net.IFirewall");
          addAccessControlPass(paramParcel1.readString());
          paramParcel2.writeNoException();
          continue;
          paramParcel1.enforceInterface("miui.net.IFirewall");
          removeAccessControlPass(paramParcel1.readString());
          paramParcel2.writeNoException();
        }
      case 7:
      }
      paramParcel1.enforceInterface("miui.net.IFirewall");
      boolean bool = checkAccessControlPass(paramParcel1.readString());
      paramParcel2.writeNoException();
      if (bool);
      int k;
      for (int j = i; ; k = 0)
      {
        paramParcel2.writeInt(j);
        break;
      }
    }

    private static class Proxy
      implements IFirewall
    {
      private IBinder mRemote;

      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }

      public void addAccessControlPass(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("miui.net.IFirewall");
          localParcel1.writeString(paramString);
          this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
        throw localObject;
      }

      public IBinder asBinder()
      {
        return this.mRemote;
      }

      public boolean checkAccessControlPass(String paramString)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("miui.net.IFirewall");
          localParcel1.writeString(paramString);
          this.mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int j = localParcel2.readInt();
          if (j != 0)
            i = 1;
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
        throw localObject;
      }

      public String getInterfaceDescriptor()
      {
        return "miui.net.IFirewall";
      }

      public void onDataConnected(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("miui.net.IFirewall");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          this.mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
        throw localObject;
      }

      public void onDataDisconnected(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("miui.net.IFirewall");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
        throw localObject;
      }

      public void onStartUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("miui.net.IFirewall");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          this.mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
        throw localObject;
      }

      public void onStopUsingNetworkFeature(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("miui.net.IFirewall");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          this.mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
        throw localObject;
      }

      public void removeAccessControlPass(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("miui.net.IFirewall");
          localParcel1.writeString(paramString);
          this.mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
        throw localObject;
      }
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.net.IFirewall
 * JD-Core Version:    0.6.0
 */