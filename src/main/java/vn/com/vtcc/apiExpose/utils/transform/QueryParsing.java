package vn.com.vtcc.apiExpose.utils.transform;

import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.vtcc.apiExpose.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class QueryParsing {

    public static Logger logger = LoggerFactory.getLogger(QueryParsing.class);

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
                System.out.println("----");
                System.out.println(mapTmp);
                metadata.put(mapTmp.get("field"), mapTmp.get("type"));
            }
            JSONObject filterQuery = jsonObject.getJSONObject("query").getJSONObject("filter");

            System.out.println("----------------------------");
            System.out.println(metadata);

            for (String field : filterQuery.keySet()) {
                try {
                    String type = metadata.get(field).trim().toLowerCase();
                    boolean check = true;
                    if (type.equals("string")) {
                        check = stringOperator.validate(filterQuery.getJSONObject(field));
                    } else if (type.equals("int") || type.equals("long") || type.equals("double") || type.equals("float")) {
                        check = numberOperator.validate(filterQuery.getJSONObject(field));
                    } else if (type.equals("timestamp")) {
                        check = timeStampOperator.validate(filterQuery.getJSONObject(field));
                    } else if (type.equals("boolean")) {
                        check = booleanOperator.validate(filterQuery.getJSONObject(field));
                    } else {
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

    public static String parse() {
        return "";
    }
}
