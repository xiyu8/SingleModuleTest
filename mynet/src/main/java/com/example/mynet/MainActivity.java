package com.example.mynet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.EOFException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void net() {

    }

    public void onc(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    Request request = new Request.Builder().url("http://192.168.155.1/www/a.php").build();
                    RequestBody requestBody = new FormBody.Builder().add("username", "admin").add("pwd", "123456").build();
                    Request request1 = new Request.Builder().url("http://192.168.155.1/www/b.php").post(requestBody).build();
                    Response response=client.newCall(request1).execute();
                    String s = response.body().string();
                    sett(s);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sett(final String s) {
        final TextView tw = (TextView) findViewById(R.id.textView);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tw.setText(s);
            }
        });
    }










    public void b1(View view) {
                try {
//                    final Request request = new Request.Builder().url("http://192.168.155.1/www/a.php").build();
//                    RequestBody requestBody = new FormBody.Builder().add("username", "admin").add("pwd", "123456").build();
//                    Request request1 = new Request.Builder().url("http://192.168.155.1/www/b.php").post(requestBody).build();
                    Response response=HttpUtil.sendRq("http://192.168.155.1/www/a.php", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            st(response.body().string());

                        }
                    });
                    String s = response.body().string();
                    st(s);

                } catch (Exception e) {
                    e.printStackTrace();
                }

    }
    public void st(final String s) {
        final Button b=(Button)findViewById(R.id.button2);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                b.setText(s);
            }
        });
    }














//    public void b1(View view) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
////                    final Request request = new Request.Builder().url("http://192.168.155.1/www/a.php").build();
////                    RequestBody requestBody = new FormBody.Builder().add("username", "admin").add("pwd", "123456").build();
////                    Request request1 = new Request.Builder().url("http://192.168.155.1/www/b.php").post(requestBody).build();
//                    Response response=HttpUtil.sendRq("http://192.168.155.1/www/a.php", new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            st(response.body().string());
//
//                        }
//                    });
//                    String s = response.body().string();
//                    st(s);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//    public void st(final String s) {
//        final Button b=(Button)findViewById(R.id.button2);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                b.setText(s);
//            }
//        });
//    }


}





