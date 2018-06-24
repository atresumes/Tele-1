package de.keyboardsurfer.android.widget.crouton;

public class Configuration {
    public static final Configuration DEFAULT = new Builder().setDuration(3000).build();
    public static final int DURATION_INFINITE = -1;
    public static final int DURATION_LONG = 5000;
    public static final int DURATION_SHORT = 3000;
    final int durationInMilliseconds;
    final int inAnimationResId;
    final int outAnimationResId;

    public static class Builder {
        private int durationInMilliseconds = 3000;
        private int inAnimationResId = 0;
        private int outAnimationResId = 0;

        public Builder setDuration(int duration) {
            this.durationInMilliseconds = duration;
            return this;
        }

        public Builder setInAnimation(int inAnimationResId) {
            this.inAnimationResId = inAnimationResId;
            return this;
        }

        public Builder setOutAnimation(int outAnimationResId) {
            this.outAnimationResId = outAnimationResId;
            return this;
        }

        public Configuration build() {
            return new Configuration();
        }
    }

    private Configuration(Builder builder) {
        this.durationInMilliseconds = builder.durationInMilliseconds;
        this.inAnimationResId = builder.inAnimationResId;
        this.outAnimationResId = builder.outAnimationResId;
    }

    public String toString() {
        return "Configuration{durationInMilliseconds=" + this.durationInMilliseconds + ", inAnimationResId=" + this.inAnimationResId + ", outAnimationResId=" + this.outAnimationResId + '}';
    }
}
