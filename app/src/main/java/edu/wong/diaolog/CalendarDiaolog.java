package edu.wong.diaolog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;

import edu.wong.activity.R;

public class CalendarDiaolog extends Dialog {

    private CalendarView cv;
    private Button leave_bt;
    private String data;
    private Button button;

    public CalendarDiaolog(Context context, Button button) {
        super(context);
        this.button = button;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_calendar);

        initView();

        initListener();
    }

    @Override
    protected void onStop() {
        leave_bt.setText(data);
    }

    private void initListener() {
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                data = year + "-" + (month + 1) + "-" + dayOfMonth;
                button.setText(data);
                dismiss();
            }
        });
    }


    private void initView() {
        cv = findViewById(R.id.diaolog_cv);
        leave_bt = findViewById(R.id.leave_bt);
    }
}
