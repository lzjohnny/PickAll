package cn.edu.xidian.sast.baselib.okhttp.exception;

import java.util.Locale;

/**
 * Created by ShiningForever on 2017/7/8.
 */

public class OkHttpException extends Exception {
    private static final long serialVersionUID = 1L;

    private String url;
    private int errCode;
    private Object errMsg;

    public OkHttpException(int errCode, Object errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public OkHttpException(String mUrl, int errCode, Object errMsg) {
        this.url = mUrl;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public Object getErrMsg() {
        return errMsg;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "[code:%d] [url:%s] [message:%s]", errCode, url, errMsg);
    }
}
