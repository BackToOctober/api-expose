package vn.com.vtcc.apiExpose.utils.transform;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.vtcc.apiExpose.exception.QueryParsingException;

import java.util.Arrays;
import java.util.List;

public class NumberOperator extends TypeOperator{

    private static Logger logger = LoggerFactory.getLogger(NumberOperator.class);
    private static List<String> numberFunc = Arrays.asList("gt", "eq", "lt", "ge", "le");

    @Override
    public boolean validate(String operator, Object value) {
        if (!NumberUtils.isDigits(value.toString())) {
            logger.warn("value " + value.toString() + " is not a number");
            return false;
        }

        for (String func : numberFunc) {
            if (operator.startsWith(func)) {
                return true;
            }
        }
        logger.warn("number type not support operator " + operator);
        return false;
    }

    @Override
    public List<String> getOperator() {
        return numberFunc;
    }

    @Override
    public String castToSql(String field, String operator, String value) throws QueryParsingException {
        if (operator.startsWith("gt")) {
            return String.format("%s > '%s'", field, value);
        } else if (operator.startsWith("eq")) {
            return String.format("%s = '%s'", field, value);
        } else if (operator.startsWith("lt")) {
            return String.format("%s < '%s'", field, value);
        } else if (operator.startsWith("ge")) {
            return String.format("%s >= '%s'", field, value);
        } else if (operator.startsWith("le")) {
            return String.format("%s <= '%s'", field, value);
        } else {
            throw new QueryParsingException("invalid query");
        }
    }
}
