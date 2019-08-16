package com.siasun.musicvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siasun.musicvideo.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends BaseReAdapter {

    private List<String> datas=new ArrayList<>();



    private Context mContext;


    public CommentAdapter(Context context){
        this.mContext=context;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return  new  CommentHolder(view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof  CommentHolder){
            CommentHolder commentHolder= ( CommentHolder) holder;

        }

    }

    @Override
    public int getItemCount() {
        return datas != null ? (datas.size() > 0 ? datas.size() : 0) :0;
    }

    private class CommentHolder extends RecyclerView.ViewHolder{


        public  CommentHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlistener!=null){
                        mlistener.onItemClick(itemView,getPosition());
                    }
                }
            });
        }
    }


    public List<String> getDatas() {
        return datas;
    }

    public void setData(List<String> newData){
        this.datas.addAll(newData);
        notifyDataSetChanged();
    }

    public void clear(List<String> newData){
        if(this.datas!=null){
            this.datas.clear();
        }
        this.datas.addAll(newData);
        notifyDataSetChanged();
    }


}
