package edu.wong.util;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class ConnUtil {

    public void getStatus(String url, String json, final Application application) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), json);

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        final Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Looper.prepare();
                Toast.makeText(application.getApplicationContext(), "请检查网络设置", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) {
                getOnResponse(application, response);
            }
        };
        client.newCall(request).enqueue(callback);
    }

    public abstract void getOnResponse(Application application, Response response);
}