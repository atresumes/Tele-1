package io.card.payment.i18n;

import android.util.Log;
import com.payu.custombrowser.util.CBConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class I18nManager<E extends Enum<?>> {
    static final /* synthetic */ boolean $assertionsDisabled = (!I18nManager.class.desiredAssertionStatus());
    private static final Set<String> RIGHT_TO_LEFT_LOCALE_SET = new HashSet();
    private static final Map<String, String> SPECIAL_LOCALE_MAP = new HashMap();
    private static final String TAG = I18nManager.class.getSimpleName();
    private SupportedLocale<E> currentLocale;
    private Class<E> enumClazz;
    private Map<String, SupportedLocale<E>> supportedLocales = new LinkedHashMap();

    static {
        SPECIAL_LOCALE_MAP.put("zh_CN", "zh-Hans");
        SPECIAL_LOCALE_MAP.put("zh_TW", "zh-Hant_TW");
        SPECIAL_LOCALE_MAP.put("zh_HK", "zh-Hant");
        SPECIAL_LOCALE_MAP.put("en_UK", "en_GB");
        SPECIAL_LOCALE_MAP.put("en_IE", "en_GB");
        SPECIAL_LOCALE_MAP.put("iw_IL", "he");
        SPECIAL_LOCALE_MAP.put("no", CBConstant.NB);
        RIGHT_TO_LEFT_LOCALE_SET.add("he");
        RIGHT_TO_LEFT_LOCALE_SET.add("ar");
    }

    public I18nManager(Class<E> enumClazz, List<SupportedLocale<E>> locales) {
        this.enumClazz = enumClazz;
        for (SupportedLocale<E> locale : locales) {
            addLocale(locale);
        }
        setLanguage(null);
    }

    private void logMissingLocalizations(String localeName) {
        for (String errorMessage : getMissingLocaleMessages(localeName)) {
            Log.i(TAG, errorMessage);
        }
    }

    private List<String> getMissingLocaleMessages(String localeName) {
        SupportedLocale<E> locale = (SupportedLocale) this.supportedLocales.get(localeName);
        List<String> errorMessages = new ArrayList();
        Log.i(TAG, "Checking locale " + localeName);
        for (E key : (Enum[]) this.enumClazz.getEnumConstants()) {
            String prettyKeyValue = "[" + localeName + "," + key + "]";
            if (locale.getAdaptedDisplay(key, null) == null) {
                errorMessages.add("Missing " + prettyKeyValue);
            }
        }
        return errorMessages;
    }

    public void setLanguage(String localeSpecifier) {
        Log.d(TAG, "setLanguage(" + localeSpecifier + ")");
        this.currentLocale = null;
        this.currentLocale = getLocaleFromSpecifier(localeSpecifier);
        if ($assertionsDisabled || this.currentLocale != null) {
            Log.d(TAG, "setting locale to:" + this.currentLocale.getName());
            return;
        }
        throw new AssertionError();
    }

    public SupportedLocale<E> getLocaleFromSpecifier(String localeSpecifier) {
        SupportedLocale<E> foundLocale = null;
        if (localeSpecifier != null) {
            foundLocale = lookupSupportedLocale(localeSpecifier);
        }
        if (foundLocale == null) {
            String phoneLanguage = Locale.getDefault().toString();
            Log.d(TAG, localeSpecifier + " not found.  Attempting to look for " + phoneLanguage);
            foundLocale = lookupSupportedLocale(phoneLanguage);
        }
        if (foundLocale == null) {
            Log.d(TAG, "defaulting to english");
            foundLocale = (SupportedLocale) this.supportedLocales.get("en");
        }
        if ($assertionsDisabled || foundLocale != null) {
            return foundLocale;
        }
        throw new AssertionError();
    }

    private SupportedLocale<E> lookupSupportedLocale(String localeSpecifier) {
        if (localeSpecifier == null || localeSpecifier.length() < 2) {
            return null;
        }
        SupportedLocale<E> supportedLocale = null;
        if (SPECIAL_LOCALE_MAP.containsKey(localeSpecifier)) {
            String localeToUse = (String) SPECIAL_LOCALE_MAP.get(localeSpecifier);
            supportedLocale = (SupportedLocale) this.supportedLocales.get(localeToUse);
            Log.d(TAG, "Overriding locale specifier " + localeSpecifier + " with " + localeToUse);
        }
        if (supportedLocale == null) {
            String language_country;
            if (localeSpecifier.contains("_")) {
                language_country = localeSpecifier;
            } else {
                language_country = localeSpecifier + "_" + Locale.getDefault().getCountry();
            }
            supportedLocale = (SupportedLocale) this.supportedLocales.get(language_country);
        }
        if (supportedLocale == null) {
            supportedLocale = (SupportedLocale) this.supportedLocales.get(localeSpecifier);
        }
        if (supportedLocale != null) {
            return supportedLocale;
        }
        return (SupportedLocale) this.supportedLocales.get(localeSpecifier.substring(0, 2));
    }

    public String getString(E key) {
        return getString(key, this.currentLocale);
    }

    public String getString(E key, SupportedLocale<E> localeToTranslate) {
        String countryCode = Locale.getDefault().getCountry().toUpperCase(Locale.US);
        String s = localeToTranslate.getAdaptedDisplay(key, countryCode);
        if (s == null) {
            Log.i(TAG, "Missing localized string for [" + this.currentLocale.getName() + ",Key." + key.toString() + "]");
            s = ((SupportedLocale) this.supportedLocales.get("en")).getAdaptedDisplay(key, countryCode);
        }
        if (s != null) {
            return s;
        }
        Log.i(TAG, "Missing localized string for [en,Key." + key.toString() + "], so defaulting to keyname");
        return key.toString();
    }

    private void addLocale(SupportedLocale<E> supportedLocale) {
        String localeName = supportedLocale.getName();
        if (localeName == null) {
            throw new RuntimeException("Null localeName");
        } else if (this.supportedLocales.containsKey(localeName)) {
            throw new RuntimeException("Locale " + localeName + " already added");
        } else {
            this.supportedLocales.put(localeName, supportedLocale);
            logMissingLocalizations(localeName);
        }
    }
}
