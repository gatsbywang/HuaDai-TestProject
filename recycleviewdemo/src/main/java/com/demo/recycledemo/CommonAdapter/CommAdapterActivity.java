package com.demo.recycledemo.CommonAdapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.demo.recycledemo.R;

import java.util.ArrayList;
import java.util.List;

public class CommAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_adapter);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewCommonAdapter adapter = new RecyclerViewCommonAdapter<String>(this, initData(),
                R.layout.item_base) {
            @Override
            public void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_num, s);
            }
        };
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        recyclerView.setAdapter(adapter);

        ListView listView = null;
//        listView.addHeaderView();
    }

    private List<String> initData() {
        List<String> datas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            datas.add(String.valueOf((char) i));
        }
        return datas;
    }

}
