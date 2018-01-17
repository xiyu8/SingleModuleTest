package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.id.content;
import static android.R.id.edit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String Button1 = "";
    public String hidPdrs = "";
    public String hidsc = "";
    public String lbLanguage = "";
    public String Textbox1 = "";
    private Button btnOK;
    private ImageView imgShow;
    private Button login;
    private EditText secrectCode;
    private EditText password;
    private EditText username;
    private String failure;
    public String code;
    public String cookie;
    public String studentName;
    private String url1 = "http://jwweb.scujcc.cn/";
    private String Path = "http://jwweb.scujcc.cn/CheckCode.aspx???";
    private String url = "http://jwweb.scujcc.cn/default2.aspx";
    private static final int SUCCESS = 1;
    private static final int FALL = 2;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    public static OkHttpClient okHttpClient;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    imgShow.setImageBitmap(bitmap);
                    break;
                case FALL:
                    Toast.makeText(MainActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    LoginServer(msg.obj.toString());
                    break;
                case 5:
                    Document name = Jsoup.parse((String) msg.obj);
                    Element nameElements = name.getElementById("xhxm");
                    if (null != nameElements) {
                        String class1 = nameElements.html();
                        Toast.makeText(MainActivity.this, "欢迎您：" + class1, Toast.LENGTH_SHORT).show();
                        Pattern p = Pattern.compile("(.+)[^同学]");
                        Matcher m = p.matcher(class1);
                        if (m.find()) {
                            studentName = m.group().toString();
                        }
                        SuccessActivity.actionStart(MainActivity.this,studentName,code,cookie);
                    } else {
                        Element infoElement = name.select("script[defer]").last();
                        String Failure = infoElement.html();
                        Pattern p = Pattern.compile("([\\u4E00-\\u9FA5]+)");
                        Matcher m = p.matcher(Failure);
                        if (m.find()) {
                            failure = m.group().toString();
                            Toast.makeText(MainActivity.this, failure, Toast.LENGTH_SHORT).show();
                            getcode();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        initView();

    }

    private void initView() {
        btnOK = (Button) findViewById(R.id.btnOK);
        login = (Button) findViewById(R.id.login);
        imgShow = (ImageView) findViewById(R.id.imgShow);
        btnOK.setOnClickListener(this);
        login.setOnClickListener(this);
        secrectCode = (EditText) findViewById(R.id.secrectCode);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        okHttpClient = new OkHttpClient();
        rememberPass=(CheckBox)findViewById(R.id.remeber_pass);
        getcode();
        boolean isRemember=pref.getBoolean("remember_password",false);
        String account=pref.getString("username","");
        username.setText(account);
        if (isRemember){
            String pwd=pref.getString("password","");
            password.setText(pwd);
            rememberPass.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                getcode();
                break;
            case R.id.login:
                getState();
                editor=pref.edit();
            if (rememberPass.isChecked()) {
                editor.putString("username", username.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.putBoolean("remember_password", true);
            }else {
                editor.putString("username", username.getText().toString());
                editor.putBoolean("remember_password", false);
            }
                editor.apply();
                break;
        }
    }

    private void getState() {
        okHttpClient = new OkHttpClient();
        Request request1 = new Request.Builder().url(url1).build();
        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = FALL;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                Document parse = Jsoup.parse(resp);
                Elements select = parse.select("input[type=hidden]");
                Element element = select.get(0);
                String __VIEWSTATE = element.attr("value");
                Message msg = handler.obtainMessage();
                msg.what = 3;
                msg.obj = __VIEWSTATE;
                Log.d("Google_lenve_fb", "onResponse: __VIEWSTATE: " + __VIEWSTATE);
                handler.sendMessage(msg);
            }
        });
    }

    private void getcode() {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Path)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = FALL;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] Picture_bt = response.body().bytes();
                Message message = handler.obtainMessage();
                message.obj = Picture_bt;
                message.what = SUCCESS;
                handler.sendMessage(message);
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("info_cookies", "onResponse-size: " + cookies);
                cookie = session.substring(0, session.indexOf(";"));
                Log.i("info_s", "session is :" + cookie);
            }
        });
    }

    private void LoginServer(String __VIEWSTATE) {
        Log.i("info_Login", "知道了session:" + cookie);
        FormBody body = new FormBody.Builder()
                .add("RadioButtonList1", "学生")
                .add("txtSecretCode", secrectCode.getText().toString())
                .add("TextBox2", password.getText().toString())
                .add("txtUserName", username.getText().toString())
                .add("__VIEWSTATE", __VIEWSTATE)
                .add("Button1", Button1)
                .add("hidPdrs", hidPdrs)
                .add("hidsc", hidsc)
                .add("lbLanguage", lbLanguage)
                .add("Textbox1", Textbox1)
                .build();
        code=username.getText().toString();
        Request request = new Request.Builder()
                .addHeader("cookie", cookie)
                .url(url)
                .post(body)
                .build();
        Call call2 = okHttpClient.newCall(request);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = FALL;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String loginbody=response.body().string();
                Log.i("info_call2success",loginbody);
                Message msg = handler.obtainMessage();
                msg.what = 5;
                msg.obj=loginbody;
                handler.sendMessage(msg);
            }
        });
    }
}
