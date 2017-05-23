package myapplication.absnavigationbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DefaultNavigationBar.Builder(this)
                .setTitle("MainActivityTitle")
                .setRightText("右边导航")
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "右边导航", Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }

}
