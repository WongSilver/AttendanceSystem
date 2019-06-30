package edu.wong.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import edu.wong.diaolog.PortDiaolog;
import edu.wong.util.ConnUtil;
import edu.wong.util.PortUtil;
import okhttp3.Response;



/**
 *
 * ip地址要在登陆页面的网络设置，设置一下。数据库端口号需要自己改
 */
public class Login extends AppCompatActivity {
    public static int uType = 0;
    private TextView tv_name;
    private TextView tv_pwd;
    private Button bt_login;
    private Button bt_forget;
    private String pwd;
    private String name;
    private String url;
    private RadioGroup rg_type;
    private int id;
    private Button bt_port;
    private String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件
        initView();

        initListener();

        //判断用户是否存在
        isExists();


    }

    private void initListener() {
        bt_port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PortDiaolog(Login.this).show();
            }
        });
    }

    private void isExists() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户填写的数据
                getData();

                //判断用户类型
                if (id == -1) {
                    Toast.makeText(getApplicationContext(), "请选择用户类型", Toast.LENGTH_LONG).show();
                } else {
                    String json = "{\"name\":\"" + name + "\",\"password\":\"" + pwd + "\"}";
                    PortUtil portUtil = new PortUtil(getApplication());
                    port = portUtil.getPort();
                    //如果IP没有填写 默认写本地
                    if (port == null || port.equals("")) {
                        port = "127.0.0.1";
                    }
                    switch (id) {
                        case R.id.rb_uStudent:
                            url = "http://" + port + ":3307/attend_system_api/StudentServlet?method=login";
                            uType = 1;
                            break;
                        case R.id.rb_uTeacher:
                            url = "http://" + port + ":3307/attend_system_api/TeacherServlet?method=login";
                            uType = 2;
                            break;
                        case R.id.rb_uAdmin:
                            url = "http://" + port + ":3307/attend_system_api/AdminServlet?method=login";
                            uType = 3;
                            break;
                    }

                    //调用连接方法并且传入参数
                    ConnUtil util = new ConnUtil() {
                        @Override
                        public void getOnResponse(Application application, Response response) {
                            String json = null;
                            try {
                                json = response.body().string();
                                JSONObject object = new JSONObject(json);
                                String status = (String) object.get("status");
                                if ("s".equals(status)) {
                                    Intent intent = new Intent(Login.this, HomeActivity.class);
                                    intent.putExtra("info", object.getString("info"));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(application.getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    util.getStatus(url, json, getApplication());
                }
            }
        });
    }

    private void getData() {
        name = tv_name.getText().toString().trim();
        pwd = tv_pwd.getText().toString().trim();
        id = rg_type.getCheckedRadioButtonId();
    }

    private void initView() {
        tv_name = findViewById(R.id.tv_uName);
        tv_pwd = findViewById(R.id.tv_uPwd);
        bt_login = findViewById(R.id.bt_uLogin);
        bt_forget = findViewById(R.id.bt_uForget);
        rg_type = findViewById(R.id.rg_uType);
        bt_port = findViewById(R.id.bt_uPort);
    }

}
