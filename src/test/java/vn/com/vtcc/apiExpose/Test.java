//package vn.com.vtcc.apiExpose;
//
//import org.apache.spark.sql.Row;
//import org.apache.spark.sql.SparkSession;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
////import vn.com.vtcc.apiExpose.app.ScheduleSchemaTableUpdateApplication;
//import vn.com.vtcc.apiExpose.utils.transform.QueryParsing;
//import vn.com.vtcc.apiExpose.utils.transform.StringOperator;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.function.Supplier;
//import java.util.stream.Collectors;
//
//
//public class Test {
//
//    public static void createData() {
//
//    }
//
//    public static void testUID() {
////        UUID uuid = UUID.randomUUID();
////        System.out.println(uuid.);
//    }
//
//    public static void check() {
//        String q = "{\n" +
//                "  \"type\": \"query_article\",\n" +
//                "  \"query\": {\n" +
//                "    \"article_type\": \"userprofile\",\n" +
//                "    \"fields\": [\n" +
//                "      \"col1\", \"col2\", \"col3\"\n" +
//                "    ],\n" +
//                "    \"filter\": {\n" +
//                "      \"name\": {\n" +
//                "        \"or\": {\n" +
//                "          \"equal\": \"111\",\n" +
//                "          \"regexp\": \"222\"\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"location\": {\n" +
//                "        \"and\": {\n" +
//                "          \"or_1\": {\n" +
//                "            \"like_1\": \"333\",\n" +
//                "            \"like_2\": \"444\"\n" +
//                "          },\n" +
//                "          \"and_1\": {\n" +
//                "            \"not_like\": \"555\"\n" +
//                "          }\n" +
//                "        }\n" +
//                "      }\n" +
//                "    },\n" +
//                "    \"limit\": \"not\",\n" +
//                "    \"fields_exclude\": [\n" +
//                "      \"col4\", \"col5\", \"col6\"\n" +
//                "    ]\n" +
//                "  }\n" +
//                "}";
//        boolean a = QueryParsing.validate(q, "config/metadata");
//        System.out.println(a);
//    }
//
//    public static void main(String[] args) throws IOException, InterruptedException {
////        System.out.println(IDGenerator.genID());
////        System.out.println(FileUtils.listFiles("config/metadata"));
////        String json = "{\"a1\": \"test\"," +
////                "\"a1\": \"test\"}";
////
////        JSONObject json1 = new JSONObject(json);
////        System.out.println(json1);
////        StringOperator operator = new StringOperator();
////        JSONObject z = null;
////        try {
////            z = new JSONObject("");
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        boolean a = operator.validateRoot(z);
////        System.out.println(a);
////        String command = "sh test.sh";
////        Runtime run  = Runtime.getRuntime();
////        Process proc = run.exec(command);
////        while (proc.isAlive()) {
////            Thread.sleep(1000);
////        }
////        proc.destroy();
////        check();
////        List<String> t = new ArrayList<>();
////        t.stream().map(r -> r).filter(a -> true).collect(Collectors.toList());
////
////        SparkSession sparkSession = SparkSession.builder().getOrCreate();
////        List<Row> list = sparkSession.sql("show tables from vtcc_dw")
////                .collectAsList()
////                .stream()
////                .filter(row -> {
////                    if (row.getString(0).startsWith("aaa")) {
////                        return true;
////                    }
////                    return false;
////                }).collect(Collectors.toList());
////
//        JSONObject t1 = new JSONObject();
//        JSONObject t2 = new JSONObject();
//        JSONArray j1 = new JSONArray();
//        JSONArray j2 = new JSONArray();
//
//        t1.put("article", "t1");
//        t1.put("metadata", j1.put(new JSONObject().put("field", "k1").put("type", "k1")));
//
//        t2.put("article", "t1");
//        t2.put("metadata", j2
//                .put(new JSONObject().put("field", "k1").put("type", "k1"))
//                .put(new JSONObject().put("field", "k2").put("type", "k2"))
//        );
//
//        boolean r = ScheduleSchemaTableUpdateApplication.compareMetadata(t1, t2);
//        System.out.println(r);
//    }
//}
