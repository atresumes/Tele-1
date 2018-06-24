package com.sinch.android.rtc.internal.client.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.View;
import com.android.volley.DefaultRetryPolicy;
import com.sinch.android.rtc.internal.client.DefaultSinchClient;
import com.sinch.android.rtc.video.VideoFrameListener;
import com.sinch.android.rtc.video.VideoScalingType;
import org.webrtc.sinch.RendererCommon.ScalingType;
import org.webrtc.sinch.VideoCapturerAndroid;
import org.webrtc.sinch.VideoRendererGui;

public class DefaultVideoController implements VideoControllerInternal, VideoTrackListenerInternal {
    private static final String TAG = DefaultVideoController.class.getSimpleName();
    private int mCameraDevicePosition;
    private final Context mContext;
    private PreviewContainer mPreviewContainer;
    private ScalingType mScalingBehaviour = ScalingType.SCALE_ASPECT_FIT;
    private final DefaultSinchClient mSinchClient;
    private GLSurfaceView mSurfaceView;
    private VideoFrameSink mVideoFrameSink;

    @TargetApi(11)
    public DefaultVideoController(Context context, DefaultSinchClient defaultSinchClient, VideoFrameSink videoFrameSink) {
        this.mContext = context;
        this.mSinchClient = defaultSinchClient;
        this.mPreviewContainer = new PreviewContainer(context);
        this.mVideoFrameSink = videoFrameSink;
        VideoCapturerAndroid.setLocalPreview(this.mPreviewContainer.getPreviewHolder());
        this.mCameraDevicePosition = 1;
    }

    private void throwIfDisposed() {
        if (this.mSinchClient.isDisposed()) {
            throw new IllegalStateException("SinchClient is stopped, further calls will throw Exceptions.");
        }
    }

    public void dispose() {
        this.mSurfaceView = null;
        if (this.mVideoFrameSink != null) {
            this.mVideoFrameSink.dispose();
        }
    }

    public int getCaptureDevicePosition() {
        return this.mCameraDevicePosition;
    }

    public View getLocalView() {
        throwIfDisposed();
        return this.mPreviewContainer;
    }

    public View getRemoteView() {
        throwIfDisposed();
        if (this.mSurfaceView == null) {
            this.mSurfaceView = new GLSurfaceView(this.mContext);
            VideoRendererGui.setView(this.mSurfaceView, null);
            try {
                this.mVideoFrameSink.setVideoRendererCallbacks(VideoRendererGui.createGuiRenderer(0, 0, 100, 100, this.mScalingBehaviour, false));
            } catch (Throwable e) {
                Log.e(TAG, "Failed to create VideoRenderer:", e);
            }
        }
        return this.mSurfaceView;
    }

    public long getVideoFrameSink() {
        return this.mVideoFrameSink.nativeRenderer();
    }

    public void onVideoTrackAddedInternal(String str) {
        this.mVideoFrameSink.setSessionId(str);
    }

    public void setBorderColor(float f, float f2, float f3) {
        if (this.mSinchClient.isStarted()) {
            throw new IllegalStateException("SinchClient already started, this needs to be set before starting");
        }
        throwIfDisposed();
        if (f > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT || f2 > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT || f3 > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT || f < 0.0f || f2 < 0.0f || f3 < 0.0f) {
            throw new IllegalArgumentException("r, g and b values needs to be between 0.0f and 1.0f");
        }
        VideoRendererGui.setClearColor(f, f2, f3);
    }

    public void setCaptureDevicePosition(int i) {
        if (i == 1 || i == 0) {
            Object nativeVideoCapturer = this.mSinchClient.getNativeVideoCapturer();
            if (nativeVideoCapturer instanceof VideoCapturerAndroid) {
                ((VideoCapturerAndroid) nativeVideoCapturer).switchToCameraPosition(i);
                this.mCameraDevicePosition = i;
                return;
            }
            Log.e(TAG, "Native capturer is not a VideoCapturerAndroid instance.");
            return;
        }
        throw new IllegalArgumentException("Invalid capture device position.");
    }

    public void setResizeBehaviour(VideoScalingType videoScalingType) {
        if (this.mSinchClient.isStarted()) {
            throw new IllegalStateException("SinchClient already started, this needs to be set before starting.");
        }
        throwIfDisposed();
        if (videoScalingType == VideoScalingType.ASPECT_FIT) {
            this.mScalingBehaviour = ScalingType.SCALE_ASPECT_FIT;
        } else if (videoScalingType == VideoScalingType.ASPECT_FILL) {
            this.mScalingBehaviour = ScalingType.SCALE_ASPECT_FILL;
        } else if (videoScalingType == VideoScalingType.ASPECT_BALANCED) {
            this.mScalingBehaviour = ScalingType.SCALE_ASPECT_BALANCED;
        } else {
            throw new IllegalArgumentException("Invalid scaling type.");
        }
    }

    public void setVideoFrameListener(VideoFrameListener videoFrameListener) {
        this.mVideoFrameSink.setVideoFrameListener(videoFrameListener);
    }

    public void toggleCaptureDevicePosition() {
        if (this.mCameraDevicePosition == 1) {
            setCaptureDevicePosition(0);
        } else if (this.mCameraDevicePosition == 0) {
            setCaptureDevicePosition(1);
        }
    }
}
