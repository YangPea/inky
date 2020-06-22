package com.example.inky.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inky.R;
import com.example.inky.ui.add.ResourceDetail;
import com.example.inky.util.JsonResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.InnerHolder> {

    private List<JsonResult.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView title = holder.itemView.findViewById(R.id.resource_title);
        JsonResult.DataBean dataBean = data.get(position);
        title.setText(dataBean.getTitle());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(JsonResult jsonResult) {
        data.clear();
        data.addAll(jsonResult.getData());
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener !=null) {
                        onItemClickListener.onItemClick(v,data.get(getLayoutPosition()).getContent());
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        public void onItemClick(View view,String data);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    //监听点击接口
}

