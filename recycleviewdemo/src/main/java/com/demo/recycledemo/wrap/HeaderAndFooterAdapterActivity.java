package com.demo.recycledemo.wrap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.demo.recycledemo.BaseUse.LinearLayoutItemDecoration;
import com.demo.recycledemo.CommonAdapter.ItemClickListener;
import com.demo.recycledemo.CommonAdapter.RecyclerViewCommonAdapter;
import com.demo.recycledemo.CommonAdapter.ViewHolder;
import com.demo.recycledemo.R;

import java.util.ArrayList;
import java.util.List;

public class HeaderAndFooterAdapterActivity extends AppCompatActivity {

    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_adapter);
        WrapRecyclerView recyclerView = (WrapRecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LinearLayoutItemDecoration(this, R.drawable.item_divider));
        final RecyclerViewCommonAdapter adapter = new RecyclerViewCommonAdapter<String>(this, initData(),
                R.layout.item_base) {
            @Override
            public void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_num, s);
            }
        };
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mDatas.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addHeaderView(this.getLayoutInflater().inflate(R.layout.layout_header, recyclerView, false));
        adapter.notifyDataSetChanged();

//        ListView listView = null;
//        listView.addHeaderView();
    }

    private List<String> initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add(String.valueOf((char) i));
        }
        return mDatas;
    }

    public static Intent buildStartIntent(Context context) {
        Intent intent = new Intent(context, HeaderAndFooterAdapterActivity.class);
        return intent;
    }

}
