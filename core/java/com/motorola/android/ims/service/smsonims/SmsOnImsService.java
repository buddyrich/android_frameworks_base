/*
 * Copyright (C) 2011 Motorola Mobility LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Integration by Hashcode [11-18-2011]
 */

package com.motorola.android.ims.service.smsonims;

import android.os.RemoteException;
import android.util.Log;
import com.motorola.android.ims.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SmsOnImsService extends IMSService {
    //****** Constants 
    private static final String TAG = "SmsOnImsService";

    //****** Private State
    private SMSCallBack mCallBack;
    private IIMSCSms mSMS;
    ArrayList<PageMessage> messages;

    private class SMSCallBack extends com.motorola.android.ims.IIMSCSmsCallback.Stub {

        public long getRegistrationId() {
            Log.i(TAG, "getRegistrationId " + Long(getRegId()).toString());
            return getRegId();
        }

        public void onMessageReceived(long regId, long msgId, String fromAddr, int format, byte body[]) {
            Log.i("SmsOnImsService", "onMessageReceived regId:"
                    + Long(regId).toString() + " msgId:" + Long(msgId).toString()
                    + " fromAdd:" + fromAddr + " format:" + Integer(format).toString()
                    + " body:" + body);
            PageMessage pagemessage = new PageMessage(SmsOnImsService.this, null, format, body, msgId);
            pagemessage.setFromAddr(fromAddr);
            ((SmsOnImsServiceListener)access$000).processMessageReceived(pagemessage);
        }

        public void onMessageResponse(long regId, long msgId, int statusCode, byte body[]) {
            String s = (new StringBuilder()).append(abyte0).toString();
            int j = Log.i(TAG, "onMessageResponse regId:" + Long(regId).toString() + " msgId:" + Long(msgId).toString() + " statusCode:" + Integer(statusCode).toString() + " body:" + s);
            Iterator iterator = messages.iterator();
            PageMessage pagemessage;
            do {
                if (!iterator.hasNext())
                    return;
                pagemessage = (PageMessage)iterator.next();
            } while(msgId != pagemessage.getMsgId());
            boolean flag;
            if (pagemessage.getListener() == null)
                Log.i("SmsOnImsService", "Listener is null.");
            else
                pagemessage.getListener().processMessageResponse(statusCode);
            messages.remove(pagemessage);
        }

        private SMSCallBack() {
            //this$0 = SmsOnImsService.this;
            super();
        }

        SMSCallBack(_cls1 _pcls1) {
            this();
        }
    }


    public SmsOnImsService(String s) {
        super(s);
        SMSCallBack smscallback = new SMSCallBack(null);
        mCallBack = smscallback;
        super.mService = this;
        ArrayList arraylist = new ArrayList();
        messages = arraylist;
        super.serviceType = "smsonims";
        String s1 = (new StringBuilder()).append("onCreate serviceName ").append(s).toString();
        int i = Log.e("SmsOnImsService", s1);
    }

    protected void addCallBack() {
        if (mSMS == null)
            return;
        try {
            mSMS.registerCallback(mCallBack);
        }
        catch(RemoteException remoteexception) {
            Log.e("SmsOnImsService", "Failed to set the callback for SMS server", remoteexception);
        }
    }

    public void close() {
        super.close();
        messages.clear();
    }

    public PageMessage createSMSMessage(String toAddr, byte body[]) {
        if(mSMS == null)
            return null;
        try {
            return createSMSMessage(toAddr, body, mSMS.getSmsProtocolType());
        } catch(RemoteException remoteexception) {
            Log.e("SmsOnImsService", "Failed to get Sms protocol type - format", remoteexception);
        }
    }

    public PageMessage createSMSMessage(String toAddr, byte body[], int format) {
        return new PageMessage(this, toAddr, format, body, 0L);
    }

    public String getFeatureTags() {
        if(mSMS == null)
            return;
        int i = mSMS.getSmsProtocolType();
        try {
            String s = "";
            int k;
            if (i == 0)
                s = "+g.3gpp2.smsip";
            else if (i == 1)
                s = "+g.3gpp.smsip";
            else if(i == 2)
                s = "";
            return s;
        } catch (RemoteException remoteexception) {
            Log.e(TAG, "Failed to get Sms protocol type - format", remoteexception);
        }
    }

    public void open() throws IMSException, IllegalStateException {
        super.open();
        mSMS = getSMSServer();
        if (mSMS == null)
            throw new IllegalStateException("Can't get SMS Server.");
    }

    protected void removeCallBack() {
        if (mSMS == null)
            return;
        try {
            mSMS.unRegisterCallback(mCallBack);
            return;
        }
        catch (RemoteException remoteexception) {
            Log.e(TAG, "Failed to remove the callback", remoteexception);
        }
    }

    void send(PageMessage pagemessage) {
        int i;
        if(super.mState != 2)
            i = Log.e("SmsOnImsService", "send error for state != STATE_OPEN");
        if(mSMS == null)
            return;
        int k;
        try
        {
            IIMSCSms iimscsms = mSMS;
            long l = getRegId();
            String s = pagemessage.getToAddr();
            int j = pagemessage.getFormat();
            byte abyte0[] = pagemessage.getBody();
            long l1 = iimscsms.sendMessage(l, s, j, abyte0);
            pagemessage.setMsgId(l1);
            boolean flag = messages.add(pagemessage);
            return;
        }
        catch(RemoteException remoteexception)
        {
            k = Log.e("SmsOnImsService", "Failed to send the sms to the remote", remoteexception);
        }
    }

    void sendResponse(PageMessage pagemessage, int i)
    {
        int j;
        if(super.mState != 2)
            j = Log.e("SmsOnImsService", "send error for state != STATE_OPEN");
        if(mSMS == null)
            return;
        int i1;
        try
        {
            IIMSCSms iimscsms = mSMS;
            long l = getRegId();
            long l1 = pagemessage.getMsgId();
            int k = i;
            boolean flag = iimscsms.sendResponse(l, l1, k, null);
            return;
        }
        catch(RemoteException remoteexception)
        {
            i1 = Log.e("SmsOnImsService", "Failed to send the response", remoteexception);
        }
    }

}
