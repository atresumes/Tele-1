package io.card.payment.i18n.locales;

import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsES implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return "es";
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsES() {
        mDisplay.put(StringKey.CANCEL, "Cancelar");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "Hecho");
        mDisplay.put(StringKey.ENTRY_CVV, "CVV");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "Código postal");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "Nombre del titular de la tarjeta");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "Vence");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "MM/AA");
        mDisplay.put(StringKey.SCAN_GUIDE, "Mantén la tarjeta aquí.\nSe escaneará automáticamente.");
        mDisplay.put(StringKey.KEYBOARD, "Teclado…");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "Número de tarjeta");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "Detalles de la tarjeta");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "Este dispositivo no puede usar la cámara para leer números de tarjeta.");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "La cámara del dispositivo no está disponible.");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "Al abrir la cámara, el dispositivo ha experimentado un error inesperado.");
    }
}
