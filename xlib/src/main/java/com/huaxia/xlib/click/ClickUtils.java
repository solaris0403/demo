package com.huaxia.xlib.click;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;


/**
 * 点击相关工具类。
 * 参考 https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/src/main/
 * java/com/blankj/utilcode/util/ClickUtils.java
 *
 * @author xzy
 */
@SuppressWarnings("unused")
public class ClickUtils {

    private static final int SCALE = -1;
    private static final float SCALE_DEFAULT_FACTOR = -0.06f;
    private static final long DEBOUNCING_DEFAULT_DURATION = 200;

    /**
     * Apply scale animation for the views' click.
     *
     * @param views The views.
     */
    public static void applyScale(final View... views) {
        applyScale(views, null);
    }

    /**
     * Apply scale animation for the views' click.
     *
     * @param views        The views.
     * @param scaleFactors The factors of scale for the views.
     */
    public static void applyScale(final View[] views, final float[] scaleFactors) {
        if (views == null || views.length == 0) return;
        for (int i = 0; i < views.length; i++) {
            if (views[i] == null) continue;
            if (scaleFactors == null || i >= scaleFactors.length) {
                views[i].setTag(SCALE, SCALE_DEFAULT_FACTOR);
            } else {
                views[i].setTag(SCALE, scaleFactors[i]);
            }
            views[i].setClickable(true);
            views[i].setOnTouchListener(OnUtilsTouchListener.getInstance());
        }
    }

    /**
     * Apply single debouncing for the view's click.
     *
     * @param view     The view.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View view, final View.OnClickListener listener) {
        applySingleDebouncing(new View[]{view}, listener);
    }

    /**
     * Apply single debouncing for the view's click.
     *
     * @param view     The view.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View view, @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applySingleDebouncing(new View[]{view}, duration, listener);
    }

    /**
     * Apply single debouncing for the views' click.
     *
     * @param views    The views.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View[] views,
                                             final View.OnClickListener listener) {
        applySingleDebouncing(views, DEBOUNCING_DEFAULT_DURATION, listener);
    }

    /**
     * Apply single debouncing for the views' click.
     *
     * @param views    The views.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View[] views,
                                             @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applyDebouncing(views, false, duration, listener);
    }

    /**
     * Apply global debouncing for the view's click.
     *
     * @param view     The view.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View view,
                                             final View.OnClickListener listener) {
        applyGlobalDebouncing(new View[]{view}, listener);
    }

    /**
     * Apply global debouncing for the view's click.
     *
     * @param view     The view.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View view, @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applyGlobalDebouncing(new View[]{view}, duration, listener);
    }


    /**
     * Apply global debouncing for the views' click.
     *
     * @param views    The views.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View[] views,
                                             final View.OnClickListener listener) {
        applyGlobalDebouncing(views, DEBOUNCING_DEFAULT_DURATION, listener);
    }

    /**
     * Apply global debouncing for the views' click.
     *
     * @param views    The views.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View[] views,
                                             @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applyDebouncing(views, true, duration, listener);
    }

    private static void applyDebouncing(final View[] views,
                                        final boolean isGlobal,
                                        @IntRange(from = 0) long duration,
                                        final View.OnClickListener listener) {
        if (views == null || views.length == 0 || listener == null) return;
        for (View view : views) {
            if (view == null) continue;
            view.setOnClickListener(new OnDebouncingClickListener(isGlobal, duration) {
                @Override
                public void onDebouncingClick(View v) {
                    listener.onClick(v);
                }
            });
        }
    }

    public static abstract class OnDebouncingClickListener implements View.OnClickListener {

        private static final int TAG_KEY = 0x7EFFFFFF;

        private static boolean mEnabled = true;

        private static final Runnable ENABLE_AGAIN = new Runnable() {
            @Override
            public void run() {
                mEnabled = true;
            }
        };

        private static boolean isValid(@NonNull final View view, final long duration) {
            long curTime = System.currentTimeMillis();
            Object tag = view.getTag(TAG_KEY);
            if (!(tag instanceof Long)) {
                view.setTag(TAG_KEY, curTime);
                return true;
            }
            long preTime = (Long) tag;
            if (curTime - preTime <= duration) return false;
            view.setTag(TAG_KEY, curTime);
            return true;
        }

        private long mDuration;
        private boolean mIsGlobal;

        public OnDebouncingClickListener() {
            this(true, DEBOUNCING_DEFAULT_DURATION);
        }

        public OnDebouncingClickListener(final boolean isGlobal) {
            this(isGlobal, DEBOUNCING_DEFAULT_DURATION);
        }

        public OnDebouncingClickListener(final long duration) {
            this(true, duration);
        }

        public OnDebouncingClickListener(final boolean isGlobal, final long duration) {
            mIsGlobal = isGlobal;
            mDuration = duration;
        }

        public abstract void onDebouncingClick(View v);

        @Override
        public final void onClick(View v) {
            if (mIsGlobal) {
                if (mEnabled) {
                    mEnabled = false;
                    v.postDelayed(ENABLE_AGAIN, mDuration);
                    onDebouncingClick(v);
                }
            } else {
                if (isValid(v, mDuration)) {
                    onDebouncingClick(v);
                }
            }
        }
    }

    public static abstract class OnMultiClickListener implements View.OnClickListener {

        private static final long DEFAULT_INTERVAL = 666;

        private final int mTriggerClickCount;
        private final long mClickInterval;

        private long mLastClickTime;
        private int mClickCount;

        public OnMultiClickListener(int triggerClickCount) {
            this(triggerClickCount, DEFAULT_INTERVAL);
        }

        public OnMultiClickListener(int triggerClickCount, long clickInterval) {
            this.mTriggerClickCount = triggerClickCount;
            this.mClickInterval = clickInterval;
        }

        public abstract void onTriggerClick(View v);

        public abstract void onBeforeTriggerClick(View v, int count);

        @Override
        public void onClick(View v) {
            if (mTriggerClickCount <= 1) {
                onTriggerClick(v);
                return;
            }
            long curTime = System.currentTimeMillis();

            if (curTime - mLastClickTime < mClickInterval) {
                mClickCount++;
                if (mClickCount == mTriggerClickCount) {
                    onTriggerClick(v);
                } else if (mClickCount < mTriggerClickCount) {
                    onBeforeTriggerClick(v, mClickCount);
                } else {
                    mClickCount = 1;
                    onBeforeTriggerClick(v, mClickCount);
                }
            } else {
                mClickCount = 1;
                onBeforeTriggerClick(v, mClickCount);
            }
            mLastClickTime = curTime;
        }
    }

    private static class OnUtilsTouchListener implements View.OnTouchListener {

        public static OnUtilsTouchListener getInstance() {
            return LazyHolder.INSTANCE;
        }

        private OnUtilsTouchListener() {/**/}

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                processScale(v, true);
            } else if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_CANCEL) {
                processScale(v, false);
            }
            return false;
        }

        private void processScale(final View view, boolean isDown) {
            Object tag = view.getTag(SCALE);
            if (!(tag instanceof Float)) return;
            float value = isDown ? 1 + (Float) tag : 1;
            view.animate()
                    .scaleX(value)
                    .scaleY(value)
                    .setDuration(100)
                    .start();
        }
    }

    private static class LazyHolder {
        private static final OnUtilsTouchListener INSTANCE = new OnUtilsTouchListener();
    }
}
