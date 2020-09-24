package vn.com.vtcc.apiExpose.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HdfsUtils {

    private static final Logger logger = LogManager.getLogger(HdfsUtils.class);

    public static class FileSystemBuilder {
        private String coreSitePath;
        private String hdfsSitePath;

        public FileSystemBuilder setCoreSite(String coreSitePath) {
            this.coreSitePath = coreSitePath;
            return this;
        }

        public FileSystemBuilder setHdfsSite(String hdfsSitePath) {
            this.hdfsSitePath = hdfsSitePath;
            return this;
        }

        public FileSystem init() throws IOException {
            Configuration conf = new Configuration();
            conf.addResource(new Path(this.coreSitePath));
            conf.addResource(new Path(this.hdfsSitePath));
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            return FileSystem.get(conf);
        }
    }

    public static FileSystemBuilder builder() {
        return new FileSystemBuilder();
    }

    public static boolean exists(String path, FileSystem fs){
        try {
            return fs.exists(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createFolder(FileSystem fs, String path){
        try {
            return fs.mkdirs(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean safeMove(String from, String target, FileSystem fs){
        try {
            if(fs.exists(new Path(target))){
                fs.delete(new Path(target), true);
            }
            return fs.rename(new Path(from), new Path(target));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> listFiles(String path, FileSystem fs) throws IOException {
        List<String> files = new ArrayList<>();
        FileStatus[] statusList = fs.listStatus(new Path(path));
        for (int i=0; i<statusList.length; i++) {
            files.add(statusList[i].getPath().toString());
        }
        return files;
    }
}
