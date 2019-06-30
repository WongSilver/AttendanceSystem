package edu.wong.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wong.activity.Login;
import edu.wong.activity.R;
import edu.wong.entity.Leave;
import edu.wong.util.ConnUtil;
import edu.wong.util.JSONUtil;
import edu.wong.util.PortUtil;
import okhttp3.Response;

public class LeaveLogActivity extends AppCompatActivity {

    private ListView lv;
    private JSONObject object;
    private int id;
    private List<Map<String, Object>> list;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            list = (List<Map<String, Object>>) msg.obj;
            //填充数据
            setData(list);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_log);

        initView();

        showList();
    }

    private void showList() {
        //获取链接
        PortUtil portUtil = new PortUtil(getApplicationContext());
        String port = portUtil.getPort();
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

    private void toHttp(String url, final Leave leave) {
        ConnUtil connUtil = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONArray json = JSONUtil.getJSONArray(response);
                Log.d("---------name", "getOnResponse: "+ json);
                list = new ArrayList<>();
                for (int i = 0; i < json.length(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    try {
                        JSONObject o = (JSONObject) json.get(i);
                        Iterator<String> keys = o.keys();
                        while (keys.hasNext()) {
                            String k = keys.next();
                            if (k.equals("status")) {
                                if ((int)o.get(k) == 0) {
                                    map.put(k, "未审核");
                                } else if ((int)o.get(k) == 1) {
                                    map.put(k, "已批准");
                                } else if ((int)o.get(k) == -1) {
                                    map.put(k, "未批准");
                                }
                                continue;
                            }
                            map.put(k, o.get(k));
                        }
                        list.add(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Message message = Message.obtain();
                message.obj = list;
                handler.sendMessage(message);
            }
        };
        String leaveJSON = JSONUtil.getLeaveJSON(leave);
        connUtil.getStatus(url, leaveJSON, getApplication());
    }

    private void setData(List<Map<String, Object>> list) {
        String[] from = {"ps", "start", "end", "type", "status", "remark"};
        int[] to = {R.id.leave_tvName, R.id.leave_tvStart, R.id.leave_tvEnd, R.id.leave_tvType, R.id.leave_tvStatus, R.id.leave_tvRemark};
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.home_list, from, to);
        lv.setAdapter(adapter);
    }

    private void initView() {
        lv = findViewById(R.id.leavelog_lv);
    }
}
