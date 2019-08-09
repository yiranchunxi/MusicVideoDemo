package com.siasun.musicvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siasun.musicvideo.R;
import com.siasun.musicvideo.model.bean.OnlineMusicList;

import java.util.ArrayList;
import java.util.List;

public class MusicAdater extends BaseReAdapter{


    private List<OnlineMusicList.SongListBean> datas=new ArrayList<>();



    private Context mContext;


    public MusicAdater(Context context){
        this.mContext=context;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false);
        return  new MusicViewHolder(view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MusicViewHolder){
            MusicViewHolder musicViewHolder= (MusicViewHolder) holder;
            musicViewHolder.tv_music_title.setText(datas.get(position).getTitle());
            musicViewHolder.tv_music_author.setText(datas.get(position).getArtist_name());
            musicViewHolder.tv_music_listened.setText(datas.get(position).getHot());
        }

    }

    @Override
    public int getItemCount() {
        return datas != null ? (datas.size() > 0 ? datas.size() : 0) :0;
    }

    private class MusicViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_music_title;
        private TextView tv_music_author;
        private TextView tv_music_listened;
        private TextView tv_current_time;
        private TextView tv_total_time;
        public  MusicViewHolder(final View itemView) {
            super(itemView);
            tv_music_title=itemView.findViewById(R.id.tv_music_title);
            tv_music_author=itemView.findViewById(R.id.tv_music_author);
            tv_music_listened=itemView.findViewById(R.id.tv_music_listened);
            tv_current_time=itemView.findViewById(R.id.tv_current_time);
            tv_total_time=itemView.findViewById(R.id.tv_total_time);
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


    public List<OnlineMusicList.SongListBean> getDatas() {
        return datas;
    }

    public void setData(List<OnlineMusicList.SongListBean> newData){
        this.datas.addAll(newData);
        notifyDataSetChanged();
    }

    public void clear(List<OnlineMusicList.SongListBean> newData){
        if(this.datas!=null){
            this.datas.clear();
        }
        this.datas.addAll(newData);
        notifyDataSetChanged();
    }
}
