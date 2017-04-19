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
            public View getView(int position, View converView) {
                ImageView itemImageView = converView == null ? new ImageView(MainActivity.this) : (ImageView) converView;
                itemImageView.setScaleType(ImageView.ScaleType.FIT_XY);
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
