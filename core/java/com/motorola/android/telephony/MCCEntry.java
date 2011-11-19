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
 * Code by Hashcode [11/17/2011]
 */

package com.motorola.android.telephony;

/*
 * This class holds Motorola Country Code Entries
 */
public final class MCCEntry {
    public String IDD;
    public int MCC;
    public int MDNLength;
    public String NDD;
    public String areaCode;
    public int countryCode;
    public String countryName;

    public MCCEntry(int lMCC, int lCountryCode, String lCountryName, String lNDD, String lIDD, String lAreaCode, int lMDNLength) {
        MCC = lMCC;
        countryCode = lCountryCode;
        countryName = lCountryName;
        NDD = lNDD;
        IDD = lIDD;
        areaCode = lAreaCode;
        MDNLength = lMDNLength;
    }

    public String toString() {
        return "CdmaMCCEntry : { MCC = " + Integer.toString(MCC) + ", CountryCode = " + Integer.toString(countryCode) + ", CountryName = " + countryName + ", NDD = " + NDD + ", IDD = " + IDD + "}"
    }
}
