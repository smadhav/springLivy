package saju.livy.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@ComponentScan("saju.livy.spring.*")
@PropertySource("classpath:application.properties")
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
