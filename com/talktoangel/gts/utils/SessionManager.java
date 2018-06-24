package com.talktoangel.gts.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.HashMap;

public class SessionManager {
    public static final String AADHAR_NO = "adhar_no";
    public static final String ABOUT = "about";
    public static final String ACC_HOLD_NM = "acc_hold_nm";
    public static final String ACC_NO = "acc_no";
    public static final String ACC_TYPE = "account_type";
    private static final String AVAILABILITY = "availability";
    public static final String BANK_NAME = "bank_name";
    public static final String BRANCH_CITY = "branch_city";
    public static final String BUDDY_ID = "buddy_id";
    public static final String COUNTRY_CODE = "country_code";
    public static final String FREE_SESSION = "free_session";
    public static final String F_APPOINTMENT = "f_appointment";
    public static final String F_GENDER = "f_gender";
    public static final String F_LANGUAGE = "f_language";
    public static final String F_MAX = "f_max";
    public static final String F_MIN = "f_min";
    public static final String F_PAYMENT = "f_payment";
    public static final String F_SPECIALITY = "f_speciality";
    public static final String F_THERAPIST = "f_therapist";
    public static final String IFSC_CODE = "ifsc_code";
    public static final String IS_BACKGROUND = "is_background";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_MOBILE_VERIFIED = "mobile_verified";
    private static final String IS_NOTIFICATION_ENABLED = "notification";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ADDRESS1 = "address1";
    public static final String KEY_ANNIVERSARY = "anniversary";
    public static final String KEY_ANSWER = "sec_answer";
    public static final String KEY_CITY = "city";
    public static final String KEY_COUNTRY = "state";
    public static final String KEY_CURRENCY_PREF = "currency_pref";
    public static final String KEY_EDUCATION = "dr_education";
    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_ID = "user_id";
    public static final String KEY_IMAGE = "user_image";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_LICENSE_NO = "charge";
    public static final String KEY_MARITAL_STATUS = "marital_status";
    public static final String KEY_MOBILE = "mobile";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_PIN = "pin";
    public static final String KEY_PIN_CODE = "zip_code";
    public static final String KEY_RATES = "rates";
    public static final String KEY_SEC_QUES = "sec_ques";
    public static final String KEY_TASK_ID = "task_id";
    public static final String KEY_THERAPIST_TYPE = "therapist_type";
    public static final String KEY_TYPE = "user_type";
    private static final String NOTIFICATION_COUNT = "notification_count";
    public static final String PAN_NO = "pan_no";
    private static final String PREF_NAME = "TalktoAngelPref";
    public static final String SPECIALITY_ID = "speciality_id";
    public static final String TEST_PRICE = "test_price";
    public static final String TEST_TITLE = "test_title";
    private Editor editor = this.pref.edit();
    private SharedPreferences pref;

    public SessionManager(Context context) {
        this.pref = context.getSharedPreferences(PREF_NAME, 0);
    }

    public void setUser(String session, String id, String userType, String firstName, String lastName, String email, String secQues, String answer, String gender, String address, String address1, String city, String country, String zipCode, String countryCode, String currencyPref, String mobile, String image) {
        this.editor.putString(FREE_SESSION, session);
        this.editor.putString(KEY_ID, id);
        this.editor.putString(KEY_TYPE, userType);
        this.editor.putString(KEY_FIRST_NAME, firstName);
        this.editor.putString(KEY_LAST_NAME, lastName);
        this.editor.putString(KEY_EMAIL, email);
        this.editor.putString(KEY_SEC_QUES, secQues);
        this.editor.putString(KEY_ANSWER, answer);
        this.editor.putString(KEY_GENDER, gender);
        this.editor.putString(KEY_ADDRESS, address);
        this.editor.putString(KEY_ADDRESS1, address1);
        this.editor.putString("city", city);
        this.editor.putString("state", country);
        this.editor.putString(KEY_PIN_CODE, zipCode);
        this.editor.putString(COUNTRY_CODE, countryCode);
        this.editor.putString(KEY_CURRENCY_PREF, currencyPref);
        this.editor.putString("mobile", mobile);
        this.editor.putString(KEY_IMAGE, image);
        this.editor.commit();
    }

    public void setCounselor(String id, String userType, String firstName, String lastName, String email, String gender, String experience, String speciality_id, String speciality, String rates, String licenseNo, String education, String address, String address1, String city, String country, String pinCode, String mobile, String therapistType, String image, String about) {
        this.editor.putString(KEY_ID, id);
        this.editor.putString(KEY_TYPE, userType);
        this.editor.putString(KEY_FIRST_NAME, firstName);
        this.editor.putString(KEY_LAST_NAME, lastName);
        this.editor.putString(KEY_EMAIL, email);
        this.editor.putString(KEY_GENDER, gender);
        this.editor.putString(KEY_EXPERIENCE, experience);
        this.editor.putString(SPECIALITY_ID, speciality_id);
        this.editor.putString(F_SPECIALITY, speciality);
        this.editor.putString(KEY_RATES, rates);
        this.editor.putString(KEY_LICENSE_NO, licenseNo);
        this.editor.putString(KEY_EDUCATION, education);
        this.editor.putString(KEY_ADDRESS, address);
        this.editor.putString(KEY_ADDRESS1, address1);
        this.editor.putString("city", city);
        this.editor.putString("state", country);
        this.editor.putString(KEY_PIN_CODE, pinCode);
        this.editor.putString("mobile", mobile);
        this.editor.putString(KEY_THERAPIST_TYPE, therapistType);
        this.editor.putString(KEY_IMAGE, image);
        this.editor.putString(ABOUT, about);
        this.editor.commit();
    }

    public HashMap<String, String> getUser() {
        HashMap<String, String> user = new HashMap();
        user.put(FREE_SESSION, this.pref.getString(FREE_SESSION, null));
        user.put(KEY_ID, this.pref.getString(KEY_ID, null));
        user.put(KEY_TYPE, this.pref.getString(KEY_TYPE, null));
        user.put(KEY_FIRST_NAME, this.pref.getString(KEY_FIRST_NAME, null));
        user.put(KEY_LAST_NAME, this.pref.getString(KEY_LAST_NAME, null));
        user.put(KEY_EMAIL, this.pref.getString(KEY_EMAIL, null));
        user.put(KEY_SEC_QUES, this.pref.getString(KEY_SEC_QUES, null));
        user.put(KEY_ANSWER, this.pref.getString(KEY_ANSWER, null));
        user.put(KEY_GENDER, this.pref.getString(KEY_GENDER, "m"));
        user.put(F_SPECIALITY, this.pref.getString(F_SPECIALITY, ""));
        user.put(KEY_EXPERIENCE, this.pref.getString(KEY_EXPERIENCE, ""));
        user.put(SPECIALITY_ID, this.pref.getString(SPECIALITY_ID, ""));
        user.put(KEY_THERAPIST_TYPE, this.pref.getString(KEY_THERAPIST_TYPE, ""));
        user.put(KEY_LICENSE_NO, this.pref.getString(KEY_LICENSE_NO, ""));
        user.put(KEY_RATES, this.pref.getString(KEY_RATES, ""));
        user.put(KEY_EDUCATION, this.pref.getString(KEY_EDUCATION, ""));
        user.put(KEY_ADDRESS, this.pref.getString(KEY_ADDRESS, null));
        user.put(KEY_ADDRESS1, this.pref.getString(KEY_ADDRESS1, null));
        user.put("city", this.pref.getString("city", null));
        user.put("state", this.pref.getString("state", "India"));
        user.put(KEY_PIN_CODE, this.pref.getString(KEY_PIN_CODE, null));
        user.put(COUNTRY_CODE, this.pref.getString(COUNTRY_CODE, null));
        user.put(KEY_CURRENCY_PREF, this.pref.getString(KEY_CURRENCY_PREF, null));
        user.put("mobile", this.pref.getString("mobile", null));
        user.put(KEY_IMAGE, this.pref.getString(KEY_IMAGE, null));
        user.put(KEY_ANNIVERSARY, this.pref.getString(KEY_ANNIVERSARY, null));
        user.put(KEY_MARITAL_STATUS, this.pref.getString(KEY_MARITAL_STATUS, null));
        user.put(KEY_TASK_ID, this.pref.getString(KEY_TASK_ID, null));
        user.put(ABOUT, this.pref.getString(ABOUT, null));
        return user;
    }

    public void setUserID(String userID) {
        this.editor.putString(KEY_ID, userID);
        this.editor.commit();
    }

    public void setUserType(String userType) {
        this.editor.putString(KEY_TYPE, userType);
        this.editor.commit();
    }

    public void logoutUser() {
        this.editor.clear();
        this.editor.commit();
    }

    public void setGender(String gender) {
        this.editor.putString(KEY_GENDER, gender);
        this.editor.commit();
    }

    public void setAnniversary(String anniversary) {
        this.editor.putString(KEY_ANNIVERSARY, anniversary);
        this.editor.commit();
    }

    public void setMaritalStatus(String maritalStatus) {
        this.editor.putString(KEY_MARITAL_STATUS, maritalStatus);
        this.editor.commit();
    }

    public void setCountry(String country) {
        this.editor.putString("state", country);
        this.editor.commit();
    }

    public void setSecQues(String secQues) {
        this.editor.putString(KEY_SEC_QUES, secQues);
        this.editor.commit();
    }

    public void setPin(String pin) {
        this.editor.putString(KEY_PIN, pin);
        this.editor.commit();
    }

    public String getPin() {
        return this.pref.getString(KEY_PIN, null);
    }

    public void setNotification(boolean key) {
        this.editor.putBoolean(IS_NOTIFICATION_ENABLED, key);
        this.editor.commit();
    }

    public boolean isNotificationEnabled() {
        return this.pref.getBoolean(IS_NOTIFICATION_ENABLED, false);
    }

    public void setMobileVerified(boolean key) {
        this.editor.putBoolean(IS_MOBILE_VERIFIED, key);
        this.editor.commit();
    }

    public boolean isMobileVerified() {
        return this.pref.getBoolean(IS_MOBILE_VERIFIED, true);
    }

    public void setLoginSession() {
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.commit();
    }

    public boolean isLoggedIn() {
        return this.pref.getBoolean(IS_LOGIN, false);
    }

    public void setFilterData(String therapist, String speciality, String payment, String appointment, String language, String gender, String minRate, String maxRate) {
        this.editor.putString(F_THERAPIST, therapist);
        this.editor.putString(F_SPECIALITY, speciality);
        this.editor.putString(F_PAYMENT, payment);
        this.editor.putString(F_APPOINTMENT, appointment);
        this.editor.putString(F_LANGUAGE, language);
        this.editor.putString(F_GENDER, gender);
        this.editor.putString(F_MIN, minRate);
        this.editor.putString(F_MAX, maxRate);
        this.editor.commit();
    }

    public HashMap<String, String> getFilterData() {
        HashMap<String, String> user = new HashMap();
        user.put(F_THERAPIST, this.pref.getString(F_THERAPIST, "Psychiatrist"));
        user.put(F_SPECIALITY, this.pref.getString(F_SPECIALITY, null));
        user.put(F_PAYMENT, this.pref.getString(F_PAYMENT, ""));
        user.put(F_APPOINTMENT, this.pref.getString(F_APPOINTMENT, null));
        user.put(F_LANGUAGE, this.pref.getString(F_LANGUAGE, null));
        user.put(F_GENDER, this.pref.getString(F_GENDER, "m"));
        user.put(F_MIN, this.pref.getString(F_MIN, "0"));
        user.put(F_MAX, this.pref.getString(F_MAX, "100"));
        return user;
    }

    public void addNotification(String notification) {
        String oldNotifications = getNotifications();
        if (oldNotifications != null) {
            oldNotifications = oldNotifications + "|" + notification;
        } else {
            oldNotifications = notification;
        }
        this.editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        this.editor.commit();
    }

    public String getNotifications() {
        return this.pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void setAvailability(String availability) {
        this.editor.putString(AVAILABILITY, availability);
        this.editor.commit();
    }

    public String getAvailability() {
        return this.pref.getString(AVAILABILITY, "");
    }

    public void setIsBackground(String task) {
        this.editor.putString(IS_BACKGROUND, task);
        this.editor.commit();
    }

    public String getIsBackground() {
        return this.pref.getString(IS_BACKGROUND, "");
    }

    public void setTask(String id) {
        this.editor.putString(KEY_TASK_ID, id);
        this.editor.commit();
    }

    public void setBuddyId(String id) {
        this.editor.putString(BUDDY_ID, id);
        this.editor.commit();
    }

    public String getBuddyId() {
        return this.pref.getString(BUDDY_ID, null);
    }

    public String getTask() {
        return this.pref.getString(KEY_TASK_ID, null);
    }

    public void setBankDetails(String name, String accNo, String accType, String bankName, String ifscCode, String branchName, String panNo, String aadharNO) {
        this.editor.putString(ACC_HOLD_NM, name);
        this.editor.putString(ACC_NO, accNo);
        this.editor.putString(ACC_TYPE, accType);
        this.editor.putString(BANK_NAME, bankName);
        this.editor.putString(IFSC_CODE, ifscCode);
        this.editor.putString(BRANCH_CITY, branchName);
        this.editor.putString(PAN_NO, panNo);
        this.editor.putString(AADHAR_NO, aadharNO);
        this.editor.commit();
    }

    public HashMap<String, String> getBankDetails() {
        HashMap<String, String> map = new HashMap();
        map.put(ACC_HOLD_NM, this.pref.getString(ACC_HOLD_NM, null));
        map.put(ACC_NO, this.pref.getString(ACC_NO, null));
        map.put(ACC_TYPE, this.pref.getString(ACC_TYPE, null));
        map.put(BANK_NAME, this.pref.getString(BANK_NAME, null));
        map.put(IFSC_CODE, this.pref.getString(IFSC_CODE, null));
        map.put(BRANCH_CITY, this.pref.getString(BRANCH_CITY, null));
        map.put(PAN_NO, this.pref.getString(PAN_NO, null));
        map.put(AADHAR_NO, this.pref.getString(AADHAR_NO, null));
        return map;
    }

    public void setTestData(String title, String price) {
        this.editor.putString(TEST_TITLE, title);
        this.editor.putString(TEST_PRICE, price);
        this.editor.commit();
    }

    public void setFreeSession(String session) {
        this.editor.putString(FREE_SESSION, session);
        this.editor.commit();
    }

    public HashMap<String, String> getTestData() {
        HashMap<String, String> map = new HashMap();
        map.put(TEST_TITLE, this.pref.getString(TEST_TITLE, null));
        map.put(TEST_PRICE, this.pref.getString(TEST_PRICE, null));
        return map;
    }

    public int getNotificationCount() {
        return this.pref.getInt(NOTIFICATION_COUNT, 0);
    }

    public void setNotificationCount(int count) {
        this.editor.putInt(NOTIFICATION_COUNT, count);
        this.editor.commit();
    }
}
