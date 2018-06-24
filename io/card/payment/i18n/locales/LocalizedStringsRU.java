package io.card.payment.i18n.locales;

import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import java.util.HashMap;
import java.util.Map;

public class LocalizedStringsRU implements SupportedLocale<StringKey> {
    private static Map<String, String> mAdapted = new HashMap();
    private static Map<StringKey, String> mDisplay = new HashMap();

    public String getName() {
        return "ru";
    }

    public String getAdaptedDisplay(StringKey key, String country) {
        String adaptedKey = key.toString() + "|" + country;
        if (mAdapted.containsKey(adaptedKey)) {
            return (String) mAdapted.get(adaptedKey);
        }
        return (String) mDisplay.get(key);
    }

    public LocalizedStringsRU() {
        mDisplay.put(StringKey.CANCEL, "Отмена");
        mDisplay.put(StringKey.CARDTYPE_AMERICANEXPRESS, "American Express");
        mDisplay.put(StringKey.CARDTYPE_DISCOVER, "Discover");
        mDisplay.put(StringKey.CARDTYPE_JCB, "JCB");
        mDisplay.put(StringKey.CARDTYPE_MASTERCARD, "MasterCard");
        mDisplay.put(StringKey.CARDTYPE_VISA, "Visa");
        mDisplay.put(StringKey.DONE, "Готово");
        mDisplay.put(StringKey.ENTRY_CVV, "Код безопасности");
        mDisplay.put(StringKey.ENTRY_POSTAL_CODE, "Индекс");
        mDisplay.put(StringKey.ENTRY_CARDHOLDER_NAME, "Имя и фамилия владельца");
        mDisplay.put(StringKey.ENTRY_EXPIRES, "Действ. до");
        mDisplay.put(StringKey.EXPIRES_PLACEHOLDER, "ММ/ГГ");
        mDisplay.put(StringKey.SCAN_GUIDE, "Держите карту внутри рамки.\nОна будет считана автоматически.");
        mDisplay.put(StringKey.KEYBOARD, "Клавиатура…");
        mDisplay.put(StringKey.ENTRY_CARD_NUMBER, "Номер карты");
        mDisplay.put(StringKey.MANUAL_ENTRY_TITLE, "Ввести данные вручную");
        mDisplay.put(StringKey.ERROR_NO_DEVICE_SUPPORT, "В данном устройстве нет опции считывания номера карты с помощью фотокамеры.");
        mDisplay.put(StringKey.ERROR_CAMERA_CONNECT_FAIL, "Фотокамера устройства недоступна.");
        mDisplay.put(StringKey.ERROR_CAMERA_UNEXPECTED_FAIL, "Возникла незапланированная ошибка при открытии фотокамеры устройства.");
    }
}
