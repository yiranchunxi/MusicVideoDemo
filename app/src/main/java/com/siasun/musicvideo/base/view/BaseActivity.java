package com.siasun.musicvideo.base.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.siasun.musicvideo.R;
import com.siasun.musicvideo.activity.GuideActivity;
import com.siasun.musicvideo.app.BaseAppHelper;
import com.siasun.musicvideo.app.callback.AppManager;
import com.siasun.musicvideo.base.mvp.BasePresenter;
import com.siasun.musicvideo.constant.BaseConfig;
import com.siasun.musicvideo.service.PlayService;
import com.siasun.musicvideo.utils.NetworkUtils;
import com.siasun.musicvideo.utils.StateAppBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;
    private Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (BaseConfig.INSTANCE.isNight()) {
            setTheme(getDarkTheme());
        }
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        unbinder= ButterKnife.bind(this);
        //避免切换横竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StateAppBar.setStatusBarColor(this, R.color.redTab);
        if (mPresenter != null){
            mPresenter.subscribe();
        }
        initView();
        initListener();
        if(!NetworkUtils.isConnected()){
            Toast.makeText(this,"请检查网络是否连接",Toast.LENGTH_SHORT).show();
        }
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.unSubscribe();
        }
        //测试内存泄漏，正式一定要隐藏
        initLeakCanary();
        unbinder.unbind();
    }


    /** 返回一个用于显示界面的布局id */
    public abstract int getContentView();

    /** 初始化View的代码写在这个方法中 */
    public abstract void initView();

    /** 初始化监听器的代码写在这个方法中 */
    public abstract void initListener();

    /** 初始数据的代码写在这个方法中，用于从服务器获取数据 */
    public abstract void initData();

    @StyleRes
    protected int getDarkTheme() {
        return R.style.AppThemeDark;
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 用来检测所有Activity的内存泄漏
     */
    private void initLeakCanary() {
        /*RefWatcher refWatcher = BaseApplication.getRefWatcher(this);
        refWatcher.watch(this);*/
    }

    /**
     * 获取到播放音乐的服务
     * @return              PlayService对象
     */
    public PlayService getPlayService () {
        PlayService playService = BaseAppHelper.get().getPlayService();
        if (playService == null) {
            //待解决：当长期处于后台，如何保活？避免service被杀死……
            //throw new NullPointerException("play service is null");
            checkServiceAlive();
        }
        return playService;
    }

    protected boolean checkServiceAlive() {
        if (BaseAppHelper.get().getPlayService() == null) {
            startActivity(new Intent(this, GuideActivity.class));
            AppManager.getAppManager().finishAllActivity();
            return false;
        }
        return true;
    }


}
