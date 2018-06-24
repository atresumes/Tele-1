package com.sinch.android.rtc.internal.service.persistence;

import java.util.List;

public interface PersistenceService {
    void clear();

    List<String> getAllKeys();

    String getValue(String str, String str2);

    void setValue(String str, String str2);
}
