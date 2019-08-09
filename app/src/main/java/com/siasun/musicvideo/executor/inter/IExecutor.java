package com.siasun.musicvideo.executor.inter;


public interface IExecutor<T> {

    void execute();
    void onPrepare();
    void onExecuteSuccess(T t);
    void onExecuteFail(Exception e);

}
