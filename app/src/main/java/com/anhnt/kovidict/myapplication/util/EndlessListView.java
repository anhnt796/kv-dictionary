package com.anhnt.kovidict.myapplication.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;

public class EndlessListView extends ListView {
    private static final String TAG = "EndlessListView";
    private boolean mIsLoading;
    private OnLoadMoreListener onLoadMoreListener;
    private ProgressBar progressBar;

    private class ELScrollChangedListener implements OnScrollListener {
        private ELScrollChangedListener() {
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean loadMore;
            if (totalItemCount == 0 || firstVisibleItem + visibleItemCount < totalItemCount) {
                loadMore = false;
            } else {
                loadMore = true;
            }
            if (!EndlessListView.this.mIsLoading && loadMore && EndlessListView.this.onLoadMoreListener != null && EndlessListView.this.onLoadMoreListener.onLoadMore()) {
                EndlessListView.this.mIsLoading = true;
                EndlessListView.this.progressBar.setVisibility(VISIBLE);
            }
        }
    }

    public interface OnLoadMoreListener {
        boolean onLoadMore();
    }

    public EndlessListView(Context context) {
        super(context);
        init();
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void loadMoreCompleat() {
        this.mIsLoading = false;
        this.progressBar.setVisibility(View.GONE);
    }

    private void init() {
        this.mIsLoading = false;
        this.progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        this.progressBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.progressBar.setPadding(6, 6, 6, 6);
        this.progressBar.setVisibility(View.GONE);
        LinearLayout footerLinearLayout = new LinearLayout(getContext());
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerLinearLayout.setGravity(17);
        footerLinearLayout.setLayoutParams(layoutParams);
        footerLinearLayout.addView(this.progressBar);
        addFooterView(footerLinearLayout);
        super.setOnScrollListener(new ELScrollChangedListener());
    }
}