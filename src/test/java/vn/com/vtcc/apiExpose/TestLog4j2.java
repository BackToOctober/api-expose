package vn.com.vtcc.apiExpose;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog4j2 {

    public static final Logger logger = LoggerFactory.getLogger(TestLog4j2.class);

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            logger.error("error");
            logger.info("info");
            logger.debug("debug");
            logger.warn("warn");
            Thread.sleep(1000);
        }

    }
}
