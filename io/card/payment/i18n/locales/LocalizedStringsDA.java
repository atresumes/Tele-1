package io.card.payment.i18n.locales;

import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsDA implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return "da";
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsDA() {
        mDisplay.put(StringKey.CANCEL, "Annuller");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "Udført");
        mDisplay.put(StringKey.ENTRY_CVV, "Kontrolcifre");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "Postnummer");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "Kortindehaverens navn");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "Udløbsdato");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "MM/ÅÅ");
        mDisplay.put(StringKey.SCAN_GUIDE, "Hold kortet her.\nDet scannes automatisk.");
        mDisplay.put(StringKey.KEYBOARD, "Tastatur…");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "Kortnummer");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "Kortoplysninger");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "Denne enhed kan ikke anvende kameraet til at læse kortnumre.");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "Enhed kamera ikke er tilgængelig.");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "Enheden havde en uventet fejl under åbning af kamera.");
    }
}
