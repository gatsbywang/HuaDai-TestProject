package myapplication.dbdemo;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 花歹 on 2017/5/8.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought: 定义Dao的规范
 */

public interface IDaoSupport<T> {

    /**
     * 初始化
     * @param sqLiteDatabase
     * @param clazz
     */
    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);
    /**
     * 插入数据
     * @param t
     * @return
     */
    public long insert(T t);
}
