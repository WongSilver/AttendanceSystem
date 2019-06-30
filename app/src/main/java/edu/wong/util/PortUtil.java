package edu.wong.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PortUtil {

    private static SharedPreferences setting;
    private Context context;

    public PortUtil(Context context) {
        this.context = context;
        setting = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public String getPort() {
        String port = setting.getString("port", "127.0.0.1");
        return port;
    }

    public void setPort(String port) {
        context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        setting.edit().putString("port", port).apply();
    }
}
