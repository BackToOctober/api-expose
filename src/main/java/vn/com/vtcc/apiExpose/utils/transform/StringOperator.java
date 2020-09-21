package vn.com.vtcc.apiExpose.utils.transform;

import org.json.JSONObject;

import java.util.*;

public class StringOperator extends TypeOperator {

    public static List<String> strFunc = Arrays.asList("lower", "like", "equal", "contain", "regexp");

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
                if (!strFunc.contains(key)) {
                    return false;
                }
            }
        }
        return true;
    }
}
