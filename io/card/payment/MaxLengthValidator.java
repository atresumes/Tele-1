package io.card.payment;

class MaxLengthValidator extends NonEmptyValidator implements Validator {
    private int maxLength;

    MaxLengthValidator(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isValid() {
        return super.isValid() && getValue().length() <= this.maxLength;
    }
}
