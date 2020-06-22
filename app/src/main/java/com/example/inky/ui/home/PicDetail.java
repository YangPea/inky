package com.example.inky.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inky.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class PicDetail extends AppCompatActivity {

    String content;
    Bitmap imgbitmap;
    ImageView picDetail;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        verifyStoragePermissions(PicDetail.this);
        Intent intent=getIntent();
        content=intent.getStringExtra("content");
        picDetail = (ImageView) findViewById(R.id.pic_detail);
        Picasso.get().load(content).into(picDetail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.download) {
            Toast.makeText(this, "download", Toast.LENGTH_SHORT).show();
            Picasso.get().load(content).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imgbitmap=bitmap;
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            Log.v("----------conten----------", "onOptionsItemSelected: "+content);
            Log.v("----------bitmap-----------", "onOptionsItemSelected: "+imgbitmap);
            saveBitmap(imgbitmap);
            picDetail.startAnimation(shakeAnimation());
            Toast.makeText(this, "图片下载完成", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    public void saveBitmap(Bitmap bitmap) {
        String savePath;
        File filePic;
        String ran = getRandomString(10);
        savePath =  "/storage/emulated/0/inkyPhoto/"+ran;
        Log.d("----------------->>>", "saveBitmap: "+savePath+ "2.jpg");
        try {
            filePic = new File(savePath + "2.jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("xxx", "saveBitmap: 2return"+e);
            return;
        }
//        Log.d("xxx", "saveBitmap: " + filePic.getAbsolutePath());
    }
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static Animation shakeAnimation() {
        Animation rotateAnimation = new  RotateAnimation(0, 359,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new AnticipateInterpolator());
        rotateAnimation.setDuration(700);
        return rotateAnimation;
    }


}