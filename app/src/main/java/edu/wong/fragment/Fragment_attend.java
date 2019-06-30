package edu.wong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.wong.activity.Login;
import edu.wong.activity.R;

public class Fragment_attend extends Fragment {

    private View view;
    private TextView attend_tv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_attend, null, false);

        //初始化控件
        initView();

        if (Login.uType == 1) {
            attend_tv.setText("无考勤权限");
            attend_tv.setTextSize(36f);
        } else if (Login.uType == 2) {
            attend_tv.setVisibility(View.GONE);
        }

        return view;
    }

    private void initView() {
        attend_tv = view.findViewById(R.id.attend_tv);
    }
}
