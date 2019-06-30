package edu.wong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.wong.activity.R;

public class Fragment_me extends Fragment {

    private View view;
    private TextView tv_name;
    private String info;
    private JSONObject object;
    private String name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_me, null, false);
        info = getActivity().getIntent().getStringExtra("info");

        initView();


        setName();

        return view;
    }

    private void setName() {
        JSONArray array = null;
        try {
            array = new JSONArray(info);
            object = new JSONObject(array.get(0).toString());
            name = object.getString("uName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_name.setText(name);
    }

    private void initView() {
        tv_name = view.findViewById(R.id.me_tv);
    }
}
