package myapplication.httpenginedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpUtils.with(this).url("").params("", "").get().execute(new EngineCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(String result) {

            }
        });
    }
}
