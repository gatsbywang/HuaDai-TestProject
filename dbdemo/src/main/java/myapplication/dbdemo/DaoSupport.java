package myapplication.dbdemo;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;

/**
 * Created by 花歹 on 2017/5/8.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class DaoSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mClazz;

    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mSqLiteDatabase = sqLiteDatabase;
        this.mClazz = clazz;

        //创建表
        /* create table if not exits Person(
            id integer primary key autoincrement,
            name text,
            age integer
            flag boolean*/
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exits ");
        //创建clazz类型的表
        stringBuffer.append(clazz.getSimpleName());
        stringBuffer.append("(id integer primary key autoincrement, ");
        //反射获取clazz 的变量
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();
            stringBuffer.append(name).append(" ").append(DaoUtils.getColumnType(type))
                    .append(", ");
        }
        stringBuffer.replace(stringBuffer.length() - 2, stringBuffer.length(), ")");
    }

    @Override
    public int insert(T t) {
        //基本insert写法
//        SQLiteDatabase.openDatabase().insert()
        return 0;
    }
}
