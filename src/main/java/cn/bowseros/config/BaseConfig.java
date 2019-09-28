package cn.bowseros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * Created by bowser Jul 16, 2019/7:30:03 PM
 * 
 */
@Configuration
public class BaseConfig {

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
