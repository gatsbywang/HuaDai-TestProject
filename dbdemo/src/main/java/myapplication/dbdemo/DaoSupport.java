package myapplication.dbdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 花歹 on 2017/5/8.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class DaoSupport<T> implements IDaoSupport<T> {

    //
    private SQLiteDatabase mSqLiteDatabase;

    //存储的实体类（如Person）
    private Class<T> mClazz;
    private final String TAG = DaoSupport.class.getName();

    //反射优化，感谢google,google大法好（参考AppCompatViewInflate的createViewFromTag）
    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();

    private final Object[] mPutArgs = new Object[2];

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

        ContentValues values = objToValue(t);

        return mSqLiteDatabase.insert(DaoUtils.getTableName(mClazz), null, values);
    }

    @Override
    public void insert(List<T> datas) {

        //使用事务优化，原则上事务是不能优化数据库插入效率，但那时sqlite的单条插入就说一个事务，如果把多条数据插入当做
        //一个事务来处理，那会大大提高插入效率。
        mSqLiteDatabase.beginTransaction();
        for (T data : datas) {
            //调用单条插入
            insert(data);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    /**
     * 将object中需要插入数据库的变量转换为插入数据的ContentValues
     *
     * @param obj
     * @return
     */
    private ContentValues objToValue(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                mPutArgs[0] = key;
                mPutArgs[1] = value;
                //1、value不知道是类型，可以通过if else一个个执行values.put(key, value);
                //2、通过反射执行put方法

                //反射优化，google大法好
                String type = field.getType().getName();
                Method method = mPutMethods.get(type);
                if (method == null) {
                    method = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(type, method);
                }
                method.invoke(values, mPutArgs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                mPutArgs[0] = null;
                mPutArgs[1] = null;
            }
        }

        return values;
    }

    @Override
    public List<T> query() {
        Cursor cursor = mSqLiteDatabase.query(DaoUtils.getTableName(mClazz), null, null, null, null, null, null);
        return cursorToList(cursor);
    }

    /**
     * 将cursor中的数据转换为List集合
     *
     * @param cursor
     * @return
     */
    private List<T> cursorToList(Cursor cursor) {

        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            //不断从游标获取数据
            do {
                try {
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();

                        //获取在第几列
                        int columnIndex = cursor.getColumnIndex(name);
                        if (columnIndex == -1) {

                        }

                        Method cursorMethod = cursorMethod(field.getType());

                        if (cursorMethod != null) {
                            //通过反射执行方法,得到
                            Object value = cursorMethod.invoke(cursor, columnIndex);
                            if (value == null) {
                                continue;
                            }

                            //处理特殊部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);

                            } else if (field.getType() == Date.class) {
                                long date = (long) value;
                                if (date < 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }

                            //反射注入
                            field.set(instance, value);
                        }
                    }


                    //
                    list.add(instance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 通过类型获取游标的方法（如curosr.getInt,还是curosr.getLong）
     *
     * @param type
     * @return
     */
    private Method cursorMethod(Class<?> type) throws NoSuchMethodException {
        //cursor.getInt(),cursor.getLong，返回的的是getInt或者getgLong，或者其他
        String methodName = getColumnMethodName(type);

        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    /**
     * 拼接cursor方法，如getLong,则应该为"get"+"Long"
     *
     * @param type T的属性变量类型
     * @return
     */
    private String getColumnMethodName(Class<?> type) {
        String typeName;
        if (type.isPrimitive()) {
            typeName = DaoUtils.capitalize(type.getName());
        } else {
            typeName = type.getSimpleName();
        }
        String methodName = "get" + typeName;
        switch (methodName) {
            case "getBoolean":
                methodName = "getInt";
                break;
            case "getChar":
            case "getCharacter":
                methodName = "getString";
                break;
            case "getDate":
                methodName = "getLong";
                break;
            case "getInteger":
                methodName = "getInt";
                break;
        }
        return methodName;
    }


    @Override
    public int delete(String whereClause, String[] whereArgs) {
        return mSqLiteDatabase.delete(DaoUtils.getTableName(mClazz), whereClause, whereArgs);
    }

    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues contentValues = objToValue(obj);
        return mSqLiteDatabase.update(DaoUtils.getTableName(mClazz), contentValues, whereClause, whereArgs);
    }


}
