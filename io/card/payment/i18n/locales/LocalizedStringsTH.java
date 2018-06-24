package io.card.payment.i18n.locales;

import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsTH implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return "th";
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsTH() {
        mDisplay.put(StringKey.CANCEL, "ยกเลิก");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "เสร็จแล้ว");
        mDisplay.put(StringKey.ENTRY_CVV, "CVV");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "รหัสไปรษณีย์");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "ชื่อผู้ถือบัตร");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "หมดอายุ");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "ดด/ปป");
        mDisplay.put(StringKey.SCAN_GUIDE, "ถือบัตรไว้ตรงนี้\nเครื่องจะสแกนโดยอัตโนมัติ");
        mDisplay.put(StringKey.KEYBOARD, "คีย์บอร์ด…");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "หมายเลขบัตร");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "รายละเอียดบัตร");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "อุปกรณ์ไม่สามารถใช้กล้องเพื่ออ่านหมายเลขบัตรได้");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "กล้องของอุปกรณ์ไม่พร้อมใช้งาน");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "อุปกรณ์พบข้อผิดพลาดขณะเปิดกล้อง");
    }
}
