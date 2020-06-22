package com.example.inky.ui.add;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inky.api.ResourceApi;
import com.example.inky.R;
import com.example.inky.adapter.ResourceAdapter;
import com.example.inky.util.BaseUrl;
import com.example.inky.util.JsonResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;
    private RecyclerView mResultList;
    private ResourceAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRequest(null);
        initView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel =
                ViewModelProviders.of(this).get(AddViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        addViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {}
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getRequest(null);
    }

    private void initView() {
        mResultList = getView().findViewById(R.id.result_list);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ResourceAdapter();
        mResultList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ResourceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Log.v("!!!!!!!", "onItemClick: "+data);
                Intent intent = new Intent(getActivity(), ResourceDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("content",data);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
//        mResultList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mResultList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 7;
                outRect.bottom = 7;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
    }


    public void getRequest(View view) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl.url).build();
        ResourceApi resourceApi = retrofit.create(ResourceApi.class);
        Call<ResponseBody> task = resourceApi.getJson();
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("------", "onResponse: "+ response.code());
                if (response.code() == HttpURLConnection.HTTP_OK) {

                    try {
//                        Log.v("1111111", "onResponse: "+response.body().string());
                        String result = response.body().string();
//                        Log.v("[[[[[[[[[", "result: "+ result);
                        Gson gson = new Gson();
                        JsonResult jsonResult = gson.fromJson(result,JsonResult.class);
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
