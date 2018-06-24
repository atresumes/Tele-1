package com.mobsandgeeks.saripaar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Checkable;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.LinkedHashMap;

public final class Rules {
    public static final String EMPTY_STRING = "";
    private static final String GOOD_IRI_CHAR = "a-zA-Z0-9 -퟿豈-﷏ﷰ-￯";
    public static final String REGEX_DECIMAL = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
    public static final String REGEX_EMAIL = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
    public static final String REGEX_INTEGER = "\\d+";
    public static final String REGEX_IP_ADDRESS = "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))";
    public static final String REGEX_WEB_URL = "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?((?:(?:[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯][a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\-]{0,64}\\.)+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])|(?:jobs|j[emop])|k[eghimnprwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eosuw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agksyz]|v[aceginu]|w[fs]|(?:xn\\-\\-0zwm56d|xn\\-\\-11b5bs3a9aj6g|xn\\-\\-80akhbyknj4f|xn\\-\\-9t4b11yi5a|xn\\-\\-deba0ad|xn\\-\\-g6w251d|xn\\-\\-hgbk6aj7f53bba|xn\\-\\-hlcj6aya9esc7a|xn\\-\\-jxalpdlp|xn\\-\\-kgbechtv|xn\\-\\-zckzah)|y[etu]|z[amw]))|(?:(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)";
    private static final String TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL = "(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])|(?:jobs|j[emop])|k[eghimnprwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eosuw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agksyz]|v[aceginu]|w[fs]|(?:xn\\-\\-0zwm56d|xn\\-\\-11b5bs3a9aj6g|xn\\-\\-80akhbyknj4f|xn\\-\\-9t4b11yi5a|xn\\-\\-deba0ad|xn\\-\\-g6w251d|xn\\-\\-hgbk6aj7f53bba|xn\\-\\-hlcj6aya9esc7a|xn\\-\\-jxalpdlp|xn\\-\\-kgbechtv|xn\\-\\-zckzah)|y[etu]|z[amw]))";

    public static Rule<TextView> required(String failureMessage, final boolean trimInput) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView textView) {
                return !TextUtils.isEmpty(Rules.getText(textView, trimInput));
            }
        };
    }

    public static Rule<TextView> regex(String failureMessage, final String regex, final boolean trimInput) {
        if (regex != null) {
            return new Rule<TextView>(failureMessage) {
                public boolean isValid(TextView textView) {
                    String text = Rules.getText(textView, trimInput);
                    return text != null ? text.matches(regex) : false;
                }
            };
        }
        throw new IllegalArgumentException("'regex' cannot be null");
    }

    public static Rule<TextView> minLength(String failureMessage, final int minLength, final boolean trimInput) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String text = Rules.getText(view, trimInput);
                if (text == null || text.length() < minLength) {
                    return false;
                }
                return true;
            }
        };
    }

    public static Rule<TextView> maxLength(String failureMessage, final int maxLength, final boolean trimInput) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String text = Rules.getText(view, trimInput);
                if (text == null || text.length() > maxLength) {
                    return false;
                }
                return true;
            }
        };
    }

    public static Rule<TextView> eq(String failureMessage, final TextView anotherTextView) {
        if (anotherTextView != null) {
            return new Rule<TextView>(failureMessage) {
                public boolean isValid(TextView view) {
                    return view.getText().toString().equals(anotherTextView.getText().toString());
                }
            };
        }
        throw new IllegalArgumentException("'anotherTextView' cannot be null");
    }

    public static Rule<TextView> eq(String failureMessage, String expectedString) {
        return eq(failureMessage, expectedString, false, false);
    }

    public static Rule<TextView> eq(String failureMessage, String expectedString, final boolean ignoreCase, final boolean trimInput) {
        String cleanString;
        if (expectedString == null) {
            cleanString = "";
        } else {
            cleanString = expectedString;
        }
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView textView) {
                String actualString = Rules.getText(textView, trimInput);
                if (actualString != null) {
                    return ignoreCase ? actualString.equalsIgnoreCase(cleanString) : actualString.equals(cleanString);
                } else {
                    return false;
                }
            }
        };
    }

    public static Rule<TextView> eq(String failureMessage, int expectedInt) {
        return eq(failureMessage, (long) expectedInt);
    }

    public static Rule<TextView> gt(String failureMessage, int lesserInt) {
        return gt(failureMessage, (long) lesserInt);
    }

    public static Rule<TextView> lt(String failureMessage, int greaterInt) {
        return lt(failureMessage, (long) greaterInt);
    }

    public static Rule<TextView> eq(String failureMessage, final long expectedLong) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView textView) {
                String actualLong = Rules.getText(textView, true);
                if (actualLong == null) {
                    return false;
                }
                if (!actualLong.matches(Rules.REGEX_INTEGER)) {
                    return false;
                }
                if (Long.parseLong(actualLong) == expectedLong) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> gt(String failureMessage, final long lesserLong) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView textView) {
                String actualLong = Rules.getText(textView, true);
                if (actualLong == null) {
                    return false;
                }
                if (!actualLong.matches(Rules.REGEX_INTEGER)) {
                    return false;
                }
                if (Long.parseLong(actualLong) > lesserLong) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> lt(String failureMessage, final long greaterLong) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView textView) {
                String actualLong = Rules.getText(textView, true);
                if (actualLong == null) {
                    return false;
                }
                if (!actualLong.matches(Rules.REGEX_INTEGER)) {
                    return false;
                }
                if (Long.parseLong(actualLong) < greaterLong) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> eq(String failureMessage, final float expectedFloat) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String actualFloat = Rules.getText(view, true);
                if (actualFloat == null) {
                    return false;
                }
                if (!actualFloat.matches(Rules.REGEX_DECIMAL)) {
                    return false;
                }
                if (Float.parseFloat(actualFloat) == expectedFloat) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> gt(String failureMessage, final float lesserFloat) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String actualFloat = Rules.getText(view, true);
                if (actualFloat == null) {
                    return false;
                }
                if (!actualFloat.matches(Rules.REGEX_DECIMAL)) {
                    return false;
                }
                if (Float.parseFloat(actualFloat) > lesserFloat) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> lt(String failureMessage, final float greaterFloat) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String actualFloat = Rules.getText(view, true);
                if (actualFloat == null) {
                    return false;
                }
                if (!actualFloat.matches(Rules.REGEX_DECIMAL)) {
                    return false;
                }
                if (Float.parseFloat(actualFloat) < greaterFloat) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> eq(String failureMessage, final double expectedDouble) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String actualDouble = Rules.getText(view, true);
                if (actualDouble == null) {
                    return false;
                }
                if (!actualDouble.matches(Rules.REGEX_DECIMAL)) {
                    return false;
                }
                if (Double.parseDouble(actualDouble) == expectedDouble) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> gt(String failureMessage, final double lesserDouble) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String actualDouble = Rules.getText(view, true);
                if (actualDouble == null) {
                    return false;
                }
                if (!actualDouble.matches(Rules.REGEX_DECIMAL)) {
                    return false;
                }
                if (Double.parseDouble(actualDouble) > lesserDouble) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<TextView> lt(String failureMessage, final double greaterDouble) {
        return new Rule<TextView>(failureMessage) {
            public boolean isValid(TextView view) {
                String actualDouble = Rules.getText(view, true);
                if (actualDouble == null) {
                    return false;
                }
                if (!actualDouble.matches(Rules.REGEX_DECIMAL)) {
                    return false;
                }
                if (Double.parseDouble(actualDouble) < greaterDouble) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Rule<Checkable> checked(String failureMessage, final boolean checked) {
        return new Rule<Checkable>(failureMessage) {
            public boolean isValid(Checkable view) {
                return view.isChecked() == checked;
            }
        };
    }

    public static Rule<Spinner> spinnerEq(String failureMessage, final String expectedString, final boolean ignoreCase, final boolean trimInput) {
        return new Rule<Spinner>(failureMessage) {
            public boolean isValid(Spinner spinner) {
                Object selectedItem = spinner.getSelectedItem();
                if (expectedString == null && selectedItem == null) {
                    return true;
                }
                if (expectedString == null || selectedItem == null) {
                    return false;
                }
                String selectedItemString = selectedItem.toString();
                if (trimInput) {
                    selectedItemString = selectedItemString.trim();
                }
                return ignoreCase ? selectedItemString.equalsIgnoreCase(expectedString) : selectedItemString.equals(expectedString);
            }
        };
    }

    public static Rule<Spinner> spinnerEq(String failureMessage, final int expectedPosition) {
        return new Rule<Spinner>(failureMessage) {
            public boolean isValid(Spinner spinner) {
                return spinner.getSelectedItemPosition() == expectedPosition;
            }
        };
    }

    public static Rule<Spinner> spinnerNotEq(String failureMessage, final int selection) {
        return new Rule<Spinner>(failureMessage) {
            public boolean isValid(Spinner spinner) {
                return spinner.getSelectedItemPosition() != selection;
            }
        };
    }

    public static Rule<View> and(String failureMessage, final Rule<?>... rules) {
        return new Rule<View>(failureMessage) {
            public boolean isValid(View view) {
                boolean valid = true;
                for (Rule rule : rules) {
                    if (rule != null) {
                        valid &= rule.isValid(view);
                    }
                    if (!valid) {
                        break;
                    }
                }
                return valid;
            }
        };
    }

    public static Rule<View> or(String failureMessage, final Rule<?>... rules) {
        return new Rule<View>(failureMessage) {
            public boolean isValid(View view) {
                boolean valid = false;
                for (Rule rule : rules) {
                    if (rule != null) {
                        valid |= rule.isValid(view);
                    }
                    if (valid) {
                        break;
                    }
                }
                return valid;
            }
        };
    }

    public static Rule<View> compositeAnd(String failureMessage, final LinkedHashMap<View, Rule<?>> viewsAndRules) {
        return new Rule<View>(failureMessage) {
            public boolean isValid(View view) {
                boolean valid = true;
                for (View viewKey : viewsAndRules.keySet()) {
                    valid &= ((Rule) viewsAndRules.get(viewKey)).isValid(view);
                    if (!valid) {
                        break;
                    }
                }
                return valid;
            }
        };
    }

    public static Rule<View> compositeOr(String failureMessage, final LinkedHashMap<View, Rule<?>> viewsAndRules) {
        return new Rule<View>(failureMessage) {
            public boolean isValid(View view) {
                boolean valid = false;
                for (View viewKey : viewsAndRules.keySet()) {
                    valid |= ((Rule) viewsAndRules.get(viewKey)).isValid(viewKey);
                    if (valid) {
                        break;
                    }
                }
                return valid;
            }
        };
    }

    private static String getText(TextView textView, boolean trim) {
        CharSequence text = null;
        if (textView != null) {
            text = textView.getText();
            if (trim) {
                text = text.toString().trim();
            }
        }
        return text != null ? text.toString() : null;
    }
}
