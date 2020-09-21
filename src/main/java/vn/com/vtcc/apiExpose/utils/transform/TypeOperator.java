package vn.com.vtcc.apiExpose.utils.transform;

import org.json.JSONObject;

import java.util.Set;

public abstract class TypeOperator {

    public abstract boolean validate(JSONObject json);

    public boolean validateRoot(JSONObject json) {
        Set<String> keySet = json.keySet();
        if (keySet.contains("and") && keySet.contains("or")) {
            return false;
        } else {
            for (String key: keySet) {
                boolean r = validate(json.getJSONObject(key));
                if (!r) {
                    return false;
                }
            }
        }
        return true;
    }

}