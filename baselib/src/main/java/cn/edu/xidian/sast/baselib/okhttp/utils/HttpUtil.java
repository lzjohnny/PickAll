package cn.edu.xidian.sast.baselib.okhttp.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.edu.xidian.sast.baselib.okhttp.exception.OkHttpException;
import cn.edu.xidian.sast.baselib.okhttp.listener.HttpResponseHandler;
import cn.edu.xidian.sast.baselib.okhttp.listener.MainHandler;
import cn.edu.xidian.sast.baselib.okhttp.request.HttpRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by ShiningForever on 2017/12/6.
 */

public class HttpUtil {
    private static final int TIME_OUT = 10;
    private static OkHttpClient mOkHttpClient;
    private static SSLSocketFactory sslSocketFactory;
    private static final MainHandler mHandler = new MainHandler();

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient != null) {
            return mOkHttpClient;
        }

        try {
            TrustManager[] trustAllCerts = new MyX509TrustManager[]{};
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new MyHostnameVerifier());
        }

        mOkHttpClient = builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true)
                .build();
        return mOkHttpClient;
    }

    public static <T> void doPost(HttpRequest<T> request, HttpResponseHandler<T> callback) {
        if (callback != null) {
            callback.setRequest(request);
            mOkHttpClient.newCall(request.build()).enqueue(callback);
        } else {
            mOkHttpClient.newCall(request.build()).enqueue(defaultCallback);
        }
    }

    private static Callback defaultCallback = new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {}

        @Override
        public void onResponse(Call call, Response response) throws IOException {}

    };


    // HTTPS所需设置（证书）
    private static class MyX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    // HTTPS所需设置
    private static class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    public static <T> void onSuccessOnMainThread(T result, HttpResponseHandler<T> handler) {
        mHandler.notifySuccess(result, handler);
    }

    public static <T> void onFailedOnMainThread(OkHttpException exception, HttpResponseHandler<T> handler) {
        mHandler.notifyFail(exception, handler);
    }

}
