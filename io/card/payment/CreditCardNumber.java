package io.card.payment;

import java.text.CharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;
import java.util.Date;

class CreditCardNumber {
    public static boolean passesLuhnChecksum(String number) {
        boolean z = true;
        int sum = 0;
        int[][] sums = new int[][]{new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{0, 2, 4, 6, 8, 1, 3, 5, 7, 9}};
        CharacterIterator iter = new StringCharacterIterator(number);
        char c = iter.last();
        int even = 0;
        while (c != '￿') {
            if (!Character.isDigit(c)) {
                return false;
            }
            int even2 = even + 1;
            sum += sums[even & 1][c - 48];
            c = iter.previous();
            even = even2;
        }
        if (sum % 10 != 0) {
            z = false;
        }
        return z;
    }

    public static String formatString(String numStr) {
        return formatString(numStr, true, null);
    }

    public static String formatString(String numStr, boolean filterDigits, CardType type) {
        String digits;
        if (filterDigits) {
            digits = StringHelper.getDigitsOnlyString(numStr);
        } else {
            digits = numStr;
        }
        if (type == null) {
            type = CardType.fromCardNumber(digits);
        }
        int numLen = type.numberLength();
        if (digits.length() != numLen) {
            return numStr;
        }
        if (numLen == 16) {
            return formatSixteenString(digits);
        }
        if (numLen == 15) {
            return formatFifteenString(digits);
        }
        return numStr;
    }

    private static String formatFifteenString(String digits) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < 15) {
            if (i == 4 || i == 10) {
                sb.append(' ');
            }
            sb.append(digits.charAt(i));
            i++;
        }
        return sb.toString();
    }

    private static String formatSixteenString(String digits) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < 16) {
            if (i != 0 && i % 4 == 0) {
                sb.append(' ');
            }
            sb.append(digits.charAt(i));
            i++;
        }
        return sb.toString();
    }

    public static boolean isDateValid(int expiryMonth, int expiryYear) {
        if (expiryMonth < 1 || 12 < expiryMonth) {
            return false;
        }
        Calendar now = Calendar.getInstance();
        int thisYear = now.get(1);
        int thisMonth = now.get(2) + 1;
        if (expiryYear < thisYear) {
            return false;
        }
        if ((expiryYear != thisYear || expiryMonth >= thisMonth) && expiryYear <= thisYear + 15) {
            return true;
        }
        return false;
    }

    public static SimpleDateFormat getDateFormatForLength(int len) {
        if (len == 4) {
            return new SimpleDateFormat("MMyy");
        }
        if (len == 6) {
            return new SimpleDateFormat("MMyyyy");
        }
        return null;
    }

    public static Date getDateForString(String dateString) {
        Date date = null;
        String digitsOnly = StringHelper.getDigitsOnlyString(dateString);
        SimpleDateFormat validDate = getDateFormatForLength(digitsOnly.length());
        if (validDate != null) {
            try {
                validDate.setLenient(false);
                date = validDate.parse(digitsOnly);
            } catch (ParseException e) {
            }
        }
        return date;
    }
}
