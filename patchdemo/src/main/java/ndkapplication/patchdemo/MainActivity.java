package ndkapplication.patchdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("bspatch");
    }

    private String mPatchPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "version_1.0_2.0.patch";

    private String mNewApkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "version2.0.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        update();
    }

    private void update() {
        //1、访问后台接口，需不需要更新版本


        //2、需要更新版本，要么提示用户下载，要么偷偷下载，然后提示用户


        //3、下载完差分包之后，调用我们的方法合并生成新的apk,耗时操作
        if (!new File(mPatchPath).exists()) {
            return;
        }
        PathUtils.combine(getPackageResourcePath(), mNewApkPath, mPatchPath);

        //4、需要校验签名 获取本地apk签名与新的apk做对比



        //5、安装最新版本
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(mNewApkPath)),
                "application/vnd.android.package-archive");
        startActivity(intent);

    }




}
