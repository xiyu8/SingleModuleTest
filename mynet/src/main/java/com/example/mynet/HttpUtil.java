package com.example.mynet;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 晞余 on 2017/2/22.
 */

public class HttpUtil {
    public static Response sendRq(String s, Callback callback) {
        try {
            OkHttpClient client=new OkHttpClient();
            Request request = new Request.Builder().url("http://192.168.155.1/www/a.php").build();
            RequestBody requestBody = new FormBody.Builder().add("username", "admin").add("pwd", "123456").build();
            Request request1 = new Request.Builder().url("http://192.168.155.1/www/b.php").post(requestBody).build();
          //  Response response=client.newCall(request1).execute();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {

        }
        return null;
    }}
