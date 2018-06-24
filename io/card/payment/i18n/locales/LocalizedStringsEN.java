package io.card.payment.i18n.locales;

import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsEN implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return "en";
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsEN() {
        mDisplay.put(StringKey.CANCEL, "Cancel");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "Done");
        mDisplay.put(StringKey.ENTRY_CVV, "CVV");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "Postal Code");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "Cardholder Name");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "Expires");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "MM/YY");
        mDisplay.put(StringKey.SCAN_GUIDE, "Hold card here.\nIt will scan automatically.");
        mDisplay.put(StringKey.KEYBOARD, "Keyboard…");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "Card Number");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "Card Details");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "This device cannot use the camera to read card numbers.");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "Device camera is unavailable.");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "The device had an unexpected error opening the camera.");
    }
}
