package com.siasun.musicvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.siasun.musicvideo.R;
import com.siasun.musicvideo.model.bean.LiveBean;
import com.siasun.musicvideo.utils.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class LiveAdapter extends BaseReAdapter {

    private List<LiveBean> datas=new ArrayList<>();

    private Context mContext;


    public LiveAdapter(Context context){
        this.mContext=context;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live,parent,false);
        return  new LiveViewHolder(view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LiveViewHolder){
            LiveViewHolder liveViewHolder= (LiveViewHolder) holder;


            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.transforms(new GlideRoundTransform(this,10));
                    .dontTransform();
            Glide.with(mContext)
                    .load(datas.get(position).getCoverUrl())
                    .apply(options)
                    .into(liveViewHolder.iv_cover);

        }

    }

    @Override
    public int getItemCount() {
        return datas != null ? (datas.size() > 0 ? datas.size() : 0) :0;
    }

    private class LiveViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_liked_count;
        private TextView tv_commented_count;
        private TextView tv_see_count;
        private TextView tv_lived_time;
        private RoundImageView iv_cover;
        public  LiveViewHolder(final View itemView) {
            super(itemView);
            tv_liked_count=itemView.findViewById(R.id.tv_liked_count);
            tv_commented_count=itemView.findViewById(R.id.tv_commented_count);
            tv_see_count=itemView.findViewById(R.id.tv_see_count);
            tv_lived_time=itemView.findViewById(R.id.tv_lived_time);
            iv_cover=itemView.findViewById(R.id.iv_cover);
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


    public List<LiveBean> getDatas() {
        return datas;
    }

    public void setData(List<LiveBean> newData){
        this.datas.addAll(newData);
        notifyDataSetChanged();
    }

    public void clear(List<LiveBean> newData){
        if(this.datas!=null){
            this.datas.clear();
        }
        this.datas.addAll(newData);
        notifyDataSetChanged();
    }

}
