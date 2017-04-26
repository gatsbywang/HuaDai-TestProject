package com.demo.recycledemo.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 花歹 on 2017/4/26.
 * Email:   gatsbywang@126.com
 */

public class WrapRecyclerView extends RecyclerView {
    private WrapRecyclerViewAdapter mAdapter;

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    };

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof WrapRecyclerViewAdapter) {
            mAdapter = (WrapRecyclerViewAdapter) adapter;
        } else {
            mAdapter = new WrapRecyclerViewAdapter(adapter);
            adapter.registerAdapterDataObserver(mObserver);
        }
        super.setAdapter(mAdapter);
    }

    public void addHeaderView(View headerView) {
        if (mAdapter != null) {
            mAdapter.addHeaderView(headerView);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeHeaderView(View headerView) {
        if (mAdapter != null) {
            mAdapter.removeHeaderView(headerView);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addFooterView(View footerView) {
        if (mAdapter != null) {
            mAdapter.addFooterView(footerView);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeFooterView(View footerView) {
        if (mAdapter != null) {
            mAdapter.removeFooterView(footerView);
            mAdapter.notifyDataSetChanged();
        }
    }
}
