package cn.edu.xidian.sast.baselib.okhttp.listener;

import android.os.Looper;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.edu.xidian.sast.baselib.okhttp.exception.OkHttpException;
import cn.edu.xidian.sast.baselib.okhttp.request.HttpRequest;
import cn.edu.xidian.sast.baselib.okhttp.request.NetConst;
import cn.edu.xidian.sast.baselib.okhttp.utils.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class HttpResponseHandler<T> extends TypeToken<T> implements Callback {

    private static final String TAG = HttpResponseHandler.class.getSimpleName();
    private static final String DEFAULT_CHARSET = "UTF-8";

    private String responseCharset = DEFAULT_CHARSET;
    private HttpRequest<T> mRequest = null;

    private boolean mCallbackOnMainThread = false;

    public HttpResponseHandler() {
        this(Looper.getMainLooper() == Looper.myLooper());
    }

    public HttpResponseHandler(boolean callOnMainThread) {
        mCallbackOnMainThread = callOnMainThread;
    }

    public void setRequest(HttpRequest<T> request) {
        mRequest = request;
    }

    public void setCharset(final String charset) {
        this.responseCharset = charset;
    }

    public String getCharset() {
        return this.responseCharset == null ? DEFAULT_CHARSET : this.responseCharset;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        OkHttpException exception = null;
        if (mRequest != null) {
            try {
                T result = mRequest.parseResponse(response);
                if (mCallbackOnMainThread) {
                    HttpUtil.onSuccessOnMainThread(result, this);
                } else {
                    onSuccess(result); // 同步处理回调接口，可能由实现类抛出特定类的异常
                }
            } catch (OkHttpException e) { // 异常来自于parseResponse中各种错误主动抛出
                exception = e;
//            } catch (JSONException e) {
//                exception = new OkHttpException(NetConst.JSON_PARSE_ERROR, e.getMessage());
            } catch (Exception e) {
                exception = new OkHttpException(NetConst.UNKNOWN, e.getMessage());
            }
        } else {
            exception = new OkHttpException(NetConst.JSON_PARSE_ERROR, "未找到解析类");
        }

        // 从建立连接到解析JSON，所有的错误都会引起异常，汇总在这里
        if (exception != null) {
            if (mCallbackOnMainThread) {
                HttpUtil.onFailedOnMainThread(exception, this);
            } else {
                onFailure(exception);
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e) { // 这个是IO上的失败，而非逻辑上的
        String message;
        if (e instanceof UnknownHostException
                || e instanceof ConnectException
                || e instanceof NoRouteToHostException
                || e instanceof SocketException
                || e instanceof SocketTimeoutException) {
            message = "不能访问到服务器地址，请检查网络";
        } else {
            message = e.getMessage();
        }
        OkHttpException exception = new OkHttpException(NetConst.NET_IO_ERROR, message);
        if (mCallbackOnMainThread) {
            HttpUtil.onFailedOnMainThread(exception, this);
        } else {
            onFailure(exception);
        }
    }

    // 设计的同步回调接口，根据mCallbackOnMainThread字段决定回调线程
    public abstract void onFailure(OkHttpException e);

    // 设计的同步回调接口，根据mCallbackOnMainThread字段决定回调线程
    public abstract void onSuccess(T data);
}
