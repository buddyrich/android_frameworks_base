// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IMSServiceListener.java

package com.motorola.android.ims;


// Referenced classes of package com.motorola.android.ims:
//            IMSService

public class IMSServiceListener
{

    public IMSServiceListener(IMSService imsservice)
    {
        mService = imsservice;
    }

    public void processServiceClosed(IMSService imsservice)
    {
    }

    public void processServiceOpen(IMSService imsservice, boolean flag)
    {
    }

    protected IMSService mService;
}
