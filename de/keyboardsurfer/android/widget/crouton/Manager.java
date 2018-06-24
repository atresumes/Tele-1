package de.keyboardsurfer.android.widget.crouton;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

final class Manager extends Handler {
    private static Manager INSTANCE;
    private final Queue<Crouton> croutonQueue = new LinkedBlockingQueue();

    private static final class Messages {
        public static final int ADD_CROUTON_TO_VIEW = -1040157475;
        public static final int DISPLAY_CROUTON = 794631;
        public static final int REMOVE_CROUTON = -1040155167;

        private Messages() {
        }
    }

    private Manager() {
    }

    static synchronized Manager getInstance() {
        Manager manager;
        synchronized (Manager.class) {
            if (INSTANCE == null) {
                INSTANCE = new Manager();
            }
            manager = INSTANCE;
        }
        return manager;
    }

    void add(Crouton crouton) {
        this.croutonQueue.add(crouton);
        displayCrouton();
    }

    private void displayCrouton() {
        if (!this.croutonQueue.isEmpty()) {
            Crouton currentCrouton = (Crouton) this.croutonQueue.peek();
            if (currentCrouton.getActivity() == null) {
                this.croutonQueue.poll();
            }
            if (currentCrouton.isShowing()) {
                sendMessageDelayed(currentCrouton, Messages.DISPLAY_CROUTON, calculateCroutonDuration(currentCrouton));
                return;
            }
            sendMessage(currentCrouton, Messages.ADD_CROUTON_TO_VIEW);
            if (currentCrouton.getLifecycleCallback() != null) {
                currentCrouton.getLifecycleCallback().onDisplayed();
            }
        }
    }

    private long calculateCroutonDuration(Crouton crouton) {
        return (((long) crouton.getConfiguration().durationInMilliseconds) + crouton.getInAnimation().getDuration()) + crouton.getOutAnimation().getDuration();
    }

    private void sendMessage(Crouton crouton, int messageId) {
        Message message = obtainMessage(messageId);
        message.obj = crouton;
        sendMessage(message);
    }

    private void sendMessageDelayed(Crouton crouton, int messageId, long delay) {
        Message message = obtainMessage(messageId);
        message.obj = crouton;
        sendMessageDelayed(message, delay);
    }

    public void handleMessage(Message message) {
        Crouton crouton = message.obj;
        if (crouton != null) {
            switch (message.what) {
                case Messages.ADD_CROUTON_TO_VIEW /*-1040157475*/:
                    addCroutonToView(crouton);
                    return;
                case Messages.REMOVE_CROUTON /*-1040155167*/:
                    removeCrouton(crouton);
                    if (crouton.getLifecycleCallback() != null) {
                        crouton.getLifecycleCallback().onRemoved();
                        return;
                    }
                    return;
                case Messages.DISPLAY_CROUTON /*794631*/:
                    displayCrouton();
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    }

    private void addCroutonToView(final Crouton crouton) {
        if (!crouton.isShowing()) {
            final View croutonView = crouton.getView();
            if (croutonView.getParent() == null) {
                LayoutParams params = croutonView.getLayoutParams();
                if (params == null) {
                    params = new MarginLayoutParams(-1, -2);
                }
                if (crouton.getViewGroup() != null) {
                    ViewGroup croutonViewGroup = crouton.getViewGroup();
                    if (shouldAddViewWithoutPosition(croutonViewGroup)) {
                        croutonViewGroup.addView(croutonView, params);
                    } else {
                        croutonViewGroup.addView(croutonView, 0, params);
                    }
                } else {
                    Activity activity = crouton.getActivity();
                    if (activity != null && !activity.isFinishing()) {
                        handleTranslucentActionBar((MarginLayoutParams) params, activity);
                        handleActionBarOverlay((MarginLayoutParams) params, activity);
                        activity.addContentView(croutonView, params);
                    } else {
                        return;
                    }
                }
            }
            croutonView.requestLayout();
            ViewTreeObserver observer = croutonView.getViewTreeObserver();
            if (observer != null) {
                observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @TargetApi(16)
                    public void onGlobalLayout() {
                        if (VERSION.SDK_INT < 16) {
                            croutonView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            croutonView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        if (crouton.getInAnimation() != null) {
                            croutonView.startAnimation(crouton.getInAnimation());
                            Manager.announceForAccessibilityCompat(crouton.getActivity(), crouton.getText());
                            if (-1 != crouton.getConfiguration().durationInMilliseconds) {
                                Manager.this.sendMessageDelayed(crouton, Messages.REMOVE_CROUTON, ((long) crouton.getConfiguration().durationInMilliseconds) + crouton.getInAnimation().getDuration());
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean shouldAddViewWithoutPosition(ViewGroup croutonViewGroup) {
        return (croutonViewGroup instanceof FrameLayout) || (croutonViewGroup instanceof AdapterView) || (croutonViewGroup instanceof RelativeLayout);
    }

    @TargetApi(19)
    private void handleTranslucentActionBar(MarginLayoutParams params, Activity activity) {
        if (VERSION.SDK_INT >= 19 && (activity.getWindow().getAttributes().flags & 67108864) == 67108864) {
            setActionBarMargin(params, activity);
        }
    }

    @TargetApi(11)
    private void handleActionBarOverlay(MarginLayoutParams params, Activity activity) {
        if (VERSION.SDK_INT >= 11 && activity.getWindow().hasFeature(9)) {
            setActionBarMargin(params, activity);
        }
    }

    private void setActionBarMargin(MarginLayoutParams params, Activity activity) {
        View actionBarContainer = activity.findViewById(Resources.getSystem().getIdentifier("action_bar_container", "id", "android"));
        if (actionBarContainer != null) {
            params.topMargin = actionBarContainer.getBottom();
        }
    }

    protected void removeCrouton(Crouton crouton) {
        View croutonView = crouton.getView();
        ViewGroup croutonParentView = (ViewGroup) croutonView.getParent();
        if (croutonParentView != null) {
            croutonView.startAnimation(crouton.getOutAnimation());
            Crouton removed = (Crouton) this.croutonQueue.poll();
            croutonParentView.removeView(croutonView);
            if (removed != null) {
                removed.detachActivity();
                removed.detachViewGroup();
                if (removed.getLifecycleCallback() != null) {
                    removed.getLifecycleCallback().onRemoved();
                }
                removed.detachLifecycleCallback();
            }
            sendMessageDelayed(crouton, Messages.DISPLAY_CROUTON, crouton.getOutAnimation().getDuration());
        }
    }

    void removeCroutonImmediately(Crouton crouton) {
        if (!(crouton.getActivity() == null || crouton.getView() == null || crouton.getView().getParent() == null)) {
            ((ViewGroup) crouton.getView().getParent()).removeView(crouton.getView());
            removeAllMessagesForCrouton(crouton);
        }
        Iterator<Crouton> croutonIterator = this.croutonQueue.iterator();
        while (croutonIterator.hasNext()) {
            Crouton c = (Crouton) croutonIterator.next();
            if (c.equals(crouton) && c.getActivity() != null) {
                removeCroutonFromViewParent(crouton);
                removeAllMessagesForCrouton(c);
                croutonIterator.remove();
                return;
            }
        }
    }

    void clearCroutonQueue() {
        removeAllMessages();
        for (Crouton crouton : this.croutonQueue) {
            removeCroutonFromViewParent(crouton);
        }
        this.croutonQueue.clear();
    }

    void clearCroutonsForActivity(Activity activity) {
        Iterator<Crouton> croutonIterator = this.croutonQueue.iterator();
        while (croutonIterator.hasNext()) {
            Crouton crouton = (Crouton) croutonIterator.next();
            if (crouton.getActivity() != null && crouton.getActivity().equals(activity)) {
                removeCroutonFromViewParent(crouton);
                removeAllMessagesForCrouton(crouton);
                croutonIterator.remove();
            }
        }
    }

    private void removeCroutonFromViewParent(Crouton crouton) {
        if (crouton.isShowing()) {
            ViewGroup parent = (ViewGroup) crouton.getView().getParent();
            if (parent != null) {
                parent.removeView(crouton.getView());
            }
        }
    }

    private void removeAllMessages() {
        removeMessages(Messages.ADD_CROUTON_TO_VIEW);
        removeMessages(Messages.DISPLAY_CROUTON);
        removeMessages(Messages.REMOVE_CROUTON);
    }

    private void removeAllMessagesForCrouton(Crouton crouton) {
        removeMessages(Messages.ADD_CROUTON_TO_VIEW, crouton);
        removeMessages(Messages.DISPLAY_CROUTON, crouton);
        removeMessages(Messages.REMOVE_CROUTON, crouton);
    }

    public static void announceForAccessibilityCompat(Context context, CharSequence text) {
        if (VERSION.SDK_INT >= 4) {
            AccessibilityManager accessibilityManager = null;
            if (context != null) {
                accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
            }
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                int eventType;
                if (VERSION.SDK_INT < 16) {
                    eventType = 8;
                } else {
                    eventType = 16384;
                }
                AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
                event.getText().add(text);
                event.setClassName(Manager.class.getName());
                event.setPackageName(context.getPackageName());
                accessibilityManager.sendAccessibilityEvent(event);
            }
        }
    }

    public String toString() {
        return "Manager{croutonQueue=" + this.croutonQueue + '}';
    }
}
