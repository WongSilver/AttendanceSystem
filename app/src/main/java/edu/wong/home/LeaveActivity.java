package edu.wong.home;

import android.app.Application;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wong.activity.R;
import edu.wong.diaolog.CalendarDiaolog;
import edu.wong.entity.Leave;
import edu.wong.util.ConnUtil;
import edu.wong.util.JSONUtil;
import edu.wong.util.PortUtil;
import edu.wong.util.TimeUtil;
import okhttp3.Response;

public class LeaveActivity extends AppCompatActivity {

    private Button leave_bt;
    private Button leave_bt2;
    private Button leave_bt3;
    private EditText leave_et;
    private Spinner leave_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        String info = getIntent().getStringExtra("info");

        initView();

        initListener();

    }


    private void initListener() {
        leave_bt.setText(TimeUtil.getTime());
        leave_bt2.setText(TimeUtil.getTime());

        leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDiaolog diaolog = new CalendarDiaolog(LeaveActivity.this, leave_bt);
                diaolog.show();

            }
        });
        leave_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CalendarDiaolog(LeaveActivity.this, leave_bt2).show();
            }
        });
        leave_bt3.setOnClickListener(new View.OnClickListener() {

            private int id;
            private JSONObject object;

            @Override
            public void onClick(View v) {
                leave_bt3.setVisibility(View.INVISIBLE);
                String info = getIntent().getStringExtra("info");
                try {
                    JSONArray array = new JSONArray(info);
                    object = new JSONObject(array.get(0).toString());
                    id = object.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String type = leave_sp.getSelectedItem().toString();
                String start = leave_bt.getText().toString();
                String end = leave_bt2.getText().toString();
                String context = leave_et.getText().toString();

                Leave leave = new Leave();
                leave.setStudent_id(id);
                leave.setStart(start);
                leave.setEnd(end);
                leave.setType(type);
                leave.setContext(context);

                PortUtil portUtil = new PortUtil(getApplicationContext());
                String port = portUtil.getPort();
                String url = "http://" + port + ":3307/attend_system_api/LeaveServlet?method=AddLeave";

                //发送数据
                toHttp(leave, url);
            }
        });
    }

    private void toHttp(Leave leave, String url) {
        ConnUtil connUtil = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONObject json = JSONUtil.getJSON(response);
        Log.d("----------", "toHttp: "+ json);
                try {
                    boolean status = json.getBoolean("status");
                    if (status) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "申请成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "申请失败，请稍后重试", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        String json = JSONUtil.getLeaveJSON(leave);
//        Log.d("----------", "toHttp: "+ json);
        connUtil.getStatus(url, json, getApplication());
    }

    private void initView() {
        leave_bt = findViewById(R.id.leave_bt);
        leave_bt2 = findViewById(R.id.leave_bt2);
        leave_bt3 = findViewById(R.id.leave_bt3);
        leave_et = findViewById(R.id.leave_et);
        leave_sp = findViewById(R.id.leave_sp);
    }
}
