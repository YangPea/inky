package com.example.inky.ui.my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.inky.R;
import com.squareup.picasso.Picasso;

public class MyFragment extends Fragment {

    private MyViewModel myViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        myViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView avator = (ImageView)getView().findViewById(R.id.avator);
        TextView name = (TextView)getView().findViewById(R.id.my_name);
        Picasso.get().load(R.drawable.avator).transform(new CircleTransform()).into(avator);
        name.setText("打脸计时器");
        name.startAnimation(alphaAnimation());
        Button about = (Button) getView().findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("关于");
                builder.setMessage("Inky——一款书法App");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

    }
    public static Animation alphaAnimation() {
        Animation animation = new AlphaAnimation(0,1);
        animation.setInterpolator(new AnticipateInterpolator());
        animation.setDuration(900);
        return animation;
    }
}
