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

package com.android.internal.telephony.gsm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.IConnectivityManager;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;

import com.android.internal.telephony.*;
import com.android.internal.telephony.cdma.*;
import com.android.internal.util.HexDump;
import com.motorola.android.internal.telephony.IccCardManager;
import com.motorola.android.ims.IMSManager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@hide}
 */
public final class LteCdmaDataConnectionTracker extends DataConnectionTracker {
    // This is supposed to be added to com.android.internal.R
    public static final int ImsConnectedDefaultValue = 0x10d0023;

    protected final String LOG_TAG = "LTE";

    private CDMAPhone mCdmaPhone;


    private final int APNCHANGED_DELAY_TIMER = 60000;

    //***** Instance Variables

    // Indicates baseband will not auto-attach
    private boolean noAutoAttach = false;

    private boolean mReregisterOnReconnectFailure = false;
    private ContentResolver mResolver;
    private boolean mPingTestActive = false;
    // Count of PDP reset attempts; reset when we see incoming,
    // call reRegisterNetwork, or pingTest succeeds.
    private int mPdpResetCount = 0;
    private boolean mIsScreenOn = true;

    /** Delay between APN attempts */
    protected static final int APN_DELAY_MILLIS = 5000;

    //useful for debugging
    boolean failNextConnect = false;

    /**
     * allApns holds all apns for this sim spn, retrieved from
     * the Carrier DB.
     *
     * Create once after simcard info is loaded
     */
    private ArrayList<ApnSetting> allApns = null;
    private ApnSetting preferredApn = null;
    private ApnSetting candidatePreferredApn = null;

    /* Currently active APN */
    protected ApnSetting mActiveApn;

    /**
     * pdpList holds all the PDP connection, i.e. IP Link in GPRS
     */
    private ArrayList<DataConnection> pdpList;

    //***** Constants

    // TODO: Increase this to match the max number of simultaneous
    // PDP contexts we plan to support.
    /**
     * Pool size of DataConnection objects.
     */
    private static final int PDP_CONNECTION_POOL_SIZE = 16;

    private static final int POLL_PDP_MILLIS = 5 * 1000;

    private static final String INTENT_RECONNECT_ALARM = "com.android.internal.telephony.gprs-reconnect";
    private static final String INTENT_RECONNECT_ALARM_EXTRA_REASON = "reason";
    private static final String INTENT_RECONNECT_ALARM_EXTRA_TYPE = "type";

    static final Uri PREFERAPN_URI = Uri.parse("content://telephony/carriers/preferapn");
    static final String APN_ID = "apn_id";
    private boolean canSetPreferApn = false;
    private ConcurrentHashMap mApnContexts;

    private RuimRecords mRuimRecords = null;
    private RuimCard mRuimCard = null;
    private IccCardManager mIccCardManager = null;
    private boolean mIsNewArch = false;
    private boolean isLteTestNoOpCheck = false;
    private boolean isLteTestWithoutCsim = false;
    private boolean mIsImsTriedInEhprd = false;
    private boolean mIsImsConnected;
    private boolean mIsPsRestricted = false;
    private int mOwnerModemId;
    INetStatService netstat;

    android.os.PowerManager.WakeLock mWakeLock = null;

    // Added LTE handling for IMSRegistration
    BroadcastReceiver mIntentReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String s = intent.getAction();
            if (s.equals(Intent.ACTION_SCREEN_ON))
            {
                mIsScreenOn = true;
                stopNetStatPoll();
                startNetStatPollNoReset();
            } else if (s.equals(Intent.ACTION_SCREEN_OFF)) {
                mIsScreenOn = false;
                stopNetStatPoll();
                startNetStatPollNoReset();
            } else if (s.startsWith(INTENT_RECONNECT_ALARM)) {
                Log.d(LOG_TAG, "GPRS reconnect alarm. Previous state was " + state);

                String reason = intent.getStringExtra(INTENT_RECONNECT_ALARM_EXTRA_REASON);
                String type = intent.getStringExtra(INTENT_RECONNECT_ALARM_EXTRA_TYPE);
                ApnContext ncontext = (ApnContext)mApnContexts.get(type);
                if(ncontext != null) {
                    ncontext.setReason(reason);
                    if (ncontext.getState() == State.FAILED)
                        ncontext.setEnabled(false);
                    Message message = obtainMessage(EVENT_TRY_SETUP_DATA, ncontext);
                    sendMessage(message);
                }
            } else if (s.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                final android.net.NetworkInfo networkInfo = (NetworkInfo)
                        intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                mIsWifiConnected = (networkInfo != null && networkInfo.isConnected());
            } else if(s.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                final boolean enabled = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN) == WifiManager.WIFI_STATE_ENABLED;
                if (!enabled) {
                    // when wifi got disabled, the NETWORK_STATE_CHANGED_ACTION
                    // quit and won't report disconnected til next enabling.
                    mIsWifiConnected = false;
                }
            } else if (s.equals(IMSManager.IMS_REGISTRATION_STATUS)) {
                int imsRegCode = intent.getIntExtra("Code", 0);
                boolean imRegReRegFlag = intent.getBooleanExtra("reReg", false);
                mPdnDeactivateCause = convertImsRegStatusToRilCause(imsRegCode, imsRegReRegFlag);
                Log.d(LOG_TAG, "IMS Registration state is : " + Integer(imsRegCode).toString + ", ReReg is: " + Boolean(imsRegRegFlag).ToString + ", DeactivateCause = " + Integer(mPdnDeactivateCause).toString);
                if (imRegCode != 0) {
                    mIsImsConnected = false;
                    if(mCdmaPhone.getServiceState().getRadioTechnology() == ServiceStateTracker.DATA_ACCESS_LTE) {
                        requestDataSetup(Phone.APN_TYPE_DEFAULT, "IMSRegFailedInEhrpd", 0);
                    }
                } else {
                    mIsImsConnected = true;
                    requestDataSetup(Phone.APN_TYPE_DEFAULT, "IMSRegistered", 0);
                }
            }
        }
    }

    private Runnable mPollNetStat = new Runnable() {
        public void run() {
            runPingTest();
        }
    }

    /**
     * Handles changes to the APN db.
     */
    private class ApnChangeObserver extends ContentObserver {
        public ApnChangeObserver () {
            super(mDataConnectionTracker);
        }


        @Override
        public void onChange(boolean selfChange) {
            sendMessage(obtainMessage(EVENT_APN_CHANGED));
        }
    }

    private ApnChangeObserver apnObserver;

    /**
     * Handles changes to the APN db.
     */
    private class ApnContext {
        String mApnType;
        String mReason;
        boolean mDataEnabled;

        ArrayList<ApnSetting> mWaitingApns = null;
        DataInactivityTracker mApnInactivityTracker = null;
        ApnSetting mActiveApn;
        GsmDataConnection mActivePdp;
        RetryManager mApnRetryMgr;
        PendingIntent mReconnectIntent;
        State mState = State.IDLE;

        final LteCdmaDataConnectionTracker this$0;

        public ApnSetting getActiveApn() {
            return mActiveApn;
        }

        public GsmDataConnection getActivePdp() {
            return mActivePdp;
        }

        public String getApnType() {
            return mApnType;
        }

        public ApnSetting getNextApn() {
            ApnSetting apnsetting = null;
            if(mWaitingApns != null && !mWaitingApns.isEmpty())
                apnsetting = (ApnSetting)mWaitingApns.get(0);
            return apnsetting;
        }

        public String getReason() {
            return mReason;
        }

        public PendingIntent getReconnectIntent() {
            return mReconnectIntent;
        }

        public RetryManager getRetryMgr() {
            return mApnRetryMgr;
        }

        public State getState() {
            return mState;
        }

        public ArrayList getWaitingApns() {
            return mWaitingApns;
        }

        public boolean isApnTypeActive() {
            boolean flag;
            if(mActiveApn != null)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public boolean isEnabled() {
            return mDataEnabled;
        }

        public void removeNextApn() {
            if(mWaitingApns == null)
                return;
            if(!mWaitingApns.isEmpty()) {
                mWaitingApns.remove(0);
            }
        }

        public void setActiveApn(ApnSetting apnsetting) {
            mActiveApn = apnsetting;
        }

        public void setActivePdp(GsmDataConnection gsmdataconnection) {
            mActivePdp = gsmdataconnection;
        }

        public void setEnabled(boolean flag) {
            mDataEnabled = flag;
        }

        public void setReason(String s) {
            mReason = s;
        }

        public void setReconnectIntent(PendingIntent pendingintent) {
            mReconnectIntent = pendingintent;
        }

        public void setState(State state) {
            log("setState: " + state + " for type " + mApnType + ", previous state: " + mState);
            mState = state;
            if(mState == State.FAILED)
                if (mWaitingApns != null) {
                    mWaitingApns.clear();
                }
        }

        public void setWaitingApns(ArrayList arraylist) {
            mWaitingApns = arraylist;
        }

        public ApnContext(String apnType) {
            //this$0 = LteCdmaDataConnectionTracker.this;
            super();
            mApnType = apnType;
            mApnRetryMgr = new RetryManager();
            if (mApnType.equals(Phone.APN_TYPE_DEFAULT)) {
                if(mApnRetryMgr.configure(SystemProperties.get("ro.gsm.data_retry_config")))
                    return;
                if(!mApnRetryMgr.configure(DEFAULT_DATA_RETRY_CONFIG)) {
                    Log.e(LOG_TAG, "Could not configure using DEFAULT_DATA_RETRY_CONFIG=" + DEFAULT_DATA_RETRY_CONFIG);
                    mApnRetryMgr.configure(20, 2000, 1000);
                    return;
                }
            } else if (mApnType.equals(Phone.APN_TYPE_IMS)) {
                if(!mApnRetryMgr.configure(SystemProperties.get("ro.gsm.ims_data_retry_config"))) {
                    mApnRetryMgr.configure(0, 0, 0);
                    return;
                }
            } else if (mApnRetryMgr.configure(SystemProperties.get("ro.gsm.2nd_data_retry_config"))) {
                return;
            } else if (!mApnRetryMgr.configure(SECONDARY_DATA_RETRY_CONFIG)) {
                Log.e(LOG_TAG, "Could note configure using SECONDARY_DATA_RETRY_CONFIG=" + SECONDARY_DATA_RETRY_CONFIG);
                mApnRetryMgr.configure("max_retries=3, 333, 333, 333");
            }
        }
    }


    // Constructors

    public
    LteCdmaDataConnectionTracker(int modemId, CDMAPhone p) {
        super.DataConnectionTracker(p);
        mIsNewArch = true;
        mIccCardManager = IccCardManager.getInstance();
        int j = SystemProperties.getInt("persist.radio.lte.testsim", 0);
        isLteTestWithoutCsim = (j == 2);
        isLteTestNoOpCheck = (j == 1 || j == 2);
        mCdmaPhone = p;
        mOwnerModemId = modemId;
        createAllPdpList();
        createApnContexts();
    }

    public
    LteCdmaDataConnectionTracker(CDMAPhone p) {
        super(p);
        mCdmaPhone = p;

        mIsImsConnected = mCdmaPhone.getContext().getResources().getBoolean(ImsConnectedDefaultValue);
        int j = SystemProperties.getInt("persist.radio.lte.testsim", 0);
        isLteTestWithoutCsim = (j == 2);
        isLteTestNoOpCheck = (j == 1 || j == 2);
        createAllPdpList();
        createApnContexts();
        activateMe();
    }

    private void activateMe() {
        super.phone.mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
        super.phone.mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        if (mIsNewArch)
            mIccCardManager.registerForIccChanged(this, EVENT_ICC_CHANGED, null);
        else
            if (!isLteTestWithoutCsim && mCdmaPhone.mCsimRecords != null)
                mCdmaPhone.mCsimRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
        super.phone.mCM.registerForDataStateChanged(this, EVENT_DATA_STATE_CHANGED, null);
        mCdmaPhone.mCT.registerForVoiceCallEnded(this, EVENT_VOICE_CALL_ENDED, null);
        mCdmaPhone.mCT.registerForVoiceCallStarted(this, VOICE_CALL_STARTED, null);
        mCdmaPhone.mSST.registerForGprsAttached(this, EVENT_GPRS_ATTACHED, null);
        mCdmaPhone.mSST.registerForGprsDetached(this, EVENT_GPRS_DETACHED, null);
        mCdmaPhone.mSST.registerForRoamingOn(this, EVENT_ROAMING_ON, null);
        mCdmaPhone.mSST.registerForRoamingOff(this, EVENT_ROAMING_OFF, null);
        mCdmaPhone.mSST.registerForPsRestrictedEnabled(this, EVENT_PS_RESTRICT_ENABLED, null);
        mCdmaPhone.mSST.registerForPsRestrictedDisabled(this, EVENT_PS_RESTRICT_DISABLED, null);

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(Intent.ACTION_SCREEN_ON);
        intentfilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentfilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentfilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentfilter.addAction(IMSManager.IMS_REGISTRATION_STATUS);

        netstat = android.os.INetStatService.Stub.asInterface(ServiceManager.getService("netstat"));

        mCdmaPhone.getContext().registerReceiver(mIntentReceiver, intentfilter, null, mCdmaPhone);

        mDataConnectionTracker = this;
        mResolver = phone.getContext().getContentResolver();

        apnObserver = new ApnChangeObserver();
        mCdmaPhone.getContext().getContentResolver().registerContentObserver(
                Telephony.Carriers.CONTENT_URI, true, apnObserver);

        mWakeLock = ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
        mWakeLock.setReferenceCounted(false);

        // This preference tells us 1) initial condition for "dataEnabled",
        // and 2) whether the RIL will setup the baseband to auto-PS attach.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(phone.getContext());
        ApnContext apncontext = (ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT);
        boolean dataEnabledSetting = true;
        try {
            dataEnabledSetting = IConnectivityManager.Stub.asInterface(ServiceManager.
                getService(Context.CONNECTIVITY_SERVICE)).getMobileDataEnabled();
        } catch (Exception e) {
            // nothing to do - use the old behavior and leave data on
        }
        boolean flag1 = !sp.getBoolean(GSMPhone.DATA_DISABLED_ON_BOOT_KEY, false) && dataEnabledSetting;
        apncontext.setEnabled(flag1);
        noAutoAttach = !flag1;
        if (isLteTestWithoutCsim) {
            createAllApnList();
        }
    }

    public void activate() {
        activateMe();
    }

    public void deactivate() {
        super.phone.mCM.unregisterForAvailable(this);
        super.phone.mCM.unregisterForOffOrNotAvailable(this);
        if (mIsNewArch) {
             mIccCardManager.unregisterForIccChanged(this);
             if(mRuimRecords != null)
                 mRuimRecords.unregisterForRecordsLoaded(this);
        }
        else if(!isLteTestWithoutCsim && mCdmaPhone.mCsimRecords != null)
            mCdmaPhone.mCsimRecords.unregisterForRecordsLoaded(this);
        super.phone.mCM.unregisterForDataStateChanged(this);
        mCdmaPhone.mCT.unregisterForVoiceCallEnded(this);
        mCdmaPhone.mCT.unregisterForVoiceCallStarted(this);
        mCdmaPhone.mSST.unregisterForGprsAttached(this);
        mCdmaPhone.mSST.unregisterForGprsDetached(this);
        mCdmaPhone.mSST.unregisterForRoamingOn(this);
        mCdmaPhone.mSST.unregisterForRoamingOff(this);
        mCdmaPhone.mSST.unregisterForPsRestrictedEnabled(this);
        mCdmaPhone.mSST.unregisterForPsRestrictedDisabled(this);
        super.phone.getContext().unregisterReceiver(mIntentReceiver);
        super.phone.getContext().getContentResolver().unregisterContentObserver(apnObserver);
        cleanUpAllConnections(false, Phone.REASON_PDP_RESET);
        removeMessages(EVENT_TRY_SETUP_DATA);
    }

    public void dispose() {
        deactivate();
        mApnContexts.clear();
        destroyAllPdpList();
    }

    protected void finalize() {
        Log.d(LOG_TAG, "LteCdmaDataConnectionTracker finalized");
    }

    private void cleanUpAllConnections(boolean tearDown, String reason) {
        log("Clean up all connections due to " + reason);
        setState(State.DISCONNECTING);

        ApnContext apncontext;
        for(Iterator iterator = mApnContexts.entrySet().iterator(); iterator.hasNext(); cleanUpConnection(tearDown, apncontext))
        {
            apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
            apncontext.setReason(reason);
        }

        stopNetStatPoll();
        setState(State.IDLE);
        super.mRequestedApnType = Phone.APN_TYPE_DEFAULT;
    }

    private void cleanUpConnection(boolean tearDown, ApnContext apncontext) {
        if (apncontext == null) {
            log("apn context is null");
            return;
        }
        log("Clean up connection due to " + reason);

        // Clear the reconnect alarm, if set.
        if (apncontext.getReconnectIntent() != null) {
            AlarmManager am =
                   (AlarmManager)super.phone.getContext().getSystemService(Context.ALARM_SERVICE);
            am.cancel(apncontext.getReconnectIntent());
            apncontext.setReconnectIntent(null);
        }

        if (!TextUtils.equals(apncontext.getApnType(), Phone.APN_TYPE_DEFAULT)) {
            if (TextUtils.equals(apncontext.getApnType(), Phone.APN_TYPE_IMS))
                mIsImsConnected = false;
        }
        else {
           removeMessages(EVENT_TRY_SETUP_DATA);
        }

        /*
         * GRR THIS IS ALL FUCKED UP.  NEED TO PLAYTEST THIS TO SEE HOW IT'S SUPPOSED TO WORK
         */
        /*
        if (apncontext.getState() != State.IDLE) {
            if (apncontext.getState() != State.DISCONNECTING)
                break;
        }
        log("state is in " + apncontext.getState());
        return;
         */

        if (apncontext.getState() != State.FAILED) {
            if (Phone.REASON_RADIO_TURNED_OFF.equals(apncontext.getReason()))
                    super.mPdnDeactivateCause = 1;
            GsmDataConnection gsmdataconnection = apncontext.getActivePdp();
            if (!tearDown || gsmdataconnection == null) {
                if (gsmdataconnection != null) {
                    gsmdataconnection.resetSynchronously();
                    gsmdataconnection.clearSettings();
                    if (apncontext.mApnInactivityTracker != null) {
                        apncontext.mApnInactivityTracker.stopInactivityTimerAlarm();
                        apncontext.mApnInactivityTracker = null;
                    }
                }
            } else {
                apncontext.setState(State.DISCONNECTING);
                gsmdataconnection.disconnect(obtainMessage(EVENT_DISCONNECT_DONE, apncontext));
            }
        }

        if (!tearDown) {
            apncontext.setState(State.IDLE);
            setState(State.IDLE);
            super.phone.notifyDataConnection(apncontext.getReason(), apncontext.getApnType());
        }
    }

    /* ANOTHER SCREWED UP CONVERT */
    private int convertImsRegStatusToRilCause(int i, boolean flag)
    {
        if(i != 3) goto _L2; else goto _L1
_L1:
        char c;
        mIsImsTriedInEhprd = true;
        c = '\205';
_L4:
        return c;
_L2:
        if(i == 0 || !flag)
            break; /* Loop/switch isn't completed */
        c = '\206';
        if(true) goto _L4; else goto _L3
_L3:
        switch(i)
        {
        case 3: // '\003'
        default:
            c = '\0';
            break;

        case 2: // '\002'
            c = '\203';
            break;

        case 4: // '\004'
            c = '\201';
            break;

        case 5: // '\005'
            c = '\200';
            break;
        }
        if(true) goto _L4; else goto _L5
_L5:
    }

    private void createApnContexts() {
        mApnContexts = new ConcurrentHashMap();
        ApnContext apncontext = new ApnContext(Phone.APN_TYPE_DEFAULT);
        if(mCdmaPhone.getServiceState().getRadioTechnology() == 14)
        {
            RetryManager retrymanager = apncontext.getRetryMgr();
            String s = SystemProperties.get("ro.cdma.data_retry_config");
            boolean flag = retrymanager.configure(s);
        }
        ConcurrentHashMap concurrenthashmap1 = mApnContexts;
        String s1 = apncontext.getApnType();
        Object obj = concurrenthashmap1.put(s1, apncontext);
        ApnContext apncontext1 = new ApnContext("mms");
        ConcurrentHashMap concurrenthashmap2 = mApnContexts;
        String s2 = apncontext1.getApnType();
        Object obj1 = concurrenthashmap2.put(s2, apncontext1);
        ApnContext apncontext2 = new ApnContext("supl");
        ConcurrentHashMap concurrenthashmap3 = mApnContexts;
        String s3 = apncontext2.getApnType();
        Object obj2 = concurrenthashmap3.put(s3, apncontext2);
    }

    private ArrayList createApnList(Cursor cursor)
    {
        ArrayList arraylist = new ArrayList();
        if(cursor.moveToFirst())
            do
            {
                Cursor cursor1 = cursor;
                String s = "type";
                int i = cursor1.getColumnIndexOrThrow(s);
                Cursor cursor2 = cursor;
                int j = i;
                String s1 = cursor2.getString(j);
                LteCdmaDataConnectionTracker ltecdmadataconnectiontracker = this;
                String s2 = s1;
                String as[] = ltecdmadataconnectiontracker.parseTypes(s2);
                Cursor cursor3 = cursor;
                String s3 = "enabled";
                int k = cursor3.getColumnIndexOrThrow(s3);
                Cursor cursor4 = cursor;
                int l = k;
                int i1 = cursor4.getInt(l);
                ApnSetting apnsetting = JVM INSTR new #538 <Class ApnSetting>;
                Cursor cursor5 = cursor;
                String s4 = "_id";
                int j1 = cursor5.getColumnIndexOrThrow(s4);
                Cursor cursor6 = cursor;
                int k1 = j1;
                int l1 = cursor6.getInt(k1);
                Cursor cursor7 = cursor;
                String s5 = "numeric";
                int i2 = cursor7.getColumnIndexOrThrow(s5);
                Cursor cursor8 = cursor;
                int j2 = i2;
                String s6 = cursor8.getString(j2);
                Cursor cursor9 = cursor;
                String s7 = "name";
                int k2 = cursor9.getColumnIndexOrThrow(s7);
                Cursor cursor10 = cursor;
                int l2 = k2;
                String s8 = cursor10.getString(l2);
                Cursor cursor11 = cursor;
                String s9 = "apn";
                int i3 = cursor11.getColumnIndexOrThrow(s9);
                Cursor cursor12 = cursor;
                int j3 = i3;
                String s10 = cursor12.getString(j3);
                Cursor cursor13 = cursor;
                String s11 = "proxy";
                int k3 = cursor13.getColumnIndexOrThrow(s11);
                Cursor cursor14 = cursor;
                int l3 = k3;
                String s12 = cursor14.getString(l3);
                Cursor cursor15 = cursor;
                String s13 = "port";
                int i4 = cursor15.getColumnIndexOrThrow(s13);
                Cursor cursor16 = cursor;
                int j4 = i4;
                String s14 = cursor16.getString(j4);
                Cursor cursor17 = cursor;
                String s15 = "mmsc";
                int k4 = cursor17.getColumnIndexOrThrow(s15);
                Cursor cursor18 = cursor;
                int l4 = k4;
                String s16 = cursor18.getString(l4);
                Cursor cursor19 = cursor;
                String s17 = "mmsproxy";
                int i5 = cursor19.getColumnIndexOrThrow(s17);
                Cursor cursor20 = cursor;
                int j5 = i5;
                String s18 = cursor20.getString(j5);
                Cursor cursor21 = cursor;
                String s19 = "mmsport";
                int k5 = cursor21.getColumnIndexOrThrow(s19);
                Cursor cursor22 = cursor;
                int l5 = k5;
                String s20 = cursor22.getString(l5);
                Cursor cursor23 = cursor;
                String s21 = "user";
                int i6 = cursor23.getColumnIndexOrThrow(s21);
                Cursor cursor24 = cursor;
                int j6 = i6;
                String s22 = cursor24.getString(j6);
                Cursor cursor25 = cursor;
                String s23 = "password";
                int k6 = cursor25.getColumnIndexOrThrow(s23);
                Cursor cursor26 = cursor;
                int l6 = k6;
                String s24 = cursor26.getString(l6);
                Cursor cursor27 = cursor;
                String s25 = "authtype";
                int i7 = cursor27.getColumnIndexOrThrow(s25);
                Cursor cursor28 = cursor;
                int j7 = i7;
                int k7 = cursor28.getInt(j7);
                boolean flag;
                Cursor cursor29;
                String s26;
                int l7;
                Cursor cursor30;
                int i8;
                int j8;
                Cursor cursor31;
                String s27;
                int k8;
                Cursor cursor32;
                int l8;
                String s28;
                Cursor cursor33;
                String s29;
                int i9;
                Cursor cursor34;
                int j9;
                int k9;
                Cursor cursor35;
                String s30;
                int l9;
                Cursor cursor36;
                int i10;
                String s31;
                ArrayList arraylist1;
                ApnSetting apnsetting1;
                boolean flag1;
                if(i1 == 0)
                    flag = false;
                else
                    flag = true;
                cursor29 = cursor;
                s26 = "inactivetimer";
                l7 = cursor29.getColumnIndexOrThrow(s26);
                cursor30 = cursor;
                i8 = l7;
                j8 = cursor30.getInt(i8);
                cursor31 = cursor;
                s27 = "iptype";
                k8 = cursor31.getColumnIndexOrThrow(s27);
                cursor32 = cursor;
                l8 = k8;
                s28 = cursor32.getString(l8);
                cursor33 = cursor;
                s29 = "class";
                i9 = cursor33.getColumnIndexOrThrow(s29);
                cursor34 = cursor;
                j9 = i9;
                k9 = cursor34.getInt(j9);
                cursor35 = cursor;
                s30 = "roaming_protocol";
                l9 = cursor35.getColumnIndexOrThrow(s30);
                cursor36 = cursor;
                i10 = l9;
                s31 = cursor36.getString(i10);
                apnsetting.ApnSetting(l1, s6, s8, s10, s12, s14, s16, s18, s20, s22, s24, k7, flag, j8, s28, as, k9, s31);
                arraylist1 = arraylist;
                apnsetting1 = apnsetting;
                flag1 = arraylist1.add(apnsetting1);
            } while(cursor.moveToNext());
        return arraylist;
    }

    private void doRecovery() {
        if (getState(Phone.APN_TYPE_DEFAULT) == State.CONNECTED) {
            int k = android.provider.Settings.Secure.getInt(mResolver, android.provider.Settings.PDP_WATCHDOG_MAX_PDP_RESET_FAIL_COUNT, 3);
            if (mPdpResetCount < k) {
                mPdpResetCount = mPdpResetCount + 1;
                EventLog.writeEvent(50103, super.sentSinceLastRecv);
                log("doRecovery clean all connection");
                cleanUpAllConnections(true, "pdpReset");
            } else {
                mPdpResetCount = 0;
                EventLog.writeEvent(50104, super.sentSinceLastRecv);
                mCdmaPhone.mSST.reRegisterNetwork(null);
            }
        }
    }

    private GsmDataConnection findFreePdp()
    {
        Iterator iterator = pdpList.iterator();
_L4:
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        GsmDataConnection gsmdataconnection = (GsmDataConnection)(DataConnection)iterator.next();
        if(!gsmdataconnection.isInactive()) goto _L4; else goto _L3
_L3:
        GsmDataConnection gsmdataconnection1 = gsmdataconnection;
_L6:
        return gsmdataconnection1;
_L2:
        gsmdataconnection1 = null;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private GsmDataConnection findReadyPdp(ApnSetting apnsetting)
    {
        Object obj;
        obj = (new StringBuilder()).append("findReadyPdp for apn string <");
        String s;
        String s1;
        Iterator iterator;
        GsmDataConnection gsmdataconnection;
        StringBuilder stringbuilder;
        String s3;
        String s4;
        String s5;
        if(apnsetting != null)
            s = apnsetting.toString();
        else
            s = "null";
        s1 = ((StringBuilder) (obj)).append(s).append(">").toString();
        log(s1);
        iterator = pdpList.iterator();
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        gsmdataconnection = (GsmDataConnection)(DataConnection)iterator.next();
        stringbuilder = (new StringBuilder()).append("pdp apn string <");
        String s2;
        if(gsmdataconnection.getApn() != null)
            s2 = gsmdataconnection.getApn().toString();
        else
            s2 = "null";
        s3 = stringbuilder.append(s2).append(">").toString();
        log(s3);
        if(gsmdataconnection.getApn() == null || apnsetting == null)
            break MISSING_BLOCK_LABEL_54;
        s4 = gsmdataconnection.getApn().toString();
        s5 = apnsetting.toString();
        if(!s4.equals(s5))
            break MISSING_BLOCK_LABEL_54;
        obj = gsmdataconnection;
_L4:
        return ((GsmDataConnection) (obj));
_L2:
        obj = null;
        if(true) goto _L4; else goto _L3
_L3:
    }

    private GsmDataConnection findReadyPdpForOem(int i)
    {
        Iterator iterator;
        String s = (new StringBuilder()).append("find a ready pdp for oem pid:").append(i).toString();
        log(s);
        iterator = pdpList.iterator();
_L4:
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        GsmDataConnection gsmdataconnection = (GsmDataConnection)(DataConnection)iterator.next();
        if(gsmdataconnection.getProfileId() != i) goto _L4; else goto _L3
_L3:
        GsmDataConnection gsmdataconnection1 = gsmdataconnection;
_L6:
        return gsmdataconnection1;
_L2:
        gsmdataconnection1 = null;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private String getIpAddrFromPdpState(ArrayList arraylist, int i)
    {
        int j;
        int k;
        j = 0;
        k = arraylist.size();
_L3:
        if(j >= k)
            break MISSING_BLOCK_LABEL_52;
        if(((DataCallState)arraylist.get(j)).cid != i) goto _L2; else goto _L1
_L1:
        String s = ((DataCallState)arraylist.get(j)).address;
_L4:
        return s;
_L2:
        j++;
          goto _L3
        s = null;
          goto _L4
    }

    private void gotoIdleAndNotifyDataConnection(String s)
    {
        String s1 = (new StringBuilder()).append("gotoIdleAndNotifyDataConnection: reason=").append(s).toString();
        log(s1);
        com.android.internal.telephony.DataConnectionTracker.State state = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
        setState(state);
        super.phone.notifyDataConnection(s);
        mActiveApn = null;
    }

    private boolean isConnected()
    {
        Iterator iterator = mApnContexts.entrySet().iterator();
_L4:
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        int i;
        int j;
        i = ((ApnContext)((java.util.Map.Entry)iterator.next()).getValue()).getState();
        j = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
        if(i != j) goto _L4; else goto _L3
_L3:
        boolean flag = true;
_L6:
        return flag;
_L2:
        flag = false;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private boolean isDataAllowed(ApnContext apncontext)
    {
        boolean flag = super.phone.getServiceState().getRoaming();
        boolean flag1;
        boolean flag2;
        StringBuilder stringbuilder;
        boolean flag3;
        StringBuilder stringbuilder1;
        boolean flag4;
        String s;
        boolean flag5;
        if(super.phone.getServiceState().getRadioTechnology() == 13)
            flag1 = true;
        else
            flag1 = false;
        flag2 = isDataAllowedByApnConfig();
        stringbuilder = (new StringBuilder()).append("roaming (").append(flag).append("), Is Lte(").append(flag1).append("), apn enabled(");
        flag3 = apncontext.isEnabled();
        stringbuilder1 = stringbuilder.append(flag3).append("), mater data enabled(");
        flag4 = mMasterDataEnabled;
        s = stringbuilder1.append(flag4).append("), isDataAllowedByApnCOnfig(").append(flag2).append(")").toString();
        log(s);
        if(apncontext.isEnabled() && (flag1 || !flag || getDataOnRoamingEnabled()) && mMasterDataEnabled && flag2)
            flag5 = true;
        else
            flag5 = false;
        return flag5;
    }

    private boolean isDataAllowedByApnConfig()
    {
        boolean flag = false;
        boolean flag1 = false;
        if(allApns != null)
        {
            Iterator iterator = allApns.iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                ApnSetting apnsetting = (ApnSetting)iterator.next();
                if(apnsetting.apnclass == 1 && apnsetting.enabled)
                    flag = true;
                if(apnsetting.apnclass == 2 && apnsetting.enabled)
                    flag1 = true;
            } while(true);
        }
        boolean flag2;
        if(flag && flag1)
            flag2 = true;
        else
            flag2 = false;
        return flag2;
    }

    private boolean isEnabled(String s)
    {
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        boolean flag;
        if(apncontext != null && apncontext.isEnabled())
            flag = true;
        else
            flag = false;
        return flag;
    }

    private boolean isIpAddrChanged(ArrayList arraylist, GsmDataConnection gsmdataconnection)
    {
        int i = gsmdataconnection.getCid();
        String s = getIpAddrFromPdpState(arraylist, i);
        boolean flag;
        if(s != null)
            flag = gsmdataconnection.isIpAddrChanged(s);
        else
            flag = false;
        return flag;
    }

    private void notifyAllEnabledDataConnection(String s)
    {
        String s1 = (new StringBuilder()).append("notify all enabled connection for:").append(s).toString();
        log(s1);
        Iterator iterator = mApnContexts.entrySet().iterator();
        do
        {
            ApnContext apncontext;
            do
            {
                if(!iterator.hasNext())
                    return;
                apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
            } while(!apncontext.isEnabled());
            StringBuilder stringbuilder = (new StringBuilder()).append("notify for type:");
            String s2 = apncontext.getApnType();
            String s3 = stringbuilder.append(s2).toString();
            log(s3);
            PhoneBase phonebase = super.phone;
            String s4;
            String s5;
            if(s != null)
                s4 = s;
            else
                s4 = apncontext.getReason();
            s5 = apncontext.getApnType();
            phonebase.notifyDataConnection(s4, s5);
        } while(true);
    }

    private void notifyDefaultData(ApnContext apncontext) {
        log("notifyDefaultData for type: " + apncontext.getApnType().toString() + ", reason:" + apncontext.getReason());
        setupDnsProperties();
        apncontext.setState(State.CONNECTED);
        setState(State.CONNECTED);
        super.phone.notifyDataConnection(apncontext.getReason(), apncontext.getApnType());
        startNetStatPoll();
        apncontext.getRetryMgr().resetRetryCount();
    }

    private void notifyNoData(com.android.internal.telephony.DataConnection.FailCause failcause) {
        log("notifyNoData");
        setState(State.FAILED);
    }

    private void notifyNoData(com.android.internal.telephony.DataConnection.FailCause failcause, ApnContext apncontext)
    {
        StringBuilder stringbuilder = (new StringBuilder()).append("notifyNoData for type:");
        String s = apncontext.getApnType();
        String s1 = stringbuilder.append(s).toString();
        log(s1);
        com.android.internal.telephony.DataConnectionTracker.State state = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        apncontext.setState(state);
        if(!failcause.isPermanentFail())
        {
            int i = com.android.internal.telephony.DataConnection.FailCause.IPV6_ADDR_ERRORS;
            if(failcause != i)
                return;
        }
        if(apncontext.getApnType().equals(Phone.APN_TYPE_DEFAULT))
        {
            return;
        } else
        {
            CDMAPhone cdmaphone = mCdmaPhone;
            String s2 = apncontext.getReason();
            String s3 = apncontext.getApnType();
            cdmaphone.notifyDataConnectionFailed(s2, s3);
            return;
        }
    }

    private void on4GTechChange()
    {
        String as[] = new String[1];
        as[0] = "GETPSDIFNAME";
        PhoneBase phonebase = super.phone;
        Message message = obtainMessage(40);
        phonebase.invokeOemRilRequestStrings(as, message);
        boolean flag;
        ApnContext apncontext;
        if(super.phone.getServiceState().getRadioTechnology() == 13)
            flag = true;
        else
            flag = false;
        if(flag)
            mIsImsTriedInEhprd = false;
        apncontext = (ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT);
        if(apncontext != null)
        {
            Iterator iterator;
            ApnContext apncontext1;
            int k;
            if(flag)
            {
                RetryManager retrymanager = apncontext.getRetryMgr();
                String s = SystemProperties.get("ro.gsm.data_retry_config");
                boolean flag1 = retrymanager.configure(s);
            } else
            {
                RetryManager retrymanager1 = apncontext.getRetryMgr();
                String s1 = SystemProperties.get("ro.cdma.data_retry_config");
                boolean flag2 = retrymanager1.configure(s1);
            }
            removeMessages(5);
            if(apncontext.getReconnectIntent() != null)
            {
                AlarmManager alarmmanager = (AlarmManager)super.phone.getContext().getSystemService("alarm");
                PendingIntent pendingintent = apncontext.getReconnectIntent();
                alarmmanager.cancel(pendingintent);
                apncontext.setReconnectIntent(null);
            }
            if(mIsImsConnected || !flag)
            {
                int i = apncontext.getState();
                int j = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
                if(i != j)
                    requestDataSetup(Phone.APN_TYPE_DEFAULT, "4GHandOver", 0);
            }
        }
        iterator = mApnContexts.entrySet().iterator();
        do
        {
            do
            {
                do
                {
                    if(!iterator.hasNext())
                        return;
                    apncontext1 = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
                } while(apncontext1.mApnInactivityTracker == null);
                apncontext1.mApnInactivityTracker.stopInactivityTimerAlarm();
            } while(apncontext1.getActiveApn().timer <= 0);
            apncontext1.mApnInactivityTracker.startInactivityTimerAlarm();
            k = Log.v(LOG_TAG, "Handover occured. Stop and Restart the inactivity Timer");
        } while(true);
    }

    private void onApnChanged()
    {
        ApnContext apncontext1;
        int k;
        int l;
        ApnContext apncontext = (ApnContext)mApnContexts.get("fota");
        if(apncontext != null && apncontext.isEnabled())
        {
            int i = apncontext.getState();
            int j = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
            if(i == j)
            {
                log("delay to clean all connections since fota link here");
                removeMessages(29);
                Message message = obtainMessage(29);
                boolean flag = sendMessageDelayed(message, 60000L);
                return;
            }
        }
        apncontext1 = (ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT);
        k = apncontext1.getState();
        l = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
        if(k == l) goto _L2; else goto _L1
_L1:
        int i1;
        int j1;
        i1 = apncontext1.getState();
        j1 = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        if(i1 == j1) goto _L2; else goto _L3
_L3:
        boolean flag1 = true;
_L5:
        createAllApnList();
        int k1 = getState();
        int l1 = com.android.internal.telephony.DataConnectionTracker.State.DISCONNECTING;
        if(k1 == l1)
            return;
        log("onApnChanged clean all connection");
        boolean flag2 = isConnected();
        cleanUpAllConnections(flag2, "apnChanged");
        if(flag1)
        {
            return;
        } else
        {
            apncontext1.getRetryMgr().resetRetryCount();
            apncontext1.setReason("apnChanged");
            boolean flag3 = trySetupData(apncontext1);
            return;
        }
_L2:
        flag1 = false;
        if(true) goto _L5; else goto _L4
_L4:
    }

    private void onGprsAttached()
    {
        log("***onGprsAttached");
        ApnContext apncontext = (ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT);
        apncontext.setReason("gprsAttached");
        int i = apncontext.getState();
        int j = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
        if(i == j)
        {
            startNetStatPoll();
            com.android.internal.telephony.DataConnectionTracker.State state = apncontext.getState();
            super.state = state;
            PhoneBase phonebase = super.phone;
            String s = apncontext.getApnType();
            phonebase.notifyDataConnection("gprsAttached", s);
            return;
        }
        int k = apncontext.getState();
        int l = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        if(k == l)
        {
            log("onGprsAttached clean connection");
            cleanUpConnection(false, apncontext);
            apncontext.getRetryMgr().resetRetryCount();
        }
        boolean flag = trySetupData(apncontext);
    }

    private void onPdpStateChanged(ArrayList arraylist, boolean flag, ApnContext apncontext)
    {
        if(apncontext == null)
            return;
        int i = apncontext.getState();
        int j = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
        if(i != j)
            return;
        int k = apncontext.getActivePdp().getCid();
        if(!pdpStatesHasCID(arraylist, k))
        {
            int l = Log.i(LOG_TAG, "PDP connection has dropped. Reconnecting");
            int i1 = -1;
            CdmaCellLocation cdmacelllocation = (CdmaCellLocation)super.phone.getCellLocation();
            if(cdmacelllocation != null)
                i1 = cdmacelllocation.getBaseStationId();
            Object aobj[] = new Object[2];
            Integer integer = Integer.valueOf(i1);
            aobj[0] = integer;
            Integer integer1 = Integer.valueOf(TelephonyManager.getDefault().getNetworkType());
            aobj[1] = integer1;
            int j1 = EventLog.writeEvent(50109, aobj);
            cleanUpConnection(true, apncontext);
            return;
        }
        int k1 = apncontext.getActivePdp().getCid();
        if(!pdpStatesHasActiveCID(arraylist, k1))
        {
            if(!flag)
            {
                String s = apncontext.getApnType();
                PhoneBase phonebase = super.phone;
                if(TextUtils.equals(s, Phone.APN_TYPE_IMS))
                {
                    log("to poll extra reason");
                    byte byte1 = (byte)apncontext.getActivePdp().getCid();
                    byte abyte0[] = new byte[9];
                    abyte0[0] = 2;
                    abyte0[1] = 5;
                    abyte0[2] = 0;
                    abyte0[3] = 24;
                    abyte0[4] = 0;
                    abyte0[5] = 0;
                    abyte0[6] = 0;
                    abyte0[7] = 1;
                    abyte0[8] = byte1;
                    PhoneBase phonebase1 = super.phone;
                    Message message = obtainMessage(42, apncontext);
                    phonebase1.invokeOemRilRequestRaw(abyte0, message);
                }
                CommandsInterface commandsinterface = super.phone.mCM;
                Message message1 = obtainMessage(11);
                commandsinterface.getPDPContextList(message1);
                return;
            }
            int l1 = Log.i(LOG_TAG, "PDP connection has dropped (active=false case).  Reconnecting");
            byte byte0 = -1;
            CdmaCellLocation cdmacelllocation1 = (CdmaCellLocation)super.phone.getCellLocation();
            int i2;
            if(cdmacelllocation1 != null)
                i2 = cdmacelllocation1.getBaseStationId();
            Object aobj1[] = new Object[2];
            Integer integer2 = Integer.valueOf(byte0);
            aobj1[0] = integer2;
            Integer integer3 = Integer.valueOf(TelephonyManager.getDefault().getNetworkType());
            aobj1[1] = integer3;
            int j2 = EventLog.writeEvent(50109, aobj1);
            cleanUpConnection(true, apncontext);
            return;
        }
        GsmDataConnection gsmdataconnection = apncontext.getActivePdp();
        if(!isIpAddrChanged(arraylist, gsmdataconnection))
        {
            return;
        } else
        {
            int k2 = Log.i(LOG_TAG, "PDP IP Adressed changed, clean up connection");
            cleanUpConnection(true, apncontext);
            return;
        }
    }

    private String[] parseTypes(String s)
    {
        String as[];
        if(s == null || s.length() == 0)
        {
            as = new String[1];
            as[0] = "*";
        } else
        {
            as = s.split(",");
        }
        return as;
    }

    private boolean
    pdpStatesHasCID (ArrayList<DataCallState> states, int cid) {
        for (int i = 0, s = states.size() ; i < s ; i++) {
            if (states.get(i).cid == cid) return true;
        }

        return false;
    }

    private boolean
    pdpStatesHasActiveCID (ArrayList<DataCallState> states, int cid) {
        for (int i = 0, s = states.size() ; i < s ; i++) {
            if ((states.get(i).cid == cid) && (states.get(i).active != 0)) {
                return true;
            }
        }

        return false;
    }

    private void reconnectAfterFail(com.android.internal.telephony.DataConnection.FailCause failcause, ApnContext apncontext)
    {
label0:
        {
            if(apncontext == null)
            {
                int i = Log.d(LOG_TAG, "It is impossible");
                return;
            }
            int j = apncontext.getState();
            int k = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
            if(j != k)
                return;
            if(!apncontext.getRetryMgr().isRetryNeeded())
            {
                if(!mReregisterOnReconnectFailure)
                    break label0;
                apncontext.getRetryMgr().retryForeverUsingLastTimeout();
            }
            int l = apncontext.getRetryMgr().getRetryTimer();
            StringBuilder stringbuilder = (new StringBuilder()).append("PDP activate failed. Scheduling next attempt for ");
            int i1 = l / 1000;
            String s = stringbuilder.append(i1).append("s").toString();
            int j1 = Log.d(LOG_TAG, s);
            int l1;
            com.android.internal.telephony.DataConnectionTracker.State state;
            CDMAPhone cdmaphone;
            String s2;
            String s3;
            ApnContext apncontext1;
            int i2;
            if(l < 15000 && apncontext.getApnType().equals(Phone.APN_TYPE_DEFAULT))
            {
                String s1 = apncontext.getReason();
                requestDataSetup(Phone.APN_TYPE_DEFAULT, s1, l);
            } else
            {
                AlarmManager alarmmanager = (AlarmManager)super.phone.getContext().getSystemService("alarm");
                IntentFilter intentfilter = new IntentFilter();
                StringBuilder stringbuilder1 = (new StringBuilder()).append("com.android.internal.telephony.gprs-reconnect.");
                String s4 = apncontext.getApnType();
                String s5 = stringbuilder1.append(s4).toString();
                intentfilter.addAction(s5);
                Context context = mCdmaPhone.getContext();
                BroadcastReceiver broadcastreceiver = mIntentReceiver;
                CDMAPhone cdmaphone1 = mCdmaPhone;
                Intent intent = context.registerReceiver(broadcastreceiver, intentfilter, null, cdmaphone1);
                StringBuilder stringbuilder2 = (new StringBuilder()).append("com.android.internal.telephony.gprs-reconnect.");
                String s6 = apncontext.getApnType();
                String s7 = stringbuilder2.append(s6).toString();
                Intent intent1 = new Intent(s7);
                String s8 = apncontext.getReason();
                Intent intent2 = intent1.putExtra("reason", s8);
                String s9 = apncontext.getApnType();
                Intent intent3 = intent1.putExtra("type", s9);
                PendingIntent pendingintent = PendingIntent.getBroadcast(super.phone.getContext(), 0, intent1, 0);
                apncontext.mReconnectIntent = pendingintent;
                long l2 = SystemClock.elapsedRealtime();
                long l3 = l;
                long l4 = l2 + l3;
                PendingIntent pendingintent1 = apncontext.mReconnectIntent;
                alarmmanager.set(2, l4, pendingintent1);
            }
            apncontext.getRetryMgr().increaseRetryCount();
            if(!shouldPostNotification(failcause))
            {
                int k1 = Log.d(LOG_TAG, "NOT Posting GPRS Unavailable notification -- likely transient error");
                return;
            } else
            {
                notifyNoData(failcause, apncontext);
                return;
            }
        }
        l1 = Log.d(LOG_TAG, "PDP activate failed, indicate to connectivity service");
        apncontext.getRetryMgr().resetRetryCount();
        state = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        apncontext.setState(state);
        if(!apncontext.getApnType().equals(Phone.APN_TYPE_DEFAULT))
        {
            cdmaphone = mCdmaPhone;
            s2 = apncontext.getReason();
            s3 = apncontext.getApnType();
            cdmaphone.notifyDataConnectionFailed(s2, s3);
        }
        if(mIsImsTriedInEhprd)
            return;
        if(super.phone.getServiceState().getRadioTechnology() == 13)
            return;
        if(!TextUtils.equals(apncontext.getApnType(), Phone.APN_TYPE_IMS))
            return;
        mIsImsTriedInEhprd = true;
        apncontext1 = (ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT);
        if(apncontext1 != null)
            apncontext1.getRetryMgr().increaseRetryCount();
        i2 = APN_DELAY_MILLIS;
        requestDataSetup(Phone.APN_TYPE_DEFAULT, "IMSLinkFailed", i2);
    }

    private void requestDataSetup(String s, String s1, int i)
    {
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        if(apncontext == null)
            return;
        apncontext.setReason(s1);
        int j = apncontext.getState();
        int k = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        if(j == k)
            cleanUpConnection(false, apncontext);
        Message message = obtainMessage(5, apncontext);
        long l = i;
        boolean flag = sendMessageDelayed(message, l);
        if(mWakeLock == null)
            return;
        if(i <= 0)
            return;
        if(i >= 30000)
        {
            return;
        } else
        {
            android.os.PowerManager.WakeLock wakelock = mWakeLock;
            long l1 = i;
            wakelock.acquire(l1);
            return;
        }
    }

    private void resetPollStats()
    {
        super.txPkts = 65535L;
        super.rxPkts = 65535L;
        super.sentSinceLastRecv = 0L;
        super.netStatPollPeriod = 1000;
        super.mNoRecvPollCount = 0;
    }

    private boolean retryAfterDisconnected(String s)
    {
        boolean flag = true;
        if("radioTurnedOff".equals(s) || "dataDisabled".equals(s))
            flag = false;
        return flag;
    }

    private void runPingTest() {
        try {
            int i = -1;
            String s = android.provider.Settings.Secure.getString(mResolver, android.provider.Settings.PDP_WATCHDOG_PING_ADDRESS);
            int j = android.provider.Settings.Secure.getInt(mResolver, android.provider.Settings.PDP_WATCHDOG_PING_DEADLINE, 5);
            log("pinging " + s + " for " + Integer(j).toString() + "s");
            if (s != null && !"0.0.0.0".equals(s)) {
                i = Runtime.getRuntime().exec("ping -c 1 -i 1 -w " + Integer(j).toString() + " " + s).waitFor();
            }
        } catch IOException ioexception {
            Log.w(LOG_TAG, "ping failed: IOException");
        } catch Exception exception {
            Log.w(LOG_TAG, "exception trying to ping");
        }
        if (i == 0) {
            EventLog.writeEvent(50103, -1);
            mPdpResetCount = 0;
            sendMessage(obtainMessage(EVENT_START_NETSTAT_POLL));
        } else {
            sendMessage(obtainMessage(EVENT_START_RECOVERY));
        }
    }

    private void setEnabled(String s, boolean flag) {
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        if(apncontext == null)
        {
            return;
        } else
        {
            apncontext.setEnabled(flag);
            return;
        }
    }

    private boolean setupData(ApnContext apncontext)
    {
        log("enter setupData!");
        String s = apncontext.getApnType();
        int i = parseOemType(s);
        ApnSetting apnsetting = apncontext.getNextApn();
        boolean flag;
        if(apnsetting == null)
        {
            log("setupData: return for no apn found!");
            flag = false;
        } else
        if((super.phone.getServiceState().getRadioTechnology() == 14 || super.phone.getServiceState().getRadioTechnology() == 13) && !apnsetting.enabled)
        {
            log("setupData: apn is disabled by carrier!");
            flag = false;
        } else
        {
            GsmDataConnection gsmdataconnection;
            if(i > 1000)
                gsmdataconnection = findReadyPdpForOem(i);
            else
                gsmdataconnection = findReadyPdp(apnsetting);
            if(gsmdataconnection == null)
            {
                log("setupData: No ready GsmDataConnection found!");
                gsmdataconnection = findFreePdp();
            }
            if(gsmdataconnection == null)
            {
                log("setupData: No free GsmDataConnection found!");
                flag = false;
            } else
            {
                apncontext.setActiveApn(apnsetting);
                apncontext.setActivePdp(gsmdataconnection);
                gsmdataconnection.setProfileId(i);
                Message message = obtainMessage();
                message.what = 1;
                message.obj = apncontext;
                log("pdp connect!");
                gsmdataconnection.connect(message, apnsetting);
                com.android.internal.telephony.DataConnectionTracker.State state = com.android.internal.telephony.DataConnectionTracker.State.INITING;
                apncontext.setState(state);
                com.android.internal.telephony.DataConnectionTracker.State state1 = com.android.internal.telephony.DataConnectionTracker.State.INITING;
                setState(state1);
                String s1 = apncontext.getApnType();
                super.mRequestedApnType = s1;
                PhoneBase phonebase = super.phone;
                String s2 = apncontext.getReason();
                String s3 = apncontext.getApnType();
                phonebase.notifyDataConnection(s2, s3);
                log("setupData: initing!");
                flag = true;
            }
        }
        return flag;
    }

    private void setupDnsProperties()
    {
        int i = Process.myPid();
        String as[] = getDnsServers(null);
        int j = 0;
        if(as == null)
            return;
        int k = 0;
        do
        {
            int l = as.length;
            if(k >= l)
                break;
            String s = as[k];
            if(!TextUtils.equals(s, "0.0.0.0"))
            {
                StringBuilder stringbuilder = (new StringBuilder()).append("net.dns");
                int i1 = k + 1;
                SystemProperties.set(stringbuilder.append(i1).append(".").append(i).toString(), s);
                j++;
            }
            k++;
        } while(true);
        for(int j1 = j + 1; j1 <= 4; j1++)
        {
            String s1 = (new StringBuilder()).append("net.dns").append(j1).append(".").append(i).toString();
            if(SystemProperties.get(s1).length() != 0)
                SystemProperties.set(s1, "");
        }

        String s2 = SystemProperties.get("net.dnschange");
        if(s2.length() == 0)
            return;
        try
        {
            int k1 = Integer.parseInt(s2);
            StringBuilder stringbuilder1 = (new StringBuilder()).append("");
            int l1 = k1 + 1;
            String s3 = stringbuilder1.append(l1).toString();
            SystemProperties.set("net.dnschange", s3);
            return;
        }
        catch(NumberFormatException numberformatexception)
        {
            return;
        }
    }

    private boolean shouldPostNotification(com.android.internal.telephony.DataConnection.FailCause failcause)
    {
        if(false) goto _L2; else goto _L1
_L1:
        int i = com.android.internal.telephony.DataConnection.FailCause.UNKNOWN;
        if(failcause == i) goto _L2; else goto _L3
_L3:
        boolean flag = true;
_L5:
        return flag;
_L2:
        flag = false;
        if(true) goto _L5; else goto _L4
_L4:
    }


    private void startPeriodicPdpPoll()
    {
        removeMessages(7);
        Message message = obtainMessage(7);
        boolean flag = sendMessageDelayed(message, 5000L);
    }

    private boolean trySetupData(ApnContext apncontext)
    {
        StringBuilder stringbuilder = (new StringBuilder()).append("trySetupData2 for type:");
        String s = apncontext.getApnType();
        StringBuilder stringbuilder1 = stringbuilder.append(s).append(" due to ");
        String s1 = apncontext.getReason();
        String s2 = stringbuilder1.append(s1).toString();
        log(s2);
        StringBuilder stringbuilder2 = (new StringBuilder()).append("[DSAC DEB] trySetupData2 with mIsPsRestricted=");
        boolean flag = mIsPsRestricted;
        String s3 = stringbuilder2.append(flag).toString();
        int i = Log.d(LOG_TAG, s3);
        if(super.phone.getSimulatedRadioControl() == null) goto _L2; else goto _L1
_L1:
        boolean flag1;
        com.android.internal.telephony.DataConnectionTracker.State state = State.CONNECTED;
        apncontext.setState(state);
        PhoneBase phonebase = super.phone;
        String s4 = apncontext.getReason();
        String s5 = apncontext.getApnType();
        phonebase.notifyDataConnection(s4, s5);
        int j = Log.i(LOG_TAG, "(fix?) We're on the simulator; assuming data is connected");
        flag1 = true;
_L4:
        return flag1;
_L2:
label0:
        {
            ArrayList arraylist;
label1:
            {
                int k;
                boolean flag2;
                boolean flag3;
                boolean flag4;
                boolean flag5;
label2:
                {
                    k = mCdmaPhone.mSST.getCurrentGprsState();
                    flag2 = mCdmaPhone.mSST.getDesiredPowerState();
                    int l;
                    int i1;
                    if(SystemProperties.getInt("radio.lte.ignoreims", 0) == 1)
                        flag3 = true;
                    else
                        flag3 = false;
                    if(mIsNewArch)
                    {
                        if(mRuimRecords != null && mRuimRecords.getRecordsLoaded())
                            flag4 = true;
                        else
                            flag4 = false;
                    } else
                    if(isLteTestWithoutCsim || mCdmaPhone.mCsimRecords != null && mCdmaPhone.mCsimRecords.getRecordsLoaded())
                        flag4 = true;
                    else
                        flag4 = false;
                    if(super.phone.getServiceState().getRadioTechnology() == 13)
                        flag5 = true;
                    else
                        flag5 = false;
                    l = apncontext.getState();
                    i1 = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
                    if(l != i1)
                    {
                        int j1 = apncontext.getState();
                        int k1 = com.android.internal.telephony.DataConnectionTracker.State.SCANNING;
                        if(j1 != k1)
                            break label2;
                    }
                    if((k == 0 || noAutoAttach) && flag4)
                    {
                        int l1 = super.phone.getState();
                        int i2 = com.android.internal.telephony.Phone.State.IDLE;
                        if((l1 == i2 || mCdmaPhone.mSST.isConcurrentVoiceAndData()) && isDataAllowed(apncontext) && !mIsPsRestricted && (mIsImsConnected || (apncontext.getApnType().equals(Phone.APN_TYPE_IMS) ? !flag3 : flag3 || !flag5 && mIsImsTriedInEhprd)) && flag2)
                        {
                            int j2 = apncontext.getState();
                            int k2 = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
                            if(j2 != k2)
                                break label0;
                            String s6 = apncontext.getApnType();
                            arraylist = buildWaitingApns(s6);
                            if(arraylist.isEmpty())
                            {
                                log("No APN found");
                                com.android.internal.telephony.DataConnection.FailCause failcause = com.android.internal.telephony.DataConnection.FailCause.MISSING_UNKNOWN_APN;
                                notifyNoData(failcause, apncontext);
                                flag1 = false;
                                continue; /* Loop/switch isn't completed */
                            }
                            break label1;
                        }
                    }
                }
label3:
                {
                    StringBuilder stringbuilder3 = (new StringBuilder()).append("trySetupData: Not ready for data:  dataState=");
                    com.android.internal.telephony.DataConnectionTracker.State state1 = apncontext.getState();
                    StringBuilder stringbuilder4 = stringbuilder3.append(state1).append(" gprsState=").append(k).append(" sim=").append(flag4).append(" UMTS=");
                    boolean flag6 = mCdmaPhone.mSST.isConcurrentVoiceAndData();
                    StringBuilder stringbuilder5 = stringbuilder4.append(flag6).append(" phoneState=");
                    com.android.internal.telephony.Phone.State state2 = super.phone.getState();
                    StringBuilder stringbuilder6 = stringbuilder5.append(state2).append(" isDataAllowed=");
                    boolean flag7 = isDataAllowed(apncontext);
                    StringBuilder stringbuilder7 = stringbuilder6.append(flag7).append(" dataEnabled=");
                    boolean flag8 = getAnyDataEnabled();
                    StringBuilder stringbuilder8 = stringbuilder7.append(flag8).append(" ps restricted=");
                    boolean flag9 = mIsPsRestricted;
                    StringBuilder stringbuilder9 = stringbuilder8.append(flag9).append(" desiredPowerState=").append(flag2).append(" mIsImsConnected=");
                    boolean flag10 = mIsImsConnected;
                    StringBuilder stringbuilder10 = stringbuilder9.append(flag10).append(" ignoreImsPdn=").append(flag3).append(" isLTE=").append(flag5).append(" mIsImsTriedInEhrpd=");
                    boolean flag11 = mIsImsTriedInEhprd;
                    StringBuilder stringbuilder11 = stringbuilder10.append(flag11).append(" MasterDataEnabled=");
                    boolean flag12 = mMasterDataEnabled;
                    String s7 = stringbuilder11.append(flag12).toString();
                    log(s7);
                    if(apncontext.getApnType().equals(Phone.APN_TYPE_DEFAULT))
                        break label3;
                    int l2 = apncontext.getState();
                    int i3 = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
                    if(l2 != i3)
                    {
                        int j3 = apncontext.getState();
                        int k3 = com.android.internal.telephony.DataConnectionTracker.State.SCANNING;
                        if(j3 != k3)
                            break label3;
                    }
                    CDMAPhone cdmaphone = mCdmaPhone;
                    String s8 = apncontext.getReason();
                    String s9 = apncontext.getApnType();
                    cdmaphone.notifyDataConnectionFailed(s8, s9);
                }
                flag1 = false;
                continue; /* Loop/switch isn't completed */
            }
            apncontext.setWaitingApns(arraylist);
            StringBuilder stringbuilder12 = (new StringBuilder()).append("Create from allApns : ");
            ArrayList arraylist1 = allApns;
            String s10 = apnListToString(arraylist1);
            String s11 = stringbuilder12.append(s10).toString();
            log(s11);
        }
        StringBuilder stringbuilder13 = (new StringBuilder()).append("Setup watingApns : ");
        ArrayList arraylist2 = apncontext.getWaitingApns();
        String s12 = apnListToString(arraylist2);
        String s13 = stringbuilder13.append(s12).toString();
        log(s13);
        String s14 = apncontext.getReason();
        apncontext.setReason(s14);
        flag1 = setupData(apncontext);
        if(true) goto _L4; else goto _L3
_L3:
    }

    private boolean trySetupData(String s, String s1)
    {
        ApnContext apncontext;
        boolean flag1;
        StringBuilder stringbuilder = (new StringBuilder()).append("***trySetupData for type:").append(s1).append(" due to ");
        String s2;
        String s3;
        StringBuilder stringbuilder1;
        boolean flag;
        String s4;
        int i;
        String s5;
        if(s == null)
            s2 = "(unspecified)";
        else
            s2 = s;
        s3 = stringbuilder.append(s2).toString();
        log(s3);
        stringbuilder1 = (new StringBuilder()).append("[DSAC DEB] trySetupData with mIsPsRestricted=");
        flag = mIsPsRestricted;
        s4 = stringbuilder1.append(flag).toString();
        i = Log.d(LOG_TAG, s4);
        if(s1 == null)
            s1 = Phone.APN_TYPE_DEFAULT;
        apncontext = (ApnContext)mApnContexts.get(s1);
        if(apncontext != null) goto _L2; else goto _L1
_L1:
        s5 = (new StringBuilder()).append("***new apn context for type:").append(s1).toString();
        log(s5);
        apncontext = new ApnContext(s1);
        if(apncontext != null) goto _L4; else goto _L3
_L3:
        log("***new apn context failed ");
        flag1 = false;
_L6:
        return flag1;
_L4:
        Object obj = mApnContexts.put(s1, apncontext);
_L2:
        apncontext.setReason(s);
        flag1 = trySetupData(apncontext);
        if(true) goto _L6; else goto _L5
_L5:
    }

    /**
     * @deprecated Method disableApnType is deprecated
     */

    public int disableApnType(String s)
    {
        this;
        JVM INSTR monitorenter ;
        ApnContext apncontext;
        String s1 = (new StringBuilder()).append("calling disableApnType with type:").append(s).toString();
        log(s1);
        apncontext = (ApnContext)mApnContexts.get(s);
        if(apncontext == null || !apncontext.isEnabled()) goto _L2; else goto _L1
_L1:
        apncontext.setEnabled(false);
        if("fota".equalsIgnoreCase(s) && hasMessages(29))
        {
            log("disableApnType fota, resend delayed APN_CHANGED");
            removeMessages(29);
            Message message = obtainMessage(29);
            boolean flag = sendMessage(message);
        }
        if(!apncontext.isApnTypeActive()) goto _L4; else goto _L3
_L3:
        int i;
        int j;
        i = apncontext.getState();
        j = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
        if(i == j) goto _L4; else goto _L5
_L5:
        int k;
        int l;
        k = apncontext.getState();
        l = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        if(k == l) goto _L4; else goto _L6
_L6:
        Message message1 = obtainMessage(34);
        message1.arg1 = 1;
        apncontext.setReason("dataDisabled");
        message1.obj = apncontext;
        boolean flag1 = sendMessage(message1);
        log("return APN_REQUEST_STARTED");
        int i1 = 1;
_L8:
        this;
        JVM INSTR monitorexit ;
        return i1;
_L4:
        log("return APN_ALREADY_INACTIVE");
        i1 = 4;
        continue; /* Loop/switch isn't completed */
_L2:
        log("no apn context was found, or not enabled apn context, return APN_REQUEST_FAILED");
        i1 = 3;
        if(true) goto _L8; else goto _L7
_L7:
        Exception exception;
        exception;
        throw exception;
    }

    /**
     * @deprecated Method enableApnType is deprecated
     */

    public int enableApnType(String s)
    {
        this;
        JVM INSTR monitorenter ;
        String s1 = (new StringBuilder()).append("calling enableApnType with type:").append(s).toString();
        log(s1);
        if(mApnContexts != null) goto _L2; else goto _L1
_L1:
        int i = Log.e(LOG_TAG, "mApnContexts is null, return fail");
        byte byte0 = 3;
_L4:
        this;
        JVM INSTR monitorexit ;
        return byte0;
_L2:
        ApnContext apncontext;
label0:
        {
label1:
            {
label2:
                {
                    apncontext = (ApnContext)mApnContexts.get(s);
                    if(apncontext == null)
                    {
                        if(!isApnTypeAvailable(s))
                            break label2;
                        apncontext = new ApnContext(s);
                        String s2 = (new StringBuilder()).append("New apn type context for type ").append(s).toString();
                        log(s2);
                        Object obj = mApnContexts.put(s, apncontext);
                    }
                    StringBuilder stringbuilder = (new StringBuilder()).append("enableApnType(").append(s).append(")").append(", state(");
                    com.android.internal.telephony.DataConnectionTracker.State state = apncontext.getState();
                    String s3 = stringbuilder.append(state).append(")").toString();
                    int j = Log.d(LOG_TAG, s3);
                    if(!apncontext.isApnTypeActive())
                        break label0;
                    log("apn type is active");
                    apncontext.setEnabled(true);
                    int k = apncontext.getState();
                    int l = com.android.internal.telephony.DataConnectionTracker.State.INITING;
                    if(k == l)
                    {
                        log("return APN_REQUEST_STARTED");
                        byte0 = 1;
                        continue; /* Loop/switch isn't completed */
                    }
                    break label1;
                }
                log("return APN_REQUEST_FAILED");
                byte0 = 3;
                continue; /* Loop/switch isn't completed */
            }
            int i1 = apncontext.getState();
            int j1 = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
            if(i1 == j1)
            {
                log("return APN_ALREADY_ACTIVE");
                byte0 = 0;
                continue; /* Loop/switch isn't completed */
            }
        }
        if(!isApnTypeAvailable(s))
        {
            log("return APN_TYPE_NOT_AVAILABLE");
            byte0 = 2;
            continue; /* Loop/switch isn't completed */
        }
        apncontext.setEnabled(true);
        super.mRequestedApnType = s;
        apncontext.setReason("dataEnabled");
        String s4 = (new StringBuilder()).append("new apn request for type ").append(s).append(" is to be handled").toString();
        log(s4);
        Message message = obtainMessage(23, apncontext);
        boolean flag = sendMessage(message);
        log("return APN_REQUEST_STARTED");
        byte0 = 1;
        if(true) goto _L4; else goto _L3
_L3:
        Exception exception;
        exception;
        throw exception;
    }


    protected String getActiveApnString()
    {
        Iterator iterator;
        log("get 1st active apn string");
        iterator = mApnContexts.entrySet().iterator();
_L4:
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
        if(!apncontext.isEnabled() || !apncontext.isApnTypeActive()) goto _L4; else goto _L3
_L3:
        String s = apncontext.getActiveApn().apn;
_L6:
        return s;
_L2:
        s = null;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public String getActiveApnString(String s)
    {
        String s1 = (new StringBuilder()).append("get active apn string for type:").append(s).toString();
        log(s1);
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        String s2;
        if(apncontext != null && apncontext.isEnabled() && apncontext.isApnTypeActive())
            s2 = apncontext.getActiveApn().apn;
        else
            s2 = null;
        return s2;
    }

    public String[] getActiveApnTypes()
    {
        log("get all active apn types");
        ArrayList arraylist = new ArrayList();
        Iterator iterator = mApnContexts.entrySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
            if(apncontext.isEnabled() && apncontext.isApnTypeActive())
            {
                String s = apncontext.getApnType();
                boolean flag = arraylist.add(s);
            }
        } while(true);
        String as[] = new String[0];
        return (String[])(String[])arraylist.toArray(as);
    }

    public String getActiveInternetInterfaceName()
    {
        String s = null;
        Iterator iterator = mApnContexts.entrySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
            if(apncontext.isEnabled() && apncontext.isApnTypeActive() && apncontext.getActivePdp() != null && apncontext.getActivePdp().getProfileId() == 103 && apncontext.getActiveApn() != null)
                s = apncontext.getActivePdp().getInterface();
        } while(true);
        return s;
    }

    public ArrayList getAllDataConnections()
    {
        return (ArrayList)pdpList.clone();
    }

    public boolean getAnyDataEnabled()
    {
        Iterator iterator = mApnContexts.entrySet().iterator();
_L4:
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        if(!((ApnContext)((java.util.Map.Entry)iterator.next()).getValue()).isEnabled()) goto _L4; else goto _L3
_L3:
        boolean flag = true;
_L6:
        return flag;
_L2:
        flag = false;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public int getCid(String s)
    {
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        int i;
        if(apncontext != null && apncontext.getActivePdp() != null)
            i = apncontext.getActivePdp().getCid();
        else
            i = 0;
        return i;
    }

    public boolean getDataEnabled()
    {
        return ((ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT)).isEnabled();
    }

    protected String[] getDnsServers(String s)
    {
        String s1 = (new StringBuilder()).append("getDnsServers for apn type:").append(s).toString();
        log(s1);
        if(s != null) goto _L2; else goto _L1
_L1:
        Iterator iterator = mApnContexts.entrySet().iterator();
_L6:
        if(!iterator.hasNext()) goto _L4; else goto _L3
_L3:
        ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
        if(apncontext.getActivePdp() == null) goto _L6; else goto _L5
_L5:
        String as[] = apncontext.getActivePdp().getDnsServers();
_L8:
        return as;
_L4:
        as = null;
        continue; /* Loop/switch isn't completed */
_L2:
        ApnContext apncontext1 = (ApnContext)mApnContexts.get(s);
        if(apncontext1 != null && apncontext1.getActivePdp() != null)
            as = apncontext1.getActivePdp().getDnsServers();
        else
            as = null;
        if(true) goto _L8; else goto _L7
_L7:
    }

    public String getGateway(String s)
    {
        String s1 = (new StringBuilder()).append("getGateway for apn type:").append(s).toString();
        log(s1);
        if(s != null) goto _L2; else goto _L1
_L1:
        Iterator iterator = mApnContexts.entrySet().iterator();
_L6:
        if(!iterator.hasNext()) goto _L4; else goto _L3
_L3:
        ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
        if(apncontext.getActivePdp() == null) goto _L6; else goto _L5
_L5:
        String s2 = apncontext.getActivePdp().getGatewayAddress();
_L8:
        return s2;
_L4:
        s2 = null;
        continue; /* Loop/switch isn't completed */
_L2:
        ApnContext apncontext1 = (ApnContext)mApnContexts.get(s);
        if(apncontext1 != null && apncontext1.getActivePdp() != null)
            s2 = apncontext1.getActivePdp().getGatewayAddress();
        else
            s2 = null;
        if(true) goto _L8; else goto _L7
_L7:
    }

    public String[] getGateways(String s)
    {
        String s1 = (new StringBuilder()).append("getGateway for apn type:").append(s).toString();
        log(s1);
        if(s != null) goto _L2; else goto _L1
_L1:
        Iterator iterator = mApnContexts.entrySet().iterator();
_L6:
        if(!iterator.hasNext()) goto _L4; else goto _L3
_L3:
        ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
        if(apncontext.getActivePdp() == null) goto _L6; else goto _L5
_L5:
        String as[] = apncontext.getActivePdp().getGatewayAddresses();
_L8:
        return as;
_L4:
        as = null;
        continue; /* Loop/switch isn't completed */
_L2:
        ApnContext apncontext1 = (ApnContext)mApnContexts.get(s);
        if(apncontext1 != null && apncontext1.getActivePdp() != null)
            as = apncontext1.getActivePdp().getGatewayAddresses();
        else
            as = null;
        if(true) goto _L8; else goto _L7
_L7:
    }

    protected String getInterfaceName(String s)
    {
        String s1 = (new StringBuilder()).append("getInterfaceName for apn type:").append(s).toString();
        log(s1);
        if(s != null) goto _L2; else goto _L1
_L1:
        Iterator iterator = mApnContexts.entrySet().iterator();
_L6:
        if(!iterator.hasNext()) goto _L4; else goto _L3
_L3:
        ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
        if(apncontext.getActivePdp() == null) goto _L6; else goto _L5
_L5:
        String s2;
        log("get active pdp is not null, return interface");
        s2 = apncontext.getActivePdp().getInterface();
_L8:
        return s2;
_L4:
        log("return null");
        s2 = null;
        continue; /* Loop/switch isn't completed */
_L2:
        ApnContext apncontext1 = (ApnContext)mApnContexts.get(s);
        if(apncontext1 != null && apncontext1.getActivePdp() != null)
        {
            log("get active pdp is not null, return interface");
            s2 = apncontext1.getActivePdp().getInterface();
        } else
        {
            log("return null");
            s2 = null;
        }
        if(true) goto _L8; else goto _L7
_L7:
    }

    protected String getIpAddress(String s)
    {
        String s1 = (new StringBuilder()).append("getIpAddress for apn type:").append(s).toString();
        log(s1);
        if(s != null) goto _L2; else goto _L1
_L1:
        Iterator iterator = mApnContexts.entrySet().iterator();
_L6:
        if(!iterator.hasNext()) goto _L4; else goto _L3
_L3:
        ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
        if(apncontext.getActivePdp() == null) goto _L6; else goto _L5
_L5:
        String s2 = apncontext.getActivePdp().getIpAddress();
_L8:
        return s2;
_L4:
        s2 = null;
        continue; /* Loop/switch isn't completed */
_L2:
        ApnContext apncontext1 = (ApnContext)mApnContexts.get(s);
        if(apncontext1 != null && apncontext1.getActivePdp() != null)
            s2 = apncontext1.getActivePdp().getIpAddress();
        else
            s2 = null;
        if(true) goto _L8; else goto _L7
_L7:
    }

    protected String[] getIpAddresses(String s)
    {
        String s1 = (new StringBuilder()).append("getIpAddress for apn type:").append(s).toString();
        log(s1);
        if(s != null) goto _L2; else goto _L1
_L1:
        Iterator iterator = mApnContexts.entrySet().iterator();
_L6:
        if(!iterator.hasNext()) goto _L4; else goto _L3
_L3:
        ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
        if(apncontext.getActivePdp() == null) goto _L6; else goto _L5
_L5:
        String as[] = apncontext.getActivePdp().getIpAddresses();
_L8:
        return as;
_L4:
        as = null;
        continue; /* Loop/switch isn't completed */
_L2:
        ApnContext apncontext1 = (ApnContext)mApnContexts.get(s);
        if(apncontext1 != null && apncontext1.getActivePdp() != null)
            as = apncontext1.getActivePdp().getIpAddresses();
        else
            as = null;
        if(true) goto _L8; else goto _L7
_L7:
    }

    public int getIpVersion(String s)
    {
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        int i;
        if(apncontext != null && apncontext.getActivePdp() != null)
            i = apncontext.getActivePdp().getIpVersion();
        else
            i = 0;
        return i;
    }

    public com.android.internal.telephony.DataConnectionTracker.State getState()
    {
        boolean flag;
        GsmDataConnection gsmdataconnection;
        com.android.internal.telephony.DataConnectionTracker.State state;
        flag = false;
        boolean flag1;
        Iterator iterator;
        if(super.phone.getServiceState().getRadioTechnology() == 14 || super.phone.getServiceState().getRadioTechnology() == 13)
            flag1 = true;
        else
            flag1 = false;
        if(!flag1)
            break MISSING_BLOCK_LABEL_110;
        iterator = pdpList.iterator();
_L3:
        if(!iterator.hasNext())
            break MISSING_BLOCK_LABEL_110;
        gsmdataconnection = (GsmDataConnection)(DataConnection)iterator.next();
        if(!gsmdataconnection.isActive()) goto _L2; else goto _L1
_L1:
        log("overall state is CONNECTED");
        state = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
_L4:
        return state;
_L2:
        if(gsmdataconnection.isActivating())
            flag = true;
          goto _L3
        if(flag)
        {
            log("overall state is CONNECTING");
            state = com.android.internal.telephony.DataConnectionTracker.State.CONNECTING;
        } else
        {
            log("overall state is IDLE");
            state = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
        }
          goto _L4
    }

    public State getState(String s) {
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        com.android.internal.telephony.DataConnectionTracker.State state;
        if(apncontext != null)
            state = apncontext.getState();
        else
            state = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        return state;
    }

    boolean handlePollDataCallsStateResult(AsyncResult asyncresult)
    {
        boolean flag = false;
        String as[] = (String[])(String[])asyncresult.result;
        boolean flag1;
        if(asyncresult.exception != null)
        {
            flag1 = false;
        } else
        {
            int i = as.length / 3;
            for(Iterator iterator = pdpList.iterator(); iterator.hasNext();)
            {
                GsmDataConnection gsmdataconnection = (GsmDataConnection)(DataConnection)iterator.next();
                int j = 0;
                while(j < i) 
                {
                    int k = j * 3;
                    int l = Integer.parseInt(as[k]);
                    int i1 = j * 3 + 1;
                    int j1 = Integer.parseInt(as[i1]);
                    int k1 = j * 3 + 2;
                    String s = as[k1];
                    if(gsmdataconnection.isActive() && gsmdataconnection.getCid() == l)
                    {
                        log("A handover has occured, switch the interface name!!!");
                        gsmdataconnection.handle4gHandover(s);
                        flag = true;
                    }
                    j++;
                }
            }

            flag1 = flag;
        }
        return flag1;
    }

    void handlePollExtraDataReason(AsyncResult asyncresult)
    {
        log("poll extra data reason done");
        if(asyncresult.exception != null)
        {
            log("error, do nothing");
            return;
        }
        if(asyncresult.userObj instanceof ApnContext)
        {
            ApnContext apncontext = (ApnContext)asyncresult.userObj;
            StringBuilder stringbuilder = (new StringBuilder()).append("apn type is ");
            String s = apncontext.getApnType();
            String s1 = stringbuilder.append(s).toString();
            log(s1);
            if(!(asyncresult.result instanceof byte[]))
                return;
            byte abyte0[] = (byte[])(byte[])asyncresult.result;
            StringBuilder stringbuilder1 = (new StringBuilder()).append("length of the byte is ");
            int i = abyte0.length;
            StringBuilder stringbuilder2 = stringbuilder1.append(i).append("; hex[");
            String s2 = HexDump.dumpHexString(abyte0);
            String s3 = stringbuilder2.append(s2).append("]; string[").append(abyte0).append("]").toString();
            log(s3);
            if(abyte0.length < 8)
                return;
            IntBuffer intbuffer = ByteBuffer.wrap(abyte0).asIntBuffer();
            StringBuilder stringbuilder3 = (new StringBuilder()).append("ret[0]=");
            int j = intbuffer.get(0);
            StringBuilder stringbuilder4 = stringbuilder3.append(j).append(";ret[1]=");
            int k = intbuffer.get(1);
            String s4 = stringbuilder4.append(k).toString();
            log(s4);
            if(intbuffer.get(0) != 2)
                return;
            if(intbuffer.get(1) != 206)
            {
                return;
            } else
            {
                apncontext.setReason("imsPdnNwInitiated");
                return;
            }
        } else
        {
            log("maybe a invalid request ");
            return;
        }
    }

    protected boolean isApnTypeActive(String s)
    {
        ApnContext apncontext = (ApnContext)mApnContexts.get(s);
        boolean flag;
        if(apncontext != null && apncontext.isApnTypeActive())
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean isApnTypeAvailable(String s)
    {
        if(allApns == null) goto _L2; else goto _L1
_L1:
        Iterator iterator = allApns.iterator();
_L5:
        if(!iterator.hasNext()) goto _L2; else goto _L3
_L3:
        if(!((ApnSetting)iterator.next()).canHandleType(s)) goto _L5; else goto _L4
_L4:
        boolean flag = true;
_L7:
        return flag;
_L2:
        flag = false;
        if(true) goto _L7; else goto _L6
_L6:
    }

    public boolean isDataConnectionAsDesired()
    {
        boolean flag = super.phone.getServiceState().getRoaming();
        boolean flag1;
        boolean flag2;
        if(mIsNewArch)
        {
            if(mRuimRecords != null && mRuimRecords.getRecordsLoaded())
                flag1 = true;
            else
                flag1 = false;
        } else
        if(isLteTestWithoutCsim || mCdmaPhone.mCsimRecords != null && mCdmaPhone.mCsimRecords.getRecordsLoaded())
            flag1 = true;
        else
            flag1 = false;
        if(flag1 && mCdmaPhone.mSST.getCurrentGprsState() == 0 && (!flag || getDataOnRoamingEnabled()) && !super.mIsWifiConnected && !mIsPsRestricted)
        {
            int i = getState();
            int j = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
            if(i == j)
                flag2 = true;
            else
                flag2 = false;
        } else
        {
            flag2 = true;
        }
        return flag2;
    }

    protected void onAutoDisconnect(ApnContext apncontext)
    {
        log("on auto disconnect");
        if(apncontext == null)
        {
            log("No contexts of mms or default");
            return;
        }
        apncontext.setEnabled(false);
        if(!apncontext.isApnTypeActive())
        {
            return;
        } else
        {
            cleanUpConnection(true, apncontext);
            return;
        }
    }


    protected void onDataSetupComplete(AsyncResult asyncresult)
    {
        ApnContext apncontext;
        boolean flag;
        boolean flag1;
        apncontext = null;
        if(asyncresult.userObj instanceof ApnContext)
            apncontext = (ApnContext)asyncresult.userObj;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i;
        LteCdmaDataConnectionTracker ltecdmadataconnectiontracker;
        String s;
        String s1;
        int j;
        Context context;
        String s2;
        LteCdmaDataConnectionTracker ltecdmadataconnectiontracker1;
        String s3;
        String s4;
        int k;
        LteCdmaDataConnectionTracker ltecdmadataconnectiontracker2;
        DataInactivityTracker datainactivitytracker;
        LteCdmaDataConnectionTracker ltecdmadataconnectiontracker3;
        ApnContext apncontext1;
        if(super.phone.getServiceState().getRadioTechnology() == 13)
            flag = true;
        else
            flag = false;
        flag1 = TextUtils.equals(apncontext.getApnType(), Phone.APN_TYPE_IMS);
        if(asyncresult.exception != null) goto _L2; else goto _L1
_L1:
        if(!flag1) goto _L4; else goto _L3
_L3:
        boolean flag2;
        if(SystemProperties.getInt("radio.lte.ignoreims", 0) == 2)
            flag2 = true;
        else
            flag2 = false;
        flag3 = false;
        if(flag2)
        {
            flag4 = true;
            mIsImsConnected = flag4;
            flag3 = true;
        }
        if(!flag && !mIsImsTriedInEhprd)
        {
            flag5 = true;
            mIsImsTriedInEhprd = flag5;
            flag3 = true;
        }
        if(flag3)
        {
            i = APN_DELAY_MILLIS;
            ltecdmadataconnectiontracker = this;
            s = Phone.APN_TYPE_DEFAULT;
            s1 = "IMSLinkUp";
            j = i;
            ltecdmadataconnectiontracker.requestDataSetup(s, s1, j);
        }
_L5:
        if(apncontext.getActiveApn().timer > 0)
        {
            context = super.phone.getContext();
            s2 = apncontext.getApnType();
            ltecdmadataconnectiontracker1 = this;
            s3 = s2;
            s4 = ltecdmadataconnectiontracker1.getInterfaceName(s3);
            k = apncontext.getActiveApn().timer;
            ltecdmadataconnectiontracker2 = this;
            datainactivitytracker = new DataInactivityTracker(ltecdmadataconnectiontracker2, context, s4, k, apncontext);
            apncontext.mApnInactivityTracker = datainactivitytracker;
            apncontext.mApnInactivityTracker.startInactivityTimerAlarm();
        }
        ltecdmadataconnectiontracker3 = this;
        apncontext1 = apncontext;
        ltecdmadataconnectiontracker3.notifyDefaultData(apncontext1);
        return;
_L4:
label0:
        {
label1:
            {
                if(!TextUtils.equals(apncontext.getApnType(), Phone.APN_TYPE_DEFAULT) || !apncontext.isApnTypeActive())
                    break label0;
                SystemProperties.set("gsm.defaultpdpcontext.active", "true");
                if(!canSetPreferApn || preferredApn != null)
                    continue; /* Loop/switch isn't completed */
                int l = Log.d(LOG_TAG, "PREFERED APN is null");
                ApnSetting apnsetting = apncontext.getActiveApn();
                preferredApn = apnsetting;
                if(candidatePreferredApn != null)
                {
                    int i1 = candidatePreferredApn.id;
                    int j1 = preferredApn.id;
                    if(i1 == j1)
                        break label1;
                }
                int k1 = preferredApn.id;
                LteCdmaDataConnectionTracker ltecdmadataconnectiontracker4 = this;
                int l1 = k1;
                ltecdmadataconnectiontracker4.setPreferredApn(l1);
            }
            if(candidatePreferredApn != null)
            {
                ApnSetting apnsetting1 = null;
                candidatePreferredApn = apnsetting1;
            }
            continue; /* Loop/switch isn't completed */
        }
        SystemProperties.set("gsm.defaultpdpcontext.active", "false");
        if(true) goto _L5; else goto _L2
_L2:
        int i2 = (com.android.internal.telephony.DataConnection.FailCause)(com.android.internal.telephony.DataConnection.FailCause)asyncresult.result;
        String s5 = (new StringBuilder()).append("PDP setup failed ").append(i2).toString();
        LteCdmaDataConnectionTracker ltecdmadataconnectiontracker5 = this;
        String s6 = s5;
        ltecdmadataconnectiontracker5.log(s6);
        if(i2.isEventLoggable())
        {
            int j2 = -1;
            CdmaCellLocation cdmacelllocation = (CdmaCellLocation)super.phone.getCellLocation();
            if(cdmacelllocation != null)
                j2 = cdmacelllocation.getBaseStationId();
            Object aobj[] = new Object[3];
            Integer integer = Integer.valueOf(i2.ordinal());
            aobj[0] = integer;
            Integer integer1 = Integer.valueOf(j2);
            aobj[1] = integer1;
            Integer integer2 = Integer.valueOf(TelephonyManager.getDefault().getNetworkType());
            aobj[2] = integer2;
            int k2 = EventLog.writeEvent(50105, aobj);
        }
        boolean flag6 = false;
        if(i2.isPermanentFail())
            flag6 = true;
        if(!TextUtils.equals(apncontext.getApnType(), Phone.APN_TYPE_DEFAULT))
        {
            int l2 = com.android.internal.telephony.DataConnection.FailCause.IPV6_ADDR_ERRORS;
            if(i2 == l2)
            {
                apncontext.setReason("no IPv6 address");
                flag6 = true;
            }
        }
        if(flag6)
        {
            LteCdmaDataConnectionTracker ltecdmadataconnectiontracker6 = this;
            com.android.internal.telephony.DataConnection.FailCause failcause = i2;
            ApnContext apncontext2 = apncontext;
            ltecdmadataconnectiontracker6.notifyNoData(failcause, apncontext2);
            if(flag)
                return;
            if(!flag1)
                return;
            if(mIsImsTriedInEhprd)
                return;
            boolean flag7 = true;
            mIsImsTriedInEhprd = flag7;
            ApnContext apncontext3 = (ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT);
            if(apncontext3 != null)
                apncontext3.getRetryMgr().increaseRetryCount();
            int i3 = APN_DELAY_MILLIS;
            LteCdmaDataConnectionTracker ltecdmadataconnectiontracker7 = this;
            String s7 = Phone.APN_TYPE_DEFAULT;
            String s8 = "IMSLinkFailed";
            int j3 = i3;
            ltecdmadataconnectiontracker7.requestDataSetup(s7, s8, j3);
            return;
        }
        apncontext.removeNextApn();
        if(apncontext.getWaitingApns().isEmpty())
        {
            LteCdmaDataConnectionTracker ltecdmadataconnectiontracker8 = this;
            com.android.internal.telephony.DataConnection.FailCause failcause1 = i2;
            ApnContext apncontext4 = apncontext;
            ltecdmadataconnectiontracker8.startDelayedRetry(failcause1, apncontext4);
            return;
        } else
        {
            com.android.internal.telephony.DataConnectionTracker.State state = com.android.internal.telephony.DataConnectionTracker.State.SCANNING;
            apncontext.setState(state);
            LteCdmaDataConnectionTracker ltecdmadataconnectiontracker9 = this;
            byte byte0 = 5;
            ApnContext apncontext5 = apncontext;
            Message message = ltecdmadataconnectiontracker9.obtainMessage(byte0, apncontext5);
            long l3 = APN_DELAY_MILLIS;
            LteCdmaDataConnectionTracker ltecdmadataconnectiontracker10 = this;
            Message message1 = message;
            long l4 = l3;
            boolean flag8 = ltecdmadataconnectiontracker10.sendMessageDelayed(message1, l4);
            return;
        }
    }

    protected void onDisconnectDone(AsyncResult asyncresult)
    {
        ApnContext apncontext = null;
        log("EVENT_DISCONNECT_DONE");
        if(asyncresult.userObj instanceof ApnContext)
            apncontext = (ApnContext)asyncresult.userObj;
        com.android.internal.telephony.DataConnectionTracker.State state = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
        setState(state);
        com.android.internal.telephony.DataConnectionTracker.State state1 = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
        apncontext.setState(state1);
        if(apncontext.mApnInactivityTracker != null)
        {
            apncontext.mApnInactivityTracker.stopInactivityTimerAlarm();
            apncontext.mApnInactivityTracker = null;
        }
        PhoneBase phonebase = super.phone;
        String s = apncontext.getReason();
        String s1 = apncontext.getApnType();
        phonebase.notifyDataConnection(s, s1);
        apncontext.setActiveApn(null);
        boolean flag;
        if(getActiveApnTypes().length == 0)
            flag = mCdmaPhone.mSST.processPendingRadioPowerOffAfterDataOff();
        if(!TextUtils.equals(apncontext.getApnType(), Phone.APN_TYPE_DEFAULT))
            return;
        String s2 = apncontext.getReason();
        if(!retryAfterDisconnected(s2))
        {
            return;
        } else
        {
            String s3 = apncontext.getReason();
            int i = APN_DELAY_MILLIS;
            requestDataSetup(Phone.APN_TYPE_DEFAULT, s3, i);
            return;
        }
    }

    protected void onEnableNewApn(ApnContext apncontext)
    {
        apncontext.getRetryMgr().resetRetryCount();
        log("onEnableNewApn setup data");
        int i = apncontext.getState();
        int j = com.android.internal.telephony.DataConnectionTracker.State.FAILED;
        if(i == j)
        {
            log("previous state is FAILED, reset to IDLE");
            com.android.internal.telephony.DataConnectionTracker.State state = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
            apncontext.setState(state);
        }
        boolean flag = trySetupData(apncontext);
    }

    protected void onGprsDetached()
    {
        stopNetStatPoll();
        notifyAllEnabledDataConnection("gprsDetached");
    }

    protected void onPdpStateChanged(AsyncResult asyncresult, boolean flag)
    {
        ArrayList arraylist = (ArrayList)(ArrayList)asyncresult.result;
        if(asyncresult.exception != null)
            return;
        Iterator iterator = mApnContexts.entrySet().iterator();
        do
        {
            if(!iterator.hasNext())
                return;
            ApnContext apncontext = (ApnContext)((java.util.Map.Entry)iterator.next()).getValue();
            onPdpStateChanged(arraylist, flag, apncontext);
        } while(true);
    }

    protected void onPollPdp()
    {
        int i = getState();
        int j = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
        if(i != j)
        {
            return;
        } else
        {
            CommandsInterface commandsinterface = super.phone.mCM;
            Message message = obtainMessage(11);
            commandsinterface.getPDPContextList(message);
            Message message1 = obtainMessage(7);
            boolean flag = sendMessageDelayed(message1, 5000L);
            return;
        }
    }

    protected void onRadioAvailable()
    {
        if(super.phone.getSimulatedRadioControl() != null)
        {
            com.android.internal.telephony.DataConnectionTracker.State state = com.android.internal.telephony.DataConnectionTracker.State.CONNECTED;
            setState(state);
            super.phone.notifyDataConnection(null);
            int i = Log.i(LOG_TAG, "We're on the simulator; assuming data is connected");
        }
        int j = getState(Phone.APN_TYPE_DEFAULT);
        int k = com.android.internal.telephony.DataConnectionTracker.State.IDLE;
        if(j == k)
        {
            return;
        } else
        {
            cleanUpConnection(true, null);
            return;
        }
    }

    protected void onRadioOffOrNotAvailable() {
        ((ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT)).getRetryMgr().resetRetryCount();
        mReregisterOnReconnectFailure = false;
        if(super.phone.getSimulatedRadioControl() != null)
        {
            int i = Log.i(LOG_TAG, "We're on the simulator; assuming radio off is meaningless");
            return;
        } else
        {
            log("Radio is off and clean up all connection");
            cleanUpAllConnections(false, "radioTurnedOff");
            return;
        }
    }

    protected void onRecordsLoaded() {
        createAllApnList();
        ApnContext apncontext = (ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT);
        if (apncontext != null) {
            apncontext.setReason("simLoaded");
            if (apncontext.getState() == State.FAILED) {
                log("onRecordsLoaded clean connection");
                cleanUpConnection(false, apncontext);
            }
            Message message = obtainMessage(EVENT_TRY_SETUP_DATA, apncontext);
            sendMessage(message);
        }
    }

    protected void onResetDone(AsyncResult asyncresult)
    {
        log("EVENT_RESET_DONE");
        String s = null;
        if(asyncresult.userObj instanceof String)
            s = (String)asyncresult.userObj;
        gotoIdleAndNotifyDataConnection(s);
    }

    protected void onRoamingOff() {
        trySetupData("roamingOff", Phone.APN_TYPE_DEFAULT);
    }

    protected void onRoamingOn() {
        if (getDataOnRoamingEnabled()) {
            trySetupData("roamingOn", Phone.APN_TYPE_DEFAULT);
        }
        if (super.phone.getServiceState().getRadioTechnology() != DATA_ACCESS_EHRPD) {
            log("Tear down data connection on roaming.");
            cleanUpAllConnections(true, "roamingOn");
        }
    }

    protected boolean onTrySetupData(ApnContext apncontext) {
        return trySetupData(apncontext);
    }

    protected boolean onTrySetupData(String reason) {
        return trySetupData(reason, Phone.APN_TYPE_DEFAULT);
    }

    protected boolean onTrySetupData(String reason String apnType) {
        return trySetupData(reason, apnType);
    }

    int parseOemType(String s)
    {
        String as[];
        String s1 = (new StringBuilder()).append("parse type :").append(s).toString();
        log(s1);
        as = s.split(":");
        if(as.length < 2) goto _L2; else goto _L1
_L1:
        StringBuilder stringbuilder = (new StringBuilder()).append("oem profile id:");
        String s2 = as[1];
        String s3 = stringbuilder.append(s2).toString();
        log(s3);
        String s4 = as[0].trim();
        as[0] = s4;
        String s5 = as[1].trim();
        as[1] = s5;
        if(!TextUtils.equals(as[0], "OEM")) goto _L2; else goto _L3
_L3:
        int i;
        int j;
        i = 0;
        j = 1;
        int k = Integer.parseInt(as[j]);
        i = k;
_L8:
        if(i <= 1000) goto _L2; else goto _L4
_L4:
        int l;
        String s6 = (new StringBuilder()).append(" return pid:").append(i).toString();
        log(s6);
        l = i;
_L6:
        return l;
_L2:
        log(" not a oem type");
        if(TextUtils.equals(s, Phone.APN_TYPE_IMS))
            l = 101;
        else
        if(TextUtils.equals(s, "fota"))
            l = 102;
        else
        if(TextUtils.equals(s, "cbs") || TextUtils.equals(s, "hipri"))
            l = 104;
        else
            l = 103;
        if(true) goto _L6; else goto _L5
_L5:
        NumberFormatException numberformatexception;
        numberformatexception;
        if(true) goto _L8; else goto _L7
_L7:
    }

    protected void restartRadio() {
        Log.d(LOG_TAG, "************TURN OFF RADIO**************");
        cleanUpAllConnections(true, "radioTurnedOff");
        super.phone.mCM.setRadioPower(false, null);
        SystemProperties.set("net.ppp.reset-by-timeout", String.valueOf(Integer.parseInt(SystemProperties.get("net.ppp.reset-by-timeout", "0")) + 1));
    }

    protected void setState(com.android.internal.telephony.DataConnectionTracker.State state)
    {
        String s = (new StringBuilder()).append("setState: ").append(state).toString();
        log(s);
        com.android.internal.telephony.DataConnectionTracker.State state1 = getState();
        super.state = state1;
        StringBuilder stringbuilder = (new StringBuilder()).append("overall state: ");
        com.android.internal.telephony.DataConnectionTracker.State state2 = super.state;
        String s1 = stringbuilder.append(state2).toString();
        log(s1);
    }

    protected void startNetStatPoll() {
        if (getState() = State.CONNECTED) {
            if (!mPingTestActive) {
                if (!super.netStatPollEnabled) {
                    Log.d(LOG_TAG, "[DataConnection] Start poll NetStat");
                    resetPollStats();
                    super.netStatPollEnabled = true;
                    mPollNetStat.run();
                }
            }
        }
    }

    protected void startNetStatPollNoReset() {
        if (getState() = State.CONNECTED) {
            if (!mPingTestActive) {
                if (!super.netStatPollEnabled) {
                    Log.d(LOG_TAG, "[DataConnection] Start poll NetStat");
                    super.netStatPollEnabled = true;
                    mPollNetStat.run();
                }
            }
        }
    }

    protected void stopNetStatPoll() {
        super.netStatPollEnabled = false;
        removeCallbacks(mPollNetStat);
        Log.d(LOG_TAG, "[DataConnection] Stop poll NetStat");
    }

    protected void onVoiceCallStarted() {
        if (isConnected() && !mCdmaPhone.mSST.isConcurrentVoiceAndData()) {
            stopNetStatPoll();
            notifyAllEnabledDataConnection(Phone.REASON_VOICE_CALL_STARTED);
        }
    }

    protected void onVoiceCallEnded() {
        if (isConnected()) {
            if (!mCdmaPhone.mSST.isConcurrentVoiceAndData()) {
                startNetStatPoll();
                notifyAllEnabledDataConnection(Phone.REASON_VOICE_CALL_ENDED);
            } else {
                // clean slate after call end.
                resetPollStats();
            }
        } else {
            // reset reconnect timer
            ((ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT)).getRetryMgr().resetRetryCount();
            mReregisterOnReconnectFailure = false;
            // in case data setup was attempted when we were on a voice call
            trySetupData(Phone.REASON_VOICE_CALL_ENDED, Phone.APN_TYPE_DEFAULT);
        }
    }

    protected void onCleanUpConnection(boolean flag, ApnContext apncontext)
    {
        log("onCleanUpConnection clean 1 connection");
        cleanUpConnection(flag, apncontext);
    }

    protected void onCleanUpConnection(boolean flag, String apnType)
    {
        log("onCleanUpConnection clean all connections");
        cleanUpAllConnections(flag, apnType);
    }

    /**
     * create a list for all possible pdps with all apns associated with that pdp
     */
    private void createAllApnList() {
        allApns = new ArrayList<ApnSetting>();
        String operator;
        if(isLteTestNoOpCheck) {
            operator = "311480";
            Log.v(LOG_TAG, "createAllApnList: Use hard-coded operator: 311480");
        } else {
            operator = SystemProperties.get("gsm.sim.operator.numeric");
        }

        if (operator != null) {
            String selection = "numeric = '" + operator + "'";
            Cursor cursor = phone.getContext().getContentResolver().query(
                    Telephony.Carriers.CONTENT_URI, null, selection, null, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    allApns = createApnList(cursor);
                }
                cursor.close();
            }
        }

        if (allApns.isEmpty()) {
            log("No APN found for carrier: " + operator);
            preferredApn = null;
            notifyNoData(FailCause.MISSING_UNKNOWN_APN);
        } else {
            log("before reset mRequestedApnType: type=" + super.mRequestedApnType);
            super.mRequestedApnType = Phone.APN_TYPE_DEFAULT;
            if (candidatePreferredApn == null) {
                preferredApn = getPreferredApn();
                Log.d(LOG_TAG, "Get PreferredAPN");
            }
            if(preferredApn != null && !preferredApn.numeric.equals(s)) {
                preferredApn = null;
                setPreferredApn(-1);
                return;
            }
            if (canSetPreferApn && preferredApn == null && candidatePreferredApn == null) {
                for(ApnSetting p:allApns) {
                   if (p.canHandleType(Phone.APN_TYPE_DEFAULT)) {
                      candidatePreferredApn = p;
                      setPreferredApn(candidatePreferredApn.id);
                      break;
                   }
                }
            }
        }
    }

    private void createAllPdpList() {
        pdpList = new ArrayList<DataConnection>();
        DataConnection pdp;

        for (int i = 0; i < PDP_CONNECTION_POOL_SIZE; i++) {
            pdp = GsmDataConnection.makeDataConnection(mGsmPhone);
            pdpList.add(pdp);
        }
    }

    private void destroyAllPdpList() {
        if (pdpList != null) {
            pdpList.removeAll(pdpList);
        }
    }

    /**
     *
     * @return waitingApns list to be used to create PDP
     *          error when waitingApns.isEmpty()
     */
    private ArrayList<ApnSetting> buildWaitingApns(String apnType) {
        ArrayList<ApnSetting> apnList = new ArrayList<ApnSetting>();
        String operator;
        if (isLteTestNoOpCheck) {
            operator = "311480";
            Log.v(LOG_TAG, "buildWaitingApns: Use hard-coded operator: 311480");
        } else {
            operator = SystemProperties.get("gsm.sim.operator.numeric");
        }

        if (apnType.equals(Phone.APN_TYPE_DEFAULT) {
            if (canSetPreferApn && preferredApn != null) {
                Log.i(LOG_TAG, "Preferred APN:" + operator + ":"
                        + preferredApn.numeric + ":" + preferredApn);
                if (preferredApn.numeric.equals(operator)) {
                    Log.i(LOG_TAG, "Waiting APN set to preferred APN");
                    apnList.add(preferredApn);
                    return apnList;
                } else {
                    setPreferredApn(-1);
                    preferredApn = null;
                }
            }
        }
        if (allApns != null) {
            for (ApnSetting apn : allApns) {
                if (apn.canHandleType(apnType)) {
                    apnList.add(apn);
                }
            }
        }
        return apnList;
    }

    private String apnListToString (ArrayList<ApnSetting> apns) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, size = apns.size(); i < size; i++) {
            result.append('[')
                  .append(apns.get(i).toString())
                  .append(']');
        }
        return result.toString();
    }

    private void startDelayedRetry(DataConnection.FailCause failcause, ApnContext apncontext) {
        notifyNoData(failcause, apncontext);
        reconnectAfterFail(failcause, apncontext);
    }

    private void setPreferredApn(int pos) {
        if (!canSetPreferApn) {
            return;
        }

        ContentResolver resolver = phone.getContext().getContentResolver();
        resolver.delete(PREFERAPN_URI, null, null);

        if (pos >= 0) {
            ContentValues values = new ContentValues();
            values.put(APN_ID, pos);
            resolver.insert(PREFERAPN_URI, values);
        }
    }

    private ApnSetting getPreferredApn() {
        if (allApns.isEmpty()) {
            return null;
        }

        Cursor cursor = phone.getContext().getContentResolver().query(
                PREFERAPN_URI, new String[] { "_id", "name", "apn" },
                null, null, Telephony.Carriers.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            canSetPreferApn = true;
        } else {
            canSetPreferApn = false;
        }

        if (canSetPreferApn && cursor.getCount() > 0) {
            int pos;
            cursor.moveToFirst();
            pos = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers._ID));
            for(ApnSetting p:allApns) {
                if (p.id == pos && p.canHandleType(mRequestedApnType)) {
                    cursor.close();
                    return p;
                }
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public void handleMessage (Message msg) {
        Log.d(LOG_TAG,"LteCdmaDataConnTrack handleMessage " + msg);

        switch(msg.what) {
            case EVENT_RECORDS_LOADED:
                onRecordsLoaded();
                break;

            case EVENT_ENABLE_NEW_APN:
                ApnContext apncontext = null;
                if (msg.obj instanceof ApnContext)
                    apncontext = (ApnContext)msg.obj;
                onEnableNewApn(apncontext);
                break;

            case EVENT_GPRS_DETACHED:
                onGprsDetached();
                break;

            case EVENT_GPRS_ATTACHED:
                onGprsAttached();
                break;

            case EVENT_DATA_STATE_CHANGED:
                onPdpStateChanged((AsyncResult)message.obj, false);
                break;

            case EVENT_GET_PDP_LIST_COMPLETE:
                onPdpStateChanged((AsyncResult)msg.obj, true);
                break;

            case EVENT_POLL_PDP:
                onPollPdp();
                break;

            case EVENT_START_NETSTAT_POLL:
                mPingTestActive = false;
                startNetStatPoll();
                break;

            case EVENT_START_RECOVERY:
                mPingTestActive = false;
                doRecovery();
                break;

            case EVENT_APN_CHANGED:
                onApnChanged();
                break;

            case EVENT_PS_RESTRICT_ENABLED:
                /**
                 * We don't need to explicitly to tear down the PDP context
                 * when PS restricted is enabled. The base band will deactive
                 * PDP context and notify us with PDP_CONTEXT_CHANGED.
                 * But we should stop the network polling and prevent reset PDP.
                 */
                Log.d(LOG_TAG, "[DSAC DEB] EVENT_PS_RESTRICT_ENABLED " + Boolean(mIsPsRestricted).toString());
                stopNetStatPoll();
                mIsPsRestricted = true;
                break;

            case EVENT_PS_RESTRICT_DISABLED:
                /**
                 * When PS restrict is removed, we need setup PDP connection if
                 * PDP connection is down.
                 */
                Log.d(LOG_TAG, "[DSAC DEB] " + "EVENT_PS_RESTRICT_DISABLED " + mIsPsRestricted);
                mIsPsRestricted  = false;
                if(getState(Phone.APN_TYPE_DEFAULT) == State.CONNECTED) {
                    startNetStatPoll();
                } else {
                    if(getState(Phone.APN_TYPE_DEFAULT) == State.FAILED) {
                        cleanUpAllConnections(false, Phone.REASON_PS_RESTRICT_ENABLED);
                        ((ApnContext)mApnContexts.get(Phone.APN_TYPE_DEFAULT)).getRetryMgr().resetRetryCount();
                        mReregisterOnReconnectFailure = false;
                    }
                    trySetupData(Phone.REASON_PS_RESTRICT_ENABLED, Phone.APN_TYPE_DEFAULT);
                }
                break;

            case EVENT_TRY_SETUP_DATA:
                if(message.obj instanceof ApnContext) {
                    onTrySetupData((ApnContext)msg.obj);
                } else if (message.obj instanceof String) {
                    onTrySetupData((String)msg.obj);
                }
                break;

            case EVENT_CLEAN_UP_CONNECTION:
                boolean flag = (msg.arg1 != 0);
                if (msg.obj instanceof ApnContext) {
                    onCleanUpConnection(flag, (ApnContext)msg.obj);
                } else {
                    onCleanUpConnection(flag, (String)message.obj);
                }
                break;

            case EVENT_4G_TECHNOLOGY_CHANGE:
                on4GTechChange();
                break;

            case EVENT_POLL_DATA_CALLS_STATE:
                Log.d(LOG_TAG, "EVENT_POLL_DATA_CALLS_STATE: change interface names for active data calls");
                if (handlePollDataCallsStateResult((AsyncResult)message.obj)) {
                    notifyAllEnabledDataConnection("handoverOccurred");
                }
                break;

            case EVENT_POLL_EXTRA_DATA_REASON:
                handlePollExtraDataReason((AsyncResult)message.obj);
                break;

            case EVENT_ICC_CHANGED:
                updateIccAvailability();
                break;

            default:
                // handle the message in the super class DataConnectionTracker
                super.handleMessage(msg);
                break;
        }
    }

    void updateIccAvailability() {
        if (mIccCardManager != null) {
            RuimCard ruimcard = (RuimCard)mIccCardManager.getApplication(mOwnerModemId, 2);
            if (mRuimCard != ruimcard) {
                if(mRuimCard != null) {
                    log("Removing stale LTE Application.");
                    if(mRuimRecords != null) {
                        mRuimRecords.unregisterForRecordsLoaded(this);
                        mRuimRecords = null;
                    }
                    mRuimCard = null;
                }
                if (ruimcard != null) {
                    log("New LTE application found");
                    mRuimCard = ruimcard;
                    mRuimRecords = (RuimRecords)mRuimCard.getApplicationRecords();
                    mRuimRecords.registerForRecordsLoaded(this, EVENT_RECORDS_LOADED, null);
                    return;
                }
            }
        }
    }
 
    protected void log(String msg) {
        Log.d(LOG_TAG, "[LteCdmaDataConnectionTracker] " + msg);
    }

/*
    static boolean access$102(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, boolean flag)
    {
        ltecdmadataconnectiontracker.mIsScreenOn = flag;
        return flag;
    }

*/




/*
    static long access$1302(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.txPkts = l;
        return l;
    }

*/


/*
    static long access$1402(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.rxPkts = l;
        return l;
    }

*/


/*
    static long access$1502(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.txPkts = l;
        return l;
    }

*/


/*
    static long access$1602(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.rxPkts = l;
        return l;
    }

*/


/*
    static long access$1702(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.txPkts = l;
        return l;
    }

*/


/*
    static long access$1802(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.rxPkts = l;
        return l;
    }

*/






/*
    static long access$2202(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.sentSinceLastRecv = l;
        return l;
    }

*/


/*
    static int access$2302(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, int i)
    {
        ltecdmadataconnectiontracker.mPdpResetCount = i;
        return i;
    }

*/



/*
    static long access$2514(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        long l1 = ((DataConnectionTracker) (ltecdmadataconnectiontracker)).sentSinceLastRecv + l;
        ltecdmadataconnectiontracker.sentSinceLastRecv = l1;
        return l1;
    }

*/


/*
    static long access$2602(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.sentSinceLastRecv = l;
        return l;
    }

*/


/*
    static long access$2702(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.sentSinceLastRecv = l;
        return l;
    }

*/


/*
    static long access$2802(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, long l)
    {
        ltecdmadataconnectiontracker.sentSinceLastRecv = l;
        return l;
    }

*/




/*
    static com.android.internal.telephony.DataConnectionTracker.Activity access$3002(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, com.android.internal.telephony.DataConnectionTracker.Activity activity)
    {
        ltecdmadataconnectiontracker.activity = activity;
        return activity;
    }

*/









/*
    static int access$3808(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker)
    {
        int i = ((DataConnectionTracker) (ltecdmadataconnectiontracker)).mNoRecvPollCount;
        int j = i + 1;
        ltecdmadataconnectiontracker.mNoRecvPollCount = j;
        return i;
    }

*/


/*
    static int access$3902(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, int i)
    {
        ltecdmadataconnectiontracker.netStatPollPeriod = i;
        return i;
    }

*/





/*
    static boolean access$4202(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, boolean flag)
    {
        ltecdmadataconnectiontracker.mPingTestActive = flag;
        return flag;
    }

*/


/*
    static int access$4302(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, int i)
    {
        ltecdmadataconnectiontracker.mNoRecvPollCount = i;
        return i;
    }

*/


/*
    static int access$4402(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, int i)
    {
        ltecdmadataconnectiontracker.netStatPollPeriod = i;
        return i;
    }

*/


/*
    static int access$4502(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, int i)
    {
        ltecdmadataconnectiontracker.netStatPollPeriod = i;
        return i;
    }

*/





/*
    static boolean access$502(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, boolean flag)
    {
        ltecdmadataconnectiontracker.mIsWifiConnected = flag;
        return flag;
    }

*/


/*
    static boolean access$602(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, boolean flag)
    {
        ltecdmadataconnectiontracker.mIsWifiConnected = flag;
        return flag;
    }

*/



/*
    static boolean access$802(LteCdmaDataConnectionTracker ltecdmadataconnectiontracker, boolean flag)
    {
        ltecdmadataconnectiontracker.mIsImsConnected = flag;
        return flag;
    }

*/

}
