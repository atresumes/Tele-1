package io.card.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DateKeyListener;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import io.card.payment.i18n.LocalizedStrings;
import io.card.payment.i18n.StringKey;
import io.card.payment.ui.ActivityHelper;
import io.card.payment.ui.Appearance;
import io.card.payment.ui.ViewUtil;

public final class DataEntryActivity extends Activity implements TextWatcher {
    private static final String TAG = DataEntryActivity.class.getSimpleName();
    private TextView activityTitleTextView;
    private boolean autoAcceptDone;
    private Button cancelBtn;
    private CreditCard capture;
    private ImageView cardView;
    private EditText cardholderNameEdit;
    private Validator cardholderNameValidator;
    private EditText cvvEdit;
    private Validator cvvValidator;
    private int defaultTextColor;
    private Button doneBtn;
    private int editTextIdCounter = 100;
    private EditText expiryEdit;
    private Validator expiryValidator;
    private String labelLeftPadding;
    private EditText numberEdit;
    private Validator numberValidator;
    private EditText postalCodeEdit;
    private Validator postalCodeValidator;
    private boolean useApplicationTheme;
    private int viewIdCounter = 1;

    class C06731 implements OnClickListener {
        C06731() {
        }

        public void onClick(View v) {
            DataEntryActivity.this.completed();
        }
    }

    class C06742 implements OnClickListener {
        C06742() {
        }

        public void onClick(View v) {
            DataEntryActivity.this.onBackPressed();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        if (getIntent().getExtras() == null) {
            onBackPressed();
            return;
        }
        this.useApplicationTheme = getIntent().getBooleanExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false);
        ActivityHelper.setActivityTheme(this, this.useApplicationTheme);
        this.defaultTextColor = new TextView(this).getTextColors().getDefaultColor();
        this.labelLeftPadding = ActivityHelper.holoSupported() ? "12dip" : "2dip";
        LocalizedStrings.setLanguage(getIntent());
        int paddingPx = ViewUtil.typedDimensionValueToPixelsInt("4dip", this);
        RelativeLayout container = new RelativeLayout(this);
        if (!this.useApplicationTheme) {
            container.setBackgroundColor(Appearance.DEFAULT_BACKGROUND_COLOR);
        }
        View scrollView = new ScrollView(this);
        int i = this.viewIdCounter;
        this.viewIdCounter = i + 1;
        scrollView.setId(i);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(10);
        container.addView(scrollView, layoutParams);
        scrollView = new LinearLayout(this);
        scrollView.setOrientation(1);
        scrollView.addView(scrollView, -1, -1);
        scrollView = new LinearLayout(this);
        scrollView.setOrientation(1);
        layoutParams = new LinearLayout.LayoutParams(-1, -1);
        this.capture = (CreditCard) getIntent().getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
        this.autoAcceptDone = getIntent().getBooleanExtra("debug_autoAcceptResult", false);
        if (this.capture != null) {
            this.numberValidator = new CardNumberValidator(this.capture.cardNumber);
            this.cardView = new ImageView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(-1, -2);
            this.cardView.setPadding(0, 0, 0, paddingPx);
            cardParams.weight = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            this.cardView.setImageBitmap(CardIOActivity.markedCardImage);
            scrollView.addView(this.cardView, cardParams);
            ViewUtil.setMargins(this.cardView, null, null, null, "8dip");
        } else {
            this.activityTitleTextView = new TextView(this);
            this.activityTitleTextView.setTextSize(24.0f);
            if (!this.useApplicationTheme) {
                this.activityTitleTextView.setTextColor(Appearance.PAY_BLUE_COLOR);
            }
            scrollView.addView(this.activityTitleTextView);
            ViewUtil.setPadding(this.activityTitleTextView, null, null, null, "8dip");
            ViewUtil.setDimensions(this.activityTitleTextView, -2, -2);
            scrollView = new LinearLayout(this);
            scrollView.setOrientation(1);
            ViewUtil.setPadding(scrollView, null, "4dip", null, "4dip");
            scrollView = new TextView(this);
            ViewUtil.setPadding(scrollView, this.labelLeftPadding, null, null, null);
            scrollView.setText(LocalizedStrings.getString(StringKey.ENTRY_CARD_NUMBER));
            if (!this.useApplicationTheme) {
                scrollView.setTextColor(Appearance.TEXT_COLOR_LABEL);
            }
            scrollView.addView(scrollView, -2, -2);
            this.numberEdit = new EditText(this);
            EditText editText = this.numberEdit;
            int i2 = this.editTextIdCounter;
            this.editTextIdCounter = i2 + 1;
            editText.setId(i2);
            this.numberEdit.setMaxLines(1);
            this.numberEdit.setImeOptions(6);
            this.numberEdit.setTextAppearance(getApplicationContext(), 16842816);
            this.numberEdit.setInputType(3);
            this.numberEdit.setHint("1234 5678 1234 5678");
            if (!this.useApplicationTheme) {
                this.numberEdit.setHintTextColor(-3355444);
            }
            this.numberValidator = new CardNumberValidator();
            this.numberEdit.addTextChangedListener(this.numberValidator);
            this.numberEdit.addTextChangedListener(this);
            this.numberEdit.setFilters(new InputFilter[]{new DigitsKeyListener(), this.numberValidator});
            scrollView.addView(this.numberEdit, -1, -2);
            scrollView.addView(scrollView, -1, -1);
        }
        scrollView = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(-1, -2);
        ViewUtil.setPadding(scrollView, null, "4dip", null, "4dip");
        scrollView.setOrientation(0);
        boolean requireExpiry = getIntent().getBooleanExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false);
        boolean requireCVV = getIntent().getBooleanExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
        boolean requirePostalCode = getIntent().getBooleanExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
        if (requireExpiry) {
            scrollView = new LinearLayout(this);
            layoutParams = new LinearLayout.LayoutParams(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            scrollView.setOrientation(1);
            TextView expiryLabel = new TextView(this);
            if (!this.useApplicationTheme) {
                expiryLabel.setTextColor(Appearance.TEXT_COLOR_LABEL);
            }
            expiryLabel.setText(LocalizedStrings.getString(StringKey.ENTRY_EXPIRES));
            ViewUtil.setPadding(expiryLabel, this.labelLeftPadding, null, null, null);
            scrollView.addView(expiryLabel, -2, -2);
            this.expiryEdit = new EditText(this);
            editText = this.expiryEdit;
            i2 = this.editTextIdCounter;
            this.editTextIdCounter = i2 + 1;
            editText.setId(i2);
            this.expiryEdit.setMaxLines(1);
            this.expiryEdit.setImeOptions(6);
            this.expiryEdit.setTextAppearance(getApplicationContext(), 16842816);
            this.expiryEdit.setInputType(3);
            this.expiryEdit.setHint(LocalizedStrings.getString(StringKey.EXPIRES_PLACEHOLDER));
            if (!this.useApplicationTheme) {
                this.expiryEdit.setHintTextColor(-3355444);
            }
            if (this.capture != null) {
                this.expiryValidator = new ExpiryValidator(this.capture.expiryMonth, this.capture.expiryYear);
            } else {
                this.expiryValidator = new ExpiryValidator();
            }
            if (this.expiryValidator.hasFullLength()) {
                this.expiryEdit.setText(this.expiryValidator.getValue());
            }
            this.expiryEdit.addTextChangedListener(this.expiryValidator);
            this.expiryEdit.addTextChangedListener(this);
            this.expiryEdit.setFilters(new InputFilter[]{new DateKeyListener(), this.expiryValidator});
            scrollView.addView(this.expiryEdit, -1, -2);
            scrollView.addView(scrollView, layoutParams);
            String str = (requireCVV || requirePostalCode) ? "4dip" : null;
            ViewUtil.setMargins(scrollView, null, null, str, null);
        } else {
            this.expiryValidator = new AlwaysValid();
        }
        if (requireCVV) {
            LinearLayout cvvLayout = new LinearLayout(this);
            LinearLayout.LayoutParams cvvLayoutParam = new LinearLayout.LayoutParams(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            cvvLayout.setOrientation(1);
            TextView cvvLabel = new TextView(this);
            if (!this.useApplicationTheme) {
                cvvLabel.setTextColor(Appearance.TEXT_COLOR_LABEL);
            }
            ViewUtil.setPadding(cvvLabel, this.labelLeftPadding, null, null, null);
            cvvLabel.setText(LocalizedStrings.getString(StringKey.ENTRY_CVV));
            cvvLayout.addView(cvvLabel, -2, -2);
            this.cvvEdit = new EditText(this);
            editText = this.cvvEdit;
            i2 = this.editTextIdCounter;
            this.editTextIdCounter = i2 + 1;
            editText.setId(i2);
            this.cvvEdit.setMaxLines(1);
            this.cvvEdit.setImeOptions(6);
            this.cvvEdit.setTextAppearance(getApplicationContext(), 16842816);
            this.cvvEdit.setInputType(3);
            this.cvvEdit.setHint("123");
            if (!this.useApplicationTheme) {
                this.cvvEdit.setHintTextColor(-3355444);
            }
            int length = 4;
            if (this.capture != null) {
                length = CardType.fromCardNumber(this.numberValidator.getValue()).cvvLength();
            }
            this.cvvValidator = new FixedLengthValidator(length);
            this.cvvEdit.setFilters(new InputFilter[]{new DigitsKeyListener(), this.cvvValidator});
            this.cvvEdit.addTextChangedListener(this.cvvValidator);
            this.cvvEdit.addTextChangedListener(this);
            cvvLayout.addView(this.cvvEdit, -1, -2);
            scrollView.addView(cvvLayout, cvvLayoutParam);
            ViewUtil.setMargins(cvvLayout, requireExpiry ? "4dip" : null, null, requirePostalCode ? "4dip" : null, null);
        } else {
            this.cvvValidator = new AlwaysValid();
        }
        if (requirePostalCode) {
            scrollView = new LinearLayout(this);
            layoutParams = new LinearLayout.LayoutParams(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            scrollView.setOrientation(1);
            scrollView = new TextView(this);
            if (!this.useApplicationTheme) {
                scrollView.setTextColor(Appearance.TEXT_COLOR_LABEL);
            }
            ViewUtil.setPadding(scrollView, this.labelLeftPadding, null, null, null);
            scrollView.setText(LocalizedStrings.getString(StringKey.ENTRY_POSTAL_CODE));
            scrollView.addView(scrollView, -2, -2);
            boolean postalCodeNumericOnly = getIntent().getBooleanExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false);
            this.postalCodeEdit = new EditText(this);
            editText = this.postalCodeEdit;
            i2 = this.editTextIdCounter;
            this.editTextIdCounter = i2 + 1;
            editText.setId(i2);
            this.postalCodeEdit.setMaxLines(1);
            this.postalCodeEdit.setImeOptions(6);
            this.postalCodeEdit.setTextAppearance(getApplicationContext(), 16842816);
            if (postalCodeNumericOnly) {
                this.postalCodeEdit.setInputType(3);
            } else {
                this.postalCodeEdit.setInputType(1);
            }
            if (!this.useApplicationTheme) {
                this.postalCodeEdit.setHintTextColor(-3355444);
            }
            this.postalCodeValidator = new MaxLengthValidator(20);
            this.postalCodeEdit.addTextChangedListener(this.postalCodeValidator);
            this.postalCodeEdit.addTextChangedListener(this);
            scrollView.addView(this.postalCodeEdit, -1, -2);
            scrollView.addView(scrollView, layoutParams);
            str = (requireExpiry || requireCVV) ? "4dip" : null;
            ViewUtil.setMargins(scrollView, str, null, null, null);
        } else {
            this.postalCodeValidator = new AlwaysValid();
        }
        scrollView.addView(scrollView, layoutParams);
        addCardholderNameIfNeeded(scrollView);
        scrollView.addView(scrollView, layoutParams);
        ViewUtil.setMargins(scrollView, "16dip", "20dip", "16dip", "20dip");
        LinearLayout buttonLayout = new LinearLayout(this);
        i = this.viewIdCounter;
        this.viewIdCounter = i + 1;
        buttonLayout.setId(i);
        RelativeLayout.LayoutParams buttonLayoutParam = new RelativeLayout.LayoutParams(-1, -2);
        buttonLayoutParam.addRule(12);
        buttonLayout.setPadding(0, paddingPx, 0, 0);
        buttonLayout.setBackgroundColor(0);
        layoutParams.addRule(2, buttonLayout.getId());
        this.doneBtn = new Button(this);
        LinearLayout.LayoutParams doneParam = new LinearLayout.LayoutParams(-1, -2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.doneBtn.setText(LocalizedStrings.getString(StringKey.DONE));
        this.doneBtn.setOnClickListener(new C06731());
        this.doneBtn.setEnabled(false);
        buttonLayout.addView(this.doneBtn, doneParam);
        ViewUtil.styleAsButton(this.doneBtn, true, this, this.useApplicationTheme);
        ViewUtil.setPadding(this.doneBtn, "5dip", null, "5dip", null);
        ViewUtil.setMargins(this.doneBtn, "8dip", "8dip", "8dip", "8dip");
        if (!this.useApplicationTheme) {
            this.doneBtn.setTextSize(16.0f);
        }
        this.cancelBtn = new Button(this);
        LinearLayout.LayoutParams cancelParam = new LinearLayout.LayoutParams(-1, -2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.cancelBtn.setText(LocalizedStrings.getString(StringKey.CANCEL));
        this.cancelBtn.setOnClickListener(new C06742());
        buttonLayout.addView(this.cancelBtn, cancelParam);
        ViewUtil.styleAsButton(this.cancelBtn, false, this, this.useApplicationTheme);
        ViewUtil.setPadding(this.cancelBtn, "5dip", null, "5dip", null);
        ViewUtil.setMargins(this.cancelBtn, "4dip", "8dip", "8dip", "8dip");
        if (!this.useApplicationTheme) {
            this.cancelBtn.setTextSize(16.0f);
        }
        container.addView(buttonLayout, buttonLayoutParam);
        ActivityHelper.addActionBarIfSupported(this);
        setContentView(container);
        Drawable icon = null;
        if (getIntent().getBooleanExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, true)) {
            Drawable bitmapDrawable = new BitmapDrawable(getResources(), ViewUtil.base64ToBitmap("iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDMzRTRFQ0M2MjQxMUUzOURBQ0E3QTY0NjU3OUI5QiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDMzRTRFREM2MjQxMUUzOURBQ0E3QTY0NjU3OUI5QiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkI0MzNFNEVBQzYyNDExRTM5REFDQTdBNjQ2NTc5QjlCIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkI0MzNFNEVCQzYyNDExRTM5REFDQTdBNjQ2NTc5QjlCIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Eyd0MQAABoFJREFUeNrMWl1MU2cY/oqnQKFYyo8tWCmpxuGi2xq4mftp3XZhZO4n3G0mW7KQBRO9WOLPpZoserMbXXSRGC42NQuBLIJb2JJl2VyWwRDGksVB3QQ7UUsrSKlA//a87i3pSHvOJ/WUvcmTtqen33n/vud93y8VyWRSEMbGxsSmTZvEcsE1K757H/cMJnOTKHAf8PNal4APgWZg3ZEjR4SW0D0pfVMo0PpRIBAojMfjjXhbI3ITelYRsJbXegJ4AXgL+MDr9b66d+9ey6Muqqh9WVFRIdxud3lxcbH3MRlQyCjj9TanvvR4PM81NjZafT7ft/39/Xemp6djsotmlT179ohz586V19bWKkJ/aSwtLT3Y3t7eAql+FK9klbq6OqPT6bQbIXkwwGQwGLbime+1tbXt2L9//8MMyCmFwuEw5et6YI3InzyFVNrpcrm+7evrC4RCofiKIwApB+yAUeRXNs7MzHgSiURpTikEsXIElDwb4IzFYk2gSVOuBlAEalfBAKvsc7UMsKxSChHVlkjop34DNjF5YsMqGJBE8YyjiCb+o2xBgRwLEWuC+4lGKYWIywx5NmAOxfNeU1OTGB8fF4uLi4aJiYnk/Py8nAGkPAoYVeG1q6A8yX3oEIQOSjQaFaOjo6bm5uaI3++XMwDWG2C9yWKxlIvVkUlkwQSKKO3Bt9FQOk+cOHF2y5YtU1IGIP0U5J8dBlhXyYBx4A/AAbQCWw8dOvQbXr8B5mU2scLsY1klA26yAXWsB6Xya8CTsixkZB7OdwSSRH7Ar8BdoImjQPq8AjTIGqBwBc73HqD0+Im9Tw50A6l2wsnXxP85hRaALmAG2AGsS/vOwMUtuwGpQoENrGAjk7WVefb+d0A3P/cdoEqLdJYu0HxJnAvmEaBQBVRam8linWQR+B74FIgCNAF6styXOQJoXQXGOLFr1y4qYkYUElsevf8n8AnwJfAG8LpKlNQjUFNTI1BArDy36i0BoA/4HPgFeBF4F3hmeWmi6szInlO0ByKRyBqdZgBqzGLsxQhv1JTyg0yTB4HnM5ALpc4YU6tmJaaiYdNhjCR+p2ZmBPiBc34UqGfF3+SjloIsuU/UOiljQGoK02qhqehMA/3AMIc5yXRnYG8TLS5cuHAhPDAwEEQ7ELDb7XMcDYXz/WX2vksjevQcn6wBMtMQpcBXwEVeXEnj65QBDwhQPtHZ2VnU1tZWBAPI49uBZ4Gd3K6rph7a6TvoRIfKysqC1dXVUim0TsKA28DHwC3gJU67YlY8yRGkzwo8b4Xyjvr6egc7qIRhlkg9aqOHW1pa/Lt37xbHjh2TioBDw4Aoh/Nn9mQbV22Fw53k93SUaITXzYB1hbPFcElJScfw8PCdhoYGoUqjsViMWmmZFKL0uc73bGf606OxC6I2fTEyMvK12WwWlZWVQrWQgUIJa7mEq7HQPVqcmz2zTjWCNnt7d3f3pdbW1oe6ZTqpW/KyzWYTx48fF9u2bbNK5H+QOdmmU79EdeHS6dOnOzs6OsYwDy/N6lkNqKqqMhw+fFiRbKGn2AB7hoZrJQUuysWNKu1fSJvP+vv7L2LzR8LhsEjPEjUaVdKmHy25x0Y8jpablL7BhEAF7irSZvLo0aMP5ubmNH+sZBhirJIRIBp9GpA5CvfxoDLL3iZXLgwODoZ7e3uDvN51bhfomkiljS4GYF6Ymp2dDTocDnthYWGVBpNEQ6FQH/ARN2/zqap95syZh8c3uchyA2wyKXTq1KmZnp6eua6urgqXy6WWQlTU/OfPn7968uRJf1qR+zeMU1M573Zl2SCvFQF6eGRoaCiAwiIQhQ0aNErpgmyYuOnz+aJ6cO3yCNRqsBB5cNLtdodQ3tGalNVoUC7d/zeKUFivgaIgAwuZNRS6vW/fvgdInzLsAa0iFuXNPqOXAeneoyPtzUL9xJrSbJI6QmA9N2tCKwJAKB8GxJklyrmNSGaIFu263/lzvcTMQAbcwqSXlwjQcHKW51FL2oCSkiKuvj8yFcrMDLTGbZPJNK+7AeDpWdBdL14H8NHEyieXpQ+Vxpter3ejx+NxakUAa0WwZuDy5ctJ/Q4j+T8H165dE1ar3FHogQMHvPhNDzCr8t+IBNa8gjXrHpeuqv+VoBMJOtSSEaSElYueKoVizbtYM6HnucySAQaDQSiK3EkKFDNymqkxlg9rXsGakbwYsIIWOJ6BqdLlBh+hLOhpwD8CDABZh9T1S2qGIgAAAABJRU5ErkJggg==", this, 240));
        }
        if (requireExpiry && this.expiryValidator.isValid()) {
            afterTextChanged(this.expiryEdit.getEditableText());
        }
        ActivityHelper.setupActionBarIfSupported(this, this.activityTitleTextView, LocalizedStrings.getString(StringKey.MANUAL_ENTRY_TITLE), "card.io - ", icon);
    }

    private void completed() {
        if (this.capture == null) {
            this.capture = new CreditCard();
        }
        if (this.expiryEdit != null) {
            this.capture.expiryMonth = ((ExpiryValidator) this.expiryValidator).month;
            this.capture.expiryYear = ((ExpiryValidator) this.expiryValidator).year;
        }
        CreditCard result = new CreditCard(this.numberValidator.getValue(), this.capture.expiryMonth, this.capture.expiryYear, this.cvvValidator.getValue(), this.postalCodeValidator.getValue(), this.cardholderNameValidator.getValue());
        Intent dataIntent = new Intent();
        dataIntent.putExtra(CardIOActivity.EXTRA_SCAN_RESULT, result);
        if (getIntent().hasExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE)) {
            dataIntent.putExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE, getIntent().getByteArrayExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE));
        }
        setResult(CardIOActivity.RESULT_CARD_INFO, dataIntent);
        finish();
    }

    public void onBackPressed() {
        setResult(CardIOActivity.RESULT_ENTRY_CANCELED);
        finish();
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        getWindow().setFlags(0, 1024);
        ActivityHelper.setFlagSecure(this);
        validateAndEnableDoneButtonIfValid();
        if (this.numberEdit != null || this.expiryEdit == null || this.expiryValidator.isValid()) {
            advanceToNextEmptyField();
        } else {
            this.expiryEdit.requestFocus();
        }
        if (!(this.numberEdit == null && this.expiryEdit == null && this.cvvEdit == null && this.postalCodeEdit == null && this.cardholderNameEdit == null)) {
            getWindow().setSoftInputMode(5);
        }
        Log.i(TAG, "ready for manual entry");
    }

    private EditText advanceToNextEmptyField() {
        int viewId = 100;
        while (true) {
            int viewId2 = viewId + 1;
            EditText et = (EditText) findViewById(viewId);
            if (et == null) {
                return null;
            }
            if (et.getText().length() == 0 && et.requestFocus()) {
                return et;
            }
            viewId = viewId2;
        }
    }

    private void validateAndEnableDoneButtonIfValid() {
        Button button = this.doneBtn;
        boolean z = this.numberValidator.isValid() && this.expiryValidator.isValid() && this.cvvValidator.isValid() && this.postalCodeValidator.isValid() && this.cardholderNameValidator.isValid();
        button.setEnabled(z);
        Log.d(TAG, "setting doneBtn.enabled=" + this.doneBtn.isEnabled());
        if (this.autoAcceptDone && this.numberValidator.isValid() && this.expiryValidator.isValid() && this.cvvValidator.isValid() && this.postalCodeValidator.isValid() && this.cardholderNameValidator.isValid()) {
            completed();
        }
    }

    public void afterTextChanged(Editable et) {
        if (this.numberEdit != null && et == this.numberEdit.getText()) {
            if (!this.numberValidator.hasFullLength()) {
                setDefaultColor(this.numberEdit);
            } else if (this.numberValidator.isValid()) {
                setDefaultColor(this.numberEdit);
                advanceToNextEmptyField();
            } else {
                this.numberEdit.setTextColor(Appearance.TEXT_COLOR_ERROR);
            }
            if (this.cvvEdit != null) {
                CharSequence charSequence;
                FixedLengthValidator v = this.cvvValidator;
                int length = CardType.fromCardNumber(this.numberValidator.getValue().toString()).cvvLength();
                v.requiredLength = length;
                EditText editText = this.cvvEdit;
                if (length == 4) {
                    charSequence = "1234";
                } else {
                    charSequence = "123";
                }
                editText.setHint(charSequence);
            }
        } else if (this.expiryEdit == null || et != this.expiryEdit.getText()) {
            if (this.cvvEdit == null || et != this.cvvEdit.getText()) {
                if (this.postalCodeEdit == null || et != this.postalCodeEdit.getText()) {
                    if (this.cardholderNameEdit != null && et == this.cardholderNameEdit.getText()) {
                        if (!this.cardholderNameValidator.hasFullLength()) {
                            setDefaultColor(this.cardholderNameEdit);
                        } else if (this.cardholderNameValidator.isValid()) {
                            setDefaultColor(this.cardholderNameEdit);
                        } else {
                            this.cardholderNameEdit.setTextColor(Appearance.TEXT_COLOR_ERROR);
                        }
                    }
                } else if (!this.postalCodeValidator.hasFullLength()) {
                    setDefaultColor(this.postalCodeEdit);
                } else if (this.postalCodeValidator.isValid()) {
                    setDefaultColor(this.postalCodeEdit);
                } else {
                    this.postalCodeEdit.setTextColor(Appearance.TEXT_COLOR_ERROR);
                }
            } else if (!this.cvvValidator.hasFullLength()) {
                setDefaultColor(this.cvvEdit);
            } else if (this.cvvValidator.isValid()) {
                setDefaultColor(this.cvvEdit);
                advanceToNextEmptyField();
            } else {
                this.cvvEdit.setTextColor(Appearance.TEXT_COLOR_ERROR);
            }
        } else if (!this.expiryValidator.hasFullLength()) {
            setDefaultColor(this.expiryEdit);
        } else if (this.expiryValidator.isValid()) {
            setDefaultColor(this.expiryEdit);
            advanceToNextEmptyField();
        } else {
            this.expiryEdit.setTextColor(Appearance.TEXT_COLOR_ERROR);
        }
        validateAndEnableDoneButtonIfValid();
    }

    private void setDefaultColor(EditText editText) {
        if (this.useApplicationTheme) {
            editText.setTextColor(this.defaultTextColor);
        } else {
            editText.setTextColor(-12303292);
        }
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    private void addCardholderNameIfNeeded(ViewGroup mainLayout) {
        if (getIntent().getBooleanExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false)) {
            LinearLayout cardholderNameLayout = new LinearLayout(this);
            ViewUtil.setPadding(cardholderNameLayout, null, "4dip", null, null);
            cardholderNameLayout.setOrientation(1);
            TextView cardholderNameLabel = new TextView(this);
            if (!this.useApplicationTheme) {
                cardholderNameLabel.setTextColor(Appearance.TEXT_COLOR_LABEL);
            }
            ViewUtil.setPadding(cardholderNameLabel, this.labelLeftPadding, null, null, null);
            cardholderNameLabel.setText(LocalizedStrings.getString(StringKey.ENTRY_CARDHOLDER_NAME));
            cardholderNameLayout.addView(cardholderNameLabel, -2, -2);
            this.cardholderNameEdit = new EditText(this);
            EditText editText = this.cardholderNameEdit;
            int i = this.editTextIdCounter;
            this.editTextIdCounter = i + 1;
            editText.setId(i);
            this.cardholderNameEdit.setMaxLines(1);
            this.cardholderNameEdit.setImeOptions(6);
            this.cardholderNameEdit.setTextAppearance(getApplicationContext(), 16842816);
            this.cardholderNameEdit.setInputType(1);
            if (!this.useApplicationTheme) {
                this.cardholderNameEdit.setHintTextColor(-3355444);
            }
            this.cardholderNameValidator = new MaxLengthValidator(175);
            this.cardholderNameEdit.addTextChangedListener(this.cardholderNameValidator);
            this.cardholderNameEdit.addTextChangedListener(this);
            cardholderNameLayout.addView(this.cardholderNameEdit, -1, -2);
            mainLayout.addView(cardholderNameLayout, -1, -2);
            return;
        }
        this.cardholderNameValidator = new AlwaysValid();
    }
}
