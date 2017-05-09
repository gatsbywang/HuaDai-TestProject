package myapplication.dbdemo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

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
    private final String TAG = DaoSupport.class.getName();

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
        stringBuffer.append("create table if not exists ")
                .append(DaoUtils.getTableName(clazz))   //获取表的名称
                .append(" (id integer primary key autoincrement, ");
        //创建clazz类型的表
//        stringBuffer.append(clazz.getSimpleName());
//        stringBuffer.append("(id integer primary key autoincrement, ");
        //反射获取clazz 的变量
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {

            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();
            String columnType = DaoUtils.getColumnType(type);
            if (TextUtils.isEmpty(columnType)) {
                continue;
            }
            stringBuffer.append(name).append(columnType)
                    .append(", ");
        }
        stringBuffer.replace(stringBuffer.length() - 2, stringBuffer.length(), ")");
        String createTableSql = stringBuffer.toString();
        Log.e(TAG, "创建表语句-->" + createTableSql);
        mSqLiteDatabase.execSQL(createTableSql);
    }

    @Override
    public long insert(T t) {
        //基本insert写法
       /* ContentValues values = new ContentValues();
        values.put(xxx,xxx);
        SQLiteDatabase.openDatabase().insert(table,null,value)*/
//        mSqLiteDatabase.insert();

        ContentValues values = new ContentValues();
//        values.put();

        return mSqLiteDatabase.insert(DaoUtils.getTableName(mClazz), null, values);
    }
}
