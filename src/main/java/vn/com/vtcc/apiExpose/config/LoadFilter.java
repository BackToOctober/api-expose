package vn.com.vtcc.apiExpose.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.com.vtcc.apiExpose.filter.CustomLogFilter;

@Configuration
public class LoadFilter {

    @Bean
    public FilterRegistrationBean<CustomLogFilter> filterRegistrationBean() {
        FilterRegistrationBean<CustomLogFilter> registrationBean = new FilterRegistrationBean<>();
        CustomLogFilter customLogFilter = new CustomLogFilter();

        registrationBean.setFilter(customLogFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
