package com.anhnt.kovidict.myapplication.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

public class IndexableListView extends ListView {
    private GestureDetector mGestureDetector;
    private boolean mIsFastScrollEnabled;
    private IndexScroller mScroller;

    public IndexableListView(Context context) {
        super(context);
        this.mIsFastScrollEnabled = false;
        this.mScroller = null;
        this.mGestureDetector = null;
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIsFastScrollEnabled = false;
        this.mScroller = null;
        this.mGestureDetector = null;
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsFastScrollEnabled = false;
        this.mScroller = null;
        this.mGestureDetector = null;
    }

    public boolean isFastScrollEnabled() {
        return this.mIsFastScrollEnabled;
    }

    public void setFastScrollEnabled(boolean enabled) {
        this.mIsFastScrollEnabled = enabled;
        if (this.mIsFastScrollEnabled) {
            if (this.mScroller == null) {
                this.mScroller = new IndexScroller(getContext(), this);
            }
        } else if (this.mScroller != null) {
            this.mScroller.hide();
            this.mScroller = null;
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mScroller != null) {
            this.mScroller.draw(canvas);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mScroller != null && this.mScroller.onTouchEvent(ev)) {
            return true;
        }
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    IndexableListView.this.mScroller.show();
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }
        this.mGestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (this.mScroller != null) {
            this.mScroller.setAdapter(adapter);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mScroller != null) {
            this.mScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }
}