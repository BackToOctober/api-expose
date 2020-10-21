package vn.com.vtcc.apiExpose.entity.response;

public class RestCode {
    public static int SUCCESS_CODE = 1;
    public static String SUCCESS_CODE_MSG = "success";

    public static int ERROR_CODE = 2;
    public static String ERROR_CODE_MSG = "error";

    public static int ERROR_PARAMETER_MISSING_CODE = 3;

    public static int ERROR_ELASTICSEARCH_CODE = 4;
    public static String ERROR_ELASTICSEARCH_CODE_MSG = "can't connect to elasticsearch";

    public static int ERROR_JSON_PARSING_CODE = 5;

    public static int ERROR_QUERY_NOT_VALID_CODE = 6;
    public static String ERROR_QUERY_NOT_VALID_CODE_MSG = "query is not valid";

    public static int ERROR_JOB_ID_NOT_FOUND_CODE = 7;
    public static String ERROR_JOB_ID_NOT_FOUND_CODE_MSG = "job id is not found";

    public static int ERROR_FILE_NOT_FOUND_CODE = 8;
    public static String ERROR_FILE_NOT_FOUND_CODE_MSG = "file is not found";

}
