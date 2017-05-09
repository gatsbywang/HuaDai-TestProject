package myapplication.dbdemo;

/**
 * Created by 花歹 on 2017/5/9.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class DaoUtils {

    public static String getColumnType(String type) {
        String value = null;
        switch (type) {
            case "String":
                value = " text";
                break;
            case "int":
            case "Integer":
                value = " integer";
                break;
            case "boolean":
            case "Boolean":
                value = " boolean";
                break;
            case "float":
            case "Float":
                value = " float";
                break;
            case "double":
            case "Double":
                value = " double";
                break;
            case "char":
            case "Character":
                value = " varchar";
                break;
            case "long":
            case "Long":
                value = " long";
                break;

        }
//        if (value == null) {
//            throw new IllegalArgumentException(type + " not support! ");
//        }
        return value;
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
