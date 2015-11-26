package org.porks.arctouch.floripaomnibus.wsaccess;

import org.json.JSONObject;

/**
 * A simple task result that we use after the ASyncTask
 */
public class TaskResult {
    /**
     * Task's status
     */
    private int status = TaskResult.STS_UNKNOWN;

    /**
     * Status unknown
     */
    public static final int STS_UNKNOWN = 1;

    /**
     * The task completed successfully
     */
    public static final int STS_OK = 2;

    /**
     * An error has occurred
     */
    public static final int STS_ERR = 3;

    /**
     * Error message
     */
    private String errMsg = "";

    /**
     * JSON Object representing the return of the task
     */
    private JSONObject jsonObj = null;

    public int getStatus() {
        return this.status;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        this.status = TaskResult.STS_ERR;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setJsonObj(JSONObject jsonObj) {
        this.jsonObj = jsonObj;
        this.status = TaskResult.STS_OK;
    }

    public JSONObject getJsonObj() {
        return this.jsonObj;
    }
}
