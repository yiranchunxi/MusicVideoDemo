package com.siasun.musicvideo.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.siasun.musicvideo.R;
import com.siasun.musicvideo.adapter.BaseReAdapter;
import com.siasun.musicvideo.adapter.MusicAdater;
import com.siasun.musicvideo.api.http.OnLineMusicModel;
import com.siasun.musicvideo.app.BaseAppHelper;
import com.siasun.musicvideo.app.BaseApplication;
import com.siasun.musicvideo.base.view.BaseActivity;
import com.siasun.musicvideo.executor.online.AbsPlayOnlineMusic;
import com.siasun.musicvideo.fragment.PlayMusicFragment;
import com.siasun.musicvideo.model.bean.AudioBean;
import com.siasun.musicvideo.model.bean.OnlineMusicList;
import com.siasun.musicvideo.service.PlayService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.ycbjie.ycthreadpoollib.PoolThread;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener ,EasyPermissions.PermissionCallbacks{

    @BindView(R.id.rv_music_list)
    RecyclerView rv_music_list;

    private int mOffset = 1;
    private static final String MUSIC_LIST_SIZE = "10";

    private boolean isPlayFragmentShow = false;
    private PlayMusicFragment mPlayFragment;
    private MusicAdater  mAdapter;
    private String type = "1";

    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };
    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("MainActivity","onServiceConnected"+name);
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            BaseAppHelper.get().setPlayService(playService);
            //scanMusic(playService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("MainActivity","onServiceDisconnected"+name);
        }
    }

    private PlayServiceConnection mPlayServiceConnection;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        initPermissions();

        rv_music_list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new MusicAdater(this);
        rv_music_list.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseReAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if( mAdapter.getDatas().size()>position && position>-1){
                    OnlineMusicList.SongListBean onlineMusic = mAdapter.getDatas().get(position);
                    //准备播放列表
                    prepareMusicList(mAdapter.getDatas());


                    playMusic(onlineMusic,position);
                }
            }
        });
    }

    private void prepareMusicList(List<OnlineMusicList.SongListBean> datas) {

        List<AudioBean> list=new ArrayList<>();


        for(int i=0;i<datas.size();i++){

            AudioBean music=new AudioBean();
            String artist = datas.get(i).getArtist_name();
            String title = datas.get(i).getTitle();
            music.setId(datas.get(i).getSong_id());
            music.setType(AudioBean.Type.ONLINE);
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(datas.get(i).getAlbum_title());
            music.setSongId(datas.get(i).getSong_id());
            list.add(music);

        }


         BaseAppHelper.get().setMusicList(list);
         getPlayService().updateMusicList();
    }

    private void playMusic(final  OnlineMusicList.SongListBean onlineMusic, final int position) {

        new AbsPlayOnlineMusic(this,position) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onExecuteSuccess() {
                getPlayService().play(position);
                showPlayingFragment();
                Toast.makeText(MainActivity.this,"正在播放" + onlineMusic.getTitle(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExecuteFail(Exception e) {
                Toast.makeText(MainActivity.this,BaseApplication.getInstance().getResources().getString(R.string.unable_to_play),Toast.LENGTH_SHORT).show();
            }
        }.execute();



    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getSongListInfo(OnLineMusicModel.METHOD_GET_MUSIC_LIST,
                type, MUSIC_LIST_SIZE, String.valueOf("0"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OnlineMusicList>() {
                    @Override
                    public void accept(OnlineMusicList onlineMusicList) throws Exception {
                        if (onlineMusicList == null || onlineMusicList.getSong_list() == null || onlineMusicList.getSong_list().size() == 0) {
                            return;
                        }
                        mAdapter.clear(onlineMusicList.getSong_list());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        super.onBackPressed();
    }
    /**
     * 展示页面
     */
    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = PlayMusicFragment.newInstance("OnLine");
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    /**
     * 隐藏页面
     */
    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }



    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void initPermissions() {
        //检查是否获取该权限
        if (hasPermissions()) {
            startCheckService();
            //具备权限 直接进行操作
        } else {
            //权限拒绝 申请权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this,
                    getString(R.string.easy_permissions), RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }

    /**
     * 判断是否添加了权限
     *
     * @return true
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS);
    }
    /**
     * 检测服务
     */
    private void startCheckService() {
        if (BaseAppHelper.get().getPlayService() == null) {
            startService();
            PoolThread executor = BaseApplication.getInstance().getExecutor();
            executor.setName("startCheckService");
            executor.setDelay(1, TimeUnit.SECONDS);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    //绑定服务
                    bindService();
                }
            });
        }
    }

    /**
     * 开启服务
     */
    private void startService() {
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }

    /**
     * 绑定服务
     * 注意对于绑定服务一定要解绑
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }



    /**
     * 将结果转发到EasyPermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 某些权限已被授予
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //某些权限已被授予
        Log.d("权限", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }


    /**
     * 某些权限已被拒绝
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //某些权限已被拒绝
        Log.d("权限", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(MainActivity.this, perms)) {
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(MainActivity.this);
            builder.setTitle("允许权限")
                    .setRationale("没有该权限，此应用程序部分功能可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setPositiveButton("去设置")
                    .setNegativeButton("取消")
                    .setRequestCode(RC_LOCATION_CONTACTS_PERM)
                    .build()
                    .show();
        }else {
            Toast.makeText(MainActivity.this,"没有存储空间权限,无法播放本地歌曲",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
