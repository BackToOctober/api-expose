package vn.com.vtcc.apiExpose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages= {
        "vn.com.vtcc.apiExpose.api",
        "vn.com.vtcc.apiExpose.config",
        "vn.com.vtcc.apiExpose.entity",
        "vn.com.vtcc.apiExpose.repository",
        "vn.com.vtcc.apiExpose.app"
})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
