package com.siasun.musicvideo.pop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.siasun.musicvideo.R;
import com.siasun.musicvideo.adapter.BaseReAdapter;
import com.siasun.musicvideo.adapter.CommentAdapter;


import java.util.ArrayList;

/**
 * Description: 仿知乎底部评论弹窗
 * Create by dance, at 2018/12/25
 */
public class ZhihuCommentPopup extends BottomPopupView {
    VerticalRecyclerView recyclerView;
    private ArrayList<String> data;
    private CommentAdapter commentAdapter;

    public ZhihuCommentPopup(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.tv_temp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出新的弹窗用来输入
                final CustomEditTextBottomPopup textBottomPopup = new CustomEditTextBottomPopup(getContext());
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
//                        .hasShadowBg(false)
                        .setPopupCallback(new XPopupCallback() {
                            @Override
                            public void onShow() { }
                            @Override
                            public void onDismiss() {
                                /*String comment = textBottomPopup.getComment();
                                if(!comment.isEmpty()){
                                    data.add(0,comment);
                                    commonAdapter.notifyDataSetChanged();
                                }*/
                            }
                        })
                        .asCustom(textBottomPopup)
                        .show();
            }
        });

        data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add("这是一个自定义Bottom类型的弹窗！你可以在里面添加任何滚动的View，我已经智能处理好嵌套滚动，你只需编写UI和逻辑即可！");
        }
        commentAdapter=new CommentAdapter(getContext());
        commentAdapter.setOnItemClickListener(new BaseReAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //可以等消失动画执行完毕再开启新界面
                dismissWith(new Runnable() {
                    @Override
                    public void run() {
                        //getContext().startActivity(new Intent(getContext(), DemoActivity.class));
                    }
                });
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commentAdapter);

        commentAdapter.clear(data);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }
}