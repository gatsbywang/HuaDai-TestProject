package com.demo.custombannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private BannerView mBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBannerView = (BannerView) findViewById(R.id.banner_view);
        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView itemImageView = new ImageView(MainActivity.this);
                itemImageView.setImageResource(R.drawable.banner_default);
                return itemImageView;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });

        mBannerView.startRoll();
    }
}
