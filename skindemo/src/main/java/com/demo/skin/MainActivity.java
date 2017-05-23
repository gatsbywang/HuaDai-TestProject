package com.demo.skin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.demo.skin.skin.SkinManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends BaseSkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_change_skin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String skinPath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + File.separator + "change.skin";
                SkinManager.getInstance().loadSkin(skinPath);
            }
        });

        findViewById(R.id.tv_change_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().restoreDefault();
            }
        });

        findViewById(R.id.tv_new_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void changeSkin() {
        ImageView imageView = (ImageView) findViewById(R.id.image_iv);
        try {
            Resources superResource = getResources();
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath",
                    String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(assetManager, Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "change.skin");
            Resources resource = new Resources(assetManager, superResource.getDisplayMetrics(),
                    superResource.getConfiguration());
            int resId = resource.getIdentifier("image_src", "drawable", "com.demo.skin");
            Drawable drawable = resource.getDrawable(resId);
            imageView.setImageDrawable(drawable);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
