package com.example.inky.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inky.R;
import com.example.inky.api.ImageApi;
import com.example.inky.api.UploadApi;
import com.example.inky.util.BaseUrl;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Url;

public class Publish extends AppCompatActivity {

    private AlertDialog alertDialog;
    private Bitmap photo;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_PIC = 2;
    private ImageView imageView;
    private TextView textView;
    String title;
    private Uri imageUri;
    String imgPath;

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
        setContentView(R.layout.activity_publish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView addBtn = (ImageView)findViewById(R.id.pic_btn);
        imageView = (ImageView)findViewById(R.id.pic_btn);
        verifyStoragePermissions(Publish.this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.submit_btn) {
//            Log.d("*********", "onActivityResult: "+photo);
            textView = (TextView)findViewById(R.id.title_input);
            title = textView.getText().toString();
            Log.v(">>>>>>>>>", "onOptionsItemSelected: "+title);
            if(imgPath == null||title == null||title==""){
                Toast.makeText(this, "图片和文字都要有哦", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "正在提交", Toast.LENGTH_SHORT).show();
                upload();
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public void showList(){
        final String[] items = {"拍照", "从相册中获取"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("上传图片");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i]=="拍照") {
                    openTakePhoto();
                }
                if(items[i]=="从相册中获取") {
                    goAlbums();
                }
                alertDialog.dismiss();
            }        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void openTakePhoto(){
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, REQUEST_CAMERA);
        }else {
            Toast.makeText(Publish.this,"sdcard不可用",Toast.LENGTH_SHORT).show();
        }
    }
    private void goAlbums() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PIC);
    }

    private void  upload() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl.baseUrl).build();
        UploadApi uploadApi = retrofit.create(UploadApi.class);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        File file=new File(imgPath);
//        Log.d("-------TITLE--------", "onCreate: "+title);
//        Log.d("0000000", "upload: "+String.valueOf(imageUri));
        RequestBody body=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        builder.addFormDataPart("file",file.getName(),body);
        builder.addFormDataPart("title", title);
        List<MultipartBody.Part> parts=builder.build().parts();
        Call<ResponseBody> task = uploadApi.postFileWithParams(parts);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT).show();
                Log.d("YES-----------", "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("NO---------", "onResponse: "+t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("===", "onActivityResult: "+data.getExtras());
        if (data!= null) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    Log.d("_______________", "onActivityResult: ");
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        photo = (Bitmap) bundle.get("data");
                        imageView.setImageBitmap(photo);
                        saveBitmap(photo);
                        Log.d("?????????????", "url:"+imageUri);
                        imgPath = String.valueOf(imageUri);
                    } else {
                        Toast.makeText(Publish.this, "找不到图片", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case SELECT_PIC:
                    imageUri = data.getData();
                    String[] proj = { MediaStore.Images.Media.DATA };
                    Cursor cursor = managedQuery(imageUri,proj,null,null,null);
                    int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //游标跳到首位，防止越界
                    cursor.moveToFirst();
                    imgPath = cursor.getString(actual_image_column_index);
                    Log.d("++++++++++++", "onActivityResult: "+imageUri);
                    Log.d("------------", "onActivityResult: "+imgPath);
                    Picasso.get().load(imageUri).resize(200,200).into(imageView);
                    break;
            }
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        String savePath;
        File filePic;
        savePath =  getFilesDir() + "/files/";
        Log.d("000000000", "saveBitmap: "+savePath + "1.jpg");
        try {
            filePic = new File(savePath + "1.jpg");
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
        Log.d("xxx", "saveBitmap: " + filePic.getAbsolutePath());
        imageUri = Uri.parse(filePic.getAbsolutePath());
    }


}