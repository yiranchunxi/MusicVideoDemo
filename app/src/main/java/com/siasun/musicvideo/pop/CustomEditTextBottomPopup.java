package com.siasun.musicvideo.pop;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lxj.xpopup.core.BottomPopupView;
import com.siasun.musicvideo.R;

/**
 * Description: 带有输入框的Bottom弹窗
 * Create by dance, at 2019/2/27
 */
public class CustomEditTextBottomPopup extends BottomPopupView {
    private EditText et;
    private Button btn_finish;
    public CustomEditTextBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_edittext_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        et=findViewById(R.id.et_comment);
        btn_finish=findViewById(R.id.btn_finish);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("test","beforeTextChanged"+s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("test","onTextChanged"+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("test","afterTextChanged"+s.toString());

                if(TextUtils.isEmpty(s)){

                    btn_finish.setTextColor(Color.parseColor("#d9d9d9"));
                    btn_finish.setClickable(false);
                }else{
                    btn_finish.setTextColor(Color.parseColor("#de6500"));
                    btn_finish.setClickable(true);
                }
            }
        });


        btn_finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test","发布评论。。。。");
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
//        Log.e("tag", "CustomEditTextBottomPopup  onShow");

    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
//        Log.e("tag", "CustomEditTextBottomPopup  onDismiss");
    }

    public String getComment(){
        return et.getText().toString();
    }

//    @Override
//    protected int getMaxHeight() {
//        return (int) (XPopupUtils.getWindowHeight(getContext())*0.75);
//    }
}
