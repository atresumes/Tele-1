package de.keyboardsurfer.android.widget.crouton;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.keyboardsurfer.android.widget.crouton.Style.Builder;

public final class Crouton {
    private static final int IMAGE_ID = 256;
    private static final String NULL_PARAMETERS_ARE_NOT_ACCEPTED = "Null parameters are not accepted";
    private static final int TEXT_ID = 257;
    private Activity activity;
    private Configuration configuration;
    private FrameLayout croutonView;
    private final View customView;
    private Animation inAnimation;
    private LifecycleCallback lifecycleCallback;
    private OnClickListener onClickListener;
    private Animation outAnimation;
    private final Style style;
    private final CharSequence text;
    private ViewGroup viewGroup;

    private Crouton(Activity activity, CharSequence text, Style style) {
        this.configuration = null;
        this.lifecycleCallback = null;
        if (activity == null || text == null || style == null) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.viewGroup = null;
        this.text = text;
        this.style = style;
        this.customView = null;
    }

    private Crouton(Activity activity, CharSequence text, Style style, ViewGroup viewGroup) {
        this.configuration = null;
        this.lifecycleCallback = null;
        if (activity == null || text == null || style == null) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.text = text;
        this.style = style;
        this.viewGroup = viewGroup;
        this.customView = null;
    }

    private Crouton(Activity activity, View customView) {
        this.configuration = null;
        this.lifecycleCallback = null;
        if (activity == null || customView == null) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.viewGroup = null;
        this.customView = customView;
        this.style = new Builder().build();
        this.text = null;
    }

    private Crouton(Activity activity, View customView, ViewGroup viewGroup) {
        this(activity, customView, viewGroup, Configuration.DEFAULT);
    }

    private Crouton(Activity activity, View customView, ViewGroup viewGroup, Configuration configuration) {
        this.configuration = null;
        this.lifecycleCallback = null;
        if (activity == null || customView == null) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.customView = customView;
        this.viewGroup = viewGroup;
        this.style = new Builder().build();
        this.text = null;
        this.configuration = configuration;
    }

    public static Crouton makeText(Activity activity, CharSequence text, Style style) {
        return new Crouton(activity, text, style);
    }

    public static Crouton makeText(Activity activity, CharSequence text, Style style, ViewGroup viewGroup) {
        return new Crouton(activity, text, style, viewGroup);
    }

    public static Crouton makeText(Activity activity, CharSequence text, Style style, int viewGroupResId) {
        return new Crouton(activity, text, style, (ViewGroup) activity.findViewById(viewGroupResId));
    }

    public static Crouton makeText(Activity activity, int textResourceId, Style style) {
        return makeText(activity, activity.getString(textResourceId), style);
    }

    public static Crouton makeText(Activity activity, int textResourceId, Style style, ViewGroup viewGroup) {
        return makeText(activity, activity.getString(textResourceId), style, viewGroup);
    }

    public static Crouton makeText(Activity activity, int textResourceId, Style style, int viewGroupResId) {
        return makeText(activity, activity.getString(textResourceId), style, (ViewGroup) activity.findViewById(viewGroupResId));
    }

    public static Crouton make(Activity activity, View customView) {
        return new Crouton(activity, customView);
    }

    public static Crouton make(Activity activity, View customView, ViewGroup viewGroup) {
        return new Crouton(activity, customView, viewGroup);
    }

    public static Crouton make(Activity activity, View customView, int viewGroupResId) {
        return new Crouton(activity, customView, (ViewGroup) activity.findViewById(viewGroupResId));
    }

    public static Crouton make(Activity activity, View customView, int viewGroupResId, Configuration configuration) {
        return new Crouton(activity, customView, (ViewGroup) activity.findViewById(viewGroupResId), configuration);
    }

    public static void showText(Activity activity, CharSequence text, Style style) {
        makeText(activity, text, style).show();
    }

    public static void showText(Activity activity, CharSequence text, Style style, ViewGroup viewGroup) {
        makeText(activity, text, style, viewGroup).show();
    }

    public static void showText(Activity activity, CharSequence text, Style style, int viewGroupResId) {
        makeText(activity, text, style, (ViewGroup) activity.findViewById(viewGroupResId)).show();
    }

    public static void showText(Activity activity, CharSequence text, Style style, int viewGroupResId, Configuration configuration) {
        makeText(activity, text, style, (ViewGroup) activity.findViewById(viewGroupResId)).setConfiguration(configuration).show();
    }

    public static void show(Activity activity, View customView) {
        make(activity, customView).show();
    }

    public static void show(Activity activity, View customView, ViewGroup viewGroup) {
        make(activity, customView, viewGroup).show();
    }

    public static void show(Activity activity, View customView, int viewGroupResId) {
        make(activity, customView, viewGroupResId).show();
    }

    public static void showText(Activity activity, int textResourceId, Style style) {
        showText(activity, activity.getString(textResourceId), style);
    }

    public static void showText(Activity activity, int textResourceId, Style style, ViewGroup viewGroup) {
        showText(activity, activity.getString(textResourceId), style, viewGroup);
    }

    public static void showText(Activity activity, int textResourceId, Style style, int viewGroupResId) {
        showText(activity, activity.getString(textResourceId), style, viewGroupResId);
    }

    public static void hide(Crouton crouton) {
        crouton.hide();
    }

    public static void cancelAllCroutons() {
        Manager.getInstance().clearCroutonQueue();
    }

    public static void clearCroutonsForActivity(Activity activity) {
        Manager.getInstance().clearCroutonsForActivity(activity);
    }

    public void cancel() {
        Manager.getInstance().removeCroutonImmediately(this);
    }

    public void show() {
        Manager.getInstance().add(this);
    }

    public Animation getInAnimation() {
        if (this.inAnimation == null && this.activity != null) {
            if (getConfiguration().inAnimationResId > 0) {
                this.inAnimation = AnimationUtils.loadAnimation(getActivity(), getConfiguration().inAnimationResId);
            } else {
                measureCroutonView();
                this.inAnimation = DefaultAnimationsBuilder.buildDefaultSlideInDownAnimation(getView());
            }
        }
        return this.inAnimation;
    }

    public Animation getOutAnimation() {
        if (this.outAnimation == null && this.activity != null) {
            if (getConfiguration().outAnimationResId > 0) {
                this.outAnimation = AnimationUtils.loadAnimation(getActivity(), getConfiguration().outAnimationResId);
            } else {
                this.outAnimation = DefaultAnimationsBuilder.buildDefaultSlideOutUpAnimation(getView());
            }
        }
        return this.outAnimation;
    }

    public void setLifecycleCallback(LifecycleCallback lifecycleCallback) {
        this.lifecycleCallback = lifecycleCallback;
    }

    public void hide() {
        Manager.getInstance().removeCrouton(this);
    }

    public Crouton setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public Crouton setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public String toString() {
        return "Crouton{text=" + this.text + ", style=" + this.style + ", configuration=" + this.configuration + ", customView=" + this.customView + ", onClickListener=" + this.onClickListener + ", activity=" + this.activity + ", viewGroup=" + this.viewGroup + ", croutonView=" + this.croutonView + ", inAnimation=" + this.inAnimation + ", outAnimation=" + this.outAnimation + ", lifecycleCallback=" + this.lifecycleCallback + '}';
    }

    public static String getLicenseText() {
        return "This application uses the Crouton library.\n\nCopyright 2012 - 2013 Benjamin Weiss \n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\n   http://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.";
    }

    boolean isShowing() {
        return this.activity != null && (isCroutonViewNotNull() || isCustomViewNotNull());
    }

    private boolean isCroutonViewNotNull() {
        return (this.croutonView == null || this.croutonView.getParent() == null) ? false : true;
    }

    private boolean isCustomViewNotNull() {
        return (this.customView == null || this.customView.getParent() == null) ? false : true;
    }

    void detachActivity() {
        this.activity = null;
    }

    void detachViewGroup() {
        this.viewGroup = null;
    }

    void detachLifecycleCallback() {
        this.lifecycleCallback = null;
    }

    LifecycleCallback getLifecycleCallback() {
        return this.lifecycleCallback;
    }

    Style getStyle() {
        return this.style;
    }

    Configuration getConfiguration() {
        if (this.configuration == null) {
            this.configuration = getStyle().configuration;
        }
        return this.configuration;
    }

    Activity getActivity() {
        return this.activity;
    }

    ViewGroup getViewGroup() {
        return this.viewGroup;
    }

    CharSequence getText() {
        return this.text;
    }

    View getView() {
        if (this.customView != null) {
            return this.customView;
        }
        if (this.croutonView == null) {
            initializeCroutonView();
        }
        return this.croutonView;
    }

    private void measureCroutonView() {
        int widthSpec;
        View view = getView();
        if (this.viewGroup != null) {
            widthSpec = MeasureSpec.makeMeasureSpec(this.viewGroup.getMeasuredWidth(), Integer.MIN_VALUE);
        } else {
            widthSpec = MeasureSpec.makeMeasureSpec(this.activity.getWindow().getDecorView().getMeasuredWidth(), Integer.MIN_VALUE);
        }
        view.measure(widthSpec, MeasureSpec.makeMeasureSpec(0, 0));
    }

    private void initializeCroutonView() {
        Resources resources = this.activity.getResources();
        this.croutonView = initializeCroutonViewGroup(resources);
        this.croutonView.addView(initializeContentView(resources));
    }

    private FrameLayout initializeCroutonViewGroup(Resources resources) {
        int height;
        int width;
        FrameLayout croutonView = new FrameLayout(this.activity);
        if (this.onClickListener != null) {
            croutonView.setOnClickListener(this.onClickListener);
        }
        if (this.style.heightDimensionResId > 0) {
            height = resources.getDimensionPixelSize(this.style.heightDimensionResId);
        } else {
            height = this.style.heightInPixels;
        }
        if (this.style.widthDimensionResId > 0) {
            width = resources.getDimensionPixelSize(this.style.widthDimensionResId);
        } else {
            width = this.style.widthInPixels;
        }
        if (width == 0) {
            width = -1;
        }
        croutonView.setLayoutParams(new LayoutParams(width, height));
        if (this.style.backgroundColorValue != -1) {
            croutonView.setBackgroundColor(this.style.backgroundColorValue);
        } else {
            croutonView.setBackgroundColor(resources.getColor(this.style.backgroundColorResourceId));
        }
        if (this.style.backgroundDrawableResourceId != 0) {
            BitmapDrawable drawable = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, this.style.backgroundDrawableResourceId));
            if (this.style.isTileEnabled) {
                drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            }
            croutonView.setBackgroundDrawable(drawable);
        }
        return croutonView;
    }

    private RelativeLayout initializeContentView(Resources resources) {
        RelativeLayout contentView = new RelativeLayout(this.activity);
        contentView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        int padding = this.style.paddingInPixels;
        if (this.style.paddingDimensionResId > 0) {
            padding = resources.getDimensionPixelSize(this.style.paddingDimensionResId);
        }
        contentView.setPadding(padding, padding, padding, padding);
        ImageView image = null;
        if (!(this.style.imageDrawable == null && this.style.imageResId == 0)) {
            image = initializeImageView();
            contentView.addView(image, image.getLayoutParams());
        }
        TextView text = initializeTextView(resources);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(-1, -2);
        if (image != null) {
            textParams.addRule(1, image.getId());
        }
        if ((this.style.gravity & 17) != 0) {
            textParams.addRule(13);
        } else if ((this.style.gravity & 16) != 0) {
            textParams.addRule(15);
        } else if ((this.style.gravity & 1) != 0) {
            textParams.addRule(14);
        }
        contentView.addView(text, textParams);
        return contentView;
    }

    private TextView initializeTextView(Resources resources) {
        TextView text = new TextView(this.activity);
        text.setId(257);
        if (this.style.fontName != null) {
            setTextWithCustomFont(text, this.style.fontName);
        } else if (this.style.fontNameResId != 0) {
            setTextWithCustomFont(text, resources.getString(this.style.fontNameResId));
        } else {
            text.setText(this.text);
        }
        text.setTypeface(Typeface.DEFAULT_BOLD);
        text.setGravity(this.style.gravity);
        if (this.style.textColorValue != -1) {
            text.setTextColor(this.style.textColorValue);
        } else if (this.style.textColorResourceId != 0) {
            text.setTextColor(resources.getColor(this.style.textColorResourceId));
        }
        if (this.style.textSize != 0) {
            text.setTextSize(2, (float) this.style.textSize);
        }
        if (this.style.textShadowColorResId != 0) {
            initializeTextViewShadow(resources, text);
        }
        if (this.style.textAppearanceResId != 0) {
            text.setTextAppearance(this.activity, this.style.textAppearanceResId);
        }
        return text;
    }

    private void setTextWithCustomFont(TextView text, String fontName) {
        if (this.text != null) {
            SpannableString s = new SpannableString(this.text);
            s.setSpan(new TypefaceSpan(text.getContext(), fontName), 0, s.length(), 33);
            text.setText(s);
        }
    }

    private void initializeTextViewShadow(Resources resources, TextView text) {
        text.setShadowLayer(this.style.textShadowRadius, this.style.textShadowDx, this.style.textShadowDy, resources.getColor(this.style.textShadowColorResId));
    }

    private ImageView initializeImageView() {
        ImageView image = new ImageView(this.activity);
        image.setId(256);
        image.setAdjustViewBounds(true);
        image.setScaleType(this.style.imageScaleType);
        if (this.style.imageDrawable != null) {
            image.setImageDrawable(this.style.imageDrawable);
        }
        if (this.style.imageResId != 0) {
            image.setImageResource(this.style.imageResId);
        }
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(-2, -2);
        imageParams.addRule(9, -1);
        imageParams.addRule(15, -1);
        image.setLayoutParams(imageParams);
        return image;
    }
}
