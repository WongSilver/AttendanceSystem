package edu.wong.diaolog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.wong.activity.R;
import edu.wong.util.PortUtil;

public class PortDiaolog extends Dialog {

    private Button bt_com;
    private Button bt_qu;
    private EditText t_ip;
    private EditText t_port;

    public PortDiaolog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_port);

        initView();

        initListener();
    }

    private void initListener() {
        bt_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortUtil util = new PortUtil(getContext());
                util.setPort(t_ip.getText().toString());
                dismiss();
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
        bt_qu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void initView() {
        bt_com = findViewById(R.id.port_commit);
        bt_qu = findViewById(R.id.port_qu);
        t_ip = findViewById(R.id.ip);
        t_port = findViewById(R.id.port);

        PortUtil util = new PortUtil(getContext());
        String port = util.getPort();
        t_ip.setText(port);
    }
}
