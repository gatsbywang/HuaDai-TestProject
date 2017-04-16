package com.demo.custombannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private BannerViewPager mBannerVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBannerVP = (BannerViewPager) findViewById(R.id.banner_vp);
        mBannerVP.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView itemImageView = new ImageView(MainActivity.this);
                itemImageView.setImageResource(R.drawable.banner_default);
                return itemImageView;
            }
        });

        mBannerVP.startRoll();
    }
}
