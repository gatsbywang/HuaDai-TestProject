package ndkapplication.patchdemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by 花歹 on 2017/8/28.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class PathUtils {

    /**
     * @param oldApkPath 原来的apk
     * @param newApkPath 合并后新的apk路径
     * @param patchPath  差分包路径，从服务器上下载
     */
    public static native void combine(String oldApkPath, String newApkPath, String patchPath);


    /**
     * 校验签名
     *
     * @param context
     * @return
     */
    public static String getSignature(Context context) {
        //1、通过Context获取当前包名
        String currentApkPackName = context.getApplicationInfo().packageName;

        //2、通过PackageManager获取所有的PackageInfo信息
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (PackageInfo packageInfo : packageInfos) {
            if (packageInfo.packageName.equals(currentApkPackName)) {
                return packageInfo.signatures[0].toCharsString();
            }
        }
        return null;
    }

    /**
     * 获取未安装的apk签名
     *
     * @param path
     * @return
     */
    public static String getSinature(String path) {
        try {
            //1、反射获取PackageParse对象
            Object packageParser = getPackageParser(path);
            //2、反射获取PackageParse.Package
            Object packageObject = getPackage(path, packageParser);
            //3、获取collectCertificates方法
            Method collectCertificatesMethod = packageObject.getClass().getDeclaredMethod("collectCertificates",
                    packageObject.getClass(), int.class);
            collectCertificatesMethod.invoke(packageParser, packageObject, 0);

            //4、获取mSinatures属性
            Field signaturesField = packageObject.getClass().getDeclaredField("mSignatures");
            signaturesField.setAccessible(true);
            Signature[] mSignatures = (Signature[]) signaturesField.get(packageObject);
            return mSignatures[0].toCharsString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取PackageParser.Package
     *
     * @param path
     * @param packageParser
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static Object getPackage(String path, Object packageParser) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= 21) {
            //1、获取parsePackage方法
            Class<?>[] paramObjects = new Class<?>[2];
            paramObjects[0] = File.class;
            paramObjects[1] = int.class;
            Method parsePackageMethod = packageParser.getClass().getDeclaredMethod("parsePackage", paramObjects);
            //2、反射执行parsePackage方法
            Object[] params = new Object[2];
            params[0] = new File(path);
            params[1] = 0;
            parsePackageMethod.setAccessible(true);
            return parsePackageMethod.invoke(packageParser, params);
        } else {
            //1、获取parsePackage方法
            Class<?>[] paramObjects = new Class<?>[2];
            paramObjects[0] = File.class;
            paramObjects[1] = String.class;
            paramObjects[2] = DisplayMetrics.class;
            paramObjects[3] = int.class;
            Method parsePackageMethod = packageParser.getClass().getDeclaredMethod("parsePackage", paramObjects);

            //2、反射执行parsePackage方法
            Object[] params = new Object[4];
            params[0] = new File(path);
            params[1] = path;
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            params[2] = metrics;
            params[3] = 0;
            parsePackageMethod.setAccessible(true);
            return parsePackageMethod.invoke(packageParser, params);
        }
    }

    private static Object getPackageParser(String path) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> packageParserClazz = Class.forName("android.content.pm.PackageParser");
        if (Build.VERSION.SDK_INT >= 21) {
            Constructor<?> packageParserConstructor = packageParserClazz.getDeclaredConstructor();
            return packageParserConstructor.newInstance();
        } else {
            Constructor<?> packageParserConstructor = packageParserClazz.getDeclaredConstructor(String.class);
            return packageParserConstructor.newInstance(path);
        }
    }


}
