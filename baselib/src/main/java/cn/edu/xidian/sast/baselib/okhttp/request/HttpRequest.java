package cn.edu.xidian.sast.baselib.okhttp.request;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.edu.xidian.sast.baselib.okhttp.exception.OkHttpException;
import cn.edu.xidian.sast.baselib.okhttp.utils.JsonUtils;
import cn.edu.xidian.sast.baselib.okhttp.utils.TypeUtil;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class HttpRequest<T> extends Request.Builder {

    private ConcurrentHashMap<String, String> mUrlParams = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> mFileParams = new ConcurrentHashMap<>();

    private final String mUrl;
    private Class<T> mResponseType;

    public HttpRequest(String url, Class<T> responseType) {
        mUrl = url;
        mResponseType = responseType;
    }

    final public Request build() {
        url(mUrl);
        mUrlParams.put("platform", "android");
        mUrlParams.put("time_stamp", String.valueOf(System.currentTimeMillis()));
        init(mUrlParams); // 请求头
        post(prepare(mUrlParams)); // 请求体
        return super.build();
    }

    private RequestBody prepare(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : mUrlParams.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    public abstract void init(Map<String, String> params);

    public final void add(String name, String value) {
        if (mUrlParams == null) {
            mUrlParams = new ConcurrentHashMap<>();
            init(mUrlParams);
        }
        mUrlParams.put(name, value);
    }

    public final void add(Map<String, String> params) {
        mUrlParams.putAll(params);
    }

    public boolean hasParams() {
        return (mUrlParams.size() > 0 || mFileParams.size() > 0);
    }

    // 真正的网络响应解析方法
    // 所有失败情况均主动抛出异常，交给上层HttpResponseHandler.onResponse处理
    // 上层检测到异常后，调用onFailure向主线程发消息
    public T parseResponse(Response response) throws OkHttpException {
        int parseCode = 0;
        String parseMsg = null;
        String url = response.request().url().toString();

        try {
            ResponseBody requestBody = response.body();
            if (response.isSuccessful() && requestBody != null && !TextUtils.isEmpty(requestBody.string())) {
                JSONObject jsonReturn = new JSONObject(requestBody.string());
                parseCode = jsonReturn.optInt(NetConst.RET_STATUS);
                parseMsg = jsonReturn.optString(NetConst.RET_MSG);

                if (parseCode == NetConst.SUCCESS) {
                    String parseData = jsonReturn.optString(NetConst.RET_DATA);

                    if (mResponseType == null || mResponseType == String.class) { // 不需要将JSON转为对象，直接返回即可
                        return (T) parseData;
                    }

                    if (!TypeUtil.isSubObjectOf(mResponseType, List.class)) { // 避免后端错误的返回了空数组
                        if ("[]".equals(parseData)) {
                            parseData = "{}";
                        }
                    }
                    return JsonUtils.jsonStrToBean(parseData, mResponseType);

                } else {
                    // JSON中的状态码错误，属于逻辑失败（JSON返回、解析正常）
                    // 通常返回的JSON中会包含状态码字段，用于标记本次请求的合法性，是否被正常处理
                    parseCode = NetConst.RET_STATUS_ERROR;
                }
            } else {
                parseCode = NetConst.BAD_RESPONSE_ERROR;
                parseMsg = "获取数据失败";
            }
        } catch (IOException e) {
            parseCode = NetConst.NET_IO_ERROR;
            parseMsg = "网络请求失败";
        } catch (JSONException e) {
            parseCode = NetConst.JSON_PARSE_ERROR;
            parseMsg = "数据格式错误";
        } catch (Exception e) {
            parseCode = NetConst.UNKNOWN;
            parseMsg = e.getMessage();
        }

        // 能执行到这里说明网络层一定有异常，抛异常来通知上层
        throw new OkHttpException(mUrl, parseCode, parseMsg);

    }
}
