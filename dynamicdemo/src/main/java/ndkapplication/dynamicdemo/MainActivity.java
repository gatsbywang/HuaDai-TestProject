package ndkapplication.dynamicdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity activity = (MainActivity) Proxy.newProxyInstance(MainActivity.class.getClassLoader(),
                new Class<?>[]{MainActivity.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
        startActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
