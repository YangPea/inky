package com.example.inky.ui.add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.example.inky.R;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

public class ResourceDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        TextView textView = (TextView) findViewById(R.id.content);
        textView.setText(content);
//        Log.v("wwwwwwwwwwwww", "onCreate: "+content);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
