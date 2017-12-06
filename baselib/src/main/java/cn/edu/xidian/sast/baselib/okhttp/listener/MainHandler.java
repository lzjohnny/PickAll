package cn.edu.xidian.sast.baselib.okhttp.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;

import cn.edu.xidian.sast.baselib.okhttp.exception.OkHttpException;
import cn.edu.xidian.sast.baselib.okhttp.request.NetConst;

public class MainHandler extends Handler {

    private static final int FAILURE = 1010101;
    private static final int SUCCESS = 1010102;

    public MainHandler() {
        super(Looper.getMainLooper());
    }

    public <T> void notifySuccess(T response, HttpResponseHandler<T> target) {
        Message message = obtainMessage(SUCCESS);
        message.obj = new Pair<>(response, target);
        sendMessage(message);
    }

    public <T> void notifyFail(OkHttpException exception, HttpResponseHandler<T> target) {
        Message message = obtainMessage(FAILURE);
        message.obj = new Pair<>(exception, target);
        sendMessage(message);
    }

    @Override
    public void handleMessage(Message msg) {
        int what = msg.what;
        HttpResponseHandler handler;
        switch (what) {
            case FAILURE:
                Pair<OkHttpException, HttpResponseHandler> pair = (Pair<OkHttpException, HttpResponseHandler>) msg.obj;
                handler = pair.second;
                if (handler != null) {
                    handler.onFailure(pair.first);
                }
                break;
            case SUCCESS:
                Pair<Object, HttpResponseHandler> callbackPair = (Pair<Object, HttpResponseHandler>) msg.obj;
                handler = callbackPair.second;
                if (handler != null) {
                    try {
                        handler.onSuccess(callbackPair.first);
                    } catch (Exception exception) {
                        handler.onFailure(new OkHttpException(NetConst.UNKNOWN, exception.getMessage()));
                    }
                }
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
