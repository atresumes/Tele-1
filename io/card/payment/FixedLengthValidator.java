package io.card.payment;

import android.text.Editable;
import android.text.Spanned;

class FixedLengthValidator implements Validator {
    public int requiredLength;
    private String value;

    public FixedLengthValidator(int length) {
        this.requiredLength = length;
    }

    public void afterTextChanged(Editable s) {
        this.value = s.toString();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public String getValue() {
        return this.value;
    }

    public boolean hasFullLength() {
        return isValid();
    }

    public boolean isValid() {
        if (this.value == null || this.value.length() != this.requiredLength) {
            return false;
        }
        return true;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (end <= 0 || ((dest.length() + dend) - dstart) + end <= this.requiredLength) {
            return null;
        }
        return "";
    }
}
