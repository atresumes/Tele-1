package android.support.v4.media;

import android.annotation.TargetApi;
import android.media.MediaDescription;
import android.net.Uri;
import android.support.annotation.RequiresApi;

@TargetApi(23)
@RequiresApi(23)
class MediaDescriptionCompatApi23 extends MediaDescriptionCompatApi21 {

    static class Builder extends Builder {
        Builder() {
        }

        public static void setMediaUri(Object builderObj, Uri mediaUri) {
            ((android.media.MediaDescription.Builder) builderObj).setMediaUri(mediaUri);
        }
    }

    MediaDescriptionCompatApi23() {
    }

    public static Uri getMediaUri(Object descriptionObj) {
        return ((MediaDescription) descriptionObj).getMediaUri();
    }
}
