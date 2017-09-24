package com.anhnt.kovidict.myapplication.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class IndexScroller {
    private static final int STATE_HIDDEN = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOWN = 2;
    private static final int STATE_HIDING = 3;
    private float mAlphaRate;
    private int mCurrentSection;
    private float mDensity;
    private Handler mHandler;
    private float mIndexbarMargin;
    private RectF mIndexbarRect;
    private float mIndexbarWidth;
    private SectionIndexer mIndexer;
    private boolean mIsIndexing;
    private ListView mListView;
    private int mListViewHeight;
    private int mListViewWidth;
    private float mPreviewPadding;
    private float mScaledDensity;
    private String[] mSections;
    private int mState;

    public IndexScroller(Context context, ListView lv) {
        this.mState = STATE_HIDDEN;
        this.mCurrentSection = -1;
        this.mIsIndexing = false;
        this.mListView = null;
        this.mIndexer = null;
        this.mSections = null;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                IndexScroller indexScroller;
                switch (IndexScroller.this.mState) {
                    case IndexScroller.STATE_SHOWING:
                        indexScroller = IndexScroller.this;
                        indexScroller.mAlphaRate = (float) (((double) indexScroller.mAlphaRate) + (((1.0 - IndexScroller.this.mAlphaRate)) * 0.2d));
                        if (((double) IndexScroller.this.mAlphaRate) > 0.9d) {
                            IndexScroller.this.mAlphaRate = 1.0f;
                            IndexScroller.this.setState(IndexScroller.STATE_SHOWN);
                        }
                        IndexScroller.this.mListView.invalidate();
                        IndexScroller.this.fade(10);
                    case IndexScroller.STATE_SHOWN:
                        IndexScroller.this.setState(IndexScroller.STATE_HIDING);
                    case IndexScroller.STATE_HIDING:
                        indexScroller = IndexScroller.this;
                        indexScroller.mAlphaRate = (float) (((double) indexScroller.mAlphaRate) - (((double) IndexScroller.this.mAlphaRate) * 0.2d));
                        if (((double) IndexScroller.this.mAlphaRate) < 0.1d) {
                            IndexScroller.this.mAlphaRate = 0.0f;
                            IndexScroller.this.setState(IndexScroller.STATE_HIDDEN);
                        }
                        IndexScroller.this.mListView.invalidate();
                        IndexScroller.this.fade(10);
                    default:
                }
            }
        };
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.mListView = lv;
        setAdapter(this.mListView.getAdapter());
        this.mIndexbarWidth = 20.0f * this.mDensity;
        this.mIndexbarMargin = 10.0f * this.mDensity;
        this.mPreviewPadding = 5.0f * this.mDensity;
    }

    public void draw(Canvas canvas) {
        if (this.mState != STATE_HIDDEN) {
            Paint indexbarPaint = new Paint();
            indexbarPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            indexbarPaint.setAlpha((int) (64.0f * this.mAlphaRate));
            indexbarPaint.setAntiAlias(true);
            canvas.drawRoundRect(this.mIndexbarRect, 5.0f * this.mDensity, 5.0f * this.mDensity, indexbarPaint);
            if (this.mSections != null && this.mSections.length > 0) {
                if (this.mCurrentSection >= 0) {
                    Paint previewPaint = new Paint();
                    previewPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
                    previewPaint.setAlpha(96);
                    previewPaint.setAntiAlias(true);
                    previewPaint.setShadowLayer(3.0f, 0.0f, 0.0f, Color.argb(64, STATE_HIDDEN, STATE_HIDDEN, STATE_HIDDEN));
                    Paint previewTextPaint = new Paint();
                    previewTextPaint.setColor(-1);
                    previewTextPaint.setAntiAlias(true);
                    previewTextPaint.setTextSize(50.0f * this.mScaledDensity);
                    float previewTextWidth = previewTextPaint.measureText(this.mSections[this.mCurrentSection]);
                    float previewSize = ((2.0f * this.mPreviewPadding) + previewTextPaint.descent()) - previewTextPaint.ascent();
                    RectF previewRect = new RectF((((float) this.mListViewWidth) - previewSize) / 2.0f, (((float) this.mListViewHeight) - previewSize) / 2.0f, ((((float) this.mListViewWidth) - previewSize) / 2.0f) + previewSize, ((((float) this.mListViewHeight) - previewSize) / 2.0f) + previewSize);
                    canvas.drawRoundRect(previewRect, 5.0f * this.mDensity, 5.0f * this.mDensity, previewPaint);
                    canvas.drawText(this.mSections[this.mCurrentSection], (previewRect.left + ((previewSize - previewTextWidth) / 2.0f)) - 1.0f, ((previewRect.top + this.mPreviewPadding) - previewTextPaint.ascent()) + 1.0f, previewTextPaint);
                }
                Paint indexPaint = new Paint();
                indexPaint.setColor(-1);
                indexPaint.setAlpha((int) (255.0f * this.mAlphaRate));
                indexPaint.setAntiAlias(true);
                indexPaint.setTextSize(12.0f * this.mScaledDensity);
                float sectionHeight = (this.mIndexbarRect.height() - (2.0f * this.mIndexbarMargin)) / ((float) this.mSections.length);
                float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2.0f;
                for (int i = STATE_HIDDEN; i < this.mSections.length; i += STATE_SHOWING) {
                    Canvas canvas2 = canvas;
                    canvas2.drawText(this.mSections[i], this.mIndexbarRect.left + ((this.mIndexbarWidth - indexPaint.measureText(this.mSections[i])) / 2.0f), (((this.mIndexbarRect.top + this.mIndexbarMargin) + (((float) i) * sectionHeight)) + paddingTop) - indexPaint.ascent(), indexPaint);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case STATE_HIDDEN:
                if (this.mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {
                    setState(STATE_SHOWN);
                    this.mIsIndexing = true;
                    this.mCurrentSection = getSectionByPoint(ev.getY());
                    try {
                        this.mListView.setSelection(this.mIndexer.getPositionForSection(this.mCurrentSection));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            case STATE_SHOWING:
                if (this.mIsIndexing) {
                    this.mIsIndexing = false;
                    this.mCurrentSection = -1;
                }
                if (this.mState == STATE_SHOWN) {
                    setState(STATE_HIDING);
                    break;
                }
                break;
            case STATE_SHOWN:
                if (this.mIsIndexing) {
                    if (!contains(ev.getX(), ev.getY())) {
                        return true;
                    }
                    this.mCurrentSection = getSectionByPoint(ev.getY());
                    try {
                        this.mListView.setSelection(this.mIndexer.getPositionForSection(this.mCurrentSection));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mListViewWidth = w;
        this.mListViewHeight = h;
        this.mIndexbarRect = new RectF((((float) w) - this.mIndexbarMargin) - this.mIndexbarWidth, this.mIndexbarMargin, ((float) w) - this.mIndexbarMargin, ((float) h) - this.mIndexbarMargin);
    }

    public void show() {
        if (this.mState == 0) {
            setState(STATE_SHOWING);
        } else if (this.mState == STATE_HIDING) {
            setState(STATE_HIDING);
        }
    }

    public void hide() {
        if (this.mState == STATE_SHOWN) {
            setState(STATE_HIDING);
        }
    }

    public void setAdapter(Adapter adapter) {
        if (adapter instanceof SectionIndexer) {
            this.mIndexer = (SectionIndexer) adapter;
            this.mSections = (String[]) this.mIndexer.getSections();
        }
    }

    private void setState(int state) {
        if (state >= 0 && state <= STATE_HIDING) {
            this.mState = state;
            switch (this.mState) {
                case STATE_HIDDEN /*0*/:
                    this.mHandler.removeMessages(STATE_HIDDEN);
                case STATE_SHOWING /*1*/:
                    this.mAlphaRate = 0.0f;
                    fade(0);
                case STATE_SHOWN /*2*/:
                    this.mHandler.removeMessages(STATE_HIDDEN);
                case STATE_HIDING /*3*/:
                    this.mAlphaRate = 1.0f;
                    fade(3000);
                default:
            }
        }
    }

    private boolean contains(float x, float y) {
        return x >= this.mIndexbarRect.left && y >= this.mIndexbarRect.top && y <= this.mIndexbarRect.top + this.mIndexbarRect.height();
    }

    private int getSectionByPoint(float y) {
        if (this.mSections == null || this.mSections.length == 0 || y < this.mIndexbarRect.top + this.mIndexbarMargin) {
            return STATE_HIDDEN;
        }
        if (y >= (this.mIndexbarRect.top + this.mIndexbarRect.height()) - this.mIndexbarMargin) {
            return this.mSections.length - 1;
        }
        return (int) (((y - this.mIndexbarRect.top) - this.mIndexbarMargin) / ((this.mIndexbarRect.height() - (2.0f * this.mIndexbarMargin)) / ((float) this.mSections.length)));
    }

    private void fade(long delay) {
        this.mHandler.removeMessages(STATE_HIDDEN);
        this.mHandler.sendEmptyMessageAtTime(STATE_HIDDEN, SystemClock.uptimeMillis() + delay);
    }
}