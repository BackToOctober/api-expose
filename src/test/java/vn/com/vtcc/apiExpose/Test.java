package vn.com.vtcc.apiExpose;



import org.json.JSONException;
import org.json.JSONObject;
import vn.com.vtcc.apiExpose.utils.transform.StringOperator;

import java.io.IOException;


public class Test {

    public static void testUID() {
//        UUID uuid = UUID.randomUUID();
//        System.out.println(uuid.);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        System.out.println(IDGenerator.genID());
//        System.out.println(FileUtils.listFiles("config/metadata"));
//        String json = "{\"a1\": \"test\"," +
//                "\"a1\": \"test\"}";
//
//        JSONObject json1 = new JSONObject(json);
//        System.out.println(json1);
//        StringOperator operator = new StringOperator();
//        JSONObject z = null;
//        try {
//            z = new JSONObject("");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        boolean a = operator.validateRoot(z);
//        System.out.println(a);
        String command = "sh test.sh";
        Runtime run  = Runtime.getRuntime();
        Process proc = run.exec(command);
        while (proc.isAlive()) {
            Thread.sleep(1000);
        }
//        proc.destroy();
    }
}