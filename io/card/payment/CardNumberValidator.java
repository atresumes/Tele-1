package io.card.payment;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

class CardNumberValidator implements Validator {
    static final int[] AMEX_SPACER = new int[]{4, 11};
    static final int[] NORMAL_SPACER = new int[]{4, 9, 14};
    private String numberString;
    private int spacerToDelete;

    public CardNumberValidator(String number) {
        this.numberString = number;
    }

    public void afterTextChanged(Editable source) {
        this.numberString = StringHelper.getDigitsOnlyString(source.toString());
        CardType type = CardType.fromCardNumber(this.numberString);
        if (this.spacerToDelete > 1) {
            int e = this.spacerToDelete;
            int s = this.spacerToDelete - 1;
            this.spacerToDelete = 0;
            if (e > s) {
                source.delete(s, e);
            }
        }
        int i = 0;
        while (i < source.length()) {
            char c = source.charAt(i);
            if ((type.numberLength() == 15 && (i == 4 || i == 11)) || ((type.numberLength() == 16 || type.numberLength() == 14) && (i == 4 || i == 9 || i == 14))) {
                if (c != ' ') {
                    source.insert(i, " ");
                }
            } else if (c == ' ') {
                source.delete(i, i + 1);
                i--;
            }
            i++;
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public boolean hasFullLength() {
        if (TextUtils.isEmpty(this.numberString)) {
            return false;
        }
        if (this.numberString.length() == CardType.fromCardNumber(this.numberString).numberLength()) {
            return true;
        }
        return false;
    }

    public boolean isValid() {
        if (hasFullLength() && CreditCardNumber.passesLuhnChecksum(this.numberString)) {
            return true;
        }
        return false;
    }

    public String getValue() {
        return this.numberString;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String updatedDigits = StringHelper.getDigitsOnlyString(new SpannableStringBuilder(dest).replace(dstart, dend, source, start, end).toString());
        int maxLength = CardType.fromCardNumber(updatedDigits).numberLength();
        if (updatedDigits.length() > maxLength) {
            return "";
        }
        int[] spacers;
        CharSequence result = new SpannableStringBuilder(source);
        if (maxLength == 15) {
            spacers = AMEX_SPACER;
        } else {
            spacers = NORMAL_SPACER;
        }
        int replen = dend - dstart;
        int i = 0;
        while (i < spacers.length) {
            if (source.length() == 0 && dstart == spacers[i] && dest.charAt(dstart) == ' ') {
                this.spacerToDelete = spacers[i];
            }
            if (dstart - replen <= spacers[i] && (dstart + end) - replen >= spacers[i]) {
                int loc = spacers[i] - dstart;
                if (loc == end || (loc >= 0 && loc < end && result.charAt(loc) != ' ')) {
                    result.insert(loc, " ");
                    end++;
                }
            }
            i++;
        }
        return result;
    }
}
