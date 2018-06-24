package io.card.payment.i18n.locales;

import com.payu.custombrowser.util.CBConstant;
import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsNB implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return CBConstant.NB;
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsNB() {
        mDisplay.put(StringKey.CANCEL, "Avbryt");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "Fullført");
        mDisplay.put(StringKey.ENTRY_CVV, "CVV");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "Postnummer");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "Kortinnehaverens navn");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "Utløper");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "MM/ÅÅ");
        mDisplay.put(StringKey.SCAN_GUIDE, "Hold kortet her.\nDet skannes automatisk.");
        mDisplay.put(StringKey.KEYBOARD, "Tastatur …");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "Kortnummer");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "Kortdetaljer");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "Denne enheten kan ikke bruke kameraet til å lese kortnumre.");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "Kameraet er utilgjengelig.");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "Det oppstod en uventet feil ved kameraoppstart.");
    }
}
