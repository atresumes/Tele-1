package com.payUMoney.sdk.entity;

import com.payUMoney.sdk.SdkConstants;

public class SdkCard extends SdkEntity {
    private String mLabel = null;
    private String mMode = null;
    private String mName = null;
    private String mNumber = null;
    private String mToken = null;

    public static String getType(String number) {
        SdkCard c = new SdkCard();
        c.setNumber(number);
        switch (c.getIssuer()) {
            case AMEX:
                return SdkConstants.AMEX;
            case VISA:
                return "VISA";
            case MASTERCARD:
                return "MAST";
            case MAESTRO:
                return "MAES";
            case LASER:
                return "LASR";
            default:
                return "VISA";
        }
    }

    public static boolean isValidNumber(String number) {
        if (number.replaceAll("0", "").trim().length() == 0) {
            return false;
        }
        int s1 = 0;
        int s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            int digit = Character.digit(reverse.charAt(i), 10);
            if (i % 2 == 0) {
                s1 += digit;
            } else {
                s2 += digit * 2;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        if ((s1 + s2) % 10 == 0) {
            return true;
        }
        return false;
    }

    public static boolean isAmex(String number) {
        return number.startsWith("34") || number.startsWith("37");
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public SdkIssuer getIssuer() {
        if (this.mNumber.startsWith("4")) {
            return SdkIssuer.VISA;
        }
        if (this.mNumber.matches("^((6304)|(6706)|(6771)|(6709))[\\d]+X+\\d+")) {
            return SdkIssuer.LASER;
        }
        if (this.mNumber.matches("(5[06-8]|6\\d)\\d{14}(\\d{2,3})?[\\d]+X+\\d+") || this.mNumber.matches("(5[06-8]|6\\d)[\\d]+X+\\d+") || this.mNumber.matches("((504([435|645|774|775|809|993]))|(60([0206]|[3845]))|(622[018])\\d)[\\d]+X+\\d+")) {
            return SdkIssuer.MAESTRO;
        }
        if (this.mNumber.matches("^5[1-5][\\d]+X+\\d+")) {
            return SdkIssuer.MASTERCARD;
        }
        if (this.mNumber.matches("^3[47][\\d]+X+\\d+")) {
            return SdkIssuer.AMEX;
        }
        if (this.mNumber.startsWith("36") || this.mNumber.startsWith("34") || this.mNumber.startsWith("37") || this.mNumber.matches("^30[0-5][\\d]+X+\\d+")) {
            return SdkIssuer.DINER;
        }
        if (this.mNumber.matches("2(014|149)[\\d]+X+\\d+")) {
            return SdkIssuer.DINER;
        }
        if (this.mNumber.matches("^35(2[89]|[3-8][0-9])[\\d]+X+\\d+")) {
            return SdkIssuer.JCB;
        }
        if (this.mNumber.matches("6(?:011|5[0-9]{2})[0-9]{12}[\\d]+X+\\d+")) {
            return SdkIssuer.DISCOVER;
        }
        return SdkIssuer.UNKNOWN;
    }

    public String getToken() {
        return this.mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getMode() {
        return this.mMode;
    }

    public void setMode(String mode) {
        this.mMode = mode;
    }
}
