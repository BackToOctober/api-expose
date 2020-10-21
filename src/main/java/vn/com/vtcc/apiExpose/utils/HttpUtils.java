package vn.com.vtcc.apiExpose.utils;

import org.json.JSONObject;
import vn.com.vtcc.apiExpose.entity.response.DataRestResponse;
import vn.com.vtcc.apiExpose.entity.dto.HtmlData;
import vn.com.vtcc.apiExpose.entity.response.RestCode;

public class HttpUtils {

    public static JSONObject genSuccessJson(String msg) {
        JSONObject successJson = new JSONObject();
        successJson.put("success", msg);
        return successJson;
    }

    public static JSONObject genErrorJson(String msg) {
        JSONObject errJson = new JSONObject();
        errJson.put("error", msg);
        return errJson;
    }

    public static DataRestResponse<String> makeSuccessResponse(String data) {
        return new DataRestResponse<>(RestCode.SUCCESS_CODE, RestCode.SUCCESS_CODE_MSG, data);
    }

    public static DataRestResponse<?> makeSuccessResponseObject(Object data) {
        return new DataRestResponse<>(RestCode.SUCCESS_CODE, RestCode.SUCCESS_CODE_MSG, data);
    }

    public static DataRestResponse<?> makeErrorResponse() {
        return new DataRestResponse<>(RestCode.ERROR_CODE, RestCode.ERROR_CODE_MSG, null);
    }
}
