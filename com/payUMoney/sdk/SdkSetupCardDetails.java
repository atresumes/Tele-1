package com.payUMoney.sdk;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.util.Arrays;

public class SdkSetupCardDetails {
    public static String findIssuer(String mNumber, String cardMode) {
        if (mNumber != null) {
            mNumber = mNumber.replaceAll("\\s+", "");
        }
        if (mNumber.length() > 5) {
            if (mNumber.matches("^3[47][\\d|\\D]+")) {
                return SdkConstants.AMEX;
            }
            if (mNumber.matches("^30[0-5][\\d|\\D]+")) {
                return "DINR";
            }
            if (mNumber.startsWith("36")) {
                return "DINR";
            }
            if (mNumber.matches("^35(2[89]|[3-8][0-9])[\\d|\\D]+")) {
                return "JCB";
            }
            if (Arrays.asList(new String[]{"6304", "6706", "6771", "6709"}).contains(mNumber.substring(0, 4))) {
                return "LASER";
            }
            if (mNumber.matches("^(4026|417500|4508|4844|491(3|7))[\\d|\\D]+")) {
                return "VISA";
            }
            if (mNumber.startsWith("4")) {
                return "VISA";
            }
            if (mNumber.matches("^5[1-5][\\d|\\D]+")) {
                return "MAST";
            }
            if (mNumber.substring(0, 6).matches("^(508[5-9][0-9][0-9])|(60698[5-9])|(60699[0-9])|(607[0-8][0-9][0-9])|(6079[0-7][0-9])|(60798[0-4])|(608[0-4][0-9][0-9])|(608500)|(6528[5-9][0-9])|(6529[0-9][0-9])|(6530[0-9][0-9])|(6531[0-4][0-9])|(6521[5-9][0-9])|(652[2-7][0-9][0-9])|(6528[0-4][0-9])")) {
                return "RUPAY";
            }
            if (mNumber.matches("(5[06-8]|6\\d|\\D)\\d{14}|\\D{14}(\\d{2,3}|\\D{2,3})?[\\d|\\D]+") || mNumber.matches("(5[06-8]|6\\d|\\D)[\\d|\\D]+") || mNumber.matches("((504([435|645|774|775|809|993]))|(60([0206]|[3845]))|(622[018])\\d|\\D)[\\d|\\D]+")) {
                return "MAES";
            }
            if (mNumber.matches("6(?:011|5[0-9]{2})[0-9]{12}")) {
                return SdkConstants.PAYMENT_MODE_CC;
            }
            if (cardMode.contentEquals(SdkConstants.PAYMENT_MODE_CC)) {
                return SdkConstants.PAYMENT_MODE_CC;
            }
            if (cardMode.contentEquals(SdkConstants.PAYMENT_MODE_DC)) {
                return "MAST";
            }
        }
        return null;
    }

    public static Drawable getCardDrawable(Resources resources, String mNumber) {
        Drawable amexDrawable = resources.getDrawable(C0360R.drawable.amex);
        Drawable dinerDrawable = resources.getDrawable(C0360R.drawable.diner);
        Drawable maestroDrawable = resources.getDrawable(C0360R.drawable.maestro);
        Drawable masterDrawable = resources.getDrawable(C0360R.drawable.master);
        Drawable visaDrawable = resources.getDrawable(C0360R.drawable.visa);
        Drawable jcbDrawable = resources.getDrawable(C0360R.drawable.jcb);
        Drawable laserDrawable = resources.getDrawable(C0360R.drawable.laser);
        Drawable discoverDrawable = resources.getDrawable(C0360R.drawable.discover);
        Drawable cardsDrawable = resources.getDrawable(C0360R.drawable.card);
        Drawable dinerClubDrawable = resources.getDrawable(C0360R.drawable.dinners_club);
        Drawable visaElectronDrawable = resources.getDrawable(C0360R.drawable.visa_electron_card);
        Drawable rupayDrawable = resources.getDrawable(C0360R.drawable.rupay_card);
        if (mNumber.matches("^3[47][\\d|\\D]+")) {
            return amexDrawable;
        }
        if (mNumber.matches("^30[0-5][\\d|\\D]+")) {
            return dinerDrawable;
        }
        if (mNumber.startsWith("36")) {
            return dinerClubDrawable;
        }
        if (mNumber.matches("^35(2[89]|[3-8][0-9])[\\d|\\D]+")) {
            return jcbDrawable;
        }
        if (Arrays.asList(new String[]{"6304", "6706", "6771", "6709"}).contains(mNumber.substring(0, 4))) {
            return laserDrawable;
        }
        if (mNumber.matches("^(4026|417500|4508|4844|491(3|7))")) {
            return visaElectronDrawable;
        }
        if (mNumber.startsWith("4")) {
            return visaDrawable;
        }
        if (mNumber.matches("^5[1-5][\\d|\\D]+")) {
            return masterDrawable;
        }
        if (mNumber.substring(0, 6).matches("(?!608000)(^(508[5-9][0-9][0-9])|(60698[5-9])|(60699[0-9])|(607[0-8][0-9][0-9])|(6079[0-7][0-9])|(60798[0-4])|(608[0-4][0-9][0-9])|(608500)|(6528[5-9][0-9])|(6529[0-9][0-9])|(6530[0-9][0-9])|(6531[0-4][0-9])|(6521[5-9][0-9])|(652[2-7][0-9][0-9])|(6528[0-4][0-9]))")) {
            return rupayDrawable;
        }
        if (!mNumber.matches("(5[06-8]|6\\d|\\D)\\d{14}|\\D{14}(\\d{2,3}|\\D{2,3})?[\\d|\\D]+")) {
            if (!mNumber.matches("(5[06-8]|6\\d|\\D)[\\d|\\D]+")) {
                if (!mNumber.matches("((504([435|645|774|775|809|993]))|(60([0206]|[3845]))|(622[018])\\d|\\D)[\\d|\\D]+")) {
                    if (mNumber.matches("6(?:011|5[0-9]{2})[0-9]{12}")) {
                        return discoverDrawable;
                    }
                    return cardsDrawable;
                }
            }
        }
        return maestroDrawable;
    }
}
