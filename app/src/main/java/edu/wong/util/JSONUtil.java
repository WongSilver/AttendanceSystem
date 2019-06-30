package edu.wong.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.wong.entity.Leave;
import okhttp3.Response;

public class JSONUtil {

    private static String json;
    private static String string;

    public static String getLeaveJSON(Leave leave) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", leave.getId());
            object.put("start", leave.getStart());
            object.put("end", leave.getEnd());
            object.put("student_id", leave.getStudent_id());
            object.put("type", leave.getType());
            object.put("context", leave.getContext());
            object.put("status", leave.getStatus());
            object.put("remark", leave.getRemark());
            json = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject getJSON(Response response) {
        JSONObject object = null;
        try {
            string = response.body().string();
//            Log.d("-----json", "getJSON: " + string);
            object = new JSONObject(string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONArray getJSONArray(Response response) {
        JSONArray array = null;
        try {
            string = response.body().string();
//            Log.d("-----json", "getJSON: " + string);
            array = new JSONArray(string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }
}
