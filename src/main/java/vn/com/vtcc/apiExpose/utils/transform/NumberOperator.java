package vn.com.vtcc.apiExpose.utils.transform;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NumberOperator extends TypeOperator{

    public static List<String> numberFunc = Arrays.asList("gt", "eq", "lt", "ge", "le");

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
                try {
                    if (!numberFunc.contains(key) || !NumberUtils.isDigits(json.get(key).toString())) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }
}
