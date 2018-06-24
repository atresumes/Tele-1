package de.keyboardsurfer.android.widget.crouton;

import android.graphics.drawable.Drawable;
import android.widget.ImageView.ScaleType;

public class Style {
    public static final Style ALERT = new Builder().setBackgroundColorValue(holoRedLight).build();
    public static final Style CONFIRM = new Builder().setBackgroundColorValue(holoGreenLight).build();
    public static final Style INFO = new Builder().setBackgroundColorValue(holoBlueLight).build();
    public static final int NOT_SET = -1;
    public static final int holoBlueLight = -13388315;
    public static final int holoGreenLight = -6697984;
    public static final int holoRedLight = -48060;
    final int backgroundColorResourceId;
    final int backgroundColorValue;
    final int backgroundDrawableResourceId;
    final Configuration configuration;
    final String fontName;
    final int fontNameResId;
    final int gravity;
    final int heightDimensionResId;
    final int heightInPixels;
    final Drawable imageDrawable;
    final int imageResId;
    final ScaleType imageScaleType;
    final boolean isTileEnabled;
    final int paddingDimensionResId;
    final int paddingInPixels;
    final int textAppearanceResId;
    final int textColorResourceId;
    final int textColorValue;
    final int textShadowColorResId;
    final float textShadowDx;
    final float textShadowDy;
    final float textShadowRadius;
    final int textSize;
    final int widthDimensionResId;
    final int widthInPixels;

    public static class Builder {
        private int backgroundColorResourceId;
        private int backgroundColorValue;
        private int backgroundDrawableResourceId;
        private Configuration configuration;
        private String fontName;
        private int fontNameResId;
        private int gravity;
        private int heightDimensionResId;
        private int heightInPixels;
        private Drawable imageDrawable;
        private int imageResId;
        private ScaleType imageScaleType;
        private boolean isTileEnabled;
        private int paddingDimensionResId;
        private int paddingInPixels;
        private int textAppearanceResId;
        private int textColorResourceId;
        private int textColorValue;
        private int textShadowColorResId;
        private float textShadowDx;
        private float textShadowDy;
        private float textShadowRadius;
        private int textSize;
        private int widthDimensionResId;
        private int widthInPixels;

        public Builder() {
            this.configuration = Configuration.DEFAULT;
            this.paddingInPixels = 10;
            this.backgroundColorResourceId = 17170450;
            this.backgroundDrawableResourceId = 0;
            this.backgroundColorValue = -1;
            this.isTileEnabled = false;
            this.textColorResourceId = 17170443;
            this.textColorValue = -1;
            this.heightInPixels = -2;
            this.widthInPixels = -1;
            this.gravity = 17;
            this.imageDrawable = null;
            this.imageResId = 0;
            this.imageScaleType = ScaleType.FIT_XY;
            this.fontName = null;
            this.fontNameResId = 0;
        }

        public Builder(Style baseStyle) {
            this.configuration = baseStyle.configuration;
            this.backgroundColorValue = baseStyle.backgroundColorValue;
            this.backgroundColorResourceId = baseStyle.backgroundColorResourceId;
            this.backgroundDrawableResourceId = baseStyle.backgroundDrawableResourceId;
            this.isTileEnabled = baseStyle.isTileEnabled;
            this.textColorResourceId = baseStyle.textColorResourceId;
            this.textColorValue = baseStyle.textColorValue;
            this.heightInPixels = baseStyle.heightInPixels;
            this.heightDimensionResId = baseStyle.heightDimensionResId;
            this.widthInPixels = baseStyle.widthInPixels;
            this.widthDimensionResId = baseStyle.widthDimensionResId;
            this.gravity = baseStyle.gravity;
            this.imageDrawable = baseStyle.imageDrawable;
            this.textSize = baseStyle.textSize;
            this.textShadowColorResId = baseStyle.textShadowColorResId;
            this.textShadowRadius = baseStyle.textShadowRadius;
            this.textShadowDx = baseStyle.textShadowDx;
            this.textShadowDy = baseStyle.textShadowDy;
            this.textAppearanceResId = baseStyle.textAppearanceResId;
            this.imageResId = baseStyle.imageResId;
            this.imageScaleType = baseStyle.imageScaleType;
            this.paddingInPixels = baseStyle.paddingInPixels;
            this.paddingDimensionResId = baseStyle.paddingDimensionResId;
            this.fontName = baseStyle.fontName;
            this.fontNameResId = baseStyle.fontNameResId;
        }

        public Builder setConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColorResourceId) {
            this.backgroundColorResourceId = backgroundColorResourceId;
            return this;
        }

        public Builder setBackgroundColorValue(int backgroundColorValue) {
            this.backgroundColorValue = backgroundColorValue;
            return this;
        }

        public Builder setBackgroundDrawable(int backgroundDrawableResourceId) {
            this.backgroundDrawableResourceId = backgroundDrawableResourceId;
            return this;
        }

        public Builder setHeight(int height) {
            this.heightInPixels = height;
            return this;
        }

        public Builder setHeightDimensionResId(int heightDimensionResId) {
            this.heightDimensionResId = heightDimensionResId;
            return this;
        }

        public Builder setWidth(int width) {
            this.widthInPixels = width;
            return this;
        }

        public Builder setWidthDimensionResId(int widthDimensionResId) {
            this.widthDimensionResId = widthDimensionResId;
            return this;
        }

        public Builder setTileEnabled(boolean isTileEnabled) {
            this.isTileEnabled = isTileEnabled;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColorResourceId = textColor;
            return this;
        }

        public Builder setTextColorValue(int textColorValue) {
            this.textColorValue = textColorValue;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setImageDrawable(Drawable imageDrawable) {
            this.imageDrawable = imageDrawable;
            return this;
        }

        public Builder setImageResource(int imageResId) {
            this.imageResId = imageResId;
            return this;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setTextShadowColor(int textShadowColorResId) {
            this.textShadowColorResId = textShadowColorResId;
            return this;
        }

        public Builder setTextShadowRadius(float textShadowRadius) {
            this.textShadowRadius = textShadowRadius;
            return this;
        }

        public Builder setTextShadowDx(float textShadowDx) {
            this.textShadowDx = textShadowDx;
            return this;
        }

        public Builder setTextShadowDy(float textShadowDy) {
            this.textShadowDy = textShadowDy;
            return this;
        }

        public Builder setTextAppearance(int textAppearanceResId) {
            this.textAppearanceResId = textAppearanceResId;
            return this;
        }

        public Builder setImageScaleType(ScaleType imageScaleType) {
            this.imageScaleType = imageScaleType;
            return this;
        }

        public Builder setPaddingInPixels(int padding) {
            this.paddingInPixels = padding;
            return this;
        }

        public Builder setPaddingDimensionResId(int paddingResId) {
            this.paddingDimensionResId = paddingResId;
            return this;
        }

        public Builder setFontName(String fontName) {
            this.fontName = fontName;
            return this;
        }

        public Builder setFontNameResId(int fontNameResId) {
            this.fontNameResId = fontNameResId;
            return this;
        }

        public Style build() {
            return new Style();
        }
    }

    private Style(Builder builder) {
        this.configuration = builder.configuration;
        this.backgroundColorResourceId = builder.backgroundColorResourceId;
        this.backgroundDrawableResourceId = builder.backgroundDrawableResourceId;
        this.isTileEnabled = builder.isTileEnabled;
        this.textColorResourceId = builder.textColorResourceId;
        this.textColorValue = builder.textColorValue;
        this.heightInPixels = builder.heightInPixels;
        this.heightDimensionResId = builder.heightDimensionResId;
        this.widthInPixels = builder.widthInPixels;
        this.widthDimensionResId = builder.widthDimensionResId;
        this.gravity = builder.gravity;
        this.imageDrawable = builder.imageDrawable;
        this.textSize = builder.textSize;
        this.textShadowColorResId = builder.textShadowColorResId;
        this.textShadowRadius = builder.textShadowRadius;
        this.textShadowDx = builder.textShadowDx;
        this.textShadowDy = builder.textShadowDy;
        this.textAppearanceResId = builder.textAppearanceResId;
        this.imageResId = builder.imageResId;
        this.imageScaleType = builder.imageScaleType;
        this.paddingInPixels = builder.paddingInPixels;
        this.paddingDimensionResId = builder.paddingDimensionResId;
        this.backgroundColorValue = builder.backgroundColorValue;
        this.fontName = builder.fontName;
        this.fontNameResId = builder.fontNameResId;
    }

    public String toString() {
        return "Style{configuration=" + this.configuration + ", backgroundColorResourceId=" + this.backgroundColorResourceId + ", backgroundDrawableResourceId=" + this.backgroundDrawableResourceId + ", backgroundColorValue=" + this.backgroundColorValue + ", isTileEnabled=" + this.isTileEnabled + ", textColorResourceId=" + this.textColorResourceId + ", textColorValue=" + this.textColorValue + ", heightInPixels=" + this.heightInPixels + ", heightDimensionResId=" + this.heightDimensionResId + ", widthInPixels=" + this.widthInPixels + ", widthDimensionResId=" + this.widthDimensionResId + ", gravity=" + this.gravity + ", imageDrawable=" + this.imageDrawable + ", imageResId=" + this.imageResId + ", imageScaleType=" + this.imageScaleType + ", textSize=" + this.textSize + ", textShadowColorResId=" + this.textShadowColorResId + ", textShadowRadius=" + this.textShadowRadius + ", textShadowDy=" + this.textShadowDy + ", textShadowDx=" + this.textShadowDx + ", textAppearanceResId=" + this.textAppearanceResId + ", paddingInPixels=" + this.paddingInPixels + ", paddingDimensionResId=" + this.paddingDimensionResId + ", fontName=" + this.fontName + ", fontNameResId=" + this.fontNameResId + '}';
    }
}
