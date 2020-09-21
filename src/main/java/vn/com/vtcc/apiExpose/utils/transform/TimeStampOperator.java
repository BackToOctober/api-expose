package vn.com.vtcc.apiExpose.utils.transform;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TimeStampOperator extends TypeOperator {

    public static List<String> timeFunc = Arrays.asList("start", "end");

    @Override
    public boolean validate(JSONObject json) {
        Set<String> keySet = json.keySet();
        for (String key: keySet) {
            if (key.startsWith("and") || key.startsWith("or")) {
                boolean r = validate(json.getJSONObject(key));
                if (!r) {
                    return false;
                }
            } else {
                if (!timeFunc.contains(key)) {
                    return false;
                }
            }
        }
        return true;
    }
}
