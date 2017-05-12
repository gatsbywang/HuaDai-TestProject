package myapplication.dbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactory().getDao(Person.class);
//        daoSupport.insert(new Person("huadai", 22));


//        DaoUtils.capitalize(long.class.getName());
//        DaoUtils.capitalize(int.class.getName());
//        DaoUtils.capitalize(float.class.getName());
//        DaoUtils.capitalize(double.class.getName());
//        DaoUtils.capitalize(char.class.getName());
//        DaoUtils.capitalize(byte.class.getName());
//        DaoUtils.capitalize(short.class.getName());
//        DaoUtils.capitalize(boolean.class.getName());

        List<Person> persons = daoSupport.querySupport().selection("age = ?").selectionArgs("23").query();
    }
}
