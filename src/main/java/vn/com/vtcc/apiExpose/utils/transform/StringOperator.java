package vn.com.vtcc.apiExpose.utils.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.vtcc.apiExpose.exception.QueryParsingException;

import java.util.*;

public class StringOperator extends TypeOperator {

    private static Logger logger = LoggerFactory.getLogger(StringOperator.class);
    private static List<String> strFunc = Arrays.asList("not_like", "like", "equal", "regexp", "not_regexp");

    @Override
    public boolean validate(String operator, Object value) {
        for (String func : strFunc) {
            if (operator.startsWith(func)) {
                return true;
            }
        }
        logger.warn("string type not support operator " + operator);
        return false;
    }

    @Override
    public List<String> getOperator() {
        return strFunc;
    }

    public String castToSql(String field, String operator, String value) throws QueryParsingException {
        if (value.startsWith("(lower)")) {
            field = String.format("lower(%s)", field);
            value = value.split(" ", 2)[1];
        }
        if (operator.startsWith("like")) {
            return String.format("%s like '%s'", field, value);
        } else if (operator.startsWith("equal")) {
            return String.format("%s = '%s'", field, value);
        } else if (operator.startsWith("regexp")) {
            return String.format("%s rlike '%s'", field, value);
        } else if (operator.startsWith("not_like")) {
            return String.format("%s not like '%s'", field, value);
        } else if (operator.startsWith("not_regexp")) {
            return String.format("%s not rlike '%s'", field, value);
        } else {
            throw new QueryParsingException("invalid query");
        }
    }
}
