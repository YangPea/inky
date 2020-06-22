package com.example.inky.adapter;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inky.R;
import com.example.inky.util.JsonResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.GridViewHolder> {

    private List<JsonResult.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public PicAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new PicAdapter.GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PicAdapter.GridViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.text);
        JsonResult.DataBean dataBean = data.get(position);
        textView.setText(dataBean.getTitle());
        ImageView imageView = holder.itemView.findViewById(R.id.image);
//        Log.v("@@@@@@@@@@", "picUrl: "+ dataBean.getContent());
        Picasso.get().load("http://47.102.141.25:10000/images/"+dataBean.getContent()).into(imageView);
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

    public class GridViewHolder extends RecyclerView.ViewHolder {
        public GridViewHolder(@NonNull View itemView) {
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
    private PicAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(PicAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    //监听点击接口
}
