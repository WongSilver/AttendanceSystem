package edu.wong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wong.activity.Login;
import edu.wong.activity.R;
import edu.wong.home.ClassActivity;
import edu.wong.home.LeaveActivity;
import edu.wong.home.LeaveLogActivity;
import edu.wong.home.LeaveRemarkActivity;

public class Fragment_home extends Fragment implements AdapterView.OnItemClickListener {

    private View view;
    private GridView gv;
    private List<Map<String, Object>> list;
    private int[] imgs = new int[]{};
    private String[] text = new String[]{};
    private Intent intent;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home, null, false);

        //初始化控件
        initView();

        //初始化数据
        initData();

        //初始化Grid
        initGrid();

        return view;
    }

    private void initData() {
        list = new ArrayList<>();
        if (Login.uType == 1) {
            imgs = new int[]{R.drawable.as01, R.drawable.as02,R.drawable.as04, R.drawable.as05, R.drawable.as06};
            text = new String[]{"日程", "请假","记录", "通知", "排行榜"};

        } else if (Login.uType == 2) {
            imgs = new int[]{R.drawable.as01, R.drawable.as03, R.drawable.as04, R.drawable.as05, R.drawable.as06, R.drawable.as07, R.drawable.as08};
            text = new String[]{"日程", "批假", "记录", "通知", "排行榜", "学生管理", "发布公告"};

        } else if (Login.uType == 3) {
            imgs = new int[]{R.drawable.as01, R.drawable.as03, R.drawable.as04, R.drawable.as05, R.drawable.as06, R.drawable.as07, R.drawable.as08, R.drawable.as09, R.drawable.as10};
            text = new String[]{"日程", "批假", "记录", "通知", "排行榜", "学生管理", "发布公告", "班级管理", "教师管理"};
        }
        for (int i = 0; i < imgs.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", imgs[i]);
            map.put("text", text[i]);
            list.add(map);
        }
    }

    private void initGrid() {
        String[] from = {"img", "text"};
        int[] to = {R.id.gv_img, R.id.tv_text};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.home_gird, from, to);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
    }

    private void initView() {
        gv = view.findViewById(R.id.home_gv);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (imgs[position]) {
            case R.drawable.as01:
                Log.d("------------------", "onItemClick: " + 1);
                break;
            case R.drawable.as02:
                intent = new Intent(getContext(), LeaveActivity.class);
                intent.putExtra("info", getActivity().getIntent().getStringExtra("info"));
                break;
            case R.drawable.as03:
                intent = new Intent(getContext(), LeaveRemarkActivity.class);
                intent.putExtra("info", getActivity().getIntent().getStringExtra("info"));
                break;
            case R.drawable.as04:
                intent = new Intent(getContext(), LeaveLogActivity.class);
                intent.putExtra("info", getActivity().getIntent().getStringExtra("info"));
                break;
            case R.drawable.as05:
                Log.d("------------------", "onItemClick: " + 5);
                break;
            case R.drawable.as06:
                Log.d("------------------", "onItemClick: " + 6);
                break;
            case R.drawable.as07:
                Log.d("------------------", "onItemClick: " + 7);
                break;
            case R.drawable.as08:
                Log.d("------------------", "onItemClick: " + 8);
                break;
            case R.drawable.as09:
                intent = new Intent(getContext(), ClassActivity.class);
                break;
            case R.drawable.as10:
                Log.d("------------------", "onItemClick: " + 10);
                break;
        }
        if (intent != null) {
            getActivity().startActivity(intent);
        }
        intent = null;
    }
}
