package edu.wong.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.wong.activity.Login;
import edu.wong.activity.R;
import edu.wong.adapter.MyItemTouchHelperCallback;
import edu.wong.adapter.MyRecyclerViewAdapter;
import edu.wong.entity.Leave;
import edu.wong.util.ConnUtil;
import edu.wong.util.JSONUtil;
import edu.wong.util.PortUtil;
import okhttp3.Response;

public class LeaveRemarkActivity extends AppCompatActivity {

    private RecyclerView rv;
    private JSONObject object;
    private int id;
    private List<Leave> leaves = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            leaves = (List<Leave>) msg.obj;
            LinearLayoutManager manager = new LinearLayoutManager(LeaveRemarkActivity.this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getApplicationContext(), leaves);
            rv.setLayoutManager(manager);
            rv.setAdapter(adapter);
            //实现左右滑动
            Sawp(adapter);
        }
    };
    private String port;
    private PortUtil portUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_remark);

        initView();

        //展示数据
        showList();


    }

    private void Sawp(MyRecyclerViewAdapter adapter) {
        //实现监听接口
        MyItemTouchHelperCallback.OnItemSwipeListener listener = new MyItemTouchHelperCallback.OnItemSwipeListener() {

            private TextView leave_tvName;

            @Override
            public void onItemLeftSwipe(View view, int position) {
                leave_tvName = view.findViewById(R.id.leave_tvID);
                int id = Integer.parseInt(leave_tvName.getText().toString());
                String url = "http://" + port + ":3307/attend_system_api/LeaveServlet?method=updateLeave";
                Leave leave = new Leave();
                leave.setId(id);
                leave.setStatus(-1);
                toHttp2(url, leave);

            }

            @Override
            public void onItemRightSwipe(View view, int position) {
                leave_tvName = view.findViewById(R.id.leave_tvID);
                int id = Integer.parseInt(leave_tvName.getText().toString());
                String url = "http://" + port + ":3307/attend_system_api/LeaveServlet?method=updateLeave";
                Leave leave = new Leave();
                leave.setId(id);
                leave.setStatus(1);
                toHttp2(url, leave);
            }
        };

        //实例化CallBack
        MyItemTouchHelperCallback callback = new MyItemTouchHelperCallback(adapter, listener);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);

    }


    private void initView() {
        rv = findViewById(R.id.leave_rv);
    }

    private void showList() {
        //获取链接
        portUtil = new PortUtil(getApplicationContext());
        port = portUtil.getPort();
        String url = "http://" + port + ":3307/attend_system_api/LeaveServlet?method=showLeaveList";

        //获取要查询的id
        //如果是学生就查询自己的
        if (Login.uType == 1) {
            String info = getIntent().getStringExtra("info");
            try {
                JSONArray array = new JSONArray(info);
                object = new JSONObject(array.get(0).toString());
                id = object.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //生成对象
        Leave leave = new Leave();
        leave.setStudent_id(id);

        toHttp(url, leave);
    }

    private void toHttp(String url, Leave leave) {
        ConnUtil util = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONArray jsonArray = JSONUtil.getJSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Leave l = new Leave();
                    try {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        l.setId(object.getInt("id"));
                        l.setStudent_id(object.getInt("student_id"));
                        l.setStart(object.getString("start"));
                        l.setEnd(object.getString("end"));
                        l.setType(object.getString("type"));
                        l.setStatus(object.getInt("status"));
                        l.setRemark(object.getString("context"));
                        l.setPs(object.getString("ps"));
                        if (object.getInt("status") == 0) {
                            leaves.add(l);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Message message = Message.obtain();
                message.obj = leaves;
                handler.sendMessage(message);

            }
        };
        String leaveJSON = JSONUtil.getLeaveJSON(leave);
        util.getStatus(url, leaveJSON, getApplication());
    }

    private void toHttp2(String url, Leave leave) {
        ConnUtil util = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONObject json = JSONUtil.getJSON(response);
                try {
                    boolean status = json.getBoolean("status");
                    if (status) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "批复成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "批复失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String json = JSONUtil.getLeaveJSON(leave);
        util.getStatus(url, json, getApplication());
    }
}
