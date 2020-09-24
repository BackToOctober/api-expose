package vn.com.vtcc.apiExpose.utils.transform;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.vtcc.apiExpose.exception.QueryParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class TypeOperator {

    private static Logger logger = LoggerFactory.getLogger(TypeOperator.class);

    public String parse(String field, JSONObject json, String parent) throws QueryParsingException {
        List<String> keys = new ArrayList<>(json.toMap()
                .keySet());
        if (keys.size() > 1) {
            if (parent == null && !parent.startsWith("and") && !parent.startsWith("or")) {
                throw new QueryParsingException("invalid query");
            }
            List<String> sub = new ArrayList<>();
            for (String k : keys) {
                if (json.get(k) instanceof String) {
                    sub.add("(" + castToSql(field, k, json.getString(k)) + ")");
                } else {
                    sub.add("(" + parse(field, json.getJSONObject(k), k) + ")");
                }
            }

            if (parent.startsWith("and")) {
                return String.join(" and ", sub);
            } else if (parent.startsWith("or")) {
                return String.join(" or ", sub);
            } else {
                throw new QueryParsingException("invalid query");
            }
        } else if (keys.size() == 1) {
            String operator = keys.get(0);
            if (operator.startsWith("or") || operator.startsWith("and")) {
                return parse(field, json.getJSONObject(operator), operator);
            } else {
                String value = json.getString(keys.get(0));
                return castToSql(field, operator, value);
            }
        }
        return "";
    }

    public abstract List<String> getOperator();

    public abstract String castToSql(String field, String operator, String value) throws QueryParsingException;

    public boolean validateRoot(JSONObject json, boolean root) {
        Set<String> keySet = json.keySet();
        if (root) {
            if (keySet.size() > 1) {
                logger.warn("root size is less or equal 1");
                logger.warn("--> error: " + json);
                return false;
            }
        }
        for (String key: keySet) {
            if (key.startsWith("and") || key.startsWith("or")) {
                boolean r = validateRoot(json.getJSONObject(key), false);
                if (!r) {
                    return false;
                }
            } else {
                if (!validate(key, json.get(key))) {
                    return false;
                }
            }
        }
        return true;
    }

    public abstract boolean validate(String operator, Object value);

}