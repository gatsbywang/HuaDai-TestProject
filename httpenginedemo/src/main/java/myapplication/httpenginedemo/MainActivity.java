package myapplication.httpenginedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpUtils.with(this).url("").params("", "").get().execute(new HttpCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {

            }

            @Override
            public void onError(Exception e) {

            }


        });
    }
}
