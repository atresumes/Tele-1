package com.sinch.android.rtc.internal.service.persistence;

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultPersistenceService implements PersistenceService {
    private final SharedPreferences sharedPreferences;

    public DefaultPersistenceService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void clear() {
        this.sharedPreferences.edit().clear().commit();
    }

    public List<String> getAllKeys() {
        Map all = this.sharedPreferences.getAll();
        List<String> arrayList = new ArrayList();
        for (Entry key : all.entrySet()) {
            arrayList.add(key.getKey());
        }
        return arrayList;
    }

    public String getValue(String str, String str2) {
        return this.sharedPreferences.getString(str, str2);
    }

    public void setValue(String str, String str2) {
        this.sharedPreferences.edit().putString(str, str2).commit();
    }
}
