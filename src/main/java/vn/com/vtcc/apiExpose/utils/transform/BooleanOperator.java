package vn.com.vtcc.apiExpose.utils.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.vtcc.apiExpose.exception.QueryParsingException;

import java.util.Arrays;
import java.util.List;

public class BooleanOperator extends TypeOperator{

    private static Logger logger = LoggerFactory.getLogger(BooleanOperator.class);
    private static List<String> boolFunc = Arrays.asList("is");

    @Override
    public boolean validate(String operator, Object value) {
        if (!value.toString().trim().toLowerCase().equals("true") && !value.toString().trim().toLowerCase().equals("false")) {
            System.out.println(value.toString().trim().toLowerCase());
            logger.warn("type of value " + value.toString() + " is not boolean type");
            return false;
        }
        for (String func : boolFunc) {
            if (operator.startsWith(func)) {
                return true;
            }
        }
        logger.warn("boolean type not support operator " + operator);
        return false;
    }

    @Override
    public List<String> getOperator() {
        return boolFunc;
    }

    @Override
    public String castToSql(String field, String operator, String value) throws QueryParsingException {
        if (operator.startsWith("is")) {
            return String.format("%s = '%s'", field, value);
        } else {
            throw new QueryParsingException("invalid query");
        }
    }
}
