package vn.com.vtcc.apiExpose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import vn.com.vtcc.apiExpose.storage.elasticsearch.ESConnectorFactory;
import vn.com.vtcc.apiExpose.utils.FileUtils;

import java.io.IOException;
import java.util.Properties;


@SpringBootApplication(
//        exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class}
        )
@ComponentScan(basePackages= {
        "vn.com.vtcc.apiExpose.controller",
        "vn.com.vtcc.apiExpose.config",
        "vn.com.vtcc.apiExpose.entity",
        "vn.com.vtcc.apiExpose.repository",
        "vn.com.vtcc.apiExpose.app"
})
public class ServerApplication {

    @Bean
    public ESConnectorFactory esConnectorFactory() throws IOException {
        Properties prop = FileUtils.readPropertiesFile("config/elasticsearch.properties");
        return new ESConnectorFactory(prop);
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
