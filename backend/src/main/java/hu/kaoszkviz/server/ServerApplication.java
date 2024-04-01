package hu.kaoszkviz.server;

import hu.kaoszkviz.server.api.tools.CustomModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
    
    @Bean
    public CustomModelMapper customModelMapper() {
      return new CustomModelMapper();
    }

}
