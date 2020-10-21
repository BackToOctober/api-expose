package vn.com.vtcc.apiExpose.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

//    @Bean
//    CommandLineRunner initDB(JobRequestRepository jobRequestRepository) {
//        return (args) -> {
//            System.out.println("hello");
//            jobRequestRepository.save(
//                    new JobRequest("test1", "waiting", System.currentTimeMillis(), System.currentTimeMillis()));
//        };
//    }
}
