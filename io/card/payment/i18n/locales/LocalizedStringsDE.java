package io.card.payment.i18n.locales;

import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsDE implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return "de";
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsDE() {
        mDisplay.put(StringKey.CANCEL, "Abbrechen");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "Fertig");
        mDisplay.put(StringKey.ENTRY_CVV, "Prüfnr.");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "PLZ");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "Karteninhaber");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "Gültig bis");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "MM/JJ");
        mDisplay.put(StringKey.SCAN_GUIDE, "Kreditkarte hierhin halten.\nSie wird automatisch gelesen.");
        mDisplay.put(StringKey.KEYBOARD, "Tastatur…");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "Kartennummer");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "Kreditkartendetails");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "Dieses Gerät kann mit der Kamera keine Kreditkartennummern lesen.");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "Die Kamera ist nicht verfügbar.");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "Beim Öffnen der Kamera ist ein unerwarteter Fehler aufgetreten.");
    }
}
