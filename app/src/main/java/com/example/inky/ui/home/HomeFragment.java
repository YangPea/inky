package com.example.inky.ui.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inky.adapter.PicAdapter;
import com.example.inky.adapter.ResourceAdapter;
import com.example.inky.api.ImageApi;
import com.example.inky.api.ResourceApi;
import com.example.inky.R;
import com.example.inky.ui.add.ResourceDetail;
import com.example.inky.util.BaseUrl;
import com.example.inky.util.JsonResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView mResultList;
    private PicAdapter mAdapter;
    private JsonResult jsonResult;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRequest(null);
        initView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        getRequest(null);

    }

    private void initView() {
        mResultList = getView().findViewById(R.id.pic_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL );
        mResultList.setLayoutManager(gridLayoutManager);


        mResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 14;
//                outRect.bottom = 10;
                outRect.left = 7;
                outRect.right = 7;
            }
        });
        mAdapter = new PicAdapter();
        mResultList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Log.v("!!!!!!!", "onItemClick: "+data);
                Intent intent = new Intent(getActivity(), PicDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("content","http://47.102.141.25:10000/images/"+data);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });


        FloatingActionButton btn = (FloatingActionButton)getView().findViewById(R.id.float_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Publish.class);
                startActivity(intent);
            }
        });
    }

    public void getRequest(View view) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl.baseUrl).build();
        ImageApi imageApi = retrofit.create(ImageApi.class);
        Call<ResponseBody> task = imageApi.getJson();
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == HttpURLConnection.HTTP_OK) {

                    try {
//                        Log.v("000000000000000000000", "onResponse: "+ response.body().string());
                        String result = response.body().string();
                        Gson gson = new Gson();
                        jsonResult = gson.fromJson(result,JsonResult.class);
//                        转json到bean
                        updateList(jsonResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("22222222222", "onFailure: "+t.toString());
            }

        });
        return;
    }

    private void updateList(JsonResult jsonResult) {
        mAdapter.setData(jsonResult);
    }



}
