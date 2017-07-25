package com.demo.ndk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int finalWidth =800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

        String filePath = "";
        //这个方法可能会造成内存溢出
        BitmapFactory.Options options = new BitmapFactory.Options();
        //不加载图片到内存，只能宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        int bitmapWidth = options.outWidth;
        int inSampleSize = 1;
        if(bitmapWidth > finalWidth){
            inSampleSize = bitmapWidth/finalWidth;
        }
        options.inSampleSize =inSampleSize;
        options.inJustDecodeBounds = false;
        String outputfile = "";
        //缩放下再进行加载成bitmap,避免内存溢出
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
        compressBitmap(bitmap, 30, outputfile);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();

    /**
     * @param bitmap
     * @param quality  30 则压缩到1/10
     * @param fileName
     * @return
     */
    public native static int compressBitmap(Bitmap bitmap, int quality, String fileName);

    // Used to load the 'native-lib' library on application startup.
    static {
//        System.loadLibrary("native-lib");
//        System.loadLibrary("libjpeg-image-compress-lib");
        System.loadLibrary("libcompressimg");
    }
}
