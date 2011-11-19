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
 * Integration by Hashcode [11-17-2011]
 */

package com.motorola.android.ims.service.smsonims;

import com.motorola.android.ims.IMSService;

public class PageMessage {
    //***** Constants
    public static final int RESPONSE_CODE_BAD_REQUEST = 0;
    public static final int RESPONSE_CODE_BUSY_EVERYWHERE = 2;
    public static final int RESPONSE_CODE_BUSY_HERE = 1;
    public static final int RESPONSE_CODE_DECLINE = 3;
    public static final int RESPONSE_CODE_EVENT_MISSING = 4;
    public static final int RESPONSE_CODE_FORBIDDEN = 5;
    public static final int RESPONSE_CODE_GONE = 6;
    public static final int RESPONSE_CODE_INTERNAL_ERROR = 7;
    public static final int RESPONSE_CODE_LOCAL_FAILURE = 8;
    public static final int RESPONSE_CODE_NO_TRANSAC = 9;
    public static final int RESPONSE_CODE_NOT_ACCEPTABLE = 10;
    public static final int RESPONSE_CODE_NOT_ACCEPTABLE_HERE = 11;
    public static final int RESPONSE_CODE_NOT_FOUND = 12;
    public static final int RESPONSE_CODE_OK = 13;
    public static final int RESPONSE_CODE_PROXY_AUTH_REQUIRED = 14;
    public static final int RESPONSE_CODE_REMOTE_FAILURE = 15;
    public static final int RESPONSE_CODE_REQ_TIMEOUT = 16;
    public static final int RESPONSE_CODE_TEMP_UNAVAIL = 17;
    public static final int RESPONSE_CODE_UNAUTHORIZED = 18;
    public static final int RESPONSE_CODE_UNSUPPORTED_MEDIA = 19;
    public static final int RESPONSE_CODE_UNKNOWN_REASON = 255;

    public static final int SMS_PROTOCOL_3GPP = 1;
    public static final int SMS_PROTOCOL_3GPP2 = 0;
    public static final int SMS_PROTOCOL_TRANS = 2;

    //****** Private State
    private int mFormat;
    private PageMessageListener mListener;
    private byte mBody[];
    private String mFromAddr;
    private long mMsgID;
    private IMSService mService;
    private String mToAddr;

    PageMessage(IMSService imsservice, String toAddr, int format, byte body[], long msgID) {
        mFormat = 0;
        mListener = null;
        mService = imsservice;
        mToAddr = toAddr;
        mFormat = format;
        mBody = body;
        mMsgID = msgID;
    }

    public byte[] getBody() {
        return mBody;
    }

    public int getFormat() {
        return mFormat;
    }

    public String getFromAddr() {
        return mFromAddr;
    }

    public PageMessageListener getListener() {
        return mListener;
    }

    public long getMsgId() {
        return mMsgID;
    }

    public String getToAddr() {
        return mToAddr;
    }

    public void send() {
        ((SmsOnImsService)mService).send(this);
    }

    public void sendResponse(int i) {
        ((SmsOnImsService)mService).sendResponse(this, i);
    }

    public void setBody(byte abyte0[]) {
        mBody = abyte0;
    }

    public void setFormat(int i) {
        mFormat = i;
    }

    public void setFromAddr(String s) {
        mFromAddr = s;
    }

    public void setListener(PageMessageListener pagemessagelistener) {
        mListener = pagemessagelistener;
    }

    public void setMsgId(long l) {
        mMsgID = l;
    }

    public void setToAddr(String s) {
        mToAddr = s;
    }

}
