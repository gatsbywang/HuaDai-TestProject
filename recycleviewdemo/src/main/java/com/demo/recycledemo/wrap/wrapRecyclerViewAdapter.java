package com.demo.recycledemo.wrap;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 花歹 on 2017/4/25.
 * Email:   gatsbywang@126.com
 */

public class wrapRecyclerViewAdapter extends RecyclerView.Adapter {

    private RecyclerView.Adapter mAdapter;

    private SparseArray<View> mHeaders, mFooters;
    private int BASE_HEADER_KEY = 1000000;
    private int BASE_FOOTER_KEY = 1000000;

    public wrapRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        mHeaders = new SparseArray<>();
        mFooters = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaders.indexOfKey(viewType) == 0) {
            return createHeaderViewHolder(mHeaders.get(viewType));
        } else if (mFooters.indexOfKey(viewType) == 0) {
            return createFooterViewHolder(mFooters.get(viewType));
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private RecyclerView.ViewHolder createFooterViewHolder(View view) {
        return null;
    }

    private RecyclerView.ViewHolder createHeaderViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //头部类型
        int numHeaders = mHeaders.size();
        if (position < numHeaders) {
            return;
        }

        //中间类型
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
            }
        }


    }

    @Override
    public int getItemViewType(int position) {
        //头部类型
        int numHeaders = mHeaders.size();
        if (position < numHeaders) {
            return mHeaders.keyAt(position);
        }

        //中间类型
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }

        //底部类型
        return mFooters.keyAt(adjPosition - adapterCount);
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return mHeaders.size() + mFooters.size() + mAdapter.getItemCount();
        } else {
            return mHeaders.size() + mFooters.size();
        }
    }

    public void addHeaderView(View headerView) {
        if (mHeaders.indexOfValue(headerView) == -1) {
            mHeaders.put(BASE_HEADER_KEY++, headerView);
        }
    }

    public void removeHeaderView(View headerView) {
        if (mHeaders.indexOfValue(headerView) != -1) {
            mHeaders.removeAt(mHeaders.indexOfValue(headerView));
        }
    }

    public void addFooterView(View footerView) {
        if (mFooters.indexOfValue(footerView) == -1) {
            mFooters.put(BASE_HEADER_KEY++, footerView);
        }
    }

    public void removeFooterView(View footerView) {
        if (mFooters.indexOfValue(footerView) != -1) {
            mFooters.removeAt(mFooters.indexOfValue(footerView));
        }
    }

}
