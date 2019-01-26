package com.gr03.amos.bikerapp.NetworkLayer;

import android.os.AsyncTask;

import com.gr03.amos.bikerapp.Requests;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpGetTask extends AsyncTask<String, Void, JSONObject> {

    private static String HOST = "10.0.2.2";
    private static String PORT = "8080";
    static MediaType jsonMedia = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    public HttpGetTask() {
        super();
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return onOnIntercept(chain);
                    }
                }).build();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String tail = params[0];

        Request request = new Request.Builder()
                .url(Requests.getUrl(tail))
                .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            String jsonData = response.body().string();
            return new JSONObject(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            String content = response.body().string();
//            Log.d(TAG, lastCalledMethodName + " - " + content);
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
        } catch (SocketTimeoutException exception) {
            exception.printStackTrace();
            System.out.println("-----------SocketTimeoutException----------------");
        }

        return chain.proceed(chain.request());
    }

}
