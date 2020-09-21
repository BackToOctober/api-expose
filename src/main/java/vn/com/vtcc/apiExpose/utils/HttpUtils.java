package vn.com.vtcc.apiExpose.utils;

import org.json.JSONObject;

public class HttpUtils {

    public static JSONObject genSuccessJson(String msg) {
        JSONObject successJson = new JSONObject();
        successJson.put("url", msg);
        return successJson;
    }

    public static JSONObject genErrorJson(String msg) {
        JSONObject errJson = new JSONObject();
        errJson.put("error", msg);
        return errJson;
    }
}
