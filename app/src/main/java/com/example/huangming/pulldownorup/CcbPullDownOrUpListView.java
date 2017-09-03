/*
 * Copyright (C) 2014 China Construction Bank
 *
 * 本程序中所包含的信息属于机密信息，如无中国建设银行的书面许可，任何人都无权复制或利用。
 */
package com.example.huangming.pulldownorup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;


/**
 * 可上拉、下拉加载更多的列表
 *
 * @author Huangming 2017/8/22
 */
public class CcbPullDownOrUpListView extends ListView {

    protected static final String TAG = "CcbPullDownOrUpListView";

    private RelativeLayout headerView;
    private RelativeLayout footerView;

    /** 能否上拉加载更多 */
    private boolean pullUpEnabled = false;

    /** 能否下拉加载更多 */
    private boolean pullDownEnabled = false;

    /** 是否加载完成 */
    private boolean isComplete = true;

    private LoadMoreListener loadMoreListener = null;

    /**
     * 实现加载更多数据的接口
     *
     * @author Huangming 2017/8/22
     */
    public interface LoadMoreListener {

        /**
         * 从顶部加载更多数据
         * 加载完成后必须要调setPullDownEnabled()方法，以隐藏转圈图标及下次下拉能否加载更多
         */
        void onLoadMoreFromTop();

        /**
         * 从底部加载更多数据
         * 加载完成后必须要调setPullUpEnabled()方法，以隐藏转圈图标及下次上拉能否加载更多
         */
        void onLoadMoreFromBottom();
    }

    /**
     * 设置加载更多监听器
     *
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    /**
     * 设置能否上拉加载更多
     *
     * @param pullUpEnabled
     */
    public void setPullUpEnabled(boolean pullUpEnabled) {
        setSelection(getCount() - 1);
        this.pullUpEnabled = pullUpEnabled;
        isComplete = true;
        if (footerView != null) {
            removeFooterView(footerView);
        }
    }

    /**
     * 设置能否下拉加载更多
     *
     * @param pullDownEnabled
     */
    public void setPullDownEnabled(boolean pullDownEnabled) {
        this.pullDownEnabled = pullDownEnabled;
        if (headerView != null) {
            removeHeaderView(headerView);
        }
    }

    public CcbPullDownOrUpListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CcbPullDownOrUpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CcbPullDownOrUpListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
            .LAYOUT_INFLATER_SERVICE);
        headerView = (RelativeLayout) inflater.inflate(R.layout.pull_down_or_up_loading_more, null);
        addHeaderView(headerView);

        footerView = (RelativeLayout) inflater.inflate(R.layout.pull_down_or_up_loading_more, null);
        addFooterView(footerView);

        setHeaderDividersEnabled(false);
        setFooterDividersEnabled(false);
        setVerticalScrollBarEnabled(false);

        setOnScrollListener(new OnScrollListener() {
            boolean isAtBottom = false;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // 下拉到顶部，进行更新
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && pullDownEnabled
                    && absListView.getFirstVisiblePosition() == 0) {
                    View topChild = absListView.getChildAt(0);
                    boolean atTop = topChild.getTop() >= -10;
                    if (headerView != null && loadMoreListener != null && atTop) {
                        addHeaderView(headerView);
                        Log.d(TAG, "去加载顶部数据。。。");
                        loadMoreListener.onLoadMoreFromTop();
                    }
                    return;
                }

                // 上拉到底部，进行更新
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getFirstVisiblePosition() == 0
                    && absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                    // 针对数据不满一屏的情况，会先加载顶部的数据，如果顶部的数据加载完了还是不满一屏，
                    // 就加载底部的数据
                    isAtBottom = isComplete;
                }
                if (footerView != null && pullUpEnabled && loadMoreListener != null && isAtBottom) {
                    isComplete = false;
                    addFooterView(footerView);
                    setSelection(absListView.getCount() - 1);
                    Log.d(TAG, "去加载底部数据。。。");
                    loadMoreListener.onLoadMoreFromBottom();
                }
            }

            @Override
            public void onScroll(AbsListView mListView, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = mListView.getChildAt(
                        mListView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom()
                        == mListView.getHeight()) {
                        System.out.println("滚动到底部。。。");
                        isAtBottom = isComplete;
                    }
                }
            }
        });
    }
}
