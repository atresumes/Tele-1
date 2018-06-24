package org.webrtc.sinch;

import android.os.Handler;
import android.os.SystemClock;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    final class C07153 implements Runnable {
        final /* synthetic */ CountDownLatch val$barrier;
        final /* synthetic */ Callable val$callable;
        final /* synthetic */ AnonymousClass1Result val$result;

        C07153(AnonymousClass1Result anonymousClass1Result, Callable callable, CountDownLatch countDownLatch) {
            this.val$result = anonymousClass1Result;
            this.val$callable = callable;
            this.val$barrier = countDownLatch;
        }

        public void run() {
            try {
                this.val$result.value = this.val$callable.call();
                this.val$barrier.countDown();
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException("Callable threw exception: " + e);
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        }
    }

    final class C07164 implements Runnable {
        final /* synthetic */ CountDownLatch val$barrier;
        final /* synthetic */ Runnable val$runner;

        C07164(Runnable runnable, CountDownLatch countDownLatch) {
            this.val$runner = runnable;
            this.val$barrier = countDownLatch;
        }

        public void run() {
            this.val$runner.run();
            this.val$barrier.countDown();
        }
    }

    public interface BlockingOperation {
        void run();
    }

    public class ThreadChecker {
        private Thread thread = Thread.currentThread();

        public void checkIsOnValidThread() {
            if (this.thread == null) {
                this.thread = Thread.currentThread();
            }
            if (Thread.currentThread() != this.thread) {
                throw new IllegalStateException("Wrong thread");
            }
        }

        public void detachThread() {
            this.thread = null;
        }
    }

    final class C11141 implements BlockingOperation {
        final /* synthetic */ Thread val$thread;

        C11141(Thread thread) {
            this.val$thread = thread;
        }

        public void run() {
            this.val$thread.join();
        }
    }

    final class C11152 implements BlockingOperation {
        final /* synthetic */ CountDownLatch val$latch;

        C11152(CountDownLatch countDownLatch) {
            this.val$latch = countDownLatch;
        }

        public void run() {
            this.val$latch.await();
        }
    }

    public static void awaitUninterruptibly(CountDownLatch countDownLatch) {
        executeUninterruptibly(new C11152(countDownLatch));
    }

    public static boolean awaitUninterruptibly(CountDownLatch countDownLatch, long j) {
        boolean z = false;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Object obj = null;
        long j2 = j;
        do {
            try {
                z = countDownLatch.await(j2, TimeUnit.MILLISECONDS);
                break;
            } catch (InterruptedException e) {
                obj = 1;
                j2 = j - (SystemClock.elapsedRealtime() - elapsedRealtime);
                if (j2 <= 0) {
                }
            }
        } while (j2 <= 0);
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
        return z;
    }

    public static void executeUninterruptibly(BlockingOperation blockingOperation) {
        Object obj = null;
        while (true) {
            try {
                blockingOperation.run();
                break;
            } catch (InterruptedException e) {
                obj = 1;
            }
        }
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
    }

    public static <V> V invokeAtFrontUninterruptibly(Handler handler, Callable<V> callable) {
        if (handler.getLooper().getThread() == Thread.currentThread()) {
            try {
                return callable.call();
            } catch (Exception e) {
                RuntimeException runtimeException = new RuntimeException("Callable threw exception: " + e);
                runtimeException.setStackTrace(e.getStackTrace());
                throw runtimeException;
            }
        }
        AnonymousClass1Result anonymousClass1Result = new Object() {
            public V value;
        };
        CountDownLatch countDownLatch = new CountDownLatch(1);
        handler.post(new C07153(anonymousClass1Result, callable, countDownLatch));
        awaitUninterruptibly(countDownLatch);
        return anonymousClass1Result.value;
    }

    public static void invokeAtFrontUninterruptibly(Handler handler, Runnable runnable) {
        if (handler.getLooper().getThread() == Thread.currentThread()) {
            runnable.run();
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        handler.postAtFrontOfQueue(new C07164(runnable, countDownLatch));
        awaitUninterruptibly(countDownLatch);
    }

    public static void joinUninterruptibly(Thread thread) {
        executeUninterruptibly(new C11141(thread));
    }

    public static boolean joinUninterruptibly(Thread thread, long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        boolean z = false;
        long j2 = j;
        while (j2 > 0) {
            try {
                thread.join(j2);
                break;
            } catch (InterruptedException e) {
                j2 = j - (SystemClock.elapsedRealtime() - elapsedRealtime);
                z = true;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
        return !thread.isAlive();
    }
}
