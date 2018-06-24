package io.card.payment.i18n.locales;

import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsIT implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return "it";
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsIT() {
        mDisplay.put(StringKey.CANCEL, "Annulla");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "OK");
        mDisplay.put(StringKey.ENTRY_CVV, "CVV");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "CAP");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "Titolare della carta");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "Scadenza");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "MM/AA");
        mDisplay.put(StringKey.SCAN_GUIDE, "Inquadra la carta.\nLa scansione è automatica.");
        mDisplay.put(StringKey.KEYBOARD, "Tastiera…");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "Numero di carta");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "Dati carta");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "La fotocamera non legge il numero di carta.");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "Fotocamera non disponibile.");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "Errore inatteso nell’apertura della fotocamera.");
    }
}
