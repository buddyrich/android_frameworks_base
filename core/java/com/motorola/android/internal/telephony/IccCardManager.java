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

package com.motorola.android.internal.telephony;

import android.content.Context;
import android.os.*;
import android.util.Log;
import com.android.internal.telephony.*;
import com.android.internal.telephony.gsm.stk.StkService;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.motorola.android.internal.telephony:
//            IccPhysicalCard

public class IccCardManager
{
    class ClientRadioCardAssociation
    {

        int getModemId()
        {
            return mModemId;
        }

        int getPhoneType()
        {
            return mPhoneType;
        }

        int getUniqueSlotId()
        {
            return mUniqueSlotId;
        }

        void setModemId(int i)
        {
            mModemId = i;
        }

        void setUniqueSlotId(int i)
        {
            mUniqueSlotId = i;
        }

        int mModemId;
        int mPhoneType;
        int mUniqueSlotId;
        final IccCardManager this$0;

        ClientRadioCardAssociation(int i, int j, int k)
        {
            this$0 = IccCardManager.this;
            super();
            mModemId = i;
            mPhoneType = j;
            mUniqueSlotId = k;
        }
    }

    class OwnerModemCardAssociation
    {

        /**
         * @deprecated Method onGetIccCardStatusDone is deprecated
         */

        private void onGetIccCardStatusDone(AsyncResult asyncresult)
        {
            this;
            JVM INSTR monitorenter ;
            if(asyncresult.exception == null)
                break MISSING_BLOCK_LABEL_34;
            String s = mLogTag;
            Throwable throwable = asyncresult.exception;
            int i = Log.e(s, "Error getting ICC status. RIL_REQUEST_GET_ICC_STATUS should never return an error", throwable);
_L1:
            this;
            JVM INSTR monitorexit ;
            return;
            IccCardStatus icccardstatus = (IccCardStatus)asyncresult.result;
            int j = 1;
            for(int k = 0; k < j; k++)
            {
                boolean flag = false;
                String s1 = mLogTag;
                StringBuilder stringbuilder = (new StringBuilder()).append("onGetIccCardStatusDone mIccPhysicalCards size = ");
                int l = mIccPhysicalCards.size();
                String s2 = stringbuilder.append(l).toString();
                int i1 = Log.d(s1, s2);
                int j1 = mIccPhysicalCards.size();
                if(k < j1 && mIccPhysicalCards.get(k) != null)
                {
                    ((IccPhysicalCard)mIccPhysicalCards.get(k)).update(icccardstatus);
                    flag = true;
                }
                String s3 = mLogTag;
                String s4 = (new StringBuilder()).append("onGetIccCardStatusDone updated = ").append(flag).toString();
                int k1 = Log.d(s3, s4);
                if(!flag)
                {
                    ArrayList arraylist = mIccPhysicalCards;
                    Context context = mContext;
                    CommandsInterface commandsinterface = mCi;
                    int l1 = IccCardManager.generateUniqueSlotId(mModemId, k);
                    OwnerModemCardAssociation ownermodemcardassociation = this;
                    IccPhysicalCard iccphysicalcard = new IccPhysicalCard(ownermodemcardassociation, icccardstatus, context, commandsinterface, l1);
                    boolean flag1 = arraylist.add(iccphysicalcard);
                }
            }

            boolean flag2 = mInitializedFromRIL = 1;
            int i2 = Log.d(mLogTag, "Notifying IccChangedRegistrants");
            mIccCardManager.notifyIccStatusChanged();
              goto _L1
            Exception exception;
            exception;
            throw exception;
        }

        public void activate()
        {
            CommandsInterface commandsinterface = mCi;
            Handler handler = mHandler;
            commandsinterface.registerForOn(handler, 1, null);
            CommandsInterface commandsinterface1 = mCi;
            Handler handler1 = mHandler;
            commandsinterface1.registerForCardError(handler1, 6, null);
            CommandsInterface commandsinterface2 = mCi;
            Handler handler2 = mHandler;
            commandsinterface2.registerForIccStatusChanged(handler2, 2, null);
            CommandsInterface commandsinterface3 = mCi;
            Handler handler3 = mHandler;
            commandsinterface3.registerForAvailable(handler3, 5, null);
        }

        public void deactivate()
        {
            CommandsInterface commandsinterface = mCi;
            Handler handler = mHandler;
            commandsinterface.unregisterForOn(handler);
            CommandsInterface commandsinterface1 = mCi;
            Handler handler1 = mHandler;
            commandsinterface1.unregisterForCardError(handler1);
            CommandsInterface commandsinterface2 = mCi;
            Handler handler2 = mHandler;
            commandsinterface2.unregisterForIccStatusChanged(handler2);
            CommandsInterface commandsinterface3 = mCi;
            Handler handler3 = mHandler;
            commandsinterface3.unregisterForOffOrNotAvailable(handler3);
            CommandsInterface commandsinterface4 = mCi;
            Handler handler4 = mHandler;
            commandsinterface4.unregisterForAvailable(handler4);
        }

        public IccPhysicalCard getIccPhysicalCard(int i)
        {
            Iterator iterator = mIccPhysicalCards.iterator();
_L4:
            if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
            IccPhysicalCard iccphysicalcard = (IccPhysicalCard)iterator.next();
            if(iccphysicalcard == null || iccphysicalcard.getUniqueSlotId() != i) goto _L4; else goto _L3
_L3:
            IccPhysicalCard iccphysicalcard1 = iccphysicalcard;
_L6:
            return iccphysicalcard1;
_L2:
            iccphysicalcard1 = null;
            if(true) goto _L6; else goto _L5
_L5:
        }

        public IccPhysicalCard[] getIccPhysicalCards()
        {
            int i = mIccPhysicalCards.size();
            String s = mLogTag;
            String s1 = (new StringBuilder()).append("getIccPhysicalCards size = ").append(i).toString();
            int j = Log.d(s, s1);
            IccPhysicalCard aiccphysicalcard1[];
            if(i > 0)
            {
                IccPhysicalCard aiccphysicalcard[] = new IccPhysicalCard[i];
                aiccphysicalcard1 = (IccPhysicalCard[])mIccPhysicalCards.toArray(aiccphysicalcard);
            } else
            {
                aiccphysicalcard1 = null;
            }
            return aiccphysicalcard1;
        }

        public int getModemId()
        {
            return mModemId;
        }

        public void triggerIccStatusChange(Object obj)
        {
            Handler handler = mHandler;
            Message message = mHandler.obtainMessage(2, obj);
            boolean flag = handler.sendMessage(message);
        }

        private static final int EVENT_GET_ICC_STATUS_DONE = 3;
        private static final int EVENT_ICC_ERROR = 6;
        private static final int EVENT_ICC_STATUS_CHANGED = 2;
        private static final int EVENT_RADIO_AVAILABLE = 5;
        private static final int EVENT_RADIO_OFF_OR_UNAVAILABLE = 4;
        private static final int EVENT_RADIO_ON = 1;
        private CommandsInterface mCi;
        private Context mContext;
        Handler mHandler;
        private IccCardManager mIccCardManager;
        private ArrayList mIccPhysicalCards;
        private int mModemId;
        private StkService mStkService;
        final IccCardManager this$0;



        public OwnerModemCardAssociation(IccCardManager icccardmanager1, int i, Context context, CommandsInterface commandsinterface)
        {
            this$0 = IccCardManager.this;
            super();
            class _cls1 extends Handler
            {

                public void handleMessage(Message message)
                {
                    AsyncResult asyncresult;
                    switch(message.what)
                    {
                    case 4: // '\004'
                    default:
                        String s = mLogTag;
                        StringBuilder stringbuilder = (new StringBuilder()).append(" Unknown Event ");
                        int j = message.what;
                        String s1 = stringbuilder.append(j).toString();
                        int k = Log.e(s, s1);
                        return;

                    case 1: // '\001'
                    case 2: // '\002'
                    case 5: // '\005'
                    case 6: // '\006'
                        int l = Log.d(mLogTag, "Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus");
                        CommandsInterface commandsinterface1 = mCi;
                        Object obj = message.obj;
                        Message message1 = obtainMessage(3, obj);
                        commandsinterface1.getIccCardStatus(message1);
                        return;

                    case 3: // '\003'
                        asyncresult = (AsyncResult)message.obj;
                        break;
                    }
                    onGetIccCardStatusDone(asyncresult);
                    if(asyncresult.userObj != null && (asyncresult.userObj instanceof AsyncResult))
                    {
                        AsyncResult asyncresult1 = (AsyncResult)asyncresult.userObj;
                        if(asyncresult1.userObj != null && (asyncresult1.userObj instanceof Message))
                        {
                            Message message2 = (Message)asyncresult1.userObj;
                            if(message2 != null)
                                message2.sendToTarget();
                        }
                        int i1 = Log.d(mLogTag, "EVENT_GET_ICC_STATUS_DONE, oncomplete(ar) send back");
                        return;
                    }
                    if(asyncresult.userObj == null)
                        return;
                    if(!(asyncresult.userObj instanceof Message))
                    {
                        return;
                    } else
                    {
                        Message message3 = (Message)asyncresult.userObj;
                        int j1 = Log.d(mLogTag, "EVENT_GET_ICC_STATUS_DONE, oncomplete send back");
                        message3.sendToTarget();
                        return;
                    }
                }

                final OwnerModemCardAssociation this$1;

                _cls1()
                {
                    this$1 = OwnerModemCardAssociation.this;
                    super();
                }
            }

            _cls1 _lcls1 = new _cls1();
            mHandler = _lcls1;
            mIccCardManager = icccardmanager1;
            mModemId = i;
            mContext = context;
            mCi = commandsinterface;
            Context context1 = mContext;
            StkService stkservice = StkService.getInstance(commandsinterface, context1, i);
            mStkService = stkservice;
            ArrayList arraylist = new ArrayList();
            mIccPhysicalCards = arraylist;
            activate();
        }
    }


    private IccCardManager(Context context)
    {
        mLogTag = "RIL_IccCardManager";
        RegistrantList registrantlist = new RegistrantList();
        mIccChangedRegistrants = registrantlist;
        mInitializedFromRIL = false;
        int i = Log.e(mLogTag, "Creating");
        ArrayList arraylist = new ArrayList();
        mOwnerModemCardAssociations = arraylist;
        ArrayList arraylist1 = new ArrayList();
        mClientRadioCardAssociations = arraylist1;
        mContext = context;
    }

    private int findIndexForModemId(int i, int j)
    {
        int k = 0;
_L3:
        ClientRadioCardAssociation clientradiocardassociation;
        int l = mClientRadioCardAssociations.size();
        if(k >= l)
            break MISSING_BLOCK_LABEL_66;
        clientradiocardassociation = (ClientRadioCardAssociation)mClientRadioCardAssociations.get(k);
        if(clientradiocardassociation == null || clientradiocardassociation.getUniqueSlotId() != j || clientradiocardassociation.getPhoneType() != i) goto _L2; else goto _L1
_L1:
        int i1 = k;
_L4:
        return i1;
_L2:
        k++;
          goto _L3
        i1 = -1;
          goto _L4
    }

    private int findIndexForUniqueSlotId(int i, int j)
    {
        int k = 0;
_L3:
        ClientRadioCardAssociation clientradiocardassociation;
        int l = mClientRadioCardAssociations.size();
        if(k >= l)
            break MISSING_BLOCK_LABEL_66;
        clientradiocardassociation = (ClientRadioCardAssociation)mClientRadioCardAssociations.get(k);
        if(clientradiocardassociation == null || clientradiocardassociation.getModemId() != i || clientradiocardassociation.getPhoneType() != j) goto _L2; else goto _L1
_L1:
        int i1 = k;
_L4:
        return i1;
_L2:
        k++;
          goto _L3
        i1 = -1;
          goto _L4
    }

    private int findUniqueSlotIdOfServiceCard(int i, int j)
    {
        int k = findIndexForUniqueSlotId(i, j);
        int l = -1;
        if(k != -1)
            l = ((ClientRadioCardAssociation)mClientRadioCardAssociations.get(k)).getUniqueSlotId();
        return l;
    }

    public static int generateSlotId(int i, int j)
    {
        int k = i * 10;
        return j - k;
    }

    public static int generateUniqueSlotId(int i, int j)
    {
        return i * 10 + j;
    }

    public static IccCardManager getInstance()
    {
        IccCardManager icccardmanager;
        if(mInstance == null)
            icccardmanager = null;
        else
            icccardmanager = mInstance;
        return icccardmanager;
    }

    public static IccCardManager getInstance(Context context)
    {
        if(mInstance == null)
            mInstance = new IccCardManager(context);
        else
            mInstance.mContext = context;
        return mInstance;
    }

    private IccCard getUnusedApplication(int i, int j)
    {
        int i2 = iccphysicalcard.getUniqueSlotId();
        updateClientRadioCardAssociation(i, j, i2);
        icccard.activate();
        icccard1 = icccard;
_L2:
        return icccard1;
        Iterator iterator = mOwnerModemCardAssociations.iterator();
        IccPhysicalCard iccphysicalcard;
        IccCard icccard;
        IccCard icccard1;
        do
        {
            if(!iterator.hasNext())
                break;
            OwnerModemCardAssociation ownermodemcardassociation = (OwnerModemCardAssociation)iterator.next();
            if(ownermodemcardassociation != null)
            {
                IccPhysicalCard aiccphysicalcard[] = ownermodemcardassociation.getIccPhysicalCards();
                if(aiccphysicalcard != null)
                {
                    int k = 0;
                    do
                    {
                        int l = aiccphysicalcard.length;
                        if(k >= l)
                            break;
                        iccphysicalcard = aiccphysicalcard[k];
                        if(iccphysicalcard != null)
                        {
                            int i1 = iccphysicalcard.getCardState();
                            int j1 = com.android.internal.telephony.IccCardStatus.CardState.CARDSTATE_PRESENT;
                            if(i1 == j1)
                            {
label0:
                                {
                                    int k1;
                                    String s;
                                    StringBuilder stringbuilder;
                                    Object obj;
                                    String s1;
                                    int l1;
                                    if(j == 1)
                                        k1 = iccphysicalcard.getGsmUmtsSubscriptionAppIndex();
                                    else
                                        k1 = iccphysicalcard.getCdmaSubscriptionAppIndex();
                                    icccard = iccphysicalcard.getIccCardApplication(k1);
                                    if(icccard != null && (icccard == null || !icccard.isAssigned()))
                                        break label0;
                                    s = mLogTag;
                                    stringbuilder = (new StringBuilder()).append("app = ");
                                    if(icccard == null)
                                        obj = "null";
                                    else
                                        obj = Boolean.valueOf(icccard.isAssigned());
                                    s1 = stringbuilder.append(obj).toString();
                                    l1 = Log.d(s, s1);
                                }
                            }
                        }
                        k++;
                    } while(true);
                }
            }
        } while(true);
        icccard1 = null;
        if(true) goto _L2; else goto _L1
_L1:
    }

    private void updateClientRadioCardAssociation(int i, int j, int k)
    {
        boolean flag = false;
        int l = findIndexForUniqueSlotId(i, j);
        if(l != -1)
        {
            ((ClientRadioCardAssociation)mClientRadioCardAssociations.get(l)).setUniqueSlotId(k);
            flag = true;
        }
        int i1;
        if(flag)
            i1 = -1;
        else
            i1 = findIndexForModemId(j, k);
        if(i1 != -1)
        {
            ((ClientRadioCardAssociation)mClientRadioCardAssociations.get(i1)).setModemId(i);
            flag = true;
        }
        if(flag)
        {
            return;
        } else
        {
            ArrayList arraylist = mClientRadioCardAssociations;
            ClientRadioCardAssociation clientradiocardassociation = new ClientRadioCardAssociation(i, j, k);
            boolean flag1 = arraylist.add(clientradiocardassociation);
            return;
        }
    }

    public void addOwnerModemCardAssociation(int i, CommandsInterface commandsinterface)
    {
        ArrayList arraylist = mOwnerModemCardAssociations;
        Context context = mContext;
        IccCardManager icccardmanager = this;
        IccCardManager icccardmanager1 = this;
        int j = i;
        CommandsInterface commandsinterface1 = commandsinterface;
        OwnerModemCardAssociation ownermodemcardassociation = icccardmanager. new OwnerModemCardAssociation(icccardmanager1, j, context, commandsinterface1);
        boolean flag = arraylist.add(ownermodemcardassociation);
    }

    /**
     * @deprecated Method getApplication is deprecated
     */

    public IccCard getApplication(int i, int j)
    {
        this;
        JVM INSTR monitorenter ;
        int k;
        k = findIndexForUniqueSlotId(i, j);
        String s = mLogTag;
        String s1 = (new StringBuilder()).append("getApplication modemId = ").append(i).append(" phoneType = ").append(j).append(" index = ").append(k).toString();
        int l = Log.d(s, s1);
        if(k != -1) goto _L2; else goto _L1
_L1:
        IccCard icccard = getUnusedApplication(i, j);
        IccCard icccard1 = icccard;
_L7:
        this;
        JVM INSTR monitorexit ;
        return icccard1;
_L2:
        IccPhysicalCard iccphysicalcard;
label0:
        {
            iccphysicalcard = getServiceIccPhysicalCard(i, j);
            if(iccphysicalcard != null)
            {
                if(iccphysicalcard == null)
                    break label0;
                int j1 = iccphysicalcard.getCardState();
                int k1 = com.android.internal.telephony.IccCardStatus.CardState.CARDSTATE_PRESENT;
                if(j1 == k1)
                    break label0;
            }
            Object obj = mClientRadioCardAssociations.remove(k);
            icccard1 = getUnusedApplication(i, j);
            continue; /* Loop/switch isn't completed */
        }
        if(j != 1) goto _L4; else goto _L3
_L3:
        int l1 = iccphysicalcard.getGsmUmtsSubscriptionAppIndex();
_L5:
        icccard1 = iccphysicalcard.getIccCardApplication(l1);
        if(icccard1 == null)
        {
            Object obj1 = mClientRadioCardAssociations.remove(k);
            icccard1 = getUnusedApplication(i, j);
        }
        continue; /* Loop/switch isn't completed */
_L4:
        int i1 = iccphysicalcard.getCdmaSubscriptionAppIndex();
        l1 = i1;
          goto _L5
        Exception exception;
        exception;
        throw exception;
        if(true) goto _L7; else goto _L6
_L6:
    }

    public IccPhysicalCard getDefaultPhysicalCard(int i)
    {
        Iterator iterator = mOwnerModemCardAssociations.iterator();
        IccPhysicalCard iccphysicalcard;
        if(iterator.hasNext())
        {
            OwnerModemCardAssociation ownermodemcardassociation = (OwnerModemCardAssociation)iterator.next();
            if(ownermodemcardassociation != null && ownermodemcardassociation.getModemId() == i)
            {
                IccPhysicalCard aiccphysicalcard[] = ownermodemcardassociation.getIccPhysicalCards();
                if(aiccphysicalcard != null && aiccphysicalcard.length > 0)
                    iccphysicalcard = aiccphysicalcard[0];
                else
                    iccphysicalcard = null;
            } else
            {
                int j = Log.e(mLogTag, "there is no card is associated with this modem.");
                iccphysicalcard = null;
            }
        } else
        {
            iccphysicalcard = null;
        }
        return iccphysicalcard;
    }

    public IccPhysicalCard getServiceIccPhysicalCard(int i, int j)
    {
        int k = findUniqueSlotIdOfServiceCard(i, j);
        IccPhysicalCard iccphysicalcard = null;
        Iterator iterator = mOwnerModemCardAssociations.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            OwnerModemCardAssociation ownermodemcardassociation = (OwnerModemCardAssociation)iterator.next();
            if(iccphysicalcard != null)
                break;
            iccphysicalcard = ownermodemcardassociation.getIccPhysicalCard(k);
        } while(true);
        return iccphysicalcard;
    }

    public int getServiceModemId(int i, int j)
    {
        Iterator iterator = mClientRadioCardAssociations.iterator();
_L4:
        if(!iterator.hasNext()) goto _L2; else goto _L1
_L1:
        ClientRadioCardAssociation clientradiocardassociation = (ClientRadioCardAssociation)iterator.next();
        if(clientradiocardassociation.getUniqueSlotId() != i || clientradiocardassociation.getPhoneType() != j) goto _L4; else goto _L3
_L3:
        int k = clientradiocardassociation.getModemId();
_L6:
        return k;
_L2:
        k = -1;
        if(true) goto _L6; else goto _L5
_L5:
    }

    public void notifyIccStatusChanged()
    {
        synchronized(mIccChangedRegistrants)
        {
            mIccChangedRegistrants.notifyRegistrants();
        }
    }

    public void registerForIccChanged(Handler handler, int i, Object obj)
    {
        Registrant registrant = new Registrant(handler, i, obj);
        synchronized(mIccChangedRegistrants)
        {
            mIccChangedRegistrants.add(registrant);
        }
        int j = Log.d(mLogTag, "registerForIccChanged ");
        if(!mInitializedFromRIL)
        {
            return;
        } else
        {
            registrant.notifyRegistrant();
            return;
        }
        exception;
        registrantlist;
        JVM INSTR monitorexit ;
        throw exception;
    }

    public void removeClientRadioCardAssociation(int i, int j)
    {
        int k = findIndexForModemId(i, j);
        if(k == -1)
            return;
        int l = mClientRadioCardAssociations.size();
        if(k >= l)
        {
            return;
        } else
        {
            Object obj = mClientRadioCardAssociations.remove(k);
            return;
        }
    }

    public void unregisterForIccChanged(Handler handler)
    {
        synchronized(mIccChangedRegistrants)
        {
            int i = Log.d(mLogTag, "unregisterForIccChanged ");
            mIccChangedRegistrants.remove(handler);
        }
    }

    private static IccCardManager mInstance;
    private ArrayList mClientRadioCardAssociations;
    private Context mContext;
    private RegistrantList mIccChangedRegistrants;
    private boolean mInitializedFromRIL;
    private String mLogTag;
    private ArrayList mOwnerModemCardAssociations;



/*
    static boolean access$302(IccCardManager icccardmanager, boolean flag)
    {
        icccardmanager.mInitializedFromRIL = flag;
        return flag;
    }

*/
}
