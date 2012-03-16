package com.android.internal.location;

import android.location.ILocationManager;
import android.location.LocationProvider;

public class DummyLocationProvider extends LocationProvider
{
  private static final String TAG = "DummyLocationProvider";
  int mAccuracy;
  boolean mHasMonetaryCost;
  String mName;
  int mPowerRequirement;
  boolean mRequiresCell;
  boolean mRequiresNetwork;
  boolean mRequiresSatellite;
  boolean mSupportsAltitude;
  boolean mSupportsBearing;
  boolean mSupportsSpeed;

  public DummyLocationProvider(String paramString, ILocationManager paramILocationManager)
  {
    super(paramString, paramILocationManager);
  }

  public int getAccuracy()
  {
    return this.mAccuracy;
  }

  public int getPowerRequirement()
  {
    return this.mPowerRequirement;
  }

  public boolean hasMonetaryCost()
  {
    return this.mHasMonetaryCost;
  }

  public boolean requiresCell()
  {
    return this.mRequiresCell;
  }

  public boolean requiresNetwork()
  {
    return this.mRequiresNetwork;
  }

  public boolean requiresSatellite()
  {
    return this.mRequiresSatellite;
  }

  public void setAccuracy(int paramInt)
  {
    this.mAccuracy = paramInt;
  }

  public void setHasMonetaryCost(boolean paramBoolean)
  {
    this.mHasMonetaryCost = paramBoolean;
  }

  public void setPowerRequirement(int paramInt)
  {
    this.mPowerRequirement = paramInt;
  }

  public void setRequiresCell(boolean paramBoolean)
  {
    this.mRequiresCell = paramBoolean;
  }

  public void setRequiresNetwork(boolean paramBoolean)
  {
    this.mRequiresNetwork = paramBoolean;
  }

  public void setRequiresSatellite(boolean paramBoolean)
  {
    this.mRequiresSatellite = paramBoolean;
  }

  public void setSupportsAltitude(boolean paramBoolean)
  {
    this.mSupportsAltitude = paramBoolean;
  }

  public void setSupportsBearing(boolean paramBoolean)
  {
    this.mSupportsBearing = paramBoolean;
  }

  public void setSupportsSpeed(boolean paramBoolean)
  {
    this.mSupportsSpeed = paramBoolean;
  }

  public boolean supportsAltitude()
  {
    return this.mSupportsAltitude;
  }

  public boolean supportsBearing()
  {
    return this.mSupportsBearing;
  }

  public boolean supportsSpeed()
  {
    return this.mSupportsSpeed;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     com.android.internal.location.DummyLocationProvider
 * JD-Core Version:    0.6.0
 */