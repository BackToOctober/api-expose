package vn.com.vtcc.apiExpose.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class FileUtils {
    public static Properties readPropertiesFile(String filePath) throws IOException {
        Properties props = new Properties();
        props.load(new FileReader(filePath));
        return props;
    }

    public static JSONObject readJsonFile(String filePath) throws IOException {
        FileInputStream fis = null;
        String data = null;
        try {
            fis = new FileInputStream(filePath);
            data = IOUtils.toString(new FileInputStream(filePath), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        if (data != null) {
            return new JSONObject(data);
        }
        return null;
    }

    public static List<String> listFiles(String path) {
        File f = new File(path);
        List<File> list = Arrays.asList(f.listFiles());
        List<String> listFileName = list.stream().map(file -> {
            return file.toString();
        }).collect(Collectors.toList());
        return listFileName;
    }

}
