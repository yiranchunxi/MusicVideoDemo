package com.siasun.musicvideo.executor.inter;


public interface IExecutor<T> {

    void execute();
    void onPrepare();
    void onExecuteSuccess();
    void onExecuteFail(Exception e);

}
