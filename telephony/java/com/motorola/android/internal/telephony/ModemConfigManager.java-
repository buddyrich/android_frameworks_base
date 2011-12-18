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
 * Edited by Hashcode [12-08-2011]
 */

package com.motorola.android.internal.telephony;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.*;
import com.android.internal.telephony.cdma.CDMAPhone;
import com.android.internal.telephony.gsm.GSMPhone;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.*;
import org.xmlpull.v1.XmlPullParser;


public class ModemConfigManager {
    public static final String LOG_TAG = "ModemConfigManager";

    public static final boolean DBG = false;

    public static final int MC_FROM_FILE_SYSTEM = 1;
    public static final int MC_FROM_XML = 0;

    private static Context mContext;
    private static ModemConfigManager mInstance;
    private static PhoneNotifier mPhoneNotifier;
    private ModemConfigFile mMCFile;
    private ArrayList<ModemInfo> mModemInfo;

    private int mMCFileSource;
    private boolean isMCFileLoaded;

    public class ModemInfo {
        public static final int MAX_RADIO_MODULE = 2;

        Context mContext;
        Phone mCdmaPhone;
        Phone mGsmPhone;
        Phone mPhoneProxy;
        CommandsInterface mCi;
        PhoneNotifier mPhoneNotifier;
        ArrayList<RadioModule> mRadioModules;

        int mCdmaSource;
        int mCurrentActivePhoneType;
        int mModemId;
        int mPreferredNetworkMode;
        int mRadioModuleNum;
        // final ModemConfigManager this$0;

        ModemInfo(Context context, PhoneNotifier phonenotifier, int modemId, int preferredNetworkMode, int cdmaSource, int radioModuleNum) {
            // this$0 = ModemConfigManager.this;
            // super();
            mContext = context;
            mModemId = modemId;
            mCdmaSource = cdmaSource;
            mPreferredNetworkMode = preferredNetworkMode;
            mPhoneNotifier = phonenotifier;
            mRadioModuleNum = radioModuleNum;
            mRadioModules = new ArrayList<RadioModule>(radioModuleNum);
            mCi = new RIL(mContext, mPreferredNetworkMode, mCdmaSource);
            mCurrentActivePhoneType = getDefaultActivePhoneType();
        }

        private void createPhone(int lPhoneType) {
            if (lPhoneType == Phone.PHONE_TYPE_CDMA) {
                /* Need to add ModemId to Phone */
                mCdmaPhone = new CDMAPhone(mContext, mCi, mPhoneNotifier, mModemId);
                return;
            }
            if (lPhoneType == Phone.PHONE_TYPE_GSM) {
                /* Need to add ModemId to Phone */
                mGsmPhone = new GSMPhone(mContext, mCi,  mPhoneNotifier, mModemId);
                return;
            }
            else {
                log("Get invalid phone type (SIP?) when createPhone, failed to create");
                return;
            }
        }

        private int getPhoneType(int i) {
            i;
            
            JVM INSTR tableswitch 0 8: default 52
        //                       0 61
        //                       1 61
        //                       2 61
        //                       3 61
        //                       4 56
        //                       5 56
        //                       6 56
        //                       7 66
        //                       8 74;
               goto _L1 _L2 _L2 _L2 _L2 _L3 _L3 _L3 _L4 _L5
_L1:
            int j = 1;
_L7:
            return j;
_L3:
            j = 2;
            continue; /* Loop/switch isn't completed */
_L2:
            j = 1;
            continue; /* Loop/switch isn't completed */
_L4:
            j = getPhoneTypeInGlobalNetworkMode();
            continue; /* Loop/switch isn't completed */
_L5:
            if(SystemProperties.getBoolean("ro.mot.lte_on_cdma", false))
                j = 2;
            else
                j = 1;
            if(true) goto _L7; else goto _L6
_L6:
        }

        void addRadioModule(RadioModule radiomodule) {
            boolean flag = mRadioModules.add(radiomodule);
            createPhone(radiomodule.getPhoneType());
        }

        public int getActivePhoneType() {
            return mCurrentActivePhoneType;
        }

        public Phone getCdmaPhone() {
            return mCdmaPhone;
        }

        public int getCdmaSubscriptionSource() {
            return mCdmaSource;
        }

        public Phone getDefaultActivePhone() {
            return getPhone(getDefaultActivePhoneType());
        }

        public int getDefaultActivePhoneType() {
            return getPhoneType(mPreferredNetworkMode);
        }

        public Phone getGsmPhone()
        {
            return mGsmPhone;
        }

        public int getModemId()
        {
            return mModemId;
        }

        public Phone getPhone(int lPhoneType) {
            Phone phone = null;
            if (lPhoneType == Phone.PHONE_TYPE_CDMA)
                phone = getCdmaPhone();
            else if (lPhoneType == Phone.PHONE_TYPE_GSM) {
                phone = getGsmPhone();
            }
            else {
                log("invalid phone type to get Phone (SIP?)");
            }
            return phone;
        }

        public Phone getPhoneProxy() {
            return mPhoneProxy;
        }

        public int getPhoneTypeInGlobalNetworkMode() {
            return SystemProperties.getInt("persist.radio.ap.phonetype", Phone.PHONE_TYPE_GSM);
        }

        public CommandsInterface getRIL() {
            return mCi;
        }

        public boolean isCdmaOnly() {
            if (mRadioModuleNum != 1) goto _L2; else goto _L1
_L1:
            RadioModule radiomodule = (RadioModule)mRadioModules.get(0);
            if(radiomodule == null || radiomodule.getPhoneType() != 2) goto _L2; else goto _L3
_L3:
            boolean flag = true;
_L5:
            return flag;
_L2:
            flag = false;
            if(true) goto _L5; else goto _L4
_L4:
        }

        public boolean isDualMode() {
            boolean flag = false;
            if (mRadioModuleNum == 2 && (mRadioModules.get(0) != null && ((RadioModule)mRadioModules.get(0)).getPhoneType() == Phone.PHONE_TYPE_CDMA && mRadioModules.get(1) != null && ((RadioModule)mRadioModules.get(1)).getPhoneType() == 1 || mRadioModules.get(0) != null && ((RadioModule)mRadioModules.get(0)).getPhoneType() == 1 && mRadioModules.get(1) != null && ((RadioModule)mRadioModules.get(1)).getPhoneType() == 2))
                flag = true;
            return flag;
        }

        public void setPhoneProxy(Phone phone) {
            mPhoneProxy = phone;
        }

        public void setPhoneTypeInGlobalNetworkMode(int lPhoneType) {
            mCurrentActivePhoneType = lPhoneType;
            SystemProperties.set("persist.radio.ap.phonetype", mCurrentActivePhoneType.toString());
        }
    }

    class RadioModule {
        int mPhoneType;
        int mRadioId;

        RadioModule(int radioId, int phoneType) {
            mRadioId = radioId;
            mPhoneType = phoneType;
        }

        int getPhoneType() {
            return mPhoneType;
        }
    }

    class ModemConfigFile {
        public HashMap<Integer, ModemConfigInfo> mMCInfoTable;
        public int mNumberOfMCEntries;

        public ModemConfigFile() {
            mNumberOfMCEntries = 0;
            mMCInfoTable = new HashMap<Integer, ModemConfigInfo>();
        }
    }

    class ModemConfigInfo {
        public static final String MODEMNAME_QCOM = "Qcom";
        public static final String MODEMNAME_TI = "TI";
        public static final String MODEMNAME_UNDEFINED = "Undefined";

        public HashMap<Integer, RadioModuleInfo> mRMInfoTable;

        public int mIccCardSlotNum;
        public int mMCInfoId;
        public String mMCInfoName;
        public String mPreferNetModeName;
        public int mRadioModuleNum;

        public ModemConfigInfo(int mcInfoId, String mcInfoName, int lRadioModuleNum, String preferNetModeName, int iccCardSlotNum) {
            mMCInfoId = mcInfoId;
            mMCInfoName = mcInfoName;
            mRadioModuleNum = lRadioModuleNum;
            mPreferNetModeName = preferNetModeName;
            mIccCardSlotNum = iccCardSlotNum;
            mRMInfoTable = new HashMap<Integer, RadioModuleInfo>();
        }
    }

    class RadioModuleInfo {
        public static final String ACTIVEMODE_ACTIVE = "true";
        public static final String ACTIVEMODE_INACTIVE = "false";
        public static final String ACTIVEMODE_OPTIONAL = "optional";
        public static final String MODULENAME_CDMA = "CDMA";
        public static final String MODULENAME_GSM = "GSM";
        public static final String MODULENAME_UNDEFINED = "Undefined";

        public String mActiveMode;
        public int mPhoneType;
        public int mRMInfoId;
        public String mRMInfoName;

        public RadioModuleInfo(int rmInfoId, String rmInfoName, int phoneType, String activityMode) {
            mRMInfoId = rmInfoId;
            mRMInfoName = rmInfoName;
            mPhoneType = phoneType;
            mActiveMode = activityMode;
        }
    }

    private ModemConfigManager(Context context, PhoneNotifier phonenotifier) {
        mMCFileSource = 0;
        isMCFileLoaded = false;
        mContext = context;
        mPhoneNotifier = phonenotifier;
        mModemInfo = new ArrayList<ModemInfo>();
        mMCFile = new ModemConfigFile();
        loadModemConfig();
    }

    public static ModemConfigManager getInstance() {
        ModemConfigManager modemconfigmanager = null;
        if (mInstance != null)
            modemconfigmanager = mInstance;
        return modemconfigmanager;
    }

    public static ModemConfigManager getInstance(Context context, PhoneNotifier phonenotifier) {
        if (mInstance == null) {
            mInstance = new ModemConfigManager(context, phonenotifier);
        }
        else {
            mContext = context;
            mPhoneNotifier = phonenotifier;
        }
        return mInstance;
    }

    private ModemConfigInfo getModemConfigInfo(int i) {
        ModemConfigInfo modemconfiginfo = null;
        if (mMCFile.mMCInfoTable.containsKey(Integer.valueOf(i))) {
            modemconfiginfo = (ModemConfigInfo)mMCFile.mMCInfoTable.get(Integer.valueOf(i));
        }
        return modemconfiginfo;
    }

    private RadioModuleInfo getRadioModuleInfo(int modemConfigInfoIndex, int j)
    {
        ModemConfigInfo modemconfiginfo = getModemConfigInfo(modemConfigInfoIndex);
        RadioModuleInfo radiomoduleinfo = null;
        if (modemconfiginfo != null) {
            if (modemconfiginfo.mRMInfoTable.containsKey(Integer.valueOf(j))) {
                radiomoduleinfo = (RadioModuleInfo)modemconfiginfo.mRMInfoTable.get(Integer.valueOf(j));
            }
        }
        return radiomoduleinfo;
    }

    private RadioModuleInfo getRadioModuleInfo(ModemConfigInfo modemconfiginfo, int i)
    {
        RadioModuleInfo radiomoduleinfo = null;
        if(modemconfiginfo.mRMInfoTable.containsKey(Integer.valueOf(i))) {
            radiomoduleinfo = (RadioModuleInfo)modemconfiginfo.mRMInfoTable.get(Integer.valueOf(i));
        }
        return radiomoduleinfo;
    }

    private void loadModemConfig() {
        int i = android.provider.Settings.Secure.getInt(mContext.getContentResolver(), "preferred_cdma_subscription", 1);
        loadModemConfigFileFromXml();
        if(isMCFileLoaded && mMCFile.mNumberOfMCEntries > 0)
        {
            int j = 0;
            do
            {
                int k = mMCFile.mNumberOfMCEntries;
                if(j >= k)
                    return;
                ModemConfigInfo modemconfiginfo = getModemConfigInfo(j);
                android.content.ContentResolver contentresolver = mContext.getContentResolver();
                String s = modemconfiginfo.mPreferNetModeName;
                int l = android.provider.Settings.Secure.getInt(contentresolver, s, 0);
                Context context = mContext;
                PhoneNotifier phonenotifier = mPhoneNotifier;
                int i1 = modemconfiginfo.mMCInfoId;
                int j1 = modemconfiginfo.mRadioModuleNum;
                ModemConfigManager modemconfigmanager = this;
                ModemInfo modeminfo = modemconfigmanager. new ModemInfo(context, phonenotifier, i1, l, i, j1);
                int k1 = 0;
                do
                {
                    int l1 = modemconfiginfo.mRadioModuleNum;
                    if(k1 >= l1)
                        break;
                    RadioModuleInfo radiomoduleinfo = getRadioModuleInfo(modemconfiginfo, k1);
                    int i2 = radiomoduleinfo.mRMInfoId;
                    int j2 = radiomoduleinfo.mPhoneType;
                    RadioModule radiomodule = new RadioModule(i2, j2);
                    modeminfo.addRadioModule(radiomodule);
                    k1++;
                } while(true);
                boolean flag = mModemInfo.add(modeminfo);
                j++;
            } while(true);
        }
        int k2 = SystemProperties.getInt("ro.telephony.default_network", 0);
        int l2 = android.provider.Settings.Secure.getInt(mContext.getContentResolver(), "preferred_network_mode", 0);
        switch(k2)
        {
        default:
            Context context1 = mContext;
            PhoneNotifier phonenotifier1 = mPhoneNotifier;
            ModemConfigManager modemconfigmanager1 = this;
            ModemInfo modeminfo1 = modemconfigmanager1. new ModemInfo(context1, phonenotifier1, 0, l2, i, 1);
            RadioModule radiomodule1 = new RadioModule(0, 1);
            modeminfo1.addRadioModule(radiomodule1);
            boolean flag1 = mModemInfo.add(modeminfo1);
            return;

        case 7: // '\007'
            Context context2 = mContext;
            PhoneNotifier phonenotifier2 = mPhoneNotifier;
            ModemConfigManager modemconfigmanager2 = this;
            ModemInfo modeminfo2 = modemconfigmanager2. new ModemInfo(context2, phonenotifier2, 0, l2, i, 2);
            RadioModule radiomodule2 = new RadioModule(0, 2);
            modeminfo2.addRadioModule(radiomodule2);
            RadioModule radiomodule3 = new RadioModule(1, 1);
            modeminfo2.addRadioModule(radiomodule3);
            boolean flag2 = mModemInfo.add(modeminfo2);
            return;

        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
            Context context3 = mContext;
            PhoneNotifier phonenotifier3 = mPhoneNotifier;
            ModemConfigManager modemconfigmanager3 = this;
            ModemInfo modeminfo3 = modemconfigmanager3. new ModemInfo(context3, phonenotifier3, 0, l2, i, 1);
            RadioModule radiomodule4 = new RadioModule(0, 2);
            modeminfo3.addRadioModule(radiomodule4);
            boolean flag3 = mModemInfo.add(modeminfo3);
            return;
        }
    }

    private void loadModemConfigFileFromFileSystem() {
        /* FIXME: ? */
    }

    private void loadModemConfigFileFromXml() {
        XmlResourceParser xmlresourceparser;
        Resources resources = mContext.getResources();
        int i = 0x10b0003;
        xmlresourceparser = resources.getXml(i);
        int k;
        XmlResourceParser xmlresourceparser1 = xmlresourceparser;
        String s = "ModemConfigFile";
        XmlUtils.beginDocument(xmlresourceparser1, s);
        ModemConfigFile modemconfigfile = mMCFile;
        XmlResourceParser xmlresourceparser2 = xmlresourceparser;
        String s1 = null;
        String s2 = "NumberOfModemEntries";
        int j = Integer.parseInt(xmlresourceparser2.getAttributeValue(s1, s2));
        modemconfigfile.mNumberOfMCEntries = j;
        k = 0;
_L6:
        String s3;
        XmlUtils.nextElement(xmlresourceparser);
        s3 = xmlresourceparser.getName();
        if(s3 != null) goto _L2; else goto _L1
_L1:
        int l = mMCFile.mNumberOfMCEntries;
        int i1 = k;
        int j1 = l;
        if(i1 != j1)
        {
            StringBuilder stringbuilder = (new StringBuilder()).append("Error Parsing ModemConfig File: ModemConfig ");
            int k1 = mMCFile.mNumberOfMCEntries;
            StringBuilder stringbuilder1 = stringbuilder.append(k1).append(" defined, ");
            int l1 = k;
            String s4 = stringbuilder1.append(l1).append(" parsed!").toString();
            int i2 = Log.e(LOG_TAG, s4);
        }
        boolean flag = true;
        isMCFileLoaded = flag;
        if(xmlresourceparser instanceof XmlResourceParser)
            ((XmlResourceParser)xmlresourceparser).close();
        if(true)
            return;
        throw false;
_L2:
        int k2;
        int i3;
        ModemConfigInfo modemconfiginfo1;
        String s5 = s3;
        String s6 = "ModemConfigInfo";
        if(!s5.equals(s6))
            continue; /* Loop/switch isn't completed */
        XmlResourceParser xmlresourceparser3 = xmlresourceparser;
        String s7 = null;
        String s8 = "Id";
        int j2 = Integer.parseInt(xmlresourceparser3.getAttributeValue(s7, s8));
        XmlResourceParser xmlresourceparser4 = xmlresourceparser;
        String s9 = null;
        String s10 = "Name";
        String s11 = xmlresourceparser4.getAttributeValue(s9, s10);
        XmlResourceParser xmlresourceparser5 = xmlresourceparser;
        String s12 = null;
        String s13 = "NumberOfRadioModuleEntries";
        k2 = Integer.parseInt(xmlresourceparser5.getAttributeValue(s12, s13));
        XmlResourceParser xmlresourceparser6 = xmlresourceparser;
        String s14 = null;
        String s15 = "PreferredNetworkModeKeyName";
        String s16 = xmlresourceparser6.getAttributeValue(s14, s15);
        XmlResourceParser xmlresourceparser7 = xmlresourceparser;
        String s17 = null;
        String s18 = "IccCardSlotNum";
        int l2 = Integer.parseInt(xmlresourceparser7.getAttributeValue(s17, s18));
        k++;
        HashMap hashmap = mMCFile.mMCInfoTable;
        Integer integer = Integer.valueOf(j2);
        ModemConfigManager modemconfigmanager = this;
        ModemConfigInfo modemconfiginfo = modemconfigmanager. new ModemConfigInfo(j2, s11, k2, s16, l2);
        Object obj = hashmap.put(integer, modemconfiginfo);
        i3 = 0;
        HashMap hashmap1 = mMCFile.mMCInfoTable;
        Integer integer1 = Integer.valueOf(j2);
        modemconfiginfo1 = (ModemConfigInfo)hashmap1.get(integer1);
        if(modemconfiginfo1 == null)
            continue; /* Loop/switch isn't completed */
_L4:
        String s19;
        XmlUtils.nextElement(xmlresourceparser);
        s19 = xmlresourceparser.getName();
        if(s19 == null)
        {
            int j3 = i3;
            int k3 = k2;
            if(j3 != k3)
            {
                StringBuilder stringbuilder2 = (new StringBuilder()).append("Error Parsing ModemConfig File: ModemConfig ID[");
                int l3 = k;
                StringBuilder stringbuilder3 = stringbuilder2.append(l3).append("]: RadioModule ").append(k2).append(" defined, ");
                int i4 = i3;
                String s20 = stringbuilder3.append(i4).append(" parsed!").toString();
                int j4 = Log.e(LOG_TAG, s20);
            }
            continue; /* Loop/switch isn't completed */
        }
        break MISSING_BLOCK_LABEL_614;
        Exception exception;
        exception;
        String s21 = LOG_TAG;
        String s22 = "Got exception while loading ModemConfig file.";
        Exception exception1 = exception;
        int k4 = Log.e(s21, s22, exception1);
        if(xmlresourceparser instanceof XmlResourceParser)
            ((XmlResourceParser)xmlresourceparser).close();
        if(true)
            return;
        throw false;
        IOException ioexception;
        ioexception;
        return;
        String s23 = s19;
        String s24 = "RadioModule";
        if(s23.equals(s24))
        {
            XmlResourceParser xmlresourceparser8 = xmlresourceparser;
            String s25 = null;
            String s26 = "Id";
            int l4 = Integer.parseInt(xmlresourceparser8.getAttributeValue(s25, s26));
            XmlResourceParser xmlresourceparser9 = xmlresourceparser;
            String s27 = null;
            String s28 = "Name";
            String s29 = xmlresourceparser9.getAttributeValue(s27, s28);
            XmlResourceParser xmlresourceparser10 = xmlresourceparser;
            String s30 = null;
            String s31 = "PhoneType";
            int i5 = Integer.parseInt(xmlresourceparser10.getAttributeValue(s30, s31));
            XmlResourceParser xmlresourceparser11 = xmlresourceparser;
            String s32 = null;
            String s33 = "Active";
            String s34 = xmlresourceparser11.getAttributeValue(s32, s33);
            i3++;
            HashMap hashmap2 = modemconfiginfo1.mRMInfoTable;
            Integer integer2 = Integer.valueOf(l4);
            ModemConfigManager modemconfigmanager1 = this;
            RadioModuleInfo radiomoduleinfo = modemconfigmanager1. new RadioModuleInfo(l4, s29, i5, s34);
            Object obj1 = hashmap2.put(integer2, radiomoduleinfo);
        }
        if(true) goto _L4; else goto _L3
_L3:
        Exception exception2;
        exception2;
        if(xmlresourceparser instanceof XmlResourceParser)
            ((XmlResourceParser)xmlresourceparser).close();
        IOException ioexception1;
        if(false)
            try
            {
                throw false;
            }
            catch(IOException ioexception2) { }
        throw exception2;
        ioexception1;
        return;
        if(true) goto _L6; else goto _L5
_L5:
    }

    private void log(String s) {
        Log.d(LOG_TAG, s);
    }

    public int getModemConfigNumberOfEntries() {
        return mMCFile.mNumberOfMCEntries;
    }

    public ModemInfo getModemInfo(int i) {
        Iterator iterator = mModemInfo.iterator();
_L4:
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        ModemInfo modeminfo = (ModemInfo)iterator.next();
        if(modeminfo.getModemId() != i) goto _L4; else goto _L3
_L3:
        ModemInfo modeminfo1 = modeminfo;
_L6:
        return modeminfo1;
_L2:
        modeminfo1 = null;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public ModemInfo[] getModemInfos() {
        ModemInfo amodeminfo[] = new ModemInfo[mModemInfo.size()];
        return (ModemInfo[])mModemInfo.toArray(amodeminfo);
    }

    public Phone getPrimaryPhoneProxy() {
        Phone phone = null;
        if(!mModemInfo.isEmpty())
            phone = ((ModemInfo)mModemInfo.get(0)).getPhoneProxy();
        return phone;
    }

    public boolean isModemConfigFileLoaded() {
        return isMCFileLoaded;
    }

    public void loadModemConfigFile() {
        switch (mMCFileSource) {
            case MC_FROM_FILE_XML:
            default:
                loadModemConfigFileFromXml();
                return;
            case MC_FROM_FILE_SYSTEM:
                loadModemConfigFileFromFileSystem();
                break;
        }
    }

}
