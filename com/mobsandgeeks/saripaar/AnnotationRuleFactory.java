package com.mobsandgeeks.saripaar;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.Spinner;
import android.widget.TextView;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.IpAddress;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class AnnotationRuleFactory {
    static final String TAG = "AnnotationToRuleConverter";
    static final String WARN_CHECKABLE = "%s - @%s can only be applied to Checkable, its implementations and subclasses.";
    static final String WARN_SPINNER = "%s - @%s can only be applied to Spinner, its implementations and subclasses.";
    static final String WARN_TEXT = "%s - @%s can only be applied to TextView and its subclasses.";

    AnnotationRuleFactory() {
    }

    public static Rule<?> getRule(Field field, View view, Annotation annotation) {
        Class<?> annotationType = annotation.annotationType();
        if (Checked.class.equals(annotationType)) {
            return getCheckedRule(field, view, (Checked) annotation);
        }
        if (Required.class.equals(annotationType)) {
            return getRequiredRule(field, view, (Required) annotation);
        }
        if (TextRule.class.equals(annotationType)) {
            return getTextRule(field, view, (TextRule) annotation);
        }
        if (Regex.class.equals(annotationType)) {
            return getRegexRule(field, view, (Regex) annotation);
        }
        if (NumberRule.class.equals(annotationType)) {
            return getNumberRule(field, view, (NumberRule) annotation);
        }
        if (Password.class.equals(annotationType)) {
            return getPasswordRule(field, view, (Password) annotation);
        }
        if (Email.class.equals(annotationType)) {
            return getEmailRule(field, view, (Email) annotation);
        }
        if (IpAddress.class.equals(annotationType)) {
            return getIpAddressRule(field, view, (IpAddress) annotation);
        }
        if (Select.class.equals(annotationType)) {
            return getSelectRule(field, view, (Select) annotation);
        }
        return null;
    }

    private static Rule<Spinner> getSelectRule(Field field, View view, Select select) {
        if (Spinner.class.isAssignableFrom(view.getClass())) {
            int messageResId = select.messageResId();
            return Rules.spinnerNotEq(messageResId != 0 ? view.getContext().getString(messageResId) : select.message(), select.defaultSelection());
        }
        Log.w(TAG, String.format(WARN_SPINNER, new Object[]{field.getName(), Spinner.class.getSimpleName()}));
        return null;
    }

    public static Rule<?> getRule(Field field, View view, Annotation annotation, Object... params) {
        if (!ConfirmPassword.class.equals(annotation.annotationType())) {
            return (params == null || params.length == 0) ? getRule(field, view, annotation) : null;
        } else {
            return getConfirmPasswordRule(field, view, (ConfirmPassword) annotation, params[0]);
        }
    }

    private static Rule<TextView> getRequiredRule(Field field, View view, Required required) {
        if (TextView.class.isAssignableFrom(view.getClass())) {
            int messageResId = required.messageResId();
            return Rules.required(messageResId != 0 ? view.getContext().getString(messageResId) : required.message(), required.trim());
        }
        Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), Required.class.getSimpleName()}));
        return null;
    }

    private static Rule<View> getTextRule(Field field, View view, TextRule textRule) {
        if (TextView.class.isAssignableFrom(view.getClass())) {
            List<Rule<?>> rules = new ArrayList();
            int messageResId = textRule.messageResId();
            String message = messageResId != 0 ? view.getContext().getString(messageResId) : textRule.message();
            if (textRule.minLength() > 0) {
                rules.add(Rules.minLength(null, textRule.minLength(), textRule.trim()));
            }
            if (textRule.maxLength() != ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
                rules.add(Rules.maxLength(null, textRule.maxLength(), textRule.trim()));
            }
            Rule<?>[] ruleArray = new Rule[rules.size()];
            rules.toArray(ruleArray);
            return Rules.and(message, ruleArray);
        }
        Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), TextRule.class.getSimpleName()}));
        return null;
    }

    private static Rule<TextView> getRegexRule(Field field, View view, Regex regexRule) {
        if (TextView.class.isAssignableFrom(view.getClass())) {
            Context context = view.getContext();
            int messageResId = regexRule.messageResId();
            String message = messageResId != 0 ? context.getString(messageResId) : regexRule.message();
            int patternResId = regexRule.patternResId();
            return Rules.regex(message, patternResId != 0 ? view.getContext().getString(patternResId) : regexRule.pattern(), regexRule.trim());
        }
        Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), Regex.class.getSimpleName()}));
        return null;
    }

    private static Rule<View> getNumberRule(Field field, View view, NumberRule numberRule) {
        if (!TextView.class.isAssignableFrom(view.getClass())) {
            Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), NumberRule.class.getSimpleName()}));
            return null;
        } else if (numberRule.type() == null) {
            throw new IllegalArgumentException(String.format("@%s.type() cannot be null.", new Object[]{NumberRule.class.getSimpleName()}));
        } else {
            double number;
            List<Rule<?>> rules = new ArrayList();
            int messageResId = numberRule.messageResId();
            String message = messageResId != 0 ? view.getContext().getString(messageResId) : numberRule.message();
            switch (numberRule.type()) {
                case INTEGER:
                case LONG:
                    Rules.regex(null, Rules.REGEX_INTEGER, true);
                    break;
                case FLOAT:
                case DOUBLE:
                    Rules.regex(null, Rules.REGEX_DECIMAL, true);
                    break;
            }
            if (numberRule.lt() != Double.MIN_VALUE) {
                String ltNumber = String.valueOf(numberRule.lt());
                number = Double.parseDouble(ltNumber);
                switch (numberRule.type()) {
                    case INTEGER:
                        rules.add(Rules.lt(null, (int) number));
                        break;
                    case LONG:
                        rules.add(Rules.lt(null, (long) number));
                        break;
                    case FLOAT:
                        rules.add(Rules.lt(null, Float.parseFloat(ltNumber)));
                        break;
                    case DOUBLE:
                        rules.add(Rules.lt(null, Double.parseDouble(ltNumber)));
                        break;
                }
            }
            if (numberRule.gt() != Double.MAX_VALUE) {
                String gtNumber = String.valueOf(numberRule.gt());
                number = Double.parseDouble(gtNumber);
                switch (numberRule.type()) {
                    case INTEGER:
                        rules.add(Rules.gt(null, (int) number));
                        break;
                    case LONG:
                        rules.add(Rules.gt(null, (long) number));
                        break;
                    case FLOAT:
                        rules.add(Rules.gt(null, Float.parseFloat(gtNumber)));
                        break;
                    case DOUBLE:
                        rules.add(Rules.gt(null, Double.parseDouble(gtNumber)));
                        break;
                }
            }
            if (numberRule.eq() != Double.MAX_VALUE) {
                String eqNumber = String.valueOf(numberRule.eq());
                number = Double.parseDouble(eqNumber);
                switch (numberRule.type()) {
                    case INTEGER:
                        rules.add(Rules.eq(null, (int) number));
                        break;
                    case LONG:
                        rules.add(Rules.eq(null, (long) number));
                        break;
                    case FLOAT:
                        rules.add(Rules.eq(null, Float.parseFloat(eqNumber)));
                        break;
                    case DOUBLE:
                        rules.add(Rules.eq(null, Double.parseDouble(eqNumber)));
                        break;
                }
            }
            Rule<?>[] ruleArray = new Rule[rules.size()];
            rules.toArray(ruleArray);
            return Rules.and(message, ruleArray);
        }
    }

    private static Rule<TextView> getPasswordRule(Field field, View view, Password password) {
        if (TextView.class.isAssignableFrom(view.getClass())) {
            int messageResId = password.messageResId();
            return Rules.required(messageResId != 0 ? view.getContext().getString(messageResId) : password.message(), false);
        }
        Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), Password.class.getSimpleName()}));
        return null;
    }

    private static Rule<TextView> getConfirmPasswordRule(Field field, View view, ConfirmPassword confirmPassword, TextView passwordTextView) {
        if (TextView.class.isAssignableFrom(view.getClass())) {
            int messageResId = confirmPassword.messageResId();
            return Rules.eq(messageResId != 0 ? view.getContext().getString(messageResId) : confirmPassword.message(), passwordTextView);
        }
        Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), ConfirmPassword.class.getSimpleName()}));
        return null;
    }

    private static Rule<View> getEmailRule(Field field, View view, Email email) {
        if (TextView.class.isAssignableFrom(view.getClass())) {
            int messageResId = email.messageResId();
            return Rules.or(messageResId != 0 ? view.getContext().getString(messageResId) : email.message(), Rules.eq(null, ""), Rules.regex(messageResId != 0 ? view.getContext().getString(messageResId) : email.message(), Rules.REGEX_EMAIL, true));
        }
        Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), Regex.class.getSimpleName()}));
        return null;
    }

    private static Rule<View> getIpAddressRule(Field field, View view, IpAddress ipAddress) {
        if (TextView.class.isAssignableFrom(view.getClass())) {
            int messageResId = ipAddress.messageResId();
            return Rules.or(messageResId != 0 ? view.getContext().getString(messageResId) : ipAddress.message(), Rules.eq(null, ""), Rules.regex(messageResId != 0 ? view.getContext().getString(messageResId) : ipAddress.message(), Rules.REGEX_IP_ADDRESS, true));
        }
        Log.w(TAG, String.format(WARN_TEXT, new Object[]{field.getName(), IpAddress.class.getSimpleName()}));
        return null;
    }

    private static Rule<Checkable> getCheckedRule(Field field, View view, Checked checked) {
        if (Checkable.class.isAssignableFrom(view.getClass())) {
            int messageResId = checked.messageResId();
            return Rules.checked(messageResId != 0 ? view.getContext().getString(messageResId) : checked.message(), checked.checked());
        }
        Log.w(TAG, String.format(WARN_CHECKABLE, new Object[]{field.getName(), Checked.class.getSimpleName()}));
        return null;
    }
}
