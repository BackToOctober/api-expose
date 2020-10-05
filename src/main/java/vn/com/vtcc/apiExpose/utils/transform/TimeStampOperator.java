package vn.com.vtcc.apiExpose.utils.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.vtcc.apiExpose.exception.QueryParsingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class TimeStampOperator extends TypeOperator {

    private static final Logger logger = LoggerFactory.getLogger(TimeStampOperator.class);
    private static final List<String> timeFunc = Arrays.asList("equal", "start", "end");

    @Override
    public boolean validate(String operator, Object value) {
        if (!isTimeStampValid(value.toString())) {
            logger.warn("time format of value " + value.toString() + " is not right");
            return false;
        }

        for (String func : timeFunc) {
            if (operator.startsWith(func)) {
                return true;
            }
        }
        logger.warn("timestamp type not support operator " + operator);
        return false;
    }

    @Override
    public List<String> getOperator() {
        return timeFunc;
    }

    @Override
    public String castToSql(String field, String operator, String value) throws QueryParsingException {
        if (operator.startsWith("start")) {
            return String.format("%s >= to_timestamp('%s','yyyy-MM-dd HH:mm:ss')", field, value);
        } else if (operator.startsWith("end")) {
            return String.format("%s < to_timestamp('%s','yyyy-MM-dd HH:mm:ss')", field, value);
        } else if (operator.startsWith("equal")) {
            return String.format("%s = to_timestamp('%s','yyyy-MM-dd HH:mm:ss')", field, value);
        } else {
            throw new QueryParsingException("invalid query");
        }
    }

    public static boolean isTimeStampValid(String inputString) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            format.parse(inputString);
            return true;
        }
        catch(ParseException e) {
            return false;
        }
    }
}
