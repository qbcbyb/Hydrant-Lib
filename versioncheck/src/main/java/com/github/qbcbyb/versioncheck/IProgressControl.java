package com.github.qbcbyb.versioncheck;

/**
 * Created by qbcby on 2016/7/29.
 */
public interface IProgressControl {
    void progressStart();

    void progressEnd();

    void progressUpdate(long progress);
}
