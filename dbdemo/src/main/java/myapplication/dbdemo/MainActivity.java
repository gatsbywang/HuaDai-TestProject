package myapplication.dbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactory().getDao(Person.class);
        daoSupport.insert(new Person("huadai", 22));
    }
}
