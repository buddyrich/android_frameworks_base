// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SmsOnImsServiceListener.java

package com.motorola.android.ims.service.smsonims;

import com.motorola.android.ims.IMSService;
import com.motorola.android.ims.IMSServiceListener;

// Referenced classes of package com.motorola.android.ims.service.smsonims:
//            PageMessage

public class SmsOnImsServiceListener extends IMSServiceListener
{

    public SmsOnImsServiceListener(IMSService imsservice)
    {
        super(imsservice);
    }

    public void processMessageReceived(PageMessage pagemessage)
    {
    }
}
