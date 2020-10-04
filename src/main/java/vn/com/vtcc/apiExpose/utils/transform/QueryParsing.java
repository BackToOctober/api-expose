package vn.com.vtcc.apiExpose.utils.transform;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.vtcc.apiExpose.exception.QueryParsingException;
import vn.com.vtcc.apiExpose.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryParsing {

    public static Logger logger = LoggerFactory.getLogger(QueryParsing.class);

    public static String DATABASE = "vtcc_dw";

    public static StringOperator stringOperator = new StringOperator();
    public static NumberOperator numberOperator = new NumberOperator();
    public static TimeStampOperator timeStampOperator = new TimeStampOperator();
    public static BooleanOperator booleanOperator = new BooleanOperator();

    public static boolean validate(String query, String metadataFolder) {
        JSONObject jsonObject = new JSONObject(query);
        String article = jsonObject.getJSONObject("query").getString("article_type");
        try {
            JSONObject metadataJson = FileUtils.readJsonFile(Paths.get(metadataFolder, article + ".json").toString());
            Map<String, String> metadata = new HashMap<>();

            for(Object o : metadataJson.getJSONArray("metadata").toList()) {
                HashMap<String, String> mapTmp = (HashMap<String, String>) o;
                metadata.put(mapTmp.get("field"), mapTmp.get("type"));
            }

            List<String> fieldsInput= jsonObject.getJSONObject("query").getJSONArray("fields")
                    .toList().stream().map(o -> o.toString()).collect(Collectors.toList());

            if (fieldsInput.size() > 0) {
                if (!fieldsInput.get(0).equals("all")) {
                    for (String field: fieldsInput) {
                        if (!metadata.containsKey(field)) {
                            logger.warn("invalid fields");
                            return false;
                        }
                    }
                }
            }

            JSONObject filterQuery = jsonObject.getJSONObject("query").getJSONObject("filter");

            for (String field : filterQuery.keySet()) {
                try {
                    String type = metadata.get(field).trim().toLowerCase();
                    boolean check = true;
                    if (type.equals("string")) {
                        check = stringOperator.validateRoot(filterQuery.getJSONObject(field), true);
                    } else if (type.equals("int") || type.equals("long") || type.equals("double") || type.equals("float") || type.equals("bigint")) {
                        check = numberOperator.validateRoot(filterQuery.getJSONObject(field), true);
                    } else if (type.equals("timestamp")) {
                        check = timeStampOperator.validateRoot(filterQuery.getJSONObject(field), true);
                    } else if (type.equals("boolean")) {
                        check = booleanOperator.validateRoot(filterQuery.getJSONObject(field), true);
                    } else {
                        logger.warn("not support type of field");
                        return false;
                    }
                    if (!check) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String parse(String query, String metadataFolder) {
        JSONObject json = new JSONObject(query);
        String sql = "";

        // 1. select fields
        String database = DATABASE;
        String table = json.getString("article_type");
        List<String> fields = json.getJSONArray("fields").toList()
                .stream()
                .map(o -> o.toString())
                .collect(Collectors.toList());
        if (fields.get(0).trim().equals("all")) {
            sql = "select * from " + database + "." + table;
        } else {
            sql = "select " + String.join(", ", fields) + " from " + database + "." + table;
        }

        // 2. where filter
        try {
            JSONObject metadataJson = FileUtils.readJsonFile(Paths.get(metadataFolder, table + ".json").toString());
            Map<String, String> metadata = new HashMap<>();
            for(Object o : metadataJson.getJSONArray("metadata").toList()) {
                HashMap<String, String> mapTmp = (HashMap<String, String>) o;
                metadata.put(mapTmp.get("field"), mapTmp.get("type"));
            }
            JSONObject filterQuery = json.getJSONObject("filter");
            List<String> filterSql = new ArrayList<>();
            for (String field : filterQuery.keySet()) {
                String type = metadata.get(field).trim().toLowerCase();
                if (type.equals("string")) {
                    filterSql.add("(" + stringOperator.parse(field, filterQuery.getJSONObject(field), null) + ")");
                } else if (type.equals("int") || type.equals("long") || type.equals("double") || type.equals("float") || type.equals("bigint")) {
                    filterSql.add("(" + numberOperator.parse(field, filterQuery.getJSONObject(field), null) + ")");
                } else if (type.equals("boolean")) {
                    filterSql.add("(" + booleanOperator.parse(field, filterQuery.getJSONObject(field), null) + ")");
                } else if (type.equals("timestamp")) {
                    filterSql.add("(" + timeStampOperator.parse(field, filterQuery.getJSONObject(field), null) + ")");
                } else {
                    throw new QueryParsingException("not support type of " + field);
                }
            }
            if (filterSql.size() > 0) {
                sql = sql + " where " + String.join(" and ", filterSql);
            }
        } catch (IOException | QueryParsingException e) {
            e.printStackTrace();
            return null;
        }

        // 3. limit
        if (json.has("limit")) {
            String limit = json.getString("limit");
            if (!limit.equals("not")) {
                sql = sql + " limit " + Long.parseLong(limit);
            }
        }
        return sql;
    }
}
