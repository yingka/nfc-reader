package com.bugull.nfctest;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Parcelable;

import com.bugull.nfctest.util.StringUtil;
import com.bugull.nfctest.util.TimeUtil;

/**
 * @author kk on 2018/1/16  13:25.
 *         Email:763744747@qq.com
 */

public class TagReader {
    /**
     * https://developer.android.com/guide/topics/connectivity/nfc/advanced-nfc.html#foreground-dispatch
     * <p>
     * Supported tag technologies
     * The following tag technlogies are not required to be supported by Android-powered devices:
     * MifareClassic;MifareUltralight
     */
    private static final String[] TECHES = new String[]{
            NfcA.class.getName(), NfcB.class.getName(), NfcF.class.getName(), NfcV.class.getName(),
            IsoDep.class.getName(), Ndef.class.getName(), NdefFormatable.class.getName(), MifareClassic.class.getName(), MifareUltralight.class.getName()};

    public static String readTag(Tag tag,Intent intent) {
        if (tag != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ReadTime:").append(TimeUtil.curTime(System.currentTimeMillis())).append("\n")
                    .append("ID-Hex:").append(StringUtil.toHexStr(tag.getId())).append("\n")
                    .append("Technologies:").append(tag.toString()).append("\n")
                    .append("----------Technologies detail---------").append("\n");
            String[] tagTechList = tag.getTechList();
            if (tagTechList != null) {
                for (int i = 0; i < tagTechList.length; i++) {
                    stringBuilder.append("*").append(tagTechList[i]).append("*").append("\n");
                    stringBuilder.append(readTech(tag, tagTechList[i],intent));
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

    private static String readTech(Tag tag, String curTagTech,Intent intent) {
        StringBuilder stringBuilder = new StringBuilder();
        if (TECHES[0].equals(curTagTech)) {
            NfcA nfcaTag = NfcA.get(tag);
            stringBuilder.append("ATQA/SENS_RES bytes:").append(StringUtil.toHexStr(nfcaTag.getAtqa())).append("\n")
                    .append("maximum number of bytes that can be sent:").append(nfcaTag.getMaxTransceiveLength()).append("\n")
                    .append("SAK/SEL_RES bytes：").append(StringUtil.toHexStr(StringUtil.shortToByteArray(nfcaTag.getSak()))).append("\n")
                    .append("current timeout:").append(nfcaTag.getTimeout()).append("\n");
            return stringBuilder.toString();
        } else if (TECHES[1].equals(curTagTech)) {
            NfcB nfcbTag = NfcB.get(tag);
            stringBuilder.append("ApplicationData:").append(StringUtil.toHexStr(nfcbTag.getApplicationData())).append("\n")
                    .append("ProtocolInfo:").append(StringUtil.toHexStr(nfcbTag.getProtocolInfo())).append("\n")
                    .append("maximum number of bytes that can be sent：").append(nfcbTag.getMaxTransceiveLength()).append("\n");
            return stringBuilder.toString();
        } else if (TECHES[2].equals(curTagTech)) {
            NfcF nfcfTag = NfcF.get(tag);
            stringBuilder.append("Manufacturer:").append(StringUtil.toHexStr(nfcfTag.getManufacturer())).append("\n")
                    .append("SystemCode:").append(StringUtil.toHexStr(nfcfTag.getSystemCode())).append("\n")
                    .append("maximum number of bytes that can be sent：").append(nfcfTag.getMaxTransceiveLength()).append("\n")
                    .append("current timeout:").append(nfcfTag.getTimeout()).append("\n");
            return stringBuilder.toString();
        } else if (TECHES[3].equals(curTagTech)) {
            NfcV nfcvTag = NfcV.get(tag);
            stringBuilder.append("DSF ID:").append(StringUtil.toHexStr(new byte[]{nfcvTag.getDsfId()})).append("\n")
                    .append(" Response Flag:").append(StringUtil.toHexStr(new byte[]{nfcvTag.getResponseFlags()})).append("\n")
                    .append("maximum number of bytes that can be sent：").append(nfcvTag.getMaxTransceiveLength()).append("\n");
            return stringBuilder.toString();
        } else if (TECHES[4].equals(curTagTech)) {
            IsoDep isoDepTag = IsoDep.get(tag);
            stringBuilder.append("higher layer response:").append(StringUtil.toHexStr(isoDepTag.getHiLayerResponse())).append("\n")
                    .append("ISO-DEP historical bytes:").append(StringUtil.toHexStr(isoDepTag.getHistoricalBytes())).append("\n")
                    .append("maximum number of bytes that can be sent：").append(isoDepTag.getMaxTransceiveLength()).append("\n")
                    .append("current timeout:").append(isoDepTag.getTimeout()).append("\n")
                    .append("isExtendedLengthSupported:").append(isoDepTag.isExtendedLengthApduSupported()).append("\n");
            return stringBuilder.toString();
        } else if (TECHES[5].equals(curTagTech)) {
            Ndef ndefTag = Ndef.get(tag);
            stringBuilder.append("maximum NDEF message size:").append(ndefTag.getMaxSize()).append("\n")
                    .append("NDEF tag type:").append(ndefTag.getType()).append("\n");
            Parcelable[] rawsMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg= (NdefMessage) rawsMsgs[0];
            stringBuilder.append(new String(msg.getRecords()[0].getPayload()));
            return stringBuilder.toString();
        } else if (TECHES[6].equals(curTagTech)) {
            NdefFormatable ndefFormatableTag = NdefFormatable.get(tag);
            stringBuilder.append("NdefFormatable with none message").append("\n");
        } else if (TECHES[7].equals(curTagTech)) {
            MifareClassic mifareClassicTag = MifareClassic.get(tag);
            stringBuilder.append("Mifare size:").append(mifareClassicTag.getSize()).append("bytes").append("\n")
                    .append("Mifare sector:").append(mifareClassicTag.getSectorCount()).append("\n")
                    .append("Mifare block:").append(mifareClassicTag.getBlockCount()).append("\n")
                    .append("maximum number of bytes that can be sent:").append(mifareClassicTag.getMaxTransceiveLength()).append("\n")
                    .append("current timeout:").append(mifareClassicTag.getTimeout()).append("\n")
                    .append("MifareClassicType:").append(getMifareClassicType(mifareClassicTag.getType())).append("\n");
            return stringBuilder.toString();
        } else if (TECHES[8].equals(curTagTech)) {
            MifareUltralight mifareUltralightTag = MifareUltralight.get(tag);
            stringBuilder.append("maximum number of bytes that can be sent:").append(mifareUltralightTag.getMaxTransceiveLength()).append("\n")
                    .append("current timeout:").append(mifareUltralightTag.getTimeout()).append("\n")
                    .append("MIFARE Ultralight type :").append(getMifareUltralightType(mifareUltralightTag.getType())).append("\n");
            return stringBuilder.toString();
        }
        return stringBuilder.toString();
    }

    private static String getMifareClassicType(int type) {
        if (type == MifareClassic.TYPE_UNKNOWN) {
            return "TYPE_UNKNOWN";
        } else if (type == MifareClassic.TYPE_CLASSIC) {
            return "TYPE_CLASSIC";
        } else if (type == MifareClassic.TYPE_PLUS) {
            return "TYPE_PLUS";
        } else if (type == MifareClassic.TYPE_PRO) {
            return "TYPE_PRO";
        }
        return "TYPE_UNKNOWN";
    }

    private static String getMifareUltralightType(int type) {
        if (type == MifareUltralight.TYPE_UNKNOWN) {
            return "TYPE_UNKNOWN";
        } else if (type == MifareUltralight.TYPE_ULTRALIGHT) {
            return "TYPE_ULTRALIGHT";
        } else if (type == MifareUltralight.TYPE_ULTRALIGHT_C) {
            return "TYPE_ULTRALIGHT_C";
        }
        return "TYPE_UNKNOWN";
    }
}
