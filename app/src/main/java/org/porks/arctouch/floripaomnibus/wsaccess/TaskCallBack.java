package org.porks.arctouch.floripaomnibus.wsaccess;

/**
 * Interface to have a callback after the ASyncTask
 */
public interface TaskCallBack {
    void onComplete(TaskResult result);
}
