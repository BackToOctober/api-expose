package vn.com.vtcc.apiExpose.utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            data = IOUtils.toString(new FileInputStream(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fis != null;
            fis.close();
        }
        if (data != null) {
            return new JSONObject(data);
        }
        return null;
    }

    public static void writeFile(String text, String filePath) throws IOException {
        File file = new File(filePath);
        org.apache.commons.io.FileUtils.writeStringToFile(file, text, StandardCharsets.UTF_8);
    }

    public static List<String> listFiles(String path) {
        File f = new File(path);
        List<File> list = Arrays.asList(f.listFiles());
        List<String> listFileName = list.stream().map(File::toString).collect(Collectors.toList());
        return listFileName;
    }

    public static boolean exist(String filePath) {
        return new File(filePath).exists();
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public static boolean createFolderIfNotExists(String path) {
        try {
            Files.createDirectories(Paths.get(path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(FileUtils.listFiles("query"));
    }

}
